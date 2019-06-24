package com.lsc.ctesterlib.utils;

import com.lsc.ctesterlib.utils.Formatter;
import org.apache.commons.codec.DecoderException;

/**
 *
 * @author dma@logossmartcard.com
 */
public class ApduValidator
{
    /**
     * Validates that the string is a valid apdu.
     *
     * @param command: command to be validated.
     * @return true if it is a valid command, false otherwise.
     */
    public static boolean isValid(final String command)
    {
        String cmd = command.replaceAll(" ", "").trim();

        // If it's less than 5 bytes.
        if (cmd.length() < 5*2)
        {
            return false;
        }

        try
        {
            // Convert it to byte array, if an exception is thrown the command is not valid.
            Formatter.fromStringToByteArray(cmd);

        } catch (DecoderException ex) {
            return false;
        }

        return true;
    }
}
