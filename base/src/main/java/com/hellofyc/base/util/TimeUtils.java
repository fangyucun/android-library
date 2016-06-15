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
 */package com.hellofyc.base.util;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Create on 2013年11月20日
 *
 * @author Fang Yucun
 */
public final class TimeUtils {
	static final boolean DEBUG = false;

	private static final int MSG_WHAT_CURRENT_TIME = 1;
	private static TimeHandler sTimeHandler;
	
	public static final long DEFAULT_RAW_OFFSET = -8 * TimeUnit.HOURS.toMillis(1);

    public static final String TEMPLATE_DATE_TIME_WITH_MILLIS			 = "yyyy-MM-dd HH:mm:ss.S";
    public static final String TEMPLATE_DATE_TIME						 = "yyyy-MM-dd HH:mm:ss";
    public static final String TEMPLATE_DATE_TIME_EXCLUDE_SECOND		 = "yyyy-MM-dd HH:mm";
    public static final String TEMPLATE_DATE							 = "yyyy-MM-dd";
    public static final String TEMPLATE_DATE_TIME_EXCLUDE_YEAR_SECOND	 = "MM-dd HH:mm";
    public static final String TEMPLATE_TIME							 = "HH:mm:ss";
    public static final String TEMPLATE_TIME_EXCLUDE_SECOND				 = "HH:mm";
    public static final String TEMPLATE_DATE_TIME_FILENAME_WITH_MILLIS	 = "yyyyMMdd_HHmmssS";
    public static final String TEMPLATE_DATE_TIME_FILENAME 				 = "yyyyMMdd_HHmmss";
    public static final String TEMPLATE_DATE_FILENAME					 = "yyyyMMdd";
    public static final String TEMPLATE_TIME_FILENAME					 = "HHmmss";

