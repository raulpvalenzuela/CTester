package com.lsc.ctesterfx.background;

import com.lsc.ctesterfx.controllers.FXMLTestItemController;
import com.lsc.ctesterfx.test.TestController;
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
    // Instance to the test controller.
    private final TestController testController;

    public ExecutionTask(TestController testController)
    {
        this.testController = testController;
    }

    @Override
    protected Object call() throws Exception
    {
        return _runTest();
    }

    /**
     * Compiles and runs the specified method notifying the changes so
     * that the GUI gets updated accordingly.
     *
     * FOR INTERNAL USE ONLY
     *
     * @return true if the execution is succesful, false otherwise.
     */
    private boolean _runTest()
    {
        Pair<Object, List<Method>> compilationResult = null;

        TestLoader testLoader     = TestLoader.newInstance();
        TestExecutor testExecutor = TestExecutor.newInstance();

        testController.notifyStartTest();

        // Compilation process starts here
        testController.getLogger().logComment("Compiling " + testController.getTestName() + "\n");
        testController.setState(FXMLTestItemController.TEST_STATE.COMPILING);

        try
        {
            // Compile and load the test class.
            if (testLoader.compile(testController.getTest()))
            {
                compilationResult = testLoader.load(testController.getTest());

                testController.getLogger().logComment("Compilation of " + testController.getTestName() + " succesful!\n");
                testController.setState(FXMLTestItemController.TEST_STATE.COMPILATION_OK);
            }
            else
            {
                testController.getLogger().logError("Compilation of " + testController.getTestName() + " failed\n");
                testController.setState(FXMLTestItemController.TEST_STATE.COMPILATION_FAILED);
            }

        } catch (Exception ex) {
            testController.getLogger().logError("Compilation of " + testController.getTestName() + " failed");
            testController.getLogger().logError("Exception: " + ex.toString() + "\n");
            testController.setState(FXMLTestItemController.TEST_STATE.COMPILATION_FAILED);
        }

        // Execution starts here
        if (compilationResult != null)
        {
            Object object = compilationResult.getKey();
            List<Method> methods = compilationResult.getValue();

            testController.setState(FXMLTestItemController.TEST_STATE.RUNNING);
            for (Method method : methods)
            {
                testController.getLogger().logComment("Calling '" + method.getName() + "' method\n");

                try
                {
                    // Call the method.
                    if (testExecutor.run(object, method))
                    {
                        testController.getLogger().logSuccess("'" + method.getName() + "' method passed succesfully\n");
                    }
                    else
                    {
                        testController.getLogger().logError("'" + method.getName() + "' method failed\n");
                        testController.setState(FXMLTestItemController.TEST_STATE.EXECUTION_FAILED);

                        testController.notifyFinishStart();

                        return false;
                    }

                } catch (Exception ex) {
                    testController.getLogger().logError("Exception executing'" + method.getName() + "' method");
                    testController.getLogger().logError("Exception: " + ex.toString() + "\n");
                    testController.setState(FXMLTestItemController.TEST_STATE.EXECUTION_FAILED);

                    testController.notifyFinishStart();

                    return false;
                }
            }

            testController.setState(FXMLTestItemController.TEST_STATE.EXECUTION_OK);

            testController.notifyFinishStart();

            return true;
        }

        testController.notifyFinishStart();

        return false;
    }
}
