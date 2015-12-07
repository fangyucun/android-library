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


/**
 * Create on 2015年6月3日 上午9:46:19
 * @author Yucun Fang
 */
public class ItemInfo extends ParcelInfo {

	public int itemType;

	public ItemInfo() {
	}

	protected ItemInfo(Parcel source, ClassLoader loader) {
		super(source, loader);
	}

	@Override
	public void writeToParcel(Parcel dest) {
		dest.writeInt(itemType);
	}

	@Override
	public void readFromParcel(Parcel source, ClassLoader loader) {
		itemType = source.readInt();
	}

}
