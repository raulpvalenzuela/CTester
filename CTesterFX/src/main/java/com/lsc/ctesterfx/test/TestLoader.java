package com.lsc.ctesterfx.test;

import com.lsc.ctesterfx.dao.Test;
import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.util.Pair;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * Class that manages the compilation and loading of the test files.
 *
 * @author dma@logossmartcard.com
 */
public class TestLoader extends ClassLoader
{
    private static final String PACKAGE         = "runnables.";
    private static final String SETUP_METHOD    = "setUp";
    private static final String RUN_METHOD      = "run";
    private static final String TEARDOWN_METHOD = "tearDown";

    // Single instance of a TestLoader.
    private static TestLoader mTestLoader;

    private TestLoader() {}

    public static synchronized TestLoader newInstance()
    {
        if (mTestLoader == null)
        {
            mTestLoader = new TestLoader();
        }

        return mTestLoader;
    }

    /**
     * Compiles the tests given. It automatically creates the correct folder structure
     * and sets the classpath.
     *
     * @param test: test file to be compiled.
     * @return true if the compilation is succesful, false otherwise.
     * @throws Exception
     *
     *    IllegalArgumentException if the list of files includes
     *        a directory.
     */
    public boolean compile(final Test test) throws Exception
    {
        Path dest = Paths.get(test.getPath());
        File[] tests = new File[]{ test.getFile() };

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        try (StandardJavaFileManager fm = compiler.getStandardFileManager(null, null, null))
        {
            Iterable<? extends JavaFileObject> fileObjects = fm.getJavaFileObjectsFromFiles(Arrays.asList(tests));

            List<String> options = new ArrayList<>();

            // Create the folder structure.
            options.add("-d");
            options.add(dest.toString());

            // Sets the classpath.
            options.add("-cp");
            options.add(System.getProperty("java.class.path"));

            JavaCompiler.CompilationTask task =
                compiler.getTask(null, fm, null, options, null, fileObjects);

            return task.call();
        }
    }

    /**
     * Dynamically loads the .class file previously generated.
     *
     * @param test: test to be loaded.
     * @return Pair containing the object and the methods 'setup', 'run' and 'teardown'. Null if there's been an exception.
     */
    public Pair<Object, List<Method>> load(final Test test)
    {
        try
        {
            // create FileInputStream object
            File file = new File(test.getPath());

            // Convert File to a URL
            URL url = file.toURI().toURL();
            URL[] urls = new URL[]{ url };

            // Create a new class loader with the directory
            ClassLoader cl = new URLClassLoader(urls);

            // Load in the class; Test.class. Should be located in path + \runnables\
            Class cls  = cl.loadClass(PACKAGE + test.getName());
            Object obj = cls.newInstance();

            Method setupMethod    = cls.getDeclaredMethod(SETUP_METHOD);
            Method runMethod      = cls.getDeclaredMethod(RUN_METHOD);
            Method teardownMethod = cls.getDeclaredMethod(TEARDOWN_METHOD);

            return new Pair<>(obj, Arrays.asList(new Method[] { setupMethod, runMethod, teardownMethod }));

        } catch (MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException ex) {
            return null;
        }
    }
}
