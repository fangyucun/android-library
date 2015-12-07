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

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;

import com.hellofyc.applib.model.ContactInfo;
import com.hellofyc.applib.util.Flog;
import com.hellofyc.applib.util.IoUtils;
import com.hellofyc.applib.util.NetUtils;
import com.hellofyc.applib.util.VerifyUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * 手机信息管理
 * Create on 2014年12月4日 下午2:10:37
 * @author Jason Fang
 */
@SuppressWarnings("deprecation")
public class PhoneHelper {
	private static final boolean DEBUG = true;

	public static final int FLAG_ALL = 0;
	public static final int FLAG_SYSTEM = 1;
	public static final int FLAG_NOT_SYSTEM = 2;
	
	public static final String CHINA_MOBILE = "中国移动";
	public static final String CHINA_UNICOM = "中国联通";
	public static final String CHINA_TELECOM = "中国电信";
	
    public static final String CMWAP = "cmwap";
    public static final String CMNET = "cmnet";
    public static final String WAP3G = "3gwap";
    public static final String NET3G = "3gnet";
    public static final String UNIWAP = "uniwap";
    public static final String UNINET = "uninet";
    public static final String CTWAP = "ctwap";
    public static final String CTNET = "ctnet";
	
	private static PhoneHelper sInstance;
	private Context mContext;
	
	private PhoneHelper(Context context) {
		mContext = context;
	}
	
