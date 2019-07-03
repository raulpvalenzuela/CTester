package com.lsc.ctesterlib.virginize;

/**
 * Class that defines a virginize parameter.
 *
 * @author dma@logossmartcard.com
 */
public class VirginizeParameter
{
    private byte tag;
    private String name;
    private boolean mac;
    private byte[] value;

    public VirginizeParameter(byte tag, String name, byte[] value)
    {
        this(tag, name, false, value);
    }

    public VirginizeParameter(byte tag, String name, boolean mac, byte[] value)
    {
        this.tag   = tag;
        this.name  = name;
        this.mac   = mac;
        this.value = value;
    }

    /**
     * Returns the tag.
     *
     * @return the tag.
     */
    public byte getTag()
    {
        return tag;
    }

    /**
     * Returns the parameter name.
     *
     * @return the parameter name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns if the paramater requires a MAC.
     *
     * @return if the paramater requires a MAC.
     */
    public boolean isMac()
    {
        return mac;
    }

    /**
     * Returns the parameter value.
     *
     * @return the parameter value.
     */
    public byte[] getValue()
    {
        return value;
    }
}
