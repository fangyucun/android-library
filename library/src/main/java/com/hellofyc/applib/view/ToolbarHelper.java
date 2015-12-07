package com.hellofyc.applib.view;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created on 2015/10/21.
 *
 * @author Yucun Fang
 */
public class ToolbarHelper {

    private AppCompatActivity mActivity;
    private Toolbar mToolbar;

    public ToolbarHelper(AppCompatActivity activity, Toolbar toolbar) {
        mActivity = activity;
        mToolbar = toolbar;

        init();
    }

    public static ToolbarHelper newInstance(AppCompatActivity activity, Toolbar toolbar) {
        return new ToolbarHelper(activity, toolbar);
    }

    public static ToolbarHelper newInstance(AppCompatActivity activity, @IdRes int toolbarId) {
        return new ToolbarHelper(activity, (Toolbar) activity.findViewById(toolbarId));
    }

    private void init() {
        mActivity.setSupportActionBar(mToolbar);
        getActionBar().setTitle("");
    }

    public void setTitle(CharSequence title) {
        if (getActionBar() != null) {
            getActionBar().setTitle(title);
        }
    }

    public void setTitle(@StringRes int id) {
        if (getActionBar() != null) {
            getActionBar().setTitle(id);
        }
    }

    public void setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(showHomeAsUp);
        }
    }

    public void setIcon(Drawable drawable) {
        mToolbar.setNavigationIcon(drawable);
    }

    public void setIcon(@DrawableRes int id) {
        mToolbar.setNavigationIcon(id);
    }

    public ActionBar getActionBar() {
        return mActivity.getSupportActionBar();
    }

}
