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
 */package com.hellofyc.applib.util;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Collection Tool
 *
 * Create on 2014年12月6日 下午12:16:13
 * @author Jason Fang
 */
public final class CollectionUtils {

	public static boolean isContained(List<String> list, String string) {
		return isContainedIgnoreCase(list, string, true);
	}
	
	public static boolean isContain(int[] array, int value) {
		if (array == null || array.length == 0) return false;

        for (int i : array) {
            if (array[i] == value) return true;
        }
		return false;
	}
	
	public static boolean isContainedIgnoreCase(List<String> list, String string, boolean isIgnoreCase) {
		if (isEmpty(list)) return false;
		
		for (String s : list) {
			if (isIgnoreCase ? s.equalsIgnoreCase(string) : s.equals(string)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isEmpty(List<?> list) {
		return list == null || list.size() == 0;
	}
	
	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.size() == 0;
	}
	
	public static boolean isContained(Map<String, ?> map, String str) {
		if (isEmpty(map)) return false;
		if (TextUtils.isEmpty(str)) return false;
		
		for (Map.Entry<String, ?> entry : map.entrySet()) {
			if (entry.getKey().equals(str)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * List To ArrayList
	 */
	public static <T> ArrayList<T> parseListToArrayList(List<T> lists) {
		if (isEmpty(lists)) return new ArrayList<>();
		
		ArrayList<T> arrayList = new ArrayList<>();
		arrayList.addAll(lists);
		return arrayList;
	}
	
	public static <V> ArrayMap<String, V> sortedMapByKey(ArrayMap<String, V> map) {
		if (map == null || map.size() == 0) return null;
		
		List<String> keyList = new ArrayList<>(map.keySet());
		Collections.sort(keyList, new Comparator<String>() {
			
			@Override
			public int compare(String lhs, String rhs) {
				return lhs.compareTo(rhs);
			}
		});
		
		ArrayMap<String, V> resultMap = new ArrayMap<>();
		for (int i=0; i<keyList.size(); i++) {
			String key = keyList.get(i);
			resultMap.put(key, map.get(key));
		}
		return resultMap;
	}
}
