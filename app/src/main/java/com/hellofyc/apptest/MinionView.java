package com.hellofyc.apptest;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created on 2016/2/3.
 *
 * @author Yucun Fang
 */
public class MinionView extends View {

    private Paint mPaint;

    private float mBodyWidth, mBodyHeight;
    private float mBodyScale = 0.6f;

    public MinionView(Context context) {
        super(context);
        init();
    }

    public MinionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MinionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MinionView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mBodyWidth = Math.min(getWidth(), getHeight() * mBodyScale);
        mBodyHeight = Math.min(getWidth(), getHeight() * mBodyScale);
    }
}
