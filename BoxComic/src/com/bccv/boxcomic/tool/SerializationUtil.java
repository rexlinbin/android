package com.bccv.boxcomic.tool;

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

import com.bccv.boxcomic.modal.Chapter;
import com.bccv.boxcomic.ebook.BookInfo;
import com.bccv.boxcomic.ebook.Bookmarks;
import com.bccv.boxcomic.ebook.ChapterInfo;
import com.bccv.boxcomic.fragment.MainChildFragment;
import com.bccv.boxcomic.modal.Channel;
import com.bccv.boxcomic.modal.Comic;
import com.bccv.boxcomic.modal.MainItem;

import android.content.Context;

public class SerializationUtil {

	public static final String COLLECT_DATA = "CollectData.txt";
	public static final String CHAPTER_DATA = "ChapterData.txt";

	public static final String COMICINFO_DATA = "ComicInfoData.txt";
	public static final String HISTORY_DATA = "HistoryData.txt";
	public static final String BOOKMARK_DATA = "BookmarkData.txt";
	public static final String BOOKMARKCOMIC_DATA = "BookmarkComicData.txt";
	public static final String BOOKINFO_DATA = "bookInfoData.txt";

	public static final String COMICCHANNEL_DATA = "ComicChannelData.txt";
	public static final String EBOOKCHANNEL_DATA = "EbookChannelData.txt";
	
	public static final String POSTFIX = ".mark";

	public static final String ONLINEFROM_DATA  = "OnlineFromData.txt";
	
	/**
	 * 存储章节书签 数据
	 * 
	 * @param mContext
	 * @param ser
	 */
	public static void wirteBookmarkSerialization(Context mContext,
			Serializable ser, String comic_id) {
		saveObject(mContext, ser, comic_id + ".txt");
	}

	/**
	 * 读取章节书签 缓存
	 * 
	 * @param mContext
	 * @return
	 */
	public static List<Chapter> readBookmarkCache(Context mContext,
			String comic_id) {
		return (List) readObject(mContext, comic_id + ".txt");
	}
	
	/**
	 * 存储章节书签 数据
	 * 
	 * @param mContext
	 * @param ser
	 */
	public static void wirteOnlineFromSerialization(Context mContext,
			Serializable ser) {
		saveObject(mContext, ser, ONLINEFROM_DATA);
	}

	/**
	 * 读取章节书签 缓存
	 * 
	 * @param mContext
	 * @return
	 */
	public static Map<String, String> readOnlineFromCache(Context mContext) {
		return (Map<String, String>) readObject(mContext, ONLINEFROM_DATA);
	}

	/**
	 * 存储漫画信息 数据
	 * 
	 * @param mContext
	 * @param ser
	 */
	public static void wirteComicInfoSerialization(Context mContext,
			Serializable ser) {
		saveObject(mContext, ser, GlobalParams.onlineFromNameIdString + COMICINFO_DATA);
	}

	/**
	 * 读取漫画信息 缓存
	 * 
	 * @param mContext
	 * @return
	 */
	public static List<Comic> readComicInfoCache(Context mContext) {
		return (List) readObject(mContext, GlobalParams.onlineFromNameIdString + COMICINFO_DATA);
	}

	/**
	 * 存储漫画分类 数据
	 * 
	 * @param mContext
	 * @param ser
	 */
	public static void wirteComicChannelSerialization(Context mContext,
			Serializable ser) {
		saveObject(mContext, ser, COMICCHANNEL_DATA);
	}

	/**
	 * 读取漫画分类 缓存
	 * 
	 * @param mContext
	 * @return
	 */
	public static List<Channel> readComicChannelCache(Context mContext) {
		return (List) readObject(mContext, COMICCHANNEL_DATA);
	}
	
	/**
	 * 存储小说分类 数据
	 * 
	 * @param mContext
	 * @param ser
	 */
	public static void wirteEbookChannelSerialization(Context mContext,
			Serializable ser) {
		saveObject(mContext, ser, EBOOKCHANNEL_DATA);
	}

	/**
	 * 读取小说分类 缓存
	 * 
	 * @param mContext
	 * @return
	 */
	public static List<Channel> readEbookChannelCache(Context mContext) {
		return (List) readObject(mContext, EBOOKCHANNEL_DATA);
	}
	
