package com.bccv.meitu.ui.activity;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;

import com.bccv.meitu.ApplicationManager;
import com.bccv.meitu.R;
import com.bccv.meitu.api.NetWorkAPI;
import com.bccv.meitu.model.VersionInfo;
import com.bccv.meitu.network.HttpCallback;
import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.sns.UserInfoManager;
import com.bccv.meitu.ui.adapter.HomeFragmentPagerAdapter;
import com.bccv.meitu.ui.fragment.HomeChildFramgent;
import com.bccv.meitu.utils.ExitUtils;
import com.bccv.meitu.utils.Logger;
import com.bccv.meitu.utils.RCUtil;
import com.bccv.meitu.utils.YuseUtil;
import com.bccv.meitu.view.MenuPopwindow;
import com.nostra13.universalimageloader.utils.ImageLoaderUtil;

public class MainActivity extends BaseFragmentActivity  {
	
	private ViewPager mViewPager;
	private HomeFragmentPagerAdapter homeFragmentPagerAdapter;
	private FragmentManager fragmentManager;
	private ArrayList<Fragment> fragmentList;
	
	private View home_root;
	private View home_top_title;
	
	private View left_buton;
	private View right_buton;
	private View home_attention_btn;
	private View home_hot_btn;
	private View home_fresh_btn;
	
	private View msg_remind;
	private ImageView user_icon;
	
	
	private MenuPopwindow menuPopwindow;
	
	private static final int HOME_FRESH = 0;
	private static final int HOME_HOT = 1;
	private static final int HOME_ATTENTION = 2;
	
	private IntentFilter rcBrocastIntentFilter = null;
	private BroadcastReceiver rcBrocastReceiver = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		initView();
		registerBrocast();
	}

	private void initView(){
		
		home_root = findViewById(R.id.home_root);
		
		fragmentManager = getSupportFragmentManager();
		msg_remind = findViewById(R.id.msg_remind);
		
		home_top_title = findViewById(R.id.home_top_title);
		left_buton = home_top_title.findViewById(R.id.left_buton);
		right_buton = home_top_title.findViewById(R.id.right_buton);
		home_fresh_btn = home_top_title.findViewById(R.id.home_fresh_btn);
		home_hot_btn = home_top_title.findViewById(R.id.home_hot_btn);
		home_attention_btn = home_top_title.findViewById(R.id.home_attention_btn);
		
		user_icon = (ImageView)findViewById(R.id.user_icon);
		
		if(UserInfoManager.isLogin()){
			ImageLoaderUtil.getInstance(mContext).displayImage(UserInfoManager.getUserIcon(),
					user_icon, ImageLoaderUtil.getUserIconImageOptions());
		}else{
			user_icon.setImageBitmap(null);
		}
		
		left_buton.setOnClickListener(this);
		right_buton.setOnClickListener(this);
		home_attention_btn.setOnClickListener(this);
		home_hot_btn.setOnClickListener(this);
		home_fresh_btn.setOnClickListener(this);
		right_buton.setVisibility(View.VISIBLE);
		
		mViewPager = (ViewPager) findViewById(R.id.home_vp);
		initChilds();
		homeFragmentPagerAdapter = new HomeFragmentPagerAdapter(fragmentManager, fragmentList);
		mViewPager.setAdapter(homeFragmentPagerAdapter);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mViewPager.setCurrentItem(0);
		mViewPager.setOffscreenPageLimit(3);
		report();//上报并获取版本更新信息
	}
	
	private void registerBrocast(){
		if(rcBrocastIntentFilter==null){
			rcBrocastIntentFilter = new IntentFilter();
			rcBrocastIntentFilter.addAction(RCUtil.RECEIVE_NOREAD_MESSAGE);
		}
		if(rcBrocastReceiver == null){
			rcBrocastReceiver = new BroadcastReceiver(){

				@Override
				public void onReceive(Context context, Intent intent) {
					
					if(RCUtil.RECEIVE_NOREAD_MESSAGE.equals(intent.getAction())){
						//TODO 更新图标
						msg_remind.setVisibility(View.VISIBLE);
					}
				}
			};
			mContext.registerReceiver(rcBrocastReceiver, rcBrocastIntentFilter);
		}
		
	}
	
	private void initChilds(){
		// 初始化各个孩子fragment;
		// cate 查询分类id（0 关注，1热门，2最新）
		fragmentList = new ArrayList<Fragment>();
		for(int i = 0;i < 3;i++)
		{
			HomeChildFramgent homeChildFramgent = new HomeChildFramgent();
			switch (i) {
			case 0://最新
				homeChildFramgent.setCate(HomeChildFramgent.FRESH);
				break;
			case 1://热门
				homeChildFramgent.setCate(HomeChildFramgent.HOT);
				break;
			case 2://关注
				homeChildFramgent.setCate(HomeChildFramgent.ATTENTION);
				break;
			default:
				break;
			}
			
			fragmentList.add(homeChildFramgent);
		}
	}
	
	private void resetTags(){
		home_attention_btn.setBackgroundResource(R.drawable.home_attention_btn);
		home_hot_btn.setBackgroundResource(R.drawable.home_hot_btn);
		home_fresh_btn.setBackgroundResource(R.drawable.home_fresh_btn);
	}
	
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.left_buton: //左按钮
			if(menuPopwindow==null){
				menuPopwindow = new MenuPopwindow(this);
			}
			menuPopwindow.show(home_root);
			
			// download test
			
