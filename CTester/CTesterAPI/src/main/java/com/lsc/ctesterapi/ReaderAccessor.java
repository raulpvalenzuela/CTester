package com.lsc.ctesterapi;

import com.lsc.ctesterfx.constants.Strings;
import com.lsc.ctesterfx.reader.IReader;
import com.lsc.ctesterfx.iso7816.ApduCommand;
import com.lsc.ctesterfx.iso7816.ApduResponse;
import com.lsc.ctesterfx.logger.ApplicationLogger;
import com.lsc.ctesterfx.reader.Reader;
import com.lsc.ctesterfx.reader.ReaderController;
import org.apache.commons.codec.binary.Hex;

/**
 * Class that provides an API to communicate with the card.
 *
 * @author dma@logossmartcard.com
 */
public class ReaderAccessor implements IReader
{
    private static final ApplicationLogger logger = ApplicationLogger.newInstance();

    /**
     * Resets the card and returns the ATR.
     *
     * @return ATR of the card.
     */
    @Override
    public byte[] reset()
    {
        byte[] atr = null;
        Reader reader;

        reader = ReaderController.getSelected();

        if (reader != null)
        {
            atr = reader.reset();
            if (atr != null)
            {
                String atrStr = Hex.encodeHexString(atr)
                    .toUpperCase().replaceAll("(.{" + 2 + "})", "$1 ").trim();

                logger.log(Strings.RESET_CARD);
                logger.logComment(Strings.ATR_HEADER + atrStr + "\n");
            }
            else
            {
                logger.logError(Strings.NO_ATR);
            }
        }
        else
        {
            logger.logWarning(Strings.NO_READER_SELECTED);
        }

        return atr;
    }

    /**
     * Transmits the APDU and returns the response.
     *
     * @param apdu: apdu to be transmitted.
     * @return response from the card.
     */
    @Override
    public ApduResponse transmit(ApduCommand apdu)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
