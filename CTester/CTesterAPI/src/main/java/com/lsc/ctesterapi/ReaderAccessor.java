package com.lsc.ctesterapi;

import com.lsc.ctesterfx.reader.IReader;
import com.lsc.ctesterfx.iso7816.ApduCommand;
import com.lsc.ctesterfx.iso7816.ApduResponse;
import com.lsc.ctesterfx.reader.Readers;
import javax.smartcardio.CardException;

/**
 * Class that provides an API to communicate with the card.
 *
 * @author dma@logossmartcard.com
 */
public class ReaderAccessor implements IReader
{
    @Override
    public byte[] reset() throws CardException
    {
        return Readers.getSelected().reset();
    }

    @Override
    public ApduResponse transmit(ApduCommand command) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
