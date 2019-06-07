package com.lsc.ctesterfx.task;

import com.lsc.ctesterfx.dao.Test;
import com.lsc.ctesterfx.interfaces.AbstractLogger;
import com.lsc.ctesterfx.logger.Logger;
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
    // Instance of the logger.
    private final AbstractLogger mLogger;
    // Instance to the test to be compiled.
    private final Test mTest;

    public CompilationTask(Test test)
    {
        mTest   = test;
        mLogger = Logger.newInstance();
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
        mLogger.logComment("Compiling " + mTest.getName() + "\n");

        Pair<Object, Method> result = null;
        TestLoader testLoader = TestLoader.newInstance();

        try
        {
            // Compile and load the test class.
            if (testLoader.compile(mTest))
            {
                result = testLoader.load(mTest);
            }

            mLogger.logSuccess("Compilation of " + mTest.getName() + " succesful!\n");

        } catch (Exception ex) {
            mLogger.logError("Compilation of " + mTest.getName() + " failed");
            mLogger.logError("Exception: " + ex.toString() + "\n");
        }

        return result;
    }
}
