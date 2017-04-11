package com.bccv.ebook.download;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Parcelable;

import com.bccv.ebook.model.Download;
import com.bccv.ebook.utils.L;

public class DownloadService extends Service {

	private static final String TAG = "DownloadService";
	
	private boolean isServiceStart = false;
	
	private BroadcastReceiver downloadBrocastReceiver;
	private IntentFilter downloadBrocastFilter;
	
	public final static String DOWNLOAD_ACTION = "com.boxuu.ebookjy.download.attention.action";
	public final static String DOWNLOAD_KEY = "download_key";
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		L.v(TAG, "onCreate", "start");
		super.onCreate();

	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		if (!isServiceStart) {
			L.v(TAG, "NotificationService-->onStartCommand");
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
		L.v(TAG, "registerNotificationBrocast-->>register");
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
						L.e(TAG, "registerDownLoadBrocast onReceive", "parcelableData is not Download");
						return;
					}
					download = (Download)parcelableData;
					int addDownloadResultType = DownloadManager.getInstance().addDownloadFile(download);
					L.v(TAG, "registerDownLoadBrocast onReceive", " addDownloadResultType : " + addDownloadResultType);
				}
			};
		}
		registerReceiver(downloadBrocastReceiver, downloadBrocastFilter);
	}
	
	/**
	 * 解除接收关注信息广播
	 */
	public void unRegisterNotificationBrocast() {
		L.v(TAG, "unRegisterNotificationBrocast>>unRegister");
		if (downloadBrocastReceiver != null) {
			unregisterReceiver(downloadBrocastReceiver);
			downloadBrocastReceiver = null;
		}
	}
}
