package com.hellofyc.base.util;

import android.support.annotation.NonNull;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created on 2015/9/7.
 *
 * @author Yucun Fang
 */
public class AESUtils {
    private static final boolean DEBUG = false;

    public static final String ALGORITHM_AES = "AES";

    /**
     * 创建密钥
     *
     * @param password 例如："0123456701234567" 128位 16*8 <br>
     *                 所有密钥长度不能超过16字符中文占两个。192 24； 256 32
     * @return SecretKeySpec 实例
     */
    private static SecretKeySpec generateKey(@NonNull String password) {
        byte[] data;
        StringBuilder sb = new StringBuilder();
        sb.append(password);
        while (sb.length() < 16) {
            sb.append("0");
        }
        if (sb.length() > 16) {
            sb.setLength(16);
        }
        try {
            data = sb.toString().getBytes("UTF-8");
            return new SecretKeySpec(data, ALGORITHM_AES);
        } catch (Exception e) {
            if (DEBUG)  FLog.e(e);
        }
        return null;
    }

    /**
     * 加密字节数据
     *
     * @param data  需要加密的字节数组
     * @param password 密钥 128 lt;16个字节 192 lt;24,256 lt;32个字节
     * @return 加密完后的字节数组
     */
    public static byte[] encrypt(@NonNull byte[] data, @NonNull String password) {
        try {
            SecretKeySpec key = generateKey(password);
            Cipher cipher = Cipher.getInstance(ALGORITHM_AES);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return null;
    }

    /**
     * 加密(结果为16进制字符串)
     *
     * @param dataString  要加密的字符串
     * @param password 密钥
     * @return 加密后的16进制字符串
     */
    public static String encrypt(String dataString, String password) {
        byte[] data = encrypt(dataString.getBytes(), password);
        if (data != null) {
            return ParseUtils.bytesToHexString(data);
        }
        return "";
    }

    public static byte[] decrypt(byte[] content, String password) {
        try {
            SecretKeySpec key = generateKey(password);
            Cipher cipher = Cipher.getInstance(ALGORITHM_AES);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(content);
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return null;
    }

    /**
     * 解密16进制的字符串为字符串 *
     */
    public static String decrypt(String dataString, String password) {
        byte[] data = ParseUtils.hexStringToBytes(dataString);
        data = decrypt(data, password);
        if (data != null) {
            try {
                return new String(data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                if (DEBUG) FLog.e(e);
            }
        }
        return null;
    }

}
