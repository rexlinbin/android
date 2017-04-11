package com.boxuu.gamebox.utils;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.boxuu.gamebox.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class ImageLoaderUtil {
	
	private ImageLoaderUtil(){}
	
	private static DisplayImageOptions nonedefaultImageOptions ;
	private static DisplayImageOptions defaultImageOptions ;
	private static DisplayImageOptions roundedImageOptions ;
	
	private static DisplayImageOptions userIconImageOptions ;
	
	private static DisplayImageOptions upLoadImageOptions ;
	private static DisplayImageOptions upLoadDefultImageOptions ;
	
	private static ImageLoaderUtil imageLoaderUtil = null;
	
	public static void init(Context context){
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
			.threadPriority(Thread.NORM_PRIORITY - 2)
			.denyCacheImageMultipleSizesInMemory()
			// 50M
			.diskCacheSize(200 * 1024 * 1024)
			// 缓存的文件数量
			.diskCacheFileCount(200)
//			.diskCache(new UnlimitedDiscCache(new File(""))) //     指定缓存路径
			.diskCacheFileNameGenerator(new Md5FileNameGenerator())
			.tasksProcessingOrder(QueueProcessingType.LIFO)
			.imageDownloader(new BaseImageDownloader(context,5 * 1000, 30 * 1000))
			// .writeDebugLogs() // Remove for release app
			.build();
	
			ImageLoader.getInstance().init(config);
		
			defaultImageOptions = new DisplayImageOptions.Builder()
//			.showImageOnLoading(R.drawable.default_small) // 设置图片在下载期间显示的图片
			.showImageForEmptyUri(R.drawable.item_bg)// 设置图片Uri为空或是错误的时候显示的图片
			.showImageOnFail(R.drawable.item_bg) // 设置图片加载/解码过程中错误时候显示的图片
			.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
			.cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
			.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
			.imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
			.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
//			.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
			.build();// 构建完成
			
			nonedefaultImageOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.item_bg) // 设置图片在下载期间显示的图片
			.showImageForEmptyUri(R.drawable.item_bg)// 设置图片Uri为空或是错误的时候显示的图片
			.showImageOnFail(R.drawable.item_bg) // 设置图片加载/解码过程中错误时候显示的图片
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
	
//	public static DisplayImageOptions getRoundedImageOptions(){
		
//		if(roundedImageOptions==null){
//			roundedImageOptions = new DisplayImageOptions.Builder()
//			.showImageOnLoading(R.drawable.item_bg) // 设置图片在下载期间显示的图片
//			.showImageForEmptyUri(R.drawable.item_bg)// 设置图片Uri为空或是错误的时候显示的图片
//			.showImageOnFail(R.drawable.item_bg) // 设置图片加载/解码过程中错误时候显示的图片
//			.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
//			.cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
//			.displayer(new CircleBitmapDisplayer())
//			.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
//			.imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
//			.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
//			.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
//			.build();// 构建完成
//		}
		
//		return roundedImageOptions;
//
//	}
	
//	public static DisplayImageOptions getUserIconImageOptions(){
//		
//		if(userIconImageOptions==null){
//			userIconImageOptions = new DisplayImageOptions.Builder()
////			.showImageOnLoading(R.drawable.none) // 设置图片在下载期间显示的图片
//			.showImageForEmptyUri(R.drawable.item_bg)// 设置图片Uri为空或是错误的时候显示的图片
//			.showImageOnFail(R.drawable.item_bg) // 设置图片加载/解码过程中错误时候显示的图片
//			.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
//			.cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
//			.displayer(new CircleBitmapDisplayer())
//			.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
//			.imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
//			.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
//			.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
//			.build();// 构建完成
//		}
//		
//		return userIconImageOptions;
//		
//	}
	
	public static DisplayImageOptions getUpLoadImageOptions(){
		
		if(upLoadImageOptions==null){
			upLoadImageOptions = new DisplayImageOptions.Builder()
//			.showImageOnLoading(R.drawable.default_small) // 设置图片在下载期间显示的图片
			.showImageForEmptyUri(R.drawable.item_bg)// 设置图片Uri为空或是错误的时候显示的图片
			.showImageOnFail(R.drawable.item_bg) // 设置图片加载/解码过程中错误时候显示的图片
			.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
			.cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
			.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
			.imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
			.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
//			.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
			.build();// 构建完成
		}
		return upLoadImageOptions;
	}
	
	public static DisplayImageOptions getUpLoadDefultImageOptions(){
		
		if(upLoadDefultImageOptions==null){
			upLoadDefultImageOptions = new DisplayImageOptions.Builder()
//			.showImageOnLoading(R.drawable.default_small) // 设置图片在下载期间显示的图片
			.showImageForEmptyUri(R.drawable.item_bg)// 设置图片Uri为空或是错误的时候显示的图片
			.showImageOnFail(R.drawable.item_bg) // 设置图片加载/解码过程中错误时候显示的图片
			.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
			.cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
			.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
			.imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
			.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
//			.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
			.build();// 构建完成
		}
		return upLoadDefultImageOptions;
	}
	
	
	public void displayImage(String uri, ImageView imageView, DisplayImageOptions options) {
		ImageLoader.getInstance().displayImage(uri, imageView, options);
	}
	public void displayImageWithNone(String uri, ImageView imageView) {
		ImageLoader.getInstance().displayImage(uri, imageView, nonedefaultImageOptions);
	}
	
	public void displayImage(String uri, ImageView imageView,int imageResource){
		
		DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(imageResource) // 设置图片在下载期间显示的图片
			.showImageForEmptyUri(imageResource)// 设置图片Uri为空或是错误的时候显示的图片
			.showImageOnFail(imageResource) // 设置图片加载/解码过程中错误时候显示的图片
			.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
			.cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
			.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
			.imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
			.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
			.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
			.build();// 构建完成
		ImageLoader.getInstance().displayImage(uri, imageView, imageOptions);
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
	
	public static File getCacheDir(Context context){
		return StorageUtils.getCacheDirectory(context);
	}
	
}
