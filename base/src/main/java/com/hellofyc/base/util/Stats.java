package com.hellofyc.base.util;

import android.support.v4.util.ArrayMap;

/**
 * Created on 2016/3/3.
 *
 * @author Yucun Fang
 */
public class Stats {

    private static final String TAG_DEFAULT = "default";

    private static ArrayMap<String, Long> mTagMap = new ArrayMap<>();

    public static void start() {
        start(TAG_DEFAULT);
    }

    public static void start(String tag) {
        mTagMap.put(tag, TimeUtils.getCurrentTimeMillis());
    }

    public static long stop() {
        return stop(TAG_DEFAULT);
    }

    public static long stop(String tag) {
        if (mTagMap.containsKey(tag)) {
            long startTime = mTagMap.get(tag);
            long duration = TimeUtils.getCurrentTimeMillis() - startTime;
            mTagMap.remove(tag);
            return duration;
        }
        return -1;
    }
}
