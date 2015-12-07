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
 */

package com.hellofyc.applib.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Create on 2015年5月7日 下午3:22:03
 * @author Yucun Fang
 */
public class User implements Parcelable {

	public String id;
	public String qid;
    public String openId;
    public String accessToken;
	public String realname;
	public String username;
	public String nickname;
    public int sex;
    public String country;
    public String province;
    public String city;
	public String avatarUrl;
	public String kamTel;
	public String email;
	public String tel;
	public boolean isAuth;
	public int authType;
	public String authTitle;
	public String authIconUrl;
	
	public User() {
	}
	
	public User(Parcel source) {
		id = source.readString();
		qid = source.readString();
        openId = source.readString();
        accessToken = source.readString();
		realname = source.readString();
		username = source.readString();
		nickname = source.readString();
		sex = source.readInt();
        country = source.readString();
        province = source.readString();
		city = source.readString();
		avatarUrl = source.readString();
		kamTel = source.readString();
		email = source.readString();
		tel = source.readString();
		isAuth = source.readInt() == 1;
		authType = source.readInt();
		authTitle = source.readString();
		authIconUrl = source.readString();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(qid);
		dest.writeString(openId);
		dest.writeString(accessToken);
		dest.writeString(realname);
		dest.writeString(username);
		dest.writeString(nickname);
		dest.writeInt(sex);
		dest.writeString(country);
		dest.writeString(province);
		dest.writeString(city);
		dest.writeString(avatarUrl);
		dest.writeString(kamTel);
		dest.writeString(email);
		dest.writeString(tel);
		dest.writeInt(isAuth ? 1 : 0);
		dest.writeInt(authType);
		dest.writeString(authTitle);
		dest.writeString(authIconUrl);
	}
	
	public static final Creator<User> CREATOR = new Creator<User>() {

		@Override
		public User createFromParcel(Parcel source) {
			return new User(source);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};

}
