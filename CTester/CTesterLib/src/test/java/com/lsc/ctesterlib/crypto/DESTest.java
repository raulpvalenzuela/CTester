package com.lsc.ctesterlib.crypto;

import com.lsc.ctesterlib.crypto.DES.MODE;
import com.lsc.ctesterlib.crypto.DES.PADDING;
import com.lsc.ctesterlib.crypto.DES.TYPE;
import com.lsc.ctesterlib.utils.Formatter;
import java.security.InvalidKeyException;
import javax.crypto.BadPaddingException;
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
public class DESTest
{
    public DESTest() {}

    @BeforeClass
    public static void setUpClass() {}

    @AfterClass
    public static void tearDownClass() {}

    @Before
    public void setUp() {}

    @After
    public void tearDown() {}

    /**
     * Test of encrypt method, of class DES.
     */
    @Test
    public void testDES_ENC_ECB_NoPadding()
    {
        byte[] key       = null;
        byte[] data      = null;
        byte[] expResult = null;
        byte[] result    = null;

        try
        {
            // Single DES_ECB - No padding
            System.out.println("DES_ENC_ECB_NoPadding");

            key       = Formatter.fromStringToByteArray("0123456789ABCDEF");
            data      = Formatter.fromStringToByteArray("00112233445566778880000000000000");
            expResult = Formatter.fromStringToByteArray("CADB6782EE2B48239536CAFE22B9270E");

            result = DES.encrypt(key, null, data, TYPE.SINGLE_DES, MODE.ECB, PADDING.NO_PADDING);

            assertArrayEquals(expResult, result);

        } catch (InvalidKeyException | BadPaddingException | DecoderException ex) {
            fail("Exception occured when encrypting (" + ex.getMessage() + ")");
        }
    }

    /**
     * Test of encrypt method, of class DES.
     */
    @Test
    public void testDES_ENC_ECB_ISO9797_M2()
    {
        byte[] key       = null;
        byte[] data      = null;
        byte[] expResult = null;
        byte[] result    = null;

        try
        {
            // Single DES_ECB - ISO9797_M2
            System.out.println("DES_ENC_ECB_Padding_ISO9797_M2");

            key       = Formatter.fromStringToByteArray("0123456789ABCDEF");
            data      = Formatter.fromStringToByteArray("001122334455667788");
            expResult = Formatter.fromStringToByteArray("CADB6782EE2B48239536CAFE22B9270E");

            result = DES.encrypt(key, null, data, TYPE.SINGLE_DES, MODE.ECB, PADDING.ISO9797_M2);

            assertArrayEquals(expResult, result);

        } catch (InvalidKeyException | BadPaddingException | DecoderException ex) {
            fail("Exception occured when encrypting (" + ex.getMessage() + ")");
        }
    }

    /**
     * Test of encrypt method, of class DES.
     */
    @Test
    public void testDES_ENC_CBC_NoPadding()
    {
        byte[] key       = null;
        byte[] data      = null;
        byte[] expResult = null;
        byte[] result    = null;

        try
        {
            // Single DES_CBC - No padding
            System.out.println("DES_ENC_CBC_NoPadding");

            key       = Formatter.fromStringToByteArray("0123456789ABCDEF");
            data      = Formatter.fromStringToByteArray("00112233445566778880000000000000");
            expResult = Formatter.fromStringToByteArray("CADB6782EE2B4823FE1B8D9BAB9C8F8E");

            result = DES.encrypt(key, DES.IV_ZEROS, data, TYPE.SINGLE_DES, MODE.CBC, PADDING.NO_PADDING);

            assertArrayEquals(expResult, result);

        } catch (InvalidKeyException | BadPaddingException | DecoderException ex) {
            fail("Exception occured when encrypting (" + ex.getMessage() + ")");
        }
    }

