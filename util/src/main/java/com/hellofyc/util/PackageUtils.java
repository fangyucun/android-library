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
 */package com.hellofyc.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Create on 2014年11月23日 下午1:50:34
 *
 * @author Jason Fang
 */
public final class PackageUtils {
	static final boolean DEBUG = false;
	
	public static final int FLAG_SYSTEM 	= 1;
	public static final int FLAG_NOT_SYSTEM = 1<<1;
	public static final int FLAG_ALL 		= FLAG_SYSTEM | FLAG_NOT_SYSTEM;
	
//	private QueryPackageSizeCallback mQueryCallback;
	
//	/**
//	 * get pacakge size
//	 * User Permission:
//	 * 				{@link android.Manifest.permission#GET_PACKAGE_SIZE}
//	 * @param packageName
//	 * @param callback
//	 * @deprecated Api19 is not working!
//	 */
//	@Deprecated
//	public void queryPackageSize(String packageName, QueryPackageSizeCallback callback) {
//		if (TextUtils.isEmpty(packageName)) return;
//		mQueryCallback = callback;
//		
//		try {
//			ReflectUtils.invokeMethod(mPM, "getPackageSizeInfo", packageName, new PackageStatsObserver());
//		} catch (Exception e) {
//			FLog.e(e);
//		}
//	}
	
//	public List<AppInfo> queryAppInfo() {
//		Intent mainIntent = new Intent();
//		mainIntent.setAction(Intent.ACTION_MAIN);
//		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//		List<ResolveInfo> resolveInfoList = mPM.queryIntentActivities(mainIntent, 0);
//		if (CollectionUtils.isEmpty(resolveInfoList)) return null;
//		
//		Collections.sort(resolveInfoList, new ResolveInfo.DisplayNameComparator(mPM));
//		List<AppInfo> appInfoList = new ArrayList<AppInfo>();
//		AppInfo appInfo = null;
//		for (ResolveInfo info : resolveInfoList) {
//			appInfo = new AppInfo();
//			appInfo.packageName = info.activityInfo.packageName;
//			appInfo.label = info.loadLabel(mPM).toString();
//		}
//		return appInfoList;
//	}
	
//	/**
//	 * PackageStats Observer
//	 * @author Jason Fang
//	 * @datetime 2014年11月23日 下午2:42:32
//	 */
//	public class PackageStatsObserver extends IPackageStatsObserver.Stub {
//
//		@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//		@Override
//		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
//				throws RemoteException {
//			
//			if (!succeeded) {
//				if (DEBUG) FLog.e("query failure!");
//				return;
//			}
//			
//			AppInfo appInfo = new AppInfo();
//			appInfo.cacheSize = pStats.cacheSize;
//			appInfo.dataSize = pStats.dataSize;
//			appInfo.codeSize = pStats.codeSize;
//			appInfo.totalSize = pStats.cacheSize + pStats.dataSize + pStats.codeSize;
//			
//			if (Build.VERSION.SDK_INT >= 14) {
//				appInfo.externalCacheSize = pStats.externalCacheSize;
//				appInfo.externalDataSize = pStats.externalDataSize;
//				appInfo.externalCodeSize = pStats.externalCodeSize;
//				appInfo.externalMediaSize = pStats.externalMediaSize;
//				appInfo.externalObbSize = pStats.externalObbSize;
//			} else {
//				appInfo.externalCacheSize = 0;
//				appInfo.externalDataSize = 0;
//				appInfo.externalCodeSize = 0;
//				appInfo.externalMediaSize = 0;
//				appInfo.externalObbSize = 0;
//			}
//			
//			if (DEBUG) {
//				FLog.i("cacheSize:" + appInfo.cacheSize +
//						", dataSize:" + appInfo.dataSize + 
//						", codeSize:" + appInfo.codeSize + 
//						", totalSize:" + appInfo.totalSize + 
//						", externalCacheSize:" + appInfo.externalCacheSize + 
//						", externalDataSize:" + appInfo.externalDataSize + 
//						", externalCodeSize:" + appInfo.externalCodeSize + 
//						", externalMediaSize:" + appInfo.externalMediaSize + 
//						", externalObbSize" + appInfo.externalObbSize );
//			}
//			
//			if (mQueryCallback != null) {
//				mQueryCallback.onQueryPackageSizeSuccess(appInfo);
//			}
//		}
//		
//	}
	
//	/**
//	 * 获取已安装的App
//	 * @param {@link #FLAG_ALL}, {@link #FLAG_SYSTEM}, {@link #FLAG_NOT_SYSTEM};
//	 */
//	public List<AppInfo> queryInstalledApps(int flag) {
//		List<AppInfo> resultList = new ArrayList<AppInfo>();
//		List<PackageInfo> pInfoList = mPM.getInstalledPackages(0);
//		AppInfo aInfo = null;
//		for (PackageInfo pInfo : pInfoList) {
//			aInfo = new AppInfo();
//			switch (flag) {
//			case FLAG_SYSTEM:
//				if ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
//					continue;
//				}
//				
//				if (DEBUG) FLog.i(pInfo.applicationInfo.flags + ", packageName:" + pInfo.packageName);
//				break;
//			case FLAG_NOT_SYSTEM:
//				if ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
//					continue;
//				}
//				
//				if (DEBUG) FLog.i(pInfo.applicationInfo.flags + ", packageName:" + pInfo.packageName);
//				break;
//			case FLAG_ALL:
//				
//				if (DEBUG) FLog.i(pInfo.applicationInfo.flags + ", packageName:" + pInfo.packageName);
//				break;
//			}
//			
//			aInfo.label = String.valueOf(mPM.getApplicationLabel(pInfo.applicationInfo));
//			aInfo.packageName = pInfo.packageName;
//			aInfo.versionName = pInfo.versionName;
//			aInfo.versionCode = pInfo.versionCode;
//			resultList.add(aInfo);
//		}
//		return resultList;
//	}
	
