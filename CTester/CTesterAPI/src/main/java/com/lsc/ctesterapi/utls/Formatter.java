package com.lsc.ctesterapi.utls;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author dma@logossmartcard.com
 */
public class Formatter
{
    /**
     * Formats an array of bytes into a string. Eg.
     *      array: {0x15, 0xFF, 0x5F}
     *      result: "15 FF 5F"
     *
     * @param array array to be formatted.
     * @return string formatted in blocks of two digits.
     */
    public static String fromByteArrayToString(byte[] array)
    {
        return Hex.encodeHexString(array)
                .toUpperCase()
                .replaceAll("(.{" + 2 + "})", "$1 ")
                .trim();
    }

    /**
     * Returns the string given separated in blocks of a specified size. Eg.
     *      string: "12Fc4A23
     *      size: 2
     *      result: "23 FC 4A 23"
     *
     * @param string: string to be formatted.
     * @param size: size of the block
     * @return string separated in blocks.
     */
    public static String separate(String string, int size)
    {
        return string
                .toUpperCase()
                .replaceAll(" ", "")
                .replaceAll("(.{" + size + "})", "$1 ")
                .trim();
    }

    /**
     * Returns an array of bytes from a string.
     *
     * @param string: string to be converted to an array of bytes.
     * @return array of bytes.
     * @throws org.apache.commons.codec.DecoderException if the string cannot be decoded.
     */
    public static byte[] fromStringToByteArray(String string) throws DecoderException
    {
        return Hex.decodeHex(string.replaceAll(" ", "").toCharArray());
    }
}