    public static String getTime(long timeInMillis, String dateFormat) {
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.CHINA);
        return simpleDateFormat.format(parseDate(timeInMillis));
    }

    /**
     * Use in timestamp
     * @param timeInMillis milliseconds
     * @return Date
     */
    private static Date parseDate(long timeInMillis) {
        int length = NumberUtils.checkNumberLength(timeInMillis);
        if (length == 10) {
            return new Date(timeInMillis * 1000);
        } else {
            return new Date(timeInMillis);
        }
    }
    
    public static String getCurrentTime(String dateFormat) {
    	return getTime(getCurrentTimeMillis(), dateFormat);
    }
    
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

	public static long getCurrentTimeMillis(int start) {
		return Long.parseLong(String.valueOf(System.currentTimeMillis()).substring(start));
	}

	public static long getCurrentTimeMillis(int start, int end) {
		return Long.parseLong(String.valueOf(System.currentTimeMillis()).substring(start, end));
	}
    
    public static String getCurrentDateTimeWithMillis() {
    	return getTime(getCurrentTimeMillis(), TEMPLATE_DATE_TIME_WITH_MILLIS);
    }

    public static String getCurrentDateTime() {
    	return getTime(getCurrentTimeMillis(), TEMPLATE_DATE_TIME);
    }
    
    public static String getCurrentDate() {
    	return getTime(getCurrentTimeMillis(), TEMPLATE_DATE);
    }
    
    public static String getCurrentTime() {
    	return getTime(getCurrentTimeMillis(), TEMPLATE_TIME);
    }

    public static String getDateTime(long timeInMillis) {
        return getTime(timeInMillis, TEMPLATE_DATE_TIME);
    }
    
    public static String getDate(long timeInMillis) {
    	return getTime(timeInMillis, TEMPLATE_DATE);
    }
    
    public static String getTime(long timeInMillis) {
    	return getTime(timeInMillis, TEMPLATE_TIME);
    }
    
    public static String formatTime(long timeInMillis) {
    	return formatTime(timeInMillis, "%02d:%02d:%02d");
    }
    
    public static String formatTime(long timeInMillis, String formatString) {
    	return String.format(Locale.CHINA, formatString, 
				TimeUnit.MILLISECONDS.toHours(timeInMillis),
				TimeUnit.MILLISECONDS.toMinutes(timeInMillis) % TimeUnit.HOURS.toMinutes(1), 
				TimeUnit.MILLISECONDS.toSeconds(timeInMillis) % TimeUnit.MINUTES.toSeconds(1));
    }
    
	/**
	 * 判断当前日期是否是当天
	 * @param timeInMillis true or false;
	 * @return true or false
	 */
	public static boolean isToday(long timeInMillis) {
		return DateUtils.isToday(timeInMillis);
	}
	
	/**
	 * 判断当前日期是否是当天
	 * @param timeInMillis timeInMillis
	 * @return true or false;
	 */
	public static boolean isYestoday(long timeInMillis) {
		return DateUtils.isToday(timeInMillis + TimeUnit.DAYS.toMillis(1));
	}
	
	/**
	 * 判断当前日期是否是当月
	 * @param timeInMillis timeInMillis
	 * @return true or false;
	 */
	public static boolean isCurrentMonth(long timeInMillis) {
		int month = Integer.parseInt(getTime(timeInMillis, "MM"));
		return new GregorianCalendar().get(Calendar.MONTH) == month;
	}
	
	/**
	 * 判断当前日期是否是当年
	 * @param timeInMillis timeInMillis
	 * @return true or false
	 */
	public static boolean isCurrentYear(long timeInMillis) {
		int year = Integer.parseInt(getTime(timeInMillis, "yyyy"));
		return new GregorianCalendar().get(Calendar.YEAR) == year;
	}
	
	public static long getTimestamp(String template, String dateStr) {
        if (TextUtils.isEmpty(template) || TextUtils.isEmpty(dateStr)) return 0;

		try {
			Date date = new SimpleDateFormat(template, Locale.CHINA).parse(dateStr);
			return date.getTime();
		} catch (ParseException e) {
			if (DEBUG) FLog.e(e);
		}
		return 0;
	}
	
	public static long getTimestamp(String dateStr) {
		return getTimestamp(TEMPLATE_DATE_TIME, dateStr);
	}

	public static String getShowTime(long timestamp) {
		if (TimeUtils.isToday(timestamp)) {
			return "今天 " + TimeUtils.getTime(timestamp, TEMPLATE_TIME_EXCLUDE_SECOND);
		} else if (TimeUtils.isYestoday(timestamp)) {
			return "昨天 " + TimeUtils.getTime(timestamp, TEMPLATE_TIME_EXCLUDE_SECOND);
		} else {
			return TimeUtils.getTime(timestamp, TEMPLATE_DATE_TIME_EXCLUDE_SECOND);
		}
	}

	public static String getShowTimeFromNow(long timestamp) {
		long duration = getCurrentTimeMillis() - timestamp;
		if (duration < TimeUnit.MINUTES.toMillis(1)) {
			return TimeUnit.MILLISECONDS.toSeconds(duration) + " 秒前";
		} else if (duration < TimeUnit.HOURS.toMillis(1)) {
			return TimeUnit.MILLISECONDS.toMinutes(duration) + " 分钟前";
		} else if (duration < TimeUnit.DAYS.toMillis(1)) {
			return TimeUnit.MILLISECONDS.toHours(duration) + " 小时前";
		} else if (duration < TimeUnit.DAYS.toMillis(7)) {
			return TimeUnit.MILLISECONDS.toDays(duration) + " 天前";
		} else {
			return TimeUtils.getTime(timestamp, TEMPLATE_DATE);
		}
	}

	public static void showTimeDynamically(TextView textView, String format) {
		if (sTimeHandler == null) {
			sTimeHandler = new TimeHandler();
		}
		Message msg = sTimeHandler.obtainMessage(MSG_WHAT_CURRENT_TIME);
		msg.obj = new HandlerResult(textView, format);
		msg.sendToTarget();
	}

	private static class HandlerResult {
		TextView mTextView;
		String mFormat;

		private HandlerResult(TextView tv, String format) {
			mTextView = tv;
			mFormat = format;
		}
	}

	private static class TimeHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case MSG_WHAT_CURRENT_TIME:
					HandlerResult result = (HandlerResult) msg.obj;
					TextView textView = result.mTextView;
					if (textView != null) {
						textView.setText(TimeUtils.getTime(TimeUtils.getCurrentTimeMillis(), result.mFormat));

						Message message = sTimeHandler.obtainMessage(MSG_WHAT_CURRENT_TIME);
						message.obj = textView;
						sTimeHandler.sendMessageDelayed(message, TimeUnit.SECONDS.toMillis(1));
					} else {
						sTimeHandler.removeMessages(MSG_WHAT_CURRENT_TIME);
					}
					break;
			}
		}
	}

	private TimeUtils() {/*Do not new me*/}
}
