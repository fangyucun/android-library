package com.hellofyc.apptest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.hellofyc.base.utils.ParseUtils;


/**
 * Created on 2016/6/28.
 *
 * @author Yucun Fang
 */

public class TestView extends View {

    private int mDiameter;
    private int mPadding;
    private Paint mPaint;

    public TestView(Context context) {
        super(context);
        init();
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(ParseUtils.dpToPx(getContext(), 10));
    }

    private void drawCircle(Canvas canvas) {
        canvas.drawCircle(mDiameter/2, mDiameter/2, mDiameter/2 - mPadding, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mDiameter = Math.min(getMeasuredWidth(), getMeasuredHeight());
        mPadding = ParseUtils.dpToPx(getContext(), (int)mPaint.getStrokeWidth() - 1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        canvas.save();
        drawCircle(canvas);
        canvas.restore();
    }
}
