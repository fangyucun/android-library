/*
 * Copyright (C) 2016 Jason Fang ( ifangyucun@gmail.com )
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hellofyc.base.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created on 2016/2/16.
 *
 * @author Yucun Fang
 */
public abstract class TaskLoader<D> extends AsyncTaskLoader<D> {

    private D mData;

    public TaskLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        } else {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        mData = null;
    }

    @Override
    public void deliverResult(D data) {
        if (isReset()) {
            if (mData != null) {
                mData = null;
            }
        }

        mData = data;

        if (isStarted()) {
            super.deliverResult(data);
        }
    }
}
