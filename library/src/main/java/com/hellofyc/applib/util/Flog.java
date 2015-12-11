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

package com.hellofyc.applib.util;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Log Tool
 * Create on 2014年12月6日 下午12:23:58
 *
 * @author Jason Fang
 */
public final class FLog {
    private static final boolean ENABLE = true;

    public static final int VERBOSE      = Log.VERBOSE;
    public static final int DEBUG        = Log.DEBUG;
    public static final int INFO         = Log.INFO;
    public static final int WARN         = Log.WARN;
    public static final int ERROR        = Log.ERROR;
    public static final int ASSERT       = Log.ASSERT;
    public static final int JSON         = 8;
    public static final int FILE         = 9;

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
    public static void i() {
        i(null, "");
    }

    public static void i(Object textObject) {
        i(null, String.valueOf(textObject));
    }

    public static void i(String tag, String text) {
        printLog(INFO, new LogInfo(tag, text));
    }

    public static void e() {
        e(null, "");
    }

    public static void e(Object textObject) {
        e(null, String.valueOf(textObject));
    }

    public static void e(String tag, String text) {
        printLog(ERROR, new LogInfo(tag, text));
    }

    public static void json(String jsonText) {
        printLog(JSON, new LogInfo(null, jsonText));
    }

    public static void file(String path, String text) {
        printLog(FILE, new LogInfo(null, text, path));
    }

    @SuppressWarnings("PointlessBooleanExpression")
    private static void printLog(int type, @NonNull  LogInfo logInfo) {
        if (!ENABLE) return;

        String[] wrapContent = getWrappedContent(logInfo.tag, logInfo.text);
        logInfo.tag = wrapContent[0];
        String prefix = wrapContent[1];
        logInfo.text = wrapContent[2];

        switch (type) {
            case JSON:
                printJson(logInfo.tag, prefix, logInfo.text);
                break;
            case FILE:
                printFile(logInfo.path, logInfo.text);
                break;
            default:
                String[] texts = StringUtils.divideString(prefix.concat(logInfo.text), 4000);
                for (String splitedText : texts) {
                    Log.println(type, logInfo.tag, splitedText);
                }
                break;
        }
    }

    private static String[] getWrappedContent(String tag, String text) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String className = stackTrace[5].getFileName();
        String methodName = stackTrace[5].getMethodName();
        int lineNumber = stackTrace[5].getLineNumber();
        String methodNameShort = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
        String textPrefix = ("[ (").concat(className).concat(":").concat(lineNumber + "").concat(")#").concat(methodNameShort).concat(" ] ");
        return new String[]{tag == null ? className : tag, textPrefix, text == null ? "NULL" : text};
    }

    private static void printJson(String tag, String prefix, String jsonText) {
        String message;
        try {
            if (jsonText.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(jsonText);
                message = jsonObject.toString(4);
            } else if (jsonText.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(jsonText);
                message = jsonArray.toString(4);
            } else {
                message = jsonText;
            }
        } catch (JSONException e) {
            message = jsonText;
        }

        message = prefix + LINE_SEPARATOR + message;
        String[] lines = message.split(LINE_SEPARATOR);
        for (String line : lines) {
            Log.println(INFO, tag, "║ " + line);
        }
    }

    private static void printFile(String path, String text) {
        File file = new File(path);
        FileUtils.writeString(text, file.getParentFile(), file.getName());
    }

    static class LogInfo {
        String tag;
        String text;
        String path;

        public LogInfo(String tag, String text) {
            this(tag, text, null);
        }

        public LogInfo(String tag, String text, String path) {
            this.tag = tag;
            this.text = text;
            this.path = path;
        }
    }
}
