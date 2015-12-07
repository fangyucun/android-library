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


public class Response<T> {

	public final T mResult;
	public final FError mError;
	
	private Response(T result) {
		mResult = result;
		mError = null;
	}
	
	private Response(FError error) {
		mResult = null;
		mError = error;
	}
	
	public static <T> Response<T> success(T result) {
        return new Response<>(result);
    }
	
	public static <T> Response<T> error(FError error) {
        return new Response<>(error);
    }
	
	public boolean isSuccess() {
		return mError == null;
	}
	
	public interface Callback<T> {
		void onResponse(T response);
	}
	
	public interface ErrorCallback {
		void onErrorResoponse(FError error);
	}
}
