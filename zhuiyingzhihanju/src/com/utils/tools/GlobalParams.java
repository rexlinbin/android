package com.utils.tools;



import com.bccv.zhuiyingzhihanju.model.User;
import com.igexin.sdk.PushBuildConfig;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import android.content.Context;

public class GlobalParams {
	public static String PROXY_IP = "";
	public static int PROXY_PORT = 0;
	
	public static int rawX = 0;
	public static int rawY = 0;
	
	public static int ControllerServerPort = 9001;
	public static int BroadCastPort = 9051;
	
	public static String md5String;
	public static String md5Key = "ee8e14a98f631b21c570ff305217cff3";
	
	public static String dataUrl = "http://hanju.zhuiying.me";
	public static String videoUrl = "http://api.zhensaikeji.com";
	public static String toFriendUrl = "http://downres.zhuiying.me/hanjumiapk/own.apk";
	public static String toFriendText = "推荐给你一个看韩剧的应用：";
	public static int magnet = 0;

	public static boolean isNewest = true;
	public static String apptype ="hanjumi";
	public static String appid="4";
	
	
	
	/**
	 * 全局Context
	 */
	public static Context context;
	/**
	 * ImageLoader的全局配置
	 */
	public static DisplayImageOptions options;
	public static DisplayImageOptions movieOptions;
	public static DisplayImageOptions bannerOptions;
	public static DisplayImageOptions foundOptions;
	public static DisplayImageOptions iconOptions;
	public static DisplayImageOptions downloadOptions;
	public static DisplayImageOptions loadingOptions;
	
	public static boolean hasLogin = false;
	public static boolean hasMSG = false;
	
//	public static Activity activity;
	
	/**
	 * 下载地址
	 */
	public static String downloadPath;
	
	/**
	 * 百度播放器
	 */
	public static String AK = "6ef3647aa17d4fc6bade3a8cd63b940b";
	public static String SK = "b608b2ecd74642689eb6a7c5592ac9ca";
	public static String ImagePathString = "";
	
	
	
	
	/**
	 * 设置
	 */
	
	public static boolean isTui=true;
	
	public static boolean isWifi=false;
	
	
	
	
	
	public static User user = null;
	
	public static boolean canWebCache = false;
	
	public static String client_id;
	
	

}
