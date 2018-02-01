package com.cwy.xxs.util;

import sun.misc.BASE64Decoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

public class WXDecrypt {

    private final static BASE64Decoder DECODER = new BASE64Decoder();

    public static byte[] decrypt(String sessionKey,String iv, String encryptedData) {
        byte[] encrypData = new byte[0];
        byte[] ivData = new byte[0];
        byte[] sessionKeyDE = new byte[0];
        try {
            encrypData = DECODER.decodeBuffer(encryptedData);
            ivData = DECODER.decodeBuffer(iv);
            sessionKeyDE = DECODER.decodeBuffer(sessionKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivData);
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        }
        SecretKeySpec keySpec = new SecretKeySpec(sessionKeyDE, "AES");
        try {
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return null;
        }
        try {
            return cipher.doFinal(encrypData);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
