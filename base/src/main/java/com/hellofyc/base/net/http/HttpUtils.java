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

import android.graphics.Bitmap;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.hellofyc.base.util.EncodeUtils;
import com.hellofyc.base.util.FLog;
import com.hellofyc.base.util.IoUtils;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.UUID;

public class HttpUtils {

    private static final int TYPE_TEXT       = 1;
    private static final int TYPE_BITMAP     = 2;
    private static final int TYPE_FILE       = 3;

    private static final String BOUNDARY = UUID.randomUUID().toString();

    private static final String CONTENT_TYPE_TEXT     = "application/x-www-form-urlencoded";
	private static final String CONTENT_TYPE_FILE     = "multipart/form-data; boundary=" + BOUNDARY;
    private static final String PREFIX = "--";

	private static final String LINE_END = "\r\n";

    private boolean mDebug = false;
    private boolean mStackTrace = false;
    private HttpRequest mRequestParams = new HttpRequest();
    private Method mMethod = Method.POST;
    private String mUrlString;
    private int mConnectTimeout = 30 * 1000;
    private int mReadTimeout = 30 * 1000;
    private Bitmap mBitmap;
    private ArrayMap<String, File> mFileMap;
    private int mType = TYPE_TEXT;
    private String mUserAgent = "Android";

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

    public HttpUtils setStackTraceEnable() {
        mStackTrace = true;
        return this;
    }

    public HttpUtils setBitmap(@NonNull Bitmap bitmap) {
        mType = TYPE_BITMAP;
        mBitmap = bitmap;
        return this;
    }

    public HttpUtils setFiles(@NonNull Map<String, File> fileMap) {
        mType = TYPE_FILE;
        if (mFileMap == null) {
            mFileMap = new ArrayMap<>();
        }
        mFileMap.putAll(fileMap);
        return this;
    }

    public HttpUtils setUserAgent(@NonNull String userAgent) {
        mUserAgent = userAgent;
        return this;
    }

