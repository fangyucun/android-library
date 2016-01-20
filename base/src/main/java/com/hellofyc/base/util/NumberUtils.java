/*
 * Copyright (C) 2015 Jason Fang ( ifangyucun@gmail.com )
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hellofyc.base.util;

import android.support.annotation.NonNull;

/**
 * Created on 2015/10/8.
 *
 * @author Yucun Fang
 */
public class NumberUtils {

    public static int parseInt(String numberString) {
        return parseInt(numberString, 10, 0);
    }

    public static int parseInt(String numberString, int radix, int defValue) {
        try {
            return Integer.parseInt(numberString, radix);
        } catch(NumberFormatException e) {
            return defValue;
        }
    }

    public static long parseLong(String numberString) {
        return parseLong(numberString, 0);
    }

    public static long parseLong(String numberString, long defValue) {
        try {
            return Long.parseLong(numberString);
        } catch(NumberFormatException e) {
            return defValue;
        }
    }

    public static float parseFloat(String numberString) {
        return parseFloat(numberString, 0f);
    }

    public static float parseFloat(String numberString, float defValue) {
        try {
            return Float.parseFloat(numberString);
        } catch(NumberFormatException e) {
            return defValue;
        }
    }

    public static double parseDouble(String numberString) {
        return parseDouble(numberString, 0);
    }

    public static double parseDouble(String numberString, double defValue) {
        try {
            return Double.parseDouble(numberString);
        } catch(NumberFormatException e) {
            return defValue;
        }
    }

    public static int checkNumberLength(long number) {
        try {
            return String.valueOf(Math.abs(number)).length();
        } catch (Exception e) {
            return -1;
        }
    }

    public static boolean isHexNumber(@NonNull String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F'))) {
                return false;
            }
        }
        return true;
    }

    private NumberUtils(){}
}
