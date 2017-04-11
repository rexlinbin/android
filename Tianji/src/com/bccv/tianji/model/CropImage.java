package com.bccv.tianji.model;

/**
 * 裁剪图片实体类包含裁剪参数
 * @author Administrator
 *
 */
public class CropImage {
	public CropImage(int aspectX, int aspectY, int outputX, int outputY) {
		this.aspectX = aspectX;
		this.aspectY = aspectY;
		this.outputX = outputX;
		this.outputY = outputY;
	}

	// aspectX aspectY 是宽高的比例
	public int aspectX = 0;
	public int aspectY = 0;

	// outputX outputY 是裁剪图片宽高
	public int outputX = 0;
	public int outputY = 0;
}
