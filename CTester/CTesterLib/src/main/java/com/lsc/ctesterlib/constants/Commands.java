package com.lsc.ctesterlib.constants;

/**
 * Class containing all the different classes.
 *
 * @author dma@logossmartcard.com
 */
public class Commands
{
    // Commands
    public static final byte VIRGINIZE             = (byte) 0x2E;
    public static final byte VISA_GET_HISTORY      = (byte) 0x4E;
    public static final byte INITIALIZE_UPDATE     = (byte) 0x50;
    public static final byte BACK_TO_BOOTLOADER    = (byte) 0x55;
    public static final byte EXTERNAL_AUTHENTICATE = (byte) 0x82;
    public static final byte SELECT                = (byte) 0xA4;
    public static final byte READ_BINARY           = (byte) 0xB0;
    public static final byte GET_RESPONSE          = (byte) 0xC0;
    public static final byte GET_DATA              = (byte) 0xCA;
    public static final byte STORE_DATA            = (byte) 0xE2;
    public static final byte INSTALL               = (byte) 0xE6;
    public static final byte PUT_KEY               = (byte) 0xD8;
    public static final byte PUT_DATA              = (byte) 0xDA;
}
