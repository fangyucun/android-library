package com.hellofyc.base.view.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hellofyc.base.utils.ParseUtils;

/**
 * Created on 2016/2/1.
 *
 * @author Yucun Fang
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int mLeftSpace, mTopSpace, mRightSpace, mBottomSpace;

    /**
     * @param leftSpace left space unit is dp
     * @param topSpace top space unit is dp
     * @param rightSpace right space unit is dp
     * @param bottomSpace bottom space unit is dp
     */
    public SpaceItemDecoration(int leftSpace, int topSpace, int rightSpace, int bottomSpace) {
        mLeftSpace = leftSpace;
        mTopSpace = topSpace;
        mRightSpace = rightSpace;
        mBottomSpace = bottomSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(ParseUtils.dpToPx(parent.getContext(), mLeftSpace),
                ParseUtils.dpToPx(parent.getContext(), mTopSpace),
                ParseUtils.dpToPx(parent.getContext(), mRightSpace),
                ParseUtils.dpToPx(parent.getContext(), mBottomSpace));
    }

}
