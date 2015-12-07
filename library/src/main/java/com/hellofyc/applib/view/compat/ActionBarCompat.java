package com.hellofyc.applib.view.compat;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

/**
 * create on 2015/8/18 16:21
 *
 * @author Yucun Fang
 */
public class ActionBarCompat {
    static class ActionBarCompatImpl {
        static void setDisplayHomeAsUpEnabled(Activity activity, boolean enable) {
            if (activity instanceof AppCompatActivity) {
                ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(enable);
                }
            } else {
                android.app.ActionBar actionBar = activity.getActionBar();
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(enable);
                }
            }
        }
    }

    public static void setDisplayHomeAsUpEnabled(Activity activity, boolean enable) {
        ActionBarCompatImpl.setDisplayHomeAsUpEnabled(activity, enable);
    }
}
