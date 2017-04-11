package com.bccv.bangyangapp.ui.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bccv.bangyangapp.R;
import com.bccv.bangyangapp.sns.UserInfoManager;
import com.bccv.bangyangapp.ui.activity.MainActivity;
import com.bccv.bangyangapp.utils.ImageLoaderUtil;

public class LeftMenuView implements OnClickListener {

	public MainActivity mMainActivity;
	public Context mContext;
	public View mView;
	private	ImageView menu_head_iv;
	private TextView menu_username;
	private TextView menu_user_elucidation;
	
	
	public LeftMenuView(MainActivity mainActivity) {
		mMainActivity = mainActivity;
		mContext = mainActivity.getApplicationContext();
		mView = LayoutInflater.from(mContext).inflate(R.layout.left_menu, null);
		initView();
		initData();
	}
	
	
	private void initView(){
		View menu_head = mView.findViewById(R.id.menu_head);
		View menu_home = mView.findViewById(R.id.menu_home);
		View menu_classification = mView.findViewById(R.id.menu_classification);
		View menu_hot = mView.findViewById(R.id.menu_hot);
		View menu_question = mView.findViewById(R.id.menu_question);
		View menu_myApp = mView.findViewById(R.id.menu_myApp);
		View menu_comment = mView.findViewById(R.id.menu_comment);
		View menu_collection = mView.findViewById(R.id.menu_collection);
		View menu_search = mView.findViewById(R.id.menu_search);
		View menu_setting = mView.findViewById(R.id.menu_setting);
		
		menu_head_iv = (ImageView) menu_head.findViewById(R.id.menu_head_iv);
		menu_username = (TextView) menu_head.findViewById(R.id.menu_username);
		menu_user_elucidation = (TextView) menu_head.findViewById(R.id.menu_user_elucidation);
		
		menu_head.setOnClickListener(mMainActivity);
		menu_home.setOnClickListener(mMainActivity);
		menu_classification.setOnClickListener(mMainActivity);
		menu_hot.setOnClickListener(mMainActivity);
		menu_question.setOnClickListener(mMainActivity);
		menu_myApp.setOnClickListener(mMainActivity);
		menu_comment.setOnClickListener(mMainActivity);
		menu_collection.setOnClickListener(mMainActivity);
		menu_search.setOnClickListener(mMainActivity);
		menu_setting.setOnClickListener(mMainActivity);
		
	}
	
	public void initData(){
		if(UserInfoManager.isLogin()){
			ImageLoaderUtil.getInstance(mContext).displayImage(UserInfoManager.getUserIcon(), menu_head_iv, ImageLoaderUtil.getUserIconImageOptions());
			menu_username.setText(UserInfoManager.getUserName());
			String userIntroduce = UserInfoManager.getUserIntroduce();
			if(TextUtils.isEmpty(userIntroduce)){
				menu_user_elucidation.setText(R.string.menu_user_elucidation);
			}else{
				menu_user_elucidation.setText(userIntroduce);
			}
		}else{
			menu_head_iv.setImageResource(R.drawable.head_default);
			menu_username.setText("请登录");
			menu_user_elucidation.setText("");
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
