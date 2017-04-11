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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class NextRegisterActivity extends BaseActivity {
	String Phone, Code;
	EditText NPhoneEdit, NcodeEdit, NRePassEdit;

	Button NRegBtn;

	Boolean isRest;
	private int RESULT = -1;
	String type;
	String msg;

	private void tcStart(){
		TCAgent.onPageStart(getApplicationContext(), "NextRegisterActivity");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		TCAgent.onPageEnd(getApplicationContext(), "NextRegisterActivity");
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tcStart();
		setContentView(R.layout.activity_nextre);
		Code = getIntent().getStringExtra("code");
		Phone = getIntent().getStringExtra("phone");

		NPhoneEdit = (EditText) findViewById(R.id.NREphone_edit);
		NPhoneEdit.setText(Phone);
		NcodeEdit = (EditText) findViewById(R.id.NREpass_edit);
		NRePassEdit = (EditText) findViewById(R.id.NREpass_edit1);

		ImageButton backBtn = (ImageButton) findViewById(R.id.titel_back);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		NRegBtn = (Button) findViewById(R.id.NRegister_btn);

		TextView text = (TextView) findViewById(R.id.Next_text);
		isRest = getIntent().getBooleanExtra("isRest", false);

		if (isRest) {
			text.setText("重置密码");
			NRegBtn.setText("修改密码");
			type = "2";
		} else {

			text.setText("注册账号");
			type = "1";
		}

		RelativeLayout clear = (RelativeLayout) findViewById(R.id.re_clear);
		clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				NPhoneEdit.setText("");
			}
		});

		NRegBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (getCodeText().equals("")) {
					Toast.makeText(getApplication(), "密码不能为空", Toast.LENGTH_SHORT).show();
				} else if (getReCodeText().equals("")) {

					Toast.makeText(getApplication(), "重复密码不能为空", Toast.LENGTH_SHORT).show();

				} else

				if (getCodeText().equals(getReCodeText())) {
					if (isRest) {
						getFortget(Phone, Code, getReCodeText());
					} else {
						getReginData(Phone, getCodeText(), Code);
					}

				} else {

					showLongToast("两次密码输入不一致");

				}

			}
		});

		NRePassEdit.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int actionId, KeyEvent arg2) {
				// TODO Auto-generated method stub

				if (getCodeText().equals("")) {
					Toast.makeText(getApplication(), "密码不能为空", Toast.LENGTH_SHORT).show();
				} else if (getReCodeText().equals("")) {

					Toast.makeText(getApplication(), "重复密码不能为空", Toast.LENGTH_SHORT).show();

				} else

				if (getCodeText().equals(getReCodeText())) {
					if (isRest) {
						getFortget(getUserText(), Code, getReCodeText());
					} else {
						getReginData(getUserText(), getCodeText(), Code);
					}

				} else {

					showLongToast("两次密码输入不一致");

				}

				return false;
			}
		});

	}

	public String getUserText() {
		return NPhoneEdit.getText().toString();
	}

	public String getCodeText() {
		return NcodeEdit.getText().toString();
	}

	public String getReCodeText() {
		return NRePassEdit.getText().toString();
	}

	private void getReginData(final String phone, final String passWord, final String code) {
		// TODO Auto-generated method stub

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (result.equals("true")) {
					// getData(true);
					RESULT = 1;
					finish();
					showLongToast("注册成功");
				} else if (result.equals("false")) {
					Toast.makeText(getApplication(), "注册失败", Toast.LENGTH_SHORT).show();
				}

				else {

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
					UserAPi re = new UserAPi(NextRegisterActivity.this);
					user = re.Register(phone, passWord, code);

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

	private void getFortget(final String phone, final String code, final String password) {
		// TODO Auto-generated method stub

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				
				// TODO Auto-generated method stub
				if (result == "true") {
					Toast.makeText(getApplication(), "修改密码成功", Toast.LENGTH_SHORT).show();

					RESULT = 2;
					finish();
				} else {

					Toast.makeText(getApplication(), "修改密码失败", Toast.LENGTH_SHORT).show();

				}
			}
		};
		new DataAsyncTask(callback, true) {

			@Override
			protected String doInBackground(String... params) {

				try {
					UserAPi re = new UserAPi(NextRegisterActivity.this);
					msg = re.forget(phone, password, code);

					return "true";
				} catch (Exception e) {
					// TODO: handle exception
				}

				return "false";

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
