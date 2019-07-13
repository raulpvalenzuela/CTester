package com.lsc.ctesterlib.virginize;

import com.lsc.ctesterlib.utils.Formatter;

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

    /**
     * Constructor.
     *
     * @param tag tag byte of the TLV.
     * @param value array of bytes conforming the value of the TLV.
     */
   public VirginizeParameter(byte tag, byte[] value)
    {
        this(tag, "Unnamed", false, value);
    }

    /**
     * Constructor.
     *
     * @param tag tag byte of the TLV.
     * @param name name of the TLV. It is just informative.
     * @param value array of bytes conforming the value of the TLV.
     */
    public VirginizeParameter(byte tag, String name, byte[] value)
    {
        this(tag, name, false, value);
    }

    /**
     * Constructor.
     *
     * @param tag tag byte of the TLV.
     * @param name name of the TLV. It is just informative.
     * @param mac true if a MAC has to be calculated.
     * @param value array of bytes conforming the value of the TLV.
     */
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
    public boolean getMAC()
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

    @Override
    public String toString()
    {
        String parameter = this.name
                + "\n - Tag: " + Byte.toString(tag)
                + "\n - Value: " + Formatter.fromByteArrayToString(value)
                + "\n - " + ((mac) ? "MAC required" : "No MAC required")
                + "\n";

        return parameter;
    }
}
