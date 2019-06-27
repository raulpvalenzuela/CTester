package com.lsc.ctesterfx.script;

import java.io.File;

/**
 * Interface to interact with a IO script file.
 *
 * @author dma@logossmartcard.com
 */
public interface IScriptExecutor
{
    /**
     * Executes the script given.
     *
     * @param script script file to be executed.
     * @return true if the execution is succesful.
     * @throws Exception if there is some communication error.
     */
    public boolean execute(File script) throws Exception;
}
