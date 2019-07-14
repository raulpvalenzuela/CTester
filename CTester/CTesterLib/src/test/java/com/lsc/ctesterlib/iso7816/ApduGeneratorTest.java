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
public class ApduGeneratorTest
{
    public ApduGeneratorTest() {}

    @BeforeClass
    public static void setUpClass() {}

    @AfterClass
    public static void tearDownClass() {}

    @Before
    public void setUp() {}

    @After
    public void tearDown() {}

    /**
     * Test of createSelect method, of class ApduGenerator.
     */
    @Test
    public void testCreateSelect()
    {
        System.out.println("createSelect");

        byte p1 = 0x11;
        byte p2 = 0x22;
        byte[] aid = new byte[] {
            (byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x30, (byte) 0x00
        };

        byte[] expResult = new byte[]
        {
              (byte) 0x00
            , (byte) 0xA4
            , (byte) 0x11
            , (byte) 0x22
            , (byte) aid.length
            , (byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x30, (byte) 0x00
        };

        byte[] result = ApduGenerator.createSelect(p1, p2, aid).asByteArray();

        assertArrayEquals(expResult, result);
    }

    /**
     * Test of createGetData method, of class ApduGenerator.
     */
    @Test
    public void testCreateGetData()
    {
        System.out.println("createGetData");

        byte p1 = (byte) 0x9F;
        byte p2 = (byte) 0x7F;
        byte le = (byte) 0x05;

        byte[] expResult = new byte[] { (byte) 0x00, (byte) 0xCA, (byte) 0x9F, (byte) 0x7F, (byte) 0x05 };
        byte[] result = ApduGenerator.createGetData(p1, p2, le).asByteArray();
        assertArrayEquals(expResult, result);
    }
}
