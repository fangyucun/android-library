package com.hellofyc.base.compat;

import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;

/**
 * Created on 2016/5/10.
 *
 * @author Yucun Fang
 */
public class AndroidCompat {
    public static void setStatusBarMode(@NonNull Window window, boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
