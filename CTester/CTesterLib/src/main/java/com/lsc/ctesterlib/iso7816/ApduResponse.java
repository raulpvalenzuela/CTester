package com.lsc.ctesterlib.iso7816;

import com.lsc.ctesterlib.utils.Formatter;
import java.util.Arrays;

/**
 * Class that represents a response APDU.
 *
 * @author dma@logossmartcard.com
 */
public class ApduResponse
{
    private byte[] sw;
    private byte[] data;

    private ApduResponse()
    {
        sw = new byte[2];
    }

    /**
     * Builder class.
     */
    public static class Builder
    {
        private byte[] sw;
        private byte[] data;

        public Builder()
        {
            sw = new byte[2];
        }

        public Builder withSW1(byte sw1)
        {
            sw[0] = sw1;

            return this;
        }

        public Builder withSW2(byte sw2)
        {
            sw[1] = sw2;

            return this;
        }

        public Builder withData(byte[] data)
        {
            this.data = data;

            return this;
        }

        public ApduResponse build()
        {
            ApduResponse apdu = new ApduResponse();

            apdu.sw   = this.sw;
            apdu.data = this.data;

            return apdu;
        }
    }

    /**
     * Returns the status word.
     *
     * @return status word.
     */
    public byte[] getSW() { return sw; }

    /**
     * Returns the data, null if there's no data.
     *
     * @return body of the response. Null if there's none.
     */
    public byte[] getData() { return data; }

    /**
     * Compares the Status Word in the command received with
     * the one received as parameter.
     *
     * @param sw: Status Word to be compared.
     * @return true if both Stats Words are equal.
     */
    public boolean checkSW(byte[] sw)
    {
        if ((sw == null) || (sw.length == 0))
        {
            return false;
        }

        return ((this.sw[0] == sw[0]) && (this.sw[1] == sw[1]));
    }

    /**
     * Compares the Status Word in the command received with
     * the one received as parameter.
     *
     * @param sw1: first byte of the Status Word to be compared.
     * @param sw2: second byte of the Status Word to be compared.
     * @return true if both Stats Words are equal.
     */
    public boolean checkSW(byte sw1, byte sw2)
    {
        return ((this.sw[0] == sw1) && (this.sw[1] == sw2));
    }

    /**
     * Compares the data received in the command with the
     * one received as parameter.
     *
     * @param data: data to be compared.
     * @return true if they are equal.
     */
    public boolean checkData(byte[] data)
    {
        return Arrays.equals(this.data, data);
    }

    /**
     * Compares the data received in the command with the
     * one received as parameter. This version receives a string that
     * may contain "??" as a wildcard.
     *
     * @param data: data to be compared.
     * @return true if they are equal.
     */
    public boolean checkData(final String data)
    {
        String received = Formatter.fromByteArrayToString(this.data).replace(" ", "");
        String expected = data.replace(" ", "");

        if (expected.contains("?"))
        {
            if (received.length() != expected.length())
            {
                return false;
            }

            for (int i = 0; i < expected.length(); ++i)
            {
                if (expected.charAt(i) != '?')
                {
                    if (expected.charAt(i) != received.charAt(i))
                    {
                        return false;
                    }
                }
            }

            return true;
        }

        return expected.equals(received);
    }

    @Override
    public String toString()
    {
        String swStr = Formatter.fromByteArrayToString(this.sw);
        String dataStr = "";

        if ((this.data != null) && (this.data.length > 0))
        {
            dataStr = Formatter.fromByteArrayToString(this.data) + " ";
        }

        return dataStr + swStr;
    }
}
