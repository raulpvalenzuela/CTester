package com.lsc.ctesterlib.iso7816;

import com.lsc.ctesterlib.utils.Formatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Class that represents a command APDU.
 *
 * @author dma@logossmartcard.com
 */
public class ApduCommand
{
    private enum TYPE
    {
        UNKNOWN,
        CASE_1,
        CASE_2,
        CASE_3,
        CASE_4;
    }

    // Offsets.
    private static final byte OFFSET_CLA   = 0;
    private static final byte OFFSET_INS   = 1;
    private static final byte OFFSET_P1    = 2;
    private static final byte OFFSET_P2    = 3;
    private static final byte OFFSET_LC    = 4;
    private static final byte OFFSET_CDATA = 5;

    // Commands
    private static final byte SELECT   = (byte) 0xA4;
    private static final byte GET_DATA = (byte) 0xCA;

    // Case 1 commands.
    private static final Byte[] CASE_ONE_COMMANDS = new Byte[] {

    };
    private static final Set<Byte> CASE_ONE_COMMANDS_SET = new HashSet<>(Arrays.asList(CASE_ONE_COMMANDS));

    // Case 2 commands.
    private static final Byte[] CASE_TWO_COMMANDS = new Byte[] {
        GET_DATA
    };
    private static final Set<Byte> CASE_TWO_COMMANDS_SET = new HashSet<>(Arrays.asList(CASE_TWO_COMMANDS));

    // Case 3 commands.
    private static final Byte[] CASE_THREE_COMMANDS = new Byte[] {
        SELECT
    };
    private static final Set<Byte> CASE_THREE_COMMANDS_SET = new HashSet<>(Arrays.asList(CASE_THREE_COMMANDS));

    // Case 4 commands.
    private static final Byte[] CASE_FOUR_COMMANDS = new Byte[] {

    };
    private static final Set<Byte> CASE_FOUR_COMMANDS_SET = new HashSet<>(Arrays.asList(CASE_FOUR_COMMANDS));

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
    // Type of the command.
    private TYPE type;

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

        // Determine the type.
        if (CASE_ONE_COMMANDS_SET.contains(this.cla))
        {
            this.type = TYPE.CASE_1;
        }
        else if (CASE_TWO_COMMANDS_SET.contains(this.cla))
        {
            this.type = TYPE.CASE_2;
        }
        else if (CASE_THREE_COMMANDS_SET.contains(this.cla))
        {
            this.type = TYPE.CASE_3;
        }
        else if (CASE_FOUR_COMMANDS_SET.contains(this.cla))
        {
            this.type = TYPE.CASE_4;
        }
        else
        {
            this.type = TYPE.UNKNOWN;
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

        this.command = command;

        // Determine the type.
        if ((this.le == 0) && (this.lc == 0))
        {
            this.type = TYPE.CASE_1;
        }
        else if ((this.lc == 0) && (this.le > 0))
        {
            this.type = TYPE.CASE_2;
        }
        else if ((this.lc > 0) && (this.le == 0))
        {
            this.type = TYPE.CASE_3;
        }
        else
        {
            this.type = TYPE.CASE_4;
        }
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
     * Returns the data.
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
