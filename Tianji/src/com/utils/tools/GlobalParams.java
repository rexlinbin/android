package com.utils.tools;



import java.net.Socket;

import android.R.integer;
import android.app.Activity;
import android.content.Context;

import com.bccv.tianji.model.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.utils.net.TcpClientHelper;
import com.utils.net.TcpServerHelper;
import com.utils.net.UdpHelper;

public class GlobalParams {
	public static String PROXY_IP = "";
	public static int PROXY_PORT = 0;
	
	public static int rawX = 0;
	public static int rawY = 0;
	
	public static int ControllerServerPort = 9001;
	public static int BroadCastPort = 9051;
	/**
	 * 全局Context
	 */
	public static Context context;
	/**
	 * ImageLoader的全局配置
	 */
	public static DisplayImageOptions options;

	public static DisplayImageOptions Toppic;
	public static DisplayImageOptions Intropic;

	
	public static DisplayImageOptions iconOptions;
	public static DisplayImageOptions typeOptions;
	
	

	
	public static boolean hasLogin = false;
	public static Activity activity;
	
	public static UdpHelper udpHelper, controllerHelper;
	public static TcpServerHelper tcpServerHelper;
	public static Socket controllerSocket;
	
	public static TcpClientHelper tcpClientHelper;
	
	/**
	 * 百度播放器
	 */
	public static String AK = "6ef3647aa17d4fc6bade3a8cd63b940b";
	public static String SK = "b608b2ecd74642689eb6a7c5592ac9ca";
	public static String ImagePathString = "";

	public static User user = null;
	public static String auth_id;
}
