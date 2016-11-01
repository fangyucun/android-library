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

package com.hellofyc.base.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

/**
 * Toast Tool
 * Create on 2014年12月6日 下午12:12:47
 *
 * @author Jason Fang
 */
public final class ToastUtils {
	static final boolean DEBUG = false;
	
	private static final int TYPE_DEFAULT = 1;

	private static Toast mToast;

	public static void show(Context context, CharSequence text) {
		show(context, text, false);
	}
	
	public static void show(final Context context, final CharSequence text, final boolean isLongTime) {
		showInMainThread(context, text, isLongTime, TYPE_DEFAULT);
	}
	
	private static void doShow(Context context, CharSequence text, boolean isLongTime) {
		if (context == null) {
			throw new IllegalArgumentException("context can not be null");
		}
		
		if (text == null) text = "";
		
		if(mToast == null) {
			mToast = Toast.makeText(context, text, isLongTime ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
			mToast.setDuration(isLongTime ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
		}
		mToast.show();
	}

	static void showInMainThread(final Context context, final CharSequence text, final boolean isLongTime, final int type) {
		if (Looper.myLooper() != Looper.getMainLooper()) {
			if (context instanceof Activity) {
				((Activity) context).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						chooseToInvoke(context, text, isLongTime, type);
					}
					
				});
			}
			return;
		}
		chooseToInvoke(context, text, isLongTime, type);
	}
	
	static void chooseToInvoke(final Context context, final CharSequence text, final boolean isLongTime, final int type) {
		switch (type) {
		case TYPE_DEFAULT:
			doShow(context, text, isLongTime);
			break;
		}
	}
	
	private ToastUtils() {/*Do not new me*/}
}
