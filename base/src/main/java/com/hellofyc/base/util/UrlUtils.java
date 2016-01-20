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

import android.text.TextUtils;
import android.webkit.URLUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Create on 2015年5月15日 上午10:59:45
 *
 * @author Yucun Fang
 */
public final class UrlUtils {

	public static final String SUFFIX_IMAGE_JGP	 = ".jpg";
	public static final String SUFFIX_IMAGE_JGEP = ".jpeg";
	public static final String SUFFIX_IMAGE_PNG	 = ".png";
	public static final String SUFFIX_IMAGE_BMP	 = ".bmp";
	public static final String SUFFIX_IMAGE_GIF  = ".gif";
	
	private static List<String> mImageSuffixList = new ArrayList<String>() {
		
		private static final long serialVersionUID = 105726999927982466L;

	{
		add(SUFFIX_IMAGE_JGP);
		add(SUFFIX_IMAGE_JGEP);
		add(SUFFIX_IMAGE_PNG);
		add(SUFFIX_IMAGE_BMP);
		add(SUFFIX_IMAGE_GIF);
		
	}};
	
    public static boolean isUrl(String url) {
    	return URLUtil.isNetworkUrl(url);
    }
    
    public static boolean isImageUrl(String url) {
    	return isUrl(url) && mImageSuffixList.contains(getSuffixFromUrl(url));
    }
    
    public static String getSuffixFromUrl(String url) {
    	if (TextUtils.isEmpty(url)) return "";
		
		return url.substring(url.lastIndexOf('.')).toLowerCase(Locale.CHINA);
    }
    
    
	
	private UrlUtils() {/*Do not new me!*/}
}
