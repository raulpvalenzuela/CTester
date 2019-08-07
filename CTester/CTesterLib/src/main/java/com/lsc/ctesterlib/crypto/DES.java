package com.lsc.ctesterlib.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.log4j.Logger;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.engines.DESEngine;
import org.bouncycastle.crypto.macs.ISO9797Alg3Mac;
import org.bouncycastle.crypto.paddings.ISO7816d4Padding;
import org.bouncycastle.crypto.params.KeyParameter;

/**
 * Static class with the DES algorithm implementation.
 *
 * @author dma@logossmartcard.com
 */
public class DES
{
    // Internal logger
    private static final Logger LOGGER = Logger.getLogger(DES.class);

    private static final byte[] IV_ZEROS = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

    // Types of padding
    public enum PADDING
    {
        NO_PADDING,
        PKCS5,
        ISO9797_M2
    }

    // DES mode
    public enum MODE
    {
        ECB,
        CBC
    }

    // DES type
    public enum TYPE
    {
        SINGLE_DES,
        TRIPLE_DES
    }

    public byte[] encrypt(byte[] key, byte[] iv, byte[] data, TYPE type, MODE mode, PADDING padding) throws InvalidKeyException, BadPaddingException
    {
        byte[] encryptedText = null;

        // Sanity checks
        if (((data.length % 8) != 0) && (padding == PADDING.NO_PADDING))
        {
            throw new BadPaddingException("No padding added");
        }

        if (((type == TYPE.SINGLE_DES) && (key.length != 8)) ||
            ((type == TYPE.TRIPLE_DES) && ((key.length != 16) && (key.length != 24))))
        {
            throw new InvalidKeyException("Incorrect key length");
        }

        try
        {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = factory.generateSecret(new DESKeySpec(key));

            AlgorithmParameterSpec algParamSpec = new IvParameterSpec((iv == null) ? IV_ZEROS : iv);

            String algorithm = "DES/" + ((mode == MODE.CBC) ? "CBC/" : "ECB/");
            switch (padding)
            {
                case NO_PADDING:
                case ISO9797_M2:
                    algorithm += "NoPadding";
                    break;

                case PKCS5:
                    algorithm += "PKCS5Padding";
                    break;
            }

            Cipher encrypter = Cipher.getInstance(algorithm);
            encrypter.init(Cipher.ENCRYPT_MODE, secretKey, algParamSpec);

            encryptedText = encrypter.doFinal(data);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException |
                 InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException ex) {
            LOGGER.error("Exception encrypting (" + ex.getLocalizedMessage() +")");
        }

        return encryptedText;
    }

    public static byte[] getRetailMAC(byte[] key, byte[] data)
    {
        BlockCipher cipher = new DESEngine();
        Mac mac = new ISO9797Alg3Mac(cipher, 64, new ISO7816d4Padding());

        KeyParameter keyP = new KeyParameter(key);
        mac.init(keyP);
        mac.update(data, 0, data.length);

        byte[] out = new byte[8];

        mac.doFinal(out, 0);

        return out;
    }

    private static int addPadding(byte[] in, int inOff)
    {
        int added = (in.length - inOff);

        in[inOff] = (byte) 0x80;
        inOff++;

        while (inOff < in.length)
        {
            in[inOff] = 0;
            inOff++;
        }

        return added;
    }

    private static int padCount(byte[] in) throws InvalidCipherTextException
    {
        int count = in.length - 1;

        while ((count > 0) && (in[count] == 0))
        {
            count--;
        }

        if (in[count] != (byte) 0x80)
        {
            throw new InvalidCipherTextException("Padding block corrupted");
        }

        return in.length - count;
    }
}
