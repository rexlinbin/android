package com.bccv.meitu.common.listener;

import android.widget.Toast;

import com.bccv.meitu.ApplicationManager;
import com.bccv.meitu.sns.UserInfoManager;
import com.bccv.meitu.utils.Logger;
import com.bccv.meitu.utils.RCUtil;
import com.bccv.meitu.utils.RCUtil.IRCConnectionStatusListener;

public class RCConnectionStatusListener implements IRCConnectionStatusListener {

	public static final String TAG = "RCConnectionStatusListener";
	
	public static RCConnectionStatusListener mRCConnectionStatusListener = new RCConnectionStatusListener();
	
	private RCConnectionStatusListener(){}
	
	public static RCConnectionStatusListener getInstance(){
		return mRCConnectionStatusListener;
	}
	
	@Override
	public void onKicked() {
		//TODO  被T下线了
		Logger.e(TAG, "onKicked", " kicked offline by other client !!!  ");
		Toast.makeText(ApplicationManager.getGlobalContext(), "您的账号已在其他设备登录", Toast.LENGTH_SHORT).show();
		UserInfoManager.logOut();
		RCUtil.disconnect(false);
	}

}
