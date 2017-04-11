package com.nostra13.universalimageloader.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.bccv.meitu.R;
import com.bccv.meitu.view.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ImageLoaderUtil {
	
	private ImageLoaderUtil(){}
	
	private static DisplayImageOptions defaultImageOptions ;
	private static DisplayImageOptions roundedImageOptions ;
	
	private static ImageLoaderUtil imageLoaderUtil = null;
	
	public static void init(Context context){
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
			.threadPriority(Thread.NORM_PRIORITY - 2)
			.denyCacheImageMultipleSizesInMemory()
			// 50M
			.diskCacheSize(200 * 1024 * 1024)
			// 缓存的文件数量
			.diskCacheFileCount(200)
//				.diskCache(new UnlimitedDiscCache(new File("")))      指定缓存路径
			.diskCacheFileNameGenerator(new Md5FileNameGenerator())
			.tasksProcessingOrder(QueueProcessingType.LIFO)
			.imageDownloader(new BaseImageDownloader(context,5 * 1000, 30 * 1000))
			// .writeDebugLogs() // Remove for release app
			.build();
	
			ImageLoader.getInstance().init(config);
		
			defaultImageOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_back_load) // 设置图片在下载期间显示的图片
			.showImageForEmptyUri(R.drawable.ic_back_load)// 设置图片Uri为空或是错误的时候显示的图片
			.showImageOnFail(R.drawable.ic_back_load) // 设置图片加载/解码过程中错误时候显示的图片
			.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
			.cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
			.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
			.imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
			.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
			.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
			.build();// 构建完成
			
			imageLoaderUtil = new ImageLoaderUtil();
	}
	
	public static ImageLoaderUtil getInstance(Context context){
		
		if(imageLoaderUtil==null){
			ImageLoaderUtil.init(context);
		}
		return imageLoaderUtil;
	}
	
	public static DisplayImageOptions getRoundedImageOptions(){
		
		if(roundedImageOptions==null){
			roundedImageOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_back_load) // 设置图片在下载期间显示的图片
			.showImageForEmptyUri(R.drawable.ic_back_load)// 设置图片Uri为空或是错误的时候显示的图片
			.showImageOnFail(R.drawable.ic_back_load) // 设置图片加载/解码过程中错误时候显示的图片
			.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
			.cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
			.displayer(new CircleBitmapDisplayer())
			.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
			.imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
			.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
			.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
			.build();// 构建完成
		}
		
		return roundedImageOptions;

	}
	
	
	public void displayImage(String uri, ImageView imageView, DisplayImageOptions options) {
		ImageLoader.getInstance().displayImage(uri, imageView, options);
	}
	
	public void displayImage(String uri, ImageView imageView) {
		
		ImageLoader.getInstance().displayImage(uri, imageView, defaultImageOptions);
	}
	
	public void disaPlayImage(String uri, ImageView imageView, ImageLoadingProgressListener progressListener){
		ImageLoader.getInstance().displayImage(uri, imageView, defaultImageOptions, new SimpleImageLoadingListener(), progressListener);
	}
	
	
	public void disaPlayImage(String uri, ImageView imageView, DisplayImageOptions options, ImageLoadingProgressListener progressListener){
		ImageLoader.getInstance().displayImage(uri, imageView, defaultImageOptions, new SimpleImageLoadingListener(),progressListener);
	}
	
	
	public void disaPlayImage(String uri, ImageView imageView,ImageLoadingListener listener, ImageLoadingProgressListener progressListener){
		ImageLoader.getInstance().displayImage(uri, imageView, defaultImageOptions, listener, progressListener);
	}
	
	public void disaPlayImage(String uri, ImageView imageView, DisplayImageOptions options, ImageLoadingListener listener, ImageLoadingProgressListener progressListener){
		ImageLoader.getInstance().displayImage(uri, imageView, options, listener, progressListener);
	}
	
}
