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

package com.hellofyc.base.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import com.hellofyc.base.util.FLog;

public class BaseService extends Service {

	private static final boolean DEBUG = false;
	
	protected static final int NOTIFICATION_FOREGROUND_ID = 1120;
	
	static BaseService sBaseService = null;
	
	private Intent mKernalServiceIntent = null; 
	
	protected int mVersion;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}
	
	private void init() {
		mVersion = Build.VERSION.SDK_INT;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}
	
	protected void startForegroundCompat() {
		try {
			if (Build.VERSION.SDK_INT < 18) {
				startForeground(NOTIFICATION_FOREGROUND_ID, new Notification());
			} else {
				sBaseService = this;
				mKernalServiceIntent = new Intent(this, KernalService.class);
				startService(mKernalServiceIntent);
			}
		} catch (Exception e) {
			if(DEBUG) FLog.e(e);
		}
	}
	
	protected void stopForegroundCompat() {
		if (Build.VERSION.SDK_INT < 18) {
			stopForeground(true);
		} else {
			try {
				stopService(mKernalServiceIntent);
			} catch (Exception e) {
				FLog.e(e);
			}
		}
	}
	
	public static class KernalService extends Service {

		@Override
		public IBinder onBind(Intent intent) {
			return null;
		}

		@Override
		public void onCreate() {
			super.onCreate();
		}

		@Override
		public int onStartCommand(Intent intent, int flags, int startId) {
			if (BaseService.sBaseService == null) return Service.START_NOT_STICKY;
					
			try {
				BaseService.sBaseService.startForeground(NOTIFICATION_FOREGROUND_ID, new Notification());
				startForeground(NOTIFICATION_FOREGROUND_ID, new Notification());
				BaseService.sBaseService.stopForeground(true);
			} catch (Exception e) {
				if(DEBUG) FLog.e(e);
			}
			return Service.START_NOT_STICKY;
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			stopForeground(true);
		}
		
	}

}
