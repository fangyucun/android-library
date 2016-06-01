package com.hellofyc.base.content;

import android.content.Context;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;

/**
 * Created on 2015/10/27.
 *
 * @author Yucun Fang
 */
public class FingerprintHelper {

    public static void authenticate(@NonNull Context context, @NonNull String keyAlias,
                                    @NonNull FingerprintManagerCompat.AuthenticationCallback callback) {
        try {
            Signature signature = Signature.getInstance("SHA256withECDSA");
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            PrivateKey key = (PrivateKey) keyStore.getKey(keyAlias, null);
            signature.initSign(key);
            FingerprintManagerCompat.CryptoObject cryptoObject = new FingerprintManagerCompat.CryptoObject(signature);
            CancellationSignal cancellationSignal = new CancellationSignal();
            FingerprintManagerCompat fingerprintManager = FingerprintManagerCompat.from(context);
            fingerprintManager.authenticate(cryptoObject, 0, cancellationSignal, callback, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static KeyPair generateKeyPair(@NonNull String keystoreAlias) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return null;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore");
            keyPairGenerator.initialize(new KeyGenParameterSpec.Builder(keystoreAlias, KeyProperties.PURPOSE_SIGN).setDigests(KeyProperties.DIGEST_SHA256).setAlgorithmParameterSpec(new ECGenParameterSpec("secp256r1")).setUserAuthenticationRequired(true).build());
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PrivateKey getPrivateKey(@NonNull String keyAlias) {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            return(PrivateKey) keyStore.getKey(keyAlias, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PublicKey getPublicKey(@NonNull String keyAlias) {
        KeyStore keyStore;
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            return keyStore.getCertificate(keyAlias).getPublicKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean hasEnrolledFingerprints(Context context) {
        FingerprintManagerCompat fingerprintManager = FingerprintManagerCompat.from(context);
        return fingerprintManager.hasEnrolledFingerprints();
    }

    public static boolean isHardwareDetected(Context context) {
        FingerprintManagerCompat fingerprintManager = FingerprintManagerCompat.from(context);
        return fingerprintManager.isHardwareDetected();
    }

}
