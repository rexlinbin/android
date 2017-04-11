package com.bccv.tianji.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.tianji.R;
import com.bccv.tianji.api.RegisterApi;
import com.bccv.tianji.model.User;
import com.utils.tools.AppConfig;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;
import com.utils.tools.GlobalParams;
import com.utils.tools.StringUtils;

public class SetRegisterPasswordActivity extends BaseActivity {

	private String phoneNum;

	private Button authBtn;
	private static int timeNum = 60;
	private static Timer timer = new Timer();
	TextView errText;
	private EditText edit;
	private String yancode;
	private EditText editPassword, editPassword1;
	private boolean isAgree=false;
	boolean isResetWord;
	String type;
private  int i = 0;
	private int RESULT = -1;
	ImageButton back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_password);

		TextView title = (TextView) findViewById(R.id.titleText);
		back = (ImageButton) findViewById(R.id.Back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		isResetWord = getIntent().getBooleanExtra("isReset", false);

		if (isResetWord) {
			title.setText("重置密码");
			type = "2";
		} else {

			title.setText("设置登录密码");
			type = "1";
		}
		phoneNum = getIntent().getStringExtra("phoneNum");

		authBtn = (Button) findViewById(R.id.register_authBtn);
	
		edit = (EditText) findViewById(R.id.register_phoneEdit);
		editPassword = (EditText) findViewById(R.id.register_passwordEdit);
		editPassword1 = (EditText) findViewById(R.id.register_passwordEdit1);
		final ImageView isA = (ImageView) findViewById(R.id.register_Choose);
		isA.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub


	if (isAgree) {
		isA.setBackgroundResource(R.drawable.check);
		isAgree = false;

	} else {
		
		isA.setBackgroundResource(R.drawable.regist_agree_law_normal);
		isAgree = true;
	}


			}
		});

		authBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				timeNum = 60;
				setData();
				TimerTask task = new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mHandler.sendEmptyMessage(0);
					}
				};
				timer = new Timer();
				timer.schedule(task, 0, 1000);

			}
		});

		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(0);
			}
		};
		timer = new Timer();
		timer.schedule(task, 0, 1000);

		TextView textRePh = (TextView) findViewById(R.id.register_phone);

		textRePh.setText("验证短信已发至" + phoneNum);

		errText = (TextView) findViewById(R.id.register_errText);
		Button RegisterBtn = (Button) findViewById(R.id.register_btn);

		RegisterBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				yancode = edit.getText().toString().trim();
				String Password = editPassword.getText().toString().trim();
				String rePassword = editPassword1.getText().toString().trim();
				if (yancode.equals("")) {

					errText.setVisibility(View.VISIBLE);
					errText.setText("验证码不能为空");
				} else

				if (Password.equals("")) {
					Toast.makeText(getApplication(), "密码不能为空",
							Toast.LENGTH_SHORT).show();
				} else if (rePassword.equals("")) {

					Toast.makeText(getApplication(), "重复密码不能为空",
							Toast.LENGTH_SHORT).show();

				} else if (!Password.equals(rePassword)) {

					Toast.makeText(getApplication(), "两次密码输入不一样",
							Toast.LENGTH_SHORT).show();

				}

				else if (Password.length() > 20 || Password.length() < 6) {

					Toast.makeText(getApplication(), "请输入6到20位之间的密码",
							Toast.LENGTH_SHORT).show();
				} else {
					if (!isAgree) {

						if (isResetWord) {
							getFortget(phoneNum, yancode, Password);
						} else {
							getTest(phoneNum, yancode, Password);
						}
					} else {
						Toast.makeText(getApplication(), "请同意天玑使用条款和隐私声明",
								Toast.LENGTH_SHORT).show();

					}
				}

			}
		});

	}

	private void getFortget(final String phone, final String code,
			final String password) {
		// TODO Auto-generated method stub

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (result == "true") {
					Toast.makeText(getApplication(), "修改密码成功",
							Toast.LENGTH_SHORT).show();
					RESULT = 2;
					finish();
				} else {

					Toast.makeText(getApplication(), "修改密码失败",
							Toast.LENGTH_SHORT).show();

				}
			}
		};
		new DataAsyncTask(callback, true) {

			@Override
			protected String doInBackground(String... params) {
				try {
					RegisterApi re = new RegisterApi(
							SetRegisterPasswordActivity.this);
					User userinfo = re.forget(phone, code, password);

					return "true";
				} catch (Exception e) {
					// TODO: handle exception
				}

				return null;

			}
		}.executeProxy("");

	}

	private void getTest(final String phone, final String code,
			final String password) {
		// TODO Auto-generated method stub

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {

				if (result.equals("false")) {

					Toast.makeText(getApplication(), "注册失败", Toast.LENGTH_SHORT)
							.show();
				} else if (result.equals("true")) {

					Toast.makeText(getApplication(), "注册成功", Toast.LENGTH_SHORT)
							.show();
					RESULT = 1;
					finish();
				}
				else{
					

					Toast.makeText(getApplication(), result, Toast.LENGTH_SHORT)
							.show();
					
				}
			}
		};
		new DataAsyncTask(callback, true) {

			@Override
			protected String doInBackground(String... params) {
				try {
					RegisterApi re = new RegisterApi(
							SetRegisterPasswordActivity.this);
					User userinfo = re.Register(phone, code, password);
					Log.e("user", userinfo.toString());
					if (userinfo != null && StringUtils.isEmpty(userinfo.getUserErr())) {
						GlobalParams.user = userinfo;
						GlobalParams.hasLogin = true;
						AppConfig.setPrefUserInfo(userinfo);

					}else{
						return userinfo.getUserErr();
					}

					return "true";
				} catch (Exception e) {
					// TODO: handle exception
					Log.e("错误提示",e.toString());
				}
				return "false";
			}
		}.executeProxy("");

	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			authBtn.setText(timeNum + "后重新获取");
			if (timeNum == 0) {
				timer.cancel();
				authBtn.setText("重新获取");
				timeNum = 60;
				authBtn.setEnabled(true);
			}
			timeNum--;
		};
	};

	private void setData() {
		// TODO Auto-generated method stub

		Callback call = new Callback() {
			@Override
			public void handleResult(String result) {

				if (result.equals("1")) {

				} else {

				}

			}
		};

		new DataAsyncTask(call, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				try {

					RegisterApi regi = new RegisterApi(
							SetRegisterPasswordActivity.this);
					String result = regi.getCode(phoneNum, type);

					return result;

				} catch (Exception e) {
					// TODO: handle exception
				}

				return "true";
			}
		}.executeProxy("");

	}

	// @Override
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
