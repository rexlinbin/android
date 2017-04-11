package com.boxuu.gamebox.sns.utils;

import android.content.Context;

import com.boxuu.gamebox.sns.SNSLoginManager;
import com.boxuu.gamebox.sns.SNSShareManager;

public class SNSUtil {

	private static String TAG = "SNSUtil";
	
	/**
	 * 初始化SNS
	 * @param context
	 */
	public static void initSNS(Context context){
		SNSLoginManager.init(context);
		SNSShareManager.init(context);
	}
	
}
