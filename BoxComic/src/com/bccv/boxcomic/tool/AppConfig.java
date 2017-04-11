package com.bccv.boxcomic.tool;


import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;


public class AppConfig {
	
	
	/**
	 * 是否加载图片的配置
	 */
	public final static String CONF_LOAD_IMAGE = "perf_loadimage";
	
	/**
	 * 用户信息配置
	 */
	public final static String USER_UNAME = "username";
	public final static String USER_UID = "uid";
	public final static String USER_UHEADIMG = "headimg";
	public final static String USER_TYPE = "type";
	/**
	 * 新闻详情页字体大小配置
	 */
	public final static String CONF_NEWSPAGE_FONTSIZE = "pref_fontsize";
	public final static int NEWSPAGE_FONTSIZE_SMALL = 14; 
	public final static int NEWSPAGE_FONTSIZE_NORMAL = 16;
	public final static int NEWSPAGE_FONTSIZE_LARGER = 18;
	public final static int NEWSPAGE_FONTSIZE_EXTRA_LARGER = 20;
	

//	private static User user;
	
	
	/**
	 * 是否加载新闻详情图片
	 * @return
	 */
	public static boolean isLoadImage() {
		return getSharedPreferences().getBoolean(CONF_LOAD_IMAGE, true);
	}
	
	/**
	 * 设置是否加载新闻详情的图片
	 * @param isLoadImage
	 */
	public static void setLoadImageConfig(boolean isLoadImage){
		getSharedPreferences().edit().putBoolean(CONF_LOAD_IMAGE, isLoadImage);
	}
	
	public static SharedPreferences getSharedPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(GlobalParams.context);
	}
	
	/**
	 * 获取上一次刷新的时间
	 * @param categoryId
	 * @return
	 */
	public static String getLastUpdateTime(String categoryId){
		return getSharedPreferences().getString(categoryId, "");
	}
	
	/**
	 * 设置上一次刷新的时间
	 * @param timeText  时间
	 * @param categoryId  新闻类别id
	 */
	public static void setLastUpdateTime(String timeText,String categoryId){
		Editor editor = getSharedPreferences().edit();
		editor.putString(categoryId, timeText);
		editor.commit();
	}
	
	/**
	 * 导航类别是否已初始化
	 * @return
	 */
	public static boolean isCategoryInitialed(){
		return getSharedPreferences().getBoolean("isCategoryInitialed", false);
	}
	
	/**
	 * 设置本地导航类别是否初始化完成
	 * @param isCategoryInitialed
	 */
	public static void setCategoryInitialed(boolean isCategoryInitialed){
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean("isCategoryInitialed", isCategoryInitialed);
		editor.commit();
	}
	
	/**
	 * 获取新闻详情界面的字体大小
	 * @return
	 */
	public static int getPrefFontsize(){
		return getSharedPreferences().getInt(CONF_NEWSPAGE_FONTSIZE, NEWSPAGE_FONTSIZE_NORMAL);
	}
	
	/**
	 * 设置新闻详情界面的字体大小
	 * @param fontSize
	 */
	public static void setPrefFontSize(int fontSize){
		getSharedPreferences().edit().putInt(CONF_NEWSPAGE_FONTSIZE, fontSize).commit();
	}
	
//	/**
//	 * 设置全局用户信息
//	 * @param user
//	 */
//	public static void setPrefUserInfo(User user){
//		if (user != null) {
//			String uid = user.getUser_id();
//			
//			Editor editor = getSharedPreferences().edit();
//			editor.putString(USER_UID, uid);
//			editor.putString(USER_UHEADIMG, user.getUser_icon());
//			editor.putString(USER_UNAME, user.getUser_name());
//			editor.commit();
//		}
//		
//	}
//	
//	/**
//	 * 获取全局用户信息
//	 * @return
//	 */
//	public static User getPrefUserInfo(){
//		String uid = getSharedPreferences().getString(USER_UID,"");
//		String uiconString = getSharedPreferences().getString(USER_UHEADIMG, "");
//		String unameString = getSharedPreferences().getString(USER_UNAME, "");
//		if (uid == "") {
//			return null;
//		}
//		
//		if (user == null) {
//			user = new User();
//		}
//		
//		user.setUser_id(uid);
//		user.setUser_icon(uiconString);
//		user.setUser_name(unameString);
//		return user;
//	}
	
	/**
	 * 退出登录
	 */
	public static void loginOut(){
		Editor editor = getSharedPreferences().edit();
		editor.putString(USER_UID, "");
		editor.commit();
	}
	
	public static void setScreen(boolean isLanscape){
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean("isLanscape", isLanscape);
		editor.commit();
	}
	
	public static boolean getScreen(){
		return getSharedPreferences().getBoolean("isLanscape", true);
	}
	
	public static void setShowTime(boolean canShowTime){
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean("canShowTime", canShowTime);
		editor.commit();
	}
	
	public static boolean getShowTime(){
		return getSharedPreferences().getBoolean("canShowTime", true);
	}
	
	public static void setDoubleClick(boolean canDoubleClick){
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean("canDoubleClick", canDoubleClick);
		editor.commit();
	}
	
	public static boolean getDoubleClick(){
		return getSharedPreferences().getBoolean("canDoubleClick", true);
	}
	
	public static void setOnlineFrom(String onlinefrom){
		Editor editor = getSharedPreferences().edit();
		editor.putString("onlineFrom", onlinefrom);
		editor.commit();
	}
	
	public static String getOnlineFrom(){
		return getSharedPreferences().getString("onlineFrom", "");
	}
	
	public static void setTui(boolean isTui){
		
		Editor editor=getSharedPreferences().edit();
		editor.putBoolean("isTui", isTui);
		editor.commit();
	}
	
	public static boolean getisTui(){
		
		return getSharedPreferences().getBoolean("isTui", true);
		
		
	}
	
	
	
}
