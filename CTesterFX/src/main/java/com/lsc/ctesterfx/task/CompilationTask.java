package com.lsc.ctesterfx.task;

import com.lsc.ctesterfx.dao.Test;
import com.lsc.ctesterfx.test.TestLoader;
import java.lang.reflect.Method;
import javafx.concurrent.Task;
import javafx.util.Pair;

/**
 * Class to run the compilation process in the background.
 *
 * @author dma@logossmartcard.com
 */
public class CompilationTask extends Task
{
    private final Test mTest;

    public CompilationTask(Test test)
    {
        mTest = test;
    }

    @Override
    protected Object call() throws Exception
    {
        return compileTest();
    }

    /**
     * Method that compiles and loads the test.
     *
     * @return Pair containing the object and the method to be invoked.
     */
    private Pair<Object, Method> compileTest()
    {
        Pair<Object, Method> result = null;
        TestLoader testLoader = TestLoader.newInstance();

        try
        {
            // Compile and load the test class.
            if (testLoader.compile(mTest))
            {
                result = testLoader.load(mTest);
            }

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        return result;
    }
}
