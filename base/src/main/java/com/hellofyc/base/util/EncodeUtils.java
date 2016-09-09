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

package com.hellofyc.base.util;

import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * Encode Tool
 *
 * Create On 2014年12月6日 下午12:23:22
 *
 * @author Jason Fang
 */
public final class EncodeUtils {

    public static String encode(String text) {
    	if (TextUtils.isEmpty(text)) return "";
    	
		try {
			return URLEncoder.encode(text, Charset.defaultCharset().name());
		} catch (UnsupportedEncodingException e) {
			FLog.e(e);
		}

		return "";
    }

	public static String getTextCodeFormat(String filePath) {
		String code;
		BufferedInputStream bis = null;
		int p = 0;
		try {
			bis = new BufferedInputStream(new FileInputStream(filePath));
			p = (bis.read() << 8) + bis.read();
		} catch (Exception e) {
			FLog.e(e);
		} finally {
			IoUtils.close(bis);
		}
		switch (p) {
		case 0xefbb:
			code = "UTF-8";
			break;
		case 0xfffe:
			code = "Unicode";
			break;
		case 0xfeff:
			code = "UTF-16BE";
			break;
		default:
			code = "GBK";
		}
		return code;
	}

	private EncodeUtils() {/*Do not new me!*/}
}
