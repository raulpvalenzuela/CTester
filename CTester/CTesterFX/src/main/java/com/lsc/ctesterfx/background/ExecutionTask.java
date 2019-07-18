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
        boolean success = false;
        long startTime = System.currentTimeMillis();
        long endTime;

        Pair<Object, List<Method>> compilationResult;

        // First we need to notify the controller that the task has started.
        // The file logger will be initialized here so it's safe to use it from now on.
        testController.notifyStartTest(TYPE.EXECUTION);
        testController.setState(TEST_STATE.COMPILING);

        testController.getLogger().logComment(" -------------------------------------------- //");
        testController.getLogger().logComment("Starting test: " + testController.getTestName() + "\n");

        compilationResult = compile();
        if (compilationResult == null)
        {
            // Notify the controller that the task has finished.
            testController.notifyFinishTest(false, TYPE.EXECUTION);
            testController.setState(TEST_STATE.COMPILATION_FAILED);
        }
        // Execution starts here
        else
        {
            if ((success = execute(compilationResult)))
            {
                endTime = System.currentTimeMillis();

                testController.getLogger().logComment("Time elapsed: " + Formatter.formatInterval(endTime - startTime));
                testController.getLogger().logComment("--------------------------------------------- //");
                testController.getLogger().log("");
            }

            testController.setState((success) ? TEST_STATE.EXECUTION_OK : TEST_STATE.EXECUTION_FAILED);
            testController.notifyFinishTest(success, TYPE.EXECUTION);
        }

        return success;
    }

    /**
     * Helper method to compile the test.
     *
     * @return Pair containing the object and the
     *         methods 'setup', 'run' and 'teardown'. Null if there's been an exception.
     */
    private Pair<Object, List<Method>> compile()
    {
        LOGGER.info("Compiling '" + testController.getTestName() + "'");

        testController.getLogger().logComment("Compiling...\n");

        Pair<Object, List<Method>> compilationResult = null;
        TestLoader testLoader = TestLoader.newInstance();

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
                }
                else
                {
                    LOGGER.info("Loading of '" + testController.getTestName() + "' succesful");

                    testController.getLogger().logComment("Compilation succesful\n");
                }
            }
            else
            {
                LOGGER.error("Compilation of '" + testController.getTestName() + "' failed");

                testController.getLogger().logError("Compilation failed\n");
            }

        } catch (Exception ex) {
            LOGGER.error("Exception compiling test");
            LOGGER.error(ex);

            testController.getLogger().logError("Compilation failed");
            testController.getLogger().logError("Exception: " + ex.toString() + "\n");
        }

        return compilationResult;
    }

    /**
     * Executes the test.
     *
     * @param compilationResult: pair containing the object and the list of methods.
     * @return true if succesful.
     */
    private boolean execute(Pair<Object, List<Method>> compilationResult)
    {
        LOGGER.info("Executing '" + testController.getTestName() + "'");

        TestExecutor testExecutor = TestExecutor.newInstance();

        // Get the test instance and the methods.
        Object object        = compilationResult.getKey();
        List<Method> methods = compilationResult.getValue();

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

                    testController.getLogger().logError("'" + method.getName() + "' method failed\n");

                    return false;
                }

            } catch (Exception ex) {
                LOGGER.error("Exception executing test");
                LOGGER.error(ex);

                testController.getLogger().logError("Exception executing '" + method.getName() + "' method");
                testController.getLogger().logError("Exception: " + ex.toString() + "\n");

                return false;
            }
        }

        return true;
    }
}
