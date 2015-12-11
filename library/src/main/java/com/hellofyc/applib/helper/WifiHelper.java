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

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.AuthAlgorithm;
import android.net.wifi.WifiConfiguration.GroupCipher;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiConfiguration.PairwiseCipher;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.hellofyc.applib.content.BaseBroadcastReceiver;
import com.hellofyc.applib.util.FLog;
import com.hellofyc.applib.util.NumberUtils;
import com.hellofyc.applib.util.ReflectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Requires Permission:
 * {@link android.Manifest.permission#ACCESS_WIFI_STATE}
 * {@link android.Manifest.permission#ACCESS_NETWORK_STATE}
 * {@link android.Manifest.permission#CHANGE_NETWORK_STATE}
 * {@link android.Manifest.permission#CHANGE_WIFI_STATE}
 *
 * Create on 2014-10-17 上午11:36:22
 * @author Jason Fang
 */
public class WifiHelper {
	private static final boolean DEBUG = false;

	public static final int REQUEST_CODE_SCAN_ALWAYS_AVAILABLE = 1;
	
	public static final int ERROR_CODE_CONNECT_TIMEOUT			 = -1;
	public static final int ERROR_CODE_CONNECT_AUTHENTICATE		 = -2;
	public static final int ERROR_CODE_CONNECT_INVALID			 = -3;
	public static final int ERROR_CODE_CONNECT_UNKNOWN			 = -4;
	public static final int ERROR_CODE_WIFI_NOT_OPEN			 = -5;
	public static final int ERROR_CODE_WIFI_NOT_FOUND			 = -6;
	
	public static final int CONNECT_STATUS_CONNECTING			 = 1;
	public static final int CONNECT_STATUS_AUTHENTICATING		 = 2;
	public static final int CONNECT_STATUS_OBTAINING_IPADDR		 = 3;

	public static final int WIFI_STATE_ENABLED					 = 1;
	public static final int WIFI_STATE_DISABLED					 = 2;
	
	public static final int WIFI_AP_STATE_DISABLING				 = 10;
	public static final int WIFI_AP_STATE_DISABLED				 = 11;
	public static final int WIFI_AP_STATE_ENABLING				 = 12;
	public static final int WIFI_AP_STATE_ENABLED				 = 13;
	public static final int WIFI_AP_STATE_FAILED				 = 14;
	
	private static final int CONNECT_TIMEOUT = 20;
	
	private static WifiHelper sInstance;
	private Context mContext;
	private final WifiManager mWifiManager;
	private WifiLock mWifiLock;
	
	private OnWifiStateListener mOnWifiStateListener;
	private OnWifiConnectListener mOnWifiConnectListener;
	private OnWifiRssiChangedListener mOnWifiRssiChangedListener;
	private OnScanResultsAvailableListener mOnScanResultsAvailableListener;
	
	private WifiStateReceiver mWifiStateReceiver;
	private WifiConnectReceiver mWifiConnectReceiver;
	private WifiRssiChangedReceiver mWifiRssiChangedReceiver;
	private ScanResultsAvailableReceiver mScanResultsAvailableReceiver;
	
	private OpenScanAlwayAvailableCallback mOpenScanAlwayAvailableCallback;
	
	private String mConnectSSID = null;
	
	private int mOldNetworkId = -1;
	private int mNewNetworkId = -1;
	
	private boolean mIsConnectFailureRestore = false;
	
	/**
	 * Encrypt Type
	 * 
	 * @author Jason Fang
	 */
	public enum SecurityMode {
		/**
		 * No Password
		 */
		OPEN, 
		/**
		 * WEP Encrypt
		 */
		WEP, 
		/**
		 * WPA Encrypt
		 */
		WPA, 
		/**
		 * WPA2 Encrypt
		 */
		WPA2, 
		/**
		 * WPA and WPA2 Encrypt
		 */
		WPA_WPA2
	}
	
	private WifiHelper(Context context) {
		mContext = context;
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	}
	
	public static WifiHelper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new WifiHelper(context);
		}
		return sInstance;
	}
	
	/**
	 * @see WifiManager#isWifiEnabled()
	 */
	public boolean isWifiEnabled() {
		return mWifiManager.isWifiEnabled();
	}
	
	/**
	 * isWifiApEnabled
	 */
	public boolean isWifiApEnabled() {
		return WIFI_AP_STATE_ENABLED == getWifiApState();
	}
	
	/**
	 * Upper API 19
	 * @see WifiManager#isScanAlwaysAvailable()
	 */
	public boolean isScanAlwaysAvailable() {
        return Build.VERSION.SDK_INT >= 18 && mWifiManager.isScanAlwaysAvailable();
    }
	
	/**
	 * @see WifiManager#is5GHzBandSupported()
	 */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public boolean is5GHzBandSupported() {
		return mWifiManager.is5GHzBandSupported();
	}
	
	/**
	 * @see WifiManager#setWifiEnabled(boolean)
	 */
	public boolean setWifiEnabled(boolean enabled, @Nullable OnWifiStateListener listener) {
		mOnWifiStateListener = listener;
		
		if (enabled && isWifiApEnabled()) {
			if (!setWifiApDisabled()) {
				return false;
			}
		}
			
		if (mOnWifiStateListener != null) {
			registerWifiStateReceiver();
		}
		return mWifiManager.setWifiEnabled(enabled);
	}
	
	/**
	 * @see WifiManager#getWifiState()
	 */
	public int getWifiState() {
		return mWifiManager.getWifiState();
	}
	
	/**
	 * 获取Ap的状态
	 */
	public int getWifiApState() {
		int apState = WIFI_AP_STATE_FAILED;
		try {
			apState = (Integer) ReflectUtils.invokeMethod(mWifiManager, "getWifiApState");
		} catch (Exception e) {
			if (DEBUG) FLog.e(e);
		}
		return apState;
	}
	
	/**
	 * @see WifiManager#disableNetwork(int)
	 */
	public boolean disableNetwork(int networkId) {
		return mWifiManager.disableNetwork(networkId);
	}
	
	public WifiManager getWifiManager() {
		return mWifiManager;
	}
	
	public void createWifiLock(String tag) {
		mWifiLock = mWifiManager.createWifiLock(tag);
	}
	
	/**
	 * Use Permission
	 * {@link android.Manifest.permission#WAKE_LOCK}
	 */
	public void acquireWifiLock() {
		try {
			mWifiLock.acquire();
		} catch (SecurityException e) {
			FLog.e(e);
		}
	}
	
	public void releaseWifiLock() {
		if (mWifiLock.isHeld()) {
			mWifiLock.release();
		}
	}
	
	/**
	 * @see WifiManager#getConfiguredNetworks()
	 */
	public List<WifiConfiguration> getWifiConfigurationList() {
		return mWifiManager.getConfiguredNetworks();
	}
	
	/**
	 * @see WifiManager#startScan()
	 */
	public void startScan() {
		mWifiManager.startScan();
	}
	
	/**
	 * 删除重复的ScanResult
	 */
	public static List<ScanResult> removeDuplicatedScanResults(List<ScanResult> scanResults) {
		List<ScanResult> results = new ArrayList<>();
		if (scanResults == null || scanResults.size() == 0) return results;
		
		Collections.sort(scanResults, new ScanResultsComparator());
		for (ScanResult result : scanResults) {
			if (!isScanResultValid(result)) {
				continue;
			}
			
			if (!isScanResultListContained(results, result)) {
				results.add(result);
			}
		}
		return results;
	}

	/**
	 * Upper API 18
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public void openScanAlwayAvailablePermission(Activity a, OpenScanAlwayAvailableCallback callback) {
		if (Build.VERSION.SDK_INT >= 18) {
            mOpenScanAlwayAvailableCallback = callback;
            a.startActivityForResult(new Intent(WifiManager.ACTION_REQUEST_SCAN_ALWAYS_AVAILABLE), REQUEST_CODE_SCAN_ALWAYS_AVAILABLE);
        }
	}
	
	public static boolean isScanResultListContained(List<ScanResult> resultList, ScanResult result) {
		for (ScanResult r : resultList) {
			if (r.SSID.equals(result.SSID) && r.capabilities.equals(result.capabilities)) {
				return true;
			}
		}
		return false;
	}
	
	public List<ScanResult> getSingleScanResults() {
		return removeDuplicatedScanResults(getScanResults());
	}

	public List<ScanResult> getScanResults() {
		boolean b = mWifiManager.startScan();
		if (b) {
			return mWifiManager.getScanResults();
		}
		return new ArrayList<>();
	}
	
	/**
	 * @see WifiManager#getScanResults()
	 */
	public void getScanResults(OnScanResultsAvailableListener listener) {
		mOnScanResultsAvailableListener = listener;
		
		if (mOnScanResultsAvailableListener != null) {
			startScan();
			registerScanResultsAvailableReceiver();
		}
	}
	
	/**
	 * @see WifiInfo#getMacAddress()
	 */
	public String getMacAddress() {
		return getConnectionInfo().getMacAddress();
	}
	
	/**
	 * @see WifiInfo#getBSSID()
	 */
	public String getBSSID() {
		if (getConnectionInfo() != null) {
			getConnectionInfo().getBSSID();
		}
		return "";
	}
	
	/**
	 * @see WifiInfo#getIpAddress()
	 */
	public int getLocalIpAddress() {
		if (getConnectionInfo() != null) {
			getConnectionInfo().getIpAddress();
		}
		return 0;
	}
	
	/**
	 * @see WifiInfo#getNetworkId()
	 */
	public int getNetworkId() {
		if (getConnectionInfo() != null) {
			getConnectionInfo().getNetworkId();
		}
		return 0;
	}
	
	/**
	 * @see WifiManager#removeNetwork(int)
	 */
	public boolean removeNetworkId(int networkId) {
		return mWifiManager.removeNetwork(networkId);
	}
	
	public boolean disconnect() {
		return mWifiManager.disconnect();
	}
	
	public void disconnect(int netId) {
		mWifiManager.disableNetwork(netId);
		if (mNewNetworkId != -1) {
			if (mNewNetworkId != mOldNetworkId) {
				mWifiManager.removeNetwork(mNewNetworkId);
			} else {
				mWifiManager.enableNetwork(mOldNetworkId, true);
			}
		}
	}
	
	/**
	 * WIFI是否已经连接
	 */
	public boolean isWifiConnected() {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            for (Network network : networks) {
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
                if (networkInfo != null && networkInfo.isConnected()) {
                    return true;
                }
            }
            return false;
        } else {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return networkInfo != null && networkInfo.isConnected();
        }
	}
	
	/**
	 * after android 4.2 ssid will have Double Quotes;
	 * 
	 * Requires Permission: 
	 * {@link android.Manifest.permission#ACCESS_WIFI_STATE}
	 * @see WifiManager#getConnectionInfo()
	 */
	public WifiInfo getConnectionInfo() {
		try {
			return mWifiManager.getConnectionInfo();
		} catch(SecurityException e) {
			FLog.e("Requires Permission: android.Manifest.permission#ACCESS_WIFI_STATE");
		}
		return null;
	}
	
	public void setOnWifiRssiChangedListener(OnWifiRssiChangedListener listener) {
		mOnWifiRssiChangedListener = listener;
		registerWifiRssiChangedReceiver();
	}
	
	public boolean connect(String ssid, SimpleOnWifiConnectListener listener) {
		return connect(ssid, false, listener);
	}
	
	public boolean connect(String ssid, boolean isConnectFailureRestore, SimpleOnWifiConnectListener listener) {
		return connect(ssid, null, "", SecurityMode.OPEN, isConnectFailureRestore, listener);
	}

	public boolean connect(String ssid, String password, SimpleOnWifiConnectListener listener) {
		return connect(ssid, null, password, SecurityMode.WPA_WPA2, false, listener);
	}
	
	public boolean connect(String ssid, String password, SecurityMode mode, boolean isConnectFailureRestore
			, SimpleOnWifiConnectListener listener) {
		return connect(ssid, null, password, mode, isConnectFailureRestore, listener);
	}
	
	public boolean connect(String ssid, String bssid, String password, SecurityMode mode
			, boolean isConnectFailureRestore, SimpleOnWifiConnectListener listener) {
		if (mode != SecurityMode.OPEN) {
			if (password == null || password.length() < 8) {
				if (listener != null) {
					listener.onWifiConnectFailure(ERROR_CODE_CONNECT_AUTHENTICATE);
					return false;
				}
			}
		}
		return connect(createWifiConfiguration(removeDoubleQuotesIfExists(ssid), bssid, password, mode), isConnectFailureRestore, listener);
	}
	
	public boolean connect(WifiConfiguration config, boolean isConnectFailureRestore, SimpleOnWifiConnectListener listener) {
		mIsConnectFailureRestore = isConnectFailureRestore;
		
		if (!isWifiEnabled()) {
			listener.onWifiConnectFailure(ERROR_CODE_WIFI_NOT_OPEN);
			return false;
		}
		
		if (!isScanResultsContained(removeDoubleQuotesIfExists(config.SSID))) {
			listener.onWifiConnectFailure(ERROR_CODE_WIFI_NOT_FOUND);
			return false;
		}
		
		WifiInfo wifiInfo = getConnectionInfo();
		if (wifiInfo != null) {
			mOldNetworkId = wifiInfo.getNetworkId();
		}
		
		mOnWifiConnectListener = listener;
		if (mOnWifiConnectListener != null) {
			registerWifiConnectReceiver();
		}
		int networkId = mWifiManager.addNetwork(config);
		mNewNetworkId = networkId;
		return mWifiManager.enableNetwork(networkId, true);
	}

	public WifiConfiguration createWifiConfiguration(String ssid, @Nullable String bssid, String password, SecurityMode mode) {
		return createWifiConfiguration(ssid, bssid, password, mode, false);
	}
	
	WifiConfiguration createWifiApConfiguration(String ssid, String password) {
		return createWifiConfiguration(ssid, "", password, SecurityMode.WPA_WPA2, true);
	}

	WifiConfiguration createWifiConfiguration(String ssid, String bssid, String password
			,SecurityMode mode, boolean isWifiAp) {
		
		WifiConfiguration configuration = new WifiConfiguration();
		configuration.allowedAuthAlgorithms.clear();
		configuration.allowedGroupCiphers.clear();
		configuration.allowedKeyManagement.clear();
		configuration.allowedPairwiseCiphers.clear();
		configuration.allowedProtocols.clear();
		configuration.SSID = mConnectSSID = isWifiAp ? ssid : NumberUtils.isHexNumber(ssid) ? ssid : addDoubleQuotesIfNotExists(ssid);
		configuration.BSSID = bssid;
		
		WifiConfiguration tempConfiguration = isContainedInConfiguraionList(ssid);
		if (tempConfiguration != null) {
			mWifiManager.removeNetwork(tempConfiguration.networkId);
			mWifiManager.saveConfiguration();
		}
		
		switch (mode) {
		case OPEN:
			configuration.allowedKeyManagement.set(KeyMgmt.NONE);
			break;
		case WEP:
            int length = password.length();
            configuration.wepKeys[0] = (length == 10 || length == 26 || length == 58) 
            		&& password.matches("[0-9A-Fa-f]*") ? password : '"' + password + '"';
			configuration.hiddenSSID = true;
			configuration.wepTxKeyIndex = 0;
			configuration.allowedKeyManagement.set(KeyMgmt.NONE);
			configuration.allowedGroupCiphers.set(GroupCipher.WEP40);
			configuration.allowedGroupCiphers.set(GroupCipher.WEP104);
			configuration.allowedGroupCiphers.set(GroupCipher.CCMP);
			configuration.allowedGroupCiphers.set(GroupCipher.TKIP);
			configuration.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
			configuration.allowedAuthAlgorithms.set(AuthAlgorithm.SHARED);
			break;
		case WPA:
		case WPA2:
		case WPA_WPA2:
			configuration.hiddenSSID = true;
			configuration.preSharedKey = isWifiAp ? password : addDoubleQuotesIfNotExists(password);
			configuration.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
			configuration.allowedGroupCiphers.set(GroupCipher.CCMP);
			configuration.allowedGroupCiphers.set(GroupCipher.TKIP);
			configuration.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
			configuration.allowedPairwiseCiphers.set(PairwiseCipher.TKIP);
			configuration.allowedPairwiseCiphers.set(PairwiseCipher.CCMP);
			configuration.status = WifiConfiguration.Status.ENABLED;
			break;
		}
		return configuration;
	}
	
	/**
	 * set Wifi Ap
	 */
	public boolean setWifiApEnabled(String ssid, String password) {
		WifiConfiguration wifiApConfig = createWifiApConfiguration(ssid, password);
		try {
			if (!setWifiEnabled(false, null)) return false;
			
			return (Boolean)ReflectUtils.invokeMethod(mWifiManager, "setWifiApEnabled", wifiApConfig, true);
		} catch (Exception e) {
			FLog.e(e);
		}
		return false;
	}
	
	/**
	 * Diabled Wifi AP
	 */
	public boolean setWifiApDisabled() {
		try {
			return (Boolean)ReflectUtils.invokeMethod(mWifiManager, "setWifiApEnabled"
					, createWifiApConfiguration("JasonFangEngineer", "18600635950"), false);
		} catch (Exception e) {
			FLog.e(e);
		}
		return false;
	}
  
	/**
	 * 是否包含在本地列表中
	 */
	private WifiConfiguration isContainedInConfiguraionList(String ssid) {
		List<WifiConfiguration> configurationList = mWifiManager.getConfiguredNetworks();
		for (WifiConfiguration configuration : configurationList == null ? new ArrayList<WifiConfiguration>() : configurationList) {
			if (ssid.equals(removeDoubleQuotesIfExists(configuration.SSID))) {
				return configuration;
			}
		}
		return null;
	}
	
	private boolean isScanResultsContained(String ssid) {
		List<ScanResult> scanResults = getScanResults();
		if (scanResults == null || scanResults.size() == 0) {
			return false;
		}
		
		for (ScanResult result : scanResults) {
			if (result.SSID.equals(ssid)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isSsidValid(String ssid) {
		return (!(TextUtils.isEmpty(ssid))
                && !(ssid.equalsIgnoreCase("<unknown ssid>")
				|| !ssid.contains("[IBSS]")
				|| !ssid.equals("0x")));
	}
	
	public static boolean isScanResultValid(ScanResult result) {
		return !("wifi".equals(result.SSID)) && !"00:00:00:00:00:00".equals(result.BSSID);
	}
	
	/**
	 * 获取信道值
	 */
	public static int getChannelValue(int frequency) {
		if (frequency > 5000) {
			return (frequency - 5000) / 5;
		} else if (frequency < 3000) {
			if (frequency == 2484) return 14;
			return ((frequency - 2407)) / 5;
		} else {
			return ((frequency - 4000) / 5);
		}
	}
	
	/**
	 * 获取连接状态
	 */
	public static String getConnectingStatusMsg(int statusCode) {
		switch (statusCode) {
		case CONNECT_STATUS_AUTHENTICATING:
			return "验证中...";
		case CONNECT_STATUS_CONNECTING:
			return "连接中...";
		case CONNECT_STATUS_OBTAINING_IPADDR:
			return "获取IP中...";
		}
		return "未知!";
	}
	
	public static String getConnectErrorMsg(int statusCode) {
		switch (statusCode) {
		case ERROR_CODE_CONNECT_AUTHENTICATE:
			return "验证错误!";
		case ERROR_CODE_CONNECT_INVALID:
			return "无效连接!";
		case ERROR_CODE_CONNECT_TIMEOUT:
			return "连接超时!";
		case ERROR_CODE_WIFI_NOT_FOUND:
			return "无用的WIFI!";
		case ERROR_CODE_WIFI_NOT_OPEN:
			return "WIFI未打开!";
		default:
			return "未知错误!";
		}
	}
	
	/**
	 * 去除SSID的双引号
	 */
	public static String removeDoubleQuotesIfExists(String string) {
		if (TextUtils.isEmpty(string)) return string;
		
		int length = string.length();
        if ((length > 1) && (string.charAt(0) == '"')
                && (string.charAt(length - 1) == '"')) {
            return string.substring(1, length - 1);
        }
        return string;
	}
	
	/**
	 * 添加双引号(如果没有的话)
	 */
	public static String addDoubleQuotesIfNotExists(String string) {
		if (TextUtils.isEmpty(string)) return string;
		
		int length = string.length();
		if ((length > 1) && (string.charAt(0) == '"')
                && (string.charAt(length - 1) == '"')) {
            return string;
        }
		
        return "\"" + string + "\"";
    }
	
    
    /**
     * 获取加密模式
     */
    public static SecurityMode getSecurityMode(String capabilities) {
    	boolean wpa = capabilities.contains("WPA-PSK");
        boolean wpa2 = capabilities.contains("WPA2-PSK");
        boolean wep = capabilities.contains("WEP");
        if (wpa2 && wpa) {
            return SecurityMode.WPA_WPA2;
        } else if (wpa2) {
            return SecurityMode.WPA2;
        } else if (wpa) {
            return SecurityMode.WPA;
        } else if (wep){
            return SecurityMode.WEP;
        } else {
        	return SecurityMode.OPEN;
        }
    }
    
    public static String getSecurityModeString(SecurityMode mode) {
    	if (mode == null) return "UNKNOWN";
    	
    	switch (mode) {
    	case OPEN:
    		return "无";
    	case WEP:
    		return "WEP";
    	case WPA:
    		return "WPA PSK";
    	case WPA2:
    		return "WPA2 PSK";
    	case WPA_WPA2:
    		return "WPA/WPA2 PSK";
    	default:
    		return "UNKNOWN";
    	}
    }
    
    /**
     * 获取信号强度
     */
    public static String getSignalStrength(int rssi) {
    	if (rssi >= -50) {
    		return "强";
    	} else if (rssi >= -70) {
    		return "中";
    	} else {
    		return "弱";
    	}
    }
    
    /**
     * 注册WifiWifi开关连接监听的广播
     */
    private void registerWifiStateReceiver() {
    	if (mWifiStateReceiver != null) return;
    	
    	mWifiStateReceiver = new WifiStateReceiver();
    	IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		mContext.registerReceiver(mWifiStateReceiver, filter);
    }
    
    /**
     * 反注册Wifi开关连接监听的广播
     */
    public void unregisterWifiStateReceiver() {
    	unregisterReceiver(mWifiStateReceiver);
    	mWifiStateReceiver = null;
    }
    
    /**
     * 注册Wifi连接连接监听的广播
     */
    private void registerWifiConnectReceiver() {
    	if (mWifiConnectReceiver != null) return;
    	
    	mWifiConnectReceiver = new WifiConnectReceiver();
    	IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		mContext.registerReceiver(mWifiConnectReceiver, filter);
    }
	
	/**
	 * 反注册Wifi连接连接监听的广播
	 */
	public void unregisterWifiConnectReceiver() {
		unregisterReceiver(mWifiConnectReceiver);
		mWifiConnectReceiver = null;
	}
	
	/**
	 * 注册Wifi信号改变的广播
	 */
	private void registerWifiRssiChangedReceiver() {
		if (mWifiRssiChangedReceiver != null) return;
		
		mWifiRssiChangedReceiver = new WifiRssiChangedReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
		mContext.registerReceiver(mWifiRssiChangedReceiver, filter);
	}
	
	/**
	 * 反注册Wifi信号改变的广播
	 */
	public void unregisterWifiRssiChangedReceiver() {
		unregisterReceiver(mWifiRssiChangedReceiver);
		mWifiRssiChangedReceiver = null;
	}
	
	/**
	 * 注册扫描结果可用的监听
	 */
	private void registerScanResultsAvailableReceiver() {
		if (mScanResultsAvailableReceiver != null) return;
		
		mScanResultsAvailableReceiver = new ScanResultsAvailableReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		mContext.registerReceiver(mScanResultsAvailableReceiver, filter);
	}
	
	/**
	 * 反注册扫描结果可用的监听
	 */
	public void unregisterScanResultsAvailableReceiver() {
		unregisterReceiver(mScanResultsAvailableReceiver);
		mScanResultsAvailableReceiver = null;
	}
	
	void unregisterReceiver(BroadcastReceiver receiver) {
		if (receiver != null) {
			try {
				mContext.unregisterReceiver(receiver);
			} catch (Exception e) {
				if (DEBUG) FLog.e(e);
			}
		}
	}
	
	/**
	 * 信号强度比较器
	 * @author Jason Fang
	 */
	static class ScanResultsComparator implements Comparator<ScanResult> {

		@Override
		public int compare(ScanResult lhs, ScanResult rhs) {
			return WifiManager.compareSignalLevel(rhs.level, lhs.level);
		}
		
	}
	
	/**
	 * 监听信号变化
	 * @author Yucun
	 *
	 */
	class WifiRssiChangedReceiver extends BaseBroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			super.onReceive(context, intent);
			if (mReceiveCount <= 1) return;
			
			String action = intent.getAction();
			if (WifiManager.RSSI_CHANGED_ACTION.equals(action)) {
				if (mOnWifiRssiChangedListener != null) {
					mOnWifiRssiChangedListener.onWifiRssiChanged();
				}
			}
		}
	}
	
	/**
	 * WIFI Switcher
	 * @author Jason Fang
	 *
	 */
	class WifiStateReceiver extends BaseBroadcastReceiver {

		public void onWifiEnabled() {
			if (mOnWifiStateListener != null) {
				mOnWifiStateListener.onWifiState(WIFI_STATE_ENABLED);
			}
			unregisterWifiStateReceiver();
		}
		
		public void onWifiDisabled() {
			if (mOnWifiStateListener != null) {
				mOnWifiStateListener.onWifiState(WIFI_STATE_DISABLED);
			}
			unregisterWifiStateReceiver();
		}
		
		@Override
		public void onReceive(Context context, Intent intent) {
			super.onReceive(context, intent);
			String action = intent.getAction();
			if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
				
				int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
				switch (wifiState) {
				case WifiManager.WIFI_STATE_ENABLED:
					onWifiEnabled();
					break;
				case WifiManager.WIFI_STATE_DISABLED:
					onWifiDisabled();
					break;
				case WifiManager.WIFI_STATE_ENABLING:
				case WifiManager.WIFI_STATE_DISABLING:
				default:
					break;
				}
			}
		}
	}
	
	/**
	 * 连接状态监听
	 *
	 */
	class WifiConnectReceiver extends BaseBroadcastReceiver {
		
		public void onWifiConnecting(int status) {
			if (mOnWifiConnectListener != null) {
				mOnWifiConnectListener.onWifiConnecting(status);
			}
		}
		
		public void onWifiConnectFailure(int errorCode) {
			if (mOnWifiConnectListener != null) {
				mOnWifiConnectListener.onWifiConnectFailure(errorCode);
			}
			
			if (mIsConnectFailureRestore) {
				mWifiManager.reconnect();
			}
		}
		
		public void onWifiConnectSuccess(WifiInfo wifiInfo) {
			if (mOnWifiConnectListener != null) {
				mOnWifiConnectListener.onWifiConnectSuccess(wifiInfo);
			}
		}
		
		@Override
		public void onReceive(Context context, Intent intent) {
			super.onReceive(context, intent);
			if (mReceiveCount <= 2) return;
			
			String action = intent.getAction();
			if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(action)) {
				
				if (mOnWifiConnectListener == null) {
					unregisterWifiConnectReceiver();
					return;
				}
				
				int errorCode = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, -1);
				if (errorCode == WifiManager.ERROR_AUTHENTICATING) {
					onWifiConnectFailure(ERROR_CODE_CONNECT_AUTHENTICATE);
					return;
				}
				
				DetailedState detailedstate = WifiInfo.getDetailedStateOf((SupplicantState)intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE));
				switch (detailedstate) {
				case SCANNING:
				case CONNECTING:
					onWifiConnecting(CONNECT_STATUS_CONNECTING);
					return;
				case AUTHENTICATING:
					onWifiConnecting(CONNECT_STATUS_AUTHENTICATING);
					return;
				case OBTAINING_IPADDR:
					onWifiConnecting(CONNECT_STATUS_OBTAINING_IPADDR);
					return;
				case FAILED:
					onWifiConnectFailure(ERROR_CODE_CONNECT_UNKNOWN);
					return;
				default:
					break;
				}
			}

			if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
				NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
				if (networkInfo != null && networkInfo.isConnected()) {
					
					WifiInfo wifiInfo = getConnectionInfo();
					String ssid = WifiHelper.removeDoubleQuotesIfExists(wifiInfo.getSSID());
					if (isSsidValid(ssid)) {
						if (ssid.equals(WifiHelper.removeDoubleQuotesIfExists(mConnectSSID))) {
							onWifiConnectSuccess(wifiInfo);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 扫描完毕监听
	 * @author Jason Fang
	 *
	 */
	class ScanResultsAvailableReceiver extends BaseBroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
				super.onReceive(context, intent);
				if (mReceiveCount <= 1) return;
				
				if (mOnScanResultsAvailableListener != null) {
					mOnScanResultsAvailableListener.onScanResultsAvailable(getScanResults());
					unregisterScanResultsAvailableReceiver();
				}
			}
		}
		
	}
	
	/**
	 * 连接超时控制类
	 * @author Jason Fang
	 *
	 */
	static class ConnectTimeoutTimer extends CountDownTimer {
		
		private static ConnectTimeoutTimer mTimeInstance;
		
		private ConnectTimeoutTimer(long secondInFuture) {
			super(secondInFuture * 1000, 1000);
		}
		
		private static ConnectTimeoutTimer getInstance() {
			if (mTimeInstance == null) {
				mTimeInstance = new ConnectTimeoutTimer(CONNECT_TIMEOUT);
			}
			return mTimeInstance;
		}
		
		public static void begin() {
			if (mTimeInstance != null) {
				mTimeInstance.cancel();
			}
			getInstance().start();
		}
		
		public static void stop() {
			if (mTimeInstance != null) {
				getInstance().cancel();
				mTimeInstance = null;
			}
		}
		
		@Override
		public void onTick(long millisUntilFinished) {
			if(DEBUG) FLog.i("onTick:" + millisUntilFinished / 1000);
		}

		@Override
		public void onFinish() {
			if (sInstance.mOnWifiConnectListener != null) {
				sInstance.mOnWifiConnectListener.onWifiConnectFailure(ERROR_CODE_CONNECT_TIMEOUT);
			}
		}
		
	}
	
	public interface OnWifiRssiChangedListener {
		void onWifiRssiChanged();
	}
	
	public interface OnWifiStateListener {
		void onWifiState(int state);
	}
	
	/**
	 * 连接抽象类
	 * @author Jason Fang
	 *
	 */
	public static class SimpleOnWifiConnectListener implements OnWifiConnectListener {
		
		@Override
		public void onWifiConnecting(int status) {
			ConnectTimeoutTimer.begin();
		}
		
		@Override
		public void onWifiConnectSuccess(WifiInfo wifiInfo) {
			sInstance.unregisterWifiConnectReceiver();
			ConnectTimeoutTimer.stop();
		}
		
		@Override
		public void onWifiConnectFailure(int errorCode) {
			sInstance.disconnect(sInstance.mNewNetworkId);
			sInstance.unregisterWifiConnectReceiver();
			ConnectTimeoutTimer.stop();
		}
	}
	
	private interface OnWifiConnectListener {
		void onWifiConnecting(int status);
		void onWifiConnectSuccess(WifiInfo wifiInfo);
		void onWifiConnectFailure(int errorCode);
	}
	
	/**
	 * 扫描完毕的回调
	 */
	public interface OnScanResultsAvailableListener {
		void onScanResultsAvailable(List<ScanResult> scanResults);
	}

	/**
	 * Open Scan Alway Available must invoke
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CODE_SCAN_ALWAYS_AVAILABLE:
			if (resultCode == Activity.RESULT_OK) {
				if (mOpenScanAlwayAvailableCallback != null) {
					mOpenScanAlwayAvailableCallback.openScanAlwayAvailableSuccess();
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				if (mOpenScanAlwayAvailableCallback != null) {
					mOpenScanAlwayAvailableCallback.openScanAlwayAvailableFailure();
				}
			}
			break;
		}
	}
	
	/**
	 * Open Scan Alway Available interface
	 * @author Jason Fang
	 */
	public interface OpenScanAlwayAvailableCallback {
		void openScanAlwayAvailableSuccess();
		void openScanAlwayAvailableFailure();
	}
}
