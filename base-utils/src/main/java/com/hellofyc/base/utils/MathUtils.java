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

package com.hellofyc.base.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * create on 2015/8/18 11:52
 *
 * @author Yucun Fang
 */
public class MathUtils {

    public static int constrain(int amount, int low, int high) {
        return amount < low ? low : (amount > high ? high : amount);
    }

    public static float constrain(float amount, float low, float high) {
        return amount < low ? low : (amount > high ? high : amount);
    }

    public static double add(double augend, double addend) {
        return BigDecimal.valueOf(augend).add(BigDecimal.valueOf(addend)).doubleValue();
    }

    public static double add(double augend, double addend, int precision) {
        MathContext context = new MathContext(precision);
        return BigDecimal.valueOf(augend).add(BigDecimal.valueOf(addend), context).doubleValue();
    }

    public static double divide(double dividend, double divisor, int precision) {
        return divide(dividend, divisor, precision, RoundingMode.HALF_EVEN);
    }

    public static double divide(double dividend, double divisor, int precision, RoundingMode roundingMode) {
        MathContext context = new MathContext(precision, roundingMode);
        return BigDecimal.valueOf(dividend).divide(BigDecimal.valueOf(divisor), context).doubleValue();
    }

    public static double minus(double minuend, double subtrahand) {
        return BigDecimal.valueOf(minuend).subtract(BigDecimal.valueOf(subtrahand)).doubleValue();
    }

    public static double minus(double minuend, double subtrahand, int precision) {
        MathContext context = new MathContext(precision);
        return BigDecimal.valueOf(minuend).subtract(BigDecimal.valueOf(subtrahand), context).doubleValue();
    }

    public static double multiply(double multiplacand, double multiplier) {
        return BigDecimal.valueOf(multiplacand).multiply(BigDecimal.valueOf(multiplier)).doubleValue();
    }

    public static double multiply(double multiplacand, double multiplier, int precision) {
        MathContext context = new MathContext(precision);
        return BigDecimal.valueOf(multiplacand).multiply(BigDecimal.valueOf(multiplier), context).doubleValue();
    }
}
