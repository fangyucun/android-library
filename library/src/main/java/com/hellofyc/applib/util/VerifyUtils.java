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

package com.hellofyc.applib.util;

import android.text.TextUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Verify
 *
 * Create on 2014年12月6日 下午12:34:23
 * @author Jason Fang
 */
public class VerifyUtils {
	static final boolean DEBUG = false;

	public static boolean isEmail(String text) {

		if (TextUtils.isEmpty(text)) return false;
		
		Pattern p = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher m = p.matcher(text);
		return m.matches();
	}
	
	public static boolean isPhoneNumber(String text) {

		if (TextUtils.isEmpty(text)) return false;
		
		Pattern p = Pattern.compile("([+]?[0-9]{2,})?1[3-8]\\d{9}");
		Matcher m = p.matcher(text);
		return m.matches();
	}
	
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		return (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS);
	}
	
	public static boolean isEmpty(Map<?, ?> paramsMap){
		return (paramsMap == null || paramsMap.size() == 0);
	}
	
	private VerifyUtils(){/*Do not new me!*/}
}
