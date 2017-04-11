package com.bccv.threedimensionalworld.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;

import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.tool.BaseActivity;

public class MovieUserActivity extends BaseActivity {
	private Button lefthistoryButton, leftcollectButton, leftdownloadButton,
			righthistoryButton, rightcollectButton, rightdownloadButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movieuser);
		setLeft();
		setRight();
	}
	
	private void setLeft(){
		lefthistoryButton = (Button) findViewById(R.id.left_userhistory_Button);
		leftcollectButton = (Button) findViewById(R.id.left_usercollect_Button);
		leftdownloadButton = (Button) findViewById(R.id.left_userdownload_Button);
		
		lefthistoryButton.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				righthistoryButton.setSelected(hasFocus);
			}
		});
		
		leftcollectButton.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				rightcollectButton.setSelected(hasFocus);
			}
		});
		
		leftdownloadButton.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				rightdownloadButton.setSelected(hasFocus);
			}
		});
	}
	
	private void setRight(){
		
		righthistoryButton = (Button) findViewById(R.id.right_userhistory_Button);
		rightcollectButton = (Button) findViewById(R.id.right_usercollect_Button);
		rightdownloadButton = (Button) findViewById(R.id.right_userdownload_Button);
	}
}
