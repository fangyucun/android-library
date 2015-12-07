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

package com.hellofyc.applib.app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import com.hellofyc.applib.R;
import com.hellofyc.applib.util.Flog;

/**
 * Fragment Container
 * Create on 2014年12月2日 下午5:54:45
 *
 * @author Jason Fang
 */
public class SingleFragmentActivity extends BaseActivity {
	static final boolean DEBUG = false;

	public static final String EXTRA_SINGLE_FRAGMENT_ACTIVITY_PARAMS	 = "single_fragment_activity_params";
	public static final String EXTRA_FROM_WHERE							 = "from_where";
	public static final String EXTRA_FRAGMENT_CLASSNAME					 = "fragment_classname";
	public static final String EXTRA_FRAGMENT_TAG						 = "fragment_tag";
	public static final String EXTRA_FRAGMENT_ARGS						 = "fragment_args";
	public static final String EXTRA_FRAGMENT_EXTRA						 = "fragment_extra";
	
	private FragmentManager mFM;
	
	private String mFragmentClassName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FrameLayout layout = new FrameLayout(this);
		layout.setId(R.id.fragment_container);
		layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		setContentView(layout);
		
		init();
		setActivityContent();
	}
	
	private void init() {
		mFM = getSupportFragmentManager();
		
		mFragmentClassName = getIntent().getStringExtra(EXTRA_FRAGMENT_CLASSNAME);
	}
	
	private void setActivityContent() {
		try {
			Class<?> clazz = getClassLoader().loadClass(mFragmentClassName);
			Fragment fragment = (Fragment) clazz.newInstance();
			
			FragmentTransaction transaction = mFM.beginTransaction();
			transaction.replace(R.id.fragment_container, fragment);
			transaction.commit();
			
		} catch (Exception e) {
			if (DEBUG) Flog.e(e);
		}
	}
	
}
