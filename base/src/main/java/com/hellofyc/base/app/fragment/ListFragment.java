//package com.hellofyc.base.app.fragment;
//
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.DrawableRes;
//import android.support.annotation.Nullable;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.RecyclerView;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.AnimationUtils;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.hellofyc.base.R;
//import com.hellofyc.base.util.ParseUtils;
//import com.hellofyc.base.widget.EmptyView;
//
///**
// * Created on 2016/5/3.
// *
// * @author Yucun Fang
// */
//public class ListFragment extends BaseFragment {
//
//    static final int INTERNAL_EMPTY_ID               = 0x00ff0001;
//    static final int INTERNAL_PROGRESS_CONTAINER_ID  = 0x00ff0002;
//    static final int INTERNAL_RECYCLER_VIEW_ID       = 0x00ff0003;
//    static final int INTERNAL_REFRESH_LAYOUT_ID      = 0x00ff0004;
//
////    final private Handler mHandler = new Handler();
//
////    final private Runnable mRequestFocus = new Runnable() {
////
////        public void run() {
////            mList.focusableViewAvailable(mList);
////        }
////
////    };
//
//    private SwipeRefreshLayout mSwipeRefreshLayout;
//    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager mLayoutManager;
//    private RecyclerView mList;
//    private View mProgressContainer;
//    private View mListContainer;
//    private EmptyView mEmptyView;
//    private boolean mListShown;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        final Context context = getActivity();
//
//        FrameLayout root = new FrameLayout(context);
//
//        FrameLayout lframe = new FrameLayout(context);
//        lframe.setId(R.id.root);
//
//        //SwipeRefreshLayout
//        SwipeRefreshLayout swipeRefreshLayout = new SwipeRefreshLayout(getActivity());
//        swipeRefreshLayout.setId(R.id.swipe_refresh_layout);
//
//        mList = new RecyclerView(getActivity());
//
//        swipeRefreshLayout.addView(mList, new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//
//        lframe.addView(swipeRefreshLayout, new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//
//        root.addView(lframe, new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//
//        EmptyView emptyView = new EmptyView(context);
//        emptyView.setId(R.id.empty_view);
//
//        root.addView(emptyView, new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//
//        root.setLayoutParams(new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//
//        return root;
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        ensureList();
//    }
//
//    public void onListItemClick(RecyclerView recyclerView, View itemView, int position, long id) {
//    }
//
//    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
//        mLayoutManager = layoutManager;
//    }
//
//    /**
//     * Provide the cursor for the list view.
//     */
//    public void setListAdapter(RecyclerView.Adapter adapter) {
//        boolean hadAdapter = mAdapter != null;
//        mAdapter = adapter;
//        if (mList != null) {
//            mList.setAdapter(adapter);
//            if (!mListShown && !hadAdapter) {
//                // The list was hidden, and previously didn't have an
//                // adapter.  It is now time to show it.
//                setListShown(true, getView() != null && getView().getWindowToken() != null);
//            }
//        }
//    }
//
//
//    public RecyclerView getListView() {
//        ensureList();
//        return mList;
//    }
//
//    public void setEmptyText(CharSequence text) {
////        ensureList();
////        mEmptyView.setText(text);
////        if (mEmptyView == null) {
////            throw new IllegalStateException("Can't be used with a custom content view");
////        }
////        mStandardEmptyView.setText(text);
////        if (mEmptyText == null) {
////            mList.setEmptyView(mStandardEmptyView);
////        }
////        mEmptyText = text;
//    }
//
//    public void setListShown(boolean shown) {
//        setListShown(shown, true);
//    }
//
//    /**
//     * Like {@link #setListShown(boolean)}, but no animation is used when
//     * transitioning from the previous state.
//     */
//    public void setListShownNoAnimation(boolean shown) {
//        setListShown(shown, false);
//    }
//
//    private void setListShown(boolean shown, boolean animate) {
//        ensureList();
//        if (mProgressContainer == null) {
//            throw new IllegalStateException("Can't be used with a custom content view");
//        }
//        if (mListShown == shown) {
//            return;
//        }
//        mListShown = shown;
//        if (shown) {
//            if (animate) {
//                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
//                        getActivity(), android.R.anim.fade_out));
//                mListContainer.startAnimation(AnimationUtils.loadAnimation(
//                        getActivity(), android.R.anim.fade_in));
//            } else {
//                mProgressContainer.clearAnimation();
//                mListContainer.clearAnimation();
//            }
//            mProgressContainer.setVisibility(View.GONE);
//            mListContainer.setVisibility(View.VISIBLE);
//        } else {
//            if (animate) {
//                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
//                        getActivity(), android.R.anim.fade_in));
//                mListContainer.startAnimation(AnimationUtils.loadAnimation(
//                        getActivity(), android.R.anim.fade_out));
//            } else {
//                mProgressContainer.clearAnimation();
//                mListContainer.clearAnimation();
//            }
//            mProgressContainer.setVisibility(View.VISIBLE);
//            mListContainer.setVisibility(View.GONE);
//        }
//    }
//
//    private void ensureList() {
//        if (mList != null) {
//            return;
//        }
//        View root = getView();
//        if (root == null) {
//            throw new IllegalStateException("Content view not yet created");
//        }
//        if (root instanceof RecyclerView) {
//            mList = (RecyclerView)root;
//        } else {
//            mEmptyView = (TextView)root.findViewById(INTERNAL_EMPTY_ID);
//            if (mEmptyView == null) {
//                mEmptyView = root.findViewById(R.id.empty);
//            } else {
//                mEmptyView.setVisibility(View.GONE);
//            }
//            mProgressContainer = root.findViewById(INTERNAL_PROGRESS_CONTAINER_ID);
//            mListContainer = root.findViewById(INTERNAL_RECYCLER_VIEW_ID);
//            View rawListView = root.findViewById(R.id.recycler_view);
//            if (!(rawListView instanceof RecyclerView)) {
//                if (rawListView == null) {
//                    throw new RuntimeException(
//                            "Your content must have a RecyclerView whose id attribute is " +
//                                    "'R.id.recycler_view'");
//                }
//                throw new RuntimeException(
//                        "Content has view with id attribute 'android.R.id.list' "
//                                + "that is not a ListView class");
//            }
//            mList = (RecyclerView) rawListView;
//            if (mEmptyView != null) {
//                mList.setEmptyView(mEmptyView);
//            } else if (mEmptyText != null) {
//                mStandardEmptyView.setText(mEmptyText);
//                mList.setEmptyView(mStandardEmptyView);
//            }
//        }
//        mListShown = true;
//        mList.setOnItemClickListener(mOnClickListener);
//        if (mAdapter != null) {
//            RecyclerView.Adapter adapter = mAdapter;
//            mAdapter = null;
//            setListAdapter(adapter);
//        } else {
//            // We are starting without an adapter, so assume we won't
//            // have our data right away and start with the progress indicator.
//            if (mProgressContainer != null) {
//                setListShown(false, false);
//            }
//        }
////        mHandler.post(mRequestFocus);
//    }
//
//    @Override
//    public void onDestroyView() {
////        mHandler.removeCallbacks(mRequestFocus);
//        mList = null;
//        mListShown = false;
//        mEmptyView = mProgressContainer = mListContainer = null;
//        mStandardEmptyView = null;
//        super.onDestroyView();
//    }
//}
