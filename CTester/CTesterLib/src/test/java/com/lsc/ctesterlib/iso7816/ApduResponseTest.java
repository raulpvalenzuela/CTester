package com.lsc.ctesterlib.iso7816;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dma@logossmartcard.com
 */
public class ApduResponseTest
{
    public ApduResponseTest() {}

    @BeforeClass
    public static void setUpClass() {}

    @AfterClass
    public static void tearDownClass() {}

    @Before
    public void setUp() {}

    @After
    public void tearDown() {}

    /**
     * Test of checkSW method, of class ApduResponse.
     */
    @Test
    public void testCheckSW_byteArr()
    {
        System.out.println("checkSW (as byteArray)");

        boolean result;

        ApduResponse instance;

        // --- Test correct SW --- //
        byte[] sw = new byte[] { (byte) 0x90, (byte) 0x00 };
        instance = new ApduResponse.Builder()
                            .withSW1((byte) 0x90)
                            .withSW2((byte) 0x00)
                            .build();

        result = instance.checkSW(sw);
        assertEquals(true, result);

        // --- Test incorrect SW --- //
        instance = new ApduResponse.Builder()
                            .withSW1((byte) 0x00)
                            .withSW2((byte) 0x90)
                            .build();

        result = instance.checkSW(sw);
        assertEquals(false, result);
    }

    /**
     * Test of checkSW method, of class ApduResponse.
     */
    @Test
    public void testCheckSW_byte_byte()
    {
        System.out.println("checkSW (as byte byte)");

        boolean result;
        ApduResponse instance;

        // --- Test correct SW --- //
        byte sw1 = (byte) 0x90;
        byte sw2 = (byte) 0x00;
        instance = new ApduResponse.Builder()
                            .withSW1((byte) 0x90)
                            .withSW2((byte) 0x00)
                            .build();

        result = instance.checkSW(sw1, sw2);
        assertEquals(true, result);

        // --- Test incorrect SW --- //
        instance = new ApduResponse.Builder()
                            .withSW1((byte) 0x00)
                            .withSW2((byte) 0x90)
                            .build();

        result = instance.checkSW(sw1, sw2);
        assertEquals(false, result);
    }

    /**
     * Test of checkData method, of class ApduResponse.
     */
    @Test
    public void testCheckData_byteArr()
    {
        System.out.println("checkData (as byteArray)");

        byte[] data = new byte[] { 0x00, 0x11, 0x22 };

        // --- Test Correct data --- //
        ApduResponse instance = new ApduResponse.Builder()
                                        .withData(new byte[] { 0x00, 0x11, 0x22 })
                                        .build();
        assertEquals(true, instance.checkData(data));

        // --- Test Incorrect data --- //
        instance = new ApduResponse.Builder()
                            .withData(new byte[] { 0x22, 0x00, 0x22 })
                            .build();
        assertEquals(false, instance.checkData(data));
    }

    /**
     * Test of checkData method, of class ApduResponse.
     */
    @Test
    public void testCheckData_String()
    {
        System.out.println("checkData (as string)");

        String data;
        ApduResponse instance;

        instance = new ApduResponse.Builder()
                            .withData(new byte[] { 0x00, 0x11, 0x22, 0x33, 0x44, 0x55 })
                            .build();

        // --- Test Correct data --- //
        data = "00 11 22 33 44 55";
        assertEquals(true, instance.checkData(data));

        // --- Test Incorrect data --- //
        data = "00 00 22 33 44 55";
        assertEquals(false, instance.checkData(data));

        // --- Test Correct data --- //
        data = "?? 11 22 33 44 55";
        assertEquals(true, instance.checkData(data));

        // --- Test Correct data --- //
        data = "?? ?? ?? ?? ?? ??";
        assertEquals(true, instance.checkData(data));

        // --- Test Incorrect data --- //
        data = "?? ?? ?? 00 ?? ??";
        assertEquals(false, instance.checkData(data));
    }
}
