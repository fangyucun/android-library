package com.hellofyc.base.view.animation;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Created on 2016/1/7.
 *
 * @author Yucun Fang
 */
public class BezierEvaluator implements TypeEvaluator<PointF> {

    private PointF mPointF1, mPointF2;

    public BezierEvaluator() {
    }

    public BezierEvaluator(PointF pointF1, PointF pointF2) {
        mPointF1 = pointF1;
        mPointF2 = pointF2;
    }

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        float x = (float)(startValue.x * Math.pow(1 - fraction, 3) + 3 * mPointF1.x * Math.pow(1 - fraction, 2) +
                3 * Math.pow(mPointF2.x, 2) * (1 - fraction) + endValue.x * Math.pow(fraction, 3));

        float y = (float)(startValue.y * Math.pow(1 - fraction, 3) + 3 * mPointF1.y * Math.pow(1 - fraction, 2) +
                3 * mPointF2.y * mPointF2.y * (1 - fraction) + endValue.y * Math.pow(fraction, 3));

        return new PointF(x, y);
    }

}
