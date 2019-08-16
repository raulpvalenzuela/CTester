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
        try
        {
            System.out.println("Encrypt - DES_ECB_NoPadding");

            byte[] iv = null;
            byte[] key = Formatter.fromStringToByteArray("0123456789ABCDEF");
            byte[] data = Formatter.fromStringToByteArray("00112233445566778880000000000000");
            byte[] expResult = Formatter.fromStringToByteArray("CADB6782EE2B48239536CAFE22B9270E");

            TYPE type = TYPE.SINGLE_DES;
            MODE mode = MODE.ECB;
            PADDING padding = PADDING.NO_PADDING;
            DES instance = new DES();

            byte[] result = instance.encrypt(key, iv, data, type, mode, padding);

            System.out.println(Formatter.fromByteArrayToString(expResult));
            System.out.println(Formatter.fromByteArrayToString(result));

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
