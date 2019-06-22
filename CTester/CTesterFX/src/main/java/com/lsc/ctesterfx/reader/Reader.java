package com.lsc.ctesterfx.reader;

import com.lsc.ctesterfx.iso7816.ApduCommand;
import com.lsc.ctesterfx.iso7816.ApduResponse;

/**
 * Abstract class that every reader should extend. It will contain
 * the public API plus some methods to be used from within the application.
 *
 * @author dma@logossmartcard.com
 */
public abstract class Reader implements IReader
{
    @Override
    public abstract byte[] reset() throws Exception;

    @Override
    public abstract ApduResponse transmit(ApduCommand command) throws Exception;

    /**
     * Creates a new connection with the card.
     */
    public abstract void connect();

    /**
     * Releases the connection with the card.
     */
    public abstract void release();

    /**
     * Returns the name of the reader.
     *
     * @return name of the reader.
     */
    public abstract String getName();
}
