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
 */package com.hellofyc.base.content;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;

import com.hellofyc.base.utils.AndroidUtils;
import com.hellofyc.base.utils.FLog;
import com.hellofyc.base.utils.IntentUtils;
import com.hellofyc.base.utils.StorageUtils;
import com.hellofyc.base.utils.StringUtils;

/**
 * 下载管理类
 * @author Jason Fang
 * @since 2014年11月19日 下午2:32:55
 * Use Permission:
 * 				{@link android.Manifest.permission#INTERNET}
 * 				{"android.permission.DOWNLOAD_WITHOUT_NOTIFICATION"}
 */
public class DownloadHelper {
	private static final boolean DEBUG = true;

	public static final int ERROR_CODE_NO_SDCARD = -1;
	
	private Context mContext;
	private DownloadManager mDownloadManager;
	private OnDownloadListener mOnDownloadListener;
	
	private BroadcastReceiver mDownloadCompleteReceiver;
	
	private DownloadHelper(Context context) {
		mContext = context;
		mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
	}
	
	public static DownloadHelper newInstance(Context context) {
		return new DownloadHelper(context.getApplicationContext());
	}
	
	public long download(String urlString, String name, OnDownloadListener listener) {
		return download(urlString, name, DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE, true, listener);
	}
	
	public long download(String urlString, String name, boolean showNotification, OnDownloadListener listener) {
		return download(urlString, name, DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE, showNotification, listener);
	}
	
	@SuppressWarnings("deprecation")
	public long download(String urlString, String name, int flags, boolean showNotification, OnDownloadListener listener) {
		if (!AndroidUtils.isDownloadManagerEnable(mContext)) {
			return -1;
		}
		
		mOnDownloadListener = listener;
		
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlString));
		request.setAllowedNetworkTypes(flags);
		if (Build.VERSION.SDK_INT >= 11) {
			request.setNotificationVisibility(showNotification ? View.VISIBLE : View.GONE);
		} else {
			request.setShowRunningNotification(showNotification);
		}
		request.setVisibleInDownloadsUi(true);
		
		if (StorageUtils.isExternalStorageAvailable()) {
			request.setDestinationInExternalFilesDir(mContext, null, TextUtils.isEmpty(name) ? StringUtils.getFileNameFromUrl(urlString) : name);
		} else {
			if (mOnDownloadListener != null) {
				mOnDownloadListener.onDownloadError(ERROR_CODE_NO_SDCARD);
			}
			return -1;
		}
		
		registerDownloadReceiver();
		return mDownloadManager.enqueue(request);
	}
	
	public void remove(long... ids) {
		mDownloadManager.remove(ids);
	}
	
	/**
	 * 获取错误信息
	 */
	public static String getErrorMsg(int errorCode) {
		switch (errorCode) {
		case ERROR_CODE_NO_SDCARD:
			return "无SD卡";
		}
		return "UNKNOWN";
	}
	
	public void setOnDownloadCompleteListener(SimpleOnDownloadListener listener) {
		mOnDownloadListener = listener;
	}
	
	public Uri getUriForDownloadedFile(long id) {
		return mDownloadManager.getUriForDownloadedFile(id);
	}
	
	class DownloadReceiver extends BroadcastReceiver {
		
        @Override
        public void onReceive(Context context, Intent intent) {
        	final String action = intent.getAction();
            if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)){  
                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (mOnDownloadListener != null) {
                	mOnDownloadListener.onDownloadComplete(downloadId);
                }
                unregisterDownloadReceiver();
            } else if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(action)) {
                Intent downloadIntent = IntentUtils.getDownloadActivityIntent(mContext);
                if (downloadIntent != null) {
                    mContext.startActivity(downloadIntent);
                }

            }
        }
    }
	
	/**
	 * 注册下载完成监听器
	 */
	private void registerDownloadReceiver() {
		if (mDownloadCompleteReceiver == null) {
			mDownloadCompleteReceiver = new DownloadReceiver();
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		filter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
		mContext.registerReceiver(mDownloadCompleteReceiver, filter);
	}
	
	/**
	 * 反注册下载完成监听器
	 */
	private void unregisterDownloadReceiver() {
		if (mDownloadCompleteReceiver != null) {
			try {
				mContext.unregisterReceiver(mDownloadCompleteReceiver);
			} catch (Exception e) {
				if (DEBUG) FLog.e(e);
			}
		}
		mDownloadCompleteReceiver = null;
	}
	
	public static class SimpleOnDownloadListener implements OnDownloadListener {

		@Override
		public void onDownloadStart() {
		}

		@Override
		public void onDownloadComplete(long downloadId) {
		}

		@Override
		public void onDownloadProgress(int progress) {
		}

		@Override
		public void onDownloadError(int errorCode) {
		}

		@Override
		public void onNotificatonClick(long downloadId) {
		}
		
	}
	
	public interface OnDownloadListener {
		void onDownloadStart();
		void onDownloadComplete(long downloadId);
		void onDownloadProgress(int progress);
		void onDownloadError(int errorCode);
		void onNotificatonClick(long downloadId);
	}
}
