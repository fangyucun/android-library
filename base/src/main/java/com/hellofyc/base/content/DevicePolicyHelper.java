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

package com.hellofyc.base.content;

import android.annotation.TargetApi;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * 设备管理器管理类
 * Create on 2014年11月8日 下午1:48:16
 * @author Jason Fang
 */
public class DevicePolicyHelper {
	
	public static final int REQUEST_CODE_ENABLE_ADMIN = 1;

	private Context mContext;
	private DevicePolicyManager mDPManager;
	private DeviceAdminReceiver mDeviceAdminReceiver;
	private ComponentName mComponentName;

	private DevicePolicyHelper(Context context) {
		mContext = context;
		mDPManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		mDeviceAdminReceiver = new DeviceAdminReceiver();
	}
	
	public static DevicePolicyHelper newInstance(Context context) {
		return new DevicePolicyHelper(context);
	}

	public DevicePolicyHelper setReceiver(DeviceAdminReceiver receiver) {
		mDeviceAdminReceiver = receiver;
		return this;
	}
	
	/**
	 * 激活设备管理器
	 */
	public void activieAdmin () {
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		mComponentName = new ComponentName(mContext, mDeviceAdminReceiver.getClass());
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
        mContext.startActivity(intent);
	}
	
	/**
	 * 取消激活设备管理器
	 */
	public void removeActiveAdmin() {
		mDPManager.removeActiveAdmin(mComponentName);
	}
	
	public boolean isActivePasswordSufficient() {
		return mDPManager.isActivePasswordSufficient();
	}
	
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public DevicePolicyHelper setProfileEnabled() {
		mDPManager.setProfileEnabled(mComponentName);
		return this;
	}
	
	public DevicePolicyManager getDevicePolicyManager() {
		return mDPManager;
	}
	
	/**
	 * 设备是否激活
	 */
	public boolean isAdminActive() {
		return mDPManager.isAdminActive(mComponentName);
	}
	
	/**
	 * 立即锁屏
	 */
	public DevicePolicyHelper lockNow() {
		mDPManager.lockNow();
		return this;
	}
	
	/**
	 * 清除数据
	 */
	public DevicePolicyHelper wipeData(boolean isWipeAllData) {
		mDPManager.wipeData(isWipeAllData ? DevicePolicyManager.WIPE_EXTERNAL_STORAGE : 0);
		return this;
	}
	
	/**
	 * 获取密码过期时间
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public long getPasswordExpiration() {
		return mDPManager.getPasswordExpiration(mComponentName);
	}
	
	/**
	 * 密码是否过期
	 */
	public boolean isPasswordExpired() {
		return getPasswordExpiration() - System.currentTimeMillis() < 0L;
	}
}