	public static FeatureInfo[] getSystemAvailableFeatures(Context context) {
		return getPackageManager(context).getSystemAvailableFeatures();
	}
	
	public static boolean hasSystemFeature(Context context, String name) {
		return getPackageManager(context).hasSystemFeature(name);
	}
	
	public static PackageManager getPackageManager(Context context) {
		return context.getPackageManager();
	}
	
	public static ApplicationInfo getApplicationInfo(Context context, String packageName) {
		PackageManager pm;
        try {
            pm = context.getPackageManager();
        } catch (RuntimeException e) {
            return null;
        }
        if (pm == null || TextUtils.isEmpty(packageName)) {
            return null;
        }
        
		try {
			return pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			FLog.e(e);
		}
		return null;
	}
	
	public static ApplicationInfo getApplicationInfo(Context context) {
        return getApplicationInfo(context, context.getPackageName());
	}
	
	public static PackageInfo getPackageInfoByApkPath(Context context, String apkPath) {
		if (TextUtils.isEmpty(apkPath)) return null;
        return getPackageManager(context).getPackageArchiveInfo(apkPath, 0);
	}
	
	public static PackageInfo getPackageInfo(Context context, String packageName) {
		try {
			return getPackageManager(context).getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			return null;
		}
	}
	
	public static int getUid(Context context, String packageName) {
		ApplicationInfo info = getApplicationInfo(context, packageName);
		if (info != null) {
			return info.uid;
		}
		return 0;
	}

	public static String getShareUserId(Context context, String packageName) {
		PackageInfo info = getPackageInfo(context, packageName);
		if (info != null) {
			return info.sharedUserId;
		}
		return null;
	}

	/**
	 * 获取App的版本号
	 */
	public static int getVersionCode(Context context, String packageName) {
		PackageInfo info = getPackageInfo(context, packageName);
		if (info != null) {
			return info.versionCode;
		}
		return 0;
	}
	
	public static int getVersionCode(Context context) {
		return getVersionCode(context, context.getPackageName());
	}
	
	public static String getVersionName(Context context) {
		return getVersionName(context, context.getPackageName());
	}
	
