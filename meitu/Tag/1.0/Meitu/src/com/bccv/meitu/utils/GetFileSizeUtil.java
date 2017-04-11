package com.bccv.meitu.utils;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

/**
 * 获取文件夹大小工具类
 * 
 * @author JasonZue
 * @since 2014-6-5
 */

public class GetFileSizeUtil {

	private static GetFileSizeUtil instance;

	public GetFileSizeUtil() {
	}

	public static GetFileSizeUtil getInstance() {
		if (instance == null) {
			instance = new GetFileSizeUtil();
		}
		return instance;
	}

	/*** 获取文件大小 ***/
	public long getFileSizes(File file) throws Exception {
		long size = 0;
		if (file.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(file);
			size = fis.available();
			fis.close();
		} else {
			file.createNewFile();
			System.out.println("文件不存在");
		}
		
		return size;
	}

	/*** 获取文件夹大小 ***/
	public long getFileSize(File file) throws Exception {
		long size = 0;
		File flist[] = file.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}
	
	/** 
	 * 根据路径获取文件夹大小(long)
	 * @param path
	 * @throws Exception 
	 * @return long  文件夹大小
	 * @throws 
	 */
	public long getFileSizeByPath(String path) throws Exception {
		
		File file = new File(path);	
		long size = 0;
		File flist[] = file.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}
	
	/** 
	 * 通过路径返回文件夹大小(已处理转为文字) 
	 * 
	 * @param path
	 * @throws Exception 
	 * @return String
	 * @throws 
	 */
	public String getFileSizeByPathFormat(String path) throws Exception {
		
		File file = new File(path);	
		long size = 0;
		File flist[] = file.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return FormetFileSize(size);
	}

	/*** 转换文件大小单位(b/kb/mb/gb) ***/
	public String FormetFileSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/*** 获取文件个数 ***/
	public long getlist(File file) {// 递归求取目录文件个数
		long size = 0;
		File flist[] = file.listFiles();
		size = flist.length;
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getlist(flist[i]);
				size--;
			}
		}
		return size;
	}

}
