package com.lsc.ctesterfx.reader;

import com.lsc.ctesterfx.iso7816.ApduResponse;
import com.lsc.ctesterfx.iso7816.ApduCommand;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;

/**
 * Class that represents a PCSC reader.
 *
 * @author dma@logossmartcard.com
 */
public class PCSCReader extends Reader
{
    // Flag to indicate if a connection to the card has been estabished.
    private boolean isConnected;

    // Reference to the card.
    private Card card;
    // Reference to the card channel used to transmit commands.
    private CardChannel channel;
    // Reference to the terminal.
    private CardTerminal reader;

    /**
     * Builder class to build a PCSCReader instance.
     */
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

            cardReader.reader      = this.reader;
            cardReader.card        = null;
            cardReader.isConnected = false;

            return cardReader;
        }
    }

    private PCSCReader() {}

    @Override
    public void connect()
    {
        channel = card.getBasicChannel();

        isConnected = true;
    }

    @Override
    public void release()
    {
        if (isConnected && (channel != null))
        {
            try
            {
                channel.close();
                card.disconnect(false);

            } catch (CardException | IllegalStateException ex) {
                // This exception is triggered if the card has been removed.

            } finally {
                isConnected = false;
            }
        }
    }

    @Override
    public byte[] reset()
    {
        try
        {
            // Establishes a connection to the card. If a connection has previously established using the
            // specified protocol, this method returns the same Card object as the previous call.
            card = reader.connect("*");

            if (!isConnected)
            {
                connect();
            }

            return card.getATR().getBytes();

        } catch (CardException ex) {
            Logger.getLogger(PCSCReader.class.getName()).log(Level.SEVERE, null, ex);

            return null;
        }
    }

    @Override
    public ApduResponse transmit(ApduCommand apdu)
    {
        return new ApduResponse();
    }

    @Override
    public String getName()
    {
        return reader.getName();
    }
}
