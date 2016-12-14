package com.hellofyc.base.security;

import android.support.annotation.NonNull;

import com.hellofyc.base.utils.IoUtils;
import com.hellofyc.base.utils.ParseUtils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created on 2016/6/15.
 *
 * @author Yucun Fang
 */

public class SHAUtils {
    private static final boolean DEBUG = true;

    public static String encodeWithMD5(@NonNull byte[] textBytes) {
        return encode(textBytes, "MD5");
    }

    public static String encodeWithSHA_1(@NonNull String text) {
        return encodeWithSHA_1(text.getBytes());
    }

    public static String encodeWithSHA_1(@NonNull byte[] textBytes) {
        return encode(textBytes, "SHA-1");
    }

    public static String encodeWithSHA_256(@NonNull String text) {
        return encodeWithSHA_256(text.getBytes());
    }

    public static String encodeWithSHA_256(@NonNull byte[] textBytes) {
        return encode(textBytes, "SHA-256");
    }

    public static String encodeWithSHA_512(@NonNull String text) {
        return encodeWithSHA_512(text.getBytes());
    }

    public static String encodeWithSHA_512(byte[] textBytes) {
        return encode(textBytes, "SHA-512");
    }

    public static String encodeWithSHA3_224(byte[] textBytes) {
        return new Keccak(1600).getHash(ParseUtils.bytesToHexString(textBytes), 1152, 28);
    }

    public static String encodeWithSHA3_256(byte[] textBytes) {
        return new Keccak(1600).getHash(ParseUtils.bytesToHexString(textBytes), 1088, 32);
    }

    public static String encodeWithSHA3_384(byte[] textBytes) {
        return new Keccak(1600).getHash(ParseUtils.bytesToHexString(textBytes), 832, 48);
    }

    public static String encodeWithSHA3_512(byte[] textBytes) {
        return new Keccak(1600).getHash(ParseUtils.bytesToHexString(textBytes), 576, 64);
    }

    public static String getFileMD5(@NonNull File file) {
        MessageDigest md = null;
        FileInputStream fis = null;
        byte[] buffer = new byte[1024 * 8];
        int length;
        try {
            md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
        } catch (Exception e) {
            return null;
        } finally {
            IoUtils.close(fis);
        }
        BigInteger bigInt = new BigInteger(1, md.digest());
        return bigInt.toString(16);
    }

    private static String encode(byte[] textBytes, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(textBytes);
            byte[] digests = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digests) {
                int buffer = b & 0xff;
                if (Integer.toHexString(b).length() == 1) {
                    sb = sb.append("0");
                }
                sb.append(Integer.toHexString(buffer));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static byte[] generateHmacSHA256Key() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            return keyGenerator.generateKey().getEncoded();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] encodeWithHmacSHA1(@NonNull byte[] textBytes, @NonNull byte[] keys) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(keys, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKeySpec);
            return mac.doFinal(textBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] encodeWithHmacSHA256(@NonNull byte[] textBytes, @NonNull byte[] keys) {
        try {
            SecretKey secretKey = new SecretKeySpec(keys, "HmacSHA256");
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            return mac.doFinal(textBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }
}
