package com.lsc.ctesterfx.shell;

import com.lsc.ctesterfx.reader.ReaderController;
import com.lsc.ctesterlib.persistence.Configuration;
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

    // Constants
    private static final String LST_EXTENSION = ".lst";

    public static void run(String lstPath, boolean verbose)
    {
        if (verbose) LOGGER.info("Executing tests from the lst file (" + lstPath + ")");

        // Check and decode .lst
        if (lstPath.endsWith(LST_EXTENSION))
        {
            // Check reader selected.
            String reader = new Configuration().getValueAsString(
                                    Configuration.SHELL, Configuration.READER);

            if (reader != null)
            {
                try
                {
                    ReaderController readerController = ReaderController.newInstance();
                    readerController.select(reader);

                } catch (Exception ex) {
                    LOGGER.error("Error selecting reader (" + reader + ")");
                    LOGGER.error(" - " + ex.getMessage());
                }
            }
            else
            {
                LOGGER.error("No reader selected, set it in 'config.xml' and re-run");
            }
        }
        else
        {
            LOGGER.error("The file containing the java test should be a .lst file");
        }
    }
}
