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
                .withClass((byte) 0x00)
                .withInstruction(SELECT)
                .withP1(p1)
                .withP2(p2)
                .withData(aid)
                .build();
    }
}
