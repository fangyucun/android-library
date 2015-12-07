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
 */package com.hellofyc.applib.model;

import android.os.Parcel;
import android.os.Parcelable.ClassLoaderCreator;

/**
 * Create on 2015年7月2日 上午9:51:54
 * @author Yucun Fang
 */
abstract public class ParcelCreator<T> implements ClassLoaderCreator<T> {
	
	@Override
	public T createFromParcel(Parcel source) {
		return createFromParcel(source, null);
	}

	@Override
	abstract public T[] newArray(int size);

	@Override
	abstract public T createFromParcel(Parcel source, ClassLoader loader);

}
