package com.hellofyc.base.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2016/3/3.
 *
 * @author Yucun Fang
 */
public class Stats {
    private static final boolean DEBUG = true;

    private static final String TAG_DEFAULT = "default";

    private Map<String, Long> mTagMap = new HashMap<>();

    private static Stats sInstance;
    private boolean mDebuggable = false;

    public static Stats getInstance() {
        if (sInstance == null) {
            sInstance = new Stats();
        }
        return sInstance;
    }

    public Stats debuggale() {
        mDebuggable = true;
        return this;
    }

    public void begin() {
        begin(TAG_DEFAULT);
    }

    public void begin(String tag) {
        mTagMap.put(tag, TimeUtils.getCurrentTimeMillis());
    }

    public long end() {
        return end(TAG_DEFAULT);
    }

    public long end(String tag) {
        if (mTagMap.containsKey(tag)) {
            long startTime = mTagMap.get(tag);
            long duration = TimeUtils.getCurrentTimeMillis() - startTime;
            mTagMap.remove(tag);
            if (mDebuggable) FLog.i("===" + tag + " Duration===" + duration);
            return duration;
        }
        return -1;
    }

    public static void sBegin() {
        getInstance().begin();
    }

    public static void sEnd() {
        getInstance().end();
    }

    public static void sBegin(String tag) {
        getInstance().end(tag);
    }

    public static void sEnd(String tag) {
        getInstance().begin(tag);
    }
}
