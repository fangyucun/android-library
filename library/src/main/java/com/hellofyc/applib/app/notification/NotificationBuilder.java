/*
 *
 *  * Copyright (C) 2015 Jason Fang ( ifangyucun@gmail.com )
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */package com.hellofyc.applib.app.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

/**
 * @author Yucun Fang
 * Create on 2015年5月8日 下午2:42:25
 */
public class NotificationBuilder {

	public static final int DEFAULT_NOTIFY_ID = 1000;
	private static NotificationBuilder sInstance;
	private NotificationManager mNM;
	
	public static NotificationBuilder getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new NotificationBuilder(context);
		}
		return sInstance;
	}
	
	public NotificationBuilder(Context context) {
		mNM = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	public void show(int id, Notification notification) {
		mNM.notify(id, notification);
	}
	
	public void showDefault(Context context, String tickerText, String title, String text) {
		showDefault(context, tickerText, title, text, null);
	}
	
	public void showDefault(Context context, String tickerText, String title, String text, PendingIntent pendingIntent) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext());
		builder.setTicker(tickerText);
		builder.setContentTitle(title);
		builder.setContentText(text);
		builder.setContentIntent(pendingIntent);
		builder.setSmallIcon(context.getApplicationInfo().icon);
		mNM.notify(DEFAULT_NOTIFY_ID, builder.build());
	}
}
