package com.lsc.ctesterfx.controllers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author dma@logossmartcard.com
 */
public class MultithreadController
{
    public enum TYPE
    {
        COMPILATION,
        EXECUTION;
    }

    // Executor used to compile the test in the background.
    private static ExecutorService mCompilationExecutor;
    // Executor used to execute the test in the background.
    private static ExecutorService mExecutionExecutor;

    /**
     * Method that initializes the controller.
     */
    public static void initializeExecutors()
    {
        mCompilationExecutor = Executors.newFixedThreadPool(1);
        mExecutionExecutor   = Executors.newFixedThreadPool(1);
    }

    /**
     * Method to free the resources.
     */
    public static void shutdown()
    {
        mCompilationExecutor.shutdown();
        mExecutionExecutor.shutdown();
    }

    /**
     * Method that executes a task using the corresponding executor.
     *
     * @param task: task to be executed.
     * @param type: type of executor to be used.
     */
    public static void execute(Runnable task, TYPE type)
    {
        switch (type)
        {
            case COMPILATION:
                mCompilationExecutor.execute(task);
                break;

            case EXECUTION:
                mExecutionExecutor.execute(task);
                break;
        }
    }
}
