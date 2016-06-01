package com.hellofyc.base.view;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created on 2016/1/6.
 *
 * @author Yucun Fang
 */
public class ViewHelper {

    public static PointF getUpperLeftCornerCoordinate(@NonNull View view) {
        float src[] = new float[8];
        float[] dst = new float[]{0, 0, view.getWidth(), 0, 0, view.getHeight(), view.getWidth(), view.getHeight()};
        view.getMatrix().mapPoints(src, dst);
        return new PointF(view.getX() + src[0], view.getY() + src[1]);
    }

    public static PointF getUpperRightCornerCoordinate(@NonNull View view) {
        float src[] = new float[8];
        float[] dst = new float[]{0, 0, view.getWidth(), 0, 0, view.getHeight(), view.getWidth(), view.getHeight()};
        view.getMatrix().mapPoints(src, dst);
        return new PointF(view.getX() + src[2], view.getY() + src[3]);
    }

    public static PointF getLowerLeftCornerCoordinate(@NonNull View view) {
        float src[] = new float[8];
        float[] dst = new float[]{0, 0, view.getWidth(), 0, 0, view.getHeight(), view.getWidth(), view.getHeight()};
        view.getMatrix().mapPoints(src, dst);
        return new PointF(view.getX() + src[4], view.getY() + src[5]);
    }

    public static PointF getLowerRightCornerCoordinate(@NonNull View view) {
        float src[] = new float[8];
        float[] dst = new float[]{0, 0, view.getWidth(), 0, 0, view.getHeight(), view.getWidth(), view.getHeight()};
        view.getMatrix().mapPoints(src, dst);
        return new PointF(view.getX() + src[6], view.getY() + src[7]);
    }
}
