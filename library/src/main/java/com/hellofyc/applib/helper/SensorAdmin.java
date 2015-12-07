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

package com.hellofyc.applib.helper;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;

public class SensorAdmin {

	private static final int SENSOR_SHAKE = 1;
	
	private SensorManager mSensorManager;
	private OnShakeAShakeListener mOnShakeAShakeListener;
	private MySensorEventListener mEventListener;
	private Vibrator mVibarator;
	private static Handler sHandler = new MainHandler();
	
	private SensorAdmin(Context context) {
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		mVibarator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}
	
	public static SensorAdmin getInstance(Context context) {
		return new SensorAdmin(context);
	}
	
	public void registerShakeAShakeListener(OnShakeAShakeListener listener) {
		mOnShakeAShakeListener = listener;
		
		mEventListener = new MySensorEventListener();
		mSensorManager.registerListener(mEventListener, 
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
				SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	public void unregisterShakeAShakeListener() {
		mSensorManager.unregisterListener(mEventListener);
	}
	
	static class MainHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SENSOR_SHAKE:
				HandlerResult result = (HandlerResult) msg.obj;
				result.mResultVibrator.vibrate(new long[]{0, 200, 200, 200}, -1);
				result.mResultShakeListener.onShakeAShake();
				break;
			}
		}
		
	}
	
	class HandlerResult {
		public OnShakeAShakeListener mResultShakeListener;
		public Vibrator mResultVibrator;
		
		public HandlerResult(OnShakeAShakeListener listener, Vibrator vibrator) {
			mResultShakeListener = listener;
			mResultVibrator = vibrator;
		}
	}
	
	private class MySensorEventListener implements SensorEventListener {

		@Override
		public void onSensorChanged(SensorEvent event) {
			float[] values = event.values;
			float x = values[0];
			float y = values[1];
			float z = values[2];
			int medumValue = 19;
			if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {
				HandlerResult result = new HandlerResult(mOnShakeAShakeListener, mVibarator);
				Message msg = sHandler.obtainMessage();
				msg.what = SENSOR_SHAKE;
				msg.obj = result;
				msg.sendToTarget();
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
		
	}
	
	public interface OnShakeAShakeListener {
		void onShakeAShake();
	}
}
