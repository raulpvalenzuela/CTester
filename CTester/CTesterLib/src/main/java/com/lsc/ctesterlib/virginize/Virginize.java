package com.lsc.ctesterlib.virginize;

import com.lsc.ctesterlib.persistence.Configuration;
import com.lsc.ctesterlib.utils.Formatter;
import org.apache.commons.codec.DecoderException;
import org.apache.log4j.Logger;
import org.jpos.tlv.TLVList;
import org.jpos.tlv.TLVList.TLVListBuilder;
import org.jpos.tlv.TLVMsg;

/**
 * Small framework to generate a Virginize command.
 *
 * @author dma@logossmartcard.com
 */
public class Virginize
{
    private static final Logger LOGGER = Logger.getLogger(Virginize.class);

    private static final byte CLA = (byte) 0x80;
    private static final byte INS = (byte) 0x2E;

    private static final byte ERASE_AND_CONFIGURE_P1 = 0x00;
    private static final byte ERASE_AND_CONFIGURE_P2 = 0x00;
    private static final byte UPDATE_ONLY_P1         = 0x01;
    private static final byte UPDATE_ONLY_P2         = 0x00;

    private static final String ERASE_AND_CONFIGURE_NAME  = "Erase and configure";
    private static final String UPDATE_ONLY_NAME          = "Update only";

    public enum MODE
    {
        ERASE_AND_CONFIGURE,
        UPDATE_ONLY
    }

    // Virginize key
    private byte[] key;
    // Virginize mode
    private MODE mode;
    // Virginize parameters
    private TLVList parameters;

    // Raw command
    private byte[] command;

    private Virginize() {}

    private Virginize(byte[] key, MODE mode, TLVList parameters)
    {
        this.key        = key;
        this.mode       = mode;
        this.parameters = parameters;
    }

    public static class Builder
    {
        private byte[] key;
        private MODE mode;
        private TLVList parameters;

        public Builder() {}

        public Builder withKey(byte[] key)
        {
            this.key = key;

            return this;
        }

        public Builder inMode(MODE mode)
        {
            this.mode = mode;

            return this;
        }

        public Builder withParameters(TLVMsg... parameters)
        {
            this.parameters = TLVListBuilder.createInstance().build();

            for (TLVMsg parameter : parameters)
            {
                this.parameters.append(parameter);
            }

            return this;
        }

        public Virginize buildFromConfig()
        {
            Virginize virginize = new Virginize();
            Configuration config = new Configuration();

            String field = null;

            try
            {
                field = config.getValueAsString(
                            Configuration.MODE
                          , ERASE_AND_CONFIGURE_NAME
                          , new String[] { Configuration.KEY });

                virginize.key = Formatter.fromStringToByteArray(field);

            } catch (DecoderException ex) {
                LOGGER.error("Exception parsing virginize key read from config.xml");
                LOGGER.error(" - Key: " + field);
            }

            return virginize;
        }

        public Virginize build()
        {
            return new Virginize(key, mode, parameters);
        }
    }

    /**
     * Returns the command as an array of bytes.
     *
     * @return virginize command.
     */
    public byte[] getCommand()
    {
        return this.command;
    }
}
