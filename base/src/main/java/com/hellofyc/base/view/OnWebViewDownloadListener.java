/*
 *
 *  * Copyright (C) 2015 Jason Fang ( ifangyucun@gmail.com )
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */package com.hellofyc.base.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.DownloadListener;

/**
 * Create on 2015年5月7日 下午2:51:44
 * @author Yucun Fang
 */
public class OnWebViewDownloadListener implements DownloadListener {

	private Context mContext;
	
	public OnWebViewDownloadListener(Context context) {
		mContext = context;
	}
	
	@Override
	public void onDownloadStart(String url, String userAgent,
			String contentDisposition, String mimetype, long contentLength) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		mContext.startActivity(intent);
	}

}
