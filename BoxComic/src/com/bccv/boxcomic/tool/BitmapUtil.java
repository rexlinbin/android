package com.bccv.boxcomic.tool;

import java.io.ByteArrayOutputStream;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class BitmapUtil {
	/**
	 * 将Bitmap转换成字符串
	 * 
	 * @param bitmap
	 * @param bitmapQuality
	 * @return
	 */
	public static String bitmapToString(Bitmap bitmap, String type) {
		String string = null;
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		if (type != null && (type.equalsIgnoreCase("jpg") || type.equalsIgnoreCase("jpeg"))) {
			bitmap.compress(CompressFormat.JPEG, 100, bStream); 
		}
		else if (type != null && type.equalsIgnoreCase("png") ) {
			bitmap.compress(CompressFormat.PNG, 100, bStream);
		} 
		else {
			bitmap.compress(CompressFormat.JPEG, 100, bStream);
		}
		
		byte[] bytes = bStream.toByteArray();
		string = Base64.encodeToString(bytes, Base64.DEFAULT);
		return string;
	}

	/**
	 * 将字符串转换成Bitmap类型
	 * 
	 * @param string
	 * @return
	 */
	public static Bitmap stringtoBitmap(String string) {
		Bitmap bitmap = null;
		try {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(string, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
					bitmapArray.length);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}
