package com.bccv.meitu.ui.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import com.bccv.meitu.ApplicationManager;
import com.bccv.meitu.R;
import com.bccv.meitu.ui.adapter.HomeFragmentPagerAdapter;
import com.bccv.meitu.ui.fragment.HomeChildFramgent;
import com.bccv.meitu.utils.ExitUtils;
import com.bccv.meitu.view.MenuPopwindow;

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
	
	private MenuPopwindow menuPopwindow;
	
	private static final int HOME_ATTENTION = 0;
	private static final int HOME_HOT = 1;
	private static final int HOME_FRESH = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		initView();
	}
	
	private void initView(){
		
		home_root = findViewById(R.id.home_root);
		
		fragmentManager = getSupportFragmentManager();
		
		home_top_title = findViewById(R.id.home_top_title);
		left_buton = home_top_title.findViewById(R.id.left_buton);
		right_buton = home_top_title.findViewById(R.id.right_buton);
		home_attention_btn = home_top_title.findViewById(R.id.home_attention_btn);
		home_hot_btn = home_top_title.findViewById(R.id.home_hot_btn);
		home_fresh_btn = home_top_title.findViewById(R.id.home_fresh_btn);
		
		left_buton.setOnClickListener(this);
		right_buton.setOnClickListener(this);
		home_attention_btn.setOnClickListener(this);
		home_hot_btn.setOnClickListener(this);
		home_fresh_btn.setOnClickListener(this);
		
		mViewPager = (ViewPager) findViewById(R.id.home_vp);
		initChilds();
		homeFragmentPagerAdapter = new HomeFragmentPagerAdapter(fragmentManager, fragmentList);
		mViewPager.setAdapter(homeFragmentPagerAdapter);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mViewPager.setCurrentItem(1);
		mViewPager.setOffscreenPageLimit(3);
		
	}
	
	private void initChilds()
	{
		// 初始化各个孩子fragment;
		// cate 查询分类id（0 关注，1热门，2最新）
		fragmentList = new ArrayList<Fragment>();
		for(int i = 0;i < 3;i++)
		{
			HomeChildFramgent homeChildFramgent = new HomeChildFramgent();
			switch (i) {
			case 0://关注
				homeChildFramgent.setCate(HomeChildFramgent.ATTENTION);
				break;
			case 1://热门
				homeChildFramgent.setCate(HomeChildFramgent.HOT);
				break;
			case 2://最新
				homeChildFramgent.setCate(HomeChildFramgent.FRESH);
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
			//TODO 点击title 右边标签
			
			break;
		case R.id.home_attention_btn: //关注
			mViewPager.setCurrentItem(0);
			break;
		case R.id.home_hot_btn: // 热门
			mViewPager.setCurrentItem(1);
			break;
		case R.id.home_fresh_btn: // 最新
			mViewPager.setCurrentItem(2);
			break;

		default:
			break;
		}
		
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
	
}
