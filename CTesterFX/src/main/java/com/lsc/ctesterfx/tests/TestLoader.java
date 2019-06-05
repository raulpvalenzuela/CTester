package com.lsc.ctesterfx.tests;

import com.lsc.ctesterfx.printer.Printer;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
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
    // Single instance of a TestLoader.
    private static TestLoader mTestLoader;
    private static Printer mPrinter;

    private TestLoader()
    {
        // Get a printer instance to log stuff.
        mPrinter = Printer.newInstance();
    }

    public static synchronized TestLoader newInstance()
    {
        if (mTestLoader == null)
        {
            mTestLoader = new TestLoader();
        }

        return mTestLoader;
    }

    /**
     * Method that compiles the tests given. It automatically creates the correct folder structure
     * and sets the classpath.
     *
     * @param dest: destination folder to place the resulting .class file.
     * @param tests: list of tests to be compiled.
     * @throws Exception
     */
    public void compile(Path dest, File... tests) throws Exception
    {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        try (StandardJavaFileManager fm = compiler.getStandardFileManager(null, null, null))
        {
            Iterable<? extends JavaFileObject> fileObjects = fm.getJavaFileObjectsFromFiles(Arrays.asList(tests));

            List<String> options = new ArrayList<>();
            options.add("-d");
            options.add(dest.toString());

            options.add("-cp");
            options.add(System.getProperty("java.class.path"));

            JavaCompiler.CompilationTask task =
                compiler.getTask(null, fm, null, options, null, fileObjects);

            task.call();
        }
    }

    /**
     * Method that dynamically loads the .class file previously generated.
     *
     * @param classFile: .class file to be loaded.
     * @return Pair containing the object and the method 'run'.
     * @throws Exception
     */
    public Pair<Object, Method> load(File classFile) throws Exception
    {
        // create FileInputStream object
        File file = new File(classFile.getParent());

        // Convert File to a URL
        URL url = file.toURI().toURL();
        URL[] urls = new URL[]{url};

        // Create a new class loader with the directory
        ClassLoader cl = new URLClassLoader(urls);

        // Load in the class; MyClass.class should be located in
        // the directory file:/c:/myclasses/com/mycompany
        Class cls = cl.loadClass("testing.Test");
        Object obj = cls.newInstance();
        Method method = cls.getDeclaredMethod("run");

        return new Pair<>(obj, method);
    }
}
