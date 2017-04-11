package com.bccv.zhuiyingzhihanju.activity;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.api.UserAPi;
import com.bccv.zhuiyingzhihanju.model.User;
import com.tendcloud.tenddata.TCAgent;
import com.utils.tools.AppConfig;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;
import com.utils.tools.GlobalParams;
import com.utils.tools.StringUtils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {
	String Phone, PassWords;
	EditText PhoneEdit, WordsEdit;

	Button LoginBtn;

	ImageView clear;
	int RESULT;
	LinearLayout re;
	private String type;
	private void tcStart(){
		TCAgent.onPageStart(getApplicationContext(), "LoginActivity");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		TCAgent.onPageEnd(getApplicationContext(), "LoginActivity");
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tcStart();
		setContentView(R.layout.activity_login);
		ImageButton backBtn = (ImageButton) findViewById(R.id.titel_back);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		type = getIntent().getStringExtra("type");

		PhoneEdit = (EditText) findViewById(R.id.phone_edit);
		WordsEdit = (EditText) findViewById(R.id.pass_edit);
		re = (LinearLayout) findViewById(R.id.title);

		clear = (ImageView) findViewById(R.id.login_clear);
		clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				PhoneEdit.setText("");
			}
		});

		LoginBtn = (Button) findViewById(R.id.login_btn);
		LoginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (getUserText().equals("")) {

					showLongToast("电话号码不能为空");

				} else if (getUserText().length() < 11 || getUserText().length() > 11) {

					showLongToast("电话号码输入错误");
				} else {
					getLogInData(getUserText(), getPassWordText());

				}

			}
		});
		RelativeLayout	passText = (RelativeLayout) findViewById(R.id.passWord_btn);
		passText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putBoolean("isRest", true);

				startActivityForResultSlideAnimation(RegisterActivity.class, bundle);
			}
		});
		RelativeLayout	RegText = (RelativeLayout) findViewById(R.id.reg_btn);
		RegText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putBoolean("isRest", false);

				startActivityForResultSlideAnimation(RegisterActivity.class, bundle);
			}
		});

		WordsEdit.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int actionId, KeyEvent arg2) {
				// TODO Auto-generated method stub

				if (getUserText().equals("")) {

					showLongToast("电话号码不能为空");

				} else if (getUserText().length() < 11 || getUserText().length() > 11) {

					showLongToast("电话号码输入错误");
				} else {
					getLogInData(getUserText(), getPassWordText());

				}

				return false;
			}
		});

	}

	public String getUserText() {
		return PhoneEdit.getText().toString();
	}

	public String getPassWordText() {
		return WordsEdit.getText().toString();
	}

	private void getLogInData(final String userName, final String passWord) {
		// TODO Auto-generated method stub

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (result.equals("true")) {
					RESULT = 1;
					showLongToast("登录成功");

					if (type.equals("C")) {

						startActivityWithSlideAnimation(CollectActivity.class);

					} else if (type.equals("D")) {

						startActivityWithSlideAnimation(DownloadActivity.class);

					} else if (type.equals("M")) {

						startActivityWithSlideAnimation(MoreListActivity.class);

					}
					finish();
				} else if (result.equals("false")) {
					showLongToast("登录失败");
				} else {

					Toast.makeText(getApplication(), result, Toast.LENGTH_SHORT).show();

				}

			}
		};
		new DataAsyncTask(callback, true) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				User user = null;
				try {
					UserAPi re = new UserAPi(LoginActivity.this);
					user = re.Login(userName, passWord);
					Log.e("user", user.toString());

					String userID = user.getUid();

					if (userID != null && StringUtils.isEmpty(user.getUserErr())) {

						GlobalParams.user = user;
						GlobalParams.hasLogin = true;
						AppConfig.setPrefUserInfo(user);
						return "true";
					} else {

						return user.getUserErr();
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				return "false";
			}
		}.executeProxy("");

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.e("requestCode", resultCode + "");
		System.out.println("request : " + requestCode + "\r\n" + "result : " + resultCode + "data  :"
				+ data.getExtras().getString("result"));
		if (resultCode == 1) {
			RESULT = 1;
			finish();

		}
		if (resultCode == 2) {
			RESULT = 2;
			finish();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void finish() {
		// 数据是使用Intent返回
		Intent intent = new Intent();
		// 把返回数据存入Intent
		intent.putExtra("result", "Is user's data changed?");
		// 设置返回数据
		this.setResult(RESULT, intent);
		super.finish();
	}

}
