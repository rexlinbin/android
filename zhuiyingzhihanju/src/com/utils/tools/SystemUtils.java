package com.utils.tools;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.WindowManager;

public class SystemUtils {
	/**
	 * 修改屏幕亮度
	 * 
	 * @param activity
	 * @param bright
	 *            0.0 ~ 1.0
	 */
	public static void setCurrentActivityBrightness(Activity activity, float bright) {
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.screenBrightness = bright;
		activity.getWindow().setAttributes(lp);
	}
	public static float getCurrentActivityBrightness1(Activity activity) {
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		return lp.screenBrightness;
	}
	public static int getCurrentActivityBrightness(Activity activity) {
		int value = 0;
		ContentResolver cr = activity.getContentResolver();
		try {
			value = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException e) {

		}
		return value;
	}

	/**
	 * 逐步设置音量
	 * @param activity
	 * @param flag 0为降低，1为增加
	 */
	public static void setCurrentActivityColumn(Activity activity,int flag) {
		AudioManager mAudioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
		//降低音量，调出系统音量控制
		if (flag == 0) {
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER,
					AudioManager.FX_FOCUS_NAVIGATION_UP);
		}
		// 增加音量，调出系统音量控制
		else if (flag == 1) {
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE,
					AudioManager.FX_FOCUS_NAVIGATION_UP);
		}
	}

	public static void setCurrentActivityColumn(Activity activity, int num, int flag) {
		AudioManager mAudioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
		int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		if (num > max) {
			num = max;
		}else if (num < 0){
			num = 0;
		}
		
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, num, 0);
	}

	public static int getCurrentActivityColumn(Activity activity) {
		AudioManager mAudioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
		return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		
	}
	
	public static int getMaxActivityColumn(Activity activity) {
		AudioManager mAudioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
		return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		
	}
	
	public static String getNetState(Context context) {
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
	 * 
	 * @param context
	 * @return
	 */
	@SuppressWarnings("deprecation")
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
	 * 
	 * @param context
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static boolean isWIFIConnectivity(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (networkInfo != null) {
			return networkInfo.isConnected();
		}

		return false;
	}
}
