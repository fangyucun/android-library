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
 */package com.hellofyc.base.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Process;

import com.hellofyc.base.app.activity.ActivityStack;
import com.hellofyc.base.util.FLog;

import java.util.List;

/**
 * Create on 2015年4月10日 上午10:31:29
 *
 * @author Yucun Fang
 */
public class BaseApplication extends Application {
	private static final boolean DEBUG = false;
	
	public static BaseApplication mApplication;
	
	private boolean mIsMonitorAppRunningBackground = false;
	private boolean mIsAppRunningForground = false;
	
	@Override
	public void onCreate() {
		super.onCreate();
		if (DEBUG) FLog.i("BaseApplication onCreate");
		init();
	}
	
	private void init() {
		mApplication = this;
		
		CrashHandler.startMonitor(getApplicationContext());
		
		registerActivityLifecycleCallbacks(new DefaultActivityLifecycleCallbacks());
	}
	
	/**
	 * 退出应用
	 */
	public void exit() {
		ActivityStack.getInstance().closeAllActivities();
		Process.killProcess(Process.myPid());
	}
	
	/**
	 * 设置是否监听App运行在后台
	 */
	protected void setMonitorAppRunningBackgroundEnabled(boolean enabled) {
		mIsMonitorAppRunningBackground = enabled;
	}
	
	@TargetApi(14)
	class DefaultActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {

		@Override
		public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
			ActivityStack.getInstance().add(activity);
		}

		@Override
		public void onActivityStarted(Activity activity) {
		}

		@Override
		public void onActivityResumed(Activity activity) {
			if (!mIsMonitorAppRunningBackground) return;
			if (!mIsAppRunningForground && isAppRunningForeground()) {
				mIsAppRunningForground = true;
				onAppRunningForground();
			}
		}

		@Override
		public void onActivityPaused(Activity activity) {
		}

		@Override
		public void onActivityStopped(Activity activity) {
			if (!mIsMonitorAppRunningBackground) return;
			if (!isAppRunningForeground()) {
				mIsAppRunningForground = false;
				onAppRunningBackground();
			}
		}

		@Override
		public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
		}

		@Override
		public void onActivityDestroyed(Activity activity) {
			ActivityStack.getInstance().remove(activity);
		}
		
	}
	
	/**
	 * 判断App是否运行在前台
	 */
	private boolean isAppRunningForeground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getPackageName();
        
        List<RunningAppProcessInfo> processList = activityManager.getRunningAppProcesses();
        if (processList == null) return false;

        for (RunningAppProcessInfo info : processList) {
            if (info.processName.equals(packageName) 
            		&& info.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
            	return true;
            }
        }
        return false;
	}

	public boolean isAppRunningForground() {
		return mIsAppRunningForground;
	}
	
	/**
	 * Run Background Invoke!
	 */
	protected void onAppRunningBackground() {
	}
	
	/**
	 * Run Forground Invoke!
	 */
	protected void onAppRunningForground() {
	}
	
}
