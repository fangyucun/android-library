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

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hellofyc.base.util.EncodeUtils;
import com.hellofyc.base.util.FLog;
import com.hellofyc.base.util.FileUtils;
import com.hellofyc.base.util.IoUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class HttpUtils {
	static final boolean DEBUG = false;
	
	private static final String HEADER_CONTENT_TYPE = "Content-Type";
	private static final String HEADER_CHARSET = "Charset";
	private static final String HEADER_CONNECTION = "connection";
	
	private static final String PREFIX = "--";
	private static final String LINE_END = "\r\n";
	
	private static final int CONNECT_TIME_OUT = 30 * 1000;
	private static final int READ_TIME_OUT = 30 * 1000;
	
	private static String BOUNDARY = UUID.randomUUID().toString();
	
	public static HttpResponse doGet(String urlString) {
		return doRequest(Method.GET, urlString, null);
	}
	
	public static HttpResponse doPost(String urlString, Map<String, String> params) {
		return doRequest(Method.POST, urlString, params);
	}
	
	public static HttpResponse doRequest(int method, String urlString, Map<String, String> params) {
        HttpResponse response = new HttpResponse();
		HttpURLConnection connection = null;
		try {
			connection = openConnection(new URL(urlString));
			setConnectionParametersForRequest(method, connection, params);
            response.code = connection.getResponseCode();
			if (DEBUG) FLog.i("===responseCode:" + response.code);
			if (response.code == HttpURLConnection.HTTP_OK) {
				String responseText = readStream(connection.getInputStream());
				if (DEBUG) FLog.i("===responseText:" + responseText);
                response.text = responseText;
			} else {
                response.text = connection.getResponseMessage();
            }
		} catch (IOException e) {
            if (DEBUG) FLog.e(e);
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
	
	public static String uploadFile(String urlString, Map<String, String> stringParams, Map<String, File> fileParams) {
		HttpURLConnection connection = null;
		try {
			connection = openConnection(new URL(urlString));
			connection.setDoOutput(true);
			
			DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
			
			addStringParams(dos, stringParams);
			addFileParams(dos, fileParams);
			addEndParams(dos);
            
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				return readStream(connection.getInputStream());
			}
		} catch (Exception e) {
			if (DEBUG) FLog.e(e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return null;
	}

	public static String readStream(InputStream inputStream)
			throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} finally {
			IoUtils.close(br);
		}
		return sb.toString();
	}
	
	private static void setConnectionParametersForRequest(int method, HttpURLConnection connection, Map<String, String> paramsMap) throws IOException {
		switch (method) {
		case Method.GET:
			connection.setRequestMethod("GET");
			break;
		case Method.POST:
			connection.setRequestMethod("POST");
			addBodyIfExists(connection, paramsMap);
			break;
		default:
            throw new IllegalStateException("Unknown method type.");
		}
	}
	
	private static HttpURLConnection openConnection(URL url) throws IOException {
		HttpURLConnection connection = createConnection(url);
		connection.setConnectTimeout(CONNECT_TIME_OUT);
        connection.setReadTimeout(READ_TIME_OUT);
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setRequestProperty(HEADER_CHARSET, EncodeUtils.getDefultCharset());
        connection.setRequestProperty(HEADER_CONNECTION, "keep-alive");
        return connection;
	}
	
	private static HttpURLConnection createConnection(URL url) throws IOException {
		return (HttpURLConnection) url.openConnection();
	}
	
    private static void addBodyIfExists(@NonNull HttpURLConnection connection, Map<String, String> params)
            throws IOException {
        String body = parseMapToUrlParamsString(params);
        connection.setDoOutput(true);
        connection.addRequestProperty(HEADER_CONTENT_TYPE, getBodyContentType());
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.write(body.getBytes());
        out.close();
    }
    
    private static String getBodyContentType() {
        return "application/x-www-form-urlencoded";
    }
    
    private static void addStringParams(DataOutputStream dos, Map<String, String> stringParamsMap) throws IOException {
        for (Map.Entry<String, String> entry : stringParamsMap.entrySet()) {
        	dos.writeBytes(PREFIX + BOUNDARY + LINE_END);
            dos.writeBytes("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINE_END);
            dos.writeBytes(LINE_END);
            dos.writeBytes(EncodeUtils.encode(entry.getValue()) + "\r\n");
        }
    }
    
    private static void addFileParams(DataOutputStream dos, Map<String, File> fileParamsMap) throws Exception {
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
    private static void addEndParams(DataOutputStream dos) throws IOException {
    	dos.writeBytes(PREFIX + BOUNDARY + PREFIX + LINE_END);  
    	dos.writeBytes(LINE_END);
    }

    public static String parseMapToUrlParamsString(Map<String, String> paramsMap) {
        if (paramsMap == null || paramsMap.size() == 0) return "";

        StringBuilder sb = new StringBuilder();
        int size = paramsMap.size();
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            size--;
            if (TextUtils.isEmpty(entry.getKey())) continue;
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue() == null ? "" : EncodeUtils.encode(String.valueOf(entry.getValue())));
            sb.append(size == 0 ? "" : "&");
        }
        return sb.toString();
    }
    
    /**
     * @param context if null, use the default format
     *                (Mozilla/5.0 (Linux; U; Android %s) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 %sSafari/534.30).
     */
    public static String getUserAgent(Context context) {
        String webUserAgent = null;
        if (context != null) {
            try {
                Class<?> sysResCls = Class.forName("com.android.internal.R$string");
                Field webUserAgentField = sysResCls.getDeclaredField("web_user_agent");
                Integer resId = (Integer) webUserAgentField.get(null);
                webUserAgent = context.getString(resId);
            } catch (Throwable ignored) {
            }
        }
        
        if (TextUtils.isEmpty(webUserAgent)) {
            webUserAgent = "Mozilla/5.0 (Linux; U; Android %s) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 %sSafari/533.1";
        }

        Locale locale = Locale.getDefault();
        StringBuffer buffer = new StringBuffer();
        // Add version
        final String version = Build.VERSION.RELEASE;
        if (version.length() > 0) {
            buffer.append(version);
        } else {
            // default to "1.0"
            buffer.append("1.0");
        }
        buffer.append("; ");
        final String language = locale.getLanguage();
        if (language != null) {
            buffer.append(language.toLowerCase(Locale.CHINA));
            final String country = locale.getCountry();
            if (country != null) {
                buffer.append("-");
                buffer.append(country.toLowerCase(Locale.CHINA));
            }
        } else {
            // default to "en"
            buffer.append("en");
        }
        // add the model for the release build
        if ("REL".equals(Build.VERSION.CODENAME)) {
            final String model = Build.MODEL;
            if (model.length() > 0) {
                buffer.append("; ");
                buffer.append(model);
            }
        }
        final String id = Build.ID;
        if (id.length() > 0) {
            buffer.append(" Build/");
            buffer.append(id);
        }
        return String.format(webUserAgent, buffer, "Mobile ");
    }
    
	private HttpUtils(){}
}
