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

package com.hellofyc.applib.media;

import android.annotation.TargetApi;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;

import com.hellofyc.applib.util.Flog;

@TargetApi(16)
public class MediaToolBox {
	
	private static final String TAG = MediaToolBox.class.getSimpleName();
	private static final boolean DEBUG = false;
	
	public static int getColorFormat(String mimeType) {
		MediaCodecInfo codecInfo = selectCodec(mimeType);
        if (codecInfo == null) return 0;
        MediaCodecInfo.CodecCapabilities capabilities = codecInfo.getCapabilitiesForType(mimeType);
        if (DEBUG) Flog.i(TAG, "capabilities.colorFormats.length:" + capabilities.colorFormats.length);
        int format = 0;
        for (int i = 0; i < capabilities.colorFormats.length; i++) {
        	format = capabilities.colorFormats[i];
        	if (DEBUG) Flog.i(TAG, "Key_Color_Format:" + format);
        }
        return format;
	}
	
	@SuppressWarnings("deprecation")
	public static MediaCodecInfo selectCodec(String mimeType) {
	     int codecsCount = MediaCodecList.getCodecCount();
	     for (int i = 0; i<codecsCount; i++) {
	         MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
	         if (!codecInfo.isEncoder()) {
	             continue;
	         }

	         String[] types = codecInfo.getSupportedTypes();
             for (String type : types) {
	             if (type.equalsIgnoreCase(mimeType)) {
	                 return codecInfo;
	             }
	         }
	     }
	     return null;
	}
	
	private MediaToolBox() {/*Do not new me!*/}
}
