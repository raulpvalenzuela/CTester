package com.lsc.ctesterfx.test;

import com.lsc.ctesterfx.dao.Test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
import org.apache.log4j.Logger;

/**
 * Class that manages the compilation and loading of the test files.
 *
 * @author dma@logossmartcard.com
 */
public class TestLoader extends ClassLoader
{
    private static final Logger LOGGER = Logger.getLogger(TestLoader.class);

    private static final String SETUP_METHOD    = "setUp";
    private static final String RUN_METHOD      = "run";
    private static final String TEARDOWN_METHOD = "tearDown";

    // Single instance of a TestLoader.
    private static TestLoader testLoader;

    private TestLoader() {}

    public static synchronized TestLoader newInstance()
    {
        if (testLoader == null)
        {
            testLoader = new TestLoader();
        }

        return testLoader;
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

            LOGGER.debug("Destination folder = " + dest.toString());
            LOGGER.debug("Classpath = " + System.getProperty("java.class.path"));

            JavaCompiler.CompilationTask task =
                compiler.getTask(null, fm, null, options, null, fileObjects);

            return task.call();
        }
    }

    /**
     * Dynamically loads the .class file previously generated.
     *
     * @param test: test to be loaded.
     * @return Pair containing the object and the
     *         methods 'setup', 'run' and 'teardown'. Null if there's been an exception.
     */
    public Pair<Object, List<Method>> load(final Test test)
    {
        String pkg = null;
        try
        {
            LOGGER.debug("Reading package name");

            // Read the package name
            BufferedReader br = new BufferedReader(new FileReader(test.getFile()));
            while (!(pkg = br.readLine()).contains("package")) {}

            // Remove all the nonsense.
            pkg = pkg.replace("package", "").replace(";", ".").replace(" ", "").trim();

            LOGGER.debug("Package found = " + pkg);

        } catch (FileNotFoundException ex) {
            LOGGER.error("Java file not found");
            LOGGER.error(ex);

            return null;

        } catch (IOException ex) {
            LOGGER.error("Exception reading the java file");
            LOGGER.error(ex);

            return null;
        }

        try
        {
            LOGGER.debug("Loading and instantiating class");

            // create FileInputStream object
            File file = new File(test.getPath());

            // Convert File to a URL
            URL url = file.toURI().toURL();
            URL[] urls = new URL[]{ url };

            // Create a new class loader with the directory
            ClassLoader cl = new URLClassLoader(urls);

            // Load in the class; Test.class. Should be located in path + \package\
            Class cls  = cl.loadClass(pkg + test.getName());
            LOGGER.debug("Class loaded");
            Object obj = cls.newInstance();
            LOGGER.debug("New instance created");

            Method setupMethod    = cls.getDeclaredMethod(SETUP_METHOD);
            LOGGER.debug("'" + SETUP_METHOD + "' method found");
            Method runMethod      = cls.getDeclaredMethod(RUN_METHOD);
            LOGGER.debug("'" + RUN_METHOD + "' method found");
            Method teardownMethod = cls.getDeclaredMethod(TEARDOWN_METHOD);
            LOGGER.debug("'" + TEARDOWN_METHOD + "' method found");

            return new Pair<>(obj, Arrays.asList(new Method[] { setupMethod, runMethod, teardownMethod }));

        } catch (MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException ex) {
            LOGGER.error("Exception loading the class");
            LOGGER.error(ex);

            return null;
        }
    }
}
