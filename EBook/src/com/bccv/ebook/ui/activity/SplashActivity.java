package com.bccv.ebook.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import com.boxuu.ebookjy.R;

@SuppressLint("HandlerLeak")
public class SplashActivity extends BaseActivity {

	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		initHandler();
		mHandler.sendMessageDelayed(mHandler.obtainMessage(), 2000);

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
