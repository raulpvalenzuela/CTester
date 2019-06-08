package com.lsc.ctesterfx.background;

import com.lsc.ctesterfx.controllers.FXMLTestItemController;
import com.lsc.ctesterfx.dao.Test;
import com.lsc.ctesterfx.logger.AbstractLogger;
import com.lsc.ctesterfx.logger.Logger;
import com.lsc.ctesterfx.test.TestExecutor;
import com.lsc.ctesterfx.test.TestLoader;
import java.lang.reflect.Method;
import java.util.List;
import javafx.concurrent.Task;
import javafx.util.Pair;

/**
 * Class to run the execution in the background to not freeze de GUI.
 * Running it in the background forces to have all the components that update the GUI
 * to use Platform.runLater(...), as the GUI cannot be updated from a thread.
 *
 * @author dma@logossmartcard.com
 */
public class ExecutionTask extends Task
{
    // Instance of the logger.
    private final AbstractLogger mLogger;

    // Instance to the test to be compiled.
    private final Test mTest;
    // Referente to the test controller to update the GUI.
    private final FXMLTestItemController mTestController;

    public ExecutionTask(Test test, FXMLTestItemController testController)
    {
        mTestController = testController;
        mTest           = test;

        mLogger         = Logger.newInstance();
    }

    @Override
    protected Object call() throws Exception
    {
        return runTest();
    }

    /**
     * Method that compiles and runs the specified method.
     *
     * FOR INTERNAL USE ONLY
     *
     * @return true if the execution is succesful, false otherwise.
     */
    private boolean runTest()
    {
        Pair<Object, List<Method>> compilationResult = null;

        TestLoader testLoader     = TestLoader.newInstance();
        TestExecutor testExecutor = TestExecutor.newInstance();

        // Compilation process starts here
        mLogger.logComment("Compiling " + mTest.getName() + "\n");
        mTestController.setState(FXMLTestItemController.TEST_STATE.COMPILING);

        try
        {
            // Compile and load the test class.
            if (testLoader.compile(mTest))
            {
                compilationResult = testLoader.load(mTest);

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

        // Execution starts here
        if (compilationResult != null)
        {
            Object object = compilationResult.getKey();
            List<Method> methods = compilationResult.getValue();

            mTestController.setState(FXMLTestItemController.TEST_STATE.RUNNING);
            for (Method method : methods)
            {
                mLogger.logComment("Calling '" + method.getName() + "' method\n");

                try
                {
                    // Call the method.
                    if (testExecutor.run(object, method))
                    {
                        mLogger.logSuccess("'" + method.getName() + "' method passed succesfully\n");
                    }
                    else
                    {
                        mLogger.logError("'" + method.getName() + "' method failed\n");
                        mTestController.setState(FXMLTestItemController.TEST_STATE.EXECUTION_FAILED);

                        return false;
                    }

                } catch (Exception ex) {
                    mLogger.logError("Exception executing'" + method.getName() + "' method");
                    mLogger.logError("Exception: " + ex.toString() + "\n");
                    mTestController.setState(FXMLTestItemController.TEST_STATE.EXECUTION_FAILED);

                    return false;
                }
            }

            mTestController.setState(FXMLTestItemController.TEST_STATE.EXECUTION_OK);

            return true;
        }

        return false;
    }
}
