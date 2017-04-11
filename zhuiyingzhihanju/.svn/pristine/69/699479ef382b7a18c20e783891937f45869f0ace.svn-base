package com.utils.tools;



import com.bccv.zhuiyingzhihanju.model.User;

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
	public final static String NICK_NAME = "nick_name";
	public final static String SEX="sex";
	public final static String MSG="msg";
	public final static String TOKEN="token";
	
	
	
	/**
	 * 新闻详情页字体大小配置
	 */
	public final static String CONF_NEWSPAGE_FONTSIZE = "pref_fontsize";
	public final static int NEWSPAGE_FONTSIZE_SMALL = 14; 
	public final static int NEWSPAGE_FONTSIZE_NORMAL = 16;
	public final static int NEWSPAGE_FONTSIZE_LARGER = 18;
	public final static int NEWSPAGE_FONTSIZE_EXTRA_LARGER = 20;
	

	private static User user;

	
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
	 * 是否存储在SD卡中
	 * @return
	 */
	public static boolean getisSD(){
		return getSharedPreferences().getBoolean("isSD", false);
	}
	
	/**
	 * 记载下载路径
	 * @param isCategoryInitialed
	 */
	public static void setSD(boolean isSD){
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean("isSD", isSD);
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
	

	/**
	 * 设置全局用户信息
	 * 
	 * @param user
	 */
	public static void setPrefUserInfo(User user) {
		if (user != null) {
			String uid = user.getUid();

			Editor editor = getSharedPreferences().edit();
			editor.putString(USER_UID, uid);
			editor.putString(USER_UHEADIMG, user.getAvatars());
			editor.putString(USER_UNAME, user.getPhone());
			editor.putString(NICK_NAME, user.getNick_name());
			editor.putString(SEX, user.getSex());
			editor.putString(MSG, user.getDes());
			editor.putString(TOKEN, user.getToken());
			
			editor.commit();

		}

	}
	
	/**
	 * 获取全局用户信息
	 * @return
	 */
	public static User getPrefUserInfo(){
		String uid = getSharedPreferences().getString(USER_UID, "");
		String uiconString = getSharedPreferences()
				.getString(USER_UHEADIMG, "");
		String unameString = getSharedPreferences().getString(USER_UNAME, "");
		String nicNameString = getSharedPreferences().getString(NICK_NAME, "");
		String sexString = getSharedPreferences().getString(SEX, "");
		String msgString = getSharedPreferences().getString(MSG, "");
		String tokenString = getSharedPreferences().getString(TOKEN, "");
		if (uid == "") {
			return null;
		}

		if (user == null) {
			user = new User();
		}

		user.setUid(uid);
		user.setAvatars(uiconString);
		user.setPhone(unameString);
		user.setNick_name(nicNameString);
		user.setDes(msgString);
		user.setSex(sexString);
		user.setToken(tokenString);
		
		return user;
	}
	
	/**
	 * 退出登录
	 */
	public static void loginOut(){
		Editor editor = getSharedPreferences().edit();
		editor.putString(USER_UID, "");
		editor.commit();
	}
	
	public static void setWifi(boolean isLanscape){
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean("isWifi", isLanscape);
		editor.commit();
	}
	
	public static boolean getWifi(){
		return getSharedPreferences().getBoolean("isWifi", false);
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
