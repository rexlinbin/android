package com.utils.tools;

import android.app.Application;
import android.app.Service;
import android.os.Handler;
import android.os.Vibrator;

import org.xutils.x;

import com.bccv.zhuiying.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tendcloud.tenddata.TCAgent;
import com.utils.download.DownloadManager;
import com.utils.download.DownloadService;
import com.utils.updatedownload.DownLoadAPI;
import com.utils.updatedownload.SystemUtil;

public class MyApplication extends Application {

	public Vibrator mVibrator;

	public static MyApplication instance;

	@Override
	public void onCreate() {
		GlobalParams.context = getApplicationContext();

		GlobalParams.options = new DisplayImageOptions.Builder()

				.showImageOnLoading(R.drawable.ic_launcher).showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true).cacheOnDisc(true).handler(new Handler())
				.build();
		GlobalParams.movieOptions = new DisplayImageOptions.Builder()

				.showImageOnLoading(R.drawable.default_200).showImageForEmptyUri(R.drawable.default_200)
				.showImageOnFail(R.drawable.default_200).cacheInMemory(true).cacheOnDisc(true).handler(new Handler())
				.build();
		GlobalParams.bannerOptions = new DisplayImageOptions.Builder()

				.showImageOnLoading(R.drawable.default_680).showImageForEmptyUri(R.drawable.default_680)
				.showImageOnFail(R.drawable.default_680).cacheInMemory(true).cacheOnDisc(true).handler(new Handler())
				.build();
		GlobalParams.foundOptions = new DisplayImageOptions.Builder()

				.showImageOnLoading(R.drawable.default_320).showImageForEmptyUri(R.drawable.default_320)
				.showImageOnFail(R.drawable.default_320).cacheInMemory(true).cacheOnDisc(true).handler(new Handler())
				.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.memoryCacheExtraOptions(480, 800).discCacheSize(50 * 1024 * 1024).memoryCacheSize(2 * 1024 * 1024)
				.build();

		ImageLoader.getInstance().init(config);

		mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
		instance = this;

		FileUtils.init(getApplicationContext());
		SystemUtil.init(getApplicationInstance());
		DownLoadAPI.init(getApplicationContext());
		Statistics.init(getApplicationContext());
		GlobalParams.isTui = AppConfig.getisTui();

		GlobalParams.isWifi = AppConfig.getWifi();

		GlobalParams.downloadPath = SystemUtil.getInstance().getRootPath() + "/ZhuiYingMovie";
		TCAgent.LOG_ON=true;
        // App ID: 在TalkingData创建应用后，进入数据报表页中，在“系统设置”-“编辑应用”页面里查看App ID。  
        // 渠道 ID: 是渠道标识符，可通过不同渠道单独追踪数据。
        TCAgent.init(getApplicationContext());
        TCAgent.setReportUncaughtExceptions(true);
        CrashHandler crashHandler = CrashHandler.getInstance();  
        crashHandler.init(getApplicationContext()); 
        x.Ext.init(this);
        x.Ext.setDebug(true); 
//        AliVcMediaPlayer.init(getApplicationContext(), "video_live", new AccessKeyCallback() {
//            public AccessKey getAccessToken() {
//                return new AccessKey("QxJIheGFRL926hFX", "hipHJKpt0TdznQG2J4D0EVSavRH7mR");
//            }
//        });
        
		super.onCreate();
	}

	public static MyApplication getApplicationInstance() {
		return instance;
	}

}