//			String url = "http://dldir1.qq.com/weixin/android/weixin540android480.apk";
//			String name = "weixin540android480.apk";
//			int ver = 1;
//			DownLoadAPI.downLoadApk(url, name, "com.chatmm.weixin", 1, true, true);
//			
//			DownLoadAPI.downLoadFile("http://b.hiphotos.baidu.com/image/pic/item/9e3df8dcd100baa184040b054510b912c8fc2e67.jpg", "9e3df8dcd100baa184040b054510b912c8fc2e67.jpg", true);
			
			break;
		case R.id.right_buton: // 右按钮
			
//			if(UserInfoManager.isLogin()){
				Intent intent2 = new Intent(this, UserActivity.class);
				startActivity(intent2);
//			}else{
//				Intent intent = new Intent(this, LoginActivity.class);
//				startActivity(intent);
//			}
			
			break;
		case R.id.home_attention_btn: //关注
			
//			if(UserInfoManager.isLogin()){
				mViewPager.setCurrentItem(3);
//			}else{
//				Intent intent = new Intent(mContext, LoginActivity.class);
//				startActivity(intent);
//			}
			
			break;
		case R.id.home_hot_btn: // 热门
			mViewPager.setCurrentItem(1);
			break;
		case R.id.home_fresh_btn: // 最新
			mViewPager.setCurrentItem(0);
			break;

		default:
			break;
		}
		
	}
	
	@Override
	protected void onResume() {

		if(RCUtil.getTotalUnreadCount()!=0){
			msg_remind.setVisibility(View.VISIBLE);
		}else{
			msg_remind.setVisibility(View.GONE);
		}
		
		if(UserInfoManager.isLogin()){
			ImageLoaderUtil.getInstance(mContext).displayImage(UserInfoManager.getUserIcon(),
					user_icon, ImageLoaderUtil.getUserIconImageOptions());
		}else{
//			if(mViewPager.getCurrentItem()==HOME_ATTENTION){
//				mViewPager.setCurrentItem(HOME_HOT);
//			}
//			HomeChildFramgent fragment = (HomeChildFramgent) fragmentList.get(HOME_ATTENTION);
//			fragment.reset();
			user_icon.setImageBitmap(null);
		}
		super.onResume();
	}
	
	@Override
	public void onBackPressed() {
		if(ExitUtils.isExit(this)){
			super.onBackPressed();
			ApplicationManager.getInstance().exitSystem();
		}
	}
	
	private class MyOnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int position) {}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}

		@Override
		public void onPageSelected(int position) {
			// 滑动ViewPager 回调  处理
			
//			if(position == HOME_ATTENTION && (!UserInfoManager.isLogin())){
//				mViewPager.setCurrentItem(1);
//				Intent intent = new Intent(mContext, LoginActivity.class);
//				startActivity(intent);
//				return;
//			}
			
			resetTags();
			switch (position) {
			case HOME_ATTENTION:
				home_attention_btn.setBackgroundResource(R.drawable.home_attention_btn_select);
				break;
			case HOME_HOT:
				home_hot_btn.setBackgroundResource(R.drawable.home_hot_btn_select);
				break;
			case HOME_FRESH:
				home_fresh_btn.setBackgroundResource(R.drawable.home_fresh_btn_select);
				break;

			default:
				break;
			}
		}
	}
	
	
	private void report(){
		NetWorkAPI.report(mContext, new HttpCallback() {
			
			@Override
			public void onResult(NetResBean response) {
				Logger.v(TAG, " report onResult ", response.success);
				if (response.success && response instanceof VersionInfo) {
					VersionInfo dataBean = (VersionInfo)response;
					YuseUtil.showUpdateDialog(dataBean, MainActivity.this, false);
				}
			}
			
			@Override
			public void onError(String errorMsg) {
				Logger.v(TAG, " report onError ", errorMsg+"");
			}
			
			@Override
			public void onCancel() {}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if(rcBrocastReceiver!=null){
			mContext.unregisterReceiver(rcBrocastReceiver);
			rcBrocastReceiver = null;
			rcBrocastIntentFilter = null;
		}
		
	}
	
}
