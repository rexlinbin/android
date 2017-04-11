package com.utils.tools;

import android.app.Application;
import android.app.Service;
import android.os.Handler;
import android.os.Vibrator;

import org.xutils.x;

import com.bccv.zhuiyingzhihanju.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.tendcloud.tenddata.TCAgent;
import com.utils.download.DownloadUtil;
import com.utils.updatedownload.DownLoadAPI;
import com.utils.updatedownload.SystemUtil;

public class MyApplication extends Application {

	public Vibrator mVibrator;

	public static MyApplication instance;

	@Override
	public void onCreate() {
		GlobalParams.context = getApplicationContext();

		GlobalParams.downloadOptions = new DisplayImageOptions.Builder()

				.showImageOnLoading(R.drawable.downloadfile).showImageForEmptyUri(R.drawable.downloadfile)
				.showImageOnFail(R.drawable.downloadfile).cacheInMemory(true).cacheOnDisc(true).handler(new Handler())
				.build();
		GlobalParams.loadingOptions = new DisplayImageOptions.Builder()
				
				.showImageOnLoading(R.drawable.loadingwhite).showImageForEmptyUri(R.drawable.loadingwhite)
				.showImageOnFail(R.drawable.loadingwhite).cacheInMemory(true).cacheOnDisc(true).handler(new Handler())
				.build();
		
		GlobalParams.options = new DisplayImageOptions.Builder()

				.showImageOnLoading(R.drawable.ic_launcher).showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true).cacheOnDisc(true).handler(new Handler())
				.build();
		GlobalParams.movieOptions = new DisplayImageOptions.Builder()

				.showImageOnLoading(R.drawable.default210290).showImageForEmptyUri(R.drawable.default210290)
				.showImageOnFail(R.drawable.default210290).cacheInMemory(true).cacheOnDisc(true).handler(new Handler())
				.build();
		GlobalParams.bannerOptions = new DisplayImageOptions.Builder()

				.showImageOnLoading(R.drawable.default720380).showImageForEmptyUri(R.drawable.default720380)
				.showImageOnFail(R.drawable.default720380).cacheInMemory(true).cacheOnDisc(true).handler(new Handler())
				.build();
		GlobalParams.foundOptions = new DisplayImageOptions.Builder()

				.showImageOnLoading(R.drawable.default720380).showImageForEmptyUri(R.drawable.default720380)
				.showImageOnFail(R.drawable.default720380).imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).cacheInMemory(true).cacheOnDisc(true).handler(new Handler())
				.build();
		GlobalParams.iconOptions = new DisplayImageOptions.Builder()

				.showImageOnLoading(R.drawable.head_default).showImageForEmptyUri(R.drawable.head_default)
				.showImageOnFail(R.drawable.head_default).cacheInMemory(true).cacheOnDisc(true).handler(new Handler())
				.build();
		
		
		
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.memoryCacheExtraOptions(480, 800).discCacheSize(50 * 1024 * 1024).memoryCacheSize(2 * 1024 * 1024)
				.build();

		ImageLoader.getInstance().init(config);

		mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
		instance = this;

		DownloadUtil.initDownload(getApplicationContext());
		FileUtils.init(getApplicationContext());
		SystemUtil.init(getApplicationInstance());
		DownLoadAPI.init(getApplicationContext());
		Statistics.init(getApplicationContext());
		GlobalParams.isTui = AppConfig.getisTui();

		GlobalParams.isWifi = AppConfig.getWifi();

		GlobalParams.downloadPath = SystemUtil.getInstance().getRootPath() + "/HanJuMovie";
		FileUtils.createAbsoluteDirectory(GlobalParams.downloadPath);
		TCAgent.LOG_ON=true;
        // App ID: 在TalkingData创建应用后，进入数据报表页中，在“系统设置”-“编辑应用”页面里查看App ID。  
        // 渠道 ID: 是渠道标识符，可通过不同渠道单独追踪数据。
        TCAgent.init(getApplicationContext());
        TCAgent.setReportUncaughtExceptions(true);
//        CrashHandler crashHandler = CrashHandler.getInstance();  
//        crashHandler.init(getApplicationContext()); 
        x.Ext.init(this);
        x.Ext.setDebug(true); 
		super.onCreate();
	}

	public static MyApplication getApplicationInstance() {
		return instance;
	}

}
