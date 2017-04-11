package com.utils.tools;



import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Vibrator;

import com.bccv.tianji.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.utils.net.TcpClientHelper;
import com.utils.net.TcpServerHelper;
import com.utils.net.UdpHelper;

public class MyApplication extends Application {

	public Vibrator mVibrator;

	public static MyApplication instance;

	@Override
	public void onCreate() {
		GlobalParams.context = getApplicationContext();
		
		GlobalParams.options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
				.cacheOnDisc(true).handler(new Handler()).build();


		GlobalParams.Toppic = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.banner_default)
		.showImageForEmptyUri(R.drawable.banner_default)
		.showImageOnFail(R.drawable.banner_default).cacheInMemory(true)
		.cacheOnDisc(true).handler(new Handler()).build();

		GlobalParams.iconOptions = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.icon)
		.showImageForEmptyUri(R.drawable.icon)
		.showImageOnFail(R.drawable.icon).cacheInMemory(true)
		.cacheOnDisc(true).handler(new Handler()).build();

		GlobalParams.typeOptions = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.classification_default)
		.showImageForEmptyUri(R.drawable.classification_default)
		.showImageOnFail(R.drawable.classification_default).cacheInMemory(true)
		.cacheOnDisc(true).handler(new Handler()).build();
		
		GlobalParams.Intropic = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.default_315_190)
		.showImageForEmptyUri(R.drawable.default_315_190)
		.showImageOnFail(R.drawable.default_315_190).cacheInMemory(true)
		.cacheOnDisc(true).handler(new Handler()).build();
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).memoryCacheExtraOptions(480, 800)
				.discCacheSize(50 * 1024 * 1024)
				.memoryCacheSize(2 * 1024 * 1024).build();
		ImageLoader.getInstance().init(config);

		mVibrator = (Vibrator) getApplicationContext().getSystemService(
				Service.VIBRATOR_SERVICE);
		instance = this;

		FileUtils.init(getApplicationContext());

		GlobalParams.ImagePathString = FileUtils.isSdcard() + "/Wallpaper";

		if (!FileUtils.checkFileExists(GlobalParams.ImagePathString)) {

			FileUtils.createDirectory(GlobalParams.ImagePathString);

		}

		WifiManager manager = (WifiManager) getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
		GlobalParams.udpHelper = new UdpHelper(manager);
		
		GlobalParams.tcpServerHelper = new TcpServerHelper();
		
		GlobalParams.tcpClientHelper = new TcpClientHelper("123.57.159.48", 9506);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				GlobalParams.tcpClientHelper.createClientSocket();
			}
		}).start();
		
		
		super.onCreate();
	}

	public static MyApplication getApplicationInstance() {
		return instance;
	}

}
