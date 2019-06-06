package com.lsc.ctesterfx.task;

import com.lsc.ctesterfx.test.TestExecutor;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private final Object mObject;
    private final Method mMethod;

    public ExecutionTask(Object object, Method method)
    {
        mObject = object;
        mMethod = method;
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
        TestExecutor testExecutor = TestExecutor.newInstance();

        try
        {
            // Call the 'run' method.
            return testExecutor.run(object, method);

        } catch (Exception ex) {
            Logger.getLogger(ExecutionTask.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }
}
