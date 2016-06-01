package com.hellofyc.base.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hellofyc.base.R;

/**
 * Created on 2016/5/9.
 *
 * @author Yucun Fang
 */
@SuppressWarnings("ResourceType")
public class EmptyView extends FrameLayout implements View.OnClickListener {

    public static final int STATUS_NO_DATA      = 1;
    public static final int STATUS_NO_NETWORK   = 2;
    private static final int STATUS_LOADING     = 3;

    private ArrayMap<Integer, ViewValue> mStatusMap = new ArrayMap<>();

    private LinearLayout mTipsView;
    private ImageView mIconView;
    private TextView mTextView;
    private ProgressBar mProgressBar;

    private int mStatus = STATUS_LOADING;

    public EmptyView(Context context) {
        this(context, null);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mStatusMap.put(STATUS_NO_DATA, new ViewValue(ContextCompat.getDrawable(getContext(),
                R.drawable.base_ic_no_data),
                getResources().getString(R.string.base_no_data), null));
        mStatusMap.put(STATUS_NO_NETWORK, new ViewValue(ContextCompat.getDrawable(getContext(),
                R.drawable.base_ic_no_network),
                getResources().getString(R.string.base_no_network), null));
        mStatusMap.put(STATUS_LOADING, null);

        View root = getLayoutInflater().inflate(R.layout.base_empty_view, this, false);
        mTipsView = (LinearLayout) root.findViewById(R.id.empty_text_container);
        mIconView = (ImageView) root.findViewById(R.id.icon);
        mTextView = (TextView) root.findViewById(R.id.text);
        mProgressBar = (ProgressBar) root.findViewById(R.id.progress_bar);

        addView(root);
        mTipsView.setOnClickListener(this);
    }

    private LayoutInflater getLayoutInflater() {
        return (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addStatus(int status, Drawable icon, CharSequence text, OnStatusClickListener listener) {
        if (mStatusMap == null) {
            mStatusMap = new ArrayMap<>();
        }
        mStatusMap.put(status, new ViewValue(icon, text, listener));
    }

    public void show(int status) {
        if (!mStatusMap.containsKey(status)) {
            throw new IllegalArgumentException("Not support this status!");
        }
        mStatus = status;
        switch (status) {
            case STATUS_LOADING:
                setViewVisibility(mProgressBar, true);
                setViewVisibility(mTipsView, false);
                break;
            default:
                setViewVisibility(mTipsView, true);
                setViewVisibility(mProgressBar, false);

                Drawable drawable = mStatusMap.get(status).mDrawable;
                if (drawable != null) {
                    setViewVisibility(mIconView, true);
                    mIconView.setImageDrawable(drawable);
                } else {
                    setViewVisibility(mIconView, false);
                }

                CharSequence text = mStatusMap.get(status).mText;
                if (!TextUtils.isEmpty(text)) {
                    setViewVisibility(mTextView, true);
                    mTextView.setText(text);
                } else {
                    setViewVisibility(mTextView, false);
                }
        }
    }

    private void setViewVisibility(@NonNull View view, boolean visibility) {
        view.setVisibility(visibility ? VISIBLE : GONE);
    }

    @Override
    public void onClick(View v) {
        if (mStatusMap.get(mStatus) != null) {
            if (mStatusMap.get(mStatus).mListener != null) {
                mStatusMap.get(mStatus).mListener.onStatusClick(mStatus);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = resolveSize(getWidth(), widthMeasureSpec);
        int height = resolveSize(getHeight(), heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    static class ViewValue {
        Drawable mDrawable;
        CharSequence mText;
        OnStatusClickListener mListener;

        ViewValue(Drawable drawable, CharSequence text, OnStatusClickListener listener) {
            mDrawable = drawable;
            mText = text;
            mListener = listener;
        }
    }

    public interface OnStatusClickListener {
        void onStatusClick(int status);
    }
}
