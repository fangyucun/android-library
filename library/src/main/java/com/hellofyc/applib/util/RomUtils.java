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
 */package com.hellofyc.applib.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.os.Build;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 版本工具类
 * Create on 2014-10-10 下午7:17:29
 *
 * @author Yucun
 */
public final class RomUtils {
	static final boolean DEBUG = false;
	
	public static final String PERMISSION_READ_SETTINGS = "com.android.launcher.permission.READ_SETTINGS";

    public enum ROM {

        DEFAULT(0),

        MIUI_V5(1),

        MIUI_V6(2),

        COLOROS(3),

        FLYME(4),

        EMUI(5),

        SMARTISAN_OS(6),

        H2OS(7);

        private int mIntValue;

        ROM (int intValue) {
            mIntValue = intValue;
        }

        public int getIntValue() {
            return mIntValue;
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static ROM getRom() {
        if (checkMiuiV5()) {
            return ROM.MIUI_V5;
        }
        if (checkMiuiV6()) {
            return ROM.MIUI_V6;
        }
        if (checkEMUI()) {
            return ROM.EMUI;
        }
        if (checkColorOS()) {
            return ROM.COLOROS;
        }
        if (checkH2OS()) {
            return ROM.H2OS;
        }
        if (checkSmartisanOS()) {
            return ROM.SMARTISAN_OS;
        }
        if (checkFlyme()) {
            return ROM.FLYME;
        }
        return ROM.DEFAULT;
    }

    public static boolean checkH2OS() {
        return false;
    }

    public static boolean checkEMUI() {
        return false;
    }

	/**
	 * Flyme OS
	 * 
	 * @return true or false
	 */
	public static boolean checkFlyme() {
		try {
			final Method method = Build.class.getMethod("hasSmartBar");
			return method != null;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Color OS
	 * 
	 * @return true or false
	 */
	public static boolean checkColorOS() {
		try {
			Field rom = ReflectUtils.getDeclaredField(Build.class, "ROM_DIFF_VERSION");
			String romStr = rom.get(null).toString();
			if (romStr.startsWith("ColorOS")) return true;
		} catch (Exception e) {
            if (DEBUG) FLog.e(e);
		}
        return false;
	}
	
	/**
	 * MIUI V5
	 * 
	 * @return true or false
	 */
	public static boolean checkMiuiV5() {
		return "V5".equalsIgnoreCase(AndroidUtils.getSystemProperty("ro.miui.ui.version.name"));
	}
	
	/**
	 * MIUI V6
	 * 
	 * @return true or false
	 */
	public static boolean checkMiuiV6() {
		return "V6".equalsIgnoreCase(AndroidUtils.getSystemProperty("ro.miui.ui.version.name"));
	}
	
	/**
	 * Smartisan OS WARN: just do Smartisan Device not Smartisan OS
	 * 
	 * @return true or false;
	 */
	public static boolean checkSmartisanOS() {
		return Build.BRAND.equals("smartisan");
	}
	
	/**
	 * 
	 * @param context context
	 * @return permission
	 */
	public static String getAuthorityFromPermission(Context context) {
		List<PackageInfo> piList = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
		if (piList != null) {
			for (PackageInfo pi : piList) {
				ProviderInfo[] infos = pi.providers;
				if (infos != null) {
					for (ProviderInfo info : infos) {
						if (PERMISSION_READ_SETTINGS.equals(info.readPermission)) {
							return info.authority;
						}
					}
				}
			}
		}
		return "";
	}

	private RomUtils() {/*Do not new me!*/}
}
