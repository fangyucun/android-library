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

package com.hellofyc.base.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.hellofyc.base.AppSupportDelegate;
import com.hellofyc.base.activity.BaseActivity;
import com.hellofyc.base.util.FLog;

/**
 *
 * Create on 2015年4月13日 下午6:22:40
 *
 * @author Yucun Fang
 */
public class BaseFragment extends Fragment implements OnClickListener {
	private static final boolean DEBUG = false;
	
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
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
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
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (DEBUG) FLog.i("===setUserVisibleHint:" + isVisibleToUser);
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
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (getUserVisibleHint()) {
			onResumedVisible();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void startActivity(Intent intent) {
		startActivityForResult(intent, -1);
	}

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void startActivity(Intent intent, Bundle options) {
        getActivity().startActivity(intent, options);
    }

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
	}

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        getActivity().startActivityForResult(intent, requestCode, options);
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
	
	public void finish() {
        getActivity().finish();
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
	
	public void setViewsClickListener(View... views) {
		getAppSupportDelegate().setViewsClickListener(this, views);
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
	
	@Override
	public void onClick(View v) {
	}
	
	public <LC> void startLoader(int id, Bundle args, LoaderCallbacks<LC> callbacks) {
		getLoaderManager().restartLoader(id, args, callbacks);
	}
	
	public AppSupportDelegate getAppSupportDelegate() {
		if (mActivityDelegate == null) {
			mActivityDelegate = AppSupportDelegate.create(this);
		}
		return mActivityDelegate;
	}

}
