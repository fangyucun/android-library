/*
 *
 *  * Copyright (C) 2015 Jason Fang ( ifangyucun@gmail.com )
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */package com.hellofyc.applib.net.http;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.hellofyc.applib.security.DESUtils;
import com.hellofyc.applib.security.MD5Utils;
import com.hellofyc.applib.security.RSAUtils;
import com.hellofyc.applib.util.DeviceUtils;
import com.hellofyc.applib.util.FLog;
import com.hellofyc.applib.util.PackageUtils;
import com.hellofyc.applib.util.ParseUtils;
import com.hellofyc.applib.util.RandomUtils;
import com.hellofyc.applib.util.TimeUtils;

/**
 * Create on 2015年6月2日 下午4:29:55
 * @author Yucun Fang
 */
public class HttpHelper {
    static final boolean DEBUG = false;
    static final boolean ENCRYPT_DEBUG = false;

	private static final int IV_START_POSITION = 6;
	private static final int KEY_START_POSITION = 65;
	
	private static HttpHelper sInstance;
	private Context mContext;
	
	private HttpHelper(Context context) {
		mContext = context;
	}
	
	public static HttpHelper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new HttpHelper(context);
		}
		return sInstance;
	}
	
	public String doPost(String urlString, ArrayMap<String, String> params) {
		if (params == null) {
			params = new ArrayMap<>();
		}
		
		String randomString = RandomUtils.getRandomNumbersAndLetters(117);
		if (DEBUG && ENCRYPT_DEBUG) FLog.i("random string", randomString);
		String keyString = randomString.substring(KEY_START_POSITION, KEY_START_POSITION + 24);
		String ivString = keyString.substring(IV_START_POSITION, IV_START_POSITION + 8);
		
		if (DEBUG && ENCRYPT_DEBUG) FLog.i("key string", keyString);
		if (DEBUG && ENCRYPT_DEBUG) FLog.i("iv string", ivString);
		
		long timestamp = TimeUtils.getCurrentTimeMillis();
		params.put("app_version", PackageUtils.getVersionCode(mContext) + "");
		params.put("app_uniqueid", DeviceUtils.getDeviceUniqueId(mContext));
		params.put("t", timestamp + "");
		params.put("token", getToken(params));
		
		if (DEBUG) FLog.i("request url", urlString);
		if (DEBUG) FLog.i("request params", params.toString());
		
		ArrayMap<String, String> finalParamsMap = getFinalParams(params, randomString, keyString, ivString);
		
		if (DEBUG && ENCRYPT_DEBUG) FLog.i("request encrypt params", finalParamsMap.toString());

        HttpResponse response = HttpUtils.doPost(urlString, finalParamsMap);
		
		if (DEBUG && ENCRYPT_DEBUG) FLog.i("response encrypt text", response.text);
		
		String responseText = DESUtils.decryptWithDESede(response.text, keyString, ivString);
		
		if (DEBUG) FLog.i("response text", responseText);
		
		return responseText;
	}
	
	private ArrayMap<String, String> getFinalParams(ArrayMap<String, String> params, String randomString, String keyString, String ivString) {
		String mapString = HttpUtils.parseMapToUrlParamsString(params);
		String desString = DESUtils.encryptWithDESede(mapString, keyString, ivString);
		ArrayMap<String, String> finalParamsMap = new ArrayMap<>();
		finalParamsMap.put("p", desString);
		finalParamsMap.put("k", RSAUtils.encrypt(randomString, RSAUtils.getPublicKey(mContext, "quc_rsa_public_key.pem")));
		return finalParamsMap;
	}
	
	private String getToken(ArrayMap<String, String> paramsMap) {
		return MD5Utils.encode(ParseUtils.mapKeySortedToString(paramsMap));
	}
	
//	private String getToken(String timestamp) {
//		String tail = timestamp.substring(timestamp.length() - 3, timestamp.length());
//		String tailMd5 = SecurityUtils.MD5(tail);
//		String bobaoMd5 = SecurityUtils.MD5("bobao_".concat(getTimeStampString(timestamp)));
//		return SecurityUtils.MD5(bobaoMd5.concat(tailMd5));
//	}
	
//	private String getTimeStampString(String timestamp) {
//		return timestamp.subSequence(0, timestamp.length() - 3) + "";
//	}
}
