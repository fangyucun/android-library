package com.hellofyc.applib.security;

import android.support.annotation.NonNull;

import com.hellofyc.applib.util.Flog;
import com.hellofyc.applib.util.IoUtils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created on 2015/9/7.
 *
 * @author Yucun Fang
 */
public class MD5Utils {
    private static final boolean DEBUG = false;

    private static final String ALGORITHM_MD5         = "MD5";

    public static String encode(@NonNull String data) {
        return encode(data.getBytes());
    }

    public static String encode(@NonNull byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM_MD5);
            md.update(data);
            byte[] bytes = md.digest();
            int i;
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : bytes) {
                i = b;
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    stringBuilder.append("0");
                }
                stringBuilder.append(Integer.toHexString(i));
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            if (DEBUG) Flog.e(e);
        }
        return "";
    }

    public static String getFileMD5(@NonNull String filePath) {
        MessageDigest md = null;
        FileInputStream fis = null;
        byte[] buffer = new byte[1024];
        int length;
        try {
            md = MessageDigest.getInstance(ALGORITHM_MD5);
            fis = new FileInputStream(new File(filePath));
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
        } catch (Exception e) {
            return "";
        } finally {
            IoUtils.close(fis);
        }
        BigInteger bigInt = new BigInteger(1, md.digest());
        return bigInt.toString(16);
    }
}
