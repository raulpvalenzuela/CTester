package com.lsc.ctesterlib.utils;

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
public class ApduValidatorTest
{
    public ApduValidatorTest() {}

    @BeforeClass
    public static void setUpClass() {}

    @AfterClass
    public static void tearDownClass() {}

    @Before
    public void setUp() {}

    @After
    public void tearDown() {}

    /**
     * Test of isValid method, of class ApduValidator.
     */
    @Test
    public void testIsValid()
    {
        System.out.println("isValid");

        // --- Correct lengths --- //
        assertEquals(true, ApduValidator.isValid("00 00 00 00 00"));
        assertEquals(true, ApduValidator.isValid("00 00 00 00 00 00"));
        assertEquals(true, ApduValidator.isValid("0000000000"));
        // --- Incorrect lengths --- //
        assertEquals(false, ApduValidator.isValid(""));
        assertEquals(false, ApduValidator.isValid("00"));
        assertEquals(false, ApduValidator.isValid("0000"));
        // --- Incorrect values --- //
        assertEquals(false, ApduValidator.isValid("00112233GG"));
        assertEquals(false, ApduValidator.isValid("G"));
    }
}
