package com.bccv.bangyangapp.sns.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * 判断网络类型
 * @author Administrator
 */
public class NetUtil {
	
	/** WAP代理IP */
	public static String PROXY_IP = "";
	
	/** WAP代理端口 */
	public static int PROXY_PORT = 80;
	
	/**
	 * 检查网络工具
	 * @return
	 */
	public static boolean checkNetWork(Context context) {
		boolean isWIFI = isWIFIConnectivity(context);
		boolean isMobile = isMobileConnectivity(context);

		if (!isWIFI && !isMobile) {
			return false;
		}
		if (isMobile) {
			readAPN(context);
		}

		return true;
	}

	/**
	 * 读取操作——APN
	 * @param context
	 */
	private static void readAPN(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		
		if(networkInfo.getExtraInfo().toLowerCase().contains("wap")) {
			TelephonyManager telephonyManager = (TelephonyManager) context  
	                .getSystemService(Context.TELEPHONY_SERVICE); 
			String IMSI = telephonyManager.getSubscriberId();  
			// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。  
	        if(null != IMSI && IMSI.startsWith("46003")) {
	        	NetUtil.PROXY_IP = "10.0.0.200";
	        } else {
	        	NetUtil.PROXY_IP = "10.0.0.172";
	        }
		}
	}

	/**
	 * 判断移动网络连接状态
	 * @param context
	 * @return
	 */
	private static boolean isMobileConnectivity(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (networkInfo != null) {
			return networkInfo.isConnected();
		}

		return false;
	}

	/**
	 * 判断WIFI连接状态
	 * @param context
	 * @return
	 */
	private static boolean isWIFIConnectivity(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (networkInfo != null) {
			return networkInfo.isConnected();
		}

		return false;
	}
}