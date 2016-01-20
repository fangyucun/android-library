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

package com.hellofyc.base.helper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.hellofyc.base.util.FLog;

/**
 * 设备管理器管理类
 * Create on 2014年11月8日 下午1:48:16
 * @author Jason Fang
 */
public class DevicePolicyAdmin {
	
	public static final int REQUEST_CODE_ENABLE_ADMIN = 1;

	private static DevicePolicyAdmin sInstance;
	private Activity mActivity;
	private DevicePolicyManager mDPManager;
	private ComponentName mConponentName;
	
	public DevicePolicyAdmin(Context context) {
		if (context instanceof Activity) {
			mActivity = (Activity)context;
		} else {
			FLog.e("context is not the instance of Activity!");
		}
		mDPManager = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		mConponentName = new ComponentName(context, JasonDeviceAdminReceiver.class);
	}
	
	public static DevicePolicyAdmin getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new DevicePolicyAdmin(context);
		}
		return sInstance;
	}
	
	/**
	 * 激活设备管理器
	 */
	public void activieAdmin () {
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mConponentName);
        mActivity.startActivity(intent);
	}
	
	/**
	 * 取消激活设备管理器
	 */
	public void removeActiveAdmin() {
		mDPManager.removeActiveAdmin(mConponentName);
	}
	
	public boolean isActivePasswordSufficient() {
		return mDPManager.isActivePasswordSufficient();
	}
	
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public void setProfileEnabled() {
		mDPManager.setProfileEnabled(mConponentName);
	}
	
	public DevicePolicyManager getDevicePolicyManager() {
		return mDPManager;
	}
	
	/**
	 * 设备是否激活
	 */
	public boolean isAdminActive() {
		return mDPManager.isAdminActive(mConponentName);
	}
	
	/**
	 * 立即锁屏
	 */
	public void lockNow() {
		mDPManager.lockNow();
	}
	
	/**
	 * 清除数据
	 */
	public void wipeData(boolean isWipeAllData) {
		mDPManager.wipeData(isWipeAllData ? DevicePolicyManager.WIPE_EXTERNAL_STORAGE : 0);
	}
	
	/**
	 * 获取密码过期时间
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public long getPasswordExpiration() {
		return mDPManager.getPasswordExpiration(mConponentName);
	}
	
	/**
	 * 密码是否过期
	 */
	public boolean isPasswordExpired() {
		return getPasswordExpiration() - System.currentTimeMillis() < 0L;
	}
	
	/**
	 * 设备监听器
	 * @author Jason Fang
	 */
	public class JasonDeviceAdminReceiver extends DeviceAdminReceiver {

		@Override
		public DevicePolicyManager getManager(Context context) {
			return super.getManager(context);
		}

		@Override
		public ComponentName getWho(Context context) {
			return super.getWho(context);
		}

		@Override
		public void onEnabled(Context context, Intent intent) {
			super.onEnabled(context, intent);
		}

		@Override
		public CharSequence onDisableRequested(Context context, Intent intent) {
			return super.onDisableRequested(context, intent);
		}

		@Override
		public void onDisabled(Context context, Intent intent) {
			super.onDisabled(context, intent);
		}

		@Override
		public void onPasswordChanged(Context context, Intent intent) {
			super.onPasswordChanged(context, intent);
		}

		@Override
		public void onPasswordFailed(Context context, Intent intent) {
			super.onPasswordFailed(context, intent);
		}

		@Override
		public void onPasswordSucceeded(Context context, Intent intent) {
			super.onPasswordSucceeded(context, intent);
		}

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
		public void onPasswordExpiring(Context context, Intent intent) {
			super.onPasswordExpiring(context, intent);
		}

		@TargetApi(Build.VERSION_CODES.LOLLIPOP)
		@Override
		public void onProfileProvisioningComplete(Context context, Intent intent) {
			super.onProfileProvisioningComplete(context, intent);
		}

		@TargetApi(Build.VERSION_CODES.LOLLIPOP)
		@Override
		public void onLockTaskModeEntering(Context context, Intent intent,
				String pkg) {
			super.onLockTaskModeEntering(context, intent, pkg);
		}

		@TargetApi(Build.VERSION_CODES.LOLLIPOP)
		@Override
		public void onLockTaskModeExiting(Context context, Intent intent) {
			super.onLockTaskModeExiting(context, intent);
		}
		
	}
	
}