    /**
     * Test of encrypt method, of class DES.
     */
    @Test
    public void testDES_ENC_CBC_ISO9797_M2()
    {
        byte[] key       = null;
        byte[] data      = null;
        byte[] expResult = null;
        byte[] result    = null;

        try
        {
            // Single DES_CBC - ISO9797_M2
            System.out.println("DES_ENC_CBC_Padding_ISO9797_M2");

            key       = Formatter.fromStringToByteArray("0123456789ABCDEF");
            data      = Formatter.fromStringToByteArray("001122334455667788");
            expResult = Formatter.fromStringToByteArray("CADB6782EE2B4823FE1B8D9BAB9C8F8E");

            result = DES.encrypt(key, DES.IV_ZEROS, data, TYPE.SINGLE_DES, MODE.CBC, PADDING.ISO9797_M2);

            assertArrayEquals(expResult, result);

        } catch (InvalidKeyException | BadPaddingException | DecoderException ex) {
            fail("Exception occured when encrypting (" + ex.getMessage() + ")");
        }
    }

    /**
     * Test of encrypt method, of class DES.
     */
    @Test
    public void test3DES_ENC_ECB_NoPadding()
    {
        byte[] key       = null;
        byte[] data      = null;
        byte[] expResult = null;
        byte[] result    = null;

        try
        {
            // 3DES_ECB - No padding
            System.out.println("3DES_ENC_ECB_NoPadding");

            key       = Formatter.fromStringToByteArray("0123456789ABCDEF00112233445566770123456789ABCDEF");
            data      = Formatter.fromStringToByteArray("00112233445566778880000000000000");
            expResult = Formatter.fromStringToByteArray("BD86704242F20EF50B2511B55DABD5C2");

            result = DES.encrypt(key, null, data, TYPE.TRIPLE_DES, MODE.ECB, PADDING.NO_PADDING);

            assertArrayEquals(expResult, result);

        } catch (InvalidKeyException | BadPaddingException | DecoderException ex) {
            fail("Exception occured when encrypting (" + ex.getMessage() + ")");
        }
    }

    /**
     * Test of encrypt method, of class DES.
     */
    @Test
    public void test3DES_ENC_ECB_ISO9797_M2()
    {
        byte[] key       = null;
        byte[] data      = null;
        byte[] expResult = null;
        byte[] result    = null;

        try
        {
            // 3DES_ECB - ISO9797_M2
            System.out.println("3DES_ENC_ECB_Padding_ISO9797_M2");

            key       = Formatter.fromStringToByteArray("0123456789ABCDEF00112233445566770123456789ABCDEF");
            data      = Formatter.fromStringToByteArray("001122334455667788");
            expResult = Formatter.fromStringToByteArray("BD86704242F20EF50B2511B55DABD5C2");

            result = DES.encrypt(key, null, data, TYPE.TRIPLE_DES, MODE.ECB, PADDING.ISO9797_M2);

            assertArrayEquals(expResult, result);

        } catch (InvalidKeyException | BadPaddingException | DecoderException ex) {
            fail("Exception occured when encrypting (" + ex.getMessage() + ")");
        }
    }

    /**
     * Test of encrypt method, of class DES.
     */
    @Test
    public void test3DES_ENC_CBC_NoPadding()
    {
        byte[] key       = null;
        byte[] data      = null;
        byte[] expResult = null;
        byte[] result    = null;

        try
        {
            // 3DES_CBC - No padding
            System.out.println("3DES_ENC_CBC_NoPadding");

            key       = Formatter.fromStringToByteArray("0123456789ABCDEF00112233445566770123456789ABCDEF");
            data      = Formatter.fromStringToByteArray("00112233445566778880000000000000");
            expResult = Formatter.fromStringToByteArray("BD86704242F20EF57B08B24AF0A5A20C");

            result = DES.encrypt(key, DES.IV_ZEROS, data, TYPE.TRIPLE_DES, MODE.CBC, PADDING.NO_PADDING);

            assertArrayEquals(expResult, result);

        } catch (InvalidKeyException | BadPaddingException | DecoderException ex) {
            fail("Exception occured when encrypting (" + ex.getMessage() + ")");
        }
    }

