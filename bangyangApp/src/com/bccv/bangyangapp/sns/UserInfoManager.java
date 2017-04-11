package com.bccv.bangyangapp.sns;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.bccv.bangyangapp.model.UserInfo;


public class UserInfoManager {

	@SuppressWarnings("unused")
	private static String TAG = "UserInfoManager";
	
	private static String USERINFO_SP = "userinfo_sp";
	
	private static SharedPreferences sp;
	
	private static String USER_NAME = "user_name";
	private static String USER_ICON = "user_icon";
	private static String USER_TYPE = "user_type";
	private static String USER_BACKDROP = "backdrop";
	private static String USER_GROUP = "user_group";
	private static String USER_MONEY = "user_money";
	private static String USER_SCORE = "user_score";
	private static String USER_ID = "user_id";
	private static String USER_BINDS = "user_binds";
	private static String USER_TOKEN = "user_token";
	private static String USER_INTRODUCE = "user_introduce";
	
	private static Context mContext;
	/**
	 * 初始化帐户管理类 需在启动应用时做初始化
	 * 
	 * @param context
	 */
	public static void init(Context context) {
		mContext = context;
		sp = mContext.getSharedPreferences(
				USERINFO_SP, Context.MODE_MULTI_PROCESS);
	}
	
	public synchronized static void saveUserInfo(UserInfo userInfo){
		if(userInfo!=null){
			Editor edit = sp.edit();
			
			if(userInfo.getUser_id()!=-1){
				edit.putInt(USER_ID, userInfo.getUser_id());
			}else{
				edit.commit();
				return;
			}
			
			switch (userInfo.getUser_type()) {
				case SNSLoginManager.TENCENT_QQ_TYPE:
						edit.putString(USER_TYPE, "QQ");
					break;
				case SNSLoginManager.SINA_WEIBO_TYPE:
						edit.putString(USER_TYPE, "SINA");
					break;
				case SNSLoginManager.TENCENT_WEIXIN_TYPE:
						edit.putString(USER_TYPE, "WEIXIN");
					break;
				default:
					break;
			}
			
			
			if(!TextUtils.isEmpty(userInfo.getUser_name())){
				edit.putString(USER_NAME, userInfo.getUser_name());
			}
			if(!TextUtils.isEmpty(userInfo.getUser_icon())){
				edit.putString(USER_ICON, userInfo.getUser_icon());
			}
			if(!TextUtils.isEmpty(userInfo.getBackdrop())){
				edit.putString(USER_BACKDROP, userInfo.getBackdrop());
			}
			if(!TextUtils.isEmpty(userInfo.getUser_group())){
				edit.putString(USER_GROUP, userInfo.getUser_group());
			}
			if(!TextUtils.isEmpty(userInfo.getUser_token())){
				edit.putString(USER_TOKEN, userInfo.getUser_token());
			}
			if(!TextUtils.isEmpty(userInfo.getUser_binds())){
				edit.putString(USER_BINDS, userInfo.getUser_binds());
			}
			if(!TextUtils.isEmpty(userInfo.getUser_introduce())){
				edit.putString(USER_INTRODUCE, userInfo.getUser_introduce());
			}
			edit.putInt(USER_SCORE, userInfo.getUser_score());

			edit.commit();
		}
	}
	
	/**
	 * 保存userid
	 * @param userId
	 */
	public static void saveUserId(int userId){
		Editor edit = sp.edit();
		edit.putInt(USER_ID, userId);
		edit.commit();
	}
	
	/**
	 * 获取userid
	 * @return
	 */
	public static int getUserId(){
		return sp.getInt(USER_ID, -1);
	}
	
	
	/**
	 * 获取userType
	 * @return
	 */
	public static String getUserType(){
		return sp.getString(USER_TYPE, "");
	}
	
	/**
	 * 保存用户名
	 * @param userName
	 */
	public static void saveUserName(String userName){
		Editor edit = sp.edit();
		edit.putString(USER_NAME, userName);
		edit.commit();
	}
	
	/**
	 * 获取用户名
	 * @return
	 */
	public static String getUserName(){
		return sp.getString(USER_NAME, "");
	}
	
