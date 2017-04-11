package com.bccv.meitu;

import android.app.Application;
import android.content.Context;

import com.bccv.meitu.api.DownLoadAPI;
import com.bccv.meitu.utils.ActivityQueue;
import com.bccv.meitu.utils.CrashHandler;
import com.bccv.meitu.utils.SystemUtil;

public class ApplicationManager extends Application {

	private static ApplicationManager appInstances;
	
	@Override
	public void onCreate() {
		super.onCreate();
		appInstances = this;
		SystemUtil.init(getGlobalContext());
		DownLoadAPI.init(getGlobalContext());
		CrashHandler.getInstance().init(this.getApplicationContext());
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
