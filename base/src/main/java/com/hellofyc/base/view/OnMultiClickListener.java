package com.hellofyc.base.view;

import android.os.CountDownTimer;
import android.view.View;

import com.hellofyc.base.utils.FLog;

/**
 * Created on 2016/12/7.
 *
 * @author Yucun Fang
 */

abstract public class OnMultiClickListener implements View.OnClickListener {

    private int mClickCount = 0;
    private DelayTimer mDelayTimer;

    public OnMultiClickListener() {
        this(1000);
    }

    public OnMultiClickListener(int clickInvalidDelayTime) {
        int millisInFuture = 1000;
        if (clickInvalidDelayTime > 0) {
            millisInFuture = clickInvalidDelayTime;
        }
        mDelayTimer = new DelayTimer(millisInFuture);
    }

    abstract public void onClick(View v, int clickCount);

    @Override
    public final void onClick(View v) {
        mDelayTimer.cancel();
        mClickCount++;
        onClick(v, mClickCount);
        mDelayTimer.start();
    }

    public class DelayTimer extends CountDownTimer {

        public DelayTimer(long millisInFuture) {
            super(millisInFuture, 200);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            FLog.i(millisUntilFinished);
        }

        @Override
        public void onFinish() {
            mClickCount = 0;
        }
    }
}
