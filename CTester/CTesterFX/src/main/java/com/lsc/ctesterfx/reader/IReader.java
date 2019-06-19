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
    public byte[] reset() throws CardException;

    public ApduResponse transmit(ApduCommand command);
}
