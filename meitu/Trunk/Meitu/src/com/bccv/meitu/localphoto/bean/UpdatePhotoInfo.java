package com.bccv.meitu.localphoto.bean;

import android.graphics.Bitmap;

public class UpdatePhotoInfo {
	private int image_id;
	private String path_file;
	private String path_absolute;
	
	private Bitmap bitmap;//压缩后的图片
	private boolean isCover;//是否封面
	private boolean isDefult;//是否是默认条目

	private int uploadState = UNUPLOAD;
			
	public static final int UNUPLOAD = 1;
	public static final int UPLOAD_SUCCESS = 2;
	public static final int UPLOAD_FAILED = 3;
	
	public int getImage_id() {
		return image_id;
	}
	public void setImage_id(int image_id) {
		this.image_id = image_id;
	}
	public String getPath_file() {
		return path_file;
	}
	public void setPath_file(String path_file) {
		this.path_file = path_file;
	}
	public String getPath_absolute() {
		return path_absolute;
	}
	public void setPath_absolute(String path_absolute) {
		this.path_absolute = path_absolute;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public boolean isCover() {
		return isCover;
	}
	public void setCover(boolean isCover) {
		this.isCover = isCover;
	}
	public boolean isDefult() {
		return isDefult;
	}
	public void setDefult(boolean isDefult) {
		this.isDefult = isDefult;
	}
	public int getUploadState() {
		return uploadState;
	}
	public void setUploadState(int uploadState) {
		this.uploadState = uploadState;
	}

	@Override
	public String toString() {
		return "UpdatePhotoInfo [image_id=" + image_id + ", path_file="
				+ path_file + ", path_absolute=" + path_absolute + ", bitmap="
				+ bitmap + ", isCover=" + isCover + ", isDefult=" + isDefult
				+ ", uploadState=" + uploadState + "]";
	}
	
	
	
}
