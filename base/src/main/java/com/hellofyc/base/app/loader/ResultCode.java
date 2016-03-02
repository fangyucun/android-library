package com.hellofyc.base.app.loader;

import android.util.SparseArray;

/**
 * Created on 2016/2/18.
 *
 * @author Yucun Fang
 */
public class ResultCode {

    public static final int SUCCESS   = 200;
    public static final int UNKNOWN   = -1;
    public static final int NET       = -1000;
    public static final int JSON      = -2000;
    public static final int SERVER    = -3000;

    private static final SparseArray<String> sCodeArray = new SparseArray<>();

    static {
        sCodeArray.put(SUCCESS, "成功");
        sCodeArray.put(UNKNOWN, "位置错误");
        sCodeArray.put(NET, "网络错误");
        sCodeArray.put(JSON, "JSON转化错误");
        sCodeArray.put(SERVER, "服务器错误");
    }

    public static String getCodeString(int code) {
        return sCodeArray.get(code);
    }
}