	/**
	 * 保存用户头像
	 * @param userIcon
	 */
	public static void saveUserIcon(String userIcon){
		Editor edit = sp.edit();
		edit.putString(USER_ICON, userIcon);
		edit.commit();
	}
	
	/**
	 * 获取用户头像
	 * @return
	 */
	public static String getUserIcon(){
		return sp.getString(USER_ICON, "");
	}
	
	/**
	 * 保存用户绑定信息
	 * @param userIcon
	 */
	public static void saveUserBinds(String binds){
		Editor edit = sp.edit();
		edit.putString(USER_BINDS, binds);
		edit.commit();
	}
	
	/**
	 * 获取用户绑定信息
	 * @return
	 */
	public static String getUserBinds(){
		return sp.getString(USER_BINDS, "");
	}
	
	/**
	 * 保存用户空间背景
	 * @param userIcon
	 */
	public static void saveUserBackdrop(String backDrop){
		Editor edit = sp.edit();
		edit.putString(USER_BACKDROP, backDrop);
		edit.commit();
	}
	
	/**
	 * 获取用户空间背景
	 * @return
	 */
	public static String getUserBackdrop(){
		return sp.getString(USER_BACKDROP, "");
	}
	
	/**
	 * 保存用户族群
	 * @param userGroup
	 */
	public static void saveUserGroup(String userGroup){
		Editor edit = sp.edit();
		edit.putString(USER_GROUP, userGroup);
		edit.commit();
	}
	
	/**
	 * 获取用户族群
	 * @return
	 */
	public static String getUserGroup(){
		return sp.getString(USER_GROUP, "");
	}

	/**
	 * 保存用户金币数
	 * @param userMoney
	 */
	public static void saveUserMoney(int userMoney){
		Editor edit = sp.edit();
		edit.putInt(USER_MONEY, userMoney);
		edit.commit();
	}
	
	/**
	 * 获取用户金币数量
	 * @return
	 */
	public static int getUserMoney(){
		return sp.getInt(USER_MONEY, 0);
	}
	
	/**
	 * 保存用户积分
	 * @param userScore
	 */
	public static void saveUserScore(int userScore){
		Editor edit = sp.edit();
		edit.putInt(USER_SCORE, userScore);
		edit.commit();
	}
	
	/**
	 * 获取用户积分
	 * @return
	 */
	public static int getUserScore(){
		return sp.getInt(USER_SCORE, 0);
	}
	
	/**
	 * 保存用户token
	 * @param userScore
	 */
	public static void saveUserToken(String userToken){
		Editor edit = sp.edit();
		edit.putString(USER_TOKEN, userToken);
		edit.commit();
	}
	
	/**
	 * 获取用户token
	 * @return
	 */
	public static String getUserToken(){
		return sp.getString(USER_TOKEN, "");
	}
	
	/**
	 * 保存用户token
	 * @param userScore
	 */
	public static void saveUserIntroduce(String userIntroduce){
		Editor edit = sp.edit();
		edit.putString(USER_INTRODUCE, userIntroduce);
		edit.commit();
	}
	
	/**
	 * 获取用户token
	 * @return
	 */
	public static String getUserIntroduce(){
		return sp.getString(USER_INTRODUCE, "");
	}
	
	/**
	 * 是否登录
	 * @return
	 */
	public static boolean isLogin(){
		return getUserId() != -1;
	}
	
	/**
	 * 用户退出
	 */
	public static void logOut(){
		clearUserInfo();
	}
	
	
	public static void clearUserInfo(){
		Editor edit = sp.edit();
		edit.putInt(USER_ID, -1);
		edit.putString(USER_NAME, "");
		edit.putString(USER_ICON, "");
		edit.putString(USER_BINDS, "");
		edit.putString(USER_BACKDROP, "");
		edit.putString(USER_GROUP, "");
		edit.putInt(USER_MONEY, 0);
		edit.putInt(USER_SCORE, 0);
		edit.putString(USER_TOKEN, "");
		edit.putString(USER_INTRODUCE, "");
		edit.commit();
	}
	
}
