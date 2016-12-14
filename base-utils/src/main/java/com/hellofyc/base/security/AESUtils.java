package com.hellofyc.base.security;

import android.support.annotation.NonNull;

import com.hellofyc.base.utils.FLog;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created on 2015/9/7.
 *
 * @author Yucun Fang
 */
public class AESUtils {
    private static final boolean DEBUG = false;

    public static final String ALGORITHM_AES = "AES";

    public static byte[] generateKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            return keyGenerator.generateKey().getEncoded();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] encrypt(@NonNull byte[] textBytes, @NonNull byte[] keyBytes) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            return cipher.doFinal(textBytes);
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return null;
    }

    public static byte[] decrypt(byte[] textBytes, byte[] keyBytes) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            return cipher.doFinal(textBytes);
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return null;
    }

}
