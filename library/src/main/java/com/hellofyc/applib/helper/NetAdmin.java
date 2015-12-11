/*
 *  Copyright (C) 2012-2015 Jason Fang ( ifangyucun@gmail.com )
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */package com.hellofyc.applib.helper;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;

import com.hellofyc.applib.content.BaseBroadcastReceiver;
import com.hellofyc.applib.net.http.FHttp;
import com.hellofyc.applib.util.FLog;
import com.hellofyc.applib.util.ReflectUtils;

/**
 * 网络开关
 * @author Jason Fang
 * Create on 2014-10-17 下午2:19:25
 */
public class NetAdmin {
	static final boolean DEBUG = true;
	
	private static NetAdmin sInstance;
	private Context mContext;
	
	private OnMobileDataListener mOnMobileDataListener;
	
	private MobileDataReceiver mMobileDataReceiver;
	
	private NetAdmin(Context context) {
		mContext = context;
	}
	
	public static NetAdmin getInstance(Fragment fragment) {
		return getInstance(fragment.getActivity());
	}
	
	public static NetAdmin getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new NetAdmin(context);
		}
		return sInstance;
	}
	
	/**
	 * 检测是否能上公网
	 */
	public void checkNetAvailable(final OnNetAvaliableListener listener) {
		FHttp.getInstance().doGet("http://www.baidu.com", new FHttp.HttpCallback() {
			
			@Override
			public void onRequestSuccess(String tag, String responseText) {
				super.onRequestSuccess(tag, responseText);
				if (responseText.contains("百度")) {
					if (listener != null) {
						listener.onNetAvailable();
					}
				} else {
					if (listener != null) {
						listener.onNetUnavailable();
					}
				}
			}

			@Override
			public void onRequestFailure(String tag, int errorCode) {
				if (listener != null) {
					listener.onNetUnavailable();
				}
			}
		});
	}
	
    public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cManager.getActiveNetworkInfo();
		
		return networkInfo != null && networkInfo.isAvailable();
	}
	
	/**
	 * 注册移动数据开关
	 */
	public void registerMobileDataReceiver() {
		if (mMobileDataReceiver == null) {
			mMobileDataReceiver = new MobileDataReceiver();
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		mContext.registerReceiver(mMobileDataReceiver, filter);
	}
	
	public void unregisterMobileDataReceiver() {
		if (mMobileDataReceiver != null) {
			try {
				mContext.unregisterReceiver(mMobileDataReceiver);
				mMobileDataReceiver = null;
			} catch (Exception e) {
				if (DEBUG) FLog.e(e);
			}
		}
	}
	
	/**
	 * Use Permission:
	 * {@link android.Manifest.permission#CHANGE_NETWORK_STATE}
	 * {@link android.Manifest.permission#MODIFY_PHONE_STATE}
	 * 
	 * @return true	{@code true} if the operation succeeds (or if the existing state
     *         is the same as the requested state). WRAN: cannot know the user refuse the permission!
	 */
	public boolean setMobileDataEnabled(boolean enabled, OnMobileDataListener listener) {
		final int version = Build.VERSION.SDK_INT;
		if (version >= 20) {
			FLog.e("setMobileDataEnabled is not support API 20!");
			return false;
		}
		mOnMobileDataListener = listener;
		if (listener != null) {
			registerMobileDataReceiver();
		}
		
		if (version >= 21) {
            TelephonyManager tManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            try {
                ReflectUtils.invokeMethod(tManager, "setDataEnabled", enabled);
            } catch (Exception e) {
                if (DEBUG) FLog.e(e);
                return false;
            }
        } else {
            ConnectivityManager cManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            try {
                ReflectUtils.invokeMethod(cManager, "setMobileDataEnabled" , enabled);
            } catch (Exception e) {
                if (DEBUG) FLog.e(e);
                return false;
            }
        }
		return true;
	}
	
	/**
	 * Mobile Data is open
	 */
	public boolean isMobileDataEnabled() {
		final int version = Build.VERSION.SDK_INT;
		if (version == 20) {
			FLog.e("API 20 is not support!");
			return false;
		}

        if (version >= 21) {
            TelephonyManager tManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            try {
                return (Boolean) ReflectUtils.invokeMethod(tManager, "getDataEnabled");
            } catch (Exception e) {
                if (DEBUG) FLog.e(e);
                return false;
            }
        } else {
            ConnectivityManager cManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            try {
                return (Boolean) ReflectUtils.invokeMethod(cManager, "getMobileDataEnabled");
            } catch (Exception e) {
                if (DEBUG) FLog.e(e);
                return false;
            }
        }

	}

	private class MobileDataReceiver extends BaseBroadcastReceiver {

		public void onMobileDataEnabled() {
			if (mOnMobileDataListener != null) {
				mOnMobileDataListener.onMobileDataEnabled();
				unregisterMobileDataReceiver();
			}
		}
		
//		public void onMobileDataDisabled() {
//			if (mOnMobileDataListener != null) {
//				mOnMobileDataListener.onMobileDataDisabled();
////				unregisterMobileDataReceiver();
//			}
//		}
		
		@Override
		public void onReceive(Context context, Intent intent) {
			super.onReceive(context, intent);
			if (DEBUG) FLog.i("===onReceive===");
			
			String action = intent.getAction();
			if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
				ConnectivityManager cManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo info = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				if (DEBUG) FLog.i("===" + info.getState());
				if (info.isConnected()) {
					onMobileDataEnabled();
				}
			}
		}

		@Override
		public int hashCode() {
			return super.hashCode();
		}

		@Override
		public String toString() {
			return super.toString();
		}
		
	}
	
	public interface OnMobileDataListener {
		void onMobileDataEnabled();
		void onMobileDataDisabled();
	}
	
	public interface OnNetAvaliableListener {
		void onNetAvailable();
		void onNetUnavailable();
	}
}
