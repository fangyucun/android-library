package com.hellofyc.base.util;

import android.text.TextUtils;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created on 2016/6/15.
 *
 * @author Yucun Fang
 */

public class SHAUtils {

    public static String encode(String text) {
        if (TextUtils.isEmpty(text)) return "";
        return encode(text.getBytes());
    }

    public static String encode(byte[] bytes) {
        return encode(bytes, "SHA-1");
    }

    public static String encodeWith256(String text) {
        if (TextUtils.isEmpty(text)) return "";
        return encodeWith256(text.getBytes());
    }

    public static String encodeWith256(byte[] bytes) {
        return encode(bytes, "SHA-256");
    }

    public static String encodeWith512(String text) {
        if (TextUtils.isEmpty(text)) return "";
        return encodeWith512(text.getBytes());
    }

    public static String encodeWith512(byte[] bytes) {
        return encode(bytes, "SHA-512");
    }

    private static String encode(byte[] bytes, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(bytes);
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

    public static String encodeWithHMAC(String text) {
        if (TextUtils.isEmpty(text)) return "";
        return encodeWithHMAC(text.getBytes());
    }

    public static String encodeWithHMAC(byte[] bytes) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(bytes, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKeySpec);
            byte[] digest = mac.doFinal(bytes);
            return Base64.encodeToString(digest, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
