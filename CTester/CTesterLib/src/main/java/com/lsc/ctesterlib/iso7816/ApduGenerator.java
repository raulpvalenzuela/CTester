package com.lsc.ctesterlib.iso7816;

import static com.lsc.ctesterlib.constants.Commands.*;

/**
 * Class to create different kinds of commands transparently.
 *
 * @author dma@logossmartcard.com
 */
public class ApduGenerator
{
    /**
     * Builds and returns a SELECT command.
     *
     * @param p1: P1 byte.
     * @param p2: P2 byte.
     * @param aid: AID of the applet to be selected.
     * @return ApduCommand object.
     */
    public static ApduCommand createSelect(byte p1, byte p2, byte[] aid)
    {
        return new ApduCommand.Builder()
                .withCLA((byte) 0x00)
                .withINS(SELECT)
                .withP1(p1)
                .withP2(p2)
                .withData(aid)
                .build();
    }

    /**
     * Builds and returns a GET DATA command.
     *
     * @param p1: P1 byte.
     * @param p2: P2 byte.
     * @param le: length expected.
     * @return ApduCommand object.
     */
    public static ApduCommand createGetData(byte p1, byte p2, byte le)
    {
        return new ApduCommand.Builder()
                .withCLA((byte) 0x00)
                .withINS(GET_DATA)
                .withP1(p1)
                .withP2(p2)
                .withLe(le)
                .build();
    }
}