	/**
	 * 获取App的版本名称
	 */
	public static String getVersionName(Context context, String packageName) {
		PackageInfo info = getPackageInfo(context, packageName);
		if (info != null) {
			return info.versionName;
		}
		return "";
	}
	
	public static int getIconResId(Context context, String packageName) {
        ApplicationInfo info = getApplicationInfo(context, packageName);
		return info == null ? 0 : info.icon;
	}
	
	public static boolean isSameApp(Context context, Uri uri) {
        return uri != null && isSameApp(context, uri.getPath());
	}
	
	public static boolean isSameApp(Context context, String apkPath) {
		if (TextUtils.isEmpty(apkPath)) return false;
		
		PackageInfo info = getPackageInfoByApkPath(context, apkPath);
		if (info != null) {
			if (info.packageName.equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}
	
    /**
     * 获取App权限
     */
    public static String[] getAppPermissions(Context context, String packageName) {
    	if (context == null) return new String[0];
    	
    	PackageManager pManager = context.getPackageManager();
    	try {
			return pManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS).requestedPermissions;
		} catch (NameNotFoundException e) {
			FLog.e(e);
		}
    	return new String[0];
    }
    
    public static void installNormal(@NonNull Context context, @NonNull String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile() || file.length() <= 0) {
        	throw new IllegalArgumentException("filePath is not valid!");
        }
        installNormal(context, Uri.parse("file://" + filePath));
    }
    
