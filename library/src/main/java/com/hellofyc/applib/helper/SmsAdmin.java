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

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * 短信工具类
 * Create on 2014-10-23 上午11:27:56
 * @author Jason Fang
 */
public class SmsAdmin {
	
	public static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	public static final String ACTION_SMS_SEND = "android.intent.action.SMS_SEND";

	private static SmsAdmin sInstance;
	private Context mContext;
	private OnSmsReceiveListener mOnSmsReceiveListener;
	private OnSmsSendListener mOnSmsSendListener;
	
	private String mLongNumber;
	private String mKeyword;
	
	private SmsReceivedReceiver mSmsReceivedReceiver;
	private SmsSendReceiver mSmsSendReceiver;
	
	private SmsAdmin(Context context) {
		mContext = context;
	}
	
	public static SmsAdmin getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new SmsAdmin(context);
		}
		return sInstance;
	}
	
	public void startMonitor(OnSmsReceiveListener listener) {
		startMonitor("", "", listener);
	}
	
	/**
	 * 开始监听
	 */
	public void startMonitor(String longNumber, String keyword, OnSmsReceiveListener listener) {
		mLongNumber = longNumber == null ? "" : longNumber;
		mKeyword = keyword == null ? "" : keyword;
		mOnSmsReceiveListener = listener;
		registerSmsReceiveReceiver();
	}
	
	public void stopMonitor() {
		try {
			mContext.unregisterReceiver(mSmsReceivedReceiver);
		} catch (Exception e) {
            e.printStackTrace();
		}
	}
	
	/**
	 * 发送短信
	 */
	public void sendSms(String receiverPhoneNumber, String message, PendingIntent sendIntent, PendingIntent deliverIntent, OnSmsSendListener listener) {
		if (TextUtils.isEmpty(receiverPhoneNumber) || TextUtils.isEmpty(message)) {
			return;
		}
		mOnSmsSendListener = listener;
		
		SmsManager sm = SmsManager.getDefault();
		if (message.length() > 70) {
			ArrayList<String> messages = sm.divideMessage(message);
			ArrayList<PendingIntent> sentIntents = new ArrayList<>();
			ArrayList<PendingIntent> deliveryIntents = new ArrayList<>();
			for (int i=0; i<messages.size(); i++) {
				sentIntents.add(sendIntent);
				deliveryIntents.add(deliverIntent);
			}
			sm.sendMultipartTextMessage(receiverPhoneNumber, null, messages, sentIntents, deliveryIntents);
		} else {
			sm.sendTextMessage(receiverPhoneNumber, null, message, sendIntent, deliverIntent);
		}
	}
	
	void registerSmsReceiveReceiver() {
		if (mSmsReceivedReceiver != null) return;
		
		mSmsReceivedReceiver = new SmsReceivedReceiver();
		IntentFilter filter = new IntentFilter(ACTION_SMS_RECEIVED);
		filter.setPriority(Integer.MAX_VALUE);
		mContext.registerReceiver(mSmsReceivedReceiver, filter);
	}
	
	void registerSmsSendReceiver() {
		if (mSmsSendReceiver != null) return;
		
		mSmsSendReceiver = new SmsSendReceiver();
		IntentFilter sendSmsFilter = new IntentFilter(ACTION_SMS_SEND);
		sendSmsFilter.setPriority(Integer.MAX_VALUE);
		mContext.registerReceiver(mSmsSendReceiver, sendSmsFilter);
	}
	
	void unregisterSmsReceiveReceiver() {
		if (mSmsReceivedReceiver != null) {
			mContext.unregisterReceiver(mSmsReceivedReceiver);
			mSmsReceivedReceiver = null;
		}
	}
	
	void unregisterSmsSendReceiver() {
		if (mSmsSendReceiver != null) {
			mContext.unregisterReceiver(mSmsSendReceiver);
			mSmsSendReceiver = null;
		}
	}
	
	class SmsSendReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			if (!ACTION_SMS_SEND.equals(action)) return;
			if (mOnSmsSendListener == null) return;
			
			int resultCode = getResultCode();
			if (resultCode == Activity.RESULT_OK) {
				mOnSmsSendListener.onSmsSendSuccess();
			} else {
				mOnSmsSendListener.onSmsSendFailure();
			}
			unregisterSmsSendReceiver();
		}
	}
	
	class SmsReceivedReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (ACTION_SMS_RECEIVED.equals(intent.getAction())) {
				Bundle bundle = intent.getExtras();
				if (bundle == null) {
					return;
				}
				
				Object[] pdus = (Object[])bundle.get("pdus");
                if (pdus == null) return;
				SmsMessage[] smsMessage = new SmsMessage[pdus.length];
				for (int i=0; i<smsMessage.length; i++) {
					byte[] pdu = (byte[])pdus[i];
					smsMessage[i] = SmsMessage.createFromPdu(pdu);
				}
				
				for (SmsMessage msg : smsMessage) {
					String srcAddress = msg.getOriginatingAddress();
					String message = msg.getMessageBody();
					if (srcAddress.startsWith(mLongNumber == null ? "" : mLongNumber) && message.contains(mKeyword)) {
						if (mOnSmsReceiveListener != null) {
							mOnSmsReceiveListener.onSmsReceived(msg.getMessageBody());
						}
						break;
					}
				}
			}
		}

	}
	
	public interface OnSmsSendListener {
		void onSmsSendSuccess();
		void onSmsSendFailure();
	}
	
	public interface OnSmsReceiveListener {
		void onSmsReceived(String text);
	}
}
