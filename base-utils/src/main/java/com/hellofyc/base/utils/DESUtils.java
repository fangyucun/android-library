package com.hellofyc.base.utils;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created on 2015/9/7.
 *
 * @author Yucun Fang
 */
public class DESUtils {
    private static final boolean DEBUG = true;

    public static final String ALGORITHM_DES										 = "DES";
    public static final String ALGORITHM_DES_EDE									 = "DESede";

    public static final String TRANSFORMATION_DES									 = "DES";
    public static final String TRANSFORMATION_DES_CBC_PKCS5PADDING					 = "DES/CBC/PKCS5Padding";
    public static final String TRANSFORMATION_DESEDE_CBC_PKCS5PADDING				 = "DESede/CBC/PKCS5Padding";
    public static final String TRANSFORMATION_DESEDE_ECB_PKCS7PADDING				 = "DESede/ECB/PKCS7Padding";

    @SuppressLint("TrulyRandom")
    public static byte[] encryptWithDEScbc(@NonNull byte[] srcData, @NonNull String password) {
        byte[] passwordBytes;
        try {
            passwordBytes = password.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            passwordBytes = password.getBytes();
        }
        try {
            DESKeySpec desKey = new DESKeySpec(passwordBytes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM_DES);
            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION_DES_CBC_PKCS5PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, securekey, new IvParameterSpec(password.getBytes()));
            return cipher.doFinal(srcData);
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return null;
    }

    @SuppressLint("TrulyRandom")
    public static byte[] decryptWithDEScbc(@NonNull byte[] destData, @NonNull String password) {
        byte[] passwordBytes;
        try {
            passwordBytes = password.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            passwordBytes = password.getBytes();
        }
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(passwordBytes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM_DES);
            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION_DES_CBC_PKCS5PADDING);
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            return cipher.doFinal(destData);
        } catch (Exception e) {
            FLog.e(e);
        }
        return null;
    }

    public static String encryptWithDEScbc(@NonNull String srcString, @NonNull String password) {
        byte[] data;
        try {
            data = encryptWithDEScbc(srcString.getBytes("UTF-8"), password);
        } catch (UnsupportedEncodingException e) {
            data = encryptWithDEScbc(srcString.getBytes(), password);
        }
        if (data != null) {
            Base64Utils.encodeToString(data);
        }
        return "";
    }

    public static String decryptWithDEScbc(@NonNull String destString, @NonNull String password) {
        byte[] destData;
        try {
            destData = decryptWithDEScbc(destString.getBytes("UTF-8"), password);
        } catch (UnsupportedEncodingException e) {
            destData = decryptWithDEScbc(destString.getBytes(), password);
        }
        if (destData != null) {
            return new String(destData);
        }
        return "";
    }

    public static String encryptWithDES(@NonNull String srcString, @NonNull String password) {
        byte[] resultData;
        try {
            resultData = encryptWithDES(srcString.getBytes("UTF-8"), password);
        } catch (UnsupportedEncodingException e) {
            resultData = encryptWithDES(srcString.getBytes(), password);
        }
        if (resultData != null) {
            return ParseUtils.bytesToHexString(resultData);
        }
        return "";
    }

    public static String decryptWithDES(@NonNull String destString, @NonNull String password) {
        byte[] destData = ParseUtils.hexStringToBytes(destString);
        if (destData != null) {
            byte[] resultData = decryptWithDES(destData, password);
            if (resultData != null) {
                return new String(resultData);
            }
        }
        return null;
    }

    /**
     * DES加密
     */
    @SuppressLint("TrulyRandom")
    public static byte[] encryptWithDES(@NonNull byte[] srcData, @NonNull String password) {
        byte[] passwordBytes;
        try {
            passwordBytes = password.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            passwordBytes = password.getBytes();
        }
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(passwordBytes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM_DES);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION_DES);
            cipher.init(Cipher.ENCRYPT_MODE, keyFactory.generateSecret(desKey), random);
            return cipher.doFinal(srcData);
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return null;
    }

    /**
     * DES解密
     */
    public static byte[] decryptWithDES(@NonNull byte[] destData, @NonNull String password) {
        byte[] passwordBytes;
        try {
            passwordBytes = password.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            passwordBytes = password.getBytes();
        }
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(passwordBytes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM_DES);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION_DES);
            cipher.init(Cipher.DECRYPT_MODE, keyFactory.generateSecret(desKey), random);
            return cipher.doFinal(destData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encryptWithDESede(@NonNull String srcString,
                                         @NonNull String keyString,
                                         @NonNull String ivString) {

        byte[] data;
        try {
            data = encryptWithDESede(srcString.getBytes("UTF-8"), keyString.getBytes(), ivString.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            data = encryptWithDESede(srcString.getBytes(), keyString.getBytes(), ivString.getBytes());
        }
        if (data != null) {
            Base64Utils.encodeToString(data);
        }
        return "";
    }

    public static byte[] encryptWithDESede(@NonNull byte[] data, @NonNull byte[] keyData, @NonNull byte[] ivData) {
        if (keyData.length != 24) {
            throw new IllegalArgumentException("key's length must equal 24!");
        }

        if (ivData.length != 8) {
            throw new IllegalArgumentException("iv's length must equal 8!");
        }

        try {
            DESedeKeySpec spec = new DESedeKeySpec(keyData);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM_DES_EDE);
            SecretKey sk = keyFactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION_DESEDE_CBC_PKCS5PADDING);
            IvParameterSpec ivSpec = new IvParameterSpec(ivData);
            cipher.init(Cipher.ENCRYPT_MODE, sk, ivSpec);
            return cipher.doFinal(data);
        } catch (Exception e) {
            FLog.e(e);
        }
        return null;
    }

    public static String decryptWithDESede(@NonNull String encryptString,
                                           @NonNull String keyString,
                                           @NonNull String ivString) {

        byte[] encryptData = Base64Utils.decode(encryptString);

        byte[] data;
        try {
            data = decryptWithDESede(encryptData, keyString.getBytes("UTF-8"), ivString.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            data = decryptWithDESede(encryptData, keyString.getBytes(), ivString.getBytes());
        }
        if (data != null) {
            return new String(data);
        }
        return null;
    }

    public static byte[] decryptWithDESede(@NonNull byte[] data, @NonNull byte[] keyData, @NonNull byte[] ivData) {
        if (keyData.length != 24) {
            throw new IllegalArgumentException("key's length must equal 24!");
        }

        if (ivData.length != 8) {
            throw new IllegalArgumentException("iv's length must equal 8!");
        }

        try {
            DESedeKeySpec keySpec = new DESedeKeySpec(keyData);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM_DES_EDE);
            SecretKey sk = keyFactory.generateSecret(keySpec);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION_DESEDE_CBC_PKCS5PADDING);
            IvParameterSpec ivSpec = new IvParameterSpec(ivData);
            cipher.init(Cipher.DECRYPT_MODE, sk, ivSpec);
            return cipher.doFinal(data);
        } catch (Exception e) {
            FLog.e(e);
        }
        return null;
    }
}
