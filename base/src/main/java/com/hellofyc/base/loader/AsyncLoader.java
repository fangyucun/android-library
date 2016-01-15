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
 */

package com.hellofyc.base.loader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.util.ArrayMap;

import com.hellofyc.base.http.HttpHelper;

import java.util.Set;

/**
 * Create on 2015年7月3日 上午11:31:25
 * @author Yucun Fang
 */
abstract public class AsyncLoader<D extends LoaderResult> extends AsyncTaskLoader<D> {

	private Bundle mArgs;
	private D mLoaderResult;
	
	public AsyncLoader(Context context, Bundle args) {
		super(context);
		mArgs = args;
	}
	
	abstract protected String getRequestUrl();
	protected void getRequestParams(ArrayMap<String, String> params) {}
	abstract public D onResult(String responseText);
	
	@Override
	public final D loadInBackground() {
		ArrayMap<String, String> params = new ArrayMap<>();
		getRequestParams(params);
		parseBundleToMap(params, mArgs);
		String responseText = HttpHelper.getInstance(getContext()).doPost(getRequestUrl(), params);
		return onResult(responseText);
	}
	
	private void parseBundleToMap(ArrayMap<String, String> mapParams, Bundle bundleParams) {
		if (mapParams == null || bundleParams == null) return;
		
		Set<String> keySet = bundleParams.keySet();
		for (String key : keySet) {
			Object value = bundleParams.get(key);
			if (value instanceof String) {
				mapParams.put(key, bundleParams.getString(key));
			}
		}
	}
	
	@Override
	public void deliverResult(D data) {
		mLoaderResult = data;
		if (isStarted()) {
			super.deliverResult(data);
		}
	}

	@Override
	protected void onStartLoading() {
		if (mLoaderResult != null && mLoaderResult.isSuccess()) {
			deliverResult(mLoaderResult);
			return;
		}
		forceLoad();
	}

	@Override
	protected void onStopLoading() {
		super.onStopLoading();
		cancelLoad();
	}

}
