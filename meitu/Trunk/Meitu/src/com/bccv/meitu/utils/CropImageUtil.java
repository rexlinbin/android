package com.bccv.meitu.utils;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.widget.Toast;

import com.bccv.meitu.model.CropImage;

/**
 * 剪切图片工具类 从相册或者外部相机获取一张图片并裁剪
 * 
 * @author liukai
 * @since 2014-11-11
 */

public class CropImageUtil {

	/* 请求码 */
	public static final int GALLERY_REQUEST_CODE = 0; // 从相册请求
	public static final int CAMERA_REQUEST_CODE = 1; // 从相机请求
	public static final int RESULT_REQUEST_CODE = 2; // 裁剪后结果

	public static final String CROP_IMAGE_URI = "crop_image_uri";
	public static final String IMAGE_FILE_NAME = "faceImage.png";
	public static final String IMAGE_FILE_NAME2 = "BGImage.png";
	public static final String IMAGE_URI = "iamge_uri";

	private File tempFile;
	private Activity mActivity;
	private static CropImageUtil corpImageUtil;

	private CropImageListener cropImageListener;
	private SharedPreferences myPreferences = null;
	private static final String PICPATH = "picpath";

	private boolean isHeadPic = true;
	private CropImage cropImage = null;

	public CropImageUtil(Activity activity) {
		this.mActivity = activity;
		myPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity
				.getApplicationContext());

	}

	public static CropImageUtil getInstance(Activity activity) {
		if (corpImageUtil == null) {
			corpImageUtil = new CropImageUtil(activity);
		}else{
			corpImageUtil.mActivity = activity;
		}
		return corpImageUtil;
	}

	public boolean isHeadPic() {
		return isHeadPic;
	}

	public void setHeadPic(boolean isHeadPic) {
		this.isHeadPic = isHeadPic;
	}

	public CropImage getCropImage() {
		return cropImage;
	}

	public String getPicPath() {
		return myPreferences.getString(PICPATH, null);
	}

	public void setPicPath(String picPath) {
		myPreferences.edit().putString(PICPATH, picPath).commit();
	}

	public CropImageListener getCropImageListener() {
		return cropImageListener;
	}

	public void setCropImageListener(CropImageListener cropImageListener) {
		this.cropImageListener = cropImageListener;
	}

	/**
	 * 从相册获取
	 */
	public void getFromGallery() {
		// 激活系统图库，选择一张图片
		Intent intentFromGallery = new Intent();
		intentFromGallery.setType("image/*");// 设置文件类型
		intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
		mActivity.startActivityForResult(intentFromGallery,
				GALLERY_REQUEST_CODE);
	}

	/**
	 * 从相机获取
	 */
	public void getFromCamera() {
		// 激活相机
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 判断存储卡是否可以用，可用进行存储
		if (checkSDCardAvailable()) {
			tempFile = new File(Environment.getExternalStorageDirectory(),
					IMAGE_FILE_NAME);
			// 从文件中创建uri
			Uri uri = Uri.fromFile(tempFile);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		}
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
		mActivity.startActivityForResult(intent, CAMERA_REQUEST_CODE);
	}

	/*
	 * 剪切图片
	 */
	public void startPhotoZoom(Uri uri) {
		// 裁剪图片意图
		Intent intent = new Intent("bccv.intent.action.CROP");
		// intent.setDataAndType(uri, "image/*");
		intent.putExtra(IMAGE_URI, uri);
		// 设置裁剪
		// intent.putExtra("output",uri);
		intent.putExtra("crop", "true");
		if (cropImage == null) { // 默认裁剪
			// aspectX aspectY 是宽高的比例
			if(isHeadPic()){
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				// outputX outputY 是裁剪图片宽高
				intent.putExtra("outputX", 320);
				intent.putExtra("outputY", 320);
			}else{
				intent.putExtra("aspectX", 5);
				intent.putExtra("aspectY", 4);
				// outputX outputY 是裁剪图片宽高
				intent.putExtra("outputX", 450);
				intent.putExtra("outputY", 360);
			}
		} else { // 自定义裁剪
			// aspectX aspectY 是宽高的比例
			intent.putExtra("aspectX", cropImage.aspectX);
			intent.putExtra("aspectY", cropImage.aspectY);
			// outputX outputY 是裁剪图片宽高
			intent.putExtra("outputX", cropImage.outputX);
			intent.putExtra("outputY", cropImage.outputY);
		}
		// imagePath = getReadPathByUri(uri);
		mActivity.startActivityForResult(intent, RESULT_REQUEST_CODE);
	}

	public void handleMyActivityResult(int requestCode, int resultCode,
			Intent data) {
		Logger.v("测试", "调用了工具handleMyActivityResult,:resultCode" + resultCode
				+ " requestCode:" + requestCode + " data:" + data);
		if (resultCode != 0) {
			switch (requestCode) {
			case GALLERY_REQUEST_CODE:
				if (data != null) {
					Logger.v("测试","data.getdata:"+data.getData());
					startPhotoZoom(data.getData());
				}
				break;
			case CAMERA_REQUEST_CODE:
				String status = Environment.getExternalStorageState();

				if (status.equals(Environment.MEDIA_MOUNTED)) {
					File tempFile = new File(
							Environment.getExternalStorageDirectory()
									+ File.separator + IMAGE_FILE_NAME);
					startPhotoZoom(Uri.fromFile(tempFile));
				} else {
					Toast.makeText(mActivity.getApplicationContext(),
							"未找到存储卡，无法存储照片！", Toast.LENGTH_LONG).show();
				}
				break;
			case RESULT_REQUEST_CODE:
				Logger.v("测试", "结果返回data:" + data + " cropImageListener:"
						+ cropImageListener);
				if (data != null && cropImageListener != null) {
					Logger.v("测试", "图像保存成功");
					if (isHeadPic()) {
						setPicPath(Environment.getExternalStorageDirectory()
								+ File.separator + IMAGE_FILE_NAME);
						Logger.v("测试", "头像:" + getPicPath());
						cropImageListener.cropImageSuccess(data, getPicPath(),isHeadPic());
					} else {
						cropImageListener.cropImageSuccess(data,
								Environment.getExternalStorageDirectory()
										+ File.separator + IMAGE_FILE_NAME2,isHeadPic());
					}

				} else if (cropImageListener != null) {
					Logger.v("测试", "图像保存失败");
					cropImageListener.cropImageFaild();
				}
				break;
			}
		}
	}

	public static boolean checkSDCardAvailable() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	public interface CropImageListener {

		public void cropImageSuccess(Intent data, String path, boolean isHeadPic);

		public void cropImageFaild();
	}
}
