package com.lsc.ctesterfx.reader;

import com.lsc.ctesterlib.iso7816.ApduResponse;
import com.lsc.ctesterlib.iso7816.ApduCommand;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import org.apache.log4j.Logger;

/**
 * Class that represents a PCSC reader.
 *
 * @author dma@logossmartcard.com
 */
public class PCSCReader implements IReader
{
    private static final Logger LOGGER = Logger.getLogger(PCSCReader.class);

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

            return cardReader;
        }
    }

    private PCSCReader() {}

    @Override
    public void connect() throws Exception
    {
        LOGGER.debug("Connecting to reader '" + reader.getName() + "'");

        // Establishes a connection to the card. If a connection has previously established using the
        // specified protocol, this method returns the same Card object as the previous call.
        card    = reader.connect("*");
        channel = card.getBasicChannel();

        LOGGER.debug("Card retrieved and channel opened");
    }

    @Override
    public void release() throws Exception
    {
        LOGGER.debug("Releasing reader '" + reader.getName() + "'");

        try
        {
            if (channel != null)
            {
                card.disconnect(false);
            }

            LOGGER.debug("Reader released");

        } catch (CardException | IllegalStateException ex) {
            LOGGER.error("Exception releasing reader");
            LOGGER.error(ex);

            throw ex;
        }

    }

    @Override
    public byte[] reset() throws Exception
    {
        try
        {
            // The reset method can be called from outside the test, so
            // the connection has to be made automatically.
            connect();

            return card.getATR().getBytes();

        } catch (Exception ex) {
            LOGGER.error("Exception resetting the card");
            LOGGER.error(ex);

            throw ex;
        }
    }

    @Override
    public ApduResponse transmit(ApduCommand apdu) throws Exception
    {
        ApduResponse apduResponse = null;

        if (channel != null)
        {
            ResponseAPDU response = channel.transmit(
                    new CommandAPDU(apdu.asByteArray()));

            apduResponse = new ApduResponse.Builder()
                    .withSw1((byte) response.getSW1())
                    .withSw2((byte) response.getSW2())
                    .withData(response.getData())
                    .build();
        }

        return apduResponse;
    }

    @Override
    public String getName()
    {
        return reader.getName();
    }
}
