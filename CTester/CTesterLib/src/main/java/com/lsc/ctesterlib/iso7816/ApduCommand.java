package com.lsc.ctesterlib.iso7816;

import com.lsc.ctesterlib.utils.Formatter;
import java.util.Arrays;

/**
 * Class that represents a command APDU.
 *
 * @author dma@logossmartcard.com
 */
public class ApduCommand
{
    // Offsets.
    public static final byte OFFSET_CLA   = 0;
    public static final byte OFFSET_INS   = 1;
    public static final byte OFFSET_P1    = 2;
    public static final byte OFFSET_P2    = 3;
    public static final byte OFFSET_LC    = 4;
    public static final byte OFFSET_CDATA = 5;

    // Fields.
    private byte cla;
    private byte ins;
    private byte p1;
    private byte p2;
    private byte lc;
    private byte le;
    private byte[] data;

    // Raw bytes.
    private byte[] command;

    private ApduCommand() {}

    private ApduCommand(byte cla, byte ins, byte p1, byte p2, byte le, byte[] data)
    {
        // Set all the bytes.
        this.cla  = cla;
        this.ins  = ins;
        this.p1   = p1;
        this.p2   = p2;

        this.lc   = (data.length == 0) ? 0 : (byte) data.length;
        this.le   = (data.length == 0) ? le : 0;

        this.data = data;

        // Create the command
        command = new byte[5 + data.length];

        command[OFFSET_CLA] = this.cla;
        command[OFFSET_INS] = this.ins;
        command[OFFSET_P1]  = this.p1;
        command[OFFSET_P2]  = this.p2;
        command[OFFSET_LC]  = (this.data.length == 0) ? this.le : this.lc;
        for (int i = 0; i < data.length; ++i)
        {
            command[i + OFFSET_CDATA] = data[i];
        }
    }

    /**
     * Constructor
     *
     * @param command array of bytes that conforms the command.
     */
    public ApduCommand(byte [] command)
    {
        assert(command.length >= 5);

        this.cla = command[OFFSET_CLA];
        this.ins = command[OFFSET_INS];
        this.p1  = command[OFFSET_P1];
        this.p2  = command[OFFSET_P2];

        if (command.length > 5)
        {
            this.lc = command[OFFSET_LC];
            this.le = 0;

            this.data = Arrays.copyOfRange(command, 4, this.lc + 5);
        }
        else
        {
            this.le = command[OFFSET_LC];
            this.lc = 0;

            this.data = new byte[] {};
        }
    }

    /**
     * Builder class.
     */
    public static class Builder
    {
        private byte cla;
        private byte ins;
        private byte p1;
        private byte p2;
        private byte le;
        private byte[] data;

        public Builder()
        {
            this.cla = 0;
            this.ins = 0;
            this.p1  = 0;
            this.p2  = 0;
            this.le  = 0;

            this.data = new byte[] {};
        }

        public Builder withCLA(byte cla)
        {
            this.cla = cla;

            return this;
        }

        public Builder withINS(byte ins)
        {
            this.ins = ins;

            return this;
        }

        public Builder withP1(byte p1)
        {
            this.p1 = p1;

            return this;
        }

        public Builder withP2(byte p2)
        {
            this.p2 = p2;

            return this;
        }

        public Builder withLe(byte le)
        {
            this.le = le;

            return this;
        }

        public Builder withData(byte[] data)
        {
            this.data = data;

            return this;
        }

        public ApduCommand build()
        {
            return new ApduCommand(cla, ins, p1, p2, le, data);
        }
    }

    /**
     * Returns class byte.
     *
     * @return class byte.
     */
    public byte getCLA() { return cla; }

    /**
     * Returns instruction byte.
     *
     * @return instruction byte.
     */
    public byte getINS() { return ins; }

    /**
     * Returns P1 byte.
     *
     * @return P1 byte.
     */
    public byte getP1() { return p1; }

    /**
     * Returns P2 byte.
     *
     * @return P2 byte.
     */
    public byte getP2() { return p2; }

    /**
     * Returns Lc byte.
     *
     * @return Lc byte.
     */
    public byte getLc() { return lc; }

    /**
     * Returns Le byte.
     *
     * @return Le byte.
     */
    public byte getLe() { return le; }

    /**
     * Returns the command  data.
     *
     * @return command data.
     */
    public byte[] getData() { return data; }

    /**
     * Returns the command as a byte array.
     *
     * @return command as a byte array.
     */
    public byte[] asByteArray()
    {
        return this.command;
    }

    @Override
    public String toString()
    {
        return Formatter.fromByteArrayToString(command);
    }
}
