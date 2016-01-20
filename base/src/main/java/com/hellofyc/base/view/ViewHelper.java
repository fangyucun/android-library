package com.hellofyc.base.view;

import android.view.View;

/**
 * Created on 2016/1/6.
 *
 * @author Yucun Fang
 */
public class ViewHelper {

    public static int getMeasureSize(int size, int measureSpec) {
        int result = size;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case View.MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case View.MeasureSpec.EXACTLY:
                result = specSize;
                break;
            case View.MeasureSpec.AT_MOST:
                result = Math.min(size, specSize);
                break;
        }
        return result;
    }
}
