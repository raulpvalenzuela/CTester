package com.lsc.ctesterfx.test;

import com.lsc.ctesterfx.controllers.FXMLTestItemController;
import com.lsc.ctesterfx.dao.Test;
import com.lsc.ctesterfx.logger.FileLogger;
import com.lsc.ctesterfx.logger.Logger;

/**
 * Class containing all the stuff related to a specific test.
 *
 * @author dma@logossmartcard.com
 */
public class TestController
{
    private final FXMLTestItemController testItemController;
    private final Test test;
    private final Logger logger;
    private final FileLogger fileLogger;

    public TestController(Test test, FXMLTestItemController testItemController1)
    {
        this.test               = test;
        this.testItemController = testItemController1;
        this.logger             = Logger.newInstance();

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
    public Logger getLogger()
    {
        return logger;
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
    public void setState(FXMLTestItemController.TEST_STATE state)
    {
        testItemController.setState(state);
    }

    /**
     * Called from the outside to notify that the test is going
     * to start, so the controller can set everything up.
     */
    public void notifyStartTest()
    {
        fileLogger.initialize();

        logger.setFileLogger(fileLogger);
    }

    /**
     * Called from the outside to notify that the test is finished
     * so that the controller can free resources.
     */
    public void notifyFinishStart()
    {
        fileLogger.close();
    }
}
