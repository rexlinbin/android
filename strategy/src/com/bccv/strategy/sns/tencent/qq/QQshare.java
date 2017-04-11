package com.bccv.strategy.sns.tencent.qq;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.bccv.strategy.sns.SNSLoginManager;
import com.bccv.strategy.sns.SNSLoginManager.AuthorizingStateListener;
import com.bccv.strategy.sns.SNSShareManager;
import com.bccv.strategy.sns.SNSShareManager.ShareStateListener;
import com.bccv.strategy.sns.ShareConstants;
import com.bccv.strategy.sns.bean.SNSUserInfo;
import com.bccv.strategy.sns.utils.SNSTokenKeeper;
import com.bccv.strategy.utils.L;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class QQshare {

    private Tencent mTencent;
    private QzoneShare mShare;
    
    private QQShare mQShare;
	
	private static final String TAG = QQshare.class.getName(); 
	
	
	public static QQshare mQQShare;
    
    private QQshare(){}
    
	public static QQshare getInstance(Context context){
		if(mQQShare==null){
			mQQShare = new QQshare();
		}
		return mQQShare;
	}
	
	
	private boolean inimTencent(Context context){
		mTencent =  Tencent.createInstance(ShareConstants.AMAYA_QQ_ID,context);
		mTencent.logout(context);
		SNSTokenKeeper.readQQToken(context,mTencent);
		
		if(mTencent.isSessionValid()){
			mShare =  new QzoneShare(context,mTencent.getQQToken());
			return true;
		}
		return false;
		
	}
	
	
    /**
     * QQ授权
     * @param mContext
     * @param mAuthorizingStateListener 授权监听	
     */
    public void authQQ(Activity mContext,AuthorizingStateListener mAuthorizingStateListener){
    	authQQ(mContext, mAuthorizingStateListener, false);
    }
	
	/**
	 * @param mContext
	 * @param mAuthorizingStateListener
	 * @param fromShare	是否是分享授权
	 */
	private void authQQ(final Context mContext,final AuthorizingStateListener mAuthorizingStateListener,final boolean fromShare){
		
    	mTencent =  Tencent.createInstance(ShareConstants.AMAYA_QQ_ID,mContext);
    	
    	if(mTencent.isSessionValid()){
    		mTencent.logout(mContext);
    	}
    	mTencent.login((Activity)mContext, "all",new IUiListener() {
    		
    		@Override
    		public void onComplete(Object response) {
    			// TODO Auto-generated method stub

   			 try {
                    //{"ret":0,"pay_token":"FCABB6BF240491F58A3A571976ABA41E","pf":"desktop_m_qq-10000144-android-2002-","query_authority_cost":134,"authority_cost":5764,"openid":"4006284219847AC2805B32E911ABCA52","expires_in":7776000,"pfkey":"c500c8b1867613fb54ecde6f9b27fd6c","msg":"","access_token":"08ED4C56A2CB1911B94EEC98B465CE5B","login_cost":578}
                    JSONObject jsonObject = (JSONObject) response;
                    String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
                    String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
                    String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
                    
                    L.v(TAG, "authQQ",  "fromShare : " + fromShare +" bundle : " + jsonObject.toString());
                    
                    if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                            && !TextUtils.isEmpty(openId)) {
                    	mTencent.setAccessToken(token, expires);
                    	mTencent.setOpenId(openId);
                    }
                    
                    if(fromShare){
                    	SNSTokenKeeper.saveQQToken(mContext, mTencent);
                   	 	//TODO 来自分享 授权成功
                    	if(mAuthorizingStateListener!=null) 
                    		mAuthorizingStateListener.authrizeAction(AuthorizingStateListener.AUTHORIZE_SUCCESS, "sina auth success for share");
                    	return;
                    }
                    if(mAuthorizingStateListener!=null) 
                    	mAuthorizingStateListener.authrizeAction(AuthorizingStateListener.AUTHORIZE_SUCCESS, " QQ auth success , waiting get userinfo ");
                    
                    SNSUserInfo snsUserInfo = new SNSUserInfo();
                    snsUserInfo.snsType = SNSLoginManager.TENCENT_QQ_TYPE;
                	snsUserInfo.expiresTime = String.valueOf(Long.valueOf(expires)*1000+System.currentTimeMillis());
                	snsUserInfo.accessToken = token;
                	snsUserInfo.thirdPlatformID = openId;
                    
                	//获取用户信息
                    UserInfo mInfo = new UserInfo(mContext, mTencent.getQQToken());
                    QQAuthIUiListener mListener = new QQAuthIUiListener(mAuthorizingStateListener, snsUserInfo);
                    mInfo.getUserInfo(mListener);
                    
                } catch(Exception e) {
                	L.e(TAG, "authQQ onComplete", e.getMessage());
                	if(mAuthorizingStateListener!=null){
                		mAuthorizingStateListener.authrizeAction(AuthorizingStateListener.AUTHORIZE_ERROR, " json parse error ! ");
                	}
                }
    		}
			
			@Override
			public void onError(UiError uiError) {
				// TODO Auto-generated method stub
            	L.e(TAG, "authQQ onError", uiError.toString());
            	if(mAuthorizingStateListener!=null){
            		mAuthorizingStateListener.authrizeAction(AuthorizingStateListener.AUTHORIZE_ERROR, uiError.toString());
            	}
			}
			
			@Override
			public void onCancel() {
	           	if(mAuthorizingStateListener!=null){
            		mAuthorizingStateListener.authrizeAction(AuthorizingStateListener.AUTHORIZE_CANCEL, " QQ auth cancel ");
            	}
			}
		});
    	
    	if(mAuthorizingStateListener==null){
    		throw new IllegalArgumentException(" AuthorizingStateListener can't be null !!!");
    	}else{
    		mAuthorizingStateListener.authrizeAction(AuthorizingStateListener.AUTHORIZING, " QQ authorzing ! ");
    	}
    	
	}
	
	/**
	 * 分享至Qzone
	 * @param context	activity
	 * @param targetUrl	链接
	 * @param title		标题
	 * @param content	内容
	 * @param imageUrl	图片url
	 * @param mShareStateListener	监听
	 */
	public void share2Qzone(final Activity context,final String title,final String content,final String imageUrl,final String targetUrl,final ShareStateListener mShareStateListener){
    	
    	if(mShareStateListener==null){
    		throw new IllegalArgumentException(" ShareStateListener can't be null !!!");
    	}else{
    		mShareStateListener.shareAction(ShareStateListener.SHARING, " sina sharing ! ",SNSShareManager.TENCENT_QZONE_TYPE);
    	}
		
//    	if(inimTencent(context)){
    		mTencent =  Tencent.createInstance(ShareConstants.AMAYA_QQ_ID,context);
    		ready2share2Qzone(context,title,content, imageUrl, targetUrl, mShareStateListener);
    		
//    	}else{
//    		AuthorizingStateListener authorizingStateListener = new AuthorizingStateListener() {
//    			
//    			@Override
//    			public void onUserInfoComplete(SNSUserInfo user) {}
//    			
//    			@Override
//    			public void authrizeAction(int action, String info) {
//    				switch (action) {
//    				case AuthorizingStateListener.AUTHORIZE_SUCCESS:
//    					ready2share2Qzone(context,title,content, imageUrl, targetUrl, mShareStateListener);
//    					break;
//    				case AuthorizingStateListener.AUTHORIZE_CANCEL:
//					case AuthorizingStateListener.AUTHORIZE_ERROR:
//						mShareStateListener.shareAction(ShareStateListener.SHARE_FAILED, "share failed by auth failed");
//						break;
//					default:
//						break;
//					}
//    			}
//
//    		};
//    	
//    		authQQ(context, authorizingStateListener, true);
//    	}
	}
	
	private void ready2share2Qzone(final Activity context, String title,String content, String imageUrl, String targetUrl,final ShareStateListener mShareStateListener) {
//		if(mTencent.isSessionValid()){
			mShare =  new QzoneShare(context,mTencent.getQQToken());
//			}
		
		if(mShare==null){
			if(mShareStateListener!=null){
				mShareStateListener.shareAction(ShareStateListener.SHARE_FAILED, " mShare is null ",SNSShareManager.TENCENT_QZONE_TYPE);
			}
			return;
		}
		
		 final Bundle params = new Bundle();
		 params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
		 params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
		 params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, content);
		 params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
		 ArrayList<String> imageUrls = new ArrayList<String>();
		 if(!TextUtils.isEmpty(imageUrl)&&imageUrl.startsWith("http://")){
			 imageUrls.add(imageUrl);
		 }
		 params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
		 
		 new Thread(new Runnable() {

			@Override
			public void run() {
				 mShare.shareToQzone(context, params,new IUiListener() {
					 @Override
					 public void onComplete(Object response) {
						 L.v(TAG, "ready2share onComplete", response + "");
						 if(mShareStateListener!=null){
							 mShareStateListener.shareAction(ShareStateListener.SHARE_SUCCESS, " Qzone share success ",SNSShareManager.TENCENT_QZONE_TYPE);
						 }
					 }
					 
					 @Override
					 public void onCancel() {
						 L.v(TAG, "ready2share onCancel", " Qzone share cancel ");
						 if(mShareStateListener!=null){
							 mShareStateListener.shareAction(ShareStateListener.SHARE_CANCEL, " Qzone share cancel ",SNSShareManager.TENCENT_QZONE_TYPE);
						 }
					 }
					
					@Override
					public void onError(UiError uiError) {
						L.e(TAG, "ready2share onError", uiError.toString());
						if(mShareStateListener!=null){
							mShareStateListener.shareAction(ShareStateListener.SHARE_FAILED, uiError.toString(),SNSShareManager.TENCENT_QZONE_TYPE);
						}
					}
					
				});
			}
			 
		 }).start();
		 
	}
	
	
	/**
	 * 分享至Qzone
	 * @param context	activity
	 * @param targetUrl	链接
	 * @param title		标题
	 * @param content	内容
	 * @param imageUrl	图片url
	 * @param mShareStateListener	监听
	 */
	public void share2QQ(final Activity context,final String title,final String content,final String imageUrl,final String targetUrl,final ShareStateListener mShareStateListener){
    	
    	if(mShareStateListener==null){
    		throw new IllegalArgumentException(" ShareStateListener can't be null !!!");
    	}else{
    		mShareStateListener.shareAction(ShareStateListener.SHARING, " sina sharing ! ",SNSShareManager.TENCENT_QQ_TYPE);
    	}
		
//    	if(inimTencent(context)){
    		mTencent =  Tencent.createInstance(ShareConstants.AMAYA_QQ_ID,context);
    		ready2share2QQ(context,title,content, imageUrl, targetUrl, mShareStateListener);
    		
//    	}else{
//    		AuthorizingStateListener authorizingStateListener = new AuthorizingStateListener() {
//    			
//    			@Override
//    			public void onUserInfoComplete(SNSUserInfo user) {}
//    			
//    			@Override
//    			public void authrizeAction(int action, String info) {
//    				switch (action) {
//    				case AuthorizingStateListener.AUTHORIZE_SUCCESS:
//    					ready2share2QQ(context,title,content, imageUrl, targetUrl, mShareStateListener);
//    					break;
//    				case AuthorizingStateListener.AUTHORIZE_CANCEL:
//					case AuthorizingStateListener.AUTHORIZE_ERROR:
//						mShareStateListener.shareAction(ShareStateListener.SHARE_FAILED, "share failed by auth failed");
//						break;
//					default:
//						break;
//					}
//    			}
//
//    		};
//    	
//    		authQQ(context, authorizingStateListener, true);
//    	}
	}
	
	private void ready2share2QQ(final Activity context, String title,String content, String imageUrl, String targetUrl,final ShareStateListener mShareStateListener) {
//		if(mTencent.isSessionValid()){
			mQShare =  new QQShare(context,mTencent.getQQToken());
//		}
		
		if(mQShare==null){
			if(mShareStateListener!=null){
				mShareStateListener.shareAction(ShareStateListener.SHARE_FAILED, " mShare is null ",SNSShareManager.TENCENT_QQ_TYPE);
			}
			return;
		}
		
		final Bundle params = new Bundle();
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
    	params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, content);
		if(!TextUtils.isEmpty(imageUrl)&&imageUrl.startsWith("http://")){
			params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
		}
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				mQShare.shareToQQ(context, params,new IUiListener() {
					@Override
					public void onComplete(Object response) {
						L.v(TAG, "ready2share onComplete", response + "");
						if(mShareStateListener!=null){
							mShareStateListener.shareAction(ShareStateListener.SHARE_SUCCESS, " Qzone share success ",SNSShareManager.TENCENT_QQ_TYPE);
						}
					}
					
					@Override
					public void onCancel() {
						L.v(TAG, "ready2share onCancel", " Qzone share cancel ");
						if(mShareStateListener!=null){
							mShareStateListener.shareAction(ShareStateListener.SHARE_CANCEL, " Qzone share cancel ",SNSShareManager.TENCENT_QQ_TYPE);
						}
					}
					
					@Override
					public void onError(UiError uiError) {
						L.e(TAG, "ready2share onError", uiError.errorCode+":"+uiError.errorMessage+":"+uiError.errorDetail);
						if(mShareStateListener!=null){
							mShareStateListener.shareAction(ShareStateListener.SHARE_FAILED, uiError.toString(),SNSShareManager.TENCENT_QQ_TYPE);
						}
					}
					
				});
			}
			
		}).start();
		
	}
	
	
	private class QQAuthIUiListener implements IUiListener{

    	AuthorizingStateListener mAuthorizingStateListener;
    	SNSUserInfo snsUserInfo;
		
		public QQAuthIUiListener(AuthorizingStateListener mAuthorizingStateListener,SNSUserInfo snsUserInfo){
			this.snsUserInfo = snsUserInfo;
    		this.mAuthorizingStateListener = mAuthorizingStateListener;
		}
		
		@Override
		public void onComplete(Object response) {

            JSONObject json = (JSONObject)response;
            try {
            	//{"is_yellow_year_vip":"0","ret":0,"figureurl_qq_1":"http:\/\/q.qlogo.cn\/qqapp\/100460854\/4006284219847AC2805B32E911ABCA52\/40","figureurl_qq_2":"http:\/\/q.qlogo.cn\/qqapp\/100460854\/4006284219847AC2805B32E911ABCA52\/100","nickname":"-","yellow_vip_level":"0","is_lost":0,"msg":"","city":"","figureurl_1":"http:\/\/qzapp.qlogo.cn\/qzapp\/100460854\/4006284219847AC2805B32E911ABCA52\/50","vip":"0","level":"0","figureurl_2":"http:\/\/qzapp.qlogo.cn\/qzapp\/100460854\/4006284219847AC2805B32E911ABCA52\/100","province":"北京","is_yellow_vip":"0","gender":"男","figureurl":"http:\/\/qzapp.qlogo.cn\/qzapp\/100460854\/4006284219847AC2805B32E911ABCA52\/30"}
            	snsUserInfo.nickName = json.getString("nickname");
            	snsUserInfo.headPic = json.getString("figureurl_qq_2");
            	
            	// 获取用户信息成功
            	if(mAuthorizingStateListener!=null){
            		mAuthorizingStateListener.authrizeAction(AuthorizingStateListener.AUTHORIZE_SUCCESS, " QQ auth success ");
            		mAuthorizingStateListener.onUserInfoComplete(snsUserInfo);
            	}
            	
            } catch (JSONException e) {
                e.printStackTrace();
               // 解析失败
               	L.e(TAG, "QQAuthIUiListener onComplete", e.toString());
            	if(mAuthorizingStateListener!=null){
            		mAuthorizingStateListener.authrizeAction(AuthorizingStateListener.AUTHORIZE_ERROR, e.getMessage());
            	}
            }
		}

		@Override
		public void onError(UiError uiError) {
        	L.e(TAG, "QQAuthIUiListener onError", uiError.toString());
        	if(mAuthorizingStateListener!=null){
        		mAuthorizingStateListener.authrizeAction(AuthorizingStateListener.AUTHORIZE_ERROR, uiError.toString());
        	}
		}
		
		@Override
		public void onCancel() {
           	if(mAuthorizingStateListener!=null){
        		mAuthorizingStateListener.authrizeAction(AuthorizingStateListener.AUTHORIZE_CANCEL, " QQ auth cancel ");
        	}
		}
	}
	
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		L.v(TAG, "onActivityResult", "running");
		if(mTencent!=null){
			// Qzone分享 回调
//			mTencent.handleLoginData(
		}
		
	}
	
	
}
