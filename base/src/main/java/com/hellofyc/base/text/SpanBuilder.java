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

package com.hellofyc.base.text;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.MaskFilterSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.TextView;

/**
 * Created on 2015年1月26日 下午9:24:13
 *
 * @author Jason Fang
 */
public class SpanBuilder {
	private static final boolean DEBUG = false;
	
	public static final int IMAGE_SPAN_ALIGN_BOTTOM = 0;
	public static final int IMAGE_SPAN_ALIGN_BASELINE = 1;
	
	public static final int STYLE_NORMAL = 0;
    public static final int STYLE_BOLD = 1;
    public static final int STYLE_ITALIC = 2;
    public static final int STYLE_BOLD_ITALIC = 3;
	
	private String mText;
	private SpannableStringBuilder mBuilder;
	
	private SpanBuilder(String text) {
		if (TextUtils.isEmpty(text)) {
			throw new IllegalArgumentException("text is null or empty!");
		}
		mText = text;
		mBuilder = new SpannableStringBuilder(mText);
	}

	public static SpanBuilder create(String text) {
		return new SpanBuilder(text);
	}
	
	/**
	 * @see AbsoluteSizeSpan#AbsoluteSizeSpan(int)
	 */
	public SpanBuilder setFontSize(String spanText, int fontSize) {
		return setFontSize(spanText, fontSize, true);
	}
	
