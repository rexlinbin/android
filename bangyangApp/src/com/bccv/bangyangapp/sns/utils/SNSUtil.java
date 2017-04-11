package com.bccv.bangyangapp.sns.utils;

import android.content.Context;

import com.bccv.bangyangapp.sns.SNSLoginManager;
import com.bccv.bangyangapp.sns.SNSShareManager;

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
