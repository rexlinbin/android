package com.bccv.meitu.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.content.Context;

public class SerializationUtil {

//	public static final String USER = "user.txt";
//	public static final String HOMEROOMLIST = "homeRoomList.txt";

//	public static void wirteUserSerialization(Context mContext,Serializable ser){
//		saveObject(mContext, ser, USER);
//	}
//	
//	public static UserInfo readUserSerialization(Context mContext){
//		return (UserInfo)readObject(mContext, USER);
//	}
//	
//	public static void removeUserSerialization(Context mContext){
//		File data = mContext.getFileStreamPath(USER);
//		if (data.exists()){
//			data.delete();
//		}
//	}

	
//	public static void writeHomeRoomListSerialization(Context mContext,Serializable ser){
//		saveObject(mContext, ser, HOMEROOMLIST);
//	}
//	
//	public static Serializable readHomeRoomListSerialization(Context mContext){
//		return readObject(mContext, HOMEROOMLIST);
//	}
	
//	/**
//	 * 保存本地化授权信息
//	 * @param mContext
//	 * @param ser
//	 * @param type
//	 */
//	public static void wirteSNSInfoSerialization(Context mContext,Serializable ser,String type){
//		saveObject(mContext, ser, type);
//	}
//	
//	/**
//	 * 读取本地化授权信息
//	 * @param mContext
//	 * @param type
//	 * @return
//	 */
//	public static SNSUserInfo readSNSInfoSerialization(Context mContext,String type){
//		return (SNSUserInfo)readObject(mContext, type);
//	}
//	
//	/**
//	 * 清除本地化授权信息
//	 * @param mContext
//	 * @param type
//	 */
//	public static void removeSNSInfoSerialization(Context mContext,String type){
//		File data = mContext.getFileStreamPath(type);
//		if (data.exists()){
//			data.delete();
//		}
//	}
//	
//	/**
//	 * 序列化历史登陆记录
//	 * @param mContext
//	 * @param userList
//	 * @param type
//	 */
//	public static void wirteUserListSerialization(Context mContext,ArrayList<String> userList,String type){
//		saveObject(mContext, userList, type);
//	}
//	
//	/**
//	 * 反序列化历史登陆记录
//	 * @param mContext
//	 * @param type
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public static ArrayList<String> readUserListSerialization(Context mContext,String type){
//		return(ArrayList<String>)readObject(mContext, type);
//	}
//	
//	/**
//	 * 清除本地化登陆历史记录
//	 * @param mContext
//	 * @param type
//	 */
//	public static void removeUserListSerialization(Context mContext,String type){
//		File data = mContext.getFileStreamPath(type);
//		if (data.exists()){
//			data.delete();
//		}
//	}
	
	/**
	 * 判断缓存是否存在
	 * @param cachefile
	 * @return
	 */
	public static boolean isExistDataCache(Context mContext, String cachefile)
	{
		boolean exist = false;
		File data = mContext.getFileStreamPath(cachefile);
		if(data.exists())
			exist = true;
		return exist;
	}
	
	/**
	 * 读取对象
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private static Serializable readObject(Context mContext, String file){
		if(!isExistDataCache(mContext, file))
			return null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try{
			fis = mContext.openFileInput(file);
			ois = new ObjectInputStream(fis);
			return (Serializable)ois.readObject();
		}catch(FileNotFoundException e){
		}catch(Exception e){
			e.printStackTrace();
			//反序列化失败 - 删除缓存文件
			if(e instanceof InvalidClassException){
				File data = mContext.getFileStreamPath(file);
				data.delete();
			}
		}finally{
			try {
				ois.close();
			} catch (Exception e) {}
			try {
				fis.close();
			} catch (Exception e) {}
		}
		return null;
	}
	
	/**
	 * 保存对象
	 * @param ser
	 * @param file
	 * @throws IOException
	 */
	private static boolean saveObject(Context mContext, Serializable ser, String file) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try{
			fos = mContext.openFileOutput(file, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ser);
			oos.flush();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			try {
				oos.close();
			} catch (Exception e) {}
			try {
				fos.close();
			} catch (Exception e) {}
		}
	}
}
