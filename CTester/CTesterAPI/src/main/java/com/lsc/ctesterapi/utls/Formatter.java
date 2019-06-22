package com.lsc.ctesterapi.utls;

import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author dma@logossmartcard.com
 */
public class Formatter
{
    /**
     * Formats an array of bytes into a string. Eg.
     *      {0x15, 0xFF, 0x5F} = "15 FF 5F"
     *
     * @param array array to be formatted.
     * @return string formatted in blocks of two digits.
     */
    public static String fromByteArrayToString(byte[] array)
    {
        return Hex.encodeHexString(array)
                .toUpperCase().replaceAll("(.{" + 2 + "})", "$1 ").trim();
    }
}
