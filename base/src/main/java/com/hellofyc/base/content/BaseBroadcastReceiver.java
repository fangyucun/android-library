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

package com.hellofyc.base.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hellofyc.base.utils.FLog;

public class BaseBroadcastReceiver extends BroadcastReceiver {
	private static final boolean DEBUG = false;
	
	protected int mReceiveCount = 0;
	
	public BaseBroadcastReceiver() {
		if (DEBUG) FLog.i("===BaseBroadcastReceiver===");
		mReceiveCount = 0;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (mReceiveCount < 20) {
			mReceiveCount++;
		}
	}
}
