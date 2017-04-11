package com.bccv.strategy.wxapi;

import java.io.InputStream;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.bccv.strategy.sns.SNSLoginManager;
import com.bccv.strategy.sns.SNSLoginManager.AuthorizingStateListener;
import com.bccv.strategy.sns.bean.SNSUserInfo;
import com.bccv.strategy.sns.tencent.weixin.WeiXinShareUtil;
import com.bccv.strategy.sns.utils.ApiClient;
import com.bccv.strategy.sns.utils.EmojiFilter;
import com.bccv.strategy.utils.L;
import com.bccv.strategy.R;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendAuth.Resp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 提供给微信回调的Activity
 * @author Administrator
 *
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler{

	private static final String TAG = "WXEntryActivity";
	
    private IWXAPI api;// IWXAPI 是第三方app和微信通信的openapi接口
    
    @SuppressWarnings("unused")
	private TextView mTitle,mContent,mButton;
    
    public static boolean isLogin = false;
    
	
    private WeiXinShareUtil mWeiXinShareUtil;
    private AuthorizingStateListener mAuthorizingStateListener;
    private Handler mHandler;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		L.v(TAG, "onCreate", "-------->");
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if(isLogin){
			//TODO 加载微信登陆的界面
			L.v(TAG, "onCreate", " weixin login  -------->");
			setContentView(R.layout.activity_weixin_page_login);
			iniHandler();
		}else{
			L.v(TAG, "onCreate", " weixin share  -------->");
			setContentView(R.layout.activity_weixin_page);
		}
		iniView();
		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(this, WeiXinShareUtil.APP_ID);
		api.handleIntent(getIntent(), this);
	}
	
	private void iniView(){
		
		if(!isLogin){
			mTitle = (TextView) findViewById(R.id.wx_title_tv);
			mContent = (TextView) findViewById(R.id.wx_content_tv);
			mButton = (TextView) findViewById(R.id.wx_button_tv);
			
			mButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}else{

		}
	}	
	
	@SuppressLint("HandlerLeak")
	private void iniHandler(){
		 mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case AuthorizingStateListener.AUTHORIZE_SUCCESS:
					if(msg.obj instanceof SNSUserInfo){
						if(mAuthorizingStateListener!=null){
							mAuthorizingStateListener.authrizeAction(AuthorizingStateListener.AUTHORIZE_SUCCESS, "授权成功");
							mAuthorizingStateListener.onUserInfoComplete((SNSUserInfo)msg.obj);
						}
					}else{
						if(mAuthorizingStateListener!=null){
							mAuthorizingStateListener.authrizeAction(AuthorizingStateListener.AUTHORIZE_ERROR, "授权失败");
						}
					}
					finish();
					break;
				case AuthorizingStateListener.AUTHORIZE_ERROR:
					if(mAuthorizingStateListener!=null){
						mAuthorizingStateListener.authrizeAction(AuthorizingStateListener.AUTHORIZE_ERROR, "授权失败");
					}
					finish();
					break;
				default:
					break;
				}
			}
			
		};
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		L.v(TAG, "onNewIntent", ">>>>>>");
		setIntent(intent);
        api.handleIntent(intent, this);
	}
	
	
	//第三方请求微信后,微信回调第三方
	@Override
	public void onResp(BaseResp resp) {
        String result = "";
		
		if(resp.getType()==ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX){
			//TODO 分享到微信返回
			L.v(TAG, "onResp", " weixin share !  resp errorCode : " + resp.errCode + " openId : " + resp.openId);
			
			switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				result = "分享成功";
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				result = "分享已取消";
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				result = "分享失败";
				break;
			default:
				result = "分享未知";
				break;
			}
			mContent.setText(result);
			
		}else if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH && resp instanceof SendAuth.Resp){
			//TODO 请求授权 返回
			L.v(TAG, "onResp", " weixin login !  resp errorCode : "+resp.errCode + " openId : " + resp.openId);
			
			SendAuth.Resp authResp = (Resp) resp;
			
			mWeiXinShareUtil = WeiXinShareUtil.getInstance(getApplicationContext());
			mAuthorizingStateListener = mWeiXinShareUtil.mAuthorizingStateListener;
			switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				getUserInfo(authResp);
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				if(mAuthorizingStateListener!=null){
					mAuthorizingStateListener.authrizeAction(AuthorizingStateListener.AUTHORIZE_CANCEL, "cancel");
				}
				finish();
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				if(mAuthorizingStateListener!=null){
					mAuthorizingStateListener.authrizeAction(AuthorizingStateListener.AUTHORIZE_ERROR, "授权失败");
				}
				finish();
				break;
			default:
				if(mAuthorizingStateListener!=null){
					mAuthorizingStateListener.authrizeAction(AuthorizingStateListener.AUTHORIZE_ERROR, "授权失败");
				}
				finish();
				break;
			}
			
		}
	}

	
	/**
	 * 获取用户信息
	 * @param resp
	 */
	private void getUserInfo(final SendAuth.Resp resp){
		L.v(TAG, "getUserInfo", " getUserInfo start !   resp.state : " + resp.state + " resp.code : " + resp.code);
		
		if(!"bangyang_wechat_login".equals(resp.state)||TextUtils.isEmpty(resp.code)){
			if(mAuthorizingStateListener!=null){
				mAuthorizingStateListener.authrizeAction(AuthorizingStateListener.AUTHORIZE_ERROR, "授权失败");
			}
			finish();
		}
		
		//TODO 获取用户信息
		
		new Thread(){
			@Override
			public void run() {
				//TODO 
				SNSUserInfo userInfo = new SNSUserInfo();
				userInfo.snsType = SNSLoginManager.TENCENT_WEIXIN_TYPE;
				userInfo = getToken(resp.code,userInfo);
				
				if(TextUtils.isEmpty(userInfo.accessToken)||TextUtils.isEmpty(userInfo.thirdPlatformID)){
					//TODO 授权失败
					if(mHandler==null){
						iniHandler();
					}
					mHandler.sendEmptyMessage(AuthorizingStateListener.AUTHORIZE_ERROR);
					return;
				}
				
				userInfo = getUserInfo(userInfo);
				if(mHandler==null){
					iniHandler();
				}
				mHandler.obtainMessage(AuthorizingStateListener.AUTHORIZE_SUCCESS, userInfo).sendToTarget();
				
			};
		}.start();
	}
	
	
	/**
	 * 获取token 和 openid
	 * @param code
	 * @return
	 */
	private SNSUserInfo getToken(String code,SNSUserInfo userInfo){
		try {
			HashMap<String, Object> params = new HashMap<String,Object>();
			params.put("appid", WeiXinShareUtil.APP_ID);
			params.put("secret", WeiXinShareUtil.APP_SECRET);
			params.put("code", code);
			params.put("grant_type", "authorization_code");
			InputStream responseIs = ApiClient.http_get("https://api.weixin.qq.com/sns/oauth2/access_token?", params);
			
			if(responseIs==null){
				return userInfo;
			}
			String data = ApiClient.IS2String(responseIs);
			L.v(TAG, "SNSUserInfo", data);
			JSONObject jsonObject = new JSONObject(data);
			
			userInfo.accessToken = jsonObject.getString("access_token");
			userInfo.thirdPlatformID = jsonObject.getString("openid");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return userInfo;
	}
	
	/**
	 * 获取用户名 头像
	 * @param userInfo
	 * @return
	 */
	private SNSUserInfo getUserInfo(SNSUserInfo userInfo){
		
		try {
			HashMap<String, Object> params = new HashMap<String,Object>();
			params.put("access_token", userInfo.accessToken);
			params.put("openid", userInfo.thirdPlatformID);
			InputStream responseIs = ApiClient.http_get("https://api.weixin.qq.com/sns/userinfo?", params);
			if(responseIs==null){
				return userInfo;
			}
			String data = ApiClient.IS2String(responseIs);
			L.v(TAG, "getUserInfo", data);
			JSONObject jsonObject = new JSONObject(data);
			userInfo.nickName = jsonObject.getString("nickname");
			userInfo.nickName = EmojiFilter.parseEmoji(userInfo.nickName);
			userInfo.headPic = jsonObject.getString("headimgurl");
			userInfo.unionid = jsonObject.getString("unionid");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return userInfo;
	}
	
	
	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {
		L.v(TAG, "onReq", " req.getType() : "+req.getType());
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
//          WeiXinShare.getInstance().setmBundle(getIntent().getExtras());
//			
//			Intent intent = new Intent();
//			intent.setClass(this, FoneExplorer.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//			intent.setAction(Constant.FONE_ACTION_WEIXIN);
//			startActivity(intent);
			finish();
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		isLogin = false;
	}
	
}
