/*
 *  Copyright (C) 2012-2015 Jason Fang ( ifangyucun@gmail.com )
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.hellofyc.base.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.view.ViewCompat;
import android.text.Html;
import android.text.TextPaint;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Create on 2014年12月6日 下午12:34:35
 *
 * @author Jason Fang
 */
public final class ViewUtils {
	private static final boolean DEBUG = false;

	/**
	 * Set the view alpha gradient
	 */
	public static void setVisibility(View view, boolean visibility) {
		if (Build.VERSION.SDK_INT < 11) {
			view.setVisibility(visibility ? View.VISIBLE : View.GONE);
			return;
		}
		
		if ((ViewCompat.getAlpha(view) == 1.0 && visibility)
				|| ViewCompat.getAlpha(view) == 0 && !visibility) return;
		
		if (sHandler.hasMessages(MSG_VISIBILITY)) return;
		
		if (DEBUG) FLog.i("begin alpha!");
		Message msg = sHandler.obtainMessage(MSG_VISIBILITY);
		msg.obj = view;
		msg.arg1 = visibility ? 1 : 0;
		sHandler.sendMessage(msg);
	}

    /**
     * set view height
     *
     * @param view view
     * @param height height
     */
    public static void setViewHeight(View view, int height) {
        if (view == null) {
            return;
        }

        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
    }

    /**
     * set SearchView OnClickListener
     */
    public static void setSearchViewOnClickListener(View v, OnClickListener listener) {
        if (v instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)v;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = group.getChildAt(i);
                if (child instanceof LinearLayout || child instanceof RelativeLayout) {
                    setSearchViewOnClickListener(child, listener);
                }

                if (child instanceof TextView) {
                    TextView text = (TextView)child;
                    text.setFocusable(false);
                }
                child.setOnClickListener(listener);
            }
        }
    }

    /**
     * 获取ActionBar的高度
     */
    public static int getActionBarHeight(Context context) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return 0;
    }

    @TargetApi(11)
	public static void setActionBarTranslation(Context context, float y) {
        int actionBarHeight = getActionBarHeight(context);

        ViewGroup content = ((ViewGroup)((Activity)context).findViewById(android.R.id.content).getParent());
        int children = content.getChildCount();
        for (int i = 0; i < children; i++) {
            View child = content.getChildAt(i);
            if (child.getId() != android.R.id.content) {
                if (y <= -actionBarHeight) {
                    child.setVisibility(View.GONE);
                } else {
                    child.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        child.setTranslationY(y);
                    }
                }
            }
        }
    }

	/**
	 * 开启跑马灯效果
	 */
	public static void setMarqueeEnabled(TextView textView) {
		textView.setSelected(true);
		textView.setSingleLine(true);
		textView.setMarqueeRepeatLimit(-1);
		textView.setEllipsize(TruncateAt.MARQUEE);
	}

	/**
	 * 获取字宽
	 */
	public static float getTextWidth(String text, float size) {
        TextPaint fontPaint = new TextPaint();
        fontPaint.setTextSize(size);
        return fontPaint.measureText(text);
    }

	/**
	 * text underline
	 */
	public static void setTextUnderline(TextView textView, String text) {
		textView.setText(Html.fromHtml("<u>" + text + "</u>"));
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	static void blur(Context context, View view) {
		view.buildDrawingCache();
		Bitmap srcBitmap = view.getDrawingCache();
		float radius = 20f;

		Bitmap overlay = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(overlay);
		canvas.translate(-view.getLeft(), -view.getTop());
		canvas.drawBitmap(srcBitmap, 0, 0, null);

		RenderScript script = RenderScript.create(context);
		Allocation allocation = Allocation.createFromBitmap(script, overlay);
		ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(script, allocation.getElement());
		blur.setInput(allocation);
		blur.setRadius(radius);
		blur.forEach(allocation);
		allocation.copyTo(overlay);

		view.setBackground(new BitmapDrawable(context.getResources(), overlay));

		script.destroy();
	}

	private static final int MSG_VISIBILITY = 1;
	private static final int MSG_PROGRESS_ANIM = 2;

	static final Handler sHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_VISIBILITY:
                    handleVisiblityMessage(msg);
                    break;
                case MSG_PROGRESS_ANIM:
                    handleVisiblityMessage(msg);
                    break;
            }
            return false;
        }
    });

	static void handleVisiblityMessage(Message msg) {
		View view = (View) msg.obj;
		boolean visibility = msg.arg1 == 1;
		if (visibility) {
			view.setVisibility(View.VISIBLE);
			if (ViewCompat.getAlpha(view) >= 1.0) {
				sHandler.removeMessages(MSG_VISIBILITY);
				return;
			}
			if (DEBUG) FLog.i("add alpha!" + ViewCompat.getAlpha(view));
			ViewCompat.setAlpha(view, ViewCompat.getAlpha(view) + 0.05f);
		} else {
			if (ViewCompat.getAlpha(view) <= 0.0) {
				view.setVisibility(View.GONE);
				sHandler.removeMessages(MSG_VISIBILITY);
				return;
			}
			if (DEBUG) FLog.i("minus alpha!" + ViewCompat.getAlpha(view));
			ViewCompat.setAlpha(view, ViewCompat.getAlpha(view) - 0.05f);
		}
		
		Message message = sHandler.obtainMessage(MSG_VISIBILITY);
		message.obj = view;
		message.arg1 = msg.arg1;
		sHandler.sendMessageDelayed(message, 20);
	}
	
}
