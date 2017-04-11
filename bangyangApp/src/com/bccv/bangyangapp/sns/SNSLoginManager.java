package com.bccv.bangyangapp.sns;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.bccv.bangyangapp.sns.bean.SNSUserInfo;
import com.bccv.bangyangapp.sns.sina.SinaShare;
import com.bccv.bangyangapp.sns.tencent.qq.QQshare;
import com.bccv.bangyangapp.sns.tencent.weixin.WeiXinShareUtil;
import com.bccv.bangyangapp.utils.L;

public class SNSLoginManager {

	public final static int DEFAULT_TYPE = 0;
	public final static int SINA_WEIBO_TYPE = 1;
	public final static int TENCENT_WEIBO_TYPE = 2;
	public final static int TENCENT_QQ_TYPE = 3;
	public final static int TENCENT_WEIXIN_TYPE = 4;
	
	private static final String TAG = "SNSLoginManager";
	private static Context mContext;
	public AuthorizingStateListener mAuthorizingStateListener;
	
	private static SNSLoginManager mSNSLoginManager;
	
	public static void init(Context context){
		mContext = context;
		if(mSNSLoginManager==null){
			mSNSLoginManager = new SNSLoginManager(context);
		}
	}
	
	private SNSLoginManager(Context context){
		mContext = context;
	}
	
	public static SNSLoginManager getInstance(){
		if(mSNSLoginManager==null){
			throw new IllegalStateException(" SNSLoginManager has not been initialized ");
		}
		return mSNSLoginManager;
	}
	
	public void setAuthiringStateListener(
			AuthorizingStateListener authorizingStateListener) {
		mAuthorizingStateListener = authorizingStateListener;
		
	}

	public AuthorizingStateListener getAuthiringStateListener() {
		return mAuthorizingStateListener;
	}
	
	public void authorizeByType(Activity activity,int type){
		
		switch (type) {
			case SINA_WEIBO_TYPE:
				authorizeBySina(activity, type);
				break;
			case TENCENT_WEIBO_TYPE:
				authorizeByTencentWB(activity, type);
				break;
			case TENCENT_QQ_TYPE:
				authorizeByQQ(activity, type);
				break;
			case TENCENT_WEIXIN_TYPE:
				authorizeByWeiXin(mContext,type);
				break;
	
			default:
				break;
		}
	}
	
	/**
	 * 微信登陆授权
	 * @param context
	 * @param type
	 */
	private void authorizeByWeiXin(Context context,int type){
		WeiXinShareUtil.getInstance(mContext).login(mAuthorizingStateListener);
	}
	
	/**
	 * 新浪授权
	 * @param context
	 * @param type
	 */
	private void authorizeBySina(Activity context,int type){
		SinaShare.getInstance(mContext).authSinaWeibo(context, mAuthorizingStateListener);
	}
	
	/**
	 * QQ授权
	 * @param context
	 * @param type
	 */
	private void authorizeByQQ(Activity context,int type){
		QQshare.getInstance(mContext).authQQ(context, mAuthorizingStateListener);
	}
	
	/**
	 * 腾讯微博授权
	 * @param context
	 * @param type
	 */
	private void authorizeByTencentWB(Activity context,int type){
//		TencentWeiboShare.getInstance(mContext).authTencentWeibo(context, mAuthorizingStateListener);
	}
		
	
	public static void onActivityResult(Context context, int requestCode,int resultCode, Intent data) {
		L.v(TAG, "onActivityResult", "requestCode : " + requestCode);
		 if(requestCode == ShareConstants.AMAYA_ACTIVITY_RESULT_SINAWEIBO){
			 SinaShare.getInstance(context).onActivityResult(requestCode,resultCode,data);
		 }else if(requestCode == ShareConstants.AMAYA_ACTIVITY_RESULT_QQ){
			 QQshare.getInstance(context).onActivityResult(requestCode,resultCode,data);
		 }
	}
	
	
	/**
	 * 授权监听接口
	 * @author user
	 *
	 */
	public interface AuthorizingStateListener{
		public final static int  AUTHORIZING = 1;
		public final static int  AUTHORIZE_SUCCESS = 2;
		public final static int  AUTHORIZE_CANCEL = 3;
		public final static int  AUTHORIZE_ERROR = 4;
		
		/**
		 * 授权中事件回调
		 * @param action 事件类型
		 * 		AuthorizingStateListener.AUTHORIZING		授权开始时调用
		 * 		AuthorizingStateListener.AUTHORIZE_SUCCESS	授权成功后调用
		 * 		AuthorizingStateListener.AUTHORIZE_CANCEL	授权取消时调用
		 * 		AuthorizingStateListener.AUTHORIZE_ERROR	授权出错时调用
		 * @param info 事件信息
		 */
		public void authrizeAction(int action,String info);
		
		/**
		 * 获取用户信息后的事件回调
		 * @param user 用户信息类
		 */
		public void onUserInfoComplete(SNSUserInfo user);
	}
	
}
