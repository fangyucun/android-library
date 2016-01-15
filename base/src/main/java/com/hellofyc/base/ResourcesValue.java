package com.hellofyc.base;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

/**
 * Created on 2015/11/3.
 *
 * @author Yucun Fang
 */
public interface ResourcesValue {

    Resources getResources();
    String getString(@StringRes int id);
    String getString(@StringRes int id, Object... args);
    int getColor(@ColorRes int id);
    int getColor(@ColorRes int id, @Nullable Resources.Theme theme);
    ColorStateList getColorStateList(@ColorRes int id);
    ColorStateList getColorStateList(@ColorRes int id, @Nullable Resources.Theme theme);
    Drawable getDrawable(@DrawableRes int id);
    Drawable getDrawable(@DrawableRes int id, Resources.Theme theme);
    Drawable getDrawableForDensity(@DrawableRes int id, int density, @Nullable Resources.Theme theme);

}
