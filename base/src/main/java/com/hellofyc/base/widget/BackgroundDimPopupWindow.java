package com.hellofyc.base.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.FloatRange;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created on 2016/1/6.
 *
 * @author Yucun Fang
 */
public class BackgroundDimPopupWindow extends PopupWindow {

    private Context mContext;

    public BackgroundDimPopupWindow(Context context) {
        super(context);
        init(context);
    }

    public BackgroundDimPopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BackgroundDimPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public BackgroundDimPopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public BackgroundDimPopupWindow(View contentView) {
        super(contentView);
        init(contentView.getContext());
    }

    public BackgroundDimPopupWindow(View contentView, int width, int height) {
        super(contentView, width, height);
        init(contentView.getContext());
    }

    public BackgroundDimPopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
        init(contentView.getContext());
    }

    private void init(Context context) {
        mContext = context;
        setBackgroundDimEnabled(true);
    }

    private void setBackgroundDimEnabled(boolean enabled) {
        if (enabled) {
            setBackgroundDimAmount(0.5f);
            setOutsideTouchable(false);
            setFocusable(true);
            setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), (Bitmap) null));
        } else {
            setBackgroundDimAmount(1.0f);
        }
    }

    /**
     * Set Background Dim Value, 1.0 means fully opaque and 0.0 means fully transparent;
     *
     * @param dimAmount dim value
     */
    public void setBackgroundDimAmount(@FloatRange(from=0.0, to=1.0) float dimAmount) {
        if (mContext instanceof Activity) {
            Activity activity = (Activity) mContext;
            WindowManager.LayoutParams params = activity.getWindow().getAttributes();
            if (params != null) {
                params.alpha = dimAmount;
                activity.getWindow().setAttributes(params);
            }
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        setBackgroundDimEnabled(false);
    }
}
