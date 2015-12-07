/*
 * Copyright (C) 2014 Jason Fang ( ijasonfang@gmail.com )
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hellofyc.applib.helper;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice.StateCallback;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;

/**
 * Create on 2015年2月10日 上午11:59:10
 * @author Jason Fang
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class CameraAdmin {
    static final boolean DEBUG = true;

    private static CameraAdmin sInstance;
    private CameraManager mCM;

    private CameraAdmin(Context context) {
        mCM = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
    }

    public static CameraAdmin getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new CameraAdmin(context);
        }
        return sInstance;
    }

    public String[] getCameraIdList() throws CameraAccessException {
        return mCM.getCameraIdList();
    }

    public void openCamera(Context context, String cameraId, StateCallback callback, Handler handler) throws CameraAccessException {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mCM.openCamera(cameraId, callback, handler);
    }
}
