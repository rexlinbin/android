package com.bccv.boxcomic.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.api.FeedbackApi;
import com.bccv.boxcomic.tool.BaseActivity;
import com.bccv.boxcomic.tool.Callback;
import com.bccv.boxcomic.tool.GlobalParams;
import com.bccv.boxcomic.tool.StringUtils;

public class FeedbackActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
	
		setBack();
		
		final EditText feedbackEditText = (EditText) findViewById(R.id.feedback_editText);
		
		Button feedbackButton = (Button) findViewById(R.id.feedback_button);
		feedbackButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (StringUtils.isEmpty(feedbackEditText.getText().toString())) {
					Toast.makeText(getApplicationContext(), "请先填写反馈意见", 1).show();
				}else {
					sendFeedback(feedbackEditText.getText().toString());
				}
			}
		});
	}
	
	private void sendFeedback(final String content){
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (result.equals("true")) {
					Toast.makeText(getApplicationContext(), "反馈成功", 1).show();
				}else {
					Toast.makeText(getApplicationContext(), "反馈失败", 1).show();
				}
				finish();
				
			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				FeedbackApi feedbackApi = new FeedbackApi();
				boolean request = feedbackApi.seedFeedback(GlobalParams.user.getUser_id() + "", content);
				if (request) {
					return "true";
				}
				return "false";
			}
		}.executeProxy("");
	}
	
	private void setBack(){
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.back_relativeLayout);
		relativeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
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
