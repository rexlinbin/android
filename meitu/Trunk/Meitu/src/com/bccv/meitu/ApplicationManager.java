package com.bccv.meitu;

import android.app.Application;
import android.content.Context;

import com.bccv.meitu.api.DownLoadAPI;
import com.bccv.meitu.sns.UserInfoManager;
import com.bccv.meitu.sns.util.SNSUtil;
import com.bccv.meitu.utils.ActivityQueue;
import com.bccv.meitu.utils.CrashHandler;
import com.bccv.meitu.utils.RCUtil;
import com.bccv.meitu.utils.SystemUtil;

public class ApplicationManager extends Application {

	private static ApplicationManager appInstances;
	public static final String APP_KEY = "z3v5yqkbv8v30";
	
	@Override
	public void onCreate() {
		super.onCreate();
		appInstances = this;
		
	       try {
	            System.loadLibrary("imdemo");
	        } catch (UnsatisfiedLinkError e) {
//	            e.printStackTrace();
	        }
		
		
		RCUtil.initRC(getGlobalContext());//初始化融云
		SystemUtil.init(getGlobalContext());
		DownLoadAPI.init(getGlobalContext());
		CrashHandler.getInstance().init(this.getApplicationContext());
		UserInfoManager.init(getGlobalContext());
		SNSUtil.initSNS(getGlobalContext());
	}

	public static ApplicationManager getInstance() {
		return appInstances;
	}
	
	/**
	 * 获取全局的context
	 * 
	 * @return
	 */
	public static Context getGlobalContext() {
		return appInstances.getApplicationContext();
	}
	/**
	 * exit app
	 * 
	 * @return void
	 * @throws
	 */
	public void exitSystem() {
		RCUtil.disconnect(false);
		ActivityQueue.finishAllActivity();
		AppExit(this);
	}

	/**
	 * 退出应用程序
	 */
	private void AppExit(Context context) {
		// 停止缓存下载服务
		System.exit(0);
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
}
