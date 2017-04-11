package com.utils.net;



import com.utils.tools.GlobalParams;
import com.utils.tools.StringUtils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;


/**
 * 检测网络是否可用&网络类型
 * @author WuYelin
 *
 */
public class NetUtil {
	public static final int NETTYPE_WIFI = 0x01;
	public static final int NETTYPE_CMWAP = 0x02;
	public static final int NETTYPE_CMNET = 0x03;
	
	//当前正在处于连接的APN信息
	private static Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
	
	
	/**
	 * 判断是否能够连接网络
	 */
	public static boolean isNetworkAvailable(Context context) {
		boolean isWIFI = isWIFI(context);
		boolean isMobile = isMobile(context);

		if (!isWIFI && !isMobile) {
			return false;
		}
		
		// 判断APN列表中哪个渠道
		if(isMobile)
		{
			// 读取当前处于连接状态的的Apn的配置信息：ip和端口
//			readAPN(context);//联系人信息的读取基本一致
		}
		return true;
	}
	
	/**
	 * 读取当前处于连接状态的的Apn的配置信息：ip和端口
	 * @param context
	 */
	protected static void readAPN(Context context) {
		ContentResolver contentResolver = context.getContentResolver();
		Cursor query = contentResolver.query(PREFERRED_APN_URI, null, null, null, null);
		if(query!=null&&query.moveToFirst())
		{
			//读取ip和port
			GlobalParams.PROXY_IP=query.getString(query.getColumnIndex("proxy"));
			GlobalParams.PROXY_PORT=query.getInt(query.getColumnIndex("port"));
		}
		
	}

	/**
	 * 判断是否WIFI处于连接状态
	 * 
	 * @return
	 */
	public static boolean isWIFI(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (networkInfo != null) {
			return networkInfo.isConnected();
		}
		return false;
	}

	/**
	 * 判断是否APN列表中某个渠道处于连接状态
	 * 
	 * @return
	 */
	public static boolean isMobile(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (networkInfo != null) {
			return networkInfo.isConnected();
		}
		return false;
	}
	

	/**
	 * 获取当前网络类型
	 * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
	 */
	@SuppressLint("DefaultLocale")
	public static int getNetworkType(Context context) {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}		
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if(!StringUtils.isEmpty(extraInfo)){
				if (extraInfo.toLowerCase().equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}

}
