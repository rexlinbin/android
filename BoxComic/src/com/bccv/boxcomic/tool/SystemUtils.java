package com.bccv.boxcomic.tool;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.WindowManager;

public class SystemUtils {
	public static void setCurrentActivityBrightness (Activity activity, float bright) {
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.screenBrightness = bright;
		activity.getWindow().setAttributes(lp);
	}
	
	public static int getCurrentActivityBrightness (Activity activity) {
		int value = 0;
	    ContentResolver cr = activity.getContentResolver();
	    try {
	        value = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
	    } catch (SettingNotFoundException e) {
	        
	    }
	    return value;
	}
	
	public static String getNetState(Context context){
		boolean isWIFI = isWIFIConnectivity(context);
		boolean isMobile = isMobileConnectivity(context);
		String stateString = "";
		if (isMobile) {
			stateString = "3G";
		}
		
		if (isWIFI) {
			stateString = "WIFI";
		}
		return stateString;
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
