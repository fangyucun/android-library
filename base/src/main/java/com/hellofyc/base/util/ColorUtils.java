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

import android.graphics.Color;

/**
 * create on 2015/8/17 18:57
 *
 * @author Yucun Fang
 */
public class ColorUtils {

    public static int getDarkenColor(int color) {
        float[] HSV = new float[3];
        Color.colorToHSV(color, HSV);
        HSV[2] *= 0.8f;
        return Color.HSVToColor(HSV);
    }

    public static String getColorString(int color) {
        if (color < 0 || color > 255) {
            throw new IllegalArgumentException("color cannot rather than 255 or less than 0!");
        }
        String hexString = Integer.toHexString(color);
        return hexString.length() != 2 ? "0".concat(hexString) : hexString;
    }

    public static String getAlphaString(float opacity) {
        return getColorString((int) (opacity * 255));
    }

    public static int getARGBColor(int alpha, int color) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }

    public static int getRGBColor(int color) {
        return Color.rgb(Color.red(color), Color.green(color), Color.blue(color));
    }

    public static String getARGBColorString(int color) {
        String alphaString = getColorString(Color.alpha(color));
        String redString = getColorString(Color.red(color));
        String greenString = getColorString(Color.green(color));
        String blueString = getColorString(Color.blue(color));
        return "#".concat(alphaString).concat(redString).concat(greenString).concat(blueString);
    }

    public static String getRGBColorString(int color) {
        String redString = getColorString(Color.red(color));
        String greenString = getColorString(Color.green(color));
        String blueString = getColorString(Color.blue(color));
        return "#".concat(redString).concat(greenString).concat(blueString);
    }
}
