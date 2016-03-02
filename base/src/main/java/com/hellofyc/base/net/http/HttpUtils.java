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

package com.hellofyc.base.net.http;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.hellofyc.base.util.EncodeUtils;
import com.hellofyc.base.util.FLog;
import com.hellofyc.base.util.FileUtils;
import com.hellofyc.base.util.IoUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.UUID;

public class HttpUtils {

	private static final String HEADER_CONTENT_TYPE = "Content-Type";
	private static final String HEADER_CHARSET = "Charset";
	private static final String HEADER_CONNECTION = "connection";

	private static final String BODY_CONTENT_TYPE = "application/x-www-form-urlencoded";

	private static final String PREFIX = "--";
	private static final String LINE_END = "\r\n";
	
	private static String BOUNDARY = UUID.randomUUID().toString();

    private boolean mDebug = false;
    private HttpRequest mRequestParams = HttpRequest.create();
    private Method mMethod = Method.POST;
    private String mUrlString;
    private int mConnectTimeout = 30 * 1000;
    private int mReadTimeout = 30 * 1000;

    protected HttpUtils() {
    }

    public static HttpUtils create() {
        return new HttpUtils();
    }

    public HttpUtils setReqeustParams(@NonNull HttpRequest requestParams) {
        mRequestParams = requestParams;
        return this;
    }

    public HttpUtils setMethod(@NonNull Method method) {
        mMethod = method;
        return this;
    }

    public HttpUtils setUrl(@NonNull String urlString) {
        mUrlString = urlString;
        return this;
    }

    public HttpUtils setReadTimeout(@IntRange(from = 1) int readTimeoutInMillis) {
        mReadTimeout = readTimeoutInMillis;
        return this;
    }

    public HttpUtils setConnectTimeout(@IntRange(from = 1) int connectTimeoutInMills) {
        mConnectTimeout = connectTimeoutInMills;
        return this;
    }

    public HttpUtils setDebugEnable() {
        mDebug = true;
        return this;
    }

	public HttpResponse request(){
        HttpResponse response = new HttpResponse();
		HttpURLConnection connection = null;
		try {
			connection = getConnection();
            response.code = connection.getResponseCode();
			if (mDebug) FLog.i("===responseCode:" + response.code);
			if (response.code == HttpURLConnection.HTTP_OK) {
				String responseText = IoUtils.readStream(connection.getInputStream());
				if (mDebug) FLog.i("===responseText:" + responseText);
                response.text = responseText;
			} else {
                response.text = connection.getResponseMessage();
            }
		} catch (IOException e) {
            if (mDebug) FLog.e(e);
            response.code = HttpResponse.STATUS_CODE_UNKNOWN;
            response.text = "未知错误";
            return response;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
        return response;
	}
	
	public String uploadFile(Map<String, File> fileParams) {
		HttpURLConnection connection = null;
		try {
			connection = getConnection();
			connection.setDoOutput(true);
			
			DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
			
			addStringParams(dos, mRequestParams.getArrayMap());
			addFileParams(dos, fileParams);
			addEndParams(dos);
            
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				return IoUtils.readStream(connection.getInputStream());
			}
		} catch (Exception e) {
			if (mDebug) FLog.e(e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return null;
	}

    protected HttpURLConnection getConnection() throws IOException {
        HttpURLConnection connection = (HttpURLConnection)new URL(mUrlString).openConnection();
        connection.setConnectTimeout(mConnectTimeout);
        connection.setReadTimeout(mReadTimeout);
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setRequestProperty(HEADER_CHARSET, EncodeUtils.getDefultCharset());
        connection.setRequestProperty(HEADER_CONNECTION, "keep-alive");
        connection.setRequestMethod(mMethod.name());
        addPostBodyData(connection);
        return connection;
    }
	
    private void addPostBodyData(@NonNull URLConnection connection)
            throws IOException {
        if (mMethod == Method.POST) {
            String body = parseMapToUrlParamsString(mRequestParams.getArrayMap());
            connection.setDoOutput(true);
            connection.addRequestProperty(HEADER_CONTENT_TYPE, BODY_CONTENT_TYPE);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(body.getBytes());
            out.close();
        }
    }
    
    private void addStringParams(DataOutputStream dos, Map<String, Object> params) throws IOException {
        for (Map.Entry<String, Object> entry : params.entrySet()) {
        	dos.writeBytes(PREFIX + BOUNDARY + LINE_END);
            dos.writeBytes("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINE_END);
            dos.writeBytes(LINE_END);
            dos.writeBytes(EncodeUtils.encode(String.valueOf(entry.getValue())) + "\r\n");
        }
    }
    
    private void addFileParams(DataOutputStream dos, Map<String, File> fileParamsMap) throws Exception {
    	for(Map.Entry<String, File> entry : fileParamsMap.entrySet()) {
    		if (entry.getValue() == null || !entry.getValue().exists()) continue;
    		
			dos.writeBytes(PREFIX + BOUNDARY + LINE_END);
    		dos.writeBytes("Content-Disposition: form-data; name=\"" + 
    							entry.getKey() + "\"; filename=\"" + 
    							EncodeUtils.encode(entry.getValue().getName()) + "\"" + LINE_END);
    		dos.writeBytes("Content-Type: application/octet-stream" + LINE_END);
    		dos.writeBytes(LINE_END);
    		dos.write(FileUtils.getBytes(entry.getValue()));
    		dos.writeBytes(LINE_END);
    		
    	}
    }
    
    /**
     * 添加Http尾部
     * @throws IOException
     */
    private void addEndParams(DataOutputStream dos) throws IOException {
    	dos.writeBytes(PREFIX + BOUNDARY + PREFIX + LINE_END);  
    	dos.writeBytes(LINE_END);
    }

    public String parseMapToUrlParamsString(ArrayMap<String, Object> paramsMap) {
        if (paramsMap == null || paramsMap.size() == 0) return "";

        StringBuilder sb = new StringBuilder();
        int size = paramsMap.size();
        for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
            size--;
            if (TextUtils.isEmpty(entry.getKey())) continue;
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue() == null ? "" : EncodeUtils.encode(String.valueOf(entry.getValue())));
            sb.append(size == 0 ? "" : "&");
        }
        return sb.toString();
    }
}
