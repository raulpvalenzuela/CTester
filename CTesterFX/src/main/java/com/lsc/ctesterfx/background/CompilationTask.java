package com.lsc.ctesterfx.background;

import com.lsc.ctesterfx.controllers.FXMLTestItemController;
import com.lsc.ctesterfx.dao.Test;
import com.lsc.ctesterfx.logger.AbstractLogger;
import com.lsc.ctesterfx.logger.Logger;
import com.lsc.ctesterfx.test.TestLoader;
import java.lang.reflect.Method;
import java.util.List;
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
    // Referente to the test controller to update the GUI.
    private final FXMLTestItemController mTestController;

    public CompilationTask(Test test, FXMLTestItemController testController)
    {
        mTest           = test;
        mTestController = testController;

        mLogger         = Logger.newInstance();
    }

    @Override
    protected Object call() throws Exception
    {
        return _compileTest();
    }

    /**
     * Compiles and loads the test, returning the object and a list of methods.
     *
     * FOR INTERNAL USE ONLY
     *
     * @return Pair containing the object and the methods to be invoked.
     */
    private Pair<Object, List<Method>> _compileTest()
    {
        mLogger.logComment("Compiling " + mTest.getName() + "\n");
        mTestController.setState(FXMLTestItemController.TEST_STATE.COMPILING);

        Pair<Object, List<Method>> result = null;
        TestLoader testLoader = TestLoader.newInstance();

        try
        {
            // Compile and load the test class.
            if (testLoader.compile(mTest))
            {
                result = testLoader.load(mTest);

                mLogger.logComment("Compilation of " + mTest.getName() + " succesful!\n");
                mTestController.setState(FXMLTestItemController.TEST_STATE.COMPILATION_OK);
            }
            else
            {
                mLogger.logError("Compilation of " + mTest.getName() + " failed\n");
                mTestController.setState(FXMLTestItemController.TEST_STATE.COMPILATION_FAILED);
            }

        } catch (Exception ex) {
            mLogger.logError("Compilation of " + mTest.getName() + " failed");
            mLogger.logError("Exception: " + ex.toString() + "\n");
            mTestController.setState(FXMLTestItemController.TEST_STATE.COMPILATION_FAILED);
        }

        return result;
    }
}