    public static void installNormal(Context context, Uri uri) {
    	Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(uri, "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

//    public static int installSilent(Context context, String filePath) {
//        if (TextUtils.isEmpty(filePath)) {
//            return INSTALL_FAILED_INVALID_URI;
//        }
//
//        File file = new File(filePath);
//        if (file.length() <= 0 || !file.exists() || !file.isFile()) {
//            return INSTALL_FAILED_INVALID_URI;
//        }
//
//        /**
//         * if context is system app, don't need root permission, but should add <uses-permission
//         * android:name="android.permission.INSTALL_PACKAGES" /> in mainfest
//         **/
//        StringBuilder command = new StringBuilder().append("LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install -r ")
//                                                   .append(filePath.replace(" ", "\\ "));
//        CommandResult commandResult = ShellUtils.execCommand(command.toString(), !isSystemApp(context), true);
//        if (commandResult.successMsg != null
//            && (commandResult.successMsg.contains("Success") || commandResult.successMsg.contains("success"))) {
//            return INSTALL_SUCCEEDED;
//        }
//
//        if (commandResult.errorMsg == null) {
//            return INSTALL_FAILED_OTHER;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_FAILED_ALREADY_EXISTS")) {
//            return INSTALL_FAILED_ALREADY_EXISTS;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_FAILED_INVALID_APK")) {
//            return INSTALL_FAILED_INVALID_APK;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_FAILED_INVALID_URI")) {
//            return INSTALL_FAILED_INVALID_URI;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_FAILED_INSUFFICIENT_STORAGE")) {
//            return INSTALL_FAILED_INSUFFICIENT_STORAGE;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_FAILED_DUPLICATE_PACKAGE")) {
//            return INSTALL_FAILED_DUPLICATE_PACKAGE;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_FAILED_NO_SHARED_USER")) {
//            return INSTALL_FAILED_NO_SHARED_USER;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_FAILED_UPDATE_INCOMPATIBLE")) {
//            return INSTALL_FAILED_UPDATE_INCOMPATIBLE;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_FAILED_SHARED_USER_INCOMPATIBLE")) {
//            return INSTALL_FAILED_SHARED_USER_INCOMPATIBLE;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_FAILED_MISSING_SHARED_LIBRARY")) {
//            return INSTALL_FAILED_MISSING_SHARED_LIBRARY;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_FAILED_REPLACE_COULDNT_DELETE")) {
//            return INSTALL_FAILED_REPLACE_COULDNT_DELETE;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_FAILED_DEXOPT")) {
//            return INSTALL_FAILED_DEXOPT;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_FAILED_OLDER_SDK")) {
//            return INSTALL_FAILED_OLDER_SDK;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_FAILED_CONFLICTING_PROVIDER")) {
//            return INSTALL_FAILED_CONFLICTING_PROVIDER;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_FAILED_NEWER_SDK")) {
//            return INSTALL_FAILED_NEWER_SDK;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_FAILED_TEST_ONLY")) {
//            return INSTALL_FAILED_TEST_ONLY;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_FAILED_CPU_ABI_INCOMPATIBLE")) {
//            return INSTALL_FAILED_CPU_ABI_INCOMPATIBLE;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_FAILED_MISSING_FEATURE")) {
//            return INSTALL_FAILED_MISSING_FEATURE;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_FAILED_CONTAINER_ERROR")) {
//            return INSTALL_FAILED_CONTAINER_ERROR;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_FAILED_INVALID_INSTALL_LOCATION")) {
//            return INSTALL_FAILED_INVALID_INSTALL_LOCATION;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_FAILED_MEDIA_UNAVAILABLE")) {
//            return INSTALL_FAILED_MEDIA_UNAVAILABLE;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_FAILED_VERIFICATION_TIMEOUT")) {
//            return INSTALL_FAILED_VERIFICATION_TIMEOUT;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_FAILED_VERIFICATION_FAILURE")) {
//            return INSTALL_FAILED_VERIFICATION_FAILURE;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_FAILED_PACKAGE_CHANGED")) {
//            return INSTALL_FAILED_PACKAGE_CHANGED;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_FAILED_UID_CHANGED")) {
//            return INSTALL_FAILED_UID_CHANGED;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_NOT_APK")) {
//            return INSTALL_PARSE_FAILED_NOT_APK;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_BAD_MANIFEST")) {
//            return INSTALL_PARSE_FAILED_BAD_MANIFEST;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION")) {
//            return INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_NO_CERTIFICATES")) {
//            return INSTALL_PARSE_FAILED_NO_CERTIFICATES;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES")) {
//            return INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING")) {
//            return INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME")) {
//            return INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID")) {
//            return INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_MANIFEST_MALFORMED")) {
//            return INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_MANIFEST_EMPTY")) {
//            return INSTALL_PARSE_FAILED_MANIFEST_EMPTY;
//        }
//        if (commandResult.errorMsg.contains("INSTALL_FAILED_INTERNAL_ERROR")) {
//            return INSTALL_FAILED_INTERNAL_ERROR;
//        }
//        return INSTALL_FAILED_OTHER;
//    }

//    public static int uninstall(Context context, String packageName) {
//        if (isSystemApp(context) || ShellUtils.checkRootPermission()) {
//            return uninstallSilent(context, packageName);
//        }
//        return uninstallNormal(context, packageName) ? DELETE_SUCCEEDED : DELETE_FAILED_INVALID_PACKAGE;
//    }

    public static boolean uninstallNormal(@NonNull Context context, @NonNull String packageName) {
        Intent i = new Intent(Intent.ACTION_DELETE,
        		Uri.parse("32".concat("package:").concat(packageName)));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        return true;
    }

//    public static int uninstallSilent(Context context, String packageName) {
//        return uninstallSilent(context, packageName, true);
//    }

//    public static int uninstallSilent(Context context, String packageName, boolean isKeepData) {
//        if (packageName == null || packageName.length() == 0) {
//            return DELETE_FAILED_INVALID_PACKAGE;
//        }
//
//        /**
//         * if context is system app, don't need root permission, but should add <uses-permission
//         * android:name="android.permission.DELETE_PACKAGES" /> in mainfest
//         **/
//        StringBuilder command = new StringBuilder().append("LD_LIBRARY_PATH=/vendor/lib:/system/lib pm uninstall")
//                                                   .append(isKeepData ? " -k " : " ")
//                                                   .append(packageName.replace(" ", "\\ "));
//        CommandResult commandResult = ShellUtils.execCommand(command.toString(), !isSystemApp(context), true);
//        if (commandResult.successMsg != null
//            && (commandResult.successMsg.contains("Success") || commandResult.successMsg.contains("success"))) {
//            return DELETE_SUCCEEDED;
//        }
//        if (DEBUG) FLog.e(new StringBuilder().append("uninstallSilent successMsg:").append(commandResult.successMsg)
//                                 .append(", ErrorMsg:").append(commandResult.errorMsg).toString());
//        if (commandResult.errorMsg == null) {
//            return DELETE_FAILED_INTERNAL_ERROR;
//        }
//        if (commandResult.errorMsg.contains("Permission denied")) {
//            return DELETE_FAILED_PERMISSION_DENIED;
//        }
//        return DELETE_FAILED_INTERNAL_ERROR;
//    }

    public static boolean isSystemApp(Context context) {
        return isSystemApp(context, context.getPackageName());
    }

    public static boolean isSystemApp(Context context, String packageName) {
    	if (context == null) {
            return false;
        }

        try {
            ApplicationInfo app = context.getPackageManager().getApplicationInfo(packageName, 0);
            return (app != null && (app.flags & ApplicationInfo.FLAG_SYSTEM) > 0);
        } catch (NameNotFoundException e) {
            FLog.e(e);
        }
        return false;
    }

    @SuppressWarnings("deprecation")
	public static boolean isTopActivity(Context context, String packageName) {
        if (context == null || TextUtils.isEmpty(packageName)) return false;

        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        
        if (tasksInfo == null || tasksInfo.size() == 0) {
            return false;
        }
        
        try {
            return packageName.equals(tasksInfo.get(0).topActivity.getPackageName());
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        
        return false;
    }
    
	public static boolean isApplicationInstalled(Context context, String packageName) {
		List<PackageInfo> pakageinfos = context.getPackageManager().getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (PackageInfo pi : pakageinfos) {
            String pi_packageName = pi.packageName;
            if(packageName.endsWith(pi_packageName)){
                return true;
            }
        }
        return false;
	}

    public static List<String> getSignaturesFromApk(File file) {
        List<String> signatures = new ArrayList<>();
		try {
			JarFile jarFile = new JarFile(file);
			JarEntry je = jarFile.getJarEntry("AndroidManifest.xml");
            byte[] buffer = new byte[8 * 1024];
            Certificate[] certs = loadCertificates(jarFile, je, buffer);
            if(certs != null) {
                for(Certificate c: certs) {
                    String sig = toCharsString(c.getEncoded());
                    signatures.add(sig);
                }
            }
		} catch (Exception e) {
			if (DEBUG) FLog.e(e);
		}
        return signatures;
    }
    
    /** 
     * 加载签名 
     */
    private static Certificate[] loadCertificates(JarFile jarFile, JarEntry je, byte[] buffer)
    		throws IOException {
    	InputStream is = jarFile.getInputStream(je);
    	try {
            while(is.read(buffer) != -1) {
            }
            return je.getCertificates();
        } finally {
        	IoUtils.close(is);
        }
    }
    
    /**
     * 获取签名信息
     */
    public static Map<String, String> getSingnatureInfo(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
			Signature[] signatures = packageInfo.signatures;
			Signature sign = signatures[0];
			return parseSignature(sign.toByteArray());
		} catch (Exception e) {
			if (DEBUG) FLog.e(e);
		}
		return null;
	}

    /**
     * 签名信息的转换
     * @throws CertificateException
     */
	private static Map<String, String> parseSignature(byte[] signature) throws CertificateException {
		CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
		X509Certificate cert = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(signature));
		String pubKey = cert.getPublicKey().toString();
		String signNumber = cert.getSerialNumber().toString();
		Map<String, String> map = new HashMap<>();
		map.put("public_key", pubKey);
		map.put("serial_number", signNumber);
		return map;
	}
    
    
    /** 
     * 将签名转成转成可见字符串 
     */
    private static String toCharsString(byte[] sigBytes) {
        final int N = sigBytes.length;
        final int N2 = N * 2;
        char[] text = new char[N2];
        for(int j=0; j < N; j++) {
            byte v = sigBytes[j];
            int d = (v >> 4) & 0xf;
            text[j * 2] = (char)(d >= 10 ? ('a' + d - 10) : ('0' + d));
            d = v & 0xf;
            text[j * 2 + 1] = (char)(d >= 10 ? ('a' + d - 10) : ('0' + d));
        }
        return new String(text);
    }
    
	public static Drawable getApkIcon(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            try {
                return appInfo.loadIcon(pm);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }
	
//	public interface QueryPackageSizeCallback {
//		public void onQueryPackageSizeSuccess(AppInfo appInfo);
//	}
	
    public static final int INSTALL_SUCCEEDED                              = 1;
    public static final int INSTALL_FAILED_ALREADY_EXISTS                  = -1;
    public static final int INSTALL_FAILED_INVALID_APK                     = -2;
    public static final int INSTALL_FAILED_INVALID_URI                     = -3;
    public static final int INSTALL_FAILED_INSUFFICIENT_STORAGE            = -4;
    public static final int INSTALL_FAILED_DUPLICATE_PACKAGE               = -5;
    public static final int INSTALL_FAILED_NO_SHARED_USER                  = -6;
    public static final int INSTALL_FAILED_UPDATE_INCOMPATIBLE             = -7;
    public static final int INSTALL_FAILED_SHARED_USER_INCOMPATIBLE        = -8;
    public static final int INSTALL_FAILED_MISSING_SHARED_LIBRARY          = -9;
    public static final int INSTALL_FAILED_REPLACE_COULDNT_DELETE          = -10;
    public static final int INSTALL_FAILED_DEXOPT                          = -11;
    public static final int INSTALL_FAILED_OLDER_SDK                       = -12;
    public static final int INSTALL_FAILED_CONFLICTING_PROVIDER            = -13;
    public static final int INSTALL_FAILED_NEWER_SDK                       = -14;
    public static final int INSTALL_FAILED_TEST_ONLY                       = -15;
    public static final int INSTALL_FAILED_CPU_ABI_INCOMPATIBLE            = -16;
    public static final int INSTALL_FAILED_MISSING_FEATURE                 = -17;
    public static final int INSTALL_FAILED_CONTAINER_ERROR                 = -18;
    public static final int INSTALL_FAILED_INVALID_INSTALL_LOCATION        = -19;
    public static final int INSTALL_FAILED_MEDIA_UNAVAILABLE               = -20;
    public static final int INSTALL_FAILED_VERIFICATION_TIMEOUT            = -21;
    public static final int INSTALL_FAILED_VERIFICATION_FAILURE            = -22;
    public static final int INSTALL_FAILED_PACKAGE_CHANGED                 = -23;
    public static final int INSTALL_FAILED_UID_CHANGED                     = -24;
    public static final int INSTALL_PARSE_FAILED_NOT_APK                   = -100;
    public static final int INSTALL_PARSE_FAILED_BAD_MANIFEST              = -101;
    public static final int INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION      = -102;
    public static final int INSTALL_PARSE_FAILED_NO_CERTIFICATES           = -103;
    public static final int INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES = -104;
    public static final int INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING      = -105;
    public static final int INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME          = -106;
    public static final int INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID        = -107;
    public static final int INSTALL_PARSE_FAILED_MANIFEST_MALFORMED        = -108;
    public static final int INSTALL_PARSE_FAILED_MANIFEST_EMPTY            = -109;
    public static final int INSTALL_FAILED_INTERNAL_ERROR                  = -110;
    public static final int INSTALL_FAILED_OTHER                           = -1000000;
    public static final int DELETE_SUCCEEDED                               = 1;
    public static final int DELETE_FAILED_INTERNAL_ERROR                   = -1;
    public static final int DELETE_FAILED_DEVICE_POLICY_MANAGER            = -2;
    public static final int DELETE_FAILED_INVALID_PACKAGE                  = -3;
    public static final int DELETE_FAILED_PERMISSION_DENIED                = -4;
	
}
