package com.hellofyc.base.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2016/3/3.
 *
 * @author Yucun Fang
 */
public class Stats {

    private static final String TAG_DEFAULT = "default";

    private static Map<String, Long> mTagMap = new HashMap<>();

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
