package com.hellofyc.base.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * Created on 2015/9/7.
 *
 * @author Yucun Fang
 */
public class RSAUtils {
    private static final boolean DEBUG = true;

    private static final String PROVIDER_ANDROID_OPEN_SSL							 = "AndroidOpenSSL";
    public static final String PROVIDER_SC											 = "SC";

    private static final String ALGORITHM_RSA										 = "RSA";

    //	public static final String TRANSFORMATION_RSA_ECB_NOPADDING						 = "RSA/ECB/NoPadding";
    private static final String TRANSFORMATION_RSA_ECB_PKCS1PADDING				 	 = "RSA/ECB/PKCS1Padding";
    public static final String TRANSFORMATION_RSA_NONE_OAEPWITHSHA1ANDMGF1PADDING 	 = "RSA/None/OAEPWithSHA1AndMGF1Padding";

    private static final String TAG_RSA_PRIVATE_KEY_BEGIN							 = "-----BEGIN RSA PRIVATE KEY-----";
    private static final String TAG_RSA_PRIVATE_KEY_END							 	 = "-----END RSA PRIVATE KEY-----";
    private static final String TAG_RSA_PUBLIC_KEY_BEGIN							 = "-----BEGIN PUBLIC KEY-----";
    private static final String TAG_RSA_PUBLIC_KEY_END								 = "-----END PUBLIC KEY-----";

    /**
     * RSA
     */
    @SuppressLint("TrulyRandom")
    public static KeyPair generateRsaKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(TRANSFORMATION_RSA_ECB_PKCS1PADDING);
            generator.initialize(1024);
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            if (DEBUG) FLog.e(e);
        }
        return null;
    }

    public static PublicKey getPublicKey(@NonNull Context context, @NonNull String assetsPath) {
        String text = ResUtils.getFileStringFromAssets(context, assetsPath);
        String publicKeyString = text.replace(TAG_RSA_PUBLIC_KEY_BEGIN, "").replaceAll(TAG_RSA_PUBLIC_KEY_END, "");
        return getPublicKey(publicKeyString);
    }

    public static PublicKey getPublicKey(@NonNull String publicKeyString) {
        return getPublicKey(Base64Utils.decode(publicKeyString));
    }

    /**
     * 通过公钥byte获取PublicKey
     */
    public static PublicKey getPublicKey(@NonNull byte[] keyData) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyData);
            return KeyFactory.getInstance(ALGORITHM_RSA).generatePublic(keySpec);
        } catch (Exception e) {
            FLog.e(e);
        }
        return null;
    }

    /**
     * 使用N、e值还原公钥
     */
    public static PublicKey getPublicKey(@NonNull String modulus, @NonNull String publicExponent) {
        BigInteger bigIntModulus = new BigInteger(modulus);
        BigInteger bigIntPrivateExponent = new BigInteger(publicExponent);
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntPrivateExponent);
        try {
            return KeyFactory.getInstance(ALGORITHM_RSA).generatePublic(keySpec);
        } catch (Exception e) {
            FLog.e(e);
        }
        return null;
    }

    public static PrivateKey getPrivateKey(@NonNull String modulus, @NonNull String privateExponent) {
        BigInteger bigIntModulus = new BigInteger(modulus);
        BigInteger bigIntPrivateExponent = new BigInteger(privateExponent);
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntPrivateExponent);
        try {
            return KeyFactory.getInstance(ALGORITHM_RSA).generatePrivate(keySpec);
        } catch (Exception e) {
            FLog.e(e);
        }
        return null;
    }

    public static PrivateKey getPrivateKey(@NonNull String privateKeyString) {
        return getPrivateKey(Base64Utils.decode(privateKeyString));
    }

    /**
     * 通过私钥byte获取PrivateKey
     */
    public static PrivateKey getPrivateKey(@NonNull byte[] keyData) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyData);
            return KeyFactory.getInstance(ALGORITHM_RSA).generatePrivate(keySpec);
        } catch (Exception e) {
            FLog.e(e);
        }
        return null;
    }

    public static String encrypt(String srcString, @NonNull Key key) {
        if (TextUtils.isEmpty(srcString)) return "";
        byte[] bytes = encrypt(srcString.getBytes(), key);
        if (bytes != null) {
            Base64Utils.encodeToString(bytes);
        }
        return "";
    }

    public static byte[] encrypt(byte[] data, @NonNull Key key) {
        if (data == null) return null;
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION_RSA_ECB_PKCS1PADDING, PROVIDER_ANDROID_OPEN_SSL);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (Exception e) {
            FLog.e(e);
        }
        return null;
    }

    public static String decrypt(String encryptedString, @NonNull Key key) {
        if (TextUtils.isEmpty(encryptedString)) return "";

        byte[] bytes = decrypt(Base64Utils.decode(encryptedString), key);
        if (bytes != null) {
            return new String(bytes);
        }
        return "";
    }

    public static byte[] decrypt(byte[] data, @NonNull Key key) {
        if (data == null) return null;
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION_RSA_ECB_PKCS1PADDING, PROVIDER_ANDROID_OPEN_SSL);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