	/**
	 * 存储小说列表 数据
	 * 
	 * @param mContext
	 * @param ser
	 */
	public static void wirteBookInfoSerialization(Context mContext,
			Serializable ser) {
		saveObject(mContext, ser, BOOKINFO_DATA);
	}

	/**
	 * 读取小说列表 缓存
	 * 
	 * @param mContext
	 * @return
	 */
	public static List<BookInfo> readBookInfoCache(Context mContext) {
		return (List) readObject(mContext, BOOKINFO_DATA);
	}

	/**
	 * 存储漫画书签 数据
	 * 
	 * @param mContext
	 * @param ser
	 */
	public static void wirteBookmarkComicSerialization(Context mContext,
			Serializable ser) {
		saveObject(mContext, ser, BOOKMARKCOMIC_DATA);
	}

	/**
	 * 读取漫画书签 缓存
	 * 
	 * @param mContext
	 * @return
	 */
	public static List<Comic> readBookmarkComicCache(Context mContext) {
		return (List) readObject(mContext, BOOKMARKCOMIC_DATA);
	}

	public static final String HOME_HOT = "HomeHot.txt";
	public static final String HOME_UPDATE = "HomeUpdate.txt";

	/**
	 * 存储主页数据
	 * 
	 * @param mContext
	 * @param ser
	 */
	public static void wirteHomeSerialization(Context mContext, Serializable ser) {

		saveObject(mContext, ser, HOME_HOT);

	}

	public static void wirteUPDATeSerialization(Context mContext,
			Serializable ser) {

		saveObject(mContext, ser, HOME_UPDATE);

	}

	/**
	 * 读取主页缓存
	 * 
	 * @param mContext
	 * @return
	 */
	public static List<Comic> readHomeCache(Context mContext) {

		return (List) readObject(mContext, HOME_HOT);

	}

	public static List<Comic> readUpdateCache(Context mContext) {

		return (List) readObject(mContext, HOME_UPDATE);

	}

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
	public static List<Comic> readCollectCache(Context mContext) {
		return (List) readObject(mContext, COLLECT_DATA);
	}

	/**
	 * 存储历史记录 数据
	 * 
	 * @param mContext
	 * @param ser
	 */
	public static void wirteHistorySerialization(Context mContext,
			Serializable ser) {
		saveObject(mContext, ser, "new" + HISTORY_DATA);
	}

	/**
	 * 读取历史记录 缓存
	 * 
	 * @param mContext
	 * @return
	 */
	public static List<Comic> readHistoryCache(Context mContext) {
		return (List) readObject(mContext, "new" + HISTORY_DATA);
	}

	/**
	 * 存储最新历史记录 数据
	 * 
	 * @param mContext
	 * @param ser
	 */
	public static void wirteNewestHistorySerialization(Context mContext,
			Serializable ser) {
		saveObject(mContext, ser, GlobalParams.onlineFromNameIdString + HISTORY_DATA);
	}

	/**
	 * 读取最新历史记录 缓存
	 * 
	 * @param mContext
	 * @return
	 */
	public static List<Comic> readNewestHistoryCache(Context mContext) {
		return (List) readObject(mContext, GlobalParams.onlineFromNameIdString + HISTORY_DATA);
	}
	
	/**
	 * 保存本地关注
	 * 
	 * @param mContext
	 * @param ser
	 */
	public static void wirteSerializationBookmarks(Context mContext,
			Serializable ser, String bookName) {
		saveObject(mContext, ser, bookName + POSTFIX);
	}

	/**
	 * 读取本地关注列表
	 * 
	 * @param mContext
	 * @return
	 */
	public static Bookmarks readSerializationBookmarks(Context mContext,
			String bookName) {
		return (Bookmarks) readObject(mContext, bookName + POSTFIX);
	}

	/**
	 * 保存小说章节列表
	 * 
	 * @param mContext
	 * @param ser
	 */
	public static void wirteSerializationEbookChapters(Context mContext,
			Serializable ser, String bookName) {
		saveObject(mContext, ser, bookName + "Chapters" + POSTFIX);
	}

	/**
	 * 读取小说章节列表
	 * 
	 * @param mContext
	 * @return
	 */
	public static List<ChapterInfo> readSerializationEbookChapters(
			Context mContext, String bookName) {
		return (List) readObject(mContext, bookName + "Chapters" + POSTFIX);
	}

	/**
	 * 清除本地关注列表
	 * 
	 * @param context
	 */
	public static void removeSerializationBookmarks(Context context,
			String bookName) {
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
