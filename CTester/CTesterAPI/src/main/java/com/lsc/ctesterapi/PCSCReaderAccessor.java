package com.lsc.ctesterapi;

import com.lsc.ctesterfx.reader.IReader;
import com.lsc.ctesterlib.iso7816.ApduCommand;
import com.lsc.ctesterlib.iso7816.ApduResponse;

/**
 * Interface with a reader.
 *
 * @author dma@logossmartcard.com
 */
public class PCSCReaderAccessor implements IReader
{
    private final IReader applicationReader;

    public PCSCReaderAccessor(IReader reader)
    {
        this.applicationReader = reader;
    }

    /**
     * Creates a new connection with the card.
     * @throws java.lang.Exception if an error occurs when connecting to the card.
     */
    @Override
    public void connect() throws Exception
    {
        applicationReader.connect();
    }
    
    /**
     * Releases the connection with the card. After calling this method, either <code>connect</code>
     * or <code>reset</code> has to be called to communicate again with the card.
     * @throws java.lang.Exception if an error occurs when releasing the reader.
     */
    @Override
    public void release() throws Exception
    {
        applicationReader.release();
    }

    /**
     * Resets the card and returns the ATR.
     *
     * @return ATR of the card. It will never be null unless an exception is thrown.
     * @throws java.lang.Exception if an error occurs resetting the card.
     */
    @Override
    public byte[] reset() throws Exception
    {
        return applicationReader.reset();
    }

    /**
     * Transmits the APDU and returns the response.
     *
     * @param apdu: apdu to be transmitted.
     * @return response from the card.
     * @throws java.lang.Exception if an error occurs sending an APDU.
     */
    @Override
    public ApduResponse transmit(ApduCommand apdu) throws Exception
    {
        return applicationReader.transmit(apdu);
    }

    /**
     * Returns the name of the reader.
     *
     * @return name of the reader.
     */
    @Override
    public String getName()
    {
        return applicationReader.getName();
    }
}
