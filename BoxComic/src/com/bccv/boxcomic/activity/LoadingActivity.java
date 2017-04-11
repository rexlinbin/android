package com.bccv.boxcomic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.api.SystemApi;
import com.bccv.boxcomic.tool.BaseActivity;
import com.bccv.boxcomic.tool.Callback;
import com.bccv.boxcomic.tool.GlobalParams;
import com.igexin.sdk.PushManager;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatReportStrategy;
import com.tencent.stat.StatService;
import com.tencent.stat.common.StatConstants;

public class LoadingActivity extends BaseActivity {
	ImageView flash;
	private boolean isStart = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		GlobalParams.isLoadingActivity = true;
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		
		StatService.trackCustomEvent(this, "onCreate", "");
		
		StatConfig.setStatSendStrategy(StatReportStrategy.APP_LAUNCH);
		
		//推送
		PushManager.getInstance().initialize(this.getApplicationContext());
		
		flash=(ImageView) findViewById(R.id.imageView1);

		flash.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (isStart) {
					return;
				}
				isStart = true;
				goMainActivity();
				
			}
		});

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (GlobalParams.canUpdate) {
					return;
				}
				if (isStart) {
					return;
				}
				isStart = true;
				goMainActivity();
			}
		};

		new DataAsyncTask(callback, false) {
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				SystemClock.sleep(3000);
				try {
					SystemApi systemApi = new SystemApi();
					systemApi.getSystemInfo();
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				return null;
			}
		}.execute("");
	}

	public void goMainActivity(){
		GlobalParams.isLoadingActivity = false;
		Intent intent = new Intent(getApplicationContext(),
				MainActivity.class);
		startActivity(intent);
		finish();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}

}
