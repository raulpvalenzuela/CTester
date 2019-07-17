package com.lsc.ctesterfx.background;

import com.lsc.ctesterfx.background.MultithreadController.TYPE;
import com.lsc.ctesterfx.test.Test.TEST_STATE;
import com.lsc.ctesterfx.test.TestController;
import com.lsc.ctesterfx.test.TestExecutor;
import com.lsc.ctesterfx.test.TestLoader;
import com.lsc.ctesterlib.utils.Formatter;
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
        return runTest();
    }

    /**
     * Compiles and runs the specified method notifying the changes so
     * that the GUI gets updated accordingly.
     *
     * @return true if the execution is succesful, false otherwise.
     */
    private boolean runTest()
    {
        long startTime;
        long endTime;
        boolean success = true;

        LOGGER.info("Compiling '" + testController.getTestName() + "'");

        Pair<Object, List<Method>> compilationResult = null;

        TestLoader testLoader     = TestLoader.newInstance();
        TestExecutor testExecutor = TestExecutor.newInstance();

        // First we need to notify the controller that the task has started.
        testController.notifyStartTest(TYPE.EXECUTION);

        startTime = System.currentTimeMillis();

        // Compilation process starts here
        testController.getLogger().logComment(" -------------------------------------------- //");
        testController.getLogger().logComment("Starting test: " + testController.getTestName() + "\n");
        testController.getLogger().logComment("Compiling...\n");
        testController.setState(TEST_STATE.COMPILING);

        try
        {
            // Compile and load the test class.
            if (testLoader.compile(testController.getTest()))
            {
                LOGGER.info("Compilation of '" + testController.getTestName() + "' succesful");

                // Load the class and get the methods.
                if ((compilationResult = testLoader.load(testController.getTest())) == null)
                {
                    LOGGER.error("Loading of '" + testController.getTestName() + "' failed");

                    success = false;
                }
                else
                {
                    LOGGER.info("Loading of '" + testController.getTestName() + "' succesful");

                    testController.getLogger().logComment("Compilation succesful\n");
                    testController.setState(TEST_STATE.COMPILATION_OK);
                }
            }
            else
            {
                LOGGER.error("Compilation of '" + testController.getTestName() + "' failed");

                success = false;

                testController.getLogger().logError("Compilation failed\n");
                testController.setState(TEST_STATE.COMPILATION_FAILED);
            }

        } catch (Exception ex) {
            LOGGER.error("Exception compiling test");
            LOGGER.error(ex);

            success = false;

            testController.getLogger().logError("Compilation failed");
            testController.getLogger().logError("Exception: " + ex.toString() + "\n");
            testController.setState(TEST_STATE.COMPILATION_FAILED);
        }

        // Execution starts here
        if (compilationResult != null)
        {
            LOGGER.info("Executing '" + testController.getTestName() + "'");

            // Get the test instance and the methods.
            Object object        = compilationResult.getKey();
            List<Method> methods = compilationResult.getValue();

            testController.setState(TEST_STATE.RUNNING);
            for (Method method : methods)
            {
                LOGGER.info("Calling '" + method.getName() + "' method");
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

                        success = false;

                        testController.getLogger().logError("'" + method.getName() + "' method failed\n");
                        testController.setState(TEST_STATE.EXECUTION_FAILED);
                        testController.notifyFinishTest(success, TYPE.EXECUTION);

                        return false;
                    }

                } catch (Exception ex) {
                    LOGGER.error("Exception executing test");
                    LOGGER.error(ex);

                    success = false;

                    testController.getLogger().logError("Exception executing '" + method.getName() + "' method");
                    testController.getLogger().logError("Exception: " + ex.toString() + "\n");
                    testController.setState(TEST_STATE.EXECUTION_FAILED);

                    testController.notifyFinishTest(success, TYPE.EXECUTION);

                    return false;
                }
            }

            endTime = System.currentTimeMillis();

            LOGGER.info("Execution of '" + testController.getTestName() + "' succesful");

            testController.getLogger().logComment("");
            testController.getLogger().logComment("Time elapsed: " + Formatter.formatInterval(endTime - startTime));
            testController.getLogger().logComment("--------------------------------------------- //");
            testController.getLogger().log("");

            testController.setState(TEST_STATE.EXECUTION_OK);
            testController.notifyFinishTest(success, TYPE.EXECUTION);

            return true;
        }

        // Notify the controller that the task has finished.
        testController.notifyFinishTest(success, TYPE.EXECUTION);

        return false;
    }
}
