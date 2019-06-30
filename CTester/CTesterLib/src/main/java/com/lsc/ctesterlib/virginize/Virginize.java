package com.lsc.ctesterlib.virginize;

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

    private Virginize() {}

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

        public Virginize build()
        {
            Virginize virginize = new Virginize();

            virginize.key = key;
            virginize.mode = mode;

            return virginize;
        }
    }
}
