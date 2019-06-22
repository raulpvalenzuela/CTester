package com.lsc.ctesterfx.reader;

import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;
import org.apache.log4j.Logger;

/**
 * Class that manages the readers connected to the computer.
 *
 * @author dma@logossmartard.com
 */
public class ReaderController
{
    // Message errors.
    public static final String OK = "No errors";

    // Internal logger.
    private static final Logger LOGGER = Logger.getLogger(ReaderController.class);
    // Reference to the current reader selected.
    private static Reader currentReader;

    /**
     * Returns a list containing all the readers' names.
     *
     * @return pair with the message error if any, and list with all the readers' names.
     */
    public static Pair<String, List<String>> list()
    {
        List<String> readers = new ArrayList<>();

        try
        {
            for (CardTerminal cardTerminal : TerminalFactory.getDefault().terminals().list())
            {
                readers.add(cardTerminal.getName());
            }

        } catch (CardException ex) {
            LOGGER.error("Exception retrieving readers");
            LOGGER.error(ex);

            return new Pair<>(ex.getMessage(), new ArrayList<>());
        }

        return new Pair<>(OK, readers);
    }

    /**
     * Selects a specific reader to be used from now on.
     *
     * @param index index of the reader to be selected.
     */
    public static void select(int index)
    {
        // Release the previous one
        if (currentReader != null)
        {
            currentReader.release();
        }

        try
        {
            currentReader = new PCSCReader.Builder()
                    .fromCardTerminal(TerminalFactory.getDefault().terminals().list().get(index))
                    .build();

        } catch (CardException ex) {
            LOGGER.error("Exception selecting reader");
            LOGGER.error(ex);
        }
    }

    /**
     * Returns the selected reader.
     *
     * @return the selected reader. Null if there is no reader selected.
     */
    public static Reader getSelected()
    {
        return currentReader;
    }
}
