package com.lsc.ctesterfx.reader;

import com.lsc.ctesterfx.iso7816.ApduResponse;
import com.lsc.ctesterfx.iso7816.ApduCommand;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;

/**
 * Class that represents a smartcard reader.
 *
 * @author dma@logossmartcard.com
 */
public class PCSCReader extends Reader
{
    private Card card;
    private CardChannel channel;
    private CardTerminal reader;

    public static class Builder
    {
        private CardTerminal reader;

        public Builder() {}

        /**
         * Sets the card terminal.
         *
         * @param reader: reference to the card terminal.
         * @return a Builder instance
         */
        public Builder fromCardTerminal(CardTerminal reader)
        {
            this.reader = reader;

            return this;
        }

        public PCSCReader build()
        {
            PCSCReader cardReader = new PCSCReader();

            cardReader.reader = this.reader;
            cardReader.card   = null;

            return cardReader;
        }
    }

    private PCSCReader() {}

    @Override
    public byte[] reset() throws CardException
    {
        card = reader.connect("*");

        return card.getATR().getBytes();
    }

    @Override
    public ApduResponse transmit(ApduCommand command)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void release() throws CardException
    {
        channel.close();
    }

    public String getName()
    {
        return reader.getName();
    }
}
