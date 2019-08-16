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
    public void testEncrypt()
    {
        byte[] iv        = null;
        byte[] key       = null;
        byte[] data      = null;
        byte[] expResult = null;
        byte[] result    = null;

        try
        {
            System.out.println("DES_ENC_ECB_NoPadding");

            iv        = null;
            key       = Formatter.fromStringToByteArray("0123456789ABCDEF");
            data      = Formatter.fromStringToByteArray("00112233445566778880000000000000");
            expResult = Formatter.fromStringToByteArray("CADB6782EE2B48239536CAFE22B9270E");

            result = DES.encrypt(key, null, data, TYPE.SINGLE_DES, MODE.ECB, PADDING.NO_PADDING);

            assertArrayEquals(expResult, result);

            System.out.println("DES_ENC_ECB_Padding_ISO9797_M2");

            iv        = null;
            key       = Formatter.fromStringToByteArray("0123456789ABCDEF");
            data      = Formatter.fromStringToByteArray("001122334455667788");
            expResult = Formatter.fromStringToByteArray("CADB6782EE2B48239536CAFE22B9270E");

            result = DES.encrypt(key, null, data, TYPE.SINGLE_DES, MODE.ECB, PADDING.ISO9797_M2);

            assertArrayEquals(expResult, result);

            System.out.println(Formatter.fromByteArrayToString(expResult));
            System.out.println(Formatter.fromByteArrayToString(result));

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
