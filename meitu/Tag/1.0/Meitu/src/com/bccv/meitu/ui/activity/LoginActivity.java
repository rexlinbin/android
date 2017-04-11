package com.bccv.meitu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bccv.meitu.R;
import com.bccv.meitu.api.NetWorkAPI;
import com.bccv.meitu.api.NetWorkAPI.UserType;
import com.bccv.meitu.model.LoginResBean;
import com.bccv.meitu.model.UserInfo;
import com.bccv.meitu.network.HttpCallback;
import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.sns.SNSLoginManager;
import com.bccv.meitu.sns.SNSLoginManager.AuthorizingStateListener;
import com.bccv.meitu.sns.UserInfoManager;
import com.bccv.meitu.sns.bean.SNSUserInfo;
import com.bccv.meitu.utils.Logger;
import com.bccv.meitu.utils.SystemUtil;

public class LoginActivity extends BaseActivity {

	private View back;
	private Button sina;
	private Button qq;
	private Button wechat;
	
	private View waitting_layout;
	
	private SNSLoginManager mSNSLoginManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
		initManager();
	}
	
	private void initView(){
		back = findViewById(R.id.login_back);
		sina = (Button) findViewById(R.id.login_weibo_loginBtn);
		qq = (Button) findViewById(R.id.login_QQ_loginBtn);
		wechat = (Button) findViewById(R.id.login_weixin_loginBtn);
		waitting_layout = findViewById(R.id.waitting_layout);
		
		back.setOnClickListener(this);
		sina.setOnClickListener(this);
		qq.setOnClickListener(this);
		wechat.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_back:
			finish();
			break;
		case R.id.login_weibo_loginBtn:
			//TODO  新浪微博
			if (SystemUtil.isNetOkWithToast(mContext)) {
				showWaiting(true);
				mSNSLoginManager
						.authorizeByType(this,SNSLoginManager.SINA_WEIBO_TYPE);
			}
			break;
		case R.id.login_QQ_loginBtn:
			//TODO  QQ
			if (SystemUtil.isNetOkWithToast(mContext)) {
				showWaiting(true);
				mSNSLoginManager
						.authorizeByType(this,SNSLoginManager.TENCENT_QQ_TYPE);
			}
			break;
		case R.id.login_weixin_loginBtn:
			//TODO  微信
			if (SystemUtil.isNetOkWithToast(mContext)) {
				showWaiting(true);
				mSNSLoginManager
				.authorizeByType(this,SNSLoginManager.TENCENT_WEIXIN_TYPE);
			}
			break;

		default:
			break;
		}
		
	}
	
	/**
	 * 初始化SNSManager
	 */
	private void initManager() {
		mSNSLoginManager = SNSLoginManager.getInstance();
		mSNSLoginManager
				.setAuthiringStateListener(new MyAuthorizingStateListener());
	}
	
	/**
	 * 保存用户信息到本地
	 * @param data
	 */
	private void saveUserInfo2Local(LoginResBean data,int snsType){
		UserInfo userInfo = new UserInfo();
		userInfo.setUser_name(data.getUser_name());
		userInfo.setUser_id(data.getUser_id());
		userInfo.setUser_icon(data.getUser_icon());
		userInfo.setUser_type(snsType);
		UserInfoManager.saveUserInfo(userInfo);
		
	}
	
	private void showWaiting(boolean show){
		
		if(show){
			waitting_layout.setVisibility(View.VISIBLE);
		}else{
			waitting_layout.setVisibility(View.GONE);
		}
	}
	
	
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        Logger.v(TAG, "onActivityResult", "resultCode : " + resultCode);
        
        if (requestCode != 0 && data != null){
        	SNSLoginManager.onActivityResult(this, requestCode, resultCode, data);
        }else{
            showWaiting(false);
        }
    }
	
	/**
	 * SNS授权监听类
	 * 
	 * @author user 用户信息
	 * 
	 */
	private class MyAuthorizingStateListener implements
			AuthorizingStateListener {

		@Override
		public void authrizeAction(int action, String info) {

			switch (action) {
			case AuthorizingStateListener.AUTHORIZING:
				Logger.v(TAG, "authrizeAction", "AUTHORIZING");
//				showWaiting(false);
//				mWaitting.setVisibility(View.VISIBLE);
				break;
			case AuthorizingStateListener.AUTHORIZE_SUCCESS:
				Logger.v(TAG, "authrizeAction", "AUTHORIZE_SUCCESS");
				showWaiting(true);
				// Toast.makeText(LoginActivity.this, "授权成功",
				// Toast.LENGTH_SHORT).show();
				break;
			case AuthorizingStateListener.AUTHORIZE_CANCEL:
				Logger.v(TAG, "authrizeAction", "AUTHORIZE_CANCEL");
				showWaiting(false);
				break;
			case AuthorizingStateListener.AUTHORIZE_ERROR:
				Logger.v(TAG, "authrizeAction", "AUTHORIZE_ERROR");
				showWaiting(false);
				Toast.makeText(LoginActivity.this, info, Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		}

		@Override
		public void onUserInfoComplete(SNSUserInfo user) {
			// Toast.makeText(LoginActivity.this, "获取了userInfo...",
			// Toast.LENGTH_SHORT).show();
			Logger.v(TAG, "onUserInfoComplete", "userInfo : " + user);
			showWaiting(true);
			
			//TODO 登录
			login(user);
			
		}
	}
	
	
	private void login(final SNSUserInfo user){
		
		UserType user_type = UserType.UNKNOW;
		switch (user.snsType) {
		case SNSLoginManager.SINA_WEIBO_TYPE:
			user_type = UserType.SINA;
			break;
		case SNSLoginManager.TENCENT_QQ_TYPE:
			user_type = UserType.QQ;
			break;
		case SNSLoginManager.TENCENT_WEIXIN_TYPE:
			user_type = UserType.WEIXIN;
			break;
		default:
			break;
			
		}
	
		NetWorkAPI.login(mContext, user.nickName, user.headPic,
				user.thirdPlatformID, user.accessToken, user_type, new HttpCallback() {
			
			@Override
			public void onResult(NetResBean response) {
				
				if(response.success && response instanceof LoginResBean){
					LoginResBean data = (LoginResBean)response;
					saveUserInfo2Local(data,user.snsType);
					showShortToast("登录成功");
					finish();
				}else{
					showShortToast("登录失败");
				}
				showWaiting(false);
			}
			
			@Override
			public void onError(String errorMsg) {
				showShortToast("登录失败");
				showWaiting(false);
			}
			
			@Override
			public void onCancel() {}
		});
	}
	
	@Override
	protected void onDestroy() {
		Intent intent = new Intent();
		intent.putExtra("result", "Is user's data changed?");
		if (UserInfoManager.isLogin()) {
			this.setResult(101, intent);
		}else{
			this.setResult(102, intent);
		}
		super.onDestroy();
	}
	
	@Override
	public void finish() {
		Intent intent = new Intent();
		intent.putExtra("result", "Is user's data changed?");
		if (UserInfoManager.isLogin()) {
			this.setResult(101, intent);
		}else{
			this.setResult(102, intent);
		}
		super.finish();
	}
}
