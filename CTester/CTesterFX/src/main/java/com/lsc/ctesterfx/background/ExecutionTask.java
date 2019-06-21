package com.lsc.ctesterfx.background;

import com.lsc.ctesterfx.controllers.FXMLTestItemController;
import com.lsc.ctesterfx.test.TestController;
import com.lsc.ctesterfx.test.TestExecutor;
import com.lsc.ctesterfx.test.TestLoader;
import java.lang.reflect.Method;
import java.util.List;
import javafx.concurrent.Task;
import javafx.util.Pair;
import org.apache.log4j.Logger;

/**
 * Class to run the execution in the background to not freeze de GUI.
 * Running it in the background forces to have all the components that update the GUI
 * to use Platform.runLater(...), as the GUI cannot be updated from a thread.
 *
 * @author dma@logossmartcard.com
 */
public class ExecutionTask extends Task
{
    private static final Logger LOGGER = Logger.getLogger(ExecutionTask.class);

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
     * @return true if the execution is succesful, false otherwise.
     */
    private boolean _runTest()
    {
        LOGGER.info("Compiling " + testController.getTestName());

        Pair<Object, List<Method>> compilationResult = null;

        TestLoader testLoader     = TestLoader.newInstance();
        TestExecutor testExecutor = TestExecutor.newInstance();

        // First we need to notify the controller that the task has started.
        testController.notifyStartTest();

        // Compilation process starts here
        testController.getLogger().logComment("Compiling " + testController.getTestName() + "\n");
        testController.setState(FXMLTestItemController.TEST_STATE.COMPILING);

        try
        {
            // Compile and load the test class.
            if (testLoader.compile(testController.getTest()))
            {
                LOGGER.info("Compilation of " + testController.getTestName() + " succesful");

                if ((compilationResult = testLoader.load(testController.getTest())) == null)
                {
                    LOGGER.error("Loading of " + testController.getTestName() + " failed");
                }
                else
                {
                    LOGGER.info("Loading of " + testController.getTestName() + " succesful");

                    testController.getLogger().logComment("Compilation of " + testController.getTestName() + " succesful!\n");
                    testController.setState(FXMLTestItemController.TEST_STATE.COMPILATION_OK);
                }
            }
            else
            {
                LOGGER.error("Compilation of " + testController.getTestName() + " failed");

                testController.getLogger().logError("Compilation of " + testController.getTestName() + " failed\n");
                testController.setState(FXMLTestItemController.TEST_STATE.COMPILATION_FAILED);
            }

        } catch (Exception ex) {
            LOGGER.error("Exception compiling test");
            LOGGER.error(ex);

            testController.getLogger().logError("Compilation of " + testController.getTestName() + " failed");
            testController.getLogger().logError("Exception: " + ex.toString() + "\n");
            testController.setState(FXMLTestItemController.TEST_STATE.COMPILATION_FAILED);
        }

        // Execution starts here
        if (compilationResult != null)
        {
            LOGGER.info("Executing " + testController.getTestName());

            Object object = compilationResult.getKey();
            List<Method> methods = compilationResult.getValue();

            testController.setState(FXMLTestItemController.TEST_STATE.RUNNING);
            for (Method method : methods)
            {
                LOGGER.info("Calling " + method.getName() + "' method");
                testController.getLogger().logComment("Calling '" + method.getName() + "' method\n");

                try
                {
                    // Call the method.
                    if (testExecutor.run(object, method))
                    {
                        LOGGER.info("'" + method.getName() + "' method passed succesfully");

                        testController.getLogger().logSuccess("'" + method.getName() + "' method passed succesfully\n");
                    }
                    else
                    {
                        LOGGER.info("'" + method.getName() + "' method failed");

                        testController.getLogger().logError("'" + method.getName() + "' method failed\n");
                        testController.setState(FXMLTestItemController.TEST_STATE.EXECUTION_FAILED);
                        testController.notifyFinishTest();

                        return false;
                    }

                } catch (Exception ex) {
                    LOGGER.error("Exception executing test");
                    LOGGER.error(ex);

                    testController.getLogger().logError("Exception executing'" + method.getName() + "' method");
                    testController.getLogger().logError("Exception: " + ex.toString() + "\n");
                    testController.setState(FXMLTestItemController.TEST_STATE.EXECUTION_FAILED);

                    testController.notifyFinishTest();

                    return false;
                }
            }

            LOGGER.info("Execution of " + testController.getTestName() + " succesful");

            testController.setState(FXMLTestItemController.TEST_STATE.EXECUTION_OK);
            testController.notifyFinishTest();

            return true;
        }

        testController.notifyFinishTest();

        return false;
    }
}
