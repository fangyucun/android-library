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
 */package com.hellofyc.base.util;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.util.Locale;
import java.util.Map;

/**
 * Create on 2013-6-5
 *
 * @author Fang Yucun
 */
public final class StringUtils {
	static final boolean DEBUG = false;
	
	public static final String FILE_EXTENSION_APK = "apk";
	public static final String FILE_EXTENSION_DOC = "doc";
	public static final String FILE_EXTENSION_DOCX = "docx";
	public static final String FILE_EXTENSION_PDF = "pdf";
	public static final String FILE_EXTENSION_PPT = "ppt";
	public static final String FILE_EXTENSION_PPTX = "pptx";
	public static final String FILE_EXTENSION_XLS = "xls";
	public static final String FILE_EXTENSION_XLSX = "xlsx";
	public static final String FILE_EXTENSION_TXT = "txt";
	public static final String FILE_EXTENSION_XML = "xml";
	public static final String FILE_EXTENSION_LOG = "log";
	public static final String FILE_EXTENSION_ZIP = "zip";
	
	public static String formatPlayTime(long milliseconds) {
		if (milliseconds < 0) {
			return "00:00";
		}
		int totalSeconds = (int)(milliseconds / 1000);
		int seconds = totalSeconds % 60;
		int minutes = totalSeconds / 60;
		int hours = minutes / 60;
		return hours == 0 ? String.format(Locale.CHINA, "%02d:%02d", minutes, seconds) :
			String.format(Locale.CHINA, "%02d:%02d:%02d", hours, minutes, seconds);
	}
	
	public static String getFileExtension(String filePath) {
		if (filePath != null && filePath.length() > 0) {
			int start = filePath.lastIndexOf('.');
			if (start > -1 && start < filePath.length() -1) {
				return filePath.substring(start + 1).toLowerCase(Locale.getDefault());
			}
		}
		return "";
	}
	
	public static boolean isBlank(CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i=0; i<strLen; i++) {
			if (!Character.isWhitespace(cs.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	public static String[] divideString(@NonNull String text, int lengthPerGroup) {
		if (lengthPerGroup < 1) {
            lengthPerGroup = 1;
        }
		if (text.length() < lengthPerGroup) return new String[]{text};
		
		int size = text.length() % lengthPerGroup == 0 ? text.length() / lengthPerGroup : text.length() / lengthPerGroup + 1;
		String[] strs = new String[size];
		for (int i=0; i<strs.length; i++) {
			strs[i] = text.substring(i * lengthPerGroup, lengthPerGroup * (i+1) >= text.length() ? text.length() : lengthPerGroup * (i+1));
		}
		return strs;
	}

    public static String substring(String text, String endText) {
        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(endText)) return text;

        int endIndex = text.indexOf(endText);
        if (endIndex == -1) return text;
        return text.substring(0, endIndex);
    }
	
	/**
	 * 通过扩展名获取MIME类型
	 */
	public static String getMimeTypeByExtension(String extension) {
		return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
	}
	
	/**
	 * 通过文件名获取MIME类型
	 */
	public static String getMimeTypeByFileName(String name) {
		return MimeTypeMap.getSingleton().getMimeTypeFromExtension(getFileExtension(name));
	}
	
	/**
	 * 通过MIME获取扩展名
	 */
	public static String getExtensionByMimeType(String mimeType) {
		return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
	}
	
	/**
	 * 通过Url获取扩展名
	 */
	public static String getExtensionFromUrl(String url) {
		return MimeTypeMap.getFileExtensionFromUrl(url);
	}
	
	public static String getFileNameFromPath(String path) {
		if (TextUtils.isEmpty(path)) return "noname";
		
		if (path.contains("\\")) {
			return path.substring(path.lastIndexOf('\\') + 1);
		} else {
			return path.substring(path.lastIndexOf('/') + 1);
		}
	}
	
	public static String getFileNameFromUrl(String urlString) {
		if (TextUtils.isEmpty(urlString)) return "noname";
		
		return urlString.substring(urlString.lastIndexOf('/') + 1);
	}
	
	/**
	 * 通过全路径报名获取简单包名
	 */
	public static String getSimpleNameFromClassName(String className) {
		int start = className.lastIndexOf(".") + 1;
		int end = className.indexOf("$");
		return className.substring(start, end == -1 ? className.length() : end);
	}

	public static String getNameFromUrl(String url) {
		if (url != null && !url.endsWith("/")) {
			return url.substring(url.lastIndexOf("/") + 1);
		} else {
			return "noname";
		}
	}
	
	public static String getSimpleNameFromFullName(String fullName) {
		return getSimpleNameFromClassName(fullName);
	}

    public static String parseMapToString(ArrayMap<String, ?> paramsMap, char betweenKeyValue, char betweenValueKey) {
        if (paramsMap == null || paramsMap.size() <= 0) return "";

        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, ?> entry : paramsMap.entrySet()) {
            builder.append(betweenValueKey);
            builder.append(entry.getKey());
            builder.append(betweenKeyValue);
            builder.append(String.valueOf(entry.getValue()));
        }
        return builder.substring(1);
    }

	private StringUtils(){/*Do not new me*/}
}
