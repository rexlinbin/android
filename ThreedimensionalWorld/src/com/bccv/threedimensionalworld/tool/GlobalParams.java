package com.bccv.threedimensionalworld.tool;

import android.app.Activity;
import android.content.Context;





import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class GlobalParams {
	public static String PROXY_IP = "";
	public static int PROXY_PORT = 0;
	/**
	 * 全局Context
	 */
	public static Context context;
	/**
	 * ImageLoader的全局配置
	 */
	public static DisplayImageOptions options;
	
	public static DisplayImageOptions leftbgOptions;
	public static DisplayImageOptions rightbgOptions;
	
	public static DisplayImageOptions appIconOptions;
	public static DisplayImageOptions appInfoOptions;
	
	public static boolean hasLogin = false;
	public static Activity activity;
	
	/**
	 * 百度播放器
	 */
	public static String AK = "6ef3647aa17d4fc6bade3a8cd63b940b";
	public static String SK = "b608b2ecd74642689eb6a7c5592ac9ca";
	public static String ImagePathString = "";
}
