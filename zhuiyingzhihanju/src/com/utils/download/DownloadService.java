package com.utils.download;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;

import java.util.List;

/**
 * Author: wyouflf Date: 13-11-10 Time: 上午1:04
 */
public class DownloadService extends Service {

	private static DownloadManager DOWNLOAD_MANAGER;
	static DownloadService downloadService;
	// static Intent downloadSvr;

	public static DownloadManager getDownloadManager(Context appContext) {
		if (!DownloadService.isServiceRunning(appContext)) {
			Intent downloadSvr = new Intent(appContext, DownloadService.class);
			appContext.bindService(downloadSvr, conn, Context.BIND_AUTO_CREATE);
			// appContext.startService(downloadSvr);
		}
		if (DownloadService.DOWNLOAD_MANAGER == null) {
			DownloadService.DOWNLOAD_MANAGER = new DownloadManager(appContext);
		}
		return DOWNLOAD_MANAGER;
	}

	private static ServiceConnection conn = new ServiceConnection() {
		/** 获取服务对象时的操作 */
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			downloadService = ((DownloadService.ServiceBinder) service).getService();

		}

		/** 无法获取到服务对象时的操作 */
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			downloadService = null;
		}

	};

	public static void unBindService(Context appContext) {
//		if (DownloadService.isServiceRunning(appContext)) {
//			appContext.unbindService(conn);
//		}
		// if (downloadSvr != null) {
		// appContext.stopService(downloadSvr);
		// }

	}

	public DownloadService() {
		super();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		if (DOWNLOAD_MANAGER != null) {
//			try {
//				DOWNLOAD_MANAGER.stopAllDownload();
//				DOWNLOAD_MANAGER.backupDownloadInfoList();
//			} catch (DbException e) {
//				LogUtils.e(e.getMessage(), e);
//			}
		}
		super.onDestroy();
	}

	public static boolean isServiceRunning(Context context) {
		boolean isRunning = false;

		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);

		if (serviceList == null || serviceList.size() == 0) {
			return false;
		}

		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(DownloadService.class.getName())) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

	class ServiceBinder extends Binder {
		public DownloadService getService() {
			return DownloadService.this;
		}
	}
}
