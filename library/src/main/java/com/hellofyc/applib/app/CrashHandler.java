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

package com.hellofyc.applib.app;

import android.content.Context;
import android.os.Build;

import com.hellofyc.applib.util.FileUtils;
import com.hellofyc.applib.util.Flog;
import com.hellofyc.applib.util.PackageUtils;
import com.hellofyc.applib.util.TimeUtils;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Yucun Fang
 * @since 2015年4月14日 下午2:33:11
 */
public class CrashHandler implements UncaughtExceptionHandler {
	static final boolean DEBUG = true;
	
	private Context mContext;
	private UncaughtExceptionHandler mDefaultHandler;
	
	private static CrashHandler mInstance;
	private Map<String, String> mInfos = new HashMap<>();
	
	private CrashHandler(){/*Do not new me*/}
	
	private CrashHandler(Context context) {
		mContext = context;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
//		uploadLog();
	}
	
	public static CrashHandler startMonitor(Context context) {
		if (mInstance == null) {
			mInstance = new CrashHandler(context);
		}
		Thread.setDefaultUncaughtExceptionHandler(mInstance);
		return mInstance;
	}
	
//	private void uploadLog() {
//		Flog.getInstance(mContext).uploadCrashLogByFtp(null);
//	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		Flog.e(ex);
		handleException(ex);
		if (!handleException(ex) && mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
//			startMainActivity();
			exit();
		}
	}

//	void startMainActivity() {
//		Intent intent = new Intent(mContext.getApplicationContext(), TestActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Bundle options = ActivityOptionsCompat.makeCustomAnimation(mContext, android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
//        ActivityCompat.startActivities(mContext, new Intent[]{intent}, options);
//	}
	
	private boolean handleException(Throwable ex) {
		collectDeviceInfo();
		saveCrashInfoToFile(ex);
		return true;
	}
	
	private void collectDeviceInfo() {
		try {
			mInfos.put("versionName", PackageUtils.getVersionName(mContext, mContext.getPackageName()));
			mInfos.put("versionCode", PackageUtils.getVersionCode(mContext, mContext.getPackageName()) + "");
		} catch (Exception e) {
			Flog.e(e);
		}
		Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                mInfos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                Flog.e(e);
            }
        }
	}
	
	private void saveCrashInfoToFile(Throwable ex) {
		StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : mInfos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key);
            sb.append("=");
            sb.append(value);
            sb.append("\n");
        }
  
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        sb.append(writer.toString());
        try {
            String fileName = "crash-" + TimeUtils.getCurrentTime(TimeUtils.TEMPLATE_DATE_TIME_FILENAME) + ".log";
            File dir = new File(mContext.getCacheDir(), "logs");
            FileUtils.createDir(dir);
            FileUtils.writeString(sb.toString(), dir, fileName);
        } catch (Exception e) {
            Flog.e(e);
        }
	}
	
	private void exit() {
		if (DEBUG) Flog.i("exit");
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(1);
	}
}
