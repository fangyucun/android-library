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

package com.hellofyc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Create on 2014年12月6日 下午12:28:06
 * @author Jason Fang
 */
public class LogCat {
	static final boolean DEBUG = false;

	private Process mProcess;
	private BufferedReader mLogCatReader;
	
	private static class SingtonHolder {
		private static final LogCat instance = new LogCat();
	}
	
	public static LogCat getInstance() {
		return SingtonHolder.instance;
	}
	
	public String getLogCatInfoByTag(String tag) {
		StringBuilder builder = new StringBuilder();
		try {
			mProcess = Runtime.getRuntime().exec(
					new String[]{"logcat", "-d", "AndroidRuntime:E" + tag + ":V *:S"});
			mLogCatReader = new BufferedReader(new InputStreamReader(mProcess.getInputStream()));
			String line;
			String separator = System.getProperty("line.separator");
			while ((line = mLogCatReader.readLine()) != null) {
				builder.append(line);
				builder.append(separator);
			}
		} catch (IOException e) {
			FLog.e(e);
		} finally {
			IoUtils.close(mLogCatReader);
		}
		return builder.toString();
	}
	
	
	private LogCat() {/*Do not new me*/}
}

