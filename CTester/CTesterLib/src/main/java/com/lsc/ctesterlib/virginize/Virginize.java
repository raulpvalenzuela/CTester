package com.lsc.ctesterlib.virginize;

import org.jpos.tlv.TLVList;

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

    private byte[] key;
    private MODE mode;
    private TLVList parameters;

    private Virginize() {}

    public static class Builder
    {
        private byte[] key;
        private MODE mode;

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

        public Virginize build()
        {
            Virginize virginize = new Virginize();

            virginize.key = key;
            virginize.mode = mode;

            return virginize;
        }
    }
}
