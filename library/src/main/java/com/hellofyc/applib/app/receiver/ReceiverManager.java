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

package com.hellofyc.applib.app.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.wifi.WifiManager;

public class ReceiverManager {

	private static ReceiverManager sInstance;
	
	public static ReceiverManager getInstance() {
		if (sInstance == null) {
			sInstance = new ReceiverManager();
		}
		return sInstance;
	}
	

	public BroadcastReceiver registerWifiConnectReceiver(Context context, BroadcastReceiver receiver) {
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		context.registerReceiver(receiver, filter);
		return receiver;
	}
	
	public BroadcastReceiver registerWifiStateReceiver(Context context, BroadcastReceiver receiver) {
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		context.registerReceiver(receiver, filter);
		return receiver;
	}
	
	public static BroadcastReceiver registerWifiRssiChangedReceiver(Context context, BroadcastReceiver receiver) {
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
		context.registerReceiver(receiver, filter);
		return receiver;
	}
	
	public BroadcastReceiver registerScanResultsAvailableReceiver(Context context, BroadcastReceiver receiver) {
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		context.registerReceiver(receiver, filter);
		return receiver;
	}
	
	public BroadcastReceiver registerDownloadCompleted(Context context, BroadcastReceiver receiver) {
		IntentFilter filter = new IntentFilter();
		filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		context.registerReceiver(receiver, filter);
		return receiver;
	}
	
	public void unregisterReceiver(Context context, BroadcastReceiver receiver) {
		if (receiver != null) {
			try {
				context.unregisterReceiver(receiver);
			} catch (Exception e) {
                e.printStackTrace();
			}
		}
	}
	
	public void registerObserver(Context context, ContentObserver observer) {
		
	}
	
	public void unregisterObserver(Context context, ContentObserver observer) {
		if (observer != null) {
			context.getContentResolver().unregisterContentObserver(observer);
		}
	}
}
