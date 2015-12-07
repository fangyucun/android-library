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
 */

package com.hellofyc.applib.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Log Tool
 * Create on 2014年12月6日 下午12:23:58
 *
 * @author Jason Fang
 */
public final class Flog {
	static final boolean DEBUG = false;
	
//	private static final String UPLOAD_URL = "";
	
	private static final int MSG_WHAT_WRITE = 1;
	private static final int MSG_WHAT_UPLOAD = 2;
	private static final int MSG_WHAT_UPLOAD_BY_FTP = 3;
	
	private static Flog mInstance;
	private Context mContext;
	private Executor mThreadPool = Executors.newFixedThreadPool(4);
	private MainHandler mHandler = new MainHandler(Looper.getMainLooper());
	private File mDefaultDir;
	private File mCrashDir;
	private String mDefaultFileName;
	
	private Flog(Context context) {
		mContext = context;
		try {
			mDefaultDir = FileUtils.getDirectory(StorageUtils.getExternalStorageRootDir().toString() + "/" + 
						StringUtils.getSimpleNameFromFullName(mContext.getPackageName()));
		} catch (IOException e) {
			mDefaultDir = context.getFilesDir();
		}
		mCrashDir = new File(context.getCacheDir(), "logs");
		FileUtils.createDir(mCrashDir);
		mDefaultFileName = "log-" + TimeUtils.getCurrentTime("yyyyMMdd_HHmmss") + ".log";
	}
	
	public static Flog getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new Flog(context);
		}
		return mInstance;
	}

	public static void i(String tag, String message) {
		i(tag, message, false);
	}
	
	public static void i(Object obj) {
        String[] messages = StringUtils.divideString(String.valueOf(obj), 4000);
        int size = messages.length;
        for (int i=0; i<size; i++) {
            if (i == 0) {
                println(Log.INFO, getInvokerClassName(), messages[i]);
            } else {
                println(Log.INFO, getInvokerClassName(), "->" + messages[i]);
            }
        }
	}
	
	public static void i(String tag, Object obj) {
		println(Log.INFO, tag, String.valueOf(obj));
	}
	
	public static void i(String tag, String format, Object... args) {
		println(Log.INFO, tag, String.format(format, args));
	}

    public static void w(Object text) {
        println(Log.WARN, getInvokerClassName(), String.valueOf(text));
    }
	
	public static void e(Object obj) {
		println(Log.ERROR, getInvokerClassName(), String.valueOf(obj));
	}
	
	public static void e(Throwable tr) {
		println(Log.ERROR, getInvokerClassName(), getStackTraceString(tr));
	}
	
	public static void e(String tag, Throwable tr) {
		println(Log.ERROR, tag, getStackTraceString(tr));
	}
	
	public static void e(String tag, String msg, Throwable tr) {
		println(Log.ERROR, tag, msg + "\n" + getStackTraceString(tr));
	}
	
	public static void e(String tag, String format, Object... args) {
		println(Log.ERROR, tag, String.format(format, args));
	}

    public static void json(String jsonText) {
        int indent = 4;
        if (TextUtils.isEmpty(jsonText)) {
            i("json is null");
            return;
        }
        try {
            if (jsonText.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(jsonText);
                String msg = jsonObject.toString(indent);
                i(msg);
            } else if (jsonText.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(jsonText);
                String msg = jsonArray.toString(indent);
                i(msg);
            }
        } catch (JSONException e) {
            e(e);
        }
    }
	
	public static String getCurrentMethodName() {
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}
	
	public static void p(Object o) {
		System.out.println(o);
	}
	
	public static void p(String TAG, Object obj) {
		System.out.println(TextUtils.isEmpty(TAG) ? obj : TAG + ":" + obj);
	}
	
	public static void err(Object o) {
		System.err.println(o);
	}
	
	public static String getStackTraceString(Throwable tr) {
		return Log.getStackTraceString(tr);
	}
	
	public static void println(int priority, String tag, String msg) {
		Log.println(priority, tag, msg);
	}
	
	public static String getInvokerClassName() {
		StackTraceElement[] elements = (new Throwable()).getStackTrace();
		return StringUtils.getSimpleNameFromFullName(elements[2].getClassName());
	}
	
	public static String getInvokerMethodName() {
		StackTraceElement[] elements = (new Throwable()).getStackTrace();
		return elements[2].getMethodName();
	}
	
	public void logToFile(String text) {
		logToFile(text, null);
	}
	
	public void logToFile(String text, OnWriteListener listener) {
		logToFile(mDefaultDir, mDefaultFileName, text, listener);
	}
	
	public void logToFile(File dir, String fileName, String text, OnWriteListener listener) {
		mThreadPool.execute(new WriteTask(dir, fileName, text, listener));
	}
	
