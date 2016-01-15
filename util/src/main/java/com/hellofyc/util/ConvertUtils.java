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

package com.hellofyc.util;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * 转换工具集
 * @author Jason Fang
 * Create on 2014年11月23日 上午11:22:25
 */
public class ConvertUtils {
	static final boolean DEBUG = false;
	
	public static int parseDbmToRssi(int dbm) {
		return -113 + dbm;
	}
	
    public static int parseDpToPx(Context context, int dp) {
        Resources res = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, res.getDisplayMetrics());
    }
    
    public static int parseSpToPx(Context context, int dp) {
    	Resources res = context.getResources();
    	return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
    			dp, res.getDisplayMetrics());
    }
    
    public static int parsePxToDp(Context context, int px) {
    	Resources res = context.getResources();
    	float density = res.getDisplayMetrics().density;
    	return (int)(px / density + 0.5f);
    }

    public static String parseBytesToHexString(byte[] bytes) {
    	if (bytes == null) return "";
    	StringBuilder sb = new StringBuilder();
        for(byte b : bytes) {
            String x16 = Integer.toHexString(b);
            if (x16.length() < 2) {
                sb.append("0");
                sb.append(x16);
            } else if (x16.length() > 2) {
                sb.append(x16.substring(x16.length() - 2));
            } else {
                sb.append(x16);
            }
        }
        return sb.toString();
    }
    
    public static byte[] parseHexStringToBytes(String intString) {
    	if (TextUtils.isEmpty(intString)) return null;
    	
    	if (intString.length() % 2 == 1) {
    		intString = "0" + intString;
    	}
        byte[] bytes = new byte[intString.length() / 2];
        
        try {
	        for (int i = 0; i < bytes.length; i++) {  
	        	bytes[i] = (byte)Integer.parseInt(intString.substring(i * 2, i * 2 + 2), 16);  
	        }
	        return bytes;
        } catch (Exception e) {
        	FLog.e(e);
        	return null;
        }
    }
    
    public static byte[] parseStringToBytes(String string) {
    	if (TextUtils.isEmpty(string)) return null;
    	
    	try {
			return string.getBytes(EncodeUtils.getDefultCharset());
		} catch (UnsupportedEncodingException e) {
			FLog.e(e);
			return null;
		}
    }
    
    public static String parseBytesToString(byte[] bytes) {
    	return new String(bytes);
    }

	public static int parseInt(String intString) {
		return parseInt(intString, 0);
	}
	
	public static int parseInt(String intString, int defValue) {
		int result;
		
		try {
			result = Integer.parseInt(intString);
		} catch(NumberFormatException e) {
			result = defValue;
		}
		return result;
	}

    public static boolean isEquals(@Nullable Object actual, @Nullable Object expected) {
        return (actual == null && expected == null) || actual == expected || (actual != null && actual.equals(expected));
    }

    public static Long[] transformLongArray(long[] source) {
        Long[] destin = new Long[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    public static long[] parseToLongArray(Long[] source) {
        long[] destin = new long[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    public static Integer[] parseToIntArray(int[] source) {
        Integer[] destin = new Integer[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    public static int[] parseToIntArray(Integer[] source) {
        int[] destin = new int[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

	public static String parseDoubleToString(double number, int precision) {
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(precision);
		return format.format(number);
	}
    
	public static String parseMapToUrlParamsString(Map<String, String> paramsMap) {
		if (CollectionUtils.isEmpty(paramsMap)) return "";
		
		StringBuilder sb = new StringBuilder();
		int size = paramsMap.size();
		for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
			size--;
			if (TextUtils.isEmpty(entry.getKey())) continue;
			sb.append(entry.getKey());
			sb.append("=");
			sb.append(entry.getValue() == null ? "" : EncodeUtils.encode(entry.getValue()));
			sb.append(size == 0 ? "" : "&");
		}
		return sb.toString();
	}
	
	public static String parseMapToString(Map<String, String> paramsMap) {
		if (CollectionUtils.isEmpty(paramsMap)) return "";
		
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
			sb.append(entry.getKey() == null ? "" : entry.getKey());
			sb.append(entry.getValue() == null ? "" : EncodeUtils.encode(entry.getValue()));
		}
		return sb.toString();
		
	}
	
	public static String parseMapToUrlParamsStringSortByKeyAlphabet(Map<String, String> paramsMap) {
		if (CollectionUtils.isEmpty(paramsMap)) return "";
		
		Set<String> keySet = paramsMap.keySet();
		String[] keys = new String[keySet.size()];
		keys = keySet.toArray(keys);
		Arrays.sort(keys);
		StringBuilder sb = new StringBuilder();
		int size = paramsMap.size();
		for (String key : keys) {
			size--;
			if (TextUtils.isEmpty(key)) continue;
			sb.append(key);
			sb.append("=");
			sb.append(paramsMap.get(key) == null ? "" : EncodeUtils.encode(paramsMap.get(key)));
			sb.append(size == 0 ? "" : "&");
		}
		return sb.toString();
	}

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static <V> int compare(V v1, V v2) {
        return v1 == null ? (v2 == null ? 0 : -1) : (v2 == null ? 1 : ((Comparable)v1).compareTo(v2));
    }
    
}
