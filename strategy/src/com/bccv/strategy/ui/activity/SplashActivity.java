package com.bccv.strategy.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.bccv.strategy.common.GlobalConstants;
import com.bccv.strategy.utils.FileUtils;
import com.bccv.strategy.R;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatReportStrategy;
import com.tencent.stat.StatService;

@SuppressLint("HandlerLeak")
public class SplashActivity extends BaseActivity {

	private Handler mHandler;
	private View view;
	private static final int GOTO_HOME = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		//启动上报
		StatConfig.setStatSendStrategy(StatReportStrategy.APP_LAUNCH);
		StatService.trackCustomEvent(this, "onCreate");
		initHandler();
		mHandler.sendMessageDelayed(mHandler.obtainMessage(GOTO_HOME), 2000);
		view = findViewById(R.id.splash_pic_def);
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mHandler.removeMessages(GOTO_HOME);
				mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(GOTO_HOME));
			}
		});
		new Thread(new Runnable(){
			@Override
			public void run() {
				FileUtils.copyFile(getResources().openRawResource(R.raw.ic_launcher), 
						GlobalConstants.launcher_path);
			}
		}).start();
	}

	private void initHandler() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Intent intent = new Intent(SplashActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			}
		};
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK://屏蔽返回键
			return true;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

}
