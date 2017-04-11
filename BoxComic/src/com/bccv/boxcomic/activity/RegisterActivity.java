package com.bccv.boxcomic.activity;

import android.os.Bundle;
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
import com.bccv.boxcomic.sns.UserInfoManager;
import com.bccv.boxcomic.tool.BaseActivity;
import com.bccv.boxcomic.tool.Callback;
import com.bccv.boxcomic.tool.Logger;

public class RegisterActivity extends BaseActivity implements OnClickListener {

	private EditText regName, regPassword, regAgainPassword;
	private String name, passWord, AgainPass;
	private ImageView reginBack, regCheck;
	private Button registerBtn;
	private boolean isCheck = true;
	private TextView regText;
	private String type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		type = getIntent().getStringExtra("type");
		intView();
	}

	private void intView() {
		// TODO Auto-generated method stub
		regName = (EditText) findViewById(R.id.reg_name);
		regPassword = (EditText) findViewById(R.id.reg_passWord);
		regAgainPassword = (EditText) findViewById(R.id.reg_aginPassWord);
		reginBack = (ImageView) findViewById(R.id.reg_back);
		regCheck = (ImageView) findViewById(R.id.reg_check);
		reginBack.setOnClickListener(this);

		registerBtn = (Button) findViewById(R.id.reg_btn);
		registerBtn.setOnClickListener(this);

		regCheck = (ImageView) findViewById(R.id.reg_check);
		regCheck.setBackgroundResource(R.drawable.check);
		regCheck.setOnClickListener(this);
		regText = (TextView) findViewById(R.id.reg_text);
		regText.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.reg_back:
			finishActivityWithAnim();
			break;
		case R.id.reg_btn:

			name = regName.getText().toString().trim();
			passWord = regPassword.getText().toString().trim();
			AgainPass = regAgainPassword.getText().toString().trim();

			Logger.e("reg_btn", name + passWord + AgainPass);
			// if(name.equals("")||passWord.equals("")||AgainPass.equals("")||name==null||passWord==null||AgainPass==null){
			if (passWord.equals(AgainPass)) {
				if (isCheck) {

					if (name.length() <= 6 && name.length() >= 3) {
						if (passWord.length() <= 13 && passWord.length() >= 6) {

							Register(name, passWord);
						} else {
							Toast.makeText(RegisterActivity.this,
									"注册密码长度不能低于6位且不能超过13位，请重新输入",
									Toast.LENGTH_SHORT).show();

						}
					}

					else {
						Toast.makeText(RegisterActivity.this,
								"注册名字长度不能低于3位且不能超过6位，请重新输入", Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					Toast.makeText(RegisterActivity.this, "没有同意《盒子漫画》使用条款不能注册",
							Toast.LENGTH_SHORT).show();
				}
			} else {

				Toast.makeText(RegisterActivity.this, "两次密码不一样，请重新输入",
						Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.reg_check:

			if (isCheck) {
				regCheck.setBackgroundResource(R.drawable.check_none);

				isCheck = false;
			} else {
				regCheck.setBackgroundResource(R.drawable.check);
				isCheck = true;

			}

			break;
		case R.id.reg_text:

			startActivityWithSlideAnimation(TermsActivity.class);

			break;
		default:
			break;
		}

	}

	private void Register(final String user_name, final String user_passWord) {

		Callback localCallback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				Logger.v("authrizeAction", UserInfoManager.isLogin() + "");
				if (UserInfoManager.isLogin()) {
					if (type.equals("UserCenter")) {

						startActivityWithSlideAnimation(UserCenterActivity.class);
					} else if (type.equals("Comment")) {
						// Intent aIntent = new Intent(RegisterActivity.this,
						// CommentActivity.class);
						// aIntent.putExtra("isUser",
						// getIntent().getBooleanExtra("isUser", false));
						//
						// startActivity(aIntent);

					}
					finishActivityWithAnim();
				} else {
					Toast.makeText(RegisterActivity.this, "注册失败",
							Toast.LENGTH_SHORT).show();

				}

			}
		};

		new DataAsyncTask(localCallback, true) {

			protected String doInBackground(String... params) {

				UserApi userApi = new UserApi();

				User userinfo = userApi.Register(user_name, user_passWord);

				if (userinfo != null) {
					saveUserInfo2Local(userinfo, 0);
					return "true";
				}
				return "false";

			};

		}.executeProxy("");

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

}
