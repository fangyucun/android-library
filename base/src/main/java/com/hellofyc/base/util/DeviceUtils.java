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

package com.hellofyc.base.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings.Secure;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.WindowManager;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Device tool
 * Create on 2014年12月6日 下午12:16:36
 *
 * @author Jason Fang
 */
public final class DeviceUtils {
	private static final boolean DEBUG = false;

	public static final String DEFAULT_IMEI = "default_imei";
	public static final String DEFAULT_SERIAL = "default_serial";

	private static String sDeviceUniqueId;

	public static boolean isPhone(@NonNull Context context) {
		TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephony.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
	}

	public static boolean isTablet(@NonNull Context context) {
		return (context.getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static boolean isEmulator() {
		return (Build.MODEL.equals("sdk")) || (Build.MODEL.equals("google_sdk"));
	}

	public static boolean isGenymotion() {
		return Build.DEVICE.startsWith("vbox86");
	}

	public static ActivityManager.MemoryInfo getMemoryInfo(@NonNull Context context) {
		ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
		activityManager.getMemoryInfo(memoryInfo);
		return memoryInfo;
	}

	public static int getHeapSize(@NonNull Context context) {
		ActivityManager aManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		return aManager.getMemoryClass();
	}

	public static String getSerial() {
		if (TextUtils.isEmpty(Build.SERIAL)) {
			return DEFAULT_SERIAL;
		}
    	return Build.SERIAL;
    }

	/**
	 * 获取IMEI
	 */
    @SuppressLint("HardwareIds")
	@RequiresPermission(Manifest.permission.READ_PHONE_STATE)
	public static String getIMEI(@NonNull Context context) {
		try {
			TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			return Objects.toString(tm.getDeviceId(), "");
		} catch (SecurityException e) {
			FLog.e("Requires android.Manifest.permission#READ_PHONE_STATE");
		}
		return DEFAULT_IMEI;
	}

	@SuppressLint("HardwareIds")
	public static String getAndroidId(@NonNull Context context) {
		return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
	}

	/**
	 * 获取设备唯一号
	 */
	@RequiresPermission(Manifest.permission.READ_PHONE_STATE)
	public static String getDeviceUniqueId(@NonNull Context context) {
		if (!TextUtils.isEmpty(sDeviceUniqueId)) {
			return sDeviceUniqueId;
		}
		String imei = getIMEI(context);
		String androidId = getAndroidId(context);
		String serial = getSerial();
		return MD5Utils.encode(imei + androidId + serial);
	}

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public static Point getScreenSize(@NonNull Context context) {
		Point point = new Point();
		WindowManager wM = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

		if (Build.VERSION.SDK_INT >= 13) {
			wM.getDefaultDisplay().getSize(point);
		} else {
			point.x = wM.getDefaultDisplay().getWidth();
			point.y = wM.getDefaultDisplay().getHeight();
		}
		return point;
	}

	public static int getScreenDensity(@NonNull Context context) {
		return context.getResources().getDisplayMetrics().densityDpi;
	}

	/**
	 * 设备是否支持该属性
	 *
	 * Use {@link PackageManager#hasSystemFeature(String)}
	 */
	public static boolean isSupportFeature(@NonNull Context context, @NonNull String feature) {
		FeatureInfo[] infos = context.getPackageManager().getSystemAvailableFeatures();
		if (infos == null || infos.length == 0) return false;
		for (FeatureInfo info : infos) {
			if (DEBUG) FLog.i("Feature Name:" + info.name);
			if (feature.equals(info.name)) {
				return true;
			}
		}
		return false;
	}

	private DeviceUtils(){/*Do not new me!*/}
}
