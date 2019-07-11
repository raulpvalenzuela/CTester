package com.lsc.ctesterfx.shell;

import org.apache.log4j.Logger;

/**
 * Class that handles the execution of the tests
 * when the application has been executed from the command line.
 *
 * @author dma@logossmartcard.com
 */
public class ShellController
{
    private static final Logger LOGGER = Logger.getLogger(ShellController.class);

    public static void run(String lstPath, boolean verbose)
    {
        if (verbose) LOGGER.info("Executing tests from the lst file (" + lstPath + ")");

        // Check and decode .lst

        // Check reader selected.
    }
}
