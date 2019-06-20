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
     * @return ATR of the card. Null if there is an error.
     */
    public byte[] reset();

    /**
     * Transmits the APDU and returns the response.
     *
     * @param apdu: apdu to be transmitted.
     * @return response from the card.
     */
    public ApduResponse transmit(ApduCommand apdu);
}
