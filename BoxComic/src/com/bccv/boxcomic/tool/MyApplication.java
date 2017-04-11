package com.bccv.boxcomic.tool;

import java.io.File;
import java.io.FileOutputStream;

import u.aly.bl;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.R.color;
import com.bccv.boxcomic.sns.UserInfoManager;
import com.bccv.boxcomic.sns.util.SNSUtil;
import com.bccv.boxcomic.tool.CrashHandler;
import com.bccv.boxcomic.updatedownload.DownLoadAPI;
import com.bccv.boxcomic.updatedownload.SystemUtil;
import com.bccv.boxcomic.updatedownload.Version;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MyApplication extends Application {

	public Vibrator mVibrator;
	private static MyApplication instance;

	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;

	@Override
	public void onCreate() {
		instance = this;
		GlobalParams.context = getApplicationContext();
		GlobalParams.options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.view_default)
				.showImageForEmptyUri(R.drawable.view_default)
				.showImageOnFail(R.drawable.view_default).cacheInMemory(true)
				.cacheOnDisc(true).handler(new Handler()).build();

		GlobalParams.comicOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.translateimage)
				.showImageForEmptyUri(R.drawable.translateimage)
				.showImageOnFail(R.drawable.translateimage).cacheInMemory(true)
				.cacheOnDisc(true).handler(new Handler()).build();

		GlobalParams.headOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.head)
				.showImageForEmptyUri(R.drawable.head)
				.showImageOnFail(R.drawable.head).cacheInMemory(true)
				.cacheOnDisc(true).handler(new Handler()).build();

		GlobalParams.frameHomeoptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.frament_home_default)
				.showImageForEmptyUri(R.drawable.frament_home_default)
				.showImageOnFail(R.drawable.frament_home_default)
				.cacheInMemory(true).cacheOnDisc(true).handler(new Handler())
				.build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).memoryCacheExtraOptions(480, 800)
				.discCacheSize(50 * 1024 * 1024)
				.memoryCacheSize(2 * 1024 * 1024).build();
		ImageLoader.getInstance().init(config);
		mVibrator = (Vibrator) getApplicationContext().getSystemService(
				Service.VIBRATOR_SERVICE);

		CrashHandler.getInstance().init(this.getApplicationContext());

		UserInfoManager.init(getGlobalContext());
		SNSUtil.initSNS(getGlobalContext());

		DownLoadAPI.init(getApplicationContext());
		SystemUtil.init(getApplicationContext());

		setUpdate(getApplicationContext(), true);

		GlobalParams.isLanscape = AppConfig.getScreen();
		GlobalParams.canShowTime = AppConfig.getShowTime();
		GlobalParams.canDoubleClick = AppConfig.getDoubleClick();
		GlobalParams.isTui = AppConfig.getisTui();
		WindowManager wm = (WindowManager) getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		GlobalParams.screenWidth = wm.getDefaultDisplay().getWidth();
		GlobalParams.screenHeight = wm.getDefaultDisplay().getHeight();

		GlobalParams.downloadPathString = getFilesDir().getPath() + "/Comic";
		GlobalParams.ebookDownloadPathString = getFilesDir().getPath()
				+ "/Ebook";
		GlobalParams.localComicPathString = SystemUtil.getInstance().getRootPath();

		// GlobalParams.ebookDownloadPathString = "/storage/sdcard0/xUtils";
		// GlobalParams.downloadPathString = "/storage/sdcard0/xUtils";
		super.onCreate();
		mearsure();
	}

	public static void setUpdate(final Context context, final boolean isStart) {
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (result != null) {
					Logger.e("asdfasdf", result);
					try {
						JSONObject jsonObject = JSON.parseObject(result);
						if (jsonObject.getIntValue("app_up") == 1) {
							GlobalParams.updateVersion = new Version();
							GlobalParams.updateVersion.setDownload(jsonObject
									.getString("app_url"));
							GlobalParams.updateVersion.setVersion(jsonObject
									.getString("app_ver"));
							GlobalParams.updateVersion
									.setVersion_code(jsonObject
											.getIntValue("app_upver"));
							GlobalParams.updateVersion.setDesString(jsonObject
									.getString("app_des"));
							GlobalParams.canUpdate = true;

							PromptManager.showUpdateDialog(
									GlobalParams.updateVersion, context,
									GlobalParams.activity);
						} else {
							if (!isStart) {
								Toast.makeText(context, "已是最新版本", 1).show();
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
					}

				} else {
					Logger.e("asdfasdf", "asdfasdf");
				}
			}
		};
		Statistics.init(context, "hezidongman", "83f292712998bdaf", callback);
		Statistics.getUpdate();
	}

	public static MyApplication getApplicationInstance() {
		return instance;
	}

	private void mearsure() {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) getGlobalContext()
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(dm);
		SCREEN_WIDTH = dm.widthPixels;
		SCREEN_HEIGHT = dm.heightPixels;
	}

	/**
	 * 获取全局的context
	 * 
	 * @return
	 */
	public static Context getGlobalContext() {
		return instance.getApplicationContext();
	}

}
