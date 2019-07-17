package com.lsc.ctesterlib.utils;

import java.util.concurrent.TimeUnit;
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
    public static String fromByteArrayToString(final byte[] array)
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
    public static String separate(final String string, final int size)
    {
        if (size == 0)
        {
            return string;
        }

        return string
                .toUpperCase()
                .replace(" ", "")
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
    public static byte[] fromStringToByteArray(final String string) throws DecoderException
    {
        return Hex.decodeHex(string.replace(" ", "").toCharArray());
    }

    /**
     * Converts a string to a single byte.
     *
     * @param string: string to be converted to a byte.
     * @return single byte from the string.
     */
    public static byte stringToByte(String string)
    {
        return (byte) ((Character.digit(string.charAt(0), 16) << 4) +
                        Character.digit(string.charAt(1), 16));
    }

    /**
     * Formats an interval in milliseconds to hh, mm, ss
     *
     * @param interval: interval to be formatted.
     * @return String with the interval formatted as hh, mm, ss
     */
    public static String formatInterval(final long interval)
    {
        final long hr = TimeUnit.MILLISECONDS.toHours(
                interval);
        final long min = TimeUnit.MILLISECONDS.toMinutes(
                interval - TimeUnit.HOURS.toMillis(hr));
        final long sec = TimeUnit.MILLISECONDS.toSeconds(
                interval - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
        final long ms = TimeUnit.MILLISECONDS.toMillis(
                interval - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min) - TimeUnit.SECONDS.toMillis(sec));

        return String.format("%02d hours %02d minutes %02d.%03d seconds", hr, min, sec, ms);
    }
}
