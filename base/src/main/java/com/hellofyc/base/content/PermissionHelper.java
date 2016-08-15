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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 2015/11/20.
 *
 * @author Yucun Fang
 */
@TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
public class PermissionHelper {

    private static final ArrayMap<String, String> sPermissionGroupMap;
    private static final ArrayMap<String, Integer> sRequestCodeMap;

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


        sRequestCodeMap = new ArrayMap<>();

        //位置信息
        sRequestCodeMap.put(Manifest.permission.ACCESS_COARSE_LOCATION, REQUEST_CODE_LOCATION);
        sRequestCodeMap.put(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_CODE_LOCATION);

        //电话
        sRequestCodeMap.put(Manifest.permission.ADD_VOICEMAIL, REQUEST_CODE_PHONE);
        sRequestCodeMap.put(Manifest.permission.CALL_PHONE, REQUEST_CODE_PHONE);
        sRequestCodeMap.put(Manifest.permission.PROCESS_OUTGOING_CALLS, REQUEST_CODE_PHONE);
        sRequestCodeMap.put(Manifest.permission.READ_CALL_LOG, REQUEST_CODE_PHONE);
        sRequestCodeMap.put(Manifest.permission.READ_PHONE_STATE, REQUEST_CODE_PHONE);
        sRequestCodeMap.put(Manifest.permission.USE_SIP, REQUEST_CODE_PHONE);
        sRequestCodeMap.put(Manifest.permission.WRITE_CALL_LOG, REQUEST_CODE_PHONE);

        //身体传感器
        if (Build.VERSION.SDK_INT >= 20) {
            sRequestCodeMap.put(Manifest.permission.BODY_SENSORS, REQUEST_CODE_SENSORS);
        }

        //相机
        sRequestCodeMap.put(Manifest.permission.CAMERA, REQUEST_CODE_CAMERA);

        //通讯录
        sRequestCodeMap.put(Manifest.permission.GET_ACCOUNTS, REQUEST_CODE_CONTACTS);
        sRequestCodeMap.put(Manifest.permission.WRITE_CONTACTS, REQUEST_CODE_CONTACTS);
        sRequestCodeMap.put(Manifest.permission.READ_CONTACTS, REQUEST_CODE_CONTACTS);

        //日历
        sRequestCodeMap.put(Manifest.permission.READ_CALENDAR, REQUEST_CODE_CALENDAR);
        sRequestCodeMap.put(Manifest.permission.WRITE_CALENDAR, REQUEST_CODE_CALENDAR);

        //存储空间
        sRequestCodeMap.put(Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_CODE_STORAGE);
        sRequestCodeMap.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_CODE_STORAGE);

        //短信
        sRequestCodeMap.put(Manifest.permission.READ_SMS, REQUEST_CODE_SMS);
        sRequestCodeMap.put(Manifest.permission.RECEIVE_MMS, REQUEST_CODE_SMS);
        sRequestCodeMap.put(Manifest.permission.RECEIVE_SMS, REQUEST_CODE_SMS);
        sRequestCodeMap.put(Manifest.permission.SEND_SMS, REQUEST_CODE_SMS);
        sRequestCodeMap.put(Manifest.permission.RECEIVE_WAP_PUSH, REQUEST_CODE_SMS);

        //麦克风
        sRequestCodeMap.put(Manifest.permission.RECORD_AUDIO, REQUEST_CODE_AUDIO);
    }

    public static String getPermissionGroupName(String permission) {
        return sPermissionGroupMap.get(permission);
    }

    public static int getPermissionRequestCode(@NonNull String permission) {
        return sRequestCodeMap.get(permission);
    }

    public static String[] getPermissionsInManifest(@NonNull Context context) {
        List<String> permissions = new ArrayList<>();
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            permissions.addAll(Arrays.asList(info.requestedPermissions));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return permissions.toArray(new String[permissions.size()]);
    }

    public static String[] getPermissionsNeedRuntimeRequest(@NonNull Context context) {
        List<String> permissions = new ArrayList<>();
        String[] applyPermissions = getPermissionsInManifest(context);
        for (String permission : applyPermissions) {
            if (sPermissionGroupMap.containsKey(permission)) {
                permissions.add(permission);
            }
        }
        return permissions.toArray(new String[permissions.size()]);
    }

    public static boolean checkSelfPermission(@NonNull Activity activity, @NonNull String permission) {
        return PermissionChecker.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermissions(@NonNull Activity activity, int requestCode, String[] permissions) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    public static void requestPermissions(@NonNull Fragment fragment, int requestCode, @NonNull String[] permissions) {
        fragment.requestPermissions(permissions, requestCode);
    }

    public static void onRequestPermissionsResult(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, boolean isShowDialogTips) {
        for (int i=0; i<permissions.length; i++) {
            boolean grantResult = grantResults[i] == PackageManager.PERMISSION_GRANTED;
            if (!grantResult
                    && !ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i])
                    && isShowDialogTips) {
                showRequestPermissionDeniedDialog(activity, permissions[i]);
                return;
            }
        }
    }

    public static void onRequestPermissionsResult(@NonNull Fragment fragment, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, boolean isShowDialogTips) {
        for (int i=0; i<permissions.length; i++) {
            boolean grantResult = grantResults[i] == PackageManager.PERMISSION_GRANTED;
            if (!grantResult
                    && !fragment.shouldShowRequestPermissionRationale(permissions[i])
                    && isShowDialogTips) {
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
