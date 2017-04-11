package com.bccv.threedimensionalworld.activity;

import com.bccv.threedimensionalworld.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

@SuppressLint("NewApi")
public class AboutAcitvity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View mainView = View.inflate(getApplicationContext(), R.layout.activity_about, null);
		mainView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);  
		setContentView(mainView);
	}

}
