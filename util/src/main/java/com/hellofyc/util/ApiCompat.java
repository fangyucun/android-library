/*
 * Copyright (C) 2014 Jason Fang ( ijasonfang@gmail.com )
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hellofyc.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

/**
 * Create on 2015年1月9日 上午11:28:48
 * @author Jason Fang
 */
public class ApiCompat {
	static final boolean DEBUG = false;

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static boolean isLayoutRtl(View view) {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
                && view.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
	}
	
	/**
	 * Set the status bar translucent
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static boolean setTranslucentStatus(Activity a, boolean on) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return false;
		if (a == null) {
			throw new RuntimeException("activity cannot be null!");
		}
		Window window = a.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		if (on) {
			params.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		} else {
			params.flags &= ~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		}
		window.setAttributes(params);
		return true;
	}

	/**
	 * 导航栏透明
	 */
    @TargetApi(Build.VERSION_CODES.KITKAT)
	public static boolean setTranslucentNavigation(Activity a, boolean on) {
    	if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return false;
		if (a == null) {
			throw new RuntimeException("activity cannot be null!");
		}
        Window win = a.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
        if (on) {
            winParams.flags |=  bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
        return true;
    }
	
	/**
	 * Know layout direction;
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static int getLayoutDirection(Configuration config) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			return config.getLayoutDirection();
		}
		return View.LAYOUT_DIRECTION_LTR;
	}
	
    /**
     * @see View#setLayoutDirection(int)
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static void setLayoutDirection(View view, int layoutDirection) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            view.setLayoutDirection(layoutDirection);
        }
    }
	
    /**
     * @return True if the running version of the Android supports printing.
     */
    public static boolean isPrintingSupported() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }
    
    /**
     * @return True if the running version of the Android supports HTML clipboard.
     */
    public static boolean isHTMLClipboardSupported() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * @see View#setTextDirection(int)
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static void setTextAlignment(View view, int textAlignment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            view.setTextAlignment(textAlignment);
        }
    }

    /**
     * @see MarginLayoutParams#setMarginStart(int)
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static void setMarginStart(MarginLayoutParams layoutParams, int start) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.setMarginStart(start);
        } else {
            layoutParams.leftMargin = start;
        }
    }

    /**
     * @see MarginLayoutParams#getMarginStart()
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static int getMarginStart(MarginLayoutParams layoutParams) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return layoutParams.getMarginStart();
        } else {
            return layoutParams.leftMargin;
        }
    }

    /**
     * @see MarginLayoutParams#setMarginEnd(int)
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static void setMarginEnd(MarginLayoutParams layoutParams, int end) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.setMarginEnd(end);
        } else {
            layoutParams.rightMargin = end;
        }
    }

    /**
     * @see MarginLayoutParams#getMarginEnd()
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static int getMarginEnd(MarginLayoutParams layoutParams) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return layoutParams.getMarginEnd();
        } else {
            return layoutParams.rightMargin;
        }
    }

    /**
     * @see View#setPaddingRelative(int, int, int, int)
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static void setPaddingRelative(View view, int start, int top, int end, int bottom) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            view.setPaddingRelative(start, top, end, bottom);
        } else {
            view.setPadding(start, top, end, bottom);
        }
    }

    /**
     * @see View#getPaddingStart()
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static int getPaddingStart(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return view.getPaddingStart();
        } else {
            return view.getPaddingLeft();
        }
    }

    /**
     * @see View#getPaddingEnd()
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static int getPaddingEnd(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return view.getPaddingEnd();
        } else {
            return view.getPaddingRight();
        }
    }

    /**
     * @see TextView#setCompoundDrawablesRelative(Drawable, Drawable, Drawable,
     *      Drawable)
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static void setCompoundDrawablesRelative(TextView textView, Drawable start, Drawable top,
            Drawable end, Drawable bottom) {
    	if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textView.setCompoundDrawablesRelative(start, top, end, bottom);
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1) {
        	boolean isRtl = isLayoutRtl(textView);
        	textView.setCompoundDrawables(isRtl ? end : start, top, isRtl ? start : end, bottom);
        } else {
        	textView.setCompoundDrawables(start, top, end, bottom);
        }
    }

    /**
     * @see TextView#setCompoundDrawablesRelativeWithIntrinsicBounds(Drawable,
     *      Drawable, Drawable, Drawable)
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static void setCompoundDrawablesRelativeWithIntrinsicBounds(TextView textView,
            Drawable start, Drawable top, Drawable end, Drawable bottom) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom);
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1){
        	boolean isRtl = isLayoutRtl(textView);
        	textView.setCompoundDrawablesWithIntrinsicBounds(isRtl ? end : start, top, isRtl ? start : end, bottom);
        } else {
        	textView.setCompoundDrawablesWithIntrinsicBounds(start, top, end, bottom);
        }
    }

    /**
     * @see TextView#setCompoundDrawablesRelativeWithIntrinsicBounds(int, int, int,
     *      int)
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static void setCompoundDrawablesRelativeWithIntrinsicBounds(TextView textView,
            int start, int top, int end, int bottom) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom);
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1) {
        	boolean isRtl = isLayoutRtl(textView);
        	textView.setCompoundDrawablesWithIntrinsicBounds(isRtl ? end : start, top, isRtl ? start : end, bottom);
        } else {
        	textView.setCompoundDrawablesWithIntrinsicBounds(start, top, end, bottom);
        }
    }

    /**
     * @see View#postInvalidateOnAnimation()
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public static void postInvalidateOnAnimation(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.postInvalidateOnAnimation();
        } else {
            view.postInvalidate();
        }
    }

    /**
     * @see RemoteViews#setContentDescription(int, CharSequence)
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
	public static void setContentDescriptionForRemoteView(RemoteViews remoteViews, int viewId,
            CharSequence contentDescription) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            remoteViews.setContentDescription(viewId, contentDescription);
        }
    }

    /**
     * @see View#setBackground(Drawable)
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressWarnings("deprecation")
    public static void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    /**
     * @see ViewTreeObserver#removeOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener)
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressWarnings("deprecation")
    public static void removeOnGlobalLayoutListener (View view, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        } else {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        }
    }

    /**
     * @see ImageView#setImageAlpha(int)
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressWarnings("deprecation")
    public static void setImageAlpha(ImageView iv, int alpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            iv.setImageAlpha(alpha);
        } else {
            iv.setAlpha(alpha);
        }
    }

    /**
     * @see PendingIntent#getCreatorPackage()
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	@SuppressWarnings("deprecation")
    public static String getCreatorPackage(PendingIntent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return intent.getCreatorPackage();
        } else {
            return intent.getTargetPackage();
        }
    }
    
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static boolean setFinishOnTouchOutside(Activity activity, boolean finish) {
		if (Build.VERSION.SDK_INT >= 11) {
			activity.setFinishOnTouchOutside(finish);
			return true;
		} else {
			return false;
		}
	}
	
	private ApiCompat() {/*Do not new me!*/}
}
