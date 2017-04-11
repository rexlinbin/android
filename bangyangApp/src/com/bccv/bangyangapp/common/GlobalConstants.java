package com.bccv.bangyangapp.common;

import android.os.Environment;

import com.bccv.bangyangapp.utils.ScreenUtil.Screen;

public class GlobalConstants {

	public static final String HOST = "http://appsapi.bccv.com/an/pretty"; // 服务器地址
	public static final String REPOST_HOST = ""; //	服务器地址

	public static final String WEBURL = ""; // webview 加载地址
	
	public static Screen screenPix ;
	public static final String md5String = "63761cd04d51c46da61ae17b123cdcc9";
	
	public static final String USER_INFO_CHANGE_BROADCAST = "com.bccv.bangyangapp.action.userinfochange";
	
	public static final int SEARCH4REQUEST = 101;
	public static final int SEARCH4RESULT = 102;
	
	public static final String USER_INFO_CHANGE_TYPE = "user_info_change_type";
	public static final int LOGIN_SUCCESS = 1;
	public static final int LOGOUT_SUCCESS = 2;

	public static final String user_agentString = "bangyangforbccv";
	
	public static final String launcher_path = Environment.getExternalStorageDirectory()+"/bangyang/ic_launcher.png";
}
