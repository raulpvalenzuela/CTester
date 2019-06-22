package com.lsc.ctesterfx.reader;

import com.lsc.ctesterfx.iso7816.ApduResponse;
import com.lsc.ctesterfx.iso7816.ApduCommand;

/**
 * Public interface with a reader.
 *
 * @author dma@logossmartcard.com
 */
public interface IReader
{
    /**
     * Resets the card and returns the ATR.
     *
     * @return ATR of the card. It will never be null unless an exception is thrown.
     * @throws java.lang.Exception if error resetting the card.
     */
    public byte[] reset() throws Exception;

    /**
     * Transmits the APDU and returns the response.
     *
     * @param apdu: apdu to be transmitted.
     * @return response from the card.
     * @throws java.lang.Exception if error sending an APDU.
     */
    public ApduResponse transmit(ApduCommand apdu) throws Exception;
}
