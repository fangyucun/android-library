/*
 * Copyright (C) 2014 Jason Fang ( ijasonfang@gmail.com )
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hellofyc.base.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.view.View;

import com.hellofyc.base.utils.FLog;

/**
 * Ui Tool
 * Create on 2014年11月26日 下午10:08:07
 * @author Jason Fang
 */
public class UiUtils {
	
	private static final boolean DEBUG = false;
	
	public static final int MSG_NOTIFY_LIGHT = 1;
	
	private static InternalHandler mHandler = new InternalHandler();
	
	static class InternalHandler extends Handler {

		private Message mMsg = null;
		
		public InternalHandler() {
			super(Looper.getMainLooper());
		}
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			final int what = msg.what;
			switch (what) {
			case MSG_NOTIFY_LIGHT:
				HandlerResult result = (HandlerResult)msg.obj;
				int flag = msg.arg1;
				
				if (DEBUG) FLog.i("flag:" + flag);

				if (flag == 0) {
					ViewCompat.setAlpha(result.mView, ViewCompat.getAlpha(result.mView) - 0.01f);

					if (ViewCompat.getAlpha(result.mView) <= 0.5f) {
						flag = 1;
					}
				} else {
					ViewCompat.setAlpha(result.mView, ViewCompat.getAlpha(result.mView) + 0.01f);

					if (ViewCompat.getAlpha(result.mView) >= 1.0f) {
						flag = 0;
					}
				}

				mMsg = obtainMessage(MSG_NOTIFY_LIGHT);
				mMsg.obj = result;
				mMsg.arg1 = flag;
				sendMessageDelayed(mMsg, 30L);
				break;
			}
		}

	}

	static class HandlerResult {
		View mView;

		public void setView(View v) {
			mView = v;
		}
	}

	/**
	 * 开启呼吸灯效果
	 */
	public static void startNotifyLight(View view) {
		if (view == null) {
			if (DEBUG) FLog.e("ArgumentException view is null!");
			return;
		}
		
		Message msg = mHandler.obtainMessage(MSG_NOTIFY_LIGHT);
		HandlerResult result = new HandlerResult();
		result.mView = view;
		msg.obj = result;
		mHandler.sendMessage(msg);
	}
	
	/**
	 * 关闭呼吸灯效果
	 */
	public static void stopNotifiLight() {
		mHandler.removeMessages(MSG_NOTIFY_LIGHT);
	}
	
	private UiUtils() {/* Do not new me!*/}
}
