package com.hellofyc.base.compat;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.hellofyc.base.content.IntentHelper;
import com.hellofyc.base.util.AndroidUtils;
import com.hellofyc.base.util.FLog;
import com.hellofyc.base.util.ParseUtils;
import com.hellofyc.base.util.Reflect;

import java.lang.reflect.Field;

/**
 * Created on 2015/11/23.
 *
 * @author Yucun Fang
 */
public class MiuiCompat {

    public static final int OP_SYSTEM_ALERT_WINDOW 			= 24;

    public static final int VERSION;

    static {
        String versionName = AndroidUtils.getSystemProperty("ro.miui.ui.version.name");
        if (versionName != null) {
            VERSION = ParseUtils.stringToInt(versionName.substring(1));
        } else {
            VERSION = 0;
        }
    }

    public static Intent getOpenPermissionManagerActivityIntent (@NonNull Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.MainAcitivty");
        return IntentHelper.isIntentAvailable(context, intent) ? intent : null;
    }

    /**
     * 打开自身权限管理界面
     */
    public static Intent getOpenPermissionActivityIntent(@NonNull Context context) {
        return getOpenPermissionActivityIntent(context, context.getPackageName());
    }

    public static Intent getOpenPermissionActivityIntent (@NonNull Context context, @NonNull String packageName) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");

        if (VERSION >= 6) {
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            intent.putExtra("extra_pkgname", packageName);
        } else if (VERSION == 5) {
            try {
                PackageInfo pInfo = context.getPackageManager().getPackageInfo(packageName, 0);
                intent.setClassName("com.android.settings", "com.miui.securitycenter.permission.AppPermissionsEditor");
                intent.putExtra("extra_package_uid", pInfo.applicationInfo.uid);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return IntentHelper.isIntentAvailable(context, intent) ? intent : null;
    }

    public static void setLauncherIconCornerMark(@NonNull Context context, int notifyId, int count) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Notification notification = builder.build();
        try {
            Class<?> miuiNotificationClass = Class.forName("android.app.MiuiNotification");
            Object miuiNotification = miuiNotificationClass.newInstance();
            Field field = miuiNotification.getClass().getDeclaredField("messageCount");
            field.setAccessible(true);
            field.set(miuiNotification, count);
            field = notification.getClass().getField("extraNotification");
            field.set(notification, miuiNotification);
        } catch (Exception e) {
            FLog.e(e);
        }
        manager.notify(notifyId, notification);
    }

    @SuppressWarnings("IncompatibleBitwiseMaskOperation")
    public static boolean isFloatWindowPermissionAllowed(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return checkOp(context, OP_SYSTEM_ALERT_WINDOW);
        } else {
            return (context.getApplicationInfo().flags & 1<<27) == 1;
        }
    }

    /**
     * {@link #checkOp(Context, int, String, int)}
     */
    public static boolean checkOp(Context context, int op) {
        return checkOp(context, op, context.getPackageName(), Binder.getCallingUid());
    }

    /**
     * 检查操作权限
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean checkOp(Context context, int op, String packageName, int uid) {
        final int version = Build.VERSION.SDK_INT;

        if (version >= Build.VERSION_CODES.KITKAT) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                return (AppOpsManager.MODE_ALLOWED == (Integer)(Reflect.on(manager).call("checkOp", op, uid, packageName).get()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            FLog.e("Below API 19 cannot invoke!");
        }
        return false;
    }

}
