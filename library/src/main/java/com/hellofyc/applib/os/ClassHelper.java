package com.hellofyc.applib.os;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import com.hellofyc.applib.util.ReflectUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created on 2015/9/24.
 *
 * @author Yucun Fang
 */
public class ClassHelper {
    private static Map<String, Class<?>> sClassMap;

    public static Class<?> getClass(@NonNull String className) throws ClassNotFoundException {
        if (sClassMap == null) {
            sClassMap = new ArrayMap<>();
        }

        Class<?> cls = sClassMap.get(className);
        if (cls == null) {
            cls = Class.forName(className);
            sClassMap.put(className, Class.forName(className));
        }
        return cls;
    }

    public static boolean isSingleton(Object obj) {
        if (obj == null) return false;

        try {
            Class<?> cls = getClass(obj.getClass().getName());
            return cls.isInstance(obj);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static Object invokeGetMethod (Object targetSingleton) throws
            NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return ReflectUtils.invokeMethod(targetSingleton, "get");
    }
}
