package com.lsc.ctesterlib.utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.DecoderException;
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
public class FormatterTest {

    public FormatterTest() {}

    @BeforeClass
    public static void setUpClass() {}

    @AfterClass
    public static void tearDownClass() {}

    @Before
    public void setUp() {}

    @After
    public void tearDown() {}

    /**
     * Test of fromByteArrayToString method, of class Formatter.
     */
    @Test
    public void testFromByteArrayToString()
    {
        System.out.println("fromByteArrayToString");

        byte[] array;
        String expResult;
        String result;

        array = new byte [] { 0x00, 0x11, 0x22, 0x33, 0x44, 0x55, (byte) 0xAA, (byte) 0xBB };
        expResult = "00 11 22 33 44 55 AA BB";
        result = Formatter.fromByteArrayToString(array);
        assertEquals(expResult, result);

        array = new byte [] { 0x00 };
        expResult = "00";
        result = Formatter.fromByteArrayToString(array);
        assertEquals(expResult, result);

        array = new byte [] { 0x00, 0x11 };
        expResult = "00 11";
        result = Formatter.fromByteArrayToString(array);
        assertEquals(expResult, result);
    }

    /**
     * Test of separate method, of class Formatter.
     */
    @Test
    public void testSeparate()
    {
        System.out.println("separate");

        String string;
        int size;
        String expResult;
        String result;

        string = "001122334455";
        size = 0;
        expResult = "001122334455";
        result = Formatter.separate(string, size);
        assertEquals(expResult, result);

        string = "001122334455";
        size = 1;
        expResult = "0 0 1 1 2 2 3 3 4 4 5 5";
        result = Formatter.separate(string, size);
        assertEquals(expResult, result);

        string = "001122334455";
        size = 2;
        expResult = "00 11 22 33 44 55";
        result = Formatter.separate(string, size);
        assertEquals(expResult, result);

        string = "001122334455";
        size = 3;
        expResult = "001 122 334 455";
        result = Formatter.separate(string, size);
        assertEquals(expResult, result);
    }

    /**
     * Test of fromStringToByteArray method, of class Formatter.
     */
    @Test
    public void testFromStringToByteArray()
    {
        System.out.println("fromStringToByteArray");

        String string;
        byte[] expResult;
        byte[] result;

        string = "0011223344";
        expResult = new byte[] { 0x00, 0x11, 0x22, 0x33, 0x44 };
        try
        {
            result = Formatter.fromStringToByteArray(string);
            assertArrayEquals(expResult, result);

        } catch (DecoderException ex) {
            fail(ex.getLocalizedMessage());
        }

        string = "00";
        expResult = new byte[] { 0x00 };
        try
        {
            result = Formatter.fromStringToByteArray(string);
            assertArrayEquals(expResult, result);

        } catch (DecoderException ex) {
            fail(ex.getLocalizedMessage());
        }

        string = "0011GG3344";
        try
        {
            Formatter.fromStringToByteArray(string);

        } catch (DecoderException ex) {
            // Expected to throw an exception
        }

        string = "0";
        try
        {
            Formatter.fromStringToByteArray(string);

        } catch (DecoderException ex) {
            // Expected to throw an exception
        }
    }

    /**
     * Test of stringToByte method, of class Formatter.
     */
    @Test
    public void testStringToByte()
    {
        System.out.println("stringToByte");

        String string = "A0";
        byte expResult = (byte) 0xA0;
        byte result = Formatter.stringToByte(string);
        assertEquals(expResult, result);
    }
}
