package com.hellofyc.base.util;

import android.support.annotation.Nullable;

import java.util.Arrays;

/**
 * Created on 2016/7/20.
 *
 * @author Yucun Fang
 */

public class Objects {

    public static boolean equals(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

    public static int hashCode(@Nullable Object... objects) {
        return Arrays.hashCode(objects);
    }

}
