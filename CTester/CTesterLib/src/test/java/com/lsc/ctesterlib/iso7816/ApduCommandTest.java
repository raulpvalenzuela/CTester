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
public class ApduCommandTest
{
    public ApduCommandTest() {}

    @BeforeClass
    public static void setUpClass() {}

    @AfterClass
    public static void tearDownClass() {}

    @Before
    public void setUp() {}

    @After
    public void tearDown() {}

    /**
     * Test of asByteArray method, of class ApduCommand.
     */
    @Test
    public void testApduCreation()
    {
        System.out.println("Creating of APDUs");
        System.out.println("Case 1 commands");

        ApduCommand instance;
        byte[] expResult;
        byte[] result;

        // --- Case 1 command #1 --- //
        instance = new ApduCommand.Builder()
                            .withCLA((byte) 0x00)
                            .withINS((byte) 0x11)
                            .withP1((byte) 0x22)
                            .withP2((byte) 0x33)
                            .build();

        expResult = new byte[] { 0x00, 0x11, 0x22, 0x33, 0x00 };
        result = instance.asByteArray();

        assertArrayEquals(expResult, result);
        assertEquals(instance.getLe(), 0x00);
        assertEquals(instance.getLc(), 0x00);

        // --- Case 1 command #2 --- //
        instance = new ApduCommand(new byte[] { 0x00, 0x11, 0x22, 0x33, 0x00 });

        expResult = new byte[] { 0x00, 0x11, 0x22, 0x33, 0x00 };
        result = instance.asByteArray();
        assertArrayEquals(expResult, result);
        assertEquals(instance.getLe(), 0x00);
        assertEquals(instance.getLc(), 0x00);

        System.out.println("Case 2 commands");

        // --- Case 2 command #1 --- //
        instance = new ApduCommand.Builder()
                            .withCLA((byte) 0x00)
                            .withINS((byte) 0x11)
                            .withP1((byte) 0x22)
                            .withP2((byte) 0x33)
                            .withLe((byte) 0x44)
                            .build();

        expResult = new byte[] { 0x00, 0x11, 0x22, 0x33, 0x44 };
        result = instance.asByteArray();

        assertArrayEquals(expResult, result);
        assertEquals(instance.getLe(), 0x44);
        assertEquals(instance.getLc(), 0x00);

        // --- Case 2 command #2 --- //
        instance = new ApduCommand(new byte[] { 0x00, 0x11, 0x22, 0x33, 0x44 });

        expResult = new byte[] { 0x00, 0x11, 0x22, 0x33, 0x44 };
        result = instance.asByteArray();
        assertArrayEquals(expResult, result);
        assertEquals(instance.getLe(), 0x44);
        assertEquals(instance.getLc(), 0x00);

        // --- Case 3 command #1 --- //
        instance = new ApduCommand.Builder()
                            .withCLA((byte) 0x00)
                            .withINS((byte) 0x11)
                            .withP1((byte) 0x22)
                            .withP2((byte) 0x33)
                            .withData(new byte[] { 0x55, 0x66 })
                            .build();

        expResult = new byte[] { 0x00, 0x11, 0x22, 0x33, 0x02, 0x55, 0x66 };
        result = instance.asByteArray();

        assertArrayEquals(expResult, result);
        assertArrayEquals(instance.getData(), new byte[] { 0x55, 0x66 });
        assertEquals(instance.getLe(), 0x00);
        assertEquals(instance.getLc(), 0x02);

        // --- Case 3 command #2 --- //
        instance = new ApduCommand(new byte[] { 0x00, 0x11, 0x22, 0x33, 0x02, 0x55, 0x66 });

        expResult = new byte[] { 0x00, 0x11, 0x22, 0x33, 0x02, 0x55, 0x66 };
        result = instance.asByteArray();
        assertArrayEquals(expResult, result);
        assertArrayEquals(instance.getData(), new byte[] { 0x55, 0x66 });
        assertEquals(instance.getLe(), 0x00);
        assertEquals(instance.getLc(), 0x02);

        // --- Case 4 command #1 --- //
        instance = new ApduCommand.Builder()
                            .withCLA((byte) 0x00)
                            .withINS((byte) 0x11)
                            .withP1((byte) 0x22)
                            .withP2((byte) 0x33)
                            .withLe((byte) 0x44)
                            .withData(new byte[] { 0x55, 0x66 })
                            .build();

        expResult = new byte[] { 0x00, 0x11, 0x22, 0x33, 0x02, 0x55, 0x66 };
        result = instance.asByteArray();

        assertArrayEquals(expResult, result);
        assertArrayEquals(instance.getData(), new byte[] { 0x55, 0x66 });
        assertEquals(instance.getLe(), 0x00);
        assertEquals(instance.getLc(), 0x02);
    }
}
