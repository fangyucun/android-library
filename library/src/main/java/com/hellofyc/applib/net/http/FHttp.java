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

package com.hellofyc.applib.net.http;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import com.hellofyc.applib.util.Flog;

import java.io.File;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Http Framework
 * @author Jason Fang
 *
 * Create on 2014年12月6日 上午11:26:15
 */
public class FHttp {
	static final boolean DEBUG = false;
	
	private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
	private static final int MAXIMUM_POOL_SIZE = CORE_POOL_SIZE * 2 + 1;
	private static final int KEEP_ALIVE = 1;
	
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, "FHttp #" + mCount.getAndIncrement());
        }
    };
    
    private static final BlockingQueue<Runnable> sPoolWorkQueue = 
    		new LinkedBlockingQueue<>(128);
	
	private static final int MSG_LOAD_INFO = 0x1;
	
	private static final InternalHandler sHandler = new InternalHandler();
	
	private static FHttp sHttpRequest;
	
	public static final Executor THREAD_POOL_EXECUTOR = 
			new ThreadPoolExecutor(CORE_POOL_SIZE
					, MAXIMUM_POOL_SIZE
					, KEEP_ALIVE
					, TimeUnit.SECONDS
					, sPoolWorkQueue
					, sThreadFactory);
	
//	private ExecutorService mExecutor = Executors.newFixedThreadPool(15);

	public static FHttp getInstance() {
		if (sHttpRequest == null) {
			sHttpRequest = new FHttp();
		}
		return sHttpRequest;
	}
	
	public void doPost(String urlString, HttpCallback callback) {
		doRequest(Method.POST, urlString, null, null, callback);
	}
	
	public void doPost(String urlString, Map<String, String> params, HttpCallback callback) {
		doRequest(Method.POST, urlString, null, params, callback);
	}
	
	public void doPost(String urlString, String tag, HttpCallback callback) {
		doRequest(Method.POST, urlString, tag, null, callback);
	}
	
	public void doPost(String urlString, String tag, Map<String, String> params, HttpCallback callback) {
		doRequest(Method.POST, urlString, tag, params, callback);
	}
	
	public void doGet(String urlString, HttpCallback callback) {
		doRequest(Method.GET, urlString, null, null, callback);
	}
	
	public void doGet(String urlString, String tag, HttpCallback callback) {
		doRequest(Method.GET, urlString, tag, null, callback);
	}
	
	public void doRequest(int method, String urlString, String tag, Map<String, String> params, HttpCallback callback) {
		THREAD_POOL_EXECUTOR.execute(new LoadDataTask(method, urlString, tag, params, callback));
	}
	
	public void uploadFile(String urlString, Map<String, String> params, File file, HttpCallback callback) {
		ArrayMap<String, File> fileParams = new ArrayMap<>();
		fileParams.put("file", file);
		uploadFiles(urlString, params, fileParams, callback);
	}
	
	public void uploadFiles(String urlString, Map<String, String> params, Map<String, File> fileParams, HttpCallback callback) {
		THREAD_POOL_EXECUTOR.execute(new UploadFileTask(urlString, params, fileParams, callback));
	}
	
	private static class InternalHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_LOAD_INFO:
				HttpResult result = (HttpResult) msg.obj;
				String responseText = result.mResponseText;
				
				if (result.mHttpCallback == null) {
					if (DEBUG) Flog.e("HttpCallback can not be null!");
					return;
				}
				
				if(DEBUG) Flog.i("Response Text:" + result.mResponseText);
				
				result.mHttpCallback.onRequestFinish(result.mTag);
				
				if (responseText != null) {
					result.mHttpCallback.onRequestSuccess(result.mTag, responseText);
				} else {
					result.mHttpCallback.onRequestFailure(result.mTag, ErrorUtils.ERROR_CODE_NETWORK);
				}
				break;
			}
			super.handleMessage(msg);
		}
		
	}
	
	private class LoadDataTask implements Runnable {

		private int mMethod;
		private String mUrlString;
		private Map<String, String> mParams;
		private String mTag;
		private HttpCallback mCallback;
		
		public LoadDataTask(int Method, String urlString, String tag, Map<String, String> params, HttpCallback callback) {
			mMethod = Method;
			mUrlString = urlString;
			mTag = tag;
			mParams = params;
			mCallback = callback;
		}
		
		@Override
		public void run() {
			Message msg = sHandler.obtainMessage(MSG_LOAD_INFO);
			HttpResponse response = HttpUtils.doRequest(mMethod, mUrlString, mParams);
			msg.obj = new HttpResult(mTag, response.text, mCallback);
			msg.sendToTarget();
		}
		
	}
	
	private class UploadFileTask implements Runnable {

		private String mUrlString;
		private Map<String, String> mParams;
		private Map<String, File> mFileParams;
		private HttpCallback mCallback;
		
		public UploadFileTask(String urlString, Map<String, String> params, Map<String, File> fileParams, HttpCallback callback) {
			mUrlString = urlString;
			mParams = params;
			mFileParams = fileParams;
			mCallback = callback;
		}
		
		@Override
		public void run() {
			Message msg = sHandler.obtainMessage();
			ArrayMap<String, File> fileParams = new ArrayMap<>();
			fileParams.put("file", mFileParams.get("file"));
			String jsonString = HttpUtils.uploadFile(mUrlString, mParams, fileParams);
			msg.obj = new HttpResult(null, jsonString, mCallback);
			msg.sendToTarget();
		}
		
	}
	
	private class HttpResult {
		final String mResponseText;
		final String mTag;
		final HttpCallback mHttpCallback;
		
		public HttpResult(String tag, String responseText, HttpCallback callback) {
			mResponseText = responseText;
			mTag = tag;
			mHttpCallback = callback;
		}
	}
	
	public static class HttpCallback {
		public void onRequestSuccess(String tag, String responseText){
		}
		
		public void onRequestFailure(String tag, int errorCode) {
		}
		
		public void onRequestFinish(String tag){
		}
	}
	
	private FHttp(){/*Do not new me!*/}
}

