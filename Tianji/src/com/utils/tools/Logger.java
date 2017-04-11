package com.utils.tools;

import android.util.Log;

/**
 * 日志管理工具
 * @author 
 *
 */
public class Logger {
	public static int LOG_LEVEL=6;
	public static final int VERBOSE = 5;
	public static final int DEBUG = 4;
	public static final int INFO = 3;
	public static final int WARN = 2;
	public static final int ERROR = 1;
	
	
	public static void v(String tag, String msg){
		if(LOG_LEVEL>VERBOSE && msg != null){
			Log.v(tag, msg);
		}
	}
	public static void d(String tag, String msg){
		if(LOG_LEVEL>DEBUG && msg != null){
			Log.d(tag, msg);
		}
	}
	public static void i(String tag, String msg){
		if(LOG_LEVEL>INFO && msg != null){
			Log.i(tag, msg);
		}
	}
	public static void w(String tag, String msg){
		if(LOG_LEVEL>WARN && msg != null){
			Log.w(tag, msg);
		}
	}
	public static void e(String tag, String msg){
		if(LOG_LEVEL>ERROR && msg != null){
			Log.e(tag, msg);
		}
	}
}