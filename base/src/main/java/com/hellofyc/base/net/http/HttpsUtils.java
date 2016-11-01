package com.hellofyc.base.net.http;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hellofyc.base.utils.FLog;
import com.hellofyc.base.utils.IoUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import static com.hellofyc.base.net.http.HttpsUtils.Policy.TRUSTMANAGER;

/**
 * Created on 2016/3/2.
 *
 * @author Yucun Fang
 */
public class HttpsUtils extends HttpUtils {

    private boolean mDebug = false;
    private SSLContext mSSLContext;
    private Policy mPolicy = TRUSTMANAGER;
    private HostnameVerifier mHostnameVerifier;

    protected HttpsUtils() {
    }

    public enum Policy {
        KEYSTORE,
        TRUSTMANAGER
    }

    public static HttpsUtils create() {
        return new HttpsUtils();
    }

    @Override
    public HttpsUtils setDebugEnable() {
        mDebug = true;
        return this;
    }

    public HttpsUtils setCertificate(Context context, String certFilePathInAssets) {
        mSSLContext = getSSLContext(context, certFilePathInAssets);
        return this;
    }

    public HttpsUtils setPolicy(Policy policy) {
        mPolicy = policy;
        return this;
    }

    public HttpsUtils setHostnameVerifier(HostnameVerifier verifier) {
        mHostnameVerifier = verifier;
        return this;
    }

    @Override
    protected void configConnection(HttpURLConnection connection) throws IOException {
        super.configConnection(connection);
        if (connection instanceof HttpsURLConnection) {
            if (mSSLContext == null) {
                throw new IllegalArgumentException("Must setCertificate()");
            }
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) connection;
            httpsURLConnection.setSSLSocketFactory(mSSLContext.getSocketFactory());
            if (mHostnameVerifier != null) {
                httpsURLConnection.setHostnameVerifier(mHostnameVerifier);
            } else {
                httpsURLConnection.setHostnameVerifier(HttpsURLConnection.getDefaultHostnameVerifier());
            }
        } else {
            FLog.e("Just support https://");
        }
    }

    private SSLContext getSSLContext(@NonNull Context context, @NonNull String certFilePath) {
        InputStream certInputStream = null;
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            certInputStream = new BufferedInputStream(context.getAssets().open(certFilePath));
            Certificate certificate = certificateFactory.generateCertificate(certInputStream);
            if (mDebug) FLog.i("subjectDN:" + ((X509Certificate)certificate).getSubjectDN());
            if (mDebug) FLog.i("publicKey:" + certificate.getPublicKey());

            SSLContext sslContext = SSLContext.getInstance("TLSv1","AndroidOpenSSL");
            initSSLContext(sslContext, certificate);
            return sslContext;
        } catch (CertificateException |
                NoSuchAlgorithmException |
                IOException |
                NoSuchProviderException e) {
            e.printStackTrace();
        } finally {
            IoUtils.close(certInputStream);
        }
        return null;
    }

    private void initSSLContext(SSLContext sslContext, Certificate certificate) {
        try {
            switch (mPolicy) {
                case KEYSTORE:
                    String keyStoreType = KeyStore.getDefaultType();
                    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                    keyStore.load(null, null);
                    keyStore.setCertificateEntry("ca", certificate);
                    String defaultAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                    TrustManagerFactory factory = TrustManagerFactory.getInstance(defaultAlgorithm);
                    factory.init(keyStore);
                    sslContext.init(null, factory.getTrustManagers(), null);
                    break;
                case TRUSTMANAGER:
                    sslContext.init(null, new TrustManager[]{new DefaultX509TrustManager(certificate)}, null);
                    break;
            }

        } catch (KeyStoreException |
                CertificateException |
                IOException |
                NoSuchAlgorithmException |
                KeyManagementException e) {
            e.printStackTrace();
        }
    }

    private class DefaultX509TrustManager implements X509TrustManager {

        private Certificate mCertificate;

        public DefaultX509TrustManager(Certificate certificate) {
            mCertificate = certificate;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            if (mDebug) FLog.i("authType:" + authType);
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            for (X509Certificate cert : chain) {
                cert.checkValidity();
                try {
                    cert.verify(mCertificate.getPublicKey());
                } catch (NoSuchAlgorithmException |
                        InvalidKeyException |
                        SignatureException |
                        NoSuchProviderException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

}
