package com.hellofyc.base.widget;

import android.support.v4.widget.AutoScrollHelper;
import android.widget.ScrollView;

/**
 * Created on 2015/9/7.
 *
 * @author Yucun Fang
 */
public class ScrollViewAutoScrollHelper extends AutoScrollHelper {

    private ScrollView mTarget;

    public ScrollViewAutoScrollHelper(ScrollView target) {
        super(target);
        mTarget = target;
    }

    @Override
    public void scrollTargetBy(int deltaX, int deltaY) {
        mTarget.smoothScrollBy(deltaX, deltaY);
    }

    @Override
    public boolean canTargetScrollHorizontally(int direction) {
        return mTarget.canScrollHorizontally(direction);
    }

    @Override
    public boolean canTargetScrollVertically(int direction) {
        return mTarget.canScrollVertically(direction);
    }
}
