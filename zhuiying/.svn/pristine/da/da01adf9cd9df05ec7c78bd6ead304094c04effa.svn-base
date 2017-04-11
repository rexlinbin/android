package com.utils.updatedownload;


import com.utils.tools.Logger;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Parcelable;


public class DownloadService extends Service {

	@SuppressWarnings("unused")
	private static final String TAG = "DownloadService";
	
	private boolean isServiceStart = false;
	
	private BroadcastReceiver downloadBrocastReceiver;
	private IntentFilter downloadBrocastFilter;
	
	public final static String DOWNLOAD_ACTION = "com.bccv.zhuiying.download.attention.action";
	public final static String DOWNLOAD_KEY = "download_key";
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Logger.v("onCreate", "start");
		super.onCreate();

	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		if (!isServiceStart) {
			Logger.v("downloadService", "NotificationService-->onStartCommand");
			isServiceStart = true;
			registerDownLoadBrocast();
		}
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 设置服务启动标志 关闭
		isServiceStart = false;
		unRegisterNotificationBrocast();
	}
	
	private void registerDownLoadBrocast(){
		Logger.v("downloadService", "registerNotificationBrocast-->>register");
		if (downloadBrocastFilter == null) {
			downloadBrocastFilter = new IntentFilter();
			downloadBrocastFilter.addAction(DOWNLOAD_ACTION);
		}
		
		if (downloadBrocastReceiver == null) {
			downloadBrocastReceiver = new BroadcastReceiver() {

				@Override
				public void onReceive(Context context, Intent intent) {
					// TODO 修改接受信息
					Parcelable parcelableData = intent.getParcelableExtra(DOWNLOAD_KEY);
					Download download = null;
					if(!(parcelableData instanceof Download)){
						Logger.e("registerDownLoadBrocast onReceive", "parcelableData is not Download");
						return;
					}
					download = (Download)parcelableData;
					int addDownloadResultType = DownloadManager.getInstance().addDownloadFile(download);
					Logger.v("registerDownLoadBrocast onReceive", " addDownloadResultType : " + addDownloadResultType);
				}
			};
		}
		registerReceiver(downloadBrocastReceiver, downloadBrocastFilter);
	}
	
	/**
	 * 解除接收关注信息广播
	 */
	public void unRegisterNotificationBrocast() {
		Logger.v("downloadService", "unRegisterNotificationBrocast>>unRegister");
		if (downloadBrocastReceiver != null) {
			unregisterReceiver(downloadBrocastReceiver);
			downloadBrocastReceiver = null;
		}
	}
}
