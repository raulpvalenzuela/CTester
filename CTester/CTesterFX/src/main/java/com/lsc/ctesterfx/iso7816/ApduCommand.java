package com.lsc.ctesterfx.iso7816;

import java.util.Arrays;

/**
 * Class that represents a command APDU.
 *
 * @author dma@logossmartcard.com
 */
public class ApduCommand
{
    private byte cla;
    private byte ins;
    private byte p1;
    private byte p2;
    private byte lc;
    private byte le;
    private byte[] data;

    private byte[] command;

    private ApduCommand() {}

    private ApduCommand(byte cla, byte ins, byte p1, byte p2, byte le, byte[] data)
    {
        this.cla  = cla;
        this.ins  = ins;
        this.p1   = p1;
        this.p2   = p2;

        this.lc   = (byte) data.length;
        this.le   = le;

        this.data = data;
    }

    /**
     * Constructor
     *
     * @param command array of bytes that conforms the command.
     */
    public ApduCommand(byte [] command)
    {
        assert(command.length >= 5);

        this.cla = command[0];
        this.ins = command[1];
        this.p1  = command[2];
        this.p2  = command[3];
        this.lc  = command[4];

        this.data = Arrays.copyOfRange(command, 4, this.lc + 5);

        this.command = command;
    }

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

        public Builder withClass(byte cla)
        {
            this.cla = cla;

            return this;
        }

        public Builder withInstruction(byte ins)
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
    public byte getCla() { return cla; }

    /**
     * Returns instruction byte.
     *
     * @return instruction byte.
     */
    public byte getIns() { return ins; }

    /**
     * Returns P1 byte.
     *
     * @return P1 byte.
     */
    public byte getP1() { return p1;  }

    /**
     * Returns P2 byte.
     *
     * @return P2 byte.
     */
    public byte getP2() { return p2;  }

    /**
     * Returns Lc byte.
     *
     * @return Lc byte.
     */
    public byte getLc() { return lc;  }

    /**
     * Returns Le byte.
     *
     * @return Le byte.
     */
    public byte getLe() { return le;  }

    /**
     * Returns the data.
     *
     * @return command data.
     */
    public byte[] getData() { return data;  }

    /**
     * Sets the class byte.
     *
     * @param cla class byte to be set.
     */
    public void setCla(byte cla) { this.cla = cla; }

    /**
     * Sets the instruction byte.
     *
     * @param ins instruction byte to be set.
     */
    public void setIns(byte ins) { this.ins = ins; }

    /**
     * Sets the P1 byte.
     *
     * @param p1 P1 byte to be set.
     */
    public void setP1(byte p1) { this.p1  = p1;  }

    /**
     * Sets the P2 byte.
     *
     * @param p2 P2 byte to be set.
     */
    public void setP2(byte p2) { this.p2  = p2;  }

    /**
     * Sets the Lc byte.
     *
     * @param lc Lc byte to be set.
     */
    public void setLc(byte lc) { this.lc  = lc;  }

    /**
     * Sets the Le byte.
     *
     * @param le Le byte to be set.
     */
    public void setLe(byte le) { this.le  = le;  }

    /**
     * Sets the command data.
     *
     * @param data command data to be set.
     */
    public void setData(byte[] data) { this.data  = data;  }

    /**
     * Returns the command formatted as a String.
     *
     * @return command formatted as a String.
     */
    public String getCommandAsString() { return Arrays.toString(this.command); }

    /**
     * Returns the command formatted as an array of bytes.
     *
     * @return command formatted as an array of bytes.
     */
    public byte[] getCommandAsArray() { return this.command; }
}