	public HttpResponse request(){
        if (mDebug) {
            FLog.i("REQUEST URL:" + mUrlString);
            FLog.i("REQUEST PARAMS:" + mRequestParams.getArrayMap().toString());
            FLog.i("REQUEST STRING:" + mRequestParams.getString());
        }

        if (mStackTrace) {
            getInvokeStackTraceElement();
        }

        HttpResponse response = new HttpResponse();
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) new URL(mUrlString).openConnection();
            configConnection(connection);
            response.code = connection.getResponseCode();
			if (mDebug) FLog.i("===responseCode:" + response.code);
			if (response.code == HttpURLConnection.HTTP_OK) {
				String responseText = IoUtils.readStream(connection.getInputStream());
                String cookie = connection.getHeaderField("Set-Cookie");
				if (mDebug) FLog.i("===responseText:" + responseText);
                if (mDebug) FLog.i("===cookie:" + cookie);
                response.cookies = HttpCookie.parse(cookie);
                response.text = responseText;
			} else {
                response.text = connection.getResponseMessage();
            }
		} catch (UnknownHostException e) {
            if (mDebug) FLog.e(e);
            response.code = HttpResponse.STATUS_CODE_NET;
            response.text = "网络错误";
            return response;
        } catch (IOException e) {
            if (mDebug) FLog.e(e);
            response.code = HttpResponse.STATUS_CODE_UNKNOWN;
            response.text = "UNKNOWN";
            return response;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
        return response;
	}

    private static byte[] bitmapToBytes(@NonNull Bitmap bitmap) {
        byte[] bytes = new byte[bitmap.getWidth() * bitmap.getHeight()];
        for (int i=0; i<bitmap.getWidth(); i++) {
            for (int j=0; j<bitmap.getHeight(); j++) {
                bytes[i+j] = (byte)(bitmap.getPixel(i, j) & 0x80 >> 7);
            }
        }
        return bytes;
    }

    protected void configConnection(HttpURLConnection connection) throws IOException {
        connection.setConnectTimeout(mConnectTimeout);
        connection.setReadTimeout(mReadTimeout);
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setRequestProperty("Charset", Charset.defaultCharset().name());
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("User-Agent", mUserAgent);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestProperty("Cookie", CookieHelper.parse(mRequestParams.getCookies()));

        switch (mType) {
            case TYPE_TEXT: {
                connection.setRequestMethod(mMethod.name());
                if (mMethod == Method.POST) {
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", CONTENT_TYPE_TEXT);
                    String paramsString;
                    if (!TextUtils.isEmpty(mRequestParams.getString())) {
                        paramsString = mRequestParams.getString();
                    } else {
                        paramsString = parseMapToUrlParamsString(mRequestParams.getArrayMap());
                    }
                    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                    outputStream.write(paramsString.getBytes());
                    outputStream.flush();
                    outputStream.close();
                }
                break;
            }
            case TYPE_BITMAP: {
                connection.setRequestMethod(Method.POST.name());
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", CONTENT_TYPE_FILE);
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

                StringBuilder builder = new StringBuilder();
                for (Map.Entry<String, Object> entry : mRequestParams.getArrayMap().entrySet()) {
                    builder.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    builder.append("Content-Disposition: form-data; name=\"").append(entry.getKey()).append("\"").append(LINE_END);
                    builder.append("Content-Type: text/plain; charset=\"utf-8\"").append(LINE_END);
                    builder.append("Content-Transfer-Encoding: 8bit").append(LINE_END);
                    builder.append(LINE_END);
                    builder.append(entry.getValue());
                    builder.append(LINE_END);
                }
                outputStream.write(builder.toString().getBytes());

                outputStream.writeBytes(PREFIX + BOUNDARY + LINE_END);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + "bitmap" + "\";filename=\"" +
                        "bitmap.jpg" + "\"" + LINE_END);
                outputStream.writeBytes(LINE_END);
                outputStream.write(bitmapToBytes(mBitmap));
                outputStream.writeBytes(LINE_END);
                outputStream.writeBytes(PREFIX + BOUNDARY + PREFIX + LINE_END);
                outputStream.flush();
                outputStream.close();
                break;
            }
            case TYPE_FILE: {
                connection.setRequestMethod(Method.POST.name());
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", CONTENT_TYPE_FILE);

                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

                StringBuilder builder = new StringBuilder();
                for (Map.Entry<String, Object> entry : mRequestParams.getArrayMap().entrySet()) {
                    builder.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    builder.append("Content-Disposition: form-data; name=\"").append(entry.getKey()).append("\"").append(LINE_END);
                    builder.append("Content-Type: text/plain; charset=\"utf-8\"").append(LINE_END);
                    builder.append("Content-Transfer-Encoding: 8bit").append(LINE_END);
                    builder.append(LINE_END);
                    builder.append(entry.getValue());
                    builder.append(LINE_END);
                }

                for (ArrayMap.Entry<String, File> entry : mFileMap.entrySet()) {
                    String text = PREFIX + BOUNDARY + LINE_END +
                            "Content-Disposition: form-data; name=\"" + entry.getKey() + "\"; filename=\"" + entry.getValue().getName() + "\"" + LINE_END +
                            "Content-Type:" + "application/octet-stream" + LINE_END +
                            "Content-Transfer-Encoding: binary" + LINE_END + LINE_END;
                    outputStream.writeBytes(builder.append(text).toString());

                    BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(entry.getValue()));
                    int length;
                    byte[] bytes = new byte[1024 * 1024];
                    while ((length = inputStream.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, length);
                    }
                    inputStream.close();
                }

                String endTag = LINE_END + PREFIX + BOUNDARY + PREFIX + LINE_END;
                outputStream.writeBytes(endTag);
                outputStream.flush();
                outputStream.close();
                break;
            }
        }
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

    private static void getInvokeStackTraceElement() {
        FLog.i("Thread ID: " + Thread.currentThread().getId() + " getInvokeStackTraceElement");
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTraceElements) {
            FLog.i("(" + element.getLineNumber() + ") " + "class name:" + element.getClassName() + ", method name:" + element.getMethodName());
        }
    }
}
