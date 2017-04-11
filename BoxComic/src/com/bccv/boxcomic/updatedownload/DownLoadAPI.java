package com.bccv.boxcomic.updatedownload;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.bccv.boxcomic.tool.Logger;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

public class DownLoadAPI {

	public static final String TAG = "DownLoadAPI";
	
	private static Context mContext;
	
	public static void init(Context context){
		mContext = context;
		DownloadManager.initDownload(context);
	}
	
	/**
	 * @param url 下载apk url
	 * @param name	apk name 
	 * @param ver	apk 版本 
	 * @param isAutoInstall	是否安装
	 */
	public static void downLoadApk(String url, String name, String packageName,
								int verCode, boolean isAutoInstall, boolean showProgress){
		
		Logger.v("downLoadApk","url : " + url + " name : " + name + " packageName : " 
				+ packageName +" verCode : " + verCode + " isAutoInstall : " + isAutoInstall);
		
		if(mContext==null){
			throw new IllegalArgumentException(" initDownLoadAPI(Context context) should be run first !!! ");
		}
		
		Logger.v("downLoad", " url : " + url);
		if(TextUtils.isEmpty(url)){
			return ;
		}
		
		//TODO 封装对象  下载
		Download download = new Download();
		download.setDownloadUrl(url);
		download.setDownloadFileName(verCode+"_"+name);
		download.setDownloadFilePackageName(packageName);
		download.setDownloadFileVer(verCode);
		download.setDownloadIsInstall(isAutoInstall);
		download.setIsApk(true);
		download.setShowProgress(showProgress);
		
		final Intent intent = new Intent();
		intent.setAction(DownloadService.DOWNLOAD_ACTION);
		intent.putExtra(DownloadService.DOWNLOAD_KEY, download);
		
		// 开启下载服务
		if(!isServiceStarted(mContext)){
			startDownLoadService(mContext);
			
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					Logger.v("download", " send download broadcast !!! ");
					mContext.sendBroadcast(intent);
				}
			}, 1000);
			
		}else{
			mContext.sendBroadcast(intent);
		}
		
	}
	
	
	/**
	 * 下载文件 （图片）
	 * @param url
	 * @param name
	 */
	public static void downLoadFile(String url, String name, boolean showProgress){
		
		Logger.v("downLoadFile","url : " + url + " name : " + name );
		
		if(mContext==null){
			throw new IllegalArgumentException(" initDownLoadAPI(Context context) should be run first !!! ");
		}
		
		Logger.v("downLoad", " url : " + url);
		if(TextUtils.isEmpty(url)){
			return ;
		}
		
		//TODO 封装对象  下载
		Download download = new Download();
		download.setDownloadUrl(url);
		download.setDownloadFileName(name);
		download.setIsApk(false);
		download.setShowProgress(showProgress);
		
		final Intent intent = new Intent();
		intent.setAction(DownloadService.DOWNLOAD_ACTION);
		intent.putExtra(DownloadService.DOWNLOAD_KEY, download);
		
		// 开启下载服务
		if(!isServiceStarted(mContext)){
			startDownLoadService(mContext);
			
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					Logger.v("download", " send download broadcast !!! ");
					mContext.sendBroadcast(intent);
				}
			}, 1000);
			
		}else{
			mContext.sendBroadcast(intent);
		}

	}
	
	
	/**
	 * 下载服务已经开启返回true，否则返回false
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isServiceStarted(Context context) {
		boolean isRunning = false;
		Logger.v("isStarted", "Check Download Service");
		String className = DownloadService.class.getName();
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);

		for (int i = 0; !isRunning && i < serviceList.size(); i++) {
			RunningServiceInfo serviceInfo = serviceList.get(i);
			ComponentName serviceName = serviceInfo.service;

			if (serviceName.getClassName().equals(className)) {
				isRunning = true;
			}
		}

		Logger.v("isStarted NotificationService is running ", String.valueOf(isRunning));
		return isRunning;
	}
	
	public static void startDownLoadService(Context context){
		Logger.v("startDownLoadService", "---start----");
		Intent downLoadService = new Intent();
		downLoadService.setClass(context, DownloadService.class);
		context.startService(downLoadService);
	}
	
	/**
	 * 关闭下载服务
	 * @param context
	 */
	public static void stopDownLoadService(Context context) {
		Logger.v("stopDownLoadService", "---stop----");
		Intent downLoadService = new Intent();
		downLoadService.setClass(context, DownloadService.class);
		context.stopService(downLoadService);
	}
}
