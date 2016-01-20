/*
 * Copyright (C) 2014 Jason Fang ( ifangyucun@gmail.com )
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

package com.hellofyc.base.helper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import com.hellofyc.base.util.FLog;

import java.io.IOException;
import java.util.UUID;

/**
 * Create on 2015年3月6日 下午7:43:39
 * @author Jason Fang
 */
public class BluetoothAdmin {
	static final boolean DEBUG = true;
	
	public static final int REQUEST_ENABLE = 1;
	public static final int REQUEST_DISABLE = 2;
	
	private static BluetoothAdmin sInstance;
	private Context mContext;
	private BluetoothAdapter mAdapter;
	
	private OnDiscoveryCallback mDiscoveryCallback;
	private DeviceReceiver mDiscoveryReceiver;
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	private BluetoothAdmin(Context context) {
		mContext = context;
		mAdapter = BluetoothAdapter.getDefaultAdapter();
	}
	
	public static BluetoothAdmin getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new BluetoothAdmin(context);
		}
		return sInstance;
	}
	
	public boolean isEnabled() {
		return mAdapter.isEnabled();
	}
	
	public boolean setEnabled(boolean enabled) {
		return enabled ? mAdapter.enable() : mAdapter.disable();
	}
	
	public boolean setEnabledByTips() {
		Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		if (mContext instanceof Activity) {
			((Activity)mContext).startActivityForResult(intent, REQUEST_ENABLE);
			return true;
		} else {
			return false;
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_ENABLE:
			break;
		}
    }
	
	public void startDiscovery(OnDiscoveryCallback callback) {
		mDiscoveryCallback = callback;
		registerDiscoveryReceiver();
	}
	
	private void registerDiscoveryReceiver() {
		if (mDiscoveryReceiver != null) {
			mDiscoveryReceiver = new DeviceReceiver();
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		mContext.registerReceiver(mDiscoveryReceiver, filter);
	}
	
	private void unregisterDiscoveryReceiver() {
		if (mDiscoveryReceiver == null) return;
		try {
			mContext.unregisterReceiver(mDiscoveryReceiver);
			mDiscoveryReceiver = null;
		} catch (Exception e) {
            e.printStackTrace();
		}
	}
	
	class DeviceReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					if (mDiscoveryCallback != null) {
						mDiscoveryCallback.onDiscovery(device);
					}
				}
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				if (mDiscoveryCallback != null) {
					mDiscoveryCallback.onDiscoveryFinish();
					unregisterDiscoveryReceiver();
				}
			}
		}
		
	}
	
	public interface OnDiscoveryCallback {
		void onDiscovery(BluetoothDevice device);
		void onDiscoveryFinish();
	}
	
	public void accept(String serverName, UUID uuid) {
		try {
			BluetoothServerSocket socket = mAdapter.listenUsingRfcommWithServiceRecord(serverName, uuid);
			socket.accept();
		} catch (IOException e) {
			FLog.e(e);
		}
	}
}
