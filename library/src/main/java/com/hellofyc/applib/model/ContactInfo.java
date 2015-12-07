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

import com.hellofyc.applib.util.ParcelUtils;

public class ContactInfo implements Parcelable {

	public String id;
	public String displayName;
	public String[] phoneNumbers;
	public String[] emails;
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
//		dest.writeString(id);
//		dest.writeString(displayName);
//		dest.writeArray(phoneNumbers);
//		dest.writeArray(emails);
		ParcelUtils.writeToParcel(dest, id, displayName, phoneNumbers, emails);
	}
	
	public static final Creator<ContactInfo> CREATOR = ParcelableCompat
			.newCreator(new ParcelableCompatCreatorCallbacks<ContactInfo>() {

				@Override
				public ContactInfo createFromParcel(Parcel in, ClassLoader loader) {
					ContactInfo info = new ContactInfo();
					info.id = in.readString();
					info.displayName = in.readString();
					info.phoneNumbers = (String[])in.readArray(loader);
					info.emails = (String[])in.readArray(loader);
					return info;
				}

				@Override
				public ContactInfo[] newArray(int size) {
					return new ContactInfo[size];
				}
				
	});

}
