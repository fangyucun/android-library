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
package com.hellofyc.base.content;

import android.app.Activity;
import android.app.SearchManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.webkit.URLUtil;

import com.hellofyc.util.CollectionUtils;
import com.hellofyc.util.PackageUtils;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


/**
 * Actvity dump tool
 *
 * Create on 2014/05
 * @author Jason Fang
 */
public final class IntentHelper {
    static final boolean DEBUG = true;

    public static final String SCHEME_FILE = "file://";
    public static final String SCHEME_MARKET = "market://";

    private static final String PACKAGE_SETTINGS = "com.android.settings";

    private static final String SCHEME_PACKAGE = "package";

    public static final String PACKAGE_BROWSER_DEFAULT = "com.android.browser";
    public static final String ACTIVITY_BROWSER_DEFAULT = "com.android.browser.BrowserActivity";

    public static final String PACKAGE_BROWSER_LIEBAO = "com.ijinshan.browser";
    public static final String ACTIVITY_BROWSER_LIEBAO = "com.ijinshan.browser.screen.BrowserActivity";

    public static final String PACKAGE_BROWSER_UC = "com.UCMobile";
    public static final String ACTIVITY_BROWSER_UC = "com.UCMobile.main.UCMobile";

    public static final String PACKAGE_BROWSER_QQ = "com.tencent.mtt";
    public static final String ACTIVITY_BROWSER_QQ = "com.tencent.mtt.MainActivity";

    public static final String PACKAGE_BROWSER_360 = "com.qihoo.browser";
    public static final String ACTIVITY_BROWSER_360 = "com.qihoo.browser.BrowserActivity";

    public static final String PACKAGE_BROWSER_CHROME = "com.android.chrome";
    public static final String ACTIVITY_BROWSER_CHROME = "com.google.android.apps.chrome.Main";

    public static final String PACKAGE_DOWNLOAD = "com.android.providers.downloads";
    public static final String ACTIVITY_DOWNLOAD = "com.android.providers.downloads.ui.DownloadList";

    public static void configureCropPhoto(@NonNull Intent intent, int outputX, int outputY) {
        intent.putExtra("crop", "true");
        //裁剪框比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("outputFormat", "JPEG");
        //人脸识别
        intent.putExtra("noFaceDetection", false);
        intent.putExtra("return-data", true);
    }

    public static void configureCropPhoto(@NonNull Intent intent, Bitmap data, int outputX, int outputY) {
        intent.setAction("com.android.camera.action.CROP");
        intent.setType("image/*");
        intent.putExtra("data", data);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("return-data", true);
    }

    public static Intent parseIntent(@NonNull String url) {
        if (url.startsWith("#Intent;")) {
            try {
                return Intent.parseUri(url, 0);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 打开网页搜索
     */
    public static Intent getOpenWebSearchActivityIntent(@NonNull Context context, @NonNull String text) {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, text);
        return isIntentAvailable(context, intent) ? intent : null;
    }

    public static Intent getOpenAppDetailActivityIntent(@NonNull Context context, @NonNull String packageName) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts(SCHEME_PACKAGE, packageName, null);
        intent.setData(uri);
        return isIntentAvailable(context, intent) ? intent : null;
    }

    public static Intent getOpenDownloadActivityIntent (Context context) {
        Intent intent = new Intent();
        intent.setClassName(PACKAGE_DOWNLOAD, ACTIVITY_DOWNLOAD);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return isIntentAvailable(context, intent) ? intent : null;
    }

    public static Intent getOpenActiveDeviceAdminActivityIntent (@NonNull Context context, Class<?> clazz) {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        ComponentName cm = new ComponentName(context, clazz);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cm);
        return isIntentAvailable(context, intent) ? intent : null;
    }

