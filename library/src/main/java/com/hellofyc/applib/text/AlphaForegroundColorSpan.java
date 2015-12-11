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

package com.hellofyc.applib.text;

import android.graphics.Color;
import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;

import com.hellofyc.applib.util.FLog;

/**
 * Alpha ForegroundColorSpan
 * Create on 2014年12月1日 下午9:07:22
 * @author Jason Fang
 */
public class AlphaForegroundColorSpan extends ForegroundColorSpan {
	static final boolean DEBUG = true;
	
    private int mAlpha;

    public AlphaForegroundColorSpan(int color) {
        super(color);
        if (DEBUG) FLog.i("alpha:" + Color.alpha(color));
        setAlpha(Color.alpha(color));
	}

    public AlphaForegroundColorSpan(Parcel src) {
        super(src);
        mAlpha = src.readInt();
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(mAlpha);
    }

	@Override
	public void updateDrawState(TextPaint ds) {
		ds.setColor(getAlphaColor());
	}

    public void setAlpha(int alpha) {
        mAlpha = alpha;
    }

    public int getAlpha() {
        return mAlpha;
    }

    private int getAlphaColor() {
        int foregroundColor = getForegroundColor();
        return Color.argb(mAlpha, Color.red(foregroundColor), Color.green(foregroundColor), Color.blue(foregroundColor));
    }
}
