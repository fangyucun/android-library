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

package com.hellofyc.applib.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hellofyc.applib.util.FLog;

public class DbHelper extends SQLiteOpenHelper {

	private static final boolean DEBUG = true;
	
	private static String DB_NAME = "test1.db";
	private static int DB_VERSION = 1;
	
	private static Context mContext;
	
//	private static final String TABLE_NAME = "table1";
	
	private DbHelper() {
		super(mContext, DB_NAME, null, DB_VERSION);
	}
	
	public static DbHelper getInstance(Context context) {
		mContext = context;
		return SingletonHolder.mInstance;
	}
	
	public SQLiteDatabase open() {
		return SingletonHolder.mInstance.getReadableDatabase();
	}
	
	public void close() {
		SingletonHolder.mInstance.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
//		db.execSQL("CREATE TABLE IF NOT EXISTS accesspoint( _id INTEGER PRIMARY KEY AUTOINCREMENT"
//				+ ", bssid TEXT NOT NULL DEFAULT ' '"
//				+ ", ssid TEXT NOT NULL DEFAULT ' '"
//				+ ", pwd TEXT NOT NULL DEFAULT ' '"
//				+ ", security_type INTEGER NOT NULL DEFAULT '0'"
//				+ ", longitude REAL NOT NULL DEFAULT '0.0'"
//				+ ", latitude REAL NOT NULL DEFAULT '0.0'"
//				+ ", address TEXT NOT NULL DEFAULT ' '"
//				+ ", belonging_to TEXT NOT NULL DEFAULT ' '"
//				+ ", contact TEXT NOT NULL DEFAULT ' '"
//				+ ", heartbeat_time INTEGER NOT NULL DEFAULT '0'"
//				+ ", average_speed TEXT NOT NULL DEFAULT ' '"
//				+ ", test_speed TEXT NOT NULL DEFAULT ' '"
//				+ ", is_puglic_wifi INTEGER NOT NULL DEFAULT '0'"
//				+ ", is_fishing_wifi INTEGER NOT NULL DEFAULT '0'"
//				+ ", is_fake_wifi INTEGER NOT NULL DEFAULT '0'"
//				+ ", is_dns_normal INTEGER NOT NULL DEFAULT '0'"
//				+ ", success_rate TEXT NOT NULL DEFAULT ' '"
//				+ ", shared_switch INTEGER NOT NULL DEFAULT '0'"
//				+ ", shared_by_me INTEGER NOT NULL DEFAULT '0'"
//				+ ", connect_times INTEGER NOT NULL DEFAULT '0'"
//				+ ", update_time INTEGER NOT NULL DEFAULT '0'  );");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (DEBUG) FLog.i("onUpgrade");
	}
	
	private static class SingletonHolder {
		private static DbHelper mInstance = new DbHelper();
	}

}
