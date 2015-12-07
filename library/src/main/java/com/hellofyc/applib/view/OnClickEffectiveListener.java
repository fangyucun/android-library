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

package com.hellofyc.applib.view;

import android.view.View;
import android.view.View.OnClickListener;

public abstract class OnClickEffectiveListener implements OnClickListener {

	private long mLastClickTime = 0;
	private static final int INTERVAL_TIME = 1000;
	
	@Override
	public void onClick(View v) {
		if (System.currentTimeMillis() - mLastClickTime < INTERVAL_TIME) {
			return;
		} else {
			mLastClickTime = System.currentTimeMillis();
		}
		onEffectiveClick(v);
	}
	
	public abstract void onEffectiveClick(View v);

}
