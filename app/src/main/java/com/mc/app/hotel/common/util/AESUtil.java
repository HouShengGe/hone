package com.mc.app.hotel.common.util;

import android.annotation.SuppressLint;
import android.util.Log;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
    private final String PASSWORD_LOCIAL = "EH76df#hj5232#dfg,.sle";/**/
    private final String PASSWORD_ALI = "ZuOMe1,38Dt.#rf7Sg2:!9f;*qM$vcTR35";//阿里云服务器使用
    private byte[] salt_locial = {6, 2, 0xA, 8, 9, 0xF, 0, 3, 5, 0xB, 1, 0xC, 7, 0xD, 4, 0xE};
    private byte[] salt_ali = {2, 9, 0xF, 0, 4, 0xE, 3, 0xA, 8, 5, 0xB, 1, 6, 0xC, 7, 0xD};//阿里云服务器使用
    private byte[] iv_locial = {3, 0xA, 1, 0xB, 5, 0x25, 0xF, 7, 9, 0x17, 1, 6, 47, 0xC, 0xD, 91};
    private byte[] iv_ali = {3, 0x24, 4, 0xA, 16, 0X51, 75, 0xC, 82, 0x16, 1, 7, 0xE, 0x71, 11, 41};//阿里云服务器使用

    private final String PASSWORD_KEY = PASSWORD_LOCIAL;

    /**
     * JAVA6支持以下任意一种算法 PBEWITHMD5ANDDES PBEWITHMD5ANDTRIPLEDES
     * PBEWITHSHAANDDESEDE PBEWITHSHA1ANDRC2_40 PBKDF2WITHHMACSHA1
     */
    private final String KEY_GENERATION_ALG = "PBKDF2WithHmacSHA1";

    private final int HASH_ITERATIONS = 10000;
    private final int KEY_LENGTH = 256;
    char[] humanPassphrase = PASSWORD_KEY.toCharArray();
    // must save this for next time we want the key
    private byte[] salt = salt_locial;
    private PBEKeySpec myKeyspec = new PBEKeySpec(humanPassphrase, salt, HASH_ITERATIONS, KEY_LENGTH);
    private final String CIPHERMODEPADDING = "AES/CBC/PKCS7Padding";
    private SecretKeyFactory keyfactory = null;
    private SecretKey sk = null;
    private SecretKeySpec skforAES = null;
    private byte[] iv =iv_locial;
    private IvParameterSpec IV;

    public AESUtil() {
        try {
            keyfactory = SecretKeyFactory.getInstance(KEY_GENERATION_ALG);
            sk = keyfactory.generateSecret(myKeyspec);

        } catch (NoSuchAlgorithmException nsae) {
            Log.e("AESdemo", "no key factory support for PBEWITHSHAANDTWOFISH-CBC");
        } catch (InvalidKeySpecException ikse) {
            Log.e("AESdemo", "invalid key spec for PBEWITHSHAANDTWOFISH-CBC");
        }
        byte[] skAsByteArray = sk.getEncoded();

        skforAES = new SecretKeySpec(skAsByteArray, "AES");

        IV = new IvParameterSpec(iv);
    }

    public String encrypt(String content) {
        try {
            byte[] plaintext = content.getBytes("UTF8");
            byte[] ciphertext = encrypt(CIPHERMODEPADDING, skforAES, IV, plaintext);
            String base64_ciphertext = Base64Encoder.encode(ciphertext);
            return base64_ciphertext;
        } catch (Exception e) {
        }
        return null;
    }

    public String decrypt(String ciphertext_base64) {
        byte[] s = Base64Decoder.decodeToBytes(ciphertext_base64);
        String decrypted = new String(decrypt(CIPHERMODEPADDING, skforAES, IV, s));
        return decrypted;
    }


    @SuppressLint("TrulyRandom")
    private byte[] encrypt(String cmp, SecretKey sk, IvParameterSpec IV, byte[] msg) {
        try {
            Cipher c = Cipher.getInstance(cmp);
            c.init(Cipher.ENCRYPT_MODE, sk, IV);
            return c.doFinal(msg);
        } catch (NoSuchAlgorithmException nsae) {
            Log.e("AESdemo", "no cipher getinstance support for " + cmp);
        } catch (NoSuchPaddingException nspe) {
            Log.e("AESdemo", "no cipher getinstance support for padding " + cmp);
        } catch (InvalidKeyException e) {
            Log.e("AESdemo", "invalid key exception");
        } catch (InvalidAlgorithmParameterException e) {
            Log.e("AESdemo", "invalid algorithm parameter exception");
        } catch (IllegalBlockSizeException e) {
            Log.e("AESdemo", "illegal block size exception");
        } catch (BadPaddingException e) {
            Log.e("AESdemo", "bad padding exception");
        }
        return null;
    }

    private byte[] decrypt(String cmp, SecretKey sk, IvParameterSpec IV, byte[] ciphertext) {
        try {
            Cipher c = Cipher.getInstance(cmp);
            c.init(Cipher.DECRYPT_MODE, sk, IV);
            return c.doFinal(ciphertext);
        } catch (NoSuchAlgorithmException nsae) {
            Log.e("AESdemo", "no cipher getinstance support for " + cmp);
        } catch (NoSuchPaddingException nspe) {
            Log.e("AESdemo", "no cipher getinstance support for padding " + cmp);
        } catch (InvalidKeyException e) {
            Log.e("AESdemo", "invalid key exception");
        } catch (InvalidAlgorithmParameterException e) {
            Log.e("AESdemo", "invalid algorithm parameter exception");
        } catch (IllegalBlockSizeException e) {
            Log.e("AESdemo", "illegal block size exception");
        } catch (BadPaddingException e) {
            Log.e("AESdemo", "bad padding exception");
            e.printStackTrace();
        }
        return null;
    }

    public static AESUtil instance;

    public static AESUtil getInstance() {
        if (instance == null) {
            instance = new AESUtil();
        }
        return instance;
    }


}