    /**
     * Test of encrypt method, of class DES.
     */
    @Test
    public void test3DES_ENC_CBC_ISO9797_M2()
    {
        byte[] key       = null;
        byte[] data      = null;
        byte[] expResult = null;
        byte[] result    = null;

        try
        {
            // 3DES_CBC - ISO9797_M2
            System.out.println("3DES_ENC_CBC_Padding_ISO9797_M2");

            key       = Formatter.fromStringToByteArray("0123456789ABCDEF00112233445566770123456789ABCDEF");
            data      = Formatter.fromStringToByteArray("001122334455667788");
            expResult = Formatter.fromStringToByteArray("BD86704242F20EF57B08B24AF0A5A20C");

            result = DES.encrypt(key, DES.IV_ZEROS, data, TYPE.TRIPLE_DES, MODE.CBC, PADDING.ISO9797_M2);

            assertArrayEquals(expResult, result);

        } catch (InvalidKeyException | BadPaddingException | DecoderException ex) {
            fail("Exception occured when encrypting (" + ex.getMessage() + ")");
        }
    }

    /**
     * Test of decrypt method, of class DES.
     */
    @Test
    public void testDES_DEC_ECB_ISO9797_M2()
    {
        byte[] key       = null;
        byte[] data      = null;
        byte[] expResult = null;
        byte[] result    = null;

        try
        {
            // Single DES_ECB - ISO9797_M2
            System.out.println("DES_DEC_ECB_Padding_ISO9797_M2");

            key       = Formatter.fromStringToByteArray("0123456789ABCDEF");
            data      = Formatter.fromStringToByteArray("CADB6782EE2B48239536CAFE22B9270E");
            expResult = Formatter.fromStringToByteArray("001122334455667788");

            result = DES.decrypt(key, null, data, TYPE.SINGLE_DES, MODE.ECB, PADDING.ISO9797_M2);

            assertArrayEquals(expResult, result);

        } catch (InvalidKeyException | BadPaddingException | DecoderException ex) {
            fail("Exception occured when encrypting (" + ex.getMessage() + ")");
        }
    }

    /**
     * Test of decrypt method, of class DES.
     */
    @Test
    public void testDES_DEC_CBC_ISO9797_M2()
    {
        byte[] key       = null;
        byte[] data      = null;
        byte[] expResult = null;
        byte[] result    = null;

        try
        {
            // Single DES_CBC - ISO9797_M2
            System.out.println("DES_DEC_CBC_Padding_ISO9797_M2");

            key       = Formatter.fromStringToByteArray("0123456789ABCDEF");
            data      = Formatter.fromStringToByteArray("CADB6782EE2B4823FE1B8D9BAB9C8F8E");
            expResult = Formatter.fromStringToByteArray("001122334455667788");

            result = DES.decrypt(key, DES.IV_ZEROS, data, TYPE.SINGLE_DES, MODE.CBC, PADDING.ISO9797_M2);

            assertArrayEquals(expResult, result);

        } catch (InvalidKeyException | BadPaddingException | DecoderException ex) {
            fail("Exception occured when encrypting (" + ex.getMessage() + ")");
        }
    }

    /**
     * Test of decrypt method, of class DES.
     */
    @Test
    public void test3DES_DEC_ECB_ISO9797_M2()
    {
        byte[] key       = null;
        byte[] data      = null;
        byte[] expResult = null;
        byte[] result    = null;

        try
        {
            // 3DES_ECB - ISO9797_M2
            System.out.println("DES_3DEC_ECB_Padding_ISO9797_M2");

            key       = Formatter.fromStringToByteArray("0123456789ABCDEF00112233445566770123456789ABCDEF");
            data      = Formatter.fromStringToByteArray("BD86704242F20EF50B2511B55DABD5C2");
            expResult = Formatter.fromStringToByteArray("001122334455667788");

            result = DES.decrypt(key, null, data, TYPE.TRIPLE_DES, MODE.ECB, PADDING.ISO9797_M2);

            assertArrayEquals(expResult, result);

        } catch (InvalidKeyException | BadPaddingException | DecoderException ex) {
            fail("Exception occured when encrypting (" + ex.getMessage() + ")");
        }
    }

    /**
     * Test of getRetailMAC method, of class DES.
     */
    @Test
    public void testGetRetailMAC()
    {
        System.out.println("getRetailMAC");
        byte[] key = null;
        byte[] data = null;
        byte[] expResult = null;
        byte[] result = DES.getRetailMAC(key, data);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
