package com.lsc.ctesterfx.tests;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private static TestLoader mTestLoader;
    
    private TestLoader() {}
    
    public static synchronized TestLoader newInstance()
    {
        if (mTestLoader == null)
        {
            return new TestLoader();
        }
        
        return mTestLoader;
    }
    
    public void compile(Path dest, File... tests) throws Exception
    {
        System.out.println("Dest = " + dest.toString());
        
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
    
    public void load(File classFile) throws Exception
    {
        System.out.println("Loading class: " + classFile.getName() + ".class");

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
        method.invoke(obj);
    }
}
