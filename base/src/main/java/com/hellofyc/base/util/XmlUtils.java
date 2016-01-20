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

package com.hellofyc.base.util;


/**
 * xml解析器
 * Create on 2013-5-10
 * @author Fang Yucun
 */
public class XmlUtils {
//	private static final boolean DEBUG = false;
//
//	private XmlUtils(){}
//
//	/**
//	 * @see StringUtils#getMimeTypeByExtension
//	 */
//	public static String getMimeType(Context context, String extension) {
//		if (extension == null || extension.trim().length() == 0) {
//			throw new IllegalArgumentException("extension can not be null or it's length is 0");
//		}
//		XmlResourceParser parser = context.getResources().getXml(R.xml.mimetypes);
//		int eventType = 0;
//		try {
//			eventType = parser.getEventType();
//		} catch (XmlPullParserException e) {
//			FLog.e(e);
//		}
//		while (eventType != XmlResourceParser.END_DOCUMENT) {
//			switch (eventType) {
//			case XmlResourceParser.START_TAG:
//				if (parser.getName().equals("type") && extension.equals(parser.getAttributeValue(0))) {
//					return parser.getAttributeValue(1);
//				}
//				break;
//			}
//			try {
//				eventType = parser.next();
//			} catch (XmlPullParserException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return "";
//	}

}
