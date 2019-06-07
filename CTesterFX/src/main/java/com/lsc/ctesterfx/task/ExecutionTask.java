package com.lsc.ctesterfx.task;

import com.lsc.ctesterfx.interfaces.AbstractLogger;
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

    public ExecutionTask(Object object, Method method)
    {
        mObject = object;
        mMethod = method;

        mLogger = Logger.newInstance();
    }

    @Override
    protected Object call() throws Exception
    {
        return runTest(mObject, mMethod);
    }

    /**
     * Method that runs the specified method.
     *
     * @param object: instance of the test.
     * @param method: method to be invoked.
     * @return true if the execution is succesful, false otherwise.
     */
    private boolean runTest(Object object, Method method)
    {
        mLogger.logComment("Calling '" + method.getName() + "' method\n");

        TestExecutor testExecutor = TestExecutor.newInstance();

        try
        {
            // Call the 'run' method.
            boolean result = testExecutor.run(object, method);

            if (result)
            {
                mLogger.logSuccess("'" + method.getName() + "' method passed succesfully\n");
            }
            else
            {
                mLogger.logError("'" + method.getName() + "' method failed\n");
            }

            return result;

        } catch (Exception ex) {
            mLogger.logError("Exception executing'" + method.getName() + "' method");
            mLogger.logError("Exception: " + ex.toString() + "\n");
        }

        return false;
    }
}
