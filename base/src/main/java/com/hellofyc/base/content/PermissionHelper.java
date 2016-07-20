package com.hellofyc.base.content;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.support.v4.util.ArrayMap;

import com.hellofyc.base.R;
import com.hellofyc.base.text.SpanBuilder;

import java.util.List;

/**
 * Created on 2015/11/20.
 *
 * @author Yucun Fang
 */
@TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
public class PermissionHelper {

    private static final ArrayMap<String, String> sPermissionGroupMap;

    public static final int REQUEST_CODE_LOCATION        = 0x00000001;
    public static final int REQUEST_CODE_PHONE           = 0x00000002;
    public static final int REQUEST_CODE_SENSORS         = 0x00000004;
    public static final int REQUEST_CODE_CAMERA          = 0x00000008;
    public static final int REQUEST_CODE_CONTACTS        = 0x00000010;
    public static final int REQUEST_CODE_CALENDAR        = 0x00000020;
    public static final int REQUEST_CODE_STORAGE         = 0x00000040;
    public static final int REQUEST_CODE_SMS             = 0x00000080;
    public static final int REQUEST_CODE_AUDIO           = 0x00000100;

    public static final int PERMISSION_GRANTED       = PackageManager.PERMISSION_GRANTED;
    public static final int PERMISSION_DENIED        = PackageManager.PERMISSION_DENIED;

//    public static final String PERMISSION_LOCATION   = Manifest.permission.ACCESS_FINE_LOCATION;
//    public static final String PERMISSION_PHONE      = Manifest.permission.CALL_PHONE;
//    public static final String PERMISSION_SENSORS    = Manifest.permission.BODY_SENSORS;
//    public static final String PERMISSION_CAMERA     = Manifest.permission.CAMERA;
//    public static final String PERMISSION_CONTACTS   = Manifest.permission.READ_CONTACTS;
//    public static final String PERMISSION_CALENDAR   = Manifest.permission.READ_CALENDAR;
//    public static final String PERMISSION_STORAGE    = Manifest.permission.READ_EXTERNAL_STORAGE;
//    public static final String PERMISSION_SMS        = Manifest.permission.READ_SMS;
//    public static final String PERMISSION_AUDIO      = Manifest.permission.RECORD_AUDIO;

    static {
        sPermissionGroupMap = new ArrayMap<>();

        //位置信息
        sPermissionGroupMap.put(Manifest.permission.ACCESS_COARSE_LOCATION, "位置信息");
        sPermissionGroupMap.put(Manifest.permission.ACCESS_FINE_LOCATION, "位置信息");

        //电话
        sPermissionGroupMap.put(Manifest.permission.ADD_VOICEMAIL, "电话");
        sPermissionGroupMap.put(Manifest.permission.CALL_PHONE, "电话");
        sPermissionGroupMap.put(Manifest.permission.PROCESS_OUTGOING_CALLS, "电话");
        sPermissionGroupMap.put(Manifest.permission.READ_CALL_LOG, "电话");
        sPermissionGroupMap.put(Manifest.permission.READ_PHONE_STATE, "电话");
        sPermissionGroupMap.put(Manifest.permission.USE_SIP, "电话");
        sPermissionGroupMap.put(Manifest.permission.WRITE_CALL_LOG, "电话");

        //身体传感器
        if (Build.VERSION.SDK_INT >= 20) {
            sPermissionGroupMap.put(Manifest.permission.BODY_SENSORS, "身体传感器");
        }

        //相机
        sPermissionGroupMap.put(Manifest.permission.CAMERA, "相机");

        //通讯录
        sPermissionGroupMap.put(Manifest.permission.GET_ACCOUNTS, "通讯录");
        sPermissionGroupMap.put(Manifest.permission.WRITE_CONTACTS, "通讯录");
        sPermissionGroupMap.put(Manifest.permission.READ_CONTACTS, "通讯录");

        //日历
        sPermissionGroupMap.put(Manifest.permission.READ_CALENDAR, "日历");
        sPermissionGroupMap.put(Manifest.permission.WRITE_CALENDAR, "日历");

        //存储空间
        sPermissionGroupMap.put(Manifest.permission.READ_EXTERNAL_STORAGE, "存储空间");
        sPermissionGroupMap.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储空间");

        //短信
        sPermissionGroupMap.put(Manifest.permission.READ_SMS, "短信");
        sPermissionGroupMap.put(Manifest.permission.RECEIVE_MMS, "短信");
        sPermissionGroupMap.put(Manifest.permission.RECEIVE_SMS, "短信");
        sPermissionGroupMap.put(Manifest.permission.SEND_SMS, "短信");
        sPermissionGroupMap.put(Manifest.permission.RECEIVE_WAP_PUSH, "短信");

        //麦克风
        sPermissionGroupMap.put(Manifest.permission.RECORD_AUDIO, "麦克风");
    }

    public static String getPermissionGroupName(String permission) {
        return sPermissionGroupMap.get(permission);
    }

    public static boolean checkSelfPermission(@NonNull Activity activity, @NonNull String permission) {
        return PermissionChecker.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermissions(@NonNull Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    public static void requestPermissions(@NonNull Fragment fragment, @NonNull String[] permissions, int requestCode) {
        fragment.requestPermissions(permissions, requestCode);
    }

    public static void onRequestPermissionsResult(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int i=0; i<permissions.length; i++) {
            boolean grantResult = grantResults[i] == PackageManager.PERMISSION_GRANTED;
            if (!grantResult && !ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i])) {
                showRequestPermissionDeniedDialog(activity, permissions[i]);
                return;
            }
        }
    }

    public static void onRequestPermissionsResult(@NonNull Fragment fragment, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int i=0; i<permissions.length; i++) {
            boolean grantResult = grantResults[i] == PackageManager.PERMISSION_GRANTED;
            if (!grantResult && !fragment.shouldShowRequestPermissionRationale(permissions[i])) {
                showRequestPermissionDeniedDialog(fragment.getActivity(), permissions[i]);
                return;
            }
        }
    }

    private static void showRequestPermissionDeniedDialog(@NonNull final Activity activity, String permission) {
        String permissionGroupName = PermissionHelper.getPermissionGroupName(permission);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        CharSequence title = SpanBuilder.create("没有访问" + permissionGroupName + "权限").setBold(permissionGroupName).build();
        builder.setTitle(title);
        builder.setMessage("如果要开启此功能, 可依次进入[设置-应用-" +
                activity.getPackageManager().getApplicationLabel(activity.getApplicationInfo()) + "-权限], 打开[" + permissionGroupName + "]");
        builder.setNegativeButton("知道了", null);
        builder.setPositiveButton("跳转", new DialogInterface.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = IntentHelper.getOpenAppDetailActivityIntent(activity, activity.getPackageName());
                if (intent != null) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(activity, R.anim.base_right_enter, R.anim.base_slow_fade_exit);
                    activity.startActivity(intent, options.toBundle());
                }
            }
        });
        builder.create().show();
    }

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

}
