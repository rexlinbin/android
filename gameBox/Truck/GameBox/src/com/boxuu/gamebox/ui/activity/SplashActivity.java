package com.boxuu.gamebox.ui.activity;

import com.boxuu.gamebox.R;
import com.wbtech.ums.UmsAgent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;

public class SplashActivity extends BaseActivity {

	private static final int TO_HOME = 1;
	private Handler mHandler;
	
	private View splash_iv ; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
		
		UmsAgent.setBaseURL("http://tongji.bccv.com/index.php?");
		UmsAgent.onError(this);
		UmsAgent.setDefaultReportPolicy(this, 1);
		UmsAgent.postClientData(this);
		
	}
	
	private void initView(){
		setContentView(R.layout.activity_splash);
		splash_iv = findViewById(R.id.splash_iv);
		splash_iv.setOnClickListener(this);
	}
	
	@SuppressLint("HandlerLeak")
	private void initData(){
		
		if(mHandler==null){
			mHandler = new Handler(){

				@Override
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case TO_HOME:
						Intent intent = new Intent(SplashActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
						break;

					default:
						break;
					}
				}
				
			};
		}
		
		mHandler.sendEmptyMessageDelayed(TO_HOME, 2000);
	
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.splash_iv:
			mHandler.removeMessages(TO_HOME);
			mHandler.sendEmptyMessage(TO_HOME);
			break;

		default:
			break;
		}
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
}
