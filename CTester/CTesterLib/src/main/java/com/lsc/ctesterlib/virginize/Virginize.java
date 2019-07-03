package com.lsc.ctesterlib.virginize;

import com.lsc.ctesterlib.persistence.Configuration;
import com.lsc.ctesterlib.utils.Formatter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.codec.DecoderException;
import org.apache.log4j.Logger;
import org.dom4j.Element;
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
    // Virginize parameters as TLVs
    private TLVList parametersTLV;
    // Virginize parameters as an object
    private List<VirginizeParameter> parameters;

    // Raw command
    private byte[] command;

    private Virginize() {}

    private Virginize(byte[] key, MODE mode, TLVList parameters)
    {
        this.key           = key;
        this.mode          = mode;
        this.parametersTLV = parameters;

        this.parameters    = null;

        // TODO construct command
    }

    private Virginize(byte[] key, MODE mode, List<VirginizeParameter> parameters)
    {
        this.key           = key;
        this.mode          = mode;
        this.parameters    = parameters;

        this.parametersTLV = null;

        // TODO construct command
    }

    public static class Builder
    {
        private byte[] key;
        private MODE mode;
        private TLVList parametersTLV;
        private List<VirginizeParameter> parameters;

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
            this.parametersTLV = TLVListBuilder.createInstance().build();

            for (TLVMsg parameter : parameters)
            {
                this.parametersTLV.append(parameter);
            }

            return this;
        }

        public Virginize buildFromConfig(MODE virginizeMode)
        {
            Virginize virginize = new Virginize();
            Configuration config = new Configuration();

            String virginizeKey = null;

            String modeName = (virginizeMode == MODE.ERASE_AND_CONFIGURE) ? ERASE_AND_CONFIGURE_NAME : UPDATE_ONLY_NAME;

            // Get the virginize key.
            try
            {
                virginizeKey = config.getValueAsString(
                            Configuration.MODE
                          , modeName
                          , new String[] { Configuration.KEY });

                if (virginizeKey == null)
                {
                    return null;
                }

                virginize.key = Formatter.fromStringToByteArray(virginizeKey);

            } catch (DecoderException ex) {
                LOGGER.error("Exception parsing virginize key read from config.xml");
                LOGGER.error(" - Key: " + virginizeKey);

                return null;
            }

            // Get the parameters.
            try
            {
                // First, get the elements from config.xml
                List<Element> elements = config.getValuesAsList(
                            Configuration.MODE
                          , modeName
                          , new String[] { Configuration.PARAMETERS , Configuration.PARAMETER});

                if (elements == null)
                {
                    return null;
                }

                // Populate the list creating VirginizeParameter objects.
                parameters = new ArrayList<>();
                for (Element parameter : elements)
                {
                    byte tag = Formatter.stringToByte(parameter.attributeValue(Configuration.TAG));
                    String name = parameter.attributeValue(Configuration.NAME);
                    boolean mac = parameter.attributeValue(Configuration.MAC).equalsIgnoreCase("true");
                    byte[] value = Formatter.fromStringToByteArray(parameter.getStringValue());

                    parameters.add(new VirginizeParameter(tag, name, mac, value));
                }

                virginize.parameters = parameters;

            } catch (DecoderException | NullPointerException ex) {
                LOGGER.error("Exception parsing virginize parameter from config.xml");

                return null;
            }

            return new Virginize();
        }

        public Virginize build()
        {
            return new Virginize(key, mode, parametersTLV);
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
