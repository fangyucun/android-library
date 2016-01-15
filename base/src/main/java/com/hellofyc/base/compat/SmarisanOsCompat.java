package com.hellofyc.base.compat;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.hellofyc.base.content.IntentHelper;

/**
 * Created on 2015/11/23.
 *
 * @author Yucun Fang
 */
class SmarisanOsCompat {

    private static final String PACKAGE_SMARTISANOS_PERMISSION_MANAGER = "com.smartisanos.security";
    private static final String ACTIVITY_SMARTISANOS_PERMISSION_MANAGER = "com.smartisanos.security.MainActivity";
    private static final String ACTIVITY_SMARTISANOS_FLOAT_WINDOW = "com.smartisanos.security.SwitchedPermissions";

    public static Intent getOpenPermissionManagerActivityIntent(@NonNull Context context) {
        Intent intent = new Intent();
        intent.setClassName(PACKAGE_SMARTISANOS_PERMISSION_MANAGER, ACTIVITY_SMARTISANOS_PERMISSION_MANAGER);
        return IntentHelper.isIntentAvailable(context, intent) ? intent : null;
    }
}
