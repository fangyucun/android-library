package com.hellofyc.base.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created on 2016/1/22.
 *
 * @author Yucun Fang
 */
public class SwipeRefreshView extends SwipeRefreshLayout implements SwipeRefreshLayout.OnRefreshListener {

    private OnSwipeRefreshListener mSwipeRefreshListener;
    private ListRecyclerView mListRecyclerView;

    public SwipeRefreshView(Context context) {
        super(context);
        init();
    }

    public SwipeRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOnRefreshListener(this);
        findListRecyclerView();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findListRecyclerView();
    }

    private void findListRecyclerView() {
        int count = getChildCount();
        for (int i=0; i<count; i++) {
            View view = getChildAt(i);
            if (view instanceof ListRecyclerView) {
                mListRecyclerView = (ListRecyclerView) view;
            }
        }
    }

    private void setListRecyclerViewRefreshStatus(ListRecyclerView.RefreshStatus refreshStatus) {
        if (mListRecyclerView != null) {
            mListRecyclerView.setRefreshStatus(refreshStatus);
        }
    }

    @Override
    @Deprecated
    public void setOnRefreshListener(OnRefreshListener listener) {
        super.setOnRefreshListener(listener);
    }

    public void setOnSwipeRefreshListener(OnSwipeRefreshListener listener) {
        mSwipeRefreshListener = listener;
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        super.setRefreshing(refreshing);
        if (refreshing) {
            setListRecyclerViewRefreshStatus(ListRecyclerView.RefreshStatus.REFRESHING);
        } else {
            setListRecyclerViewRefreshStatus(ListRecyclerView.RefreshStatus.NONE);
        }
    }

    @Override
    @Deprecated
    public void onRefresh() {
        if (mListRecyclerView != null && mListRecyclerView.getLoadMoreStatus() == ListRecyclerView.LoadMoreStatus.LOADING) {
            setRefreshing(false);
            return;
        }

        setListRecyclerViewRefreshStatus(ListRecyclerView.RefreshStatus.REFRESHING);
        if (mSwipeRefreshListener != null) {
            mSwipeRefreshListener.onSwipeRefresh();
        }
    }

    public interface OnSwipeRefreshListener {
        void onSwipeRefresh();
    }
}