	public static PhoneHelper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new PhoneHelper(context);
		}
		return sInstance;
	}
	
	/**
	 * 获取网络运营商名称
	 * @link #permission:android.permission.READ_PHONE_STATE
	 */
	public static String getOperatorName(Context context) {
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		
		try {
			if (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
				return CHINA_TELECOM;
			} else {
				String str = tm.getNetworkOperator();
				if ("46000".equals(str)
						|| "46002".equals(str)
						|| "46007".equals(str)) {
					return CHINA_MOBILE;
				} else if ("46001".equals(str)) {
					return CHINA_UNICOM;
				} else if ("46003".equals(str)) {
					return CHINA_TELECOM;
				} else {
					return null;
				}
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取运营商编号
	 */
	public static String getOperatorId(Context context) {
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		try {
			if (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
				return "cdma";
			} else {
				return tm.getNetworkOperator();
			}
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 获取ICCID
	 * 
	 * 中国移动编码格式:89 86 00 M F SS YY G XXXXXX P
	 * 89:国际编号
	 * 86:国家编号
	 * 00:运营商编号		00代表中国移动, 01代表中国联通, 03代表中国电信
	 * M:号段对应号码前三位		0:159	1:158	2:150	3:151	4-9:134-139   A:157	  B:188	  C:152   D:147   E:187   F:187
	 * F:用户号码第四位
	 * SS:省编号			01:北京   02:天津   03:河北   04:山西   05:内蒙古   06:辽宁   07:吉林   08:黑龙江   09:上海   10:江苏
	 * 					11:浙江   12:安徽   13:福建   1
	 * 4:江西   15:山东   16:河南   17:湖北   18:湖南   19:广东   20:广西
	 * 					21:海南   22:四川   23:贵州   24:云南   25:西藏   26:陕西   27:甘肃   28:青海   29:宁夏   30:新疆   
	 * 					31:重庆
	 * YY:编制ICCID时年号的后两位
	 * G:SIM卡供应商代码
	 * XXXXXX:用户识别码
	 * P:校验码
	 * 
	 * 中国联通编码格式:89 86 01 YY M SS XXXXXXXX P
	 * SS:省编号			10:内蒙古   11:北京   13:天津   17:山东   18:河北   19:山西   30:安徽   31:上海   34:江苏   36:浙江  
	 * 					38:福建   50:海南   51:广东   59:广西   70:青海   71:湖北   74:湖南   75:江西   76:河南   79:西藏    
	 * 					81:四川   83:重庆   84:陕西   85:贵州    86:云南    87:甘肃   88:宁夏   89:新疆   90:吉林   91:辽宁   97:黑龙江
	 * M:默认为8
	 * XXXXXXXX:卡商生产的顺序编码
	 * 
	 * 中国电信编码格式:89 86 03 M YY HHH XXXXXXXX
	 * M:保留位 默认为0
	 * HHH:省份区号
	 * XXXXXXXX:流水号
	 */
	public static String getICCID(Context context) {
		try {
			TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			return tm.getSimSerialNumber();
		} catch (Exception e) {
			Flog.e(e);
		}
		return "";
	}

	/**
	 * 获取IMSI
	 * Requires Permission:
	 * {@link android.Manifest.permission#READ_PHONE_STATE}
	 */
	public static String getIMSI(Context context) {
		try {
			TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			return tm.getSubscriberId() == null ? "" : tm.getSubscriberId();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 判断sim卡是否准备好
	 */
	public static boolean isSimReady(Context context) {
		TelephonyManager tm = (TelephonyManager)context
				.getSystemService(Context.TELEPHONY_SERVICE); 
		return tm.getSimState() == TelephonyManager.SIM_STATE_READY;
	}

    /*
     * 获取当前的手机号
     */
    public static String getPhoneNumber(Context context) {
    	try {
			TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			String phoneNumber = tm.getLine1Number();
			if (!TextUtils.isEmpty(phoneNumber)) {
				if (phoneNumber.startsWith("+")) {
					phoneNumber = tm.getLine1Number().substring(3);
				}
				if (VerifyUtils.isPhoneNumber(phoneNumber)) {
					return phoneNumber;
				}
			}
		} catch (Exception e) {
			if (DEBUG) Flog.e(e);
		}
		return "";
    }
    
    public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cManager.getActiveNetworkInfo();
		
		return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI &&
				WifiHelper.getInstance(context).isWifiConnected();
	}
	
	/**
	 * 获取IPv4
	 */
	public static String getLocalIpAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> niList = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : niList) {
                List<InetAddress> iaList = Collections.list(intf.getInetAddresses());
                for (InetAddress ia : iaList) {
                    if (!ia.isLoopbackAddress()) {
                        String sAddr = ia.getHostAddress().toUpperCase(Locale.CHINESE);
                        boolean isIPv4 = NetUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4) {
                                return sAddr;
                            }
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%');
                                return delim<0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
	
	/**
	 * 获取外网ip
	 */
	public static String getIpAddress2() {
		URL url;
        InputStream is = null;
        try {
        	url = new URL("http://iframe.ip138.com/ic.asp");
            HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
            int responseCode = urlConn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                is = urlConn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
                int start = sb.indexOf("[");
                int end = sb.indexOf("]", start + 1);
                line = sb.substring(start + 1, end);
                return line;   
            }
        } catch(IOException e) {
            Flog.e(e);
        } finally {
        	IoUtils.closeIS(is);
        }
        return "";
	}
	
    /**
     * 判断当前网络是否是wifi网络
     * @return boolean
     */  
    public static boolean isWifi(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return (info != null && info.getType() == ConnectivityManager.TYPE_WIFI);
    }
    
    /** 
     * 判断当前网络是否是移动数据网络
     */
    public static boolean isMobileData(Context context) {
    	ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.getType() == ConnectivityManager.TYPE_MOBILE;
    }
	
	/**
	 * 获取网络类型
	 */
	public static int getNetworkType(Context context) {
		TelephonyManager tManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tManager.getNetworkType();
	}
	
	public static String getNetworkTypeName(int type) {
		switch (type) {
		case TelephonyManager.NETWORK_TYPE_GPRS:
			return "GPRS";
		case TelephonyManager.NETWORK_TYPE_EDGE:
			return "EDGE";
		case TelephonyManager.NETWORK_TYPE_HSPA:
			return "HSPA";
		case TelephonyManager.NETWORK_TYPE_HSPAP:
			return "HSPA+";
		case TelephonyManager.NETWORK_TYPE_LTE:
			return "LTE";
		case TelephonyManager.NETWORK_TYPE_CDMA:
			return "CDMA";
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			return "CDMA 1xRTT";
		case TelephonyManager.NETWORK_TYPE_EHRPD:
			return "CDMA EHRPD";
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			return "CDMA EVDO-0";
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			return "CDMA EVDO-A";
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
			return "CDMA EVDO-B";
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return "IDEN";
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			return "HSDPA";
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			return "HSUPA";
		case TelephonyManager.NETWORK_TYPE_UMTS:
			return "UMTS";
		default:
			return "UNKNOWN";
		}
	}
	
	/**
	 * 获取基站编号
	 * User Permission:
	 * 				{@link android.Manifest.permission#ACCESS_COARSE_LOCATION}
	 */
	public static int getCid(Context context) {
		if (context == null) return 0;
		
		TelephonyManager tManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		CellLocation location = tManager.getCellLocation();
		if (location == null) return 0;
		
		if (location instanceof GsmCellLocation) {
			return ((GsmCellLocation)location).getCid();
		} else {
			return ((CdmaCellLocation)location).getBaseStationId();
		}
	}
	
	/**
	 * 获取位置区域码
	 * User Permission:
	 * 				{@link android.Manifest.permission#ACCESS_COARSE_LOCATION}
	 */
	public static int getLac(Context context) {
		TelephonyManager tManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		CellLocation location = tManager.getCellLocation();
		if (location == null) return 0;
		
		if (location instanceof GsmCellLocation) {
			return ((GsmCellLocation)location).getLac();
		} else {
			return ((CdmaCellLocation)location).getNetworkId();
		}
	}
	
	/**
	 * 
	 * 获取相邻基站信息
	 * User Permission:
	 * 				{@link android.Manifest.permission#ACCESS_COARSE_LOCATION} 
	 */
	public static List<NeighboringCellInfo> getNeighboringCellInfo(Context context) {
		TelephonyManager tManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		return tManager.getNeighboringCellInfo();
	}
	
	/**
	 * 读取通讯录
	 * Use Permission:
	 * {@link android.Manifest.permission#READ_CONTACTS}
	 */
	public ArrayList<ContactInfo> readContacts() {
		ArrayList<ContactInfo> contacts = new ArrayList<>();
		ContentResolver cr = mContext.getContentResolver();
		Cursor c = null;
		try {
			c = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		} catch (SecurityException e) {
			Flog.e(e);
		}
		if (c == null) return contacts;
		
		ContactInfo info;
		while (c.moveToNext()) {
			info = new ContactInfo();
			info.id = c.getString(c.getColumnIndex(PhoneLookup._ID));
			info.displayName = c.getString(c.getColumnIndex(PhoneLookup.DISPLAY_NAME));
			
			Cursor phoneNumberCursor = cr.query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + " = " + info.id, null, null);
			if (phoneNumberCursor == null) continue;
			
			StringBuilder sb = new StringBuilder();
			while (phoneNumberCursor.moveToNext()) {
				String phoneNumber = phoneNumberCursor.getString(phoneNumberCursor.getColumnIndex(Phone.NUMBER));
				sb.append(phoneNumber);
				sb.append(",");
			}
			IoUtils.close(phoneNumberCursor);
			info.phoneNumbers = sb.toString().split(",");
			if (DEBUG) Flog.i(info.displayName + "\n" + sb.toString());
			
			contacts.add(info);
		}
		IoUtils.close(c);
		return contacts;
	}
}
