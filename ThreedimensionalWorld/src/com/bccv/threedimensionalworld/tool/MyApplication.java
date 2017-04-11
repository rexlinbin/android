package com.bccv.threedimensionalworld.tool;

import android.app.Application;
import android.app.Service;
import android.os.Handler;
import android.os.Vibrator;

import com.bccv.threedimensionalworld.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MyApplication extends Application {

	public Vibrator mVibrator;

	public static MyApplication instance;

	@Override
	public void onCreate() {
		GlobalParams.context = getApplicationContext();
		GlobalParams.options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.movie_bg)
				.showImageForEmptyUri(R.drawable.movie_bg)
				.showImageOnFail(R.drawable.movie_bg).cacheInMemory(true)
				.cacheOnDisc(true).handler(new Handler()).build();

		GlobalParams.appIconOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.game_s_default)
				.showImageForEmptyUri(R.drawable.game_s_default)
				.showImageOnFail(R.drawable.game_s_default).cacheInMemory(true)
				.cacheOnDisc(true).handler(new Handler()).build();

		GlobalParams.appInfoOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.game_b_default_half)
				.showImageForEmptyUri(R.drawable.game_b_default_half)
				.showImageOnFail(R.drawable.game_b_default_half).cacheInMemory(true)
				.cacheOnDisc(true).handler(new Handler()).build();

		GlobalParams.leftbgOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.leftbg)
				.showImageForEmptyUri(R.drawable.leftbg)
				.showImageOnFail(R.drawable.leftbg).cacheInMemory(true)
				.cacheOnDisc(true).handler(new Handler()).build();

		GlobalParams.rightbgOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.rightbg)
				.showImageForEmptyUri(R.drawable.rightbg)
				.showImageOnFail(R.drawable.rightbg).cacheInMemory(true)
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

		super.onCreate();
	}

	public static MyApplication getApplicationInstance() {
		return instance;
	}

}
