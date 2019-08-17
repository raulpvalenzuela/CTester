package com.lsc.ctesterlib.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.log4j.Logger;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.engines.DESEngine;
import org.bouncycastle.crypto.macs.ISO9797Alg3Mac;
import org.bouncycastle.crypto.paddings.ISO7816d4Padding;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Arrays;

/**
 * Static class with the DES algorithm implementation. All this algorithms are supported:
 *  - DES
 *  - 3DES two keys.
 *  - 3DES three keys.
 *
 *  - Retail MAC (ISO 9797 Algorithm 3 MAC)
 *
 * @author dma@logossmartcard.com
 */
public class DES
{
    // Internal logger
    private static final Logger LOGGER = Logger.getLogger(DES.class);

    public static final byte[] IV_ZEROS = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

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

    /**
     * Encrypts data using DES or 3DES.
     *
     * @param key: 8-byte key if single DES, 16 or 24-byte key if 3DES.
     * @param iv: initialization vector, if it's null and in CBC mode, zeroes will be used. Ignored argument if in ECB mode.
     * @param data: data to be encrypted.
     * @param type: Single or triple DES.
     * @param mode: ECB or CBC.
     * @param padding: type of padding, none if it's already padded.
     * @return array containing the input data encrypted.
     * @throws InvalidKeyException
     *                    thrown if in single DES an 8-byte key is not specified. Also if in triple DES and the key is not 16 or 24 bytes.
     * @throws BadPaddingException
     *                    if no padding is specified and the data is not multiple of 8.
     */
    public static byte[] encrypt(
              byte[] key
            , byte[] iv
            , byte[] data
            , TYPE type
            , MODE mode
            , PADDING padding) throws InvalidKeyException, BadPaddingException
    {
        byte[] encryptedText = null;

        // Sanity checks
        if (((data.length % 8) != 0) && (padding == PADDING.NO_PADDING))
        {
            throw new BadPaddingException("No padding added");
        }

        if (((type == TYPE.SINGLE_DES) && (key.length != 8)) ||
            ((type == TYPE.TRIPLE_DES) && (key.length != 24)))
        {
            throw new InvalidKeyException("Incorrect key size");
        }

        try
        {
            SecretKey secretKey = new SecretKeySpec(key, (type == TYPE.SINGLE_DES) ? "DES" : "DESede");

            AlgorithmParameterSpec algParamSpec = new IvParameterSpec((iv == null) ? IV_ZEROS : iv);
            Cipher encrypter = getEngine(type, mode, padding);

            // Add padding manually
            if (padding == PADDING.ISO9797_M2)
            {
                data = pad(data);
            }

            if (mode == MODE.ECB)
            {
                encrypter.init(Cipher.ENCRYPT_MODE, secretKey);
            }
            else
            {
                encrypter.init(Cipher.ENCRYPT_MODE, secretKey, algParamSpec);
            }

            encryptedText = encrypter.doFinal(data);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                 InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException ex) {
            LOGGER.error("Exception encrypting (" + ex.getLocalizedMessage() +")");
        }

        return encryptedText;
    }

    /**
     * Decrypts data using DES or 3DES.
     *
     * @param key: 8-byte key if single DES, 16 or 24-byte key if 3DES.
     * @param iv: initialization vector, if it's null and in CBC mode, zeroes will be used. Ignored argument if in ECB mode.
     * @param data: data to be encrypted.
     * @param type: Single or triple DES.
     * @param mode: ECB or CBC.
     * @param padding: type of padding.
     * @return array containing the input data encrypted.
     * @throws InvalidKeyException
     *                    thrown if in single DES an 8-byte key is not specified. Also if in triple DES and the key is not 16 or 24 bytes.
     * @throws BadPaddingException
     *                    if no padding is specified and the data is not multiple of 8.
     */
    public static byte[] decrypt(
              byte[] key
            , byte[] iv
            , byte[] data
            , TYPE type
            , MODE mode
            , PADDING padding) throws InvalidKeyException, BadPaddingException
    {
        byte[] decryptedText = null;

        // Sanity checks
        if (padding == PADDING.NO_PADDING)
        {
            throw new BadPaddingException("No padding specified");
        }

        if (((type == TYPE.SINGLE_DES) && (key.length != 8)) ||
            ((type == TYPE.TRIPLE_DES) && ((key.length != 16) && (key.length != 24))))
        {
            throw new InvalidKeyException("Incorrect key length");
        }

        try
        {
            SecretKey secretKey = new SecretKeySpec(key, (type == TYPE.SINGLE_DES) ? "DES" : "DESede");

            AlgorithmParameterSpec algParamSpec = new IvParameterSpec((iv == null) ? IV_ZEROS : iv);
            Cipher encrypter = getEngine(type, mode, padding);

            if (mode == MODE.ECB)
            {
                encrypter.init(Cipher.DECRYPT_MODE, secretKey);
            }
            else
            {
                encrypter.init(Cipher.DECRYPT_MODE, secretKey, algParamSpec);
            }

            decryptedText = encrypter.doFinal(data);

            // Remove padding manually
            if (padding == PADDING.ISO9797_M2)
            {
                decryptedText = unpad(decryptedText);
            }

        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                 InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | InvalidCipherTextException ex) {
            LOGGER.error("Exception encrypting (" + ex.getLocalizedMessage() +")");
        }

        return decryptedText;
    }

    /**
     * Implementation of the ISO-9797 Algorithm 3 MAC.
     *
     * @param key: key to be used.
     * @param data: data to be signed.
     * @return 8-byte MAC of the data.
     */
    public static byte[] calculateRetailMAC(byte[] key, byte[] data)
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

    /**
     * Returns
     */
    private static Cipher getEngine(
              TYPE type
            , MODE mode
            , PADDING padding) throws NoSuchAlgorithmException, NoSuchPaddingException
    {
        String algorithm =
                ((type == TYPE.SINGLE_DES) ? "DES/" : "DESede/") +
                ((mode == MODE.CBC)        ? "CBC/" : "ECB/");

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

        return Cipher.getInstance(algorithm);
    }

    /**
     * Adds padding to the input data.
     */
    private static byte[] pad(byte[] in)
    {
        int paddingBytes = 8 - (in.length % 8);
        byte[] padding = new byte[(paddingBytes == 0) ? 1 : paddingBytes];

        padding[0] = (byte) 0x80;

        return Arrays.concatenate(in, padding);
    }

    /**
     * Removes the padding.
     */
    private static byte[] unpad(byte[] in) throws InvalidCipherTextException
    {
        int count = padCount(in);

        return Arrays.copyOfRange(in, 0, in.length - count);
    }

    /**
     * Returns the number of bytes of padding.
     */
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
