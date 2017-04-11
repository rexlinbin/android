package com.bccv.boxcomic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.api.UserApi;
import com.bccv.boxcomic.modal.User;
import com.bccv.boxcomic.sns.SNSLoginManager;
import com.bccv.boxcomic.sns.SNSLoginManager.AuthorizingStateListener;
import com.bccv.boxcomic.sns.UserInfoManager;
import com.bccv.boxcomic.sns.UserInfoManager.UserType;
import com.bccv.boxcomic.sns.bean.SNSUserInfo;
import com.bccv.boxcomic.tool.BaseActivity;
import com.bccv.boxcomic.tool.Callback;
import com.bccv.boxcomic.tool.Logger;

public class LoginActivity extends BaseActivity implements OnClickListener {
	private View back;
	private Button sina;
	private Button LoginBtn;
	private Button qq;
	private Button wechat;
	private SNSLoginManager mSNSLoginManager;
	public UserType user_type;
	private EditText nameEdit, passWrodEdit;
	private String name, passWrod;
	private TextView toRegister;
	private ImageView nameClear, ClosePass;
	private boolean isOpen = true;
	private String type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		type = getIntent().getStringExtra("type");
		initView();
		initManager();
	}

	/**
	 * 初始化SNSManager
	 */
	private void initManager() {
		// TODO Auto-generated method stub
		mSNSLoginManager = SNSLoginManager.getInstance();
		mSNSLoginManager
				.setAuthiringStateListener(new MyAuthorizingStateListener());
	}

	private void initView() {
		// TODO Auto-generated method stub
		back = findViewById(R.id.login_back);
		sina = (Button) findViewById(R.id.login_weibo_loginBtn);
		qq = (Button) findViewById(R.id.login_QQ_loginBtn);
		wechat = (Button) findViewById(R.id.login_weixin_loginBtn);
		nameEdit = (EditText) findViewById(R.id.login_name);
		passWrodEdit = (EditText) findViewById(R.id.login_password);

		nameClear = (ImageView) findViewById(R.id.login_name_clear);
		ClosePass = (ImageView) findViewById(R.id.login_pass_close);

		LoginBtn = (Button) findViewById(R.id.login_btn);
		toRegister = (TextView) findViewById(R.id.login_regi_text);
		toRegister.setOnClickListener(this);
		LoginBtn.setOnClickListener(this);

		nameClear.setOnClickListener(this);
		ClosePass.setOnClickListener(this);

		back.setOnClickListener(this);
		sina.setOnClickListener(this);
		qq.setOnClickListener(this);
		wechat.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login_back:
			finish();
			break;
		case R.id.login_weibo_loginBtn:

			mSNSLoginManager.authorizeByType(this,
					SNSLoginManager.SINA_WEIBO_TYPE);

			break;
		case R.id.login_QQ_loginBtn:
			mSNSLoginManager.authorizeByType(this,
					SNSLoginManager.TENCENT_QQ_TYPE);

			break;

		case R.id.login_weixin_loginBtn:
			mSNSLoginManager.authorizeByType(this,
					SNSLoginManager.TENCENT_WEIXIN_TYPE);
			break;

		case R.id.login_btn:
			name = nameEdit.getText().toString().trim();
			passWrod = passWrodEdit.getText().toString().trim();
			LoginOwn(name, passWrod);

			break;
		case R.id.login_regi_text:

			Intent aIntent = new Intent(LoginActivity.this,
					RegisterActivity.class);
			aIntent.putExtra("type", type);
			startActivity(aIntent);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

			finishActivityWithAnim();

			break;
		case R.id.login_pass_close:
			if (isOpen) {

				ClosePass.setBackgroundResource(R.drawable.open_eye);
				passWrodEdit
						.setTransformationMethod(HideReturnsTransformationMethod
								.getInstance());

				isOpen = false;
			} else {
				passWrodEdit
						.setTransformationMethod(PasswordTransformationMethod
								.getInstance());

				ClosePass.setBackgroundResource(R.drawable.close_eye);
				isOpen = true;
			}

			break;
		case R.id.login_name_clear:

			nameEdit.setText("");
			break;

		default:
			break;
		}

	}

	/**
	 * 保存用户信息到本地
	 * 
	 * @param data
	 */
	private void saveUserInfo2Local(User data, int snsType) {
		User userInfo = new User();
		userInfo.setUser_name(data.getUser_name());
		userInfo.setUser_id(data.getUser_id());
		userInfo.setUser_icon(data.getUser_icon());
		userInfo.setUser_type(snsType);

		UserInfoManager.saveUserInfo(userInfo);

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Logger.v("onActivityResult", "resultCode : " + resultCode);

		if (requestCode != 0 && data != null) {
			SNSLoginManager.onActivityResult(this, requestCode, resultCode,
					data);
		} else {
			// showWaiting(false);
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
				Logger.v("authrizeAction", "AUTHORIZING");
				// showWaiting(false);
				// mWaitting.setVisibility(View.VISIBLE);
				break;
			case AuthorizingStateListener.AUTHORIZE_SUCCESS:
				Logger.v("authrizeAction", "AUTHORIZE_SUCCESS");
				// showWaiting(true);
				Toast.makeText(LoginActivity.this, "授权成功", Toast.LENGTH_SHORT)
						.show();
				break;
			case AuthorizingStateListener.AUTHORIZE_CANCEL:
				Logger.v("authrizeAction", "AUTHORIZE_CANCEL");
				// showWaiting(false);
				break;
			case AuthorizingStateListener.AUTHORIZE_ERROR:
				Logger.v("authrizeAction", "AUTHORIZE_ERROR");
				// showWaiting(false);
				Toast.makeText(LoginActivity.this, info, Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		}

		@Override
		public void onUserInfoComplete(SNSUserInfo user) {
			// TODO Auto-generated method stub

			Logger.v("user", user.toString());
			login(user);
		}

	}

	private void login(final SNSUserInfo user) {
		// TODO Auto-generated method stub
		user_type = UserType.UNKNOW;
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

		Callback localCallback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				Logger.v("authrizeAction", UserInfoManager.isLogin() + "");
				if (UserInfoManager.isLogin()) {
					if (type.equals("UserCenter")) {

						startActivityWithSlideAnimation(UserCenterActivity.class);
					} else if (type.equals("Comment")) {
						Intent aIntent = new Intent(LoginActivity.this,
								CommentActivity.class);

						aIntent.putExtra("isUser",
								getIntent().getBooleanExtra("isUser", false));

						startActivity(aIntent);
						overridePendingTransition(R.anim.in_from_right,
								R.anim.out_to_left);
					}
					finishActivityWithAnim();
				}

			}
		};

		new DataAsyncTask(localCallback, false) {

			protected String doInBackground(String... params) {

				UserApi userApi = new UserApi();

				User userinfo = userApi.login(user_type, user.nickName,
						user.headPic, user.thirdPlatformID, user.accessToken);

				saveUserInfo2Local(userinfo, user.snsType);

				return null;
			};

		}.executeProxy("");

	}

	private void LoginOwn(final String user_name, final String user_passWord) {

		Callback localCallback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				Logger.v("authrizeAction", UserInfoManager.isLogin() + "");

				// if(result.equals("false")){
				// Toast.makeText(LoginActivity.this, "登录失败",
				// Toast.LENGTH_SHORT).show();
				// }

				if (UserInfoManager.isLogin()) {
					if (type.equals("UserCenter")) {

						startActivityWithSlideAnimation(UserCenterActivity.class);
					} else if (type.equals("Comment")) {
//						Intent aIntent = new Intent(LoginActivity.this,
//								CommentActivity.class);
//
//						aIntent.putExtra("isUser",
//								getIntent().getBooleanExtra("isUser", false));
//
//						startActivity(aIntent);
//						overridePendingTransition(R.anim.in_from_right,
//								R.anim.out_to_left);

					}
					finishActivityWithAnim();
				} else {
					Toast.makeText(LoginActivity.this, "登录失败",
							Toast.LENGTH_SHORT).show();
				}

			}
		};

		new DataAsyncTask(localCallback, true) {

			protected String doInBackground(String... params) {

				UserApi userApi = new UserApi();

				User userinfo1 = userApi.loginOWn(user_name, user_passWord);

				if (userinfo1 != null) {
					saveUserInfo2Local(userinfo1, 0);
					return "true";
				}
				return "false";

			};

		}.executeProxy("");

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

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

}