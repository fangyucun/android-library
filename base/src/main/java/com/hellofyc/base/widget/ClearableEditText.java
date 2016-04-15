package com.hellofyc.base.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.hellofyc.base.R;

/**
 * Created on 2016/4/15.
 *
 * @author Yucun Fang
 */
public class ClearableEditText extends AppCompatEditText {

    private Drawable[] mDrawables;
    private Drawable mClearButtonDrawable;
    private boolean mIsHit = false;

    public ClearableEditText(Context context) {
        super(context);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init() {
        mDrawables = getCompoundDrawablesRelative();
        mClearButtonDrawable = ContextCompat.getDrawable(getContext(), R.drawable.base_ic_input_clear);
        setCompoundDrawablesWithIntrinsicBounds(mDrawables[0], mDrawables[1], mClearButtonDrawable, mDrawables[3]);
        addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    setClearButtonVisible(true);
                } else {
                    setClearButtonVisible(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        setSelection(getText().length());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (hitTheClearButton(event)) {
                    mIsHit = true;
                    return true;
                } else {
                    mIsHit = false;
                }
            case MotionEvent.ACTION_UP:
                if (mIsHit && hitTheClearButton(event)) {
                    setText("");
                    setClearButtonVisible(false);
                    return true;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean hitTheClearButton(MotionEvent event) {
        int[] location = new int[2];
        getLocationOnScreen(location);
        return (event.getRawX() >= (location[0] + getWidth() - mClearButtonDrawable.getBounds().width()));
    }

    private void setClearButtonVisible(boolean visibility) {
        setCompoundDrawablesRelativeWithIntrinsicBounds(
                mDrawables[0],
                mDrawables[1],
                visibility ? mClearButtonDrawable : null,
                mDrawables[3]);
    }
}
