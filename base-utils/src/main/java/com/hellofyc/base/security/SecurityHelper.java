package com.hellofyc.base.security;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.security.auth.x500.X500Principal;

/**
 * Created on 2016/12/15.
 *
 * @author Yucun Fang
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class SecurityHelper {

    private static final int MSG_ENCRYPT      = 1;
    private static final int MSG_DECRYPT      = 2;
    private static final int MSG_ENCRYPT_TASK = 3;
    private static final int MSG_DECRYPT_TASK = 4;

    private static SecurityHelper sInstance;
    private KeyStore mKeyStore;
    private KeyPair mKeyPair;

    private Executor mExecutor = Executors.newFixedThreadPool(10);

    private Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ENCRYPT: {
                    Object[] objects = (Object[]) msg.obj;
                    OnEncryptCallback callback = (OnEncryptCallback) objects[0];
                    byte[] bytes = (byte[]) objects[1];
                    if (callback != null) {
                        callback.onEncrypt(bytes, bytes != null ? Base64.encodeToString(bytes, Base64.DEFAULT) : "");
                    }
                    break;
                }
                case MSG_DECRYPT: {
                    Object[] objects = (Object[]) msg.obj;
                    OnDecryptCallback callback = (OnDecryptCallback) objects[0];
                    if (callback != null) {
                        byte[] bytes = (byte[]) objects[1];
                        callback.onDecrypt(bytes != null ? new String((byte[]) objects[1]) : "");
                    }
                    break;
                }
                case MSG_ENCRYPT_TASK: {
                    Object[] objects = (Object[]) msg.obj;
                    Context context = (Context) objects[0];
                    String alias = String.valueOf(objects[1]);
                    String value = String.valueOf(objects[2]);
                    OnEncryptCallback callback = (OnEncryptCallback) objects[3];
                    mExecutor.execute(new EncryptTask(context, alias, value, callback));
                    break;
                }
                case MSG_DECRYPT_TASK: {
                    Object[] objects = (Object[]) msg.obj;
                    Context context = (Context) objects[0];
                    String alias = String.valueOf(objects[1]);
                    String value = String.valueOf(objects[2]);
                    OnDecryptCallback callback = (OnDecryptCallback) objects[3];
                    mExecutor.execute(new DecryptTask(context, alias, value, callback));
                    break;
                }
            }
            return true;
        }
    });

    public SecurityHelper() {
        try {
            mKeyStore = KeyStore.getInstance("AndroidKeyStore");
            mKeyStore.load(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SecurityHelper getInstance() {
        if (sInstance == null) {
            synchronized (SecurityHelper.class) {
                if (sInstance == null) {
                    sInstance = new SecurityHelper();
                }
            }
        }
        return sInstance;
    }

    public KeyStore getKeyStore() {
        return mKeyStore;
    }

    public List<String> getAliases() {
        List<String> aliasList = new ArrayList<>();
        try {
            Enumeration<String> enumeration = mKeyStore.aliases();
            while (enumeration.hasMoreElements()) {
                aliasList.add(enumeration.nextElement());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aliasList;
    }

    public void deleteAlias(@NonNull String alias) {
        if (!isContainedAlias(alias)) return;

        try {
            mKeyStore.deleteEntry(alias);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("GetInstance")
    public byte[] wrap(Context context, String alias, SecretKey key) {
        ensureKeyPair(context, alias);

        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.WRAP_MODE, mKeyPair.getPublic());
            return cipher.wrap(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("GetInstance")
    public SecretKey unwrap(Context context, String alias, byte[] bytes) {
        ensureKeyPair(context, alias);
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.UNWRAP_MODE, mKeyPair.getPrivate());
            return (SecretKey) cipher.unwrap(bytes, "AES", Cipher.SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SecretKey generateKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256, new SecureRandom());
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SecretKey generateKey(char[] passphraseOrPin, byte[] salt) {
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec keySpec = new PBEKeySpec(passphraseOrPin, salt, 1000, 256);
            return secretKeyFactory.generateSecret(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void encrypt (@NonNull Context context, @NonNull String alias, @NonNull String value, @NonNull OnEncryptCallback callback) {
        Message msg = mHandler.obtainMessage(MSG_ENCRYPT_TASK);
        Object[] objects = new Object[4];
        objects[0] = context;
        objects[1] = alias;
        objects[2] = value;
        objects[3] = callback;
        msg.obj = objects;
        mHandler.sendMessage(msg);
    }

    public void decrypt (@NonNull Context context, @NonNull String alias, @NonNull String value, @NonNull OnDecryptCallback callback) {
        Message msg = mHandler.obtainMessage(MSG_DECRYPT_TASK);
        Object[] objects = new Object[4];
        objects[0] = context;
        objects[1] = alias;
        objects[2] = value;
        objects[3] = callback;
        msg.obj = objects;
        mHandler.sendMessage(msg);
    }

    @SuppressLint("GetInstance")
    private byte[] encrypt(@NonNull Context context, @NonNull String alias, @NonNull byte[] value) {
        ensureKeyPair(context, alias);
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, mKeyPair.getPublic());
            return cipher.doFinal(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("GetInstance")
    public byte[] decrypt(@NonNull Context context, @NonNull String alias, @NonNull byte[] value) {
        ensureKeyPair(context, alias);
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, mKeyPair.getPrivate());
            return cipher.doFinal(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void ensureKeyPair(Context context, String alias) {
        if (!isContainedAlias(alias)) {
            mKeyPair = generateKeyPair(context, alias);
        } else {
            try {
                KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) mKeyStore.getEntry(alias, null);
                mKeyPair = new KeyPair(privateKeyEntry.getCertificate().getPublicKey(), privateKeyEntry.getPrivateKey());
            }  catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    abstract class BaseTask implements Runnable {

        protected Context mContext;
        protected String mAlias;
        protected String mValue;

        public BaseTask(Context context, String alias, String value) {
            mContext = context;
            mAlias = alias;
            mValue = value;
        }
    }

    class EncryptTask extends BaseTask {

        private OnEncryptCallback mCallback;

        public EncryptTask(Context context, String alias, String value, OnEncryptCallback callback) {
            super(context, alias, value);
            mCallback = callback;
        }

        @Override
        public void run() {
            try {
                byte[] bytes = encrypt(mContext, mAlias, mValue.getBytes());
                Message msg = mHandler.obtainMessage(MSG_ENCRYPT);
                Object[] objs = new Object[2];
                objs[0] = mCallback;
                objs[1] = bytes;
                msg.obj = objs;
                mHandler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class DecryptTask extends BaseTask {

        private OnDecryptCallback mCallback;

        public DecryptTask(Context context, String alias, String value, OnDecryptCallback callback) {
            super(context, alias, value);
            mCallback = callback;
        }

        @Override
        public void run() {
            byte[] bytes = decrypt(mContext, mAlias, Base64.decode(mValue.getBytes(), Base64.DEFAULT));
            Message msg = mHandler.obtainMessage(MSG_DECRYPT);
            Object[] objs = new Object[2];
            objs[0] = mCallback;
            objs[1] = bytes;
            msg.obj = objs;
            mHandler.sendMessage(msg);
        }
    }

    public KeyPair generateKeyPair(@NonNull Context context, @NonNull String alias) {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.YEAR, 1);

        try {
            AlgorithmParameterSpec spec;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                spec = new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setCertificateNotBefore(startDate.getTime())
                        .setCertificateNotAfter(endDate.getTime())
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                        .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                        .build();
            } else {
                spec = new KeyPairGeneratorSpec.Builder(context)
                        .setAlias(alias)
                        .setSubject(new X500Principal("CN=" + alias))
                        .setSerialNumber(BigInteger.ONE)
                        .setStartDate(startDate.getTime())
                        .setEndDate(endDate.getTime())
                        .build();
            }
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
            keyPairGenerator.initialize(spec);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isContainedAlias(String alias) {
        try {
            return mKeyStore.containsAlias(alias);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public interface OnEncryptCallback {
        void onEncrypt(byte[] data, String base64String);
    }

    public interface OnDecryptCallback {
        void onDecrypt(String decryptString);
    }

}
