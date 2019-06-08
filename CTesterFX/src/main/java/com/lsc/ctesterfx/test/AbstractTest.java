package com.lsc.ctesterfx.test;

/**
 * Abstract class that every test should extend.
 *
 * @author dma@logossmartcard.com
 */
public abstract class AbstractTest
{
    /**
     * This method will be executed before the 'run' method.
     *
     * @return true if the execution of the setup is succesful, false otherwise.
     */
    public abstract boolean setUp();

    /**
     * This is the main method of the test.
     *
     * @return true if the execution of the test is succesful, false otherwise.
     */
    public abstract boolean run();

    /**
     * This method will be executed after the 'run' method.
     *
     * @return true if the execution of the teardown is succesful, false otherwise.
     */
    public abstract boolean tearDown();
}
