package com.hellofyc.applib.os;

import com.hellofyc.applib.util.Reflect;

/**
 * Created on 2015/11/26.
 *
 * @author Yucun Fang
 */
public class SystemPropertiesHelper {

    private static final Class sSystemPropertiesClass;

    static {
        sSystemPropertiesClass = getSystemPropertiesClass();
    }

    private static Class getSystemPropertiesClass() {
        try {
            return Class.forName("android.os.SystemProperties");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String get(String key) {
        return get(key, "");
    }

    public static String get(String key, String defaultValue) {
        return Reflect.on(sSystemPropertiesClass).call("get", key, defaultValue).get();
    }

}
