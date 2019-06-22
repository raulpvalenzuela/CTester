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
     * Creates a new connection with the card.
     * @throws java.lang.Exception if an error occurs when connecting to the card.
     */
    public void connect() throws Exception;

    /**
     * Releases the connection with the card. After calling this method, either <code>connect</code>
     * or <code>reset</code> has to be called to communicate again with the card.
     * @throws java.lang.Exception if an error occurs when releasing the reader.
     */
    public void release() throws Exception;

    /**
     * Resets the card and returns the ATR.
     *
     * @return ATR of the card. It will never be null unless an exception is thrown.
     * @throws java.lang.Exception if an error occurs resetting the card.
     */
    public byte[] reset() throws Exception;

    /**
     * Transmits the APDU and returns the response.
     *
     * @param apdu: apdu to be transmitted.
     * @return response from the card.
     * @throws java.lang.Exception if an error occurs sending an APDU.
     */
    public ApduResponse transmit(ApduCommand apdu) throws Exception;

    /**
     * Returns the name of the reader.
     *
     * @return name of the reader.
     */
    public String getName();
}
