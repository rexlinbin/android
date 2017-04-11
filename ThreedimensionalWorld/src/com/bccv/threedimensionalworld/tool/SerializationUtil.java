package com.bccv.threedimensionalworld.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;





import com.bccv.threedimensionalworld.model.App;
import com.bccv.threedimensionalworld.model.Movie;

import android.content.Context;

public class SerializationUtil {

	public static final String COLLECT_DATA = "CollectData.txt";
	public static final String HISTORY_DATA = "HistoryData.txt";
	public static final String MYAPP_DATA = "MyAppData.txt";
	
	/**
	 * 存储收藏 数据
	 * 
	 * @param mContext
	 * @param ser
	 */
	public static void wirteCollectSerialization(Context mContext,
			Serializable ser) {
		saveObject(mContext, ser, COLLECT_DATA);
	}

	/**
	 * 读取收藏 缓存
	 * 
	 * @param mContext
	 * @return
	 */
	public static List<Movie> readCollectCache(Context mContext) {
		return (List) readObject(mContext, COLLECT_DATA);
	}
	
	/**
	 * 存储收藏 数据
	 * 
	 * @param mContext
	 * @param ser
	 */
	public static void wirteHistorySerialization(Context mContext,
			Serializable ser) {
		saveObject(mContext, ser, HISTORY_DATA);
	}

	/**
	 * 读取收藏 缓存
	 * 
	 * @param mContext
	 * @return
	 */
	public static List<Movie> readHistoryCache(Context mContext) {
		return (List) readObject(mContext, HISTORY_DATA);
	}

	
	/**
	 * 存储收藏 数据
	 * 
	 * @param mContext
	 * @param ser
	 */
	public static void wirteMyAppSerialization(Context mContext,
			Serializable ser) {
		saveObject(mContext, ser, MYAPP_DATA);
	}

	/**
	 * 读取收藏 缓存
	 * 
	 * @param mContext
	 * @return
	 */
	public static List<App> readMyAppCache(Context mContext) {
		return (List) readObject(mContext, MYAPP_DATA);
	}

	/**
	 * 清除本地化登陆历史记录
	 * 
	 * @param mContext
	 * @param type
	 */
	public static void removeSerialization(Context mContext, String type) {
		File data = mContext.getFileStreamPath(type);
		if (data.exists()) {
			data.delete();
		}
	}

	/**
	 * 判断缓存是否存在
	 * 
	 * @param cachefile
	 * @return
	 */
	public static boolean isExistDataCache(Context mContext, String cachefile) {
		boolean exist = false;
		File data = mContext.getFileStreamPath(cachefile);
		if (data.exists())
			exist = true;
		return exist;
	}

	/**
	 * 读取对象
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private static Serializable readObject(Context mContext, String file) {
		if (!isExistDataCache(mContext, file))
			return null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = mContext.openFileInput(file);
			ois = new ObjectInputStream(fis);
			return (Serializable) ois.readObject();
		} catch (FileNotFoundException e) {
		} catch (Exception e) {
			e.printStackTrace();
			// 反序列化失败 - 删除缓存文件
			if (e instanceof InvalidClassException) {
				File data = mContext.getFileStreamPath(file);
				data.delete();
			}
		} finally {
			try {
				ois.close();
			} catch (Exception e) {
			}
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return null;
	}

	/**
	 * 保存对象
	 * 
	 * @param ser
	 * @param file
	 * @throws IOException
	 */
	private static boolean saveObject(Context mContext, Serializable ser,
			String file) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = mContext.openFileOutput(file, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ser);
			oos.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				oos.close();
			} catch (Exception e) {
			}
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}
}
