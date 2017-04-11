package com.bccv.bangyangapp.sns.sina;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;

import com.bccv.bangyangapp.sns.SNSLoginManager;
import com.bccv.bangyangapp.sns.SNSLoginManager.AuthorizingStateListener;
import com.bccv.bangyangapp.sns.SNSShareManager;
import com.bccv.bangyangapp.sns.SNSShareManager.ShareStateListener;
import com.bccv.bangyangapp.sns.ShareConstants;
import com.bccv.bangyangapp.sns.bean.SNSUserInfo;
import com.bccv.bangyangapp.sns.utils.SNSTokenKeeper;
import com.bccv.bangyangapp.utils.L;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

public class SinaShare {

	private static final String TAG = SinaShare.class.getSimpleName();
	
    /**
     * Sina微博相关实例
     */
    private WeiboAuth.AuthInfo amayaAuthInfo;
    private SsoHandler mSsoHandler;
    private SinaAPI sinaApi;
    
    public static SinaShare mSinaShare;
    
    private SinaShare(){}
    
	public static SinaShare getInstance(Context context){
		if(mSinaShare==null){
			mSinaShare = new SinaShare();
		}
		return mSinaShare;
	}
    
    
    private boolean initSinaAPI(Context context) {
        Oauth2AccessToken token = SNSTokenKeeper.readSinaToken(context);
        boolean authed = token.isSessionValid();
        if(authed){
            sinaApi = new SinaAPI(token);
        }
        return authed;
    }
    
    
    /**
     * 新浪授权
     * @param mContext
     * @param mAuthorizingStateListener 授权监听	
     */
    public void authSinaWeibo(Context mContext,AuthorizingStateListener mAuthorizingStateListener){
    	authSinaWeibo(mContext, mAuthorizingStateListener, false);
    }
    
    
    /**
     * 发布消息到新浪微博
     * 
     * @param mContext
     * @param content	内容
     * @param imageUrl	图片url
     * @param bitmap	图片bitmap
     * @param mShareStateListener	监听
     */
    public void share2sina(final Activity context,final String content,final String imageUrl,final Bitmap bitmap,final ShareStateListener mShareStateListener){
    	
    	if(mShareStateListener==null){
    		throw new IllegalArgumentException(" ShareStateListener can't be null !!!");
    	}else{
    		mShareStateListener.shareAction(ShareStateListener.SHARING, " sina sharing ! ",SNSShareManager.SINA_WEIBO_TYPE);
    	}
    	
    	if(initSinaAPI(context)){
    		ready2share(context, content, imageUrl, bitmap, mShareStateListener);
    	}else{
    		AuthorizingStateListener authorizingStateListener = new AuthorizingStateListener() {
    			
    			@Override
    			public void onUserInfoComplete(SNSUserInfo user) {}
    			
    			@Override
    			public void authrizeAction(int action, String info) {
    				switch (action) {
    				case AuthorizingStateListener.AUTHORIZE_SUCCESS:
    					ready2share(context, content, imageUrl, bitmap, mShareStateListener);
    					break;
    				case AuthorizingStateListener.AUTHORIZE_CANCEL:
					case AuthorizingStateListener.AUTHORIZE_ERROR:
						mShareStateListener.shareAction(ShareStateListener.SHARE_FAILED, "share failed by auth failed",SNSShareManager.SINA_WEIBO_TYPE);
						break;
					default:
						break;
					}
    			}
    		};
    		authSinaWeibo(context, authorizingStateListener, true);
    	}
    }
    
    private void ready2share(Context context,String content, String imageUrl, Bitmap bitmap,ShareStateListener mShareStateListener){
    	
    	L.v(TAG, "ready2share", " -- ready2share start !!! --" );
    	SinaShareRequestListener sinaShareRequestListener = new SinaShareRequestListener(mShareStateListener);
    	
    	L.v(TAG, "ready2share", "content : " + content + " imageUrl : " + imageUrl + " bitmap == null : " + String.valueOf(bitmap == null) );
    	
    	if(!TextUtils.isEmpty(imageUrl)&&imageUrl.startsWith("http://")){
    		sinaApi.uploadUrlText(content, imageUrl, null, null, null, sinaShareRequestListener);
    		
//    		ImageLoader.getInstance().loadImageSync("");
    		
    	}else if(bitmap!=null){
    		sinaApi.upload(content,bitmap,null,null,sinaShareRequestListener);
    	}else {
    		sinaApi.update(content,null,null,sinaShareRequestListener);
    	}
    }
    
