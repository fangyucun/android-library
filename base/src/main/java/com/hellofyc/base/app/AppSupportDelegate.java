/*
 *  Copyright (C) 2012-2015 Jason Fang ( ifangyucun@gmail.com )
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.hellofyc.base.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.content.res.ResourcesCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;

import com.hellofyc.base.app.activity.SingleFragmentActivity;
import com.hellofyc.base.util.AndroidUtils;
import com.hellofyc.base.util.FLog;
import com.hellofyc.base.util.ToastUtils;

/**
 * Create on 2015-4-24 11:44:49
 *
 * @author Yucun Fang
 */
public class AppSupportDelegate implements ResourcesValue {

    private Activity mActivity;

	private AppSupportDelegate() {
    }

    private AppSupportDelegate(Activity activity) {
        mActivity = activity;
    }

    private AppSupportDelegate(Fragment fragment) {
        mActivity = fragment.getActivity();
    }

    public static AppSupportDelegate create() {
        return new AppSupportDelegate();
    }

    public static AppSupportDelegate create(@NonNull Activity activity) {
        return new AppSupportDelegate(activity);
    }

    public static AppSupportDelegate create(@NonNull Fragment fragment) {
        return new AppSupportDelegate(fragment);
    }

	public int getScreenWidth() {
		return getDisplayMetrics().widthPixels;
	}

    public int getScreenHeight() {
        return getDisplayMetrics().heightPixels;
    }

    public float getScreenDensity() {
        return getDisplayMetrics().density;
    }

	public View findViewById(View rootView, @IdRes int id) {
		return rootView != null ? rootView.findViewById(id) : null;
	}

	public View findViewById(Window window, @IdRes int id) {
		return window != null ? window.findViewById(id) : null;
	}

	public void setViewsOnClickListener(@Nullable OnClickListener listener, @NonNull View... views) {
		for (View view : views) {
			if (view != null) {
				view.setOnClickListener(listener);
			} else {
				FLog.e("Some Views are null!");
			}
		}
	}

	public void setViewsOnClickListener(@Nullable OnClickListener listener, int... viewResIds) {
		for (int id : viewResIds) {
			if (id != 0) {
                View view = mActivity.findViewById(id);
                if (view != null) {
                    view.setOnClickListener(listener);
                } else {
                    FLog.e("Some Views are null!");
                }
			}
		}
	}

	public DisplayMetrics getDisplayMetrics() {
		return getResources().getDisplayMetrics();
	}

    public final boolean checkSelfPermission(@NonNull String permission) {
        return PermissionChecker.checkSelfPermission(mActivity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Nullable
    public final Uri getReferrer() {
        return new ActivityCompat().getReferrer(mActivity);
    }

	public Configuration getConfiguration() {
		return getResources().getConfiguration();
	}

    public void startFragment(@NonNull Intent intent) {
        startFragmentForResult(intent, -1, null);
    }

    public void startFragment(@NonNull Intent intent, @Nullable Bundle options) {
        startFragmentForResult(intent, -1, options);
    }

    public void startFragmentForResult (@NonNull Intent intent, int requestCode) {
        startFragmentForResult(intent, requestCode, null);
    }

    public void startFragmentForResult(@NonNull Intent intent, int requestCode, @Nullable Bundle options) {
        ComponentName componentName = intent.getComponent();
        String targetFragmentClassName = componentName.getClassName();
        intent.setComponent(new ComponentName(componentName.getPackageName(), SingleFragmentActivity.class.getName()));
        intent.putExtra(SingleFragmentActivity.EXTRA_FRAGMENT_CLASSNAME, targetFragmentClassName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mActivity.startActivityForResult(intent, requestCode, options);
        } else {
            mActivity.startActivityForResult(intent, requestCode);
        }
    }

	public void forbidScreenshots(boolean trueOrFalse) {
		setAttributesFlag(LayoutParams.FLAG_SECURE, trueOrFalse);
	}

	public void setScreenFull(boolean trueOrFalse) {
        setAttributesFlag(LayoutParams.FLAG_FULLSCREEN, trueOrFalse);
	}

	public boolean isScreenLandscape() {
		return getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
	}

	public boolean isScreenPortrait() {
		return getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
	}

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setSystemUiVisibility(int flag) {
        mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | flag);
    }

    public void showInputMethod(@NonNull EditText editText) {
        AndroidUtils.showInputMethod(editText);
    }

    public void showInputMethod(@NonNull Window window) {
        AndroidUtils.showSoftKeyboard(window);
    }

    public void showToast(Context context, CharSequence text) {
        ToastUtils.showDefault(context, text);
    }

    public void hideInputMethod(@NonNull EditText editText) {
        AndroidUtils.hideInputMethod(editText);
    }

	private void setAttributesFlag(int flags, boolean trueOrFalse) {
		Window win = mActivity.getWindow();
        LayoutParams params = win.getAttributes();
        if (trueOrFalse) {
            params.flags |=  flags;
        } else {
            params.flags &= ~flags;
        }
        win.setAttributes(params);
	}

    @Override
    public Resources getResources() {
        return mActivity.getResources();
    }

    @Override
    public String getString(@StringRes int id) {
        return getResources().getString(id);
    }

    @Override
    public String getString(@StringRes int id, Object... args) {
        return getResources().getString(id, args);
    }

    @Override
    public int getColor(@ColorRes int id) {
        return ContextCompat.getColor(mActivity, id);
    }

    @Override
    public int getColor(@ColorRes int id, @Nullable Resources.Theme theme) {
        return ResourcesCompat.getColor(getResources(), id, theme);
    }

    @Override
    public ColorStateList getColorStateList(@ColorRes int id) {
        return ContextCompat.getColorStateList(mActivity, id);
    }

    @Override
    public ColorStateList getColorStateList(@ColorRes int id, @Nullable Resources.Theme theme) {
        return ResourcesCompat.getColorStateList(getResources(), id, theme);
    }

    @Override
    public Drawable getDrawable(@DrawableRes int id) {
        return ContextCompat.getDrawable(mActivity, id);
    }

    @Override
    public Drawable getDrawable(@DrawableRes int id, Resources.Theme theme) {
        return ResourcesCompat.getDrawable(getResources(), id, theme);
    }

    @Override
    public Drawable getDrawableForDensity(@DrawableRes int id, int density, @Nullable Resources.Theme theme) {
        return ResourcesCompat.getDrawableForDensity(getResources(), id, density, theme);
    }

    public void setOnKeyboardVisibileListener(final OnKeyboardVisibleListener listener) {
        final View activityRootView = mActivity.findViewById(android.R.id.content);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (listener != null) {
                    listener.onKeyboardVisible(heightDiff > 100);
                }
            }
        });
    }

    public interface OnKeyboardVisibleListener {
        void onKeyboardVisible(boolean visible);
    }
}
