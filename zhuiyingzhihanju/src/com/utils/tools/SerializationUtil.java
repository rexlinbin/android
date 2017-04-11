package com.utils.tools;

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

import com.bccv.zhuiyingzhihanju.model.Movie;
import com.utils.model.DownloadMovie;

import android.content.Context;

public class SerializationUtil {

	public static final String COLLECT_DATA = "CollectData.txt";
	public static final String SEARCHHISTORY_DATA = "SearchHistoryData.txt";
	public static final String HISTORY_DATA = "HistoryData.txt";
	public static final String DOWNLOAD_DATA = "DownloadData.txt";
	public static final String MAIN_DATA = "MainData.txt";
	public static final String MAIN_TVDATA = "MainTVData.txt";

	/**
	 * 
	 * 首界面缓存
	 * 
	 * 
	 * 
	 **/
	public static void wirteMainSerialization(Context context, Serializable ser) {

		saveObject(context, ser, MAIN_DATA);

	}

	/**
	 * 
	 * 读取首界面缓存
	 * 
	 ***/

	public static List<Movie> readMainSerialization(Context context) {

		return (List<Movie>) readObject(context, MAIN_DATA);

	}

	/**
	 * 
	 * 写入首界面电视缓存
	 * 
	 * 
	 * 
	 **/
	public static void wirteMainTVSerialization(Context context, Serializable ser) {

		saveObject(context, ser, MAIN_TVDATA);

	}

	/**
	 * 
	 * 读取面电视缓存
	 * 
	 * 
	 * 
	 **/
	public static List<Movie> readMainTVSerialization(Context context) {

		return (List<Movie>) readObject(context, MAIN_TVDATA);

	}

	/**
	 * 存储收藏 数据
	 * 
	 * @param mContext
	 * @param ser
	 */
	public static void wirteCollectSerialization(Context mContext, Serializable ser) {
		saveObject(mContext, ser, COLLECT_DATA);
	}

	/**
	 * 读取收藏 缓存
	 * 
	 * @param mContext
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Movie> readCollectCache(Context mContext) {
		return (List) readObject(mContext, COLLECT_DATA);
	}

	/**
	 * 存储搜索历史记录 数据
	 * 
	 * @param mContext
	 * @param ser
	 */
	public static void wirteHistorySerialization(Context mContext, Serializable ser) {
		saveObject(mContext, ser, HISTORY_DATA);
	}

	/**
	 * 读取搜索历史记录 缓存
	 * 
	 * @param mContext
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Movie> readHistoryCache(Context mContext) {
		return (List) readObject(mContext, HISTORY_DATA);
	}

	/**
	 * 存储搜索历史记录 数据
	 * 
	 * @param mContext
	 * @param ser
	 */
	public static void wirteSelectHistorySerialization(Context mContext, Serializable ser) {
		saveObject(mContext, ser, SEARCHHISTORY_DATA);
	}

	/**
	 * 读取搜索历史记录 缓存
	 * 
	 * @param mContext
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<String> readSelectHistoryCache(Context mContext) {
		return (List) readObject(mContext, SEARCHHISTORY_DATA);
	}

	/**
	 * 存储下载记录 数据
	 * 
	 * @param mContext
	 * @param ser
	 */
	public static void wirteDownloadSerialization(Context mContext, Serializable ser) {
		saveObject(mContext, ser, DOWNLOAD_DATA);
	}

	/**
	 * 读取下载记录 缓存
	 * 
	 * @param mContext
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, DownloadMovie> readDownloadCache(Context mContext) {
		return (Map<String, DownloadMovie>) readObject(mContext, DOWNLOAD_DATA);
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
	private static boolean saveObject(Context mContext, Serializable ser, String file) {
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
