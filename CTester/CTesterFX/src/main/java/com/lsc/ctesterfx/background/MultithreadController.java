package com.lsc.ctesterfx.background;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;

/**
 * Class that handles the executors needed in the system. One will be used to
 * compile, the other to execute tests.
 *
 * @author dma@logossmartcard.com
 */
public class MultithreadController
{
    private static final Logger LOGGER = Logger.getLogger(MultithreadController.class);

    public enum TYPE
    {
        COMPILATION,
        EXECUTION;
    }

    // Executor used to compile the test in the background.
    private static ExecutorService compilationExecutor;
    // Executor used to execute the test in the background.
    private static ExecutorService executionExecutor;

    /**
     * Initializes the controller creating different executor instances.
     */
    public static void initialize()
    {
        LOGGER.info("Executors initialized");

        compilationExecutor = Executors.newFixedThreadPool(1);
        executionExecutor   = Executors.newFixedThreadPool(1);
    }

    /**
     * Frees resources.
     */
    public static void shutdown()
    {
        LOGGER.info("Shutting the executors down");

        compilationExecutor.shutdownNow();
        executionExecutor.shutdownNow();

        LOGGER.info("Executors shut down");
    }

    /**
     * Executes a task using the corresponding executor.
     *
     * @param task: task to be executed.
     * @param type: type of executor to be used.
     */
    public static void execute(Runnable task, TYPE type)
    {
        switch (type)
        {
            case COMPILATION:
                LOGGER.info("New compilation task queued");
                compilationExecutor.execute(task);
                break;

            case EXECUTION:
                LOGGER.info("New execution task queued");
                executionExecutor.execute(task);
                break;
        }
    }
}