    public static Intent getOpenSettingsActivityIntent(@NonNull Context context) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        return isIntentAvailable(context, intent) ? intent : null;
    }

    public static Intent getOpenShareTextActivityIntent(@NonNull Context context, String title, String text) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        Intent shareIntent = Intent.createChooser(intent, title);
        return isIntentAvailable(context, shareIntent) ? shareIntent : null;
    }

    public static Intent getOpenDialActivityIntent(@NonNull String tel) {
        return new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
    }

    public static Intent getOpenEmailActivityIntent (@NonNull Context context, @NonNull String receiverEmail) {
        Uri uri = Uri.parse("mailto:" + receiverEmail);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        Intent emailIntent = Intent.createChooser(intent, "请选择邮件类应用");
        return isIntentAvailable(context, emailIntent) ? emailIntent : null;
    }

    public static Intent getOpenSMSActivityIntent (@NonNull Context context, @NonNull String receiver) {
        Uri uri = Uri.parse("smsto:" + receiver);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        return isIntentAvailable(context, intent) ? intent : null;
    }

    public static Intent getOpenMarketByPackageNameIntent(@NonNull Context context, @NonNull String packageName) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=".concat(packageName)));
        return isIntentAvailable(context, intent) ? intent : null;
    }

    public static Intent getOpenMarketByPublisherNameIntent(@NonNull Context context, @NonNull String publisherName) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://search?q=pub:".concat(publisherName)));
        return isIntentAvailable(context, intent) ? intent : null;
    }

    public static Intent getOpenMarketBySearchQueryIntent(@NonNull Context context, @NonNull String searchQuery) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://search?q=".concat(searchQuery).concat("&c=apps")));
        return isIntentAvailable(context, intent) ? intent : null;
    }

    public static Intent getOpenBrowserActivityIntent(@NonNull Context context, @NonNull String urlString) {
        if (!URLUtil.isHttpUrl(urlString) && !URLUtil.isHttpsUrl(urlString)) return null;

        Intent intent = new Intent();
        if (PackageUtils.isApplicationInstalled(context, PACKAGE_BROWSER_CHROME)) {
            intent.setClassName(PACKAGE_BROWSER_CHROME, ACTIVITY_BROWSER_CHROME);
        } else if (PackageUtils.isApplicationInstalled(context, PACKAGE_BROWSER_LIEBAO)) {
            intent.setClassName(PACKAGE_BROWSER_LIEBAO, ACTIVITY_BROWSER_LIEBAO);
        } else if (PackageUtils.isApplicationInstalled(context, PACKAGE_BROWSER_UC)) {
            intent.setClassName(PACKAGE_BROWSER_UC, ACTIVITY_BROWSER_UC);
        } else if (PackageUtils.isApplicationInstalled(context, PACKAGE_BROWSER_QQ)) {
            intent.setClassName(PACKAGE_BROWSER_QQ, ACTIVITY_BROWSER_QQ);
        } else if (PackageUtils.isApplicationInstalled(context, PACKAGE_BROWSER_360)) {
            intent.setClassName(PACKAGE_BROWSER_360, ACTIVITY_BROWSER_360);
        }
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(urlString));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return isIntentAvailable(context, intent) ? intent : null;
    }

    /**
     * 检查包含intent的Activity
     */
    public static List<ActivityInfo> queryIntentActivities(@NonNull Context context, @NonNull Intent intent) {
        List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (CollectionUtils.isEmpty(resolveInfoList))
            return null;

        List<ActivityInfo> activityInfoList = new ArrayList<>();
        for (ResolveInfo info : resolveInfoList) {
            activityInfoList.add(info.activityInfo);
        }
        return activityInfoList;
    }

    /**
     * 启动默认的Activity
     */
    public static boolean startMainActivity(@NonNull final Context context, @NonNull String packageName) {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setPackage(packageName);

        List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);

        ResolveInfo ri = apps.iterator().next();
        if (ri != null) {
            String className = ri.activityInfo.name;
            intent.setComponent(new ComponentName(packageName, className));
            if (isIntentAvailable(context, intent)) {
                context.startActivity(intent);
                return true;
            }
        }
        return false;
    }

	/**
	 * 打开文件
	 */
	public static boolean openFileByMimeType(Context context, String path, String mimeType) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse(SCHEME_FILE + path), mimeType);
		if (isIntentAvailable(context, intent)) {
			if (context instanceof Activity) {
				context.startActivity(intent);
			} else {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
			return true;
		}
		return false;
	}

	/**
	 * 判断是否有可以接受的Activity
	 */
	public static boolean isIntentAvailable(@NonNull Context context, @NonNull Intent intent) {
		return !context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).isEmpty();
	}
	
}
