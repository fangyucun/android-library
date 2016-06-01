package com.hellofyc.base.compat;

import android.support.annotation.NonNull;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * Created on 2015/11/23.
 *
 * @author Yucun Fang
 */
public class FlymeCompat {
    public static void setStatusBarMode(@NonNull Window window, boolean dark) {
        try {
            WindowManager.LayoutParams params = window.getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field field = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            field.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = field.getInt(params);
            if (dark) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            field.setInt(params, value);
            window.setAttributes(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
