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

package com.hellofyc.base.app.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.hellofyc.base.app.AppSupportDelegate;
import com.hellofyc.base.app.activity.BaseActivity;
import com.hellofyc.base.content.PermissionHelper;

/**
 *
 * Create on 2015年4月13日 下午6:22:40
 *
 * @author Yucun Fang
 */
public class BaseFragment extends Fragment implements OnClickListener {

	protected static final int RESULT_CANCELED		 = 0;
    protected static final int RESULT_OK			 = -1;

	/**
	 * Do not use direct please use {@link #getAppSupportDelegate()}}
	 */
	private AppSupportDelegate mActivityDelegate;

	public View findViewById(int resId) {
        View v = getView();
        if (v == null) return null;
        return v.findViewById(resId);
	}

    public void setScreenFull(boolean full) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity)getActivity()).setScreenFull(full);
        }
    }

    public BaseActivity getBaseActivity() {
        Activity activity = getActivity();
        if (activity instanceof BaseActivity) {
            return (BaseActivity) super.getActivity();
        } else {
            throw new RuntimeException("It's activity is not extends the BaseActivity!");
        }
    }

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isResumed() && isVisibleToUser) {
			onResumedVisible();
		}
	}

    public void setOnKeyboardVisibleListener (AppSupportDelegate.OnKeyboardVisibleListener listener) {
        getAppSupportDelegate().setOnKeyboardVisibileListener(listener);
    }

	public void onResumedVisible() {
	}

	@Override
	public void onResume() {
		super.onResume();
		if (getUserVisibleHint()) {
			onResumedVisible();
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

	public void setViewsOnClickListener(View... views) {
		getAppSupportDelegate().setViewsOnClickListener(this, views);
	}

	public void setViewsOnClickListener(int... viewResIds) {
		getAppSupportDelegate().setViewsOnClickListener(this, viewResIds);
	}

	public LayoutInflater getLayoutInflater() {
		return LayoutInflater.from(getActivity());
	}

	public final int getScreenWidth() {
		return getAppSupportDelegate().getScreenWidth();
	}

	public final int getScreenHeight() {
		return getAppSupportDelegate().getScreenWidth();
	}

	public final float getScreenDensity() {
		return getAppSupportDelegate().getScreenDensity();
	}

	protected Intent getIntent() {
		return getActivity().getIntent();
	}

	public void setResult(int resultCode) {
		getActivity().setResult(resultCode);
	}

	public void onBackPressed() {
		getActivity().onBackPressed();
	}

	protected String[] getStringArray(@ArrayRes int id) {
		return getActivity().getResources().getStringArray(id);
	}

	protected int getColorCompat(@ColorRes int id) {
        return getAppSupportDelegate().getColor(id);
	}

	public Drawable getDrawableCompat(@DrawableRes int id) {
		return getAppSupportDelegate().getDrawable(id);
	}

	public final boolean checkPermission(String permission) {
		return getAppSupportDelegate().checkPermission(permission);
	}

	public final void requestPermission(String permission) {
		getAppSupportDelegate().requestPermission(this, permission);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (!(getActivity() instanceof BaseActivity)) {
			PermissionHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults, requestCode != 0);
		}
	}

	@Override
	public void onClick(View v) {
	}

	public AppSupportDelegate getAppSupportDelegate() {
		if (mActivityDelegate == null) {
			mActivityDelegate = AppSupportDelegate.create(this);
		}
		return mActivityDelegate;
	}

}