    /**
     * 新浪授权
     * @param mContext
     * @param mAuthorizingStateListener	授权监听	
     * @param fromShare	是否来自分享授权
     */
    private void authSinaWeibo(final Context mContext,final AuthorizingStateListener mAuthorizingStateListener,final boolean fromShare) {
       
		amayaAuthInfo = new WeiboAuth.AuthInfo(mContext, ShareConstants.AMAYA_SINA_KEY, ShareConstants.AMAYA_SINA_REDIRECTURL,ShareConstants.AMAYA_SINA_SCOPE);
        WeiboAuth weiboAuth = new WeiboAuth(mContext, amayaAuthInfo);
        mSsoHandler = new SsoHandler((Activity)mContext, weiboAuth);
    	
        if (mSsoHandler != null) {
            mSsoHandler.authorize(new WeiboAuthListener(){
                @Override
                public void onComplete(Bundle bundle) {
                    Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(bundle);
                    if (accessToken != null && accessToken.isSessionValid()) {
                        sinaApi = new SinaAPI(accessToken);
                        UsersAPI mUsersAPI = new UsersAPI(accessToken);
                        L.v(TAG, "authSinaWeibo",  "fromShare : " + fromShare +" bundle : " + bundle.toString());
                        
                        if(fromShare){
                        	SNSTokenKeeper.saveSinaToken(mContext, accessToken);
                        	if(mAuthorizingStateListener!=null) 
                        		mAuthorizingStateListener.authrizeAction(AuthorizingStateListener.AUTHORIZE_SUCCESS, "sina auth success for share");
                        	return;
                        }
                        
                        if(mAuthorizingStateListener!=null) 
                        	mAuthorizingStateListener.authrizeAction(AuthorizingStateListener.AUTHORIZE_SUCCESS, " QQ auth success , waiting get userinfo ");
                        
                        SNSUserInfo snsUserInfo = new SNSUserInfo();
                        snsUserInfo.snsType = SNSLoginManager.SINA_WEIBO_TYPE;
                    	snsUserInfo.expiresTime = String.valueOf(accessToken.getExpiresTime());
                    	snsUserInfo.accessToken = accessToken.getToken();
                    	snsUserInfo.thirdPlatformID = accessToken.getUid();
                        
                    	SinaAuthRequestListener mListener = new SinaAuthRequestListener(mAuthorizingStateListener, snsUserInfo);
                        mUsersAPI.show(accessToken.getUid(), mListener);
                        
                    }else{
                    	if(mAuthorizingStateListener != null) 
                    		mAuthorizingStateListener.authrizeAction(AuthorizingStateListener.AUTHORIZE_ERROR, " accessToken is null or is not SessionValid ");
                    }
                }

                @Override
                public void onWeiboException(WeiboException e) {
                	L.v(TAG, "onWeiboException", e.getMessage());
                    if(mAuthorizingStateListener != null) 
                    	mAuthorizingStateListener.authrizeAction(AuthorizingStateListener.AUTHORIZE_ERROR, e.getMessage());
                    	
                }

                @Override
                public void onCancel() {
                    if(mAuthorizingStateListener != null) 
                    mAuthorizingStateListener.authrizeAction(AuthorizingStateListener.AUTHORIZE_CANCEL, " sina auth cancel ");
                }
            });
            
        	if(mAuthorizingStateListener==null){
        		throw new IllegalArgumentException(" AuthorizingStateListener can't be null !!!");
        	}else{
        		mAuthorizingStateListener.authrizeAction(AuthorizingStateListener.AUTHORIZING, " sina authorzing ! ");
        	}
            
        } else {
            L.e(TAG, "authSinaWeibo", "Please setWeiboAuthInfo(...) for first");
        }
    }
	
    private class SinaAuthRequestListener implements RequestListener{

    	AuthorizingStateListener mAuthorizingStateListener;
    	SNSUserInfo snsUserInfo;
    	
    	public SinaAuthRequestListener (AuthorizingStateListener mAuthorizingStateListener,SNSUserInfo snsUserInfo){
    		
    		this.snsUserInfo = snsUserInfo;
    		this.mAuthorizingStateListener = mAuthorizingStateListener;
    		
    	}
    	
		@Override
		public void onComplete(String response) {
			L.v(TAG, "SinaRequestListener onComplete", response+"");
	        if (!TextUtils.isEmpty(response)) {
                // 调用 User#parse 将JSON串解析成User对象
                User user = User.parse(response);
                if (user != null) {
                	snsUserInfo.nickName = user.screen_name;
                	snsUserInfo.headPic = user.profile_image_url;
                } 
            }
            if(mAuthorizingStateListener != null) {
            	if(TextUtils.isEmpty(snsUserInfo.nickName)){
            		snsUserInfo.nickName = "新浪用户";
            	}
            	mAuthorizingStateListener.authrizeAction(AuthorizingStateListener.AUTHORIZE_SUCCESS, response);
            	mAuthorizingStateListener.onUserInfoComplete(snsUserInfo);
            }
		}

		@Override
		public void onWeiboException(WeiboException e) {
			L.e(TAG, "SinaRequestListener onWeiboException", e.getMessage());
            if(mAuthorizingStateListener != null) {
            	if(TextUtils.isEmpty(snsUserInfo.nickName)){
            		snsUserInfo.nickName = "新浪用户";
            	}
            	mAuthorizingStateListener.onUserInfoComplete(snsUserInfo);
            	mAuthorizingStateListener.authrizeAction(AuthorizingStateListener.AUTHORIZE_SUCCESS, e.getMessage());
            }
		}
	}
    	
    private class SinaShareRequestListener implements RequestListener{
    	
    	ShareStateListener mShareStateListener;
    	
    	public SinaShareRequestListener(ShareStateListener shareStateListener){
    		this.mShareStateListener = shareStateListener;
    	}

		@Override
		public void onComplete(String response) {
			L.v(TAG, "SinaShareRequestListener onComplete", response);
			if(mShareStateListener!=null){
				mShareStateListener.shareAction(ShareStateListener.SHARE_SUCCESS, " sina share success ",SNSShareManager.SINA_WEIBO_TYPE);
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
			L.e(TAG, "SinaShareRequestListener onComplete", e.getMessage());
			if(mShareStateListener!=null){
				mShareStateListener.shareAction(ShareStateListener.SHARE_FAILED, "分享失败",SNSShareManager.SINA_WEIBO_TYPE);
			}
		}
    }

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		L.v(TAG, "onActivityResult", "running");
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
	}
}
