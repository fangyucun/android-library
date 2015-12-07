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

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.hellofyc.applib.security.DESUtils;
import com.hellofyc.applib.security.MD5Utils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * SharePreferences Tool
 *
 * Create on 2014年12月5日 下午5:28:22
 *
 * @author Jason Fang
 */
public final class PrefsUtils {
	static final boolean DEBUG = true;
	
	private static final String SECURITY_KEY = "iJasonfang";
	
	/**
	 * Put Value
	 */
	public static boolean putValue(Context context, String key, Object value) {
		return putValue(context, null, key, value);
	}
	
	public static boolean putValue(Context context, String key, Object value, boolean isKeyEncrypt, boolean isValueEncrypt) {
		return putValue(context, null, key, value, isKeyEncrypt, isValueEncrypt);
	}
	
	public static boolean putValue(Context context, String name, String key, Object value) {
		return putValue(context, name, key, value, true, true);
	}
	
	public static boolean putValue(Context context, String name, String key, Object value, boolean isKeyEncrypt, boolean isValueEncrypt) {
		return putValue(context, name, key, value, Context.MODE_PRIVATE, isKeyEncrypt, isValueEncrypt);
	}
	
	public static boolean putValue(Context context, String name, String key, Object value, int mode, boolean isKeyEncrypt, boolean isValueEncrypt) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put(key, value);
		return putValue(context, name, map, mode, isKeyEncrypt, isValueEncrypt);
	}

    public static boolean putValue(Context context, Map<String, Object> mapValue) {
        return putValue(context, null, mapValue);
    }
	
	public static boolean putValue(Context context, String name, Map<String, Object> mapValue) {
		return putValue(context, name, mapValue, true, true);
	}

    public static boolean putValue(Context context, String name, Map<String, Object> mapValue, boolean isKeyEncrypt, boolean isValueEncrypt) {
        return putValue(context, name, mapValue, Context.MODE_PRIVATE, isKeyEncrypt, isValueEncrypt);
    }

	@SuppressWarnings("unchecked")
	public static boolean putValue(Context context, String name, Map<String, Object> map, int mode, boolean isKeyEncrpyt, boolean isValueEncrypt) {
		SharedPreferences preferences;
		if (TextUtils.isEmpty(name)) {
			preferences = PreferenceManager.getDefaultSharedPreferences(context);
		} else {
			preferences = context.getSharedPreferences(name, mode);
		}
		
		Editor editor = preferences.edit();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = isKeyEncrpyt ? MD5Utils.encode(entry.getKey()) : entry.getKey();
			Object value = entry.getValue();
			
			if (isValueEncrypt && !(value instanceof Set)) {
				editor.putString(key, DESUtils.encryptWithDES(String.valueOf(value), SECURITY_KEY));
			} else {
				if (value instanceof Boolean) {
					editor.putBoolean(key, Boolean.parseBoolean(String.valueOf(value)));
				} else if (value instanceof Float) {
					editor.putFloat(key, Float.parseFloat(String.valueOf(value)));
				} else if (value instanceof Integer) {
					editor.putInt(key, Integer.parseInt(String.valueOf(value)));
				} else if (value instanceof Long) {
					editor.putLong(key, Long.parseLong(String.valueOf(value)));
				} else if (value instanceof String) {
					editor.putString(key, String.valueOf(value));
				} else if (value instanceof Set) {
					if (isValueEncrypt) {
						Set<String> sets = (Set<String>)value;
						Set<String> tempSets = new HashSet<>();
						for (String s : sets) {
							tempSets.add(DESUtils.encryptWithDES(String.valueOf(s), SECURITY_KEY));
						}
						editor.putStringSet(key, tempSets);
					} else {
						editor.putStringSet(key, (Set<String>)value);
					}
				} else {
					throw new IllegalArgumentException("Value type is not support!");
				}
			}
		}
		return editor.commit();
	}

	/**
	 * Remove Key
	 */
	public static boolean removeKey(Context context, String key) {
		return removeKey(context, null, key);
	}
	
	public static boolean removeKey(Context context, String name, String key) {
		return removeKey(context, name, key, false);
	}
	
	public static boolean removeKey(Context context, String name, String key, boolean isKeyEncrypt) {
		return removeKey(context, name, key, Context.MODE_PRIVATE, isKeyEncrypt);
	}
	
	public static boolean removeKey(Context context, String name, String key, int mode, boolean isKeyEncrypt) {
		SharedPreferences preferences;
		if (TextUtils.isEmpty(name)) {
			preferences = PreferenceManager.getDefaultSharedPreferences(context);
		} else {
			preferences = context.getSharedPreferences(name, mode);
		}
		
		Editor editor = preferences.edit();
		editor.remove(isKeyEncrypt ? DESUtils.encryptWithDES(key, SECURITY_KEY) : key);
		return editor.commit();
	}
	
	/**
	 * Get String
	 */
	public static String getString(Context context, String key, String defValue) {
		return getString(context, null, key, defValue);
	}
	
	public static String getString(Context context, String name, String key, String defValue) {
		return getString(context, name, key, defValue, Context.MODE_PRIVATE, true, true);
	}
	
	public static String getString(Context context, String name, String key, String defValue, boolean isKeyEncrypt, boolean isValueEncrypt) {
		return getString(context, name, key, defValue, Context.MODE_PRIVATE, isKeyEncrypt, isValueEncrypt);
	}
	
	public static String getString(Context context, String name, String key, String defValue, int mode, boolean isKeyEncrypt, boolean isValueEncrypt) {
		SharedPreferences preferences;
		if (TextUtils.isEmpty(name)) {
			preferences = PreferenceManager.getDefaultSharedPreferences(context);
		} else {
			preferences = context.getSharedPreferences(name, mode);
		}
		
		String value = preferences.getString(isKeyEncrypt ? MD5Utils.encode(key) : key, defValue);
		if (value.equals(defValue)) {
			return value;
		} else {
			return isValueEncrypt ? DESUtils.decryptWithDES(value, SECURITY_KEY) : value;
		}
	}
	
	/**
	 * Get Int
	 */
	public static int getInt(Context context, String key, int defValue) {
		return getInt(context, null, key, defValue);
	}
	
	public static int getInt(Context context, String name, String key, int defValue) {
		return getInt(context, name, key, defValue, true, true);
	}
	
	public static int getInt(Context context, String name, String key, int defValue, boolean isKeyEncrypt, boolean isValueEncrypt) {
		return getInt(context, name, key, defValue, Context.MODE_PRIVATE, isKeyEncrypt, isValueEncrypt);
	}

	public static int getInt(Context context, String name, String key, int defValue, int mode, boolean isKeyEncrypt, boolean isValueEncrypt) {
		SharedPreferences preferences;
		if (TextUtils.isEmpty(name)) {
			preferences = PreferenceManager.getDefaultSharedPreferences(context);
		} else {
			preferences = context.getSharedPreferences(name, mode);
		}

		if (isValueEncrypt) {
			String value = getString(context, name, key, String.valueOf(defValue), mode, isKeyEncrypt, true);
			try {
				return Integer.valueOf(value);
			} catch (Exception e) {
				return defValue;
			}
		} else {
			return preferences.getInt(isKeyEncrypt ? MD5Utils.encode(key) : key, defValue);
		}
	}
	
	/**
	 * Get Long
	 */
	public static long getLong(Context context, String key, long defValue) {
		return getLong(context, null, key, defValue);
	}
	
	public static long getLong(Context context, String name, String key, long defValue) {
		return getLong(context, name, key, defValue, true, true);
	}
	
	public static long getLong(Context context, String name, String key, long defValue, boolean isKeyEncrypt, boolean isValueEncrypt) {
		return getLong(context, name, key, defValue, Context.MODE_PRIVATE, isKeyEncrypt, isValueEncrypt);
	}
	
	public static long getLong(Context context, String name, String key, long defValue, int mode, boolean isKeyEncrypt, boolean isValueEncrypt) {
		SharedPreferences preferences;
		if (TextUtils.isEmpty(name)) {
			preferences = PreferenceManager.getDefaultSharedPreferences(context);
		} else {
			preferences = context.getSharedPreferences(name, mode);
		}

		if (isValueEncrypt) {
			String value = getString(context, name, key, String.valueOf(defValue), mode, isKeyEncrypt, true);
			try {
				return Long.valueOf(value);
			} catch (Exception e) {
				return defValue;
			}
		} else {
			return preferences.getLong(isKeyEncrypt ? MD5Utils.encode(key) : key, defValue);
		}
	}
	
	/**
	 * Get Float
	 */
	public static float getFloat(Context context, String key, float defValue) {
		return getFloat(context, null, key, defValue);
	}
	
	public static float getFloat(Context context, String name, String key, float defValue) {
		return getFloat(context, name, key, defValue, true, true);
	}
	
	public static float getFloat(Context context, String name, String key, float defValue, boolean isKeyEncrypt, boolean isValueEncrypt) {
		return getFloat(context, name, key, defValue, Context.MODE_PRIVATE, isKeyEncrypt, isValueEncrypt);
	}
	
	public static float getFloat(Context context, String name, String key, float defValue, int mode, boolean isKeyEncrypt, boolean isValueEncrypt) {
		SharedPreferences preferences;
		if (TextUtils.isEmpty(name)) {
			preferences = PreferenceManager.getDefaultSharedPreferences(context);
		} else {
			preferences = context.getSharedPreferences(name, mode);
		}

		if (isValueEncrypt) {
			String value = getString(context, name, key, String.valueOf(defValue), mode, isKeyEncrypt, true);
			try {
				return Float.valueOf(value);
			} catch (Exception e) {
				return defValue;
			}
		} else {
			return preferences.getFloat(isKeyEncrypt ? MD5Utils.encode(key) : key, defValue);
		}
		
	}
	
	/**
	 * boolean
	 */
	public static boolean getBoolean(Context context, String key, boolean defValue) {
		return getBoolean(context, null, key, defValue);
	}
	
	public static boolean getBoolean(Context context, String name, String key, boolean defValue) {
		return getBoolean(context, name, key, defValue, true, true);
	}
	
	public static boolean getBoolean(Context context, String name, String key, boolean defValue, boolean isKeyEncrypt, boolean isValueEncrypt) {
		return getBoolean(context, name, key, defValue, Context.MODE_PRIVATE, isKeyEncrypt, isValueEncrypt);
	}
	
	public static boolean getBoolean(Context context, String name, String key, boolean defValue, int mode, boolean isKeyEncrypt, boolean isValueEncrypt) {
		SharedPreferences preferences;
		if (TextUtils.isEmpty(name)) {
			preferences = PreferenceManager.getDefaultSharedPreferences(context);
		} else {
			preferences = context.getSharedPreferences(name, mode);
		}
		
		if (isValueEncrypt) {
			String valueString = getString(context, name, key, String.valueOf(defValue), mode, isKeyEncrypt, true);
			try {
				return Boolean.valueOf(valueString);
			} catch (Exception e) {
				return defValue;
			}
		} else {
			return preferences.getBoolean(isKeyEncrypt ? MD5Utils.encode(key) : key, defValue);
		}
	}
	
	/**
	 * StringSet
	 */
	public static Set<String> getStringSet(Context context, String key, Set<String> defValues) {
		return getStringSet(context, null, key, defValues);
	}
	
	public static Set<String> getStringSet(Context context, String name, String key, Set<String> defValues) {
		return getStringSet(context, name, key, defValues, true, true);
	}
	
	public static Set<String> getStringSet(Context context, String name, String key, Set<String> defValues, boolean isKeyEncrypt, boolean isValueEncrypt) {
		return getStringSet(context, name, key, defValues, Context.MODE_PRIVATE, isKeyEncrypt, isValueEncrypt);
	}
	
	/**
     *
	 */
	@TargetApi(11)
	public static Set<String> getStringSet(Context context, String name, String key, Set<String> defValues, int mode, boolean isKeyEncrypt, boolean isValueEncrypt) {
		SharedPreferences preferences;
		if (TextUtils.isEmpty(name)) {
			preferences = PreferenceManager.getDefaultSharedPreferences(context);
		} else {
			preferences = context.getSharedPreferences(name, mode);
		}
		Set<String> valueSet = preferences.getStringSet(isKeyEncrypt ? MD5Utils.encode(key) : key, defValues);
		Set<String> tempValueSet = new HashSet<>();
		for (String s : valueSet) {
			tempValueSet.add(DESUtils.decryptWithDES(s, SECURITY_KEY));
		}
		return tempValueSet;
	}
	
	private PrefsUtils(){/*Do not new me*/}
}
