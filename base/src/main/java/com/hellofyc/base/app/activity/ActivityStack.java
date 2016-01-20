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

import android.app.Activity;
import android.support.v4.app.ActivityCompat;

import java.util.Stack;

/**
 * Activity栈
 * Create on 2014年11月22日 下午9:51:42
 *
 * @author Jason Fang
 */
public class ActivityStack {

	private static ActivityStack sInstance;
	private Stack<Activity> mActivityStack = new Stack<>();
	
	public static ActivityStack getInstance() {
		if (sInstance == null) {
			sInstance = new ActivityStack();
		}
		return sInstance;
	}

	public void add(Activity a) {
		if (a == null) return;
		if (!mActivityStack.contains(a)) {
			mActivityStack.add(a);
		}
	}
	
	public void remove(Activity a) {
		if (a == null) return;
		
		if (mActivityStack.contains(a)) {
			mActivityStack.remove(a);
		}
	}
	
	public void closeAllActivities() {
		if (mActivityStack.empty()) return;
		
		for (Activity a : mActivityStack) {
			if (a == null || a.isFinishing()) continue;
			ActivityCompat.finishAffinity(a);
		}
		mActivityStack.clear();
	}
	
}
