package com.boxuu.gamebox.ui.menupages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.boxuu.gamebox.R;
import com.boxuu.gamebox.ui.activity.MainActivity;

public class LeftMenuView implements OnClickListener {

	public MainActivity mMainActivity;
	public Context mContext;
	public View mView;
	
	
	public LeftMenuView(MainActivity mainActivity) {
		mMainActivity = mainActivity;
		mContext = mainActivity.getApplicationContext();
		mView = LayoutInflater.from(mContext).inflate(R.layout.left_menu, null);
		initView();
		initData();
	}
	
	
	private void initView(){
		
	}
	
	private void initData(){
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
