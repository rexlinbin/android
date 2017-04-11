package com.bccv.boxcomic.sns.util;

import android.content.Context;

import com.bccv.boxcomic.sns.SNSLoginManager;
import com.bccv.boxcomic.sns.SNSShareManager;

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
