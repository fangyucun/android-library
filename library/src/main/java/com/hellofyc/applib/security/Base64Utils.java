package com.hellofyc.applib.security;

import android.support.annotation.NonNull;
import android.util.Base64;

import com.hellofyc.applib.util.FLog;

import java.io.UnsupportedEncodingException;

/**
 * Created on 2015/9/7.
 *
 * @author Yucun Fang
 */
public class Base64Utils {
    private static final boolean DEBUG = false;

    public static byte[] encode(@NonNull String data) {
        return encode(data.getBytes());
    }

    public static byte[] encode(@NonNull byte[] data) {
        try {
            return Base64.encode(data, Base64.DEFAULT);
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return null;
    }

    public static String encodeToString(@NonNull String data) {
        return encodeToString(data.getBytes());
    }

    public static String encodeToString(@NonNull byte[] data) {
        try {
            return Base64.encodeToString(data, Base64.DEFAULT);
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return "";
    }

    public static byte[] decode(@NonNull String data) {
        try {
            return decode(data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return decode(data.getBytes());
        }
    }

    public static byte[] decode(@NonNull byte[] data) {
        return Base64.decode(data, Base64.DEFAULT);
    }

    public static String decodeToString(@NonNull String data) {
        try {
            return decodeToString(data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return decodeToString(data.getBytes());
        }
    }

    public static String decodeToString(@NonNull byte[] data) {
        try {
            return new String(Base64.decode(data, Base64.DEFAULT), "US-ASCII");
        } catch (Exception e) {
            if(DEBUG) FLog.e(e);
        }
        return "";
    }
}
