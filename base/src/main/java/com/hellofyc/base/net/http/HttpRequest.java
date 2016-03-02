package com.hellofyc.base.net.http;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

/**
 * Created on 2016/3/2.
 *
 * @author Yucun Fang
 */
public class HttpRequest {

    private ArrayMap<String, Object> mParams = new ArrayMap<>();

    private HttpRequest() {
    }

    public static HttpRequest create() {
        return new HttpRequest();
    }

    public HttpRequest add(@NonNull String key, Object value) {
        mParams.put(key, String.valueOf(value));
        return this;
    }

    public HttpRequest remove(@NonNull String key) {
        mParams.remove(key);
        return this;
    }

    public ArrayMap<String, Object> getArrayMap() {
        return mParams;
    }
}
