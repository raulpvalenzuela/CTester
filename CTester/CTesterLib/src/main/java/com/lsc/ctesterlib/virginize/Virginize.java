package com.lsc.ctesterlib.virginize;

import com.lsc.ctesterlib.iso7816.ApduCommand;
import com.lsc.ctesterlib.persistence.Configuration;
import com.lsc.ctesterlib.utils.Formatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.codec.DecoderException;
import org.apache.log4j.Logger;
import org.dom4j.Element;

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

    // Raw command
    private byte[] command;

    private Virginize() {}

    private Virginize(byte[] key, MODE mode, List<VirginizeParameter> parameters)
    {
        // We don't know yet the total size we'll use a list that will be converted to an array.
        List<Byte> temp = new ArrayList<>();

        // Class and instruction bytes
        temp.add(CLA);
        temp.add(INS);

        // P1 and P2 bytes
        temp.add((mode == MODE.ERASE_AND_CONFIGURE) ? ERASE_AND_CONFIGURE_P1 : UPDATE_ONLY_P1);
        temp.add((mode == MODE.ERASE_AND_CONFIGURE) ? ERASE_AND_CONFIGURE_P2 : UPDATE_ONLY_P2);

        // Dummy length byte
        temp.add((byte) 0);

        // Virginize key
        for (int i = 0; i < key.length; ++i)
        {
            temp.add(key[i]);
        }

        // Virginize parameters
        parameters.forEach((parameter) ->
        {
            temp.add(parameter.getTag());
            temp.add((byte) parameter.getValue().length);

            for (int i = 0; i < parameter.getValue().length; ++i)
            {
                temp.add(parameter.getValue()[i]);
            }
        });

        // With the list populated, copy it to the array.
        this.command = new byte[temp.size()];
        for (int i = 0; i < temp.size(); ++i)
        {
            this.command[i] = temp.get(i);
        }

        // Calculate the length
        this.command[ApduCommand.OFFSET_LC] = (byte) (temp.size() - 5);
    }

    /**
     * Builder class.
     */
    public static class Builder
    {
        private byte[] key;
        private MODE mode;
        private List<VirginizeParameter> parameters;

        public Builder() {}

        /**
         * Sets the virginize key.
         *
         * @param key: virginize key.
         * @return builder reference.
         */
        public Builder withKey(byte[] key)
        {
            this.key = key;

            return this;
        }

        /**
         * Sets the virginize mode.
         *
         * @param mode: virginize mode.
         * @return buidler reference.
         */
        public Builder inMode(MODE mode)
        {
            this.mode = mode;

            return this;
        }

        /**
         * Sets the virginize parameters.
         *
         * @param parameters: virginize parameters.
         * @return builder reference.
         */
        public Builder withParameters(VirginizeParameter... parameters)
        {
            this.parameters = Arrays.asList(parameters);

            return this;
        }

        /**
         * Builds a virginize command from the config.xml file given the virginize mode.
         *
         * @param virginizeMode: virginize mode.
         * @return virginize command instance. Null if there is an error constructing it.
         */
        public Virginize buildFromConfig(MODE virginizeMode)
        {
            Configuration config = new Configuration();
            String virginizeKey = null;

            mode = virginizeMode;

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

                key = Formatter.fromStringToByteArray(virginizeKey);

            } catch (DecoderException ex) {
                LOGGER.error("Exception parsing virginize key read from config.xml (" + ex + ")");
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
                    byte tag     = Formatter.stringToByte(parameter.attributeValue(Configuration.TAG));
                    byte[] value = Formatter.fromStringToByteArray(parameter.getStringValue());
                    boolean mac  = parameter.attributeValue(Configuration.MAC).equalsIgnoreCase("true");
                    String name  = parameter.attributeValue(Configuration.NAME);

                    VirginizeParameter virginizeParameter = new VirginizeParameter(tag, name, mac, value);

                    parameters.add(virginizeParameter);
                }

            } catch (DecoderException | NullPointerException ex) {
                LOGGER.error("Exception parsing virginize parameter from config.xml (" + ex + ")");

                return null;
            }

            return new Virginize(key, mode, parameters);
        }

        public Virginize build()
        {
            if ((key == null) || (parameters == null))
            {
                return null;
            }

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
