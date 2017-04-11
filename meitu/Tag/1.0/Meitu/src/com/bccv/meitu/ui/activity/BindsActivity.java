package com.bccv.meitu.ui.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bccv.meitu.R;
import com.bccv.meitu.api.NetWorkAPI;
import com.bccv.meitu.api.NetWorkAPI.UserType;
import com.bccv.meitu.network.HttpCallback;
import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.sns.SNSLoginManager;
import com.bccv.meitu.sns.SNSLoginManager.AuthorizingStateListener;
import com.bccv.meitu.sns.UserInfoManager;
import com.bccv.meitu.sns.bean.SNSUserInfo;
import com.bccv.meitu.utils.Logger;
import com.bccv.meitu.utils.SystemUtil;
import com.bccv.meitu.view.BindButton;

public class BindsActivity extends BaseActivity {
	
	private SNSLoginManager mSNSLoginManager;
	private View leftBt;
	private View waitting;
	
	private BindButton QQBt;
	private BindButton weixinBt;
	private BindButton sinaBt;
	
	private String tempBindInfo = "";
	
	private ArrayList<String> bindsList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more_platform);
		initView();
		initData();
	}

	private void initView(){
		leftBt = findViewById(R.id.left_buton);
		QQBt = (BindButton) findViewById(R.id.bind_QQ_check_box);
		weixinBt = (BindButton) findViewById(R.id.bind_weixin_check_box);
		sinaBt = (BindButton) findViewById(R.id.bind_sina_check_box);
		waitting = findViewById(R.id.waitting_layout);
		
		leftBt.setOnClickListener(this);
		QQBt.setOnClickListener(this);
		weixinBt.setOnClickListener(this);
		sinaBt.setOnClickListener(this);
		
	}
	
	/**
	 * 初始化SNSManager
	 */
	private void initManager() {
		mSNSLoginManager = SNSLoginManager.getInstance();
		mSNSLoginManager
				.setAuthiringStateListener(new MyAuthorizingStateListener());
	}
	
	private void initData(){
		getIntentData();
		
		String userBinds = UserInfoManager.getUserBinds();
		if(!TextUtils.isEmpty(userBinds)){
			String[] split = userBinds.split(",");
			for (int i = 0; i < split.length; i++) {
				bindsList.add(split[i]);
			}
		}
		iniBindsState();
		initManager();
		
	}
	
	private void getIntentData(){
		
	}
	
	
	/**
	 * 初始化绑定状态
	 */
	private void iniBindsState() {
		for (String bind : bindsList) {
			if(bind.equalsIgnoreCase("QQ")){
				QQBt.setChecked(true);
			}else if(bind.equalsIgnoreCase("WEIXIN")){
				weixinBt.setChecked(true);
			}else if(bind.equalsIgnoreCase("SINA")){
				sinaBt.setChecked(true);
			}
		}
		
		if(UserInfoManager.getUserType().equalsIgnoreCase("QQ")){
			QQBt.setClickable(false);
		}else if(UserInfoManager.getUserType().equalsIgnoreCase("WEIXIN")){
			weixinBt.setClickable(false);
		}else if(UserInfoManager.getUserType().equalsIgnoreCase("SINA")){
			sinaBt.setClickable(false);
		}
		
	}
	
	/**
	 * 保存绑定信息
	 */
	private void saveBinds(){
		StringBuilder binds = new StringBuilder();
		for (String bind : bindsList) {
			binds.append(bind);
			binds.append(",");
		}
		if(binds.length()>0){
			binds.deleteCharAt(binds.length()-1);
		}
		UserInfoManager.saveUserBinds(binds.toString());
		Logger.v(TAG, "saveBinds", "binds.toString() : " + binds.toString());
		
	}

	/**
	 * 改变绑定状态
	 * 
	 * @param Type
	 * 
	 */
	private void changeBindState(int type) {

		if (SystemUtil.isNetOkWithToast(mContext)) {
			BindButton temp;
			UserType userType = UserType.UNKNOW;
			switch (type) {
			case SNSLoginManager.SINA_WEIBO_TYPE:
				userType = UserType.SINA;
				temp = sinaBt;
				break;
			case SNSLoginManager.TENCENT_WEIXIN_TYPE:
				userType = UserType.WEIXIN;
				temp = weixinBt;
				break;
			case SNSLoginManager.TENCENT_QQ_TYPE:
				userType = UserType.QQ;
				temp = QQBt;
				break;
			default:
				return;
			}
			
			if(userType!=UserType.UNKNOW){
				
				if (temp.isChecked()) {
					//TODO  如果已经绑定  解绑
					waitting.setVisibility(View.VISIBLE);
					tempBindInfo = userType.name();
					NetWorkAPI.offuniteaccount(mContext, userType, new UnBindCallBack());
				} else {
					//TODO 如果没有绑定 
					waitting.setVisibility(View.VISIBLE);
					tempBindInfo = userType.name();
					mSNSLoginManager.authorizeByType(this,type);
				}
				
			}
		}
	}
	
	private void setunBindstate(){
		if(tempBindInfo.equals("QQ")){
			QQBt.setChecked(false);
		}else if(tempBindInfo.equals("WEIXIN")){
			weixinBt.setChecked(false);
		}else if(tempBindInfo.equals("SINA")){
			sinaBt.setChecked(false);
		}
	}
	
	private void bind(final SNSUserInfo user){
	
		UserType user_type = UserType.UNKNOW;
		switch (user.snsType) {
		case SNSLoginManager.SINA_WEIBO_TYPE:
			user_type = UserType.SINA;
			break;
		case SNSLoginManager.TENCENT_QQ_TYPE:
			user_type = UserType.QQ;
			break;
		case SNSLoginManager.TENCENT_WEIXIN_TYPE:
			user_type = UserType.WEIXIN;
			break;
		default:
			break;
			
		}
		
		NetWorkAPI.uniteaccount(mContext, user.thirdPlatformID, user_type, new BindCallBack());
		
	}
	
	
	private class UnBindCallBack implements HttpCallback{

		@Override
		public void onResult(NetResBean response) {
			if(response.success){
				setunBindstate();
				bindsList.remove(tempBindInfo);
				saveBinds();
				showShortToast("已解除绑定");
				tempBindInfo = "";
			}else{
				tempBindInfo = "";
				showShortToast("解绑失败");
			}
			waitting.setVisibility(View.GONE);
		}

		@Override
		public void onError(String errorMsg) {
			tempBindInfo = "";
			showShortToast("解绑失败");
			waitting.setVisibility(View.GONE);
			
		}

		@Override
		public void onCancel() {}

	}
	
	
	private class BindCallBack implements HttpCallback{

		@Override
		public void onResult(NetResBean response) {

			if(response.success){
				if(!bindsList.contains(tempBindInfo)){
					bindsList.add(tempBindInfo);
				}
				saveBinds();
				iniBindsState();
				showShortToast("绑定成功");
				tempBindInfo = "";
			}else{
				tempBindInfo = "";
				showShortToast("绑定失败");
			}
			waitting.setVisibility(View.GONE);
		}

		@Override
		public void onError(String errorMsg) {
			tempBindInfo = "";
			showShortToast("绑定失败");
			waitting.setVisibility(View.GONE);			
		}
		
		@Override
		public void onCancel() {}
		
	}
	
	
	/**
	 * SNS授权监听类
	 * 
	 * @author user 用户信息
	 * 
	 */
	private class MyAuthorizingStateListener implements
			AuthorizingStateListener {

		@Override
		public void authrizeAction(int action, String info) {

			switch (action) {
			case AuthorizingStateListener.AUTHORIZING:
				Logger.v(TAG, "authrizeAction", "AUTHORIZING");
				break;
			case AuthorizingStateListener.AUTHORIZE_SUCCESS:
				Logger.v(TAG, "authrizeAction", "AUTHORIZE_SUCCESS");
				waitting.setVisibility(View.VISIBLE);
				break;
			case AuthorizingStateListener.AUTHORIZE_CANCEL:
				Logger.v(TAG, "authrizeAction", "AUTHORIZE_CANCEL");
				waitting.setVisibility(View.GONE);
				break;
			case AuthorizingStateListener.AUTHORIZE_ERROR:
				Logger.v(TAG, "authrizeAction", "AUTHORIZE_ERROR");
				waitting.setVisibility(View.GONE);
				showShortToast(info);
				break;
			default:
				break;
			}
		}

		@Override
		public void onUserInfoComplete(SNSUserInfo user) {
			Logger.v(TAG, "onUserInfoComplete", "userInfo : " + user);
			waitting.setVisibility(View.VISIBLE);
			
			//TODO 绑定
			bind(user);
		}
	}
	
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        Logger.v(TAG, "onActivityResult", "resultCode : " + resultCode);
        
        if (requestCode != 0 && data != null){
        	SNSLoginManager.onActivityResult(this, requestCode, resultCode, data);
        }else{
        	waitting.setVisibility(View.VISIBLE);
        }
    }
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.left_buton:
			finish();
			break;
		case R.id.bind_QQ_check_box:
			changeBindState(SNSLoginManager.TENCENT_QQ_TYPE);
			break;
		case R.id.bind_weixin_check_box:
			changeBindState(SNSLoginManager.TENCENT_WEIXIN_TYPE);
			break;
		case R.id.bind_sina_check_box:
			changeBindState(SNSLoginManager.SINA_WEIBO_TYPE);
			break;
		default:
			break;
		}
		
	}
	
}