//	public void uploadCrashLogByFtp(OnUploadListener listener) {
//		mThreadPool.execute(new UploadCrashLogByFtpTask(listener));
//	}
	
	/**
	 * 上传日志
	 * @param id 可以指定上传的目录ID
	 * @param fileList 文件列表 为空时取默认
	 * @param listener 回调
	 */
	public void uploadLog(@Nullable String id, @Nullable List<File> fileList, @Nullable OnUploadListener listener) {
		mThreadPool.execute(new UploadTask(id, fileList, listener));
	}
	
	static class MainHandler extends Handler {

		public MainHandler(Looper looper) {
			super(looper);
		}
		
		@Override
		public void handleMessage(Message msg) {
			HandlerResult result = (HandlerResult) msg.obj;
			if (msg.what == MSG_WHAT_WRITE) {
				OnWriteListener onWriteListener = null;
				if (result.mOnOpListener instanceof OnWriteListener) {
					onWriteListener = (OnWriteListener)result.mOnOpListener;
				}
				
				if (result.mIsSuccess) {
					if (onWriteListener != null) {
						onWriteListener.onWriteSuccess();
					}
				} else {
					if (onWriteListener != null) {
						onWriteListener.onWriteFailure(msg.what);
					}
				}
			} else if (msg.what == MSG_WHAT_UPLOAD || msg.what == MSG_WHAT_UPLOAD_BY_FTP) {
				OnUploadListener onUploadListener = null;
				if (result.mOnOpListener instanceof OnUploadListener) {
					onUploadListener = (OnUploadListener)result.mOnOpListener;
				}
				
				if (onUploadListener != null) {
					onUploadListener.onUploadResult(result.mResultMap);
				}
			}
			super.handleMessage(msg);
		}
		
	}
	
	static class HandlerResult {
		OnOpListener mOnOpListener;
		boolean mIsSuccess;
		Map<String, Boolean> mResultMap;
		
		public HandlerResult(OnOpListener listener) {
			mOnOpListener = listener;
		}
		
		public void setResult(boolean isSuccess) {
			mIsSuccess = isSuccess;
		}
		
		public void setResultMap(Map<String, Boolean> map) {
			mResultMap = map;
		}
		
	}
	
	/**
	 * 日志线程
	 * @author Yucun
	 *
	 */
	class WriteTask implements Runnable {

		private File mTargetDir;
		private String mFileName;
		private String mText;
		private OnWriteListener mOnWriteListener;
		
		public WriteTask(File dir, String fileName, String text, OnWriteListener listener) {
			mTargetDir = dir;
			mFileName = fileName;
			mText = text;
			mOnWriteListener = listener;
		}
		
		@Override
		public void run() {
			if (mContext == null) {
				Flog.e("mContext can not be null!");
				return;
			}
			
			Message msg = mHandler.obtainMessage(MSG_WHAT_WRITE);
			HandlerResult result = new HandlerResult(mOnWriteListener);
			
			if (StorageUtils.isExternalStorageAvailable(mContext)) {
				File file = FileUtils.writeString(mText, mTargetDir, mFileName);
				if (file == null) {
					result.setResult(false);
				} else {
					result.setResult(true);
				}
			} else {
				result.setResult(false);
			}
			msg.obj = result;
			
			if (mOnWriteListener != null) {
				mHandler.sendMessage(msg);
			}
		}
		
	}

	/**
	 * 上传线程
	 * @author Yucun
	 *
	 */
	class UploadTask implements Runnable {

		private String mId;
		private List<File> mFileList;
		private OnUploadListener mOnUploadListener;
		
		public UploadTask(String id, List<File> fileList, OnUploadListener listener) {
			mId = id;
			mFileList = fileList;
			mOnUploadListener = listener;
		}
		
		@Override
		public void run() {
			if (mContext == null) {
				Flog.e("mContext can not be null!");
				return;
			}
			
			Message msg = mHandler.obtainMessage(MSG_WHAT_UPLOAD);
//			HandlerResult result = new HandlerResult(mOnUploadListener);
			Map<String, String> params = new ArrayMap<>();
			params.put("id", mId);
			
			if (CollectionUtils.isEmpty(mFileList)) {
				mFileList = FileUtils.getSubFiles(mDefaultDir);
			}
			
//			ArrayMap<String, Boolean> resultMap = new ArrayMap<String, Boolean>();
//			for (File file : mFileList) {
//				FIXME
//				String resultString = HttpUtils.uploadFile(UPLOAD_URL, params, file);
//				if (!TextUtils.isEmpty(resultString)) {
//					FileUtils.deleteFile(file);
//					resultMap.put(file.getPath(), true);
//				} else {
//					resultMap.put(file.getPath(), false);
//				}
//			}
//			result.setResultMap(resultMap);
//			msg.obj = result;
//			
			if (mOnUploadListener != null) {
				mHandler.sendMessage(msg);
			}
		}
		
	}
	
//	class UploadCrashLogByFtpTask implements Runnable {
//
//		private OnUploadListener mOnUploadListener;
//		
//		public UploadCrashLogByFtpTask(OnUploadListener listener) {
//			mOnUploadListener = listener;
//		}
//		
//		@Override
//		public void run() {
//			if (mContext == null) {
//				Flog.e("mContext can not be null!");
//				return;
//			}
//			
//			Message msg = mHandler.obtainMessage(MSG_WHAT_UPLOAD_BY_FTP);
//			HandlerResult result = new HandlerResult(mOnUploadListener);
//			
//			Map<String, Boolean> resultMap = new ArrayMap<String, Boolean>();
//			
//			File[] files = mCrashDir.listFiles();
//			if (files != null && files.length != 0) {
//				resultMap = FtpUtils.upload(TimeUtils.getCurrentDate(), Arrays.asList(files));
//			}
//			
//			for (Map.Entry<String, Boolean> entry : resultMap.entrySet()) {
//				if (entry.getValue()) FileUtils.deleteFile(entry.getKey());
//			}
//			
//			result.setResultMap(resultMap);
//			msg.obj = result;
//			
//			if (mOnUploadListener != null) {
//				mHandler.sendMessage(msg);
//			}
//		}
//		
//	}
	
	public interface OnWriteListener extends OnOpListener {
		void onWriteSuccess();
		void onWriteFailure(int errorCode);
	}
	
	public interface OnUploadListener extends OnOpListener {
		void onUploadResult(Map<String, Boolean> filePathMap);
	}
	
	interface OnOpListener {
	}
}
