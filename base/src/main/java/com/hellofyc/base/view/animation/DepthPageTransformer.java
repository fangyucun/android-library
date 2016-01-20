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

package com.hellofyc.base.view.animation;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;

public class DepthPageTransformer implements PageTransformer {
    private static float MIN_SCALE = 0.75f;
 
    @Override
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        if (position < -1) {
        	ViewCompat.setAlpha(view, 0);
        } else if (position <= 0) {
        	ViewCompat.setAlpha(view, 1);
        	ViewCompat.setTranslationX(view, 0);
        	ViewCompat.setScaleX(view, 1);
        	ViewCompat.setScaleY(view, 1);
        } else if (position <= 1) {
        	ViewCompat.setAlpha(view, 1 - position);
        	ViewCompat.setTranslationX(view, pageWidth * -position);
            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
            ViewCompat.setScaleX(view, scaleFactor);
        	ViewCompat.setScaleY(view, scaleFactor);
        } else {
        	ViewCompat.setAlpha(view, 0);
         }
    }
}