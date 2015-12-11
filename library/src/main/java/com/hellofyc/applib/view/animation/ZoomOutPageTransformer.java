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

package com.hellofyc.applib.view.animation;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;

import com.hellofyc.applib.util.FLog;

public class ZoomOutPageTransformer implements PageTransformer {
	static final boolean DEBUG = false;
	
    private static float MIN_SCALE = 0.85f;
    private static float MIN_ALPHA = 0.5f;
 
    @Override
    public void transformPage(final View view, final float position) {
		if (DEBUG) FLog.i("view.hasCode:" + view.hashCode() + ", position:" + position);
    	
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();
 
        if (position < -1) {
            ViewCompat.setAlpha(view, 0);
        } else if (position <= 1) {
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float verticalMargin = pageHeight * (1 - scaleFactor) / 2;
            float horizontalMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
            	FLog.i("verticalMargin:" + verticalMargin + ", horizontalMargin:" + horizontalMargin);
            	ViewCompat.setTranslationX(view, horizontalMargin - verticalMargin / 2);
            } else {
            	ViewCompat.setTranslationX(view, -horizontalMargin + verticalMargin / 2);
            }
            ViewCompat.setScaleX(view, scaleFactor);
            ViewCompat.setScaleY(view, scaleFactor);
            ViewCompat.setAlpha(view, MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
        } else {
            ViewCompat.setAlpha(view, 0);
        }
    }
}