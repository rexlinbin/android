package com.boxuu.gamebox.common;

import java.io.File;

import android.os.Environment;

public class GlobalConstants {

	public static final String HOST = ""; // 服务器地址
	public static final String REPOST_HOST = ""; //	服务器地址

	public static final String WEBURL = ""; // webview 加载地址
	
	public static final String GAME_HOST_URL = "http://m.cgame.cn/smallgame/index/"; //游戏列表主页
//	public static final String GAME_HOST_URL_HOT = "http://mg.cgame.cn/home/index/hot/"; //游戏列表 热门
//	public static final String GAME_HOST_URL_RANK = "http://mg.cgame.cn/home/index/rank/"; //游戏列表 排行
	
	public static final String GAME_URL = "/gg/";	//打开游戏的url 包含的字段
	
	public static final String BASE_PATH = Environment.getExternalStorageDirectory().toString();
	public static final String GAME_PATH = File.separator+"gamebox" + File.separator + "download/";
}
