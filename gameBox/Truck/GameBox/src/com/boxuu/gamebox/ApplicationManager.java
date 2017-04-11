package com.boxuu.gamebox;

import android.app.Application;
import android.content.Context;

import com.boxuu.gamebox.sns.UserInfoManager;
import com.boxuu.gamebox.sns.utils.SNSUtil;
import com.boxuu.gamebox.ui.utils.ActivityQueue;
import com.boxuu.gamebox.utils.SystemUtil;

public class ApplicationManager extends Application {

	private static ApplicationManager appInstances;
//	public static final String APP_KEY = "z3v5yqkbv8v30";
	
	@Override
	public void onCreate() {
		super.onCreate();
		appInstances = this;
		
		
		SystemUtil.init(getGlobalContext());
//		DownLoadAPI.init(getGlobalContext());
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
//		RCUtil.disconnect(false);
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
