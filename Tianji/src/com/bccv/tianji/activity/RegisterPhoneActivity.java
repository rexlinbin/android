package com.bccv.tianji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.tianji.R;
import com.bccv.tianji.api.RegisterApi;
import com.utils.tools.AppConfig;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;
import com.utils.tools.GlobalParams;

public class RegisterPhoneActivity extends BaseActivity {
	String phoneNum;
	TextView errText;

	boolean isResetWord;
	String type;
	EditText phone;
	int RESULT;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.register_phone);

		TextView title = (TextView) findViewById(R.id.titleText);

		isResetWord = getIntent().getBooleanExtra("isReset", false);

		if (isResetWord) {
			title.setText("忘记密码");

			type = "2";
			RESULT=2;
		} else {

			title.setText("手机注册");

			type = "1";
			RESULT=1;
		}

		ImageButton backBtn = (ImageButton) findViewById(R.id.Back);

		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finishActivityWithAnim();
			}
		});

		phone = (EditText) findViewById(R.id.register_tellEdit);

		Button NextBtn = (Button) findViewById(R.id.register_nextbtn);

		errText = (TextView) findViewById(R.id.register_errText);
		NextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				phoneNum = phone.getText().toString().trim();
				if (phoneNum.equals("")) {
					errText.setVisibility(View.VISIBLE);
					errText.setText("电话号码不能为空");

				} else if (phoneNum.length() < 11 || phoneNum.length() > 11) {
					errText.setVisibility(View.VISIBLE);
					errText.setText("电话号码输入错误");
				} else {
					setData();
					Intent aIntent = new Intent(RegisterPhoneActivity.this,
							SetRegisterPasswordActivity.class);
					aIntent.putExtra("phoneNum", phoneNum);

					aIntent.putExtra("isReset", isResetWord);
				startActivityForResult(aIntent, 0);
				}

			}
		});

	}

	private void setData() {
		// TODO Auto-generated method stub

		Callback call = new Callback() {
			@Override
			public void handleResult(String result) {

				if (result.equals("1")) {

				} else {

					Toast.makeText(getApplication(), result, Toast.LENGTH_SHORT)
							.show();
				}

			}
		};

		new DataAsyncTask(call, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				try {

					RegisterApi regi = new RegisterApi(
							RegisterPhoneActivity.this);
					String result = regi.getCode(phoneNum, type);

					return result;

				} catch (Exception e) {
					// TODO: handle exception
				}

				return "true";
			}
		}.executeProxy("");

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.e("requestCode", resultCode + "");
		System.out
				.println("request : " + requestCode + "\r\n" + "result : "
						+ resultCode + "data  :"
						+ data.getExtras().getString("result"));
		if (resultCode == 1) {
			finish();

		}
		if (resultCode == 2) {
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
