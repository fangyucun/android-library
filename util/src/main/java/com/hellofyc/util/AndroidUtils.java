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

package com.hellofyc.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.app.Instrumentation;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.os.StrictMode;
import android.provider.Settings;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Android Tool
 *
 * Create on 2014年12月6日 下午12:13:44
 *
 * @author Jason Fang
 */
public final class AndroidUtils {
	private static final boolean DEBUG = false;

    public static void killProcess(int pid) {
        Process.killProcess(pid);
    }

    public static String getCurrentProcessName(@NonNull Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            List<RunningAppProcessInfo> runningAppProcessInfoList = activityManager.getRunningAppProcesses();
            if (runningAppProcessInfoList != null && runningAppProcessInfoList.size() > 0) {
                for (RunningAppProcessInfo processInfo : runningAppProcessInfoList) {
                    if (Process.myPid() == processInfo.pid) {
                        return processInfo.processName;
                    }
                }
            }
        }
        return null;
    }

	/**
	 * 屏幕保持常亮
	 */
    @RequiresPermission(Manifest.permission.WAKE_LOCK)
	public static void setKeepScreenOnEnabled(@NonNull Activity activity) {
		activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                , WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	public static int getStatusBarHeight(@NonNull Activity activity) {
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		return frame.top;
	}

	/**
	 * 设置StrictMode
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void setStrictModeEnabled() {
		 StrictMode.ThreadPolicy.Builder tpBuilder = new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog();
         StrictMode.VmPolicy.Builder vpBuilder = new StrictMode.VmPolicy.Builder().detectAll().penaltyLog();
         if (Build.VERSION.SDK_INT >= 11) {
        	 tpBuilder.penaltyFlashScreen();
         }
         StrictMode.setThreadPolicy(tpBuilder.build());
         StrictMode.setVmPolicy(vpBuilder.build());
	}

	/**
	 * 清除应用默认设置
	 * @param context context
	 * @param packageName name
	 */
	public static void clearDefaultSettings(@NonNull Context context, @NonNull String packageName) {
		Intent intent = new Intent();
		intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		Uri uriAppSettings = Uri.fromParts("package", packageName, null);
		intent.setData(uriAppSettings);
		context.startActivity(intent);
	}

	public static boolean isDownloadManagerEnable(@NonNull Context context) {
		int state = context.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");
		if (DEBUG) FLog.i("===state===" + state);
		switch (state) {
			case PackageManager.COMPONENT_ENABLED_STATE_DISABLED:
			case PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED:
			case PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER:
				return false;
		}
		return true;
	}

	/**
     * 杀进程
     */
    public static void forceStopPackage(@NonNull Context context, @NonNull String packageName){
    	ActivityManager aManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    	try {
            Reflect.on(aManager).call("forceStopPackage", packageName);
    	} catch (Exception e) {
    		FLog.e(e);
    	}
    }

	/**
     * 获取可用的内存大小
     */
	public static long getAvailableMemorySize(@NonNull Context context) {
		ActivityManager aManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo info = new MemoryInfo();
		aManager.getMemoryInfo(info);
		return info.availMem;
	}

	/**
	 * 获取堆大小
	 * @param context context
	 * @return int
	 */
	public static int getHeapSize(@NonNull Context context) {
		ActivityManager aManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		return aManager.getMemoryClass();
	}

	/**
     * 获取桌面信息
     */
    public static List<String> getLauncherInfo(@NonNull Context context) {
    	List<String> names = new ArrayList<>();
		Intent intent = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> resolveInfo = context.getPackageManager()
				.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

		for (ResolveInfo ri : resolveInfo) {
			names.add(ri.activityInfo.packageName);
		}
		return names;
	}

	public static String getMarketAppLink(@NonNull String packageName) {
	    return "market://details?id=" + packageName;
	}

	/**
	 * 获取app可占用的内存大小
	 * @return MB
	 */
	public static int getMemoryClass(@NonNull Context context) {
		return ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
	}

	/**
	 * 获取Meta-Data的值
	 */
    public static String getMetaValue(@NonNull Context context, @NonNull Class<?> cls, @NonNull String metaKey) {
        try {
        	PackageItemInfo pii = null;
        	if (Application.class.isAssignableFrom(cls)) {
        		pii = context.getPackageManager()
        				.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        	} else if (Activity.class.isAssignableFrom(cls)) {
        		pii = context.getPackageManager()
                        .getActivityInfo(new ComponentName(context, cls), PackageManager.GET_META_DATA);
        	} else if (Service.class.isAssignableFrom(cls)) {
        		pii = context.getPackageManager()
        				.getServiceInfo(new ComponentName(context, cls), PackageManager.GET_META_DATA);
        	} else if (BroadcastReceiver.class.isAssignableFrom(cls)) {
        		pii = context.getPackageManager()
        				.getReceiverInfo(new ComponentName(context, cls), PackageManager.GET_META_DATA);
        	}

        	if (pii != null) {
        		Bundle metaData = pii.metaData;
                if (metaData != null) {
                    return String.valueOf(metaData.get(metaKey));
                }
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

	/**
     * 获取签名密钥
     */
    public static String getSignaturePublicKey(@NonNull Context context, @NonNull String packageName) {
    	X509Certificate cert = getX509Certificate(context, packageName);
        if (cert == null) return "";
    	String publicKey = cert.getPublicKey().toString();
    	int start = publicKey.indexOf("modulus=") + 8;
    	int end = publicKey.indexOf(",");
    	return publicKey.substring(start, end);
    }

    /**
     * Get signature md5
     */
    public static String getSignatureMD5(@NonNull Context context, @NonNull String packageName) {
    	try {
			Signature[] signatures = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures;
            if (signatures != null && signatures.length > 0) {
                return MD5Utils.encode(signatures[0].toByteArray());
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
		}
        return "";
    }


	/**
     * 获取系统属性值
     * @param propertyName name
     * @return string
     */
    @Deprecated
    public static String getSystemProperty(@NonNull String propertyName) {
        BufferedReader reader = null;
        try {
            java.lang.Process p = Runtime.getRuntime().exec("getprop " + propertyName);
            reader = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            return reader.readLine();
        } catch (IOException e) {
        	FLog.e(e);
        } finally {
            IoUtils.close(reader);
        }
        return "UNKNOWN";
    }

	/**
     * Requires Permission:
     * {@link Manifest.permission#GET_TASKS}
     * @param context context
     * @return ComponentName
     */
    @SuppressWarnings("deprecation")
	public static ComponentName getTopComponentName(@NonNull Context context) {
	    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    ComponentName cm = null;
	    try {
			List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1);
			cm = taskInfo.get(0).topActivity;
	    } catch (SecurityException e) {
	    	FLog.e("Requires Permission: android.Manifest.permission#GET_TASKS");
	    }
		return cm;
    }

	/**
     * 获取X509证书
     * @param context context
     * @param packageName name
     * @return X509Certificate
     */
    public static X509Certificate getX509Certificate(@NonNull Context context, @NonNull String packageName) {
    	List<PackageInfo> packageInfoList = context.getPackageManager()
        		.getInstalledPackages(PackageManager.GET_SIGNATURES);

        byte[] signatureByteArray = null;
        for (PackageInfo info : packageInfoList) {
        	if (info.packageName.equals(packageName)) {
        		signatureByteArray = info.signatures[0].toByteArray();
        	}
        }
        if (signatureByteArray == null) return null;

		try {
			CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
			return (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(signatureByteArray));
		} catch (CertificateException e) {
			if (DEBUG) FLog.e(e);
		}
		return null;
    }

	public static void hideInputMethod(@NonNull EditText editText) {
		InputMethodManager imManager = ((InputMethodManager) editText.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE));
		imManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

    public static void hideInputMethod(@NonNull EditText editText, boolean isClearFocus, boolean isEmptyContent) {
        hideInputMethod(editText);
        if (isClearFocus) {
            editText.clearFocus();
        }
        if (isEmptyContent) {
            editText.setText("");
        }
    }

	public static void showInputMethod(@NonNull EditText editText) {
		InputMethodManager imManager = ((InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
		imManager.showSoftInput(editText, 0);
	}

    public static void showSoftKeyboard(@NonNull View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void showSoftKeyboard(@NonNull Window window) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

	/**
	 * 创建快捷方式
	 */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresPermission(Manifest.permission.INSTALL_SHORTCUT)
	public static void installShortcut(@NonNull Context context, @NonNull String name, int iconResId, @NonNull Class<?> targetClass) {
        if (isShortcutInstalled(context, name)) {
            return;
        }

        Intent targetIntent = new Intent(context, targetClass);
        targetIntent.setAction(Intent.ACTION_MAIN);
        targetIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        targetIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, iconResId));
		shortcutIntent.putExtra("duplicate", false);
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, targetIntent);
		context.sendOrderedBroadcast(shortcutIntent, null);
	}

    /**
     *
     * @param context context
     * @return boolean
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresPermission(Manifest.permission.INSTALL_SHORTCUT)
    private static boolean isShortcutInstalled(@NonNull Context context, @NonNull String name) {
        ContentResolver cr = context.getContentResolver();
        final String AUTHORITY = getAuthorityFromPermission(context);
        final Uri contentUri = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");
        Cursor c = null;
        try {
            c = cr.query(contentUri, new String[]{"title", "iconPackage"}, "title=? AND iconPackage=?", new String[]{name, context.getPackageName()}, null);
            if (c != null && c.getCount() > 0) {
                return true;
            }
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        } finally {
            IoUtils.close(c);
        }
        return false;
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
                        if ("com.android.launcher.permission.READ_SETTINGS".equals(info.readPermission)) {
                            return info.authority;
                        }
                    }
                }
            }
        }
        return "";
    }

    /**
     * 移除快捷方式
     */
    @RequiresPermission(Manifest.permission.UNINSTALL_SHORTCUT)
    public static void uninstallShortcut(@NonNull Context context, @NonNull String name, @NonNull Intent targetIntent) {
        Intent shortcutIntent = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, targetIntent);
        context.sendBroadcast(shortcutIntent);
    }

	/**
     * 判断Launcher而是否在顶栈
     */
    public static boolean isLauncherTop(@NonNull Context context) {
    	List<String> strList = getLauncherInfo(context);
    	if(CollectionUtils.isEmpty(strList)) return false;

    	ComponentName cm = getTopComponentName(context);
    	return cm != null && strList.contains(cm.getPackageName());
    }

    /**
	 * 服务是否在运行
	 */
	public static boolean isServiceRunning(@NonNull Context context, @NonNull String className) {
		if (TextUtils.isEmpty(className)) return false;

		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(Integer.MAX_VALUE);
		if (CollectionUtils.isEmpty(runningServices)) return false;

		for (ActivityManager.RunningServiceInfo info : runningServices) {
			if (info.service.getClassName().equals(className)) {
				return true;
			}
		}
		return false;
	}

    /**
     * 杀进程
     */
    @RequiresPermission(Manifest.permission.KILL_BACKGROUND_PROCESSES)
    public static void killBackgroundProcesses(@NonNull Context context, String processName){
    	if (TextUtils.isEmpty(processName)) return;
    	
    	ActivityManager aManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    	List<RunningAppProcessInfo> appInfoList = aManager.getRunningAppProcesses();
    	if (CollectionUtils.isEmpty(appInfoList)) return;
    	
    	for (RunningAppProcessInfo appInfo : appInfoList) {
    		if (processName.equals(appInfo.processName)) {
    			String[] pkgList = appInfo.pkgList;
    			if (pkgList == null || pkgList.length == 0) continue;
    			for (String pkg : pkgList) {
    				aManager.killBackgroundProcesses(pkg);
    			}
    			return;
    		}
    	}
    }
    
    /**
	 * 模拟按键
	 */
	public static void sendKeyCode(final int keyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(keyCode);
				} catch (Exception e) {
					if (DEBUG) FLog.e(e);
				}
			}
		}.start();
	}
    
    /**
	 * 禁用组件
	 */
    public static void setComponentEnabledSetting(@NonNull Context context, @NonNull Class<?> cls, boolean enabled) {
        PackageManager pm = context.getPackageManager();
        ComponentName cn = new ComponentName(context, cls);
        pm.setComponentEnabledSetting(cn,
                enabled ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    /**
	 * 设置密码可见
	 */
	public static void setPasswordVisibility(@NonNull EditText input, boolean visibility) {
		if (visibility) {
			input.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
			input.setSelection(input.length());
		} else {
			input.setTransformationMethod(PasswordTransformationMethod.getInstance());
			input.setSelection(input.length());
		}
	}
    
    /**
	 * 设置代理
	 */
	public static void setProxy(@NonNull Context context, String proxy) {
		Settings.System.putString(context.getContentResolver(),
				"http_proxy", proxy + ":8080");
	}
	
	public static String takeScreenShot(@NonNull Activity activity, String dirPath) throws IOException {
		File file;
		String name = TimeUtils.getCurrentDateTime() + ".png";
		
		if (TextUtils.isEmpty(dirPath)) {
			file = new File(StorageUtils.getExternalStorageRootDir(), name);
		} else {
			file = new File(dirPath, name);
		}
		
		View view = activity.getWindow().getDecorView();
		Bitmap bitmap = Bitmap.createBitmap(DeviceUtils.getScreenSize(activity).x, DeviceUtils.getScreenSize(activity).y, Bitmap.Config.ARGB_8888);
		view.draw(new Canvas(bitmap));
		
		try {
			bitmap.compress(CompressFormat.PNG, 100, new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			if (DEBUG) FLog.e(e.getMessage());
		}
		return file.getPath();
	}
	
	public static String takeScreenShot2(@NonNull Activity activity, String path) throws IOException {
		if (TextUtils.isEmpty(path)) path = StorageUtils.getExternalStorageRootDir().getPath();
		
		View decorView = activity.getWindow().getDecorView();
		decorView.setDrawingCacheEnabled(true);
		Bitmap bitmap = decorView.getDrawingCache();
		
		File file = new File(path);
		try {
			bitmap.compress(CompressFormat.PNG, 100, new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			if(DEBUG) FLog.e(e.getMessage());
		}
		return file.getPath();
	}

	/**
	 * Update Language
	 */
	public static void updateLanguage(@NonNull Context context, Locale locale) {
		Resources resource = context.getResources();
		Configuration config = resource.getConfiguration();
		config.locale = locale;
		resource.updateConfiguration(config, resource.getDisplayMetrics());
	}
	
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static boolean isAdbEnabled(@NonNull Context context) {
		try {
			if (Build.VERSION.SDK_INT >= 17) {
				return Global.getInt(context.getContentResolver(), Global.ADB_ENABLED) == 1;
			} else {
				return Secure.getInt(context.getContentResolver(), Secure.ADB_ENABLED) == 1;
			}
		} catch (SettingNotFoundException e) {
			FLog.e(e);
			return false;
		}
	}
	
	/**
	 * Copy the text to clipboard
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressWarnings("deprecation")
	public static void copyTextToClipboard(@NonNull Context context, String text) {
		if (Build.VERSION.SDK_INT >= 11) {
			ClipboardManager manager = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
			manager.setPrimaryClip(ClipData.newPlainText(null, text));
		} else {
			android.text.ClipboardManager manager = (android.text.ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
			manager.setText(text);
		}
	}
	
	public static String getVMVersion() {
		return System.getProperty("java.vm.version");
	}
	
	private AndroidUtils() {/* Do not new me */}
}
