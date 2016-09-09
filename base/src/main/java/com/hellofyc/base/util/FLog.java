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

package com.hellofyc.base.util;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Log Tool
 * Create on 2014年12月6日 下午12:23:58
 *
 * @author Jason Fang
 */
public final class FLog {

    public static final int VERBOSE      = Log.VERBOSE;
    public static final int DEBUG        = Log.DEBUG;
    public static final int INFO         = Log.INFO;
    public static final int WARN         = Log.WARN;
    public static final int ERROR        = Log.ERROR;
    public static final int ASSERT       = Log.ASSERT;
    public static final int JSON         = 8;
    public static final int FILE         = 9;

    private static boolean mIsShow = true;

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static void setLogEnabled(boolean enabled) {
        mIsShow = enabled;
    }

    public static void v(Object textObject) {
        v(null, String.valueOf(textObject));
    }

    public static void v(String tag, Object text) {
        printLog(VERBOSE, new LogInfo(tag, String.valueOf(text)));
    }

    public static void a(Object textObject) {
        a(null, String.valueOf(textObject));
    }

    public static void a(String tag, Object text) {
        printLog(ASSERT, new LogInfo(tag, String.valueOf(text)));
    }

    public static void i(Object textObject) {
        i(null, String.valueOf(textObject));
    }

    public static void i(String tag, Object text) {
        printLog(INFO, new LogInfo(tag, String.valueOf(text)));
    }

    public static void w() {
        w(null, "");
    }

    public static void w(Object textObject) {
        w(null, String.valueOf(textObject));
    }

    public static void w(String tag, String textObject) {
        printLog(WARN, new LogInfo(tag, textObject));
    }

    public static void e(Object textObject) {
        e(null, String.valueOf(textObject));
    }

    public static void e(Throwable tr) {
        Log.e(getInvokeStackTraceElement().getFileName(), "", tr);
    }

    public static void e(String tag, Object textObject) {
        printLog(ERROR, new LogInfo(tag, String.valueOf(textObject)));
    }

    public static void e(String tag, Object textObject, Throwable e) {
        printLog(ERROR, new LogInfo(tag, String.valueOf(textObject), e));
    }

    public static void json(String jsonText) {
        printLog(JSON, new LogInfo(null, jsonText));
    }

    public static void file(String path, String text) {
        printLog(FILE, new LogInfo(null, text, path, null));
    }

    @SuppressWarnings("PointlessBooleanExpression")
    private static void printLog(int type, @NonNull LogInfo logInfo) {
        if (!mIsShow) return;

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
        StackTraceElement element = getInvokeStackTraceElement();
        String className = element.getFileName();
        String methodName = element.getMethodName();
        int lineNumber = element.getLineNumber();
        String methodNameShort = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
        String textPrefix = ("【 (").concat(className)
                .concat(":")
                .concat(lineNumber + "")
                .concat(")#")
                .concat(methodNameShort)
                .concat(" 】 ");
        return new String[]{tag == null ? className : tag, textPrefix, text == null ? "NULL" : text};
    }

    private static StackTraceElement getInvokeStackTraceElement() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        int lastFindIndex = -1;
        for (int i=0; i<stackTraceElements.length; i++) {
            if (stackTraceElements[i].getClassName().equals(FLog.class.getName())) {
                lastFindIndex = i;
            }
        }
        return stackTraceElements[lastFindIndex + 1];
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

    public static String getLogCatInfoByTag(String tag) {
        StringBuilder builder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            Process mProcess = Runtime.getRuntime().exec(
                    new String[]{"logcat", "-d", "AndroidRuntime:E" + tag + ":V *:S"});
            bufferedReader = new BufferedReader(new InputStreamReader(mProcess.getInputStream()));
            String line;
            String separator = System.getProperty(LINE_SEPARATOR);
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
                builder.append(separator);
            }
        } catch (IOException e) {
            FLog.e(e);
        } finally {
            IoUtils.close(bufferedReader);
        }
        return builder.toString();
    }

    private static class LogInfo {
        String tag;
        String text;
        String path;
        Throwable throwable;

        public LogInfo(String tag, String text) {
            this(tag, text, null, null);
        }

        public LogInfo(String tag, String text, Throwable e) {
            this(tag, text, null, e);
        }

        public LogInfo(String tag, String text, String path, Throwable e) {
            this.tag = tag;
            this.text = text;
            this.path = path;
            this.throwable = e;
        }
    }
}
