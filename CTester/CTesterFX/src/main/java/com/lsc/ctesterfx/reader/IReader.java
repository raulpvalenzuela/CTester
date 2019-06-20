package com.lsc.ctesterfx.reader;

import com.lsc.ctesterfx.iso7816.ApduResponse;
import com.lsc.ctesterfx.iso7816.ApduCommand;
import javax.smartcardio.CardException;

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
     * @return ATR of the card.
     * @throws CardException if the card is not available or accessible.
     */
    public byte[] reset() throws CardException;

    /**
     * Transmits the APDU and returns the response.
     *
     * @param apdu: apdu to be transmitted.
     * @return response from the card.
     * @throws CardException if there's an error transmitting the command.
     */
    public ApduResponse transmit(ApduCommand apdu) throws CardException;
}
