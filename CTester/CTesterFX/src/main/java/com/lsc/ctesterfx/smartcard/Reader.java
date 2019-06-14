package com.lsc.ctesterfx.smartcard;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;

/**
 * Class that represents a smartcard reader.
 *
 * @author dma@logossmartcard.com
 */
public class Reader implements IReader
{
    private Card card;
    private CardTerminal reader;

    public static class Builder
    {
        private CardTerminal reader;

        public Builder() {}

        /**
         * Sets the card terminal
         * @param reader: reference to the card terminal.
         * @return a Builder instance
         */
        public Builder fromCardTerminal(CardTerminal reader)
        {
            this.reader = reader;

            return this;
        }

        public Reader build()
        {
            Reader cardReader = new Reader();

            cardReader.reader      = this.reader;
            cardReader.card        = null;

            return cardReader;
        }
    }

    private Reader() {}

    @Override
    public byte[] reset()
    {
        try
        {
            card = reader.connect("*");

            return card.getATR().getBytes();

        } catch (CardException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public ApduResponse transmit(ApduCommand command) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getName()
    {
        return reader.getName();
    }
}
