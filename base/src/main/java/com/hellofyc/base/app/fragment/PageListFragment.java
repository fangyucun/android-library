package com.hellofyc.base.app.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hellofyc.base.R;
import com.hellofyc.base.app.adapter.BaseRecyclerViewAdapter;
import com.hellofyc.base.app.loader.PageListLoader;
import com.hellofyc.base.app.loader.PageListResult;
import com.hellofyc.base.model.PageInfo;
import com.hellofyc.base.widget.ListRecyclerView;
import com.hellofyc.base.widget.SwipeRefreshView;

import java.util.List;

/**
 * Created on 2016/2/3.
 *
 * @author Yucun Fang
 */
public abstract class PageListFragment<D> extends BaseFragment implements SwipeRefreshView.OnSwipeRefreshListener,
        ListRecyclerView.OnLoadMoreListener {

    private static final int LOADER_ID_REFRESH  = 10001;
    private static final int LOADER_ID_LOADMORE = 10002;

    private SwipeRefreshView mSwipeRefreshView;
    private ListRecyclerView mRecyclerView;
    private ImageView mEmptyView;
    private DataObserver mDataObserver;
    private Bundle mRequestParams = new Bundle();
    private PageInfo mPageInfo = new PageInfo();

    public abstract int getContentViewResId();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getContentViewResId(), container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mSwipeRefreshView = (SwipeRefreshView) findViewById(R.id.swipe_refresh_view);
        mRecyclerView = (ListRecyclerView) findViewById(R.id.list_recyclerview);
        mEmptyView = (ImageView) findViewById(R.id.empty);

        mSwipeRefreshView.setOnSwipeRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);

        new Handler().post(new Runnable() {

            @Override
            public void run() {
                getRequestParams(mRequestParams);
                mRequestParams.putParcelable("page_info", mPageInfo);
                getLoaderManager().initLoader(LOADER_ID_REFRESH, mRequestParams, new NewDataCallbacks());
            }
        });

    }

    public void setListAdapter(BaseRecyclerViewAdapter<D> adapter) {
        mRecyclerView.setAdapter(adapter);
        if (mDataObserver == null) {
            mDataObserver = new DataObserver();
        }
        mRecyclerView.getAdapter().registerAdapterDataObserver(mDataObserver);
        setupList();
    }

    @SuppressWarnings("unchecked")
    public BaseRecyclerViewAdapter<D> getListAdapter() {
        return (BaseRecyclerViewAdapter<D>) mRecyclerView.getAdapter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecyclerView.getAdapter().unregisterAdapterDataObserver(mDataObserver);
    }

    private void setupList() {
        if (mRecyclerView.getAdapter().getItemCount() <= 0) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    public void setEmptyImage(Drawable drawable) {
        mEmptyView.setImageDrawable(drawable);
    }

    private void setSwipeEnabled(boolean enabled) {
        mSwipeRefreshView.setEnabled(enabled);
    }

    public SwipeRefreshView getSwipeRefreshView() {
        return mSwipeRefreshView;
    }

    public ListRecyclerView getListRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }


    public void getRequestParams(Bundle args){}
    public abstract PageListLoader<D> onLoaderCreate(Bundle args);
    public void onLoadFinish(boolean isRefresh, List<D> data){}

    @Override
    public void onLoadMore() {
        if (mPageInfo.getTotalPage() != 0 && mPageInfo.getCurrentPage() >= mPageInfo.getTotalPage()) {
            getListRecyclerView().setLoadMoreStatus(ListRecyclerView.LoadMoreStatus.NONE);
            return;
        }

        getRequestParams(mRequestParams);
        mRequestParams.putParcelable("page_info", mPageInfo);
        getLoaderManager().restartLoader(LOADER_ID_LOADMORE, mRequestParams, new LoaderManager.LoaderCallbacks<PageListResult<D>>() {

            @Override
            public Loader<PageListResult<D>> onCreateLoader(int id, Bundle args) {
                return PageListFragment.this.onLoaderCreate(args);
            }

            @Override
            public void onLoadFinished(Loader<PageListResult<D>> loader, PageListResult<D> data) {
                mPageInfo = data.getPageInfo();
                getListAdapter().addItems(data.getValue());
                onLoadFinish(false, data.getValue());
            }

            @Override
            public void onLoaderReset(Loader<PageListResult<D>> loader) {

            }
        });
    }

    @Override
    public void onSwipeRefresh() {
        getRequestParams(mRequestParams);
        mRequestParams.putParcelable("page_info", mPageInfo);
        getLoaderManager().restartLoader(LOADER_ID_REFRESH, mRequestParams, new NewDataCallbacks());
    }

    class NewDataCallbacks implements LoaderManager.LoaderCallbacks<PageListResult<D>> {

        @Override
        public Loader<PageListResult<D>> onCreateLoader(int id, Bundle args) {
            mPageInfo.setCurrentPage(0);
            return PageListFragment.this.onLoaderCreate(args);
        }

        @Override
        public void onLoadFinished(Loader<PageListResult<D>> loader, PageListResult<D> data) {
            mPageInfo = data.getPageInfo();
            onLoadFinish(true, data.getValue());
            getListAdapter().updateItems(data.getValue());
        }

        @Override
        public void onLoaderReset(Loader<PageListResult<D>> loader) {

        }
    }

    class DataObserver extends RecyclerView.AdapterDataObserver {

        @Override
        public void onChanged() {
            super.onChanged();
            setupList();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            setupList();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            setupList();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            setupList();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            setupList();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            setupList();
        }
    }
}
