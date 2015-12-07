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

package com.hellofyc.applib.net.http;

import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RequestParams {

	protected ConcurrentHashMap<String, String> mUrlParams = new ConcurrentHashMap<>();
	protected ConcurrentHashMap<String, StreamWrapper> mStreamParams = new ConcurrentHashMap<>();
	protected ConcurrentHashMap<String, FileWrapper> mFileParams = new ConcurrentHashMap<>();
	protected ConcurrentHashMap<String, Object> mUrlParamsWithObjects = new ConcurrentHashMap<>();
	
	public RequestParams(Map<String, String> paramsMap) {
		if (paramsMap != null) {
			for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
				put(entry.getKey(), entry.getValue());
			}
		}
	}
	
	public RequestParams(Object... keysAndValues) {
		
		int length = keysAndValues.length;
		if (length%2 != 0) throw new IllegalArgumentException ("Supplied arguments must be even!");
		
		for (int i=0; i<length; i+=2) {
			String key = String.valueOf(keysAndValues[i]);
			String value = String.valueOf(keysAndValues[i + 1]);
			put(key, value);
		}
	}
	
	public void put(String key, String value) {
		mUrlParams.put(key, value);
	}
	
	public void put(String key, File file) throws FileNotFoundException {
		put(key, file, null);
	}
	
	public void put(String key, File file, String contentType) throws FileNotFoundException {
		if (file != null && contentType != null) {
			mFileParams.put(key, new FileWrapper(file, contentType));
		}
	}
	
	public void put(String key, InputStream inputStream) {
		put(key, inputStream, null, null);
	}
	
	public void put(String key, InputStream inputStream, String name) {
		put(key, inputStream, name, null);
	}
	
	public void put(String key, InputStream inputStream, String name, String contentType) {
		if (!TextUtils.isEmpty(key) && inputStream != null) {
			mStreamParams.put(key, new StreamWrapper(inputStream, name, contentType));
		}
	}
	
	public void put(String key, Object value) {
		if (!TextUtils.isEmpty(key) && value != null) {
			mUrlParamsWithObjects.put(key, value);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void add(String key, String value) {
		if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
		    Object params = mUrlParamsWithObjects.get(key);
		    if (params == null) {
		    	params = new HashSet();
		        put(key, params);
		    }
		    if ((params instanceof List)) {
		    	((List)params).add(value);
		    } else if ((params instanceof Set)) {
		    	((Set)params).add(value);
		    }
	    }
	}
	
	public void remove(String key) {
		mUrlParams.remove(key);
		mFileParams.remove(key);
		mStreamParams.remove(key);
		mUrlParamsWithObjects.remove(key);
	}
	
	class FileWrapper {
		
		public File file;
		public String contentType;
		
		public FileWrapper(File file, String contentType) {
			this.file = file;
			this.contentType = contentType;
		}
	}
	
	class StreamWrapper {
		
		public InputStream inputStream;
		public String name;
		public String contentType;
		
		public StreamWrapper(InputStream inputStream, String name, String contentType) {
			this.inputStream = inputStream;
			this.name = name;
			this.contentType = contentType;
		}
		
	}
}
