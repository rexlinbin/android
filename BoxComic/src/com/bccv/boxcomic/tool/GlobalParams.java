package com.bccv.boxcomic.tool;

import java.util.Map;

import com.bccv.boxcomic.activity.ReadComicActivity;
import com.bccv.boxcomic.activity.ReadComicActivity.OnFragmentClickListener;
import com.bccv.boxcomic.activity.ReadComicLocalActivity;
import com.bccv.boxcomic.ebook.EbookActivity;
import com.bccv.boxcomic.ebook.PageActivity;
import com.bccv.boxcomic.modal.User;
import com.bccv.boxcomic.updatedownload.Version;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import android.R.integer;
import android.app.Activity;
import android.content.Context;

/**
 * 全局变量。（请为每个全局变量添加详细注释）。
 * @author WuYelin
 *
 */
public class GlobalParams {
	
	public static String PROXY_IP = null;
	public static int PROXY_PORT = 0;
	/**
	 * 全局Context
	 */
	public static Context context;
	/**
	 * ImageLoader的全局配置
	 */
	public static DisplayImageOptions options;
	public static DisplayImageOptions comicOptions;
	public static DisplayImageOptions headOptions;
	public static DisplayImageOptions frameHomeoptions;
	public static Activity activity;
	public static int rawX;
	public static int rawY;
	public static int state = 1;
	public static int collect = 0;
	
	public static boolean TimeOut = false;
	
	public static User user = new User();
	public static String selectedChannel = "";
	
	public static String imageHeadUrlString = "http://dmappimg.boxuu.com/mhimg/";
	public static String imageUrlString="http://img-comic.boxuu.com/";
	public static String BookImagepic="http://img-comic.boxuu.com/xs/";
	public static String appKeyString = "";
	
	public static String imageHighString = "";
	
	public static ReadComicActivity readComicActivity;
	public static ReadComicLocalActivity readComicLocalActivity;
	
	public static boolean canUpdate = false;
	public static Version updateVersion;
	
	public static boolean isLanscape = true;
	public static boolean canShowTime = true;
	public static boolean canDoubleClick = true;
	public static boolean isTui=true;
	
	public static String nextComicString = "";
	public static String preComicString = "";
	
	public static int screenWidth;
	public static int screenHeight;
	
	public static boolean isEditing = false;
	
	public static String downloadPathString = "";
	
	public static String ebookDownloadPathString = "";
	
	public static String localComicPathString = "";
	
	public static String md5String = "3eda670b7b9fdd70a667702b8fc2872e";
	
	public static String user_agentString = "hezidongman";
	
	public static EbookActivity pageActivity;
	
	public static String onlineFromMenuIdString = "";
	
	public static String onlineFromNameIdString = "";
	
	public static boolean hasBook = true;
	
	public static boolean isLoadingActivity = false;
	
}
