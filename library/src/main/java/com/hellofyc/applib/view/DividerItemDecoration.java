/*
 *
 *  * Copyright (C) 2015 Jason Fang ( ifangyucun@gmail.com )
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */package com.hellofyc.applib.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hellofyc.applib.util.CollectionUtils;


/**
 * Recycler分隔符
 * Create on 2014年11月24日 下午3:33:33
 * @author Jason Fang
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL = LinearLayoutManager.VERTICAL;

    private int mDividerHeight = 1;

    private Context mContext;
    private int mOrientation;
    private Paint mPaint;
    
    private int[] mExceptDividerPosition;

    public DividerItemDecoration(Context context, int orientation) {
    	mContext = context;
    	mOrientation = orientation;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }
    
    public void setDividerColor(int color) {
    	mPaint.setColor(color);
    }
    
    public void setDividerColorResource(Context context, @ColorRes int id) {
    	setDividerColor(ContextCompat.getColor(context, id));
    }
    
    public void setDividerDrawable(Drawable drawable) {
    	
    }
    
    public void setExceptDividerPositions(int... position) {
    	mExceptDividerPosition = position;
    }
    
    public void setDivierHeight(int height) {
    	mDividerHeight = height;
    }
    
    public Resources getResources() {
    	return mContext.getResources();
    }
    
    public void drawVertical(Canvas canvas, RecyclerView parent) {
    	final int left = parent.getPaddingLeft() ;
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight() ;
        final int childSize = parent.getChildCount();
        for(int i=0; i<childSize; i++) {
        	if (CollectionUtils.isContain(mExceptDividerPosition, i)) continue;
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)child.getLayoutParams();
            final int top = child.getBottom() + layoutParams.bottomMargin ;
            final int bottom = top + mDividerHeight ;
            canvas.drawRect(left, top, right, bottom, mPaint);
        }
    }

    public void drawHorizontal(Canvas canvas, RecyclerView parent) {
    	final int top = parent.getPaddingTop() ;
        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom() ;
        final int childSize = parent.getChildCount();
        for(int i=0 ; i<childSize; i++) {
            final View child = parent.getChildAt( i );
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)child.getLayoutParams();
            final int left = child.getRight() + layoutParams.rightMargin ;
            final int right = left + mDividerHeight;
            canvas.drawRect(left, top, right, bottom, mPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    	if (mOrientation == LinearLayoutManager.VERTICAL) {
            outRect.set(0, 0, 0, mDividerHeight);
        } else {
            outRect.set(0, 0, mDividerHeight, 0);
        }
    }
}
