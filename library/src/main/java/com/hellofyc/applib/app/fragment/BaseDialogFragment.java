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

package com.hellofyc.applib.app.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

import com.hellofyc.applib.app.AppSupportDelegate;

public class BaseDialogFragment extends DialogFragment implements OnClickListener {

	protected static final int THEME = android.R.style.Theme;
	protected static final int THEME_LIGHT = android.R.style.Theme_Light;
	protected static final int THEME_LIGHT_PANEL = android.R.style.Theme_Light_Panel;
	protected static final int THEME_PANEL = android.R.style.Theme_Panel;
	protected static final int THEME_DIALOG = android.R.style.Theme_Dialog;

    private AppSupportDelegate mDelegate;

    private AppSupportDelegate getAppSupportDelegate() {
        if (mDelegate == null) {
            mDelegate = AppSupportDelegate.create();
        }
        return mDelegate;
    }

    public static DialogFragment newInstance(@NonNull Context context, Class<?> fclass) {
        return (DialogFragment) Fragment.instantiate(context, fclass.getName());
    }

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

    @Override
    public void onClick(View v) {

    }

    public <D> void startLoader(int id, Bundle args, LoaderManager.LoaderCallbacks<D> callbacks) {
        getLoaderManager().restartLoader(id, args, callbacks);
    }

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

    public View findViewById(int resId) {
        View v = getView();
        if (v == null) return null;
        return v.findViewById(resId);
    }

	public void setViewsClickListener(View... views) {
        getAppSupportDelegate().setViewsClickListener(this, views);
	}
	
	protected int getColor(int resId) {
		return ContextCompat.getColor(getActivity(), resId);
	}

    public int getScreenWidth() {
        return getAppSupportDelegate().getScreenWidth();
    }

    public void setWidth(float ratio) {
        setWidth((int) (getScreenWidth() * ratio));
    }
	
	protected void setWidth(int width) {
		WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
		params.width = width;
		getDialog().getWindow().setAttributes(params);
	}
	
	protected void setHeight(int height) {
		WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
		params.height = height;
		getDialog().getWindow().setAttributes(params);
	}

}
