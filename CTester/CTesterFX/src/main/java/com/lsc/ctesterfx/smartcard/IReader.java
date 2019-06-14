package com.lsc.ctesterfx.smartcard;

/**
 * Interface with a reader.
 *
 * @author dma@logossmartcard.com
 */
public interface IReader
{
    public byte[] reset();

    public ApduResponse transmit(ApduCommand command);
}
