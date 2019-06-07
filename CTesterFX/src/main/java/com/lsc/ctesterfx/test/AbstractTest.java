package com.lsc.ctesterfx.test;

/**
 * Abstract class that every test should extend.
 *
 * @author dma@logossmartcard.com
 */
public abstract class AbstractTest
{
    /**
     * This method must be implemented by every test executed by this tool.
     *
     * @return true if the execution of the test is succesful, false otherwise.
     */
    public abstract boolean run();
}
