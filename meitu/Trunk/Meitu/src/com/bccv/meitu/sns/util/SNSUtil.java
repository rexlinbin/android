package com.bccv.meitu.sns.util;

import android.content.Context;

import com.bccv.meitu.sns.SNSLoginManager;
import com.bccv.meitu.sns.SNSShareManager;

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
