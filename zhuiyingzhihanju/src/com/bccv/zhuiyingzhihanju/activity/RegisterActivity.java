package com.bccv.zhuiyingzhihanju.activity;

import java.util.Timer;
import java.util.TimerTask;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.api.UserAPi;
import com.tendcloud.tenddata.TCAgent;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class RegisterActivity extends BaseActivity {
	String Phone, Code;
	EditText PhoneEdit, codeEdit;

	Button RegBtn, CodeBtn;


	TextView text;
	Boolean isRest;
	String type;
	private boolean Agree = false;
	int RESULT;
	private static int timeNum = 60;
	private static Timer timer = new Timer();
	String code,CheckCode="";
	
	private void tcStart(){
		TCAgent.onPageStart(getApplicationContext(), "RegisterActivity");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		TCAgent.onPageEnd(getApplicationContext(), "RegisterActivity");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
tcStart();
		setContentView(R.layout.activity_register);

		text = (TextView) findViewById(R.id.re_text);
		final ImageView isA = (ImageView) findViewById(R.id.register_Choose);
		ImageButton backBtn = (ImageButton) findViewById(R.id.titel_back);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		isRest = getIntent().getBooleanExtra("isRest", false);

		TextView isAgree = (TextView) findViewById(R.id.register_isGRee);
		Log.e("isRest", isRest + "");

		if (isRest) {
			text.setText("忘记密码");

			type = "2";
			RESULT = 2;
			isAgree.setVisibility(View.INVISIBLE);
			isA.setVisibility(View.INVISIBLE);
		} else {

			text.setText("手机注册");

			type = "1";
			RESULT = 1;
			isAgree.setVisibility(View.VISIBLE);
		}

		
		isA.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (Agree) { 
					isA.setBackgroundResource(R.drawable.agree);
					Agree = false;

				} else {

					isA.setBackgroundResource(R.drawable.disagree);
					Agree = true;
				}

			}
		});

		RegBtn = (Button) findViewById(R.id.Register_btn);
		RegBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!Agree) {
					
				
					getData();
			
					
				} else {
					Toast.makeText(getApplication(), "请同意韩剧迷使用条款和隐私声明", Toast.LENGTH_SHORT).show();

				}

			}
		});
		isAgree.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityWithSlideAnimation(HanJuMiAgreeActivity.class);
			}
		});
		PhoneEdit = (EditText) findViewById(R.id.REphone_edit);
		codeEdit = (EditText) findViewById(R.id.REpass_edit);
		codeEdit.setInputType(EditorInfo.TYPE_CLASS_PHONE);

		RelativeLayout clear = (RelativeLayout) findViewById(R.id.re_clear);
		clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				PhoneEdit.setText("");

			}
		});

		CodeBtn = (Button) findViewById(R.id.regCode_btn);
		CodeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (getUserText().equals("")) {

					showLongToast("电话号码不能为空");

				} else if (getUserText().length() < 11 || getUserText().length() > 11) {

					showLongToast("电话号码输入错误");
				} else {
					CodeBtn.setClickable(false);
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

			}
		});

		codeEdit.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int actionId, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (!Agree) {
					Intent aIntent = new Intent(RegisterActivity.this, NextRegisterActivity.class);

					aIntent.putExtra("code", getCodeText());
					aIntent.putExtra("phone", getUserText());
					aIntent.putExtra("isRest", isRest);
					startActivityForResult(aIntent, 0);

				} else {
					Toast.makeText(getApplication(), "请同意韩剧迷使用条款和隐私声明", Toast.LENGTH_SHORT).show();

				}
				return false;
			}
		});
		

	}

	public String getUserText() {
		return PhoneEdit.getText().toString();
	}

	public String getCodeText() {
		return codeEdit.getText().toString();
	}

	private void setData() {
		// TODO Auto-generated method stub

		Callback call = new Callback() {
			@Override
			public void handleResult(String result) {
				Log.e("result", result);
//
//			if(result.length()!=4){
				Toast.makeText(getApplication(), code, Toast.LENGTH_SHORT).show();
//			}
					
				
					
					
			}
		};

		new DataAsyncTask(call, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				try {

					UserAPi regi = new UserAPi(RegisterActivity.this);
					 code = regi.getCode(getUserText(), type);
						
					return code;

				} catch (Exception e) {
					// TODO: handle exception
				}

				return "true";
			}
		}.executeProxy("");

	}


	private String  getData() {
		// TODO Auto-generated method stub

		Callback call = new Callback() {
			@Override
			public void handleResult(String result) {
			

				if(CheckCode.equals("验证码正确")){

					Intent aIntent = new Intent(RegisterActivity.this, NextRegisterActivity.class);

					aIntent.putExtra("code", getCodeText());
					aIntent.putExtra("phone", getUserText());
					aIntent.putExtra("isRest", isRest);
					
					
					startActivityForResult(aIntent, 0);

				}else{
					showShortToast(CheckCode);
				}
					
				
					
					
			}
		};

		new DataAsyncTask(call, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				try {

					UserAPi regi = new UserAPi(RegisterActivity.this);
					CheckCode = regi.getCodeMsg(getUserText(), type,getCodeText());

					return CheckCode;

				} catch (Exception e) {
					// TODO: handle exception
				}

				return CheckCode;
			}
		}.executeProxy("");
		
	
		return CheckCode;
	}
	
	
	
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
			CodeBtn.setText(timeNum + "秒后重新获取");
		
			
			if (timeNum == 0) {
				CodeBtn.setClickable(true);
				timer.cancel();
				CodeBtn.setText("重新获取");
				timeNum = 60;
				CodeBtn.setEnabled(true);
			}
			timeNum--;
		};
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.e("requestCode", resultCode + "");
		System.out.println("request : " + requestCode + "\r\n" + "result : " + resultCode + "data  :"
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
