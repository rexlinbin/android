package com.bccv.strategy.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

public class UserInfoUtil {

	
	/**
	 * 头像图片转换字符串    
	 * @param path
	 * @return
	 */
	public static String getHeadPicString(String path){
		String picString = null;
		if(TextUtils.isEmpty(path)){
			return null;
		}
		File mfile = new File(path);
		if(!mfile.exists()){
			return null;
		}
		FileInputStream fis = null;
		ByteArrayOutputStream baos = null;
		try {
			byte[] temp = new byte[1024];
			int flag = -1;
			
			fis = new FileInputStream(mfile);
			baos = new ByteArrayOutputStream();
			
			while((flag=fis.read(temp))!=-1){
				baos.write(temp, 0, flag);
			}
			byte[] data = baos.toByteArray();
			picString = Base64Encode(data);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
				try {
					if(fis!=null){
						fis.close();
						fis = null;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					if(baos!=null){
						baos.close();
						baos = null;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		return picString;
	}
	
	/**
	 * 图片转换字符串    
	 * @param path
	 * @return
	 */
	public static String getPicString(Bitmap bitmap){
		String picString = null;
		if(bitmap==null){
			return null;
		}
		byte[] data =  Bitmap2Bytes(bitmap);
		picString = Base64Encode(data);
//		picString = byte2string(data);
//		try {
//			picString = new String(data,"ISO8859-1");
//		} catch (UnsupportedEncodingException e) {
//			Log.e("UserInfoUtil", "图片转换String异常："+e.getMessage());
//		}
		return picString;
	}
	
	public static byte[] Bitmap2Bytes(Bitmap bm) {  
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
	    return baos.toByteArray();  
	}  
	
	/**
	 * Base64String
	 * 
	 * @param data
	 * @return
	 */
	public static String Base64Encode(byte[] data) {
		String encodeData = "";
			encodeData = Base64.encodeToString(data, Base64.DEFAULT);
		return encodeData;
	}
	
}
