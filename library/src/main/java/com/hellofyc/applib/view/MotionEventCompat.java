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

import android.view.MotionEvent;

public class MotionEventCompat {
	
	public static final int ACTION_DOWN = 0;
	public static final int ACTION_UP = 1;
	public static final int ACTION_MOVE = 2;
	public static final int ACTION_CANCEL = 3;
	public static final int ACTION_OUTSIDE = 4;
			
	/**
     * Synonym for {@link MotionEvent#ACTION_MASK}.
     */
    public static final int ACTION_MASK = 0xff;

    /**
     * Synonym for {@link MotionEvent#ACTION_POINTER_DOWN}.
     */
    public static final int ACTION_POINTER_DOWN = 5;

    /**
     * Synonym for {@link MotionEvent#ACTION_POINTER_UP}.
     */
    public static final int ACTION_POINTER_UP = 6;

    /**
     * Synonym for {@link MotionEvent#ACTION_HOVER_MOVE}.
     */
    public static final int ACTION_HOVER_MOVE = 7;

    /**
     * Synonym for {@link MotionEvent#ACTION_SCROLL}.
     */
    public static final int ACTION_SCROLL = 8;

    /**
     * Synonym for {@link MotionEvent#ACTION_POINTER_INDEX_MASK}.
     */
    public static final int ACTION_POINTER_INDEX_MASK  = 0xff00;

    /**
     * Synonym for {@link MotionEvent#ACTION_POINTER_INDEX_SHIFT}.
     */
    public static final int ACTION_POINTER_INDEX_SHIFT = 8;

    /**
     * Constant for The pointer is not down but has entered the
     * boundaries of a window or view.
     * <p>
     * This action is always delivered to the window or view under the pointer.
     * </p><p>
     * This action is not a touch event so it is delivered to
     * {@link android.view.View#onGenericMotionEvent(MotionEvent)} rather than
     * {@link android.view.View#onTouchEvent(MotionEvent)}.
     * </p>
     */
    public static final int ACTION_HOVER_ENTER = 9;

    /**
     * Constant for: The pointer is not down but has exited the
     * boundaries of a window or view.
     * <p>
     * This action is always delivered to the window or view that was previously under the pointer.
     * </p><p>
     * This action is not a touch event so it is delivered to
     * {@link android.view.View#onGenericMotionEvent(MotionEvent)} rather than
     * {@link android.view.View#onTouchEvent(MotionEvent)}.
     * </p>
     */
    public static final int ACTION_HOVER_EXIT = 10;

	public static String actionToString(int action) {
        switch (action) {
            case ACTION_DOWN:
                return "ACTION_DOWN";
            case ACTION_UP:
                return "ACTION_UP";
            case ACTION_CANCEL:
                return "ACTION_CANCEL";
            case ACTION_OUTSIDE:
                return "ACTION_OUTSIDE";
            case ACTION_MOVE:
                return "ACTION_MOVE";
            case ACTION_HOVER_MOVE:
                return "ACTION_HOVER_MOVE";
            case ACTION_SCROLL:
                return "ACTION_SCROLL";
            case ACTION_HOVER_ENTER:
                return "ACTION_HOVER_ENTER";
            case ACTION_HOVER_EXIT:
                return "ACTION_HOVER_EXIT";
        }
        int index = (action & ACTION_POINTER_INDEX_MASK) >> ACTION_POINTER_INDEX_SHIFT;
        switch (action & ACTION_MASK) {
            case ACTION_POINTER_DOWN:
                return "ACTION_POINTER_DOWN(" + index + ")";
            case ACTION_POINTER_UP:
                return "ACTION_POINTER_UP(" + index + ")";
            default:
                return Integer.toString(action);
        }
    }
}
