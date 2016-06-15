package com.hellofyc.base.net.http;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import java.net.HttpCookie;
import java.util.List;
import java.util.Set;

/**
 * Created on 2016/3/2.
 *
 * @author Yucun Fang
 */
public class HttpRequest {

    private ArrayMap<String, Object> mParams = new ArrayMap<>();
    private String mParamsString;
    private List<HttpCookie> mCookies;

    public HttpRequest add(@NonNull String key, Object value) {
        mParams.put(key, String.valueOf(value));
        return this;
    }

    public HttpRequest add(ArrayMap<String, Object> paramsMap) {
        if (paramsMap == null || paramsMap.size() == 0) return this;

        Set<String> sets = paramsMap.keySet();
        for (String key : sets) {
            mParams.put(key, paramsMap.get(key));
        }
        return this;
    }

    public HttpRequest setString(String text) {
        mParamsString = text;
        return this;
    }

    public HttpRequest setCookies(List<HttpCookie> cookies) {
        mCookies = cookies;
        return this;
    }

    public List<HttpCookie> getCookies() {
        return mCookies;
    }

    public String getString() {
        return mParamsString;
    }

    public HttpRequest remove(@NonNull String key) {
        mParams.remove(key);
        return this;
    }

    public ArrayMap<String, Object> getArrayMap() {
        return mParams;
    }
}
