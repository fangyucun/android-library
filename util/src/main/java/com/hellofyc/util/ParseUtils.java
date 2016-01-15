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
 */package com.hellofyc.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.TypedValue;
import android.webkit.URLUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * 转换工具集
 *
 * Create on 2014年11月23日 上午11:22:25
 *
 * @author Yucun Fang
 */
public class ParseUtils {
	static final boolean DEBUG = false;
	
	public static int dbmToRssi(int dbm) {
		return -113 + dbm;
	}
	
    public static int dpToPx(Context context, int dp) {
        return Math.round(context.getResources().getDisplayMetrics().density * (float)dp);
    }
    
    public static int spToPx(Context context, int sp) {
    	Resources res = context.getResources();
    	return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
    			sp, res.getDisplayMetrics());
    }
    
    public static int pxToSp(Context context, float pxValue) { 
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (pxValue / fontScale + 0.5f);
    }
    
    public static int pxToDp(Context context, int px) {
    	Resources res = context.getResources();
    	float density = res.getDisplayMetrics().density;
    	return (int)(px / density + 0.5f);
    }
    
    public static String bytesToHexString(@NonNull byte[] bytes) {
    	StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
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
    
    public static byte[] hexStringToBytes(@NonNull String intString) {
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
        	if (DEBUG) FLog.e(e);
        }
        return null;
    }
    
    public static byte[] htmlToBytes(String url) {
    	if (!URLUtil.isNetworkUrl(url)) return null;
    	
    	URL htmlUrl;
    	InputStream inputStream;
    	try {
    		htmlUrl = new URL(url);
    		HttpURLConnection urlConnection = (HttpURLConnection)htmlUrl.openConnection();
    		int responseCode = urlConnection.getResponseCode();
    		if (responseCode == HttpURLConnection.HTTP_OK) {
    			inputStream = urlConnection.getInputStream();
    			return inputStreamToBytes(inputStream);
    		} else {
    			if (DEBUG) FLog.i("===Resonpse Code===" + responseCode);
    		}
		} catch (IOException e) {
			if (DEBUG) FLog.e(e);
		}
    	return null;
    }
    
    public static byte[] inputStreamToBytes(InputStream is) {
    	if (is == null) return null;
    	
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	try {
    		int oneByte;
			while ((oneByte = is.read()) != -1) {
				baos.write(oneByte);
			}
		} catch (IOException e) {
			if (DEBUG) FLog.e(e);
		} finally {
			IoUtils.close(baos);
		}
    	return baos.toByteArray();
    }
    
    public static byte[] bitmapToBytes(Bitmap bitmap, boolean recycle) {
    	if (bitmap == null) {
    		FLog.e("args bitmap is null");
    		return null;
    	}
    	
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	bitmap.compress(CompressFormat.PNG, 100, baos);
		IoUtils.close(baos);
		if (recycle) {
			bitmap.recycle();
		}
		return baos.toByteArray();
    }
    
	public static int stringToInt(String intString) {
		return stringToInt(intString, 0);
	}
	
	public static int stringToInt(String intString, int defValue) {
		int result;
		try {
			result = Integer.parseInt(intString);
		} catch(NumberFormatException e) {
			result = defValue;
		}
		return result;
	}

    public static Long[] transformLongArray(long[] source) {
        Long[] destin = new Long[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    public static long[] longArrayToLongArray(Long[] source) {
        long[] destin = new long[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    public static Integer[] intArrayToIntegerArray(int[] source) {
        Integer[] destin = new Integer[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    public static int[] integerArrayToIntArray(Integer[] source) {
        int[] destin = new int[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

	public static String doubleToString(double number, int precision) {
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(precision);
		return format.format(number);
	}
    
	/**
	 * Map To String
	 */
//	public static String mapToUrlParamsString(Map<String, String> paramsMap) {
//		if (paramsMap == null || paramsMap.isEmpty()) return "";
//
//		StringBuilder sb = new StringBuilder();
//		int size = paramsMap.size();
//		for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
//			size--;
//			if (TextUtils.isEmpty(entry.getKey())) continue;
//			sb.append(entry.getKey());
//			sb.append("=");
//			sb.append(entry.getValue() == null ? "" : EncodeUtils.encode(String.valueOf(entry.getValue())));
//			sb.append(size == 0 ? "" : "&");
//		}
//		return sb.toString();
//	}
	
	public static String mapToString(@NonNull Map<String, String> paramsMap) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
			sb.append(entry.getKey() == null ? "" : entry.getKey());
			sb.append(entry.getValue() == null ? "" : EncodeUtils.encode(entry.getValue()));
		}
		return sb.toString();
		
	}
	
	/**
	 * 参数排序(大写优于小写)
	 */
	
	public static String mapKeySortedToString(@NonNull ArrayMap<String, String> paramsMap) {
		StringBuilder builder = new StringBuilder();
		paramsMap = CollectionUtils.sortedMapByKey(paramsMap);
		
		for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
			if (!TextUtils.isEmpty(entry.getKey())) {
				builder.append(entry.getKey());
				builder.append("=");
				builder.append(TextUtils.isEmpty(entry.getValue()) ? "" : entry.getValue());
			}
		}
		return builder.toString();
	}
	
	public static String mapToUrlParamsStringSortByKeyAlphabet(Map<String, String> paramsMap) {
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

	@SuppressWarnings("unchecked")
    public static <V> int compare(V v1, V v2) {
        return v1 == null ? (v2 == null ? 0 : -1) : (v2 == null ? 1 : ((Comparable)v1).compareTo(v2));
    }
    
}
