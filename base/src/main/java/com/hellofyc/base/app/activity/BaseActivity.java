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

package com.hellofyc.base.app.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.hellofyc.base.app.AppSupportDelegate;
import com.hellofyc.base.app.BaseApplication;
import com.hellofyc.base.content.IntentWrapper;
import com.hellofyc.base.content.PermissionHelper;
import com.hellofyc.base.utils.FLog;
import com.hellofyc.base.utils.Reflect;
import com.hellofyc.base.utils.ResUtils;
import com.hellofyc.base.utils.ToastUtils;

abstract public class BaseActivity extends AppCompatActivity implements OnClickListener {

    public static final String EXTRA_REFERRER = "android.intent.extra.REFERRER";
    public static final String EXTRA_REFERRER_NAME = "android.intent.extra.REFERRER_NAME";

    private AppSupportDelegate mAppSupportDelegate;

	protected View mRootView;

	private boolean mPressTwoExit = false;
	private boolean mIsHighPriority = false;
    private long mPressTime = 0;

    private IntentWrapper mIntentWrapper;
    private EntityKeyReciver mEntityKeyReciver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestPermissions(PermissionHelper.getPermissionsInManifest(this));
	}

	@Override
	public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mRootView = LayoutInflater.from(this).inflate(layoutResID, null);
    }

	@Override
	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
		mRootView = view;
	}

	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		mRootView = view;
	}

    public void setViewsOnClickListener(View... views) {
		getAppSupportDelegate().setViewsOnClickListener(this, views);
	}

    public void setViewsOnClickListener(@IdRes int... id) {
        getAppSupportDelegate().setViewsOnClickListener(this, id);
    }

	/**
	 * @param resName name
	 */
	public void setContentView(String resName) {
		setContentView(ResUtils.getLayoutResId(this, resName));
	}

    /**
     * @see View#setSystemUiVisibility(int)
     * @param flag {@link View#SYSTEM_UI_FLAG_LOW_PROFILE},
     * {@link View#SYSTEM_UI_FLAG_HIDE_NAVIGATION}, {@link View#SYSTEM_UI_FLAG_FULLSCREEN},
     * {@link View#SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION},{@link View#SYSTEM_UI_FLAG_IMMERSIVE_STICKY}
     * {@link View#SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN}, {@link View#SYSTEM_UI_FLAG_IMMERSIVE},
     */
    public void setSystemUiVisibility(int flag) {
        getAppSupportDelegate().setSystemUiVisibility(flag);
    }

    public void setOnKeyboardVisibileListener (AppSupportDelegate.OnKeyboardVisibleListener listener) {
        getAppSupportDelegate().setOnKeyboardVisibileListener(listener);
    }

    public void setActionBarOverflowButtonVisible() {
        Reflect.on(ViewConfiguration.class).set("sHasPermanentMenuKey", false);
    }

    public void showActionBarMenuIcon() {
        Reflect.on(Menu.class).set("sHasPermanentMenuKey", false);
    }

	/**
	 * Finds a view that was identified by the name attribute from the XML that
	 * @param resName name
	 * @return view
	 */
	public View findViewByName(String resName) {
		return findViewById(ResUtils.getIdResId(this, resName));
	}

    @Override
    public Intent getIntent() {
        mIntentWrapper = IntentWrapper.newInstance();
        mIntentWrapper.set(super.getIntent());
        return mIntentWrapper;
    }

    @Override
    public void setIntent(Intent newIntent) {
        super.setIntent(newIntent);
        mIntentWrapper.set(newIntent);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if ("com.android.internal.view.menu.MenuBuilder".equals(menu.getClass().getName())) {
                Reflect.on(menu).call("setOptionalIconsVisible", true);
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        try {
            super.startActivityForResult(intent, requestCode, options);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void startFragment(Intent intent) {
        getAppSupportDelegate().startFragment(intent);
    }

    public void startFragment(Intent intent, Bundle options) {
        getAppSupportDelegate().startFragment(intent, options);
    }

    public void startFragmentForResult (Intent intent, int requestCode) {
        getAppSupportDelegate().startFragmentForResult(intent, requestCode);
    }

    public void startFragmentForResult (Intent intent, int requestCode, Bundle options) {
        getAppSupportDelegate().startFragmentForResult(intent, requestCode, options);
    }

    /**
     * {@link Activity#setEnterSharedElementCallback(android.app.SharedElementCallback)}
     */
    public void setEnterSharedElementCallbackCompat(SharedElementCallback callback) {
        ActivityCompat.setEnterSharedElementCallback(this, callback);
    }

    /**
     * {@link Activity#setExitSharedElementCallback(android.app.SharedElementCallback)}
     */
    public void setExitSharedElementCallbackCompat(SharedElementCallback callback) {
        ActivityCompat.setExitSharedElementCallback(this, callback);
    }

    /**
     * {@link Activity#postponeEnterTransition()}
     */
    public void postponeEnterTransitionCompat() {
        ActivityCompat.postponeEnterTransition(this);
    }

    /**
     * {@link Activity#startPostponedEnterTransition()}
     */
    public void startPostponedEnterTransitionCompat() {
        ActivityCompat.startPostponedEnterTransition(this);
    }

    /**
     * {@link Activity#finishAfterTransition()}
     */
    public void finishAfterTransitionCompat() {
        ActivityCompat.finishAfterTransition(this);
    }

    /**
     * {@link Activity#getReferrer()}
     */
    @Nullable
    public Uri getReferrerCompat() {
        return getAppSupportDelegate().getReferrer();
    }

    public void hideInputMethod(@NonNull EditText editText) {
        getAppSupportDelegate().hideInputMethod(editText);
    }

    public void showInputMethod(@NonNull EditText editText) {
        getAppSupportDelegate().showInputMethod(editText);
    }

    public void showInputMethod() {
        getAppSupportDelegate().showInputMethod(getWindow());
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onSupportNavigateUp() {
		finish();
		return true;
	}

	@Override
	public void unbindService(ServiceConnection conn) {
		try {
			super.unbindService(conn);
		} catch (IllegalArgumentException e) {
			FLog.e("Service not registered!");
		}
	}

	@Override
	public boolean stopService(Intent name) {
		if (name == null) {
			FLog.e("intent cannot be null!");
			return true;
		}
		return super.stopService(name);
	}

    public final boolean checkPermission(String permission) {
        return getAppSupportDelegate().checkPermission(permission);
    }

    public final void requestPermission(String permission) {
        getAppSupportDelegate().requestPermission(this, permission);
    }

    public final void requestPermissions(String[] permissions) {
        PermissionHelper.requestPermissions(this, 0, permissions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults, requestCode != 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setStatusBarColorTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

	public void forbidScreenshots(boolean trueOrFalse) {
		getAppSupportDelegate().forbidScreenshots(trueOrFalse);
	}

	public void setScreenFull(boolean on) {
		getAppSupportDelegate().setScreenFull(on);
	}

	public boolean isScreenLandscape() {
		return getAppSupportDelegate().isScreenLandscape();
	}

    public boolean isScreenPortrait() {
        return getAppSupportDelegate().isScreenPortrait();
    }

	protected void setPressTwoExitEnable(boolean isHighPriority) {
		mPressTwoExit = true;
		mIsHighPriority = isHighPriority;
	}

	public final AssetManager getAssets() {
		return getResources().getAssets();
	}

	public final boolean getBooleanCompat(@BoolRes int id) {
		return getResources().getBoolean(id);
	}

    public final int getColorCompat(@ColorRes int id) {
        return getAppSupportDelegate().getColor(id);
    }

    public final Configuration getConfiguration() {
		return getResources().getConfiguration();
	}

	public final float getDimensionCompat(@DimenRes int id) {
		return getResources().getDimension(id);
	}

	public final int getScreenWidth() {
		return getAppSupportDelegate().getScreenWidth();
	}

	public final int getScreenHeight() {
		return getAppSupportDelegate().getScreenHeight();
	}

	public final float getScreenDensity() {
		return getAppSupportDelegate().getScreenDensity();
	}

	public final Drawable getDrawableCompat(@DrawableRes int resId) {
		return getAppSupportDelegate().getDrawable(resId);
	}

    public final ColorStateList getColorStateListCompat(@ColorRes int id) {
        return getAppSupportDelegate().getColorStateList(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerEntityKeyReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterEntityKeyReceiver();
    }

    @Override
	public void onBackPressed() {
		if (mPressTwoExit && !mIsHighPriority) {
			pressBackToExit();
		} else {
			super.onBackPressed();
		}
	}

    public void onHomePressed() {
    }

    public void onRecentAppsPressed() {
    }

	void pressBackToExit() {
		if (System.currentTimeMillis() - mPressTime > 2000) {
			ToastUtils.show(this, "再按一次退出" + getPackageManager().getApplicationLabel(getApplicationInfo()) + "!");
			mPressTime = System.currentTimeMillis();
		} else {
            if (getApplication() instanceof BaseApplication) {
                ((BaseApplication)getApplication()).exit();
            } else {
                ActivityCompat.finishAffinity(this);
            }
		}
	}

    /**
     * {@link Activity#invalidateOptionsMenu()}
     */
    public boolean invalidateOptionsMenuCompat() {
        return ActivityCompat.invalidateOptionsMenu(this);
    }

    private void registerEntityKeyReceiver() {
        if (mEntityKeyReciver == null) {
            mEntityKeyReciver = new EntityKeyReciver();
        }
        IntentFilter filter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(mEntityKeyReciver, filter);
    }

    private void unregisterEntityKeyReceiver() {
        if (mEntityKeyReciver != null) {
            try {
                unregisterReceiver(mEntityKeyReciver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class EntityKeyReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String reason = intent.getStringExtra("reason");
            if ("homekey".equals(reason)) {
                onHomePressed();
            } else if ("recentapps".equals(reason)) {
                onRecentAppsPressed();
            }
        }
    }

	@Override
	public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
		final int keyCode = event.getKeyCode();
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mPressTwoExit && mIsHighPriority) {
				final int action = event.getAction();
				if (action == KeyEvent.ACTION_UP) {
					pressBackToExit();
					return true;
				}
			}
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	public void onClick(View v) {
	}

	public AppSupportDelegate getAppSupportDelegate() {
		if (mAppSupportDelegate == null) {
			mAppSupportDelegate = AppSupportDelegate.create(this);
		}
		return mAppSupportDelegate;
	}
}
