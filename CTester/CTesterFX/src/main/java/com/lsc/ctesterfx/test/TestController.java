package com.lsc.ctesterfx.test;

import com.lsc.ctesterfx.background.MultithreadController.TYPE;
import com.lsc.ctesterfx.controllers.FXMLTestItemController;
import com.lsc.ctesterfx.logger.FileLogger;
import com.lsc.ctesterfx.logger.ApplicationLogger;
import com.lsc.ctesterfx.test.Test.TEST_STATE;
import org.apache.log4j.Logger;

/**
 * Class containing all the stuff related to a specific test.
 *
 * @author dma@logossmartcard.com
 */
public class TestController
{
    private static final Logger LOGGER = Logger.getLogger(TestController.class);

    private final FXMLTestItemController testItemController;
    private final Test test;
    private final ApplicationLogger applicationLogger;
    private final FileLogger fileLogger;

    /**
     * Constructor.
     *
     * @param test: reference to the test object.
     * @param testItemController: referente to the FXMLTestItemController.
     */
    public TestController(Test test, FXMLTestItemController testItemController)
    {
        this.test               = test;
        this.testItemController = testItemController;
        this.applicationLogger  = ApplicationLogger.newInstance();

        this.fileLogger = new FileLogger.Builder()
                                .withName(test.getName())
                                .in(test.getPath())
                                .build();
    }

    /**
     * Returns the test name.
     *
     * @return the test name.
     */
    public String getTestName()
    {
        return test.getName();
    }

    /**
     * Returns the logger instance.
     *
     * @return the logger instance.
     */
    public ApplicationLogger getLogger()
    {
        return applicationLogger;
    }

    /**
     * Returns the test instance
     *
     * @return the test instance.
     */
    public Test getTest()
    {
        return test;
    }

    /**
     * Notifies the FXMLTestItemController to change the test state.
     *
     * @param state: new test state to be set.
     */
    public void setState(TEST_STATE state)
    {
        testItemController.setState(state);
        test.setState(state);
    }

    /**
     * Called from the outside to notify that the test is going
     * to start, so the controller can set everything up.
     * @param type type of the task (compilation or execution)
     */
    public void notifyStartTest(TYPE type)
    {
        LOGGER.debug("Setting new file logger");

        fileLogger.initialize();
        testItemController.notifyStartExecution(type);
        applicationLogger.setFileLogger(fileLogger);
    }

    /**
     * Called from the outside to notify that the test is finished
     * so that the controller can free resources.
     * @param success true if the task was succesful.
     * @param type type of the task (compilation or execution)
     */
    public void notifyFinishTest(boolean success, TYPE type)
    {
        LOGGER.debug("Closing file logger");

        testItemController.notifyFinishedExecution(success, type);
        fileLogger.close();
    }
}
