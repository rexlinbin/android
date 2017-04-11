package com.bccv.strategy.sns.utils;

import android.content.Context;

import com.bccv.strategy.sns.SNSLoginManager;
import com.bccv.strategy.sns.SNSShareManager;

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
