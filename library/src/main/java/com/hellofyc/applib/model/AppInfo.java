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

package com.hellofyc.applib.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;

public class AppInfo implements Parcelable {

	private long id;
	public String label;
	public long size;
	public String packageName;
	public int versionCode;
	public String versionName;
	
	public long cacheSize;
	public long dataSize;
	//ApkSize
	public long codeSize;
	public long totalSize;
	
	public long externalCacheSize;
	public long externalDataSize;
	public long externalCodeSize;
	public long externalMediaSize;
	public long externalObbSize;

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(label);
		dest.writeLong(size);
		dest.writeString(packageName);
		dest.writeString(versionName);
		dest.writeInt(versionCode);
		dest.writeLong(cacheSize);
		dest.writeLong(dataSize);
		dest.writeLong(codeSize);
		dest.writeLong(totalSize);
		dest.writeLong(externalCacheSize);
		dest.writeLong(externalDataSize);
		dest.writeLong(externalCodeSize);
		dest.writeLong(externalMediaSize);
		dest.writeLong(externalObbSize);
	}

	public static final Creator<AppInfo> CREATOR = ParcelableCompat
			.newCreator(new ParcelableCompatCreatorCallbacks<AppInfo>() {

				@Override
				public AppInfo[] newArray(int size) {
					return new AppInfo[size];
				}

				@Override
				public AppInfo createFromParcel(Parcel in, ClassLoader loader) {
					AppInfo info = new AppInfo();
					info.id = in.readLong();
					info.label = in.readString();
					info.size = in.readLong();
					info.packageName = in.readString();
					info.versionName = in.readString();
					info.versionCode = in.readInt();
					info.cacheSize = in.readLong();
					info.dataSize = in.readLong();
					info.codeSize = in.readLong();
					info.totalSize = in.readLong();
					info.externalCacheSize = in.readLong();
					info.externalDataSize = in.readLong();
					info.externalCodeSize = in.readLong();
					info.externalMediaSize = in.readLong();
					info.externalObbSize = in.readLong();
					return info;
				}
			});

	@Override
	public int describeContents() {
		return 0;
	}
}
