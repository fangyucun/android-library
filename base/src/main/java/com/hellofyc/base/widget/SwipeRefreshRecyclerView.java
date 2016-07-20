package com.hellofyc.base.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created on 2016/5/11.
 *
 * @author Yucun Fang
 */
public class SwipeRefreshRecyclerView extends SwipeRefreshLayout {

    private RecyclerView mRecyclerView;
    private EmptyView mEmptyView;
    private OnLoadMoreListener mLoadMoreListener;

    private enum Status {
        NONE,

        REFRESHING,

        LOADING,
    }

    private Status mStatus = Status.NONE;
    private boolean mIsSupportLoadMore = true;

    public SwipeRefreshRecyclerView(Context context) {
        super(context);
        init();
    }

    public SwipeRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mRecyclerView = new RecyclerView(getContext());

        mEmptyView = new EmptyView(getContext());
        addView(mRecyclerView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(mEmptyView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mRecyclerView.addOnScrollListener(new PageScrollListener());
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new DataChangedObserver());
    }

    public void setSupportLoadMore(boolean isSupport) {
        mIsSupportLoadMore = isSupport;
    }

    public void setSupportRefresh(boolean isSupport) {
        setEnabled(isSupport);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public EmptyView getEmptyView() {
        return mEmptyView;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mIsSupportLoadMore = true;
        mLoadMoreListener = listener;
    }

    public void setLoadMoreFinish() {
        mStatus = Status.NONE;
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        super.setRefreshing(refreshing);
        mStatus = refreshing ? Status.REFRESHING : Status.NONE;
    }

    public RecyclerView.Adapter getAdapter() {
        return mRecyclerView.getAdapter();
    }

    public void addEmptyViewStatus(int status, Drawable icon, CharSequence text, EmptyView.OnStatusClickListener listener) {
        mEmptyView.addStatus(status, icon, text, listener);
    }

    private void setupList() {
        if (getAdapter() == null) {
            throw new IllegalArgumentException("Adapter is null!");
        }
        if (getAdapter().getItemCount() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    public void setEmptyViewStatus(int status) {
        setupList();
        mEmptyView.show(status);
    }

    class PageScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            dispatchScrolled(recyclerView, dx, dy);
        }
    }

    private void dispatchScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (mIsSupportLoadMore && (dx !=0 || dy != 0)) {
            doLoadMore(recyclerView);
        }
    }

    private void doLoadMore(RecyclerView recyclerView) {
        int lastVisiblePosition;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            lastVisiblePosition = gridLayoutManager.findLastVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            //                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            //                lastVisiblePosition = staggeredGridLayoutManager.findLastVisibleItemPositions();
            return;
        } else {
            return;
        }

        if (lastVisiblePosition == recyclerView.getAdapter().getItemCount() - 1) {
            if (mStatus == Status.NONE) {
                mStatus = Status.LOADING;
                if (mLoadMoreListener != null) {
                    mLoadMoreListener.onLoadMore();
                }
            }
        }
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = resolveSize(getMeasuredWidth(), widthMeasureSpec);
        int height = resolveSize(getMeasuredHeight(), heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        FLog.i("onDraw");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    class DataChangedObserver extends RecyclerView.AdapterDataObserver {

        @Override
        public void onChanged() {
            super.onChanged();
            setupList();
            mStatus = Status.NONE;
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            setupList();
            mStatus = Status.NONE;
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            setupList();
            mStatus = Status.NONE;
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            setupList();
            mStatus = Status.NONE;
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            setupList();
            mStatus = Status.NONE;
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            setupList();
            mStatus = Status.NONE;
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
