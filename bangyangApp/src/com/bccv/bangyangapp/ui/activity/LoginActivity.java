package com.bccv.bangyangapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bccv.bangyangapp.R;
import com.bccv.bangyangapp.api.NetWorkAPI;
import com.bccv.bangyangapp.api.NetWorkAPI.UserType;
import com.bccv.bangyangapp.common.GlobalConstants;
import com.bccv.bangyangapp.model.LoginResBean;
import com.bccv.bangyangapp.model.UserInfo;
import com.bccv.bangyangapp.network.HttpCallback;
import com.bccv.bangyangapp.network.NetResBean;
import com.bccv.bangyangapp.sns.SNSLoginManager;
import com.bccv.bangyangapp.sns.SNSLoginManager.AuthorizingStateListener;
import com.bccv.bangyangapp.sns.UserInfoManager;
import com.bccv.bangyangapp.sns.bean.SNSUserInfo;
import com.bccv.bangyangapp.ui.view.BackGroundView;
import com.bccv.bangyangapp.utils.L;
import com.bccv.bangyangapp.utils.SystemUtil;

public class LoginActivity extends BaseActivity {

	private BackGroundView background_view;
	private View loadingView;
	private View login_back, login_weibo_loginBtn, login_QQ_loginBtn,
			login_weixin_loginBtn;

	private int[] background;
	private SNSLoginManager mSNSLoginManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		background = intent.getIntArrayExtra(BackGroundView.BACKGROUND_COLOR);
		setContentView(R.layout.activity_login);
		initView();
		initManager();
	}

	private void initView() {
		background_view = (BackGroundView) findViewById(R.id.background_view);
		loadingView = findViewById(R.id.login_loading);
		login_back = findViewById(R.id.login_back);
		login_weibo_loginBtn = findViewById(R.id.login_weibo_loginBtn);
		login_QQ_loginBtn = findViewById(R.id.login_QQ_loginBtn);
		login_weixin_loginBtn = findViewById(R.id.login_weixin_loginBtn);

		background_view.setGradient(background[0], background[1]);

		login_back.setOnClickListener(this);
		login_weibo_loginBtn.setOnClickListener(this);
		login_QQ_loginBtn.setOnClickListener(this);
		login_weixin_loginBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_back:
			finish();
			break;
		case R.id.login_weibo_loginBtn:
			// TODO 新浪微博
			if (SystemUtil.isNetOkWithToast(mContext)) {
				showWaiting(true);
				mSNSLoginManager.authorizeByType(this,
						SNSLoginManager.SINA_WEIBO_TYPE);
			}
			break;
		case R.id.login_QQ_loginBtn:
			// TODO QQ登录
			if (SystemUtil.isNetOkWithToast(mContext)) {
				showWaiting(true);
				mSNSLoginManager.authorizeByType(this,
						SNSLoginManager.TENCENT_QQ_TYPE);
			}

			break;
		case R.id.login_weixin_loginBtn:
			// TODO 微信登陆
			if (SystemUtil.isNetOkWithToast(mContext)) {
				showWaiting(true);
				mSNSLoginManager.authorizeByType(this,
						SNSLoginManager.TENCENT_WEIXIN_TYPE);
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
	 * 
	 * @param data
	 */
	private void saveUserInfo2Local(LoginResBean data, int snsType) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUser_name(data.getUser_name());
		userInfo.setUser_id(data.getUser_id());
		userInfo.setUser_icon(data.getUser_icon());
		userInfo.setUser_type(snsType);
		userInfo.setUser_token(data.getToken());
		userInfo.setUser_introduce(data.getIntroduce());
		UserInfoManager.saveUserInfo(userInfo);

	}

	private void showWaiting(boolean show) {

		if (show) {
			loadingView.setVisibility(View.VISIBLE);
		} else {
			loadingView.setVisibility(View.GONE);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		L.v(TAG, "onActivityResult", "resultCode : " + resultCode);

		if (requestCode != 0 && data != null) {
			SNSLoginManager.onActivityResult(this, requestCode, resultCode,
					data);
		} else {
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
				L.v(TAG, "authrizeAction", "AUTHORIZING");
				// showWaiting(false);
				// mWaitting.setVisibility(View.VISIBLE);
				break;
			case AuthorizingStateListener.AUTHORIZE_SUCCESS:
				L.v(TAG, "authrizeAction", "AUTHORIZE_SUCCESS");
				showWaiting(true);
				// Toast.makeText(LoginActivity.this, "授权成功",
				// Toast.LENGTH_SHORT).show();
				break;
			case AuthorizingStateListener.AUTHORIZE_CANCEL:
				L.v(TAG, "authrizeAction", "AUTHORIZE_CANCEL");
				showWaiting(false);
				break;
			case AuthorizingStateListener.AUTHORIZE_ERROR:
				L.v(TAG, "authrizeAction", "AUTHORIZE_ERROR");
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
			L.v(TAG, "onUserInfoComplete", "userInfo : " + user);
			showWaiting(true);

			// TODO 登录
			login(user);

		}
	}

	private void login(final SNSUserInfo user) {

		// TODO 服务器登录
		if (SystemUtil.isNetOkWithToast(this.getApplicationContext())) {
			
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
					user.thirdPlatformID, user_type, new HttpCallback() {
				
				@Override
				public void onResult(NetResBean response) {
					L.i(TAG, "onResult", response.toString());
					if (response.success
							&& response instanceof LoginResBean) {
						LoginResBean data = (LoginResBean) response;
						data.setToken(user.accessToken);
						saveUserInfo2Local(data, user.snsType);
						sendLoginSuccessBroadcast();
						L.i(TAG, "onResult", "userinfo : " + data);
						showShortToast("登录成功");
						finish();
					} else {
						showShortToast("登录失败");
						showWaiting(false);
					}
				}
				
				@Override
				public void onCancel() {
				}
				
				@Override
				public void onError(String errorMsg) {
					// TODO Auto-generated method stub
					showShortToast("登录失败");
					showWaiting(false);
				}
			});
			
		}

	}

	private void sendLoginSuccessBroadcast(){
	      Intent intent = new Intent();
          intent.setAction(GlobalConstants.USER_INFO_CHANGE_BROADCAST);
          intent.putExtra(GlobalConstants.USER_INFO_CHANGE_TYPE, GlobalConstants.LOGIN_SUCCESS);
          sendBroadcast(intent);
	}
	
	@Override
	protected void onDestroy() {
		Intent intent = new Intent();
		intent.putExtra("result", "Is user's data changed?");
		if (UserInfoManager.isLogin()) {
			this.setResult(101, intent);
		} else {
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
		} else {
			this.setResult(102, intent);
		}
		super.finish();
	}

}
