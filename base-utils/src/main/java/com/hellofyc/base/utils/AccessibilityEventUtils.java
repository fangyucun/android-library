/*
 * Copyright (C) 2012-2015 Jason Fang ( ifangyucun@gmail.com )
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hellofyc.base.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created on 2015/8/24.
 *
 * @author Yucun Fang
 */
public class AccessibilityEventUtils {
    private static final boolean DEBUG = false;

    public static void clickNotification(@NonNull AccessibilityEvent event) {
        Parcelable parcelable = event.getParcelableData();
        if (parcelable instanceof Notification) {
            Notification notification = (Notification) parcelable;
            PendingIntent pendingIntent = notification.contentIntent;
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                if (DEBUG) FLog.e(e);
            }
        }
    }
}
