package com.hellofyc.base.view;

import android.view.View;

/**
 * Created on 2016/12/7.
 *
 * @author Yucun Fang
 */

abstract public class OnValidClickListener implements View.OnClickListener {

    private long mLastClickTime = -1;
    private long mInvalidIntervalTime = 0;

    public OnValidClickListener() {
        this(2000);
    }

    public OnValidClickListener(long intervalTime) {
        mInvalidIntervalTime = intervalTime;
    }

    @Override
    public final void onClick(View v) {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - mLastClickTime) >= mInvalidIntervalTime) {
            onValidClick(v);
            mLastClickTime = currentTime;
        }

    }

    abstract public void onValidClick(View v);
}
