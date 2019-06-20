package com.lsc.ctesterapi;

import com.lsc.ctesterfx.reader.IReader;
import com.lsc.ctesterfx.iso7816.ApduCommand;
import com.lsc.ctesterfx.iso7816.ApduResponse;
import com.lsc.ctesterfx.reader.ReaderController;
import javax.smartcardio.CardException;

/**
 * Class that provides an API to communicate with the card.
 *
 * @author dma@logossmartcard.com
 */
public class ReaderAccessor implements IReader
{
    /**
     * Resets the card and returns the ATR.
     *
     * @return ATR of the card.
     * @throws CardException
     */
    @Override
    public byte[] reset() throws CardException
    {
        return ReaderController.getSelected().reset();
    }

    /**
     * Transmits the APDU and returns the response.
     *
     * @param apdu: apdu to be transmitted.
     * @return response from the card.
     * @throws CardException
     */
    @Override
    public ApduResponse transmit(ApduCommand apdu) throws CardException
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
