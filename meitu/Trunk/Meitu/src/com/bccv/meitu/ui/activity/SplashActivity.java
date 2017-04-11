package com.bccv.meitu.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import com.bccv.meitu.R;
import com.bccv.meitu.api.NetWorkAPI;
import com.bccv.meitu.network.HttpCallback;
import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.sns.UserInfoManager;
import com.bccv.meitu.utils.Logger;
import com.bccv.meitu.utils.RCUtil;
import com.bccv.meitu.utils.RCUtil.RCConnectCallback;
import com.bccv.meitu.utils.SystemUtil;

@SuppressLint("HandlerLeak")
public class SplashActivity extends BaseActivity {

	private Handler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		
		if(SystemUtil.isNetOk(mContext)){
			if(UserInfoManager.isLogin()){
				
				Logger.v(TAG, "onCreate", "token : "+UserInfoManager.getUserToken());
				
				RCUtil.connect(UserInfoManager.getUserToken(), new RCConnectCallback() {
					
					@Override
					public void onSuccess(String userId) {
						Logger.v(TAG, "onCreate onSuccess", " rc login success");
					}
					
					@Override
					public void onError(String msg, int errorCode) {
						Logger.e(TAG, "onCreate onError", " rc login error : " 
								+ msg + " errorCode : " + errorCode );
					}
				});
			}
		}
		
		if(UserInfoManager.isLogin()){
			RCUtil.setCommunicationInfo(String.valueOf(UserInfoManager.getUserId()),
					UserInfoManager.getUserName(), UserInfoManager.getUserIcon());
		}
		
		initHandler();
		mHandler.sendMessageDelayed(mHandler.obtainMessage(), 2000);
		
	}
	
	private void initHandler(){
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				Intent intent = new Intent(SplashActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
			}
		};
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			return true;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
