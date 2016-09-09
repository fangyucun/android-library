package com.hellofyc.base.compat;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.hellofyc.base.util.IntentUtils;

/**
 * Created on 2015/11/23.
 *
 * @author Yucun Fang
 */
public class ColorOsCompat {

    private static final String PACKAGE_COLOROS_PURE_BACKGROUND = "com.oppo.purebackground";
    private static final String ACTIVITY_COLOROS_PURE_BACKGROUND = "com.oppo.purebackground.PurebackgroundTopActivity";

    public static final String PACKAGE_COLOROS_PERMISSION_MANAGER = "com.oppo.safe";
    public static final String ACTIVITY_COLOROS_PERMISSION_MANAGER = "com.oppo.safe.permission.PermissionTopActivity";
    public static final String ACTIVITY_COLOROS_FLOAT_WINDOW = PACKAGE_COLOROS_PERMISSION_MANAGER + ".permission.floatwindow.FloatWindowListActivity";

    public static Intent getOpenPureBackgroundActivityIntent(@NonNull Context context) {
        Intent intent = new Intent();
        intent.setClassName(PACKAGE_COLOROS_PURE_BACKGROUND, ACTIVITY_COLOROS_PURE_BACKGROUND);
        return IntentUtils.isIntentAvailable(context, intent) ? intent : null;
    }

    public static Intent getOpenPermissionManagerActivityIntent(@NonNull Context context) {
        Intent intent = new Intent();
        intent.setClassName(PACKAGE_COLOROS_PERMISSION_MANAGER, ACTIVITY_COLOROS_PERMISSION_MANAGER);
        return IntentUtils.isIntentAvailable(context, intent) ? intent : null;
    }
}
