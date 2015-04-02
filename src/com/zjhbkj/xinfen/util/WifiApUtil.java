package com.zjhbkj.xinfen.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.zjhbkj.xinfen.listener.StartWifiApListener;
import com.zjhbkj.xinfen.listener.TimerCheckListener;

/**
 * 创建wifi热点
 * 
 * @author zou.sq
 * 
 */
public class WifiApUtil {
	public static final String TAG = "WifiHotAdmin";
	private WifiManager mWifiManager;
	private Context mContext;
	private String mSSID;
	private String mPasswd;
	private StartWifiApListener mStartWifiApListener;

	public static void closeWifiAp(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		closeWifiAp(wifiManager);
	}

	public WifiApUtil(Context context) {
		mContext = context;
		mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		closeWifiAp(mWifiManager);
	}

	public void startWifiAp(String ssid, String passwd, StartWifiApListener listener) {
		mStartWifiApListener = listener;
		mSSID = ssid;
		mPasswd = passwd;
		if (mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(false);
		}
		stratWifiAp();
		final TimerCheckUtil timerCheck = new TimerCheckUtil(new TimerCheckListener() {

			@Override
			public boolean doTimerCheckWork() {
				boolean isEnable = isWifiApEnabled(mWifiManager);
				Log.d("aaa", "----" + isEnable);
				if (isEnable && null != mStartWifiApListener) {
					mStartWifiApListener.enableWifiApSuccess();
				}
				return isEnable;
			}

			@Override
			public void doTimeOutWork() {
				if (null != mStartWifiApListener) {
					mStartWifiApListener.enableWifiApFail();
				}
			}
		});
		timerCheck.start(30, 1000);
	}

	public void stratWifiAp() {
		Method method1 = null;
		try {
			method1 = mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
			WifiConfiguration netConfig = new WifiConfiguration();

			netConfig.SSID = mSSID;
			netConfig.preSharedKey = mPasswd;

			netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
			netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
			netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
			netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			method1.invoke(mWifiManager, netConfig, true);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	private static void closeWifiAp(WifiManager wifiManager) {
		if (isWifiApEnabled(wifiManager)) {
			try {
				Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
				method.setAccessible(true);
				WifiConfiguration config = (WifiConfiguration) method.invoke(wifiManager);
				Method method2 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class,
						boolean.class);
				method2.invoke(wifiManager, config, false);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	private static boolean isWifiApEnabled(WifiManager wifiManager) {
		try {
			Method method = wifiManager.getClass().getMethod("isWifiApEnabled");
			method.setAccessible(true);
			return (Boolean) method.invoke(wifiManager);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}