	/**
	 * @see AbsoluteSizeSpan#AbsoluteSizeSpan(int, boolean)
	 */
	public SpanBuilder setFontSize(String spanText, int fontSize, boolean dp) {
		if (TextUtils.isEmpty(spanText)) return this;
		
		int start = mText.indexOf(spanText);
		if (start == -1) return this;
		AbsoluteSizeSpan fontSizeSpan = new AbsoluteSizeSpan(fontSize, dp);
		mBuilder.setSpan(fontSizeSpan, start, start + spanText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}
	
	/**
	 * @see RelativeSizeSpan#RelativeSizeSpan(float)
	 */
	public SpanBuilder setFontSizes(String spanText, float proportion) {
		if (TextUtils.isEmpty(spanText)) return this;
		
		int start = mText.indexOf(spanText);
		if (start == -1) return this;
		RelativeSizeSpan fontSizeSpan = new RelativeSizeSpan(proportion);
		mBuilder.setSpan(fontSizeSpan, start, start + spanText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}
	
	/**
	 * @see AlphaForegroundColorSpan#AlphaForegroundColorSpan(int)
	 */
	public SpanBuilder setTextColor(String spanText, int color) {
		if (TextUtils.isEmpty(spanText)) return this;
		
		int start = mText.indexOf(spanText);
		if (start == -1) return this;
        int end = start + spanText.length();
        if (start >= end) return this;
		AlphaForegroundColorSpan colorSpan = new AlphaForegroundColorSpan(color);
		mBuilder.setSpan(colorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}
	
	/**
	 * @see BackgroundColorSpan#BackgroundColorSpan(int)
	 */
	public SpanBuilder setBackgroundColor(String spanText, int color) {
		if (TextUtils.isEmpty(spanText)) return this;
		
		int start = mText.indexOf(spanText);
		if (start == -1) return this;
		BackgroundColorSpan colorSpan = new BackgroundColorSpan(color);
		mBuilder.setSpan(colorSpan, start, start + spanText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}
	
	/**
	 * @see UnderlineSpan#UnderlineSpan()
	 */
	public SpanBuilder setUnderline(String spanText) {
		if (TextUtils.isEmpty(spanText)) return this;
		
		int start = mText.indexOf(spanText);
		if (start == -1) return this;
		mBuilder.setSpan(new UnderlineSpan(), start, start + spanText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}

	public SpanBuilder setBold(String spanText) {
		if (TextUtils.isEmpty(spanText)) return this;

		int start = mText.indexOf(spanText);
		if (start == -1) return this;
		mBuilder.setSpan(new StyleSpan(Typeface.BOLD), start, start + spanText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}

	/**
	 * @see StrikethroughSpan#StrikethroughSpan()
	 */
	public SpanBuilder setStrikethrough(String spanText) {
		if (TextUtils.isEmpty(spanText)) return this;

		int start = mText.indexOf(spanText);
		if (start == -1) return this;
		mBuilder.setSpan(new StrikethroughSpan(), start, start + spanText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}
	
	/**
	 * @param style {@link #STYLE_NORMAL} {@link #STYLE_BOLD} {@link #STYLE_ITALIC} {@link #STYLE_BOLD_ITALIC}
	 *
	 * @see StyleSpan#StyleSpan(int)
	 */
	public SpanBuilder setTextStyle(String spanText, int style) {
		if (TextUtils.isEmpty(spanText)) return this;

		int start = mText.indexOf(spanText);
		if (start == -1) return this;
		mBuilder.setSpan(new StyleSpan(style), start, start + spanText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}

	/**
	 * @see SubscriptSpan#SubscriptSpan()
	 */
	public SpanBuilder setSubscript(String spanText) {
		if (TextUtils.isEmpty(spanText)) return this;

		int start = mText.indexOf(spanText);
		if (start == -1) return this;
		mBuilder.setSpan(new SubscriptSpan(), start, start + spanText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}
	
	/**
	 * @see SuperscriptSpan#SuperscriptSpan()
	 */
	public SpanBuilder setSuperscript(String spanText) {
		if (TextUtils.isEmpty(spanText)) return this;
		
		int start = mText.indexOf(spanText);
		if (start == -1) return this;
		mBuilder.setSpan(new SuperscriptSpan(), start, start + spanText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}
	
	/**
	 * @see URLSpan#URLSpan(String)
	 */
	public SpanBuilder setURL(String spanText, String url) {
		if (TextUtils.isEmpty(spanText)) return this;
		if (!(URLUtil.isHttpUrl(url) || URLUtil.isHttpsUrl(url))) return this;
		
		int start = mText.indexOf(spanText);
		if (start == -1) return this;
		mBuilder.setSpan(new URLSpan(url), start, start + spanText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}
	
	/**
	 * @see ScaleXSpan#ScaleXSpan(float)
	 */
	public SpanBuilder setScaleX(String spanText, float proportion) {
		if (TextUtils.isEmpty(spanText)) return this;
		
		int start = mText.indexOf(spanText);
		if (start == -1) return this;
		mBuilder.setSpan(new ScaleXSpan(proportion), start, start + spanText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}
	
	/**
	 * @param filter {@link BlurMaskFilter#BlurMaskFilter(float, Blur)} 
	 * 				 {@link EmbossMaskFilter#EmbossMaskFilter(float[], float, float, float)}
	 * @see MaskFilterSpan#MaskFilterSpan(MaskFilter)
	 */
	public SpanBuilder setMaskFilter(String spanText, MaskFilter filter) {
		if (TextUtils.isEmpty(spanText)) return this;
		
		int start = mText.indexOf(spanText);
		if (start == -1) return this;
		mBuilder.setSpan(new MaskFilterSpan(filter), start, start + spanText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}
	
	/**
	 * @see ClickableSpan#ClickableSpan()
	 */
	public SpanBuilder setClickable(TextView textView, String spanText, SpanClickListener listener) {
		return setClickable(textView, spanText, listener, -1, true);
	}

	/**
	 * @see ClickableSpan#ClickableSpan()
	 */
	public SpanBuilder setClickable(TextView textView, String spanText, SpanClickListener listener, 
			int hightLightColor) {
		return setClickable(textView, spanText, listener, hightLightColor, true);
	}
	
	/**
	 * @see ClickableSpan#ClickableSpan()
	 */
	public SpanBuilder setClickable(TextView textView, String spanText, SpanClickListener listener, 
			int hightLightColor, boolean needUnderline) {
		if (textView == null
				|| TextUtils.isEmpty(spanText)
				|| listener == null) 
			return this;
		
		int start = mText.indexOf(spanText);
		if (start == -1) return this;
		JasonClickableSpan clickableSpan = new JasonClickableSpan(listener, hightLightColor, needUnderline);
		mBuilder.setSpan(clickableSpan, start, start + spanText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.setHighlightColor(Color.TRANSPARENT);
		textView.setMovementMethod(LinkMovementMethod.getInstance());
		return this;
	}
	
	/**
	 * @see ImageSpan#ImageSpan(Bitmap)
	 */
	public SpanBuilder setImage(Context context, Bitmap bitmap, String replaceText) {
		return setImage(context, bitmap, IMAGE_SPAN_ALIGN_BOTTOM, replaceText);
	}
	
	/**
	 * @see ImageSpan#ImageSpan(Context, Bitmap, int)
	 */
	public SpanBuilder setImage(Context context, Bitmap bitmap, int verticalAlignment, String replaceText) {
		if (context == null || bitmap == null) return this;
		
		Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
		return setImage(drawable, verticalAlignment, replaceText);
	}
	
	/**
	 * @see ImageSpan#ImageSpan(Drawable)
	 */
	public SpanBuilder setImage(Drawable drawable, String replaceText) {
		return setImage(drawable, IMAGE_SPAN_ALIGN_BASELINE, replaceText);
	}
	
	/**
	 * @see ImageSpan#ImageSpan(Drawable, int)
	 */
	public SpanBuilder setImage(Drawable drawable, int verticalAlignment, String replaceText) {
		if (drawable == null) return this;
		
		int start = mText.indexOf(replaceText);
		if (start == -1) return this;
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		ImageSpan imageSpan = new ImageSpan(drawable, verticalAlignment);
		mBuilder.setSpan(imageSpan, start, start + replaceText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}
	
	/**
	 * @see ImageSpan#ImageSpan(Drawable, String)
	 */
	public SpanBuilder setImage(Drawable drawable, String source, String replaceText) {
		return setImage(drawable, source, IMAGE_SPAN_ALIGN_BOTTOM, replaceText);
	}
	
	/**
	 * @see ImageSpan#ImageSpan(Drawable, String, int)
	 */
	public SpanBuilder setImage(Drawable drawable, String source, int verticalAlignment, String replaceText) {
		if (drawable == null || TextUtils.isEmpty(source) || TextUtils.isEmpty(replaceText)) return this;
		
		int start = mText.indexOf(replaceText);
		if (start == -1) return this;
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		ImageSpan imageSpan = new ImageSpan(drawable, source, verticalAlignment);
		mBuilder.setSpan(imageSpan, start, start + replaceText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}
	
	public SpanBuilder setImage(Context context, Uri uri, String replaceText) {
		return setImage(context, uri, IMAGE_SPAN_ALIGN_BOTTOM, replaceText);
	}
	
	/**
	 * @see ImageSpan#ImageSpan(Context, Uri, int)
	 */
	public SpanBuilder setImage(Context context, Uri uri, int verticalAlignment, String replaceText) {
		if (context == null || uri == null || TextUtils.isEmpty(replaceText)) return this;
		
		int start = mText.indexOf(replaceText);
		if (start == -1) return this;
		ImageSpan imageSpan = new ImageSpan(context, uri, verticalAlignment);
		mBuilder.setSpan(imageSpan, start, start + replaceText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}
	
	/**
	 * @see ImageSpan#ImageSpan(Context, int)
	 */
	public SpanBuilder setImage(Context context, int resourceId, String replaceText) {
		return setImage(context, resourceId, IMAGE_SPAN_ALIGN_BOTTOM, replaceText);
	}
	
	/**
	 * @see ImageSpan#ImageSpan(Context, int, int)
	 */
	public SpanBuilder setImage(Context context, int resourceId, int verticalAlignment, String replaceText) {
		if (context == null || resourceId < 0 || TextUtils.isEmpty(replaceText)) return this;
		
		int start = mText.indexOf(replaceText);
		if (start == -1) return this;
		ImageSpan imageSpan = new ImageSpan(context, resourceId, verticalAlignment);
		mBuilder.setSpan(imageSpan, start, start + replaceText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}
	
	public SpannableStringBuilder build() {
		return mBuilder;
	}
	
	/**
	 * @author Jason Fang
	 * @since 2015年1月27日 下午3:15:15
	 */
	class JasonClickableSpan extends ClickableSpan {
		
		SpanClickListener mListener;
		int mColor;
		boolean mUnderline;
		
		public JasonClickableSpan(SpanClickListener listener) {
			this(listener, -1);
		} 
		
		public JasonClickableSpan(SpanClickListener listener, int color) {
			this(listener, -1, true);
		}
		
		public JasonClickableSpan(SpanClickListener listener, int color, boolean underline) {
			mListener = listener;
			mColor = color;
			mUnderline = underline;
		}
		
		@Override
		public void onClick(View widget) {
			if (mListener != null) {
				mListener.onClick(widget);
			}
		}

		@Override
		public void updateDrawState(@NonNull TextPaint ds) {
			super.updateDrawState(ds);
			ds.setColor(mColor == -1 ? ds.linkColor : mColor);
	        ds.setUnderlineText(mUnderline);
		}
		
	}
	
	/**
	 * @author Jason Fang
	 * @since 2015年1月27日 下午3:15:08
	 */
	public interface SpanClickListener {
		void onClick(View spanView);
	}
}
