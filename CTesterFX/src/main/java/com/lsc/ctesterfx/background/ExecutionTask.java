package com.lsc.ctesterfx.background;

import com.lsc.ctesterfx.controllers.FXMLTestItemController;
import com.lsc.ctesterfx.logger.AbstractLogger;
import com.lsc.ctesterfx.logger.Logger;
import com.lsc.ctesterfx.test.TestExecutor;
import java.lang.reflect.Method;
import javafx.concurrent.Task;

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

    // Instance of the test class.
    private final Object mObject;
    // Method to be invoked.
    private final Method mMethod;
    // Referente to the test controller to update the GUI.
    private final FXMLTestItemController mTestController;

    public ExecutionTask(Object object, Method method, FXMLTestItemController testController)
    {
        mObject         = object;
        mMethod         = method;
        mTestController = testController;

        mLogger         = Logger.newInstance();
    }

    @Override
    protected Object call() throws Exception
    {
        return runTest();
    }

    /**
     * Method that runs the specified method.
     *
     * FOR INTERNAL USE ONLY
     *
     * @return true if the execution is succesful, false otherwise.
     */
    private boolean runTest()
    {
        mLogger.logComment("Calling '" + mMethod.getName() + "' method\n");
        mTestController.setState(FXMLTestItemController.TEST_STATE.RUNNING);

        TestExecutor testExecutor = TestExecutor.newInstance();

        try
        {
            // Call the 'run' method.
            boolean result = testExecutor.run(mObject, mMethod);

            if (result)
            {
                mLogger.logSuccess("'" + mMethod.getName() + "' method passed succesfully\n");
                mTestController.setState(FXMLTestItemController.TEST_STATE.EXECUTION_OK);
            }
            else
            {
                mLogger.logError("'" + mMethod.getName() + "' method failed\n");
                mTestController.setState(FXMLTestItemController.TEST_STATE.EXECUTION_FAILED);
            }

            return result;

        } catch (Exception ex) {
            mLogger.logError("Exception executing'" + mMethod.getName() + "' method");
            mLogger.logError("Exception: " + ex.toString() + "\n");
        }

        mTestController.setState(FXMLTestItemController.TEST_STATE.EXECUTION_FAILED);

        return false;
    }
}
