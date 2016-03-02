package com.hellofyc.base.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created on 2016/1/22.
 *
 * @author Yucun Fang
 */
public class ListRecyclerView extends RecyclerView {

    private RefreshStatus  mRefreshStatus = RefreshStatus.NONE;
    private LoadMoreStatus  mLoadMoreStatus = LoadMoreStatus.NONE;

    private boolean mIsSupportLoadMore = true;
    private View mLoadMoreLayout;

    private SwipeRefreshView mSwipeRefreshView;
    private OnLoadMoreListener mLoadMoreListener;

    public enum LoadMoreStatus {
        NONE,
        LOADING,
        ERROR;
    }

    public enum RefreshStatus {
        NONE,
        REFRESHING,
        ERROR;
    }

    public ListRecyclerView(Context context) {
        super(context);
        init();
    }

    public ListRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new DataChangedObserver());
    }

    private void init() {
        addOnScrollListener(new PageScrollListener());
    }

    public void setSupportLoadMore(boolean isSupport) {
        mIsSupportLoadMore = isSupport;
    }

    public void setLoadMoreStatus(LoadMoreStatus loadMoreStatus) {
        mLoadMoreStatus = loadMoreStatus;
    }

    public LoadMoreStatus getLoadMoreStatus() {
        return mLoadMoreStatus;
    }

    public void setRefreshStatus(RefreshStatus refreshStatus) {
        mRefreshStatus = refreshStatus;
    }

    public void setLoadMoreLayout(View layout) {
        mLoadMoreLayout = layout;
    }

    private boolean isLoadingMore() {
        return mLoadMoreStatus == LoadMoreStatus.LOADING;
    }

    private boolean isRefreshing() {
        return mRefreshStatus == RefreshStatus.REFRESHING;
    }

    private void setSwipeRefreshing(boolean refreshing) {
        findSwipeRefreshView();
        if (mSwipeRefreshView != null) {
            mSwipeRefreshView.setRefreshing(refreshing);
        }
    }

    private void findSwipeRefreshView() {
        if (getParent() instanceof SwipeRefreshView) {
            mSwipeRefreshView = (SwipeRefreshView) getParent();
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mLoadMoreListener = listener;
    }

    class DataChangedObserver extends AdapterDataObserver {

        @Override
        public void onChanged() {
            super.onChanged();
            setLoadMoreStatus(LoadMoreStatus.NONE);
            setRefreshStatus(RefreshStatus.NONE);
            setSwipeRefreshing(false);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            setLoadMoreStatus(LoadMoreStatus.NONE);
            setRefreshStatus(RefreshStatus.NONE);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            setLoadMoreStatus(LoadMoreStatus.NONE);
            setRefreshStatus(RefreshStatus.NONE);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            setLoadMoreStatus(LoadMoreStatus.NONE);
            setRefreshStatus(RefreshStatus.NONE);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            setLoadMoreStatus(LoadMoreStatus.NONE);
            setRefreshStatus(RefreshStatus.NONE);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            setLoadMoreStatus(LoadMoreStatus.NONE);
            setRefreshStatus(RefreshStatus.NONE);
        }
    }

    class PageScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            doLoadMore(recyclerView);
        }
    }

    private void doLoadMore(RecyclerView recyclerView) {
        if (!mIsSupportLoadMore) return;

        int lastVisiblePosition;
        LayoutManager layoutManager = recyclerView.getLayoutManager();
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
            if (mLoadMoreStatus != LoadMoreStatus.LOADING && mRefreshStatus != RefreshStatus.REFRESHING) {
                mLoadMoreStatus = LoadMoreStatus.LOADING;
                if (mLoadMoreListener != null) {
                    mLoadMoreListener.onLoadMore();
                }
            }
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

}
