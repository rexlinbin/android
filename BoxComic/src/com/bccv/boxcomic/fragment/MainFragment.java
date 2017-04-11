package com.bccv.boxcomic.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.activity.ChannelActivity;
import com.bccv.boxcomic.activity.UserCenterActivity;
import com.bccv.boxcomic.adapter.HomeFragmentPagerAdapter;

import com.bccv.boxcomic.sns.UserInfoManager;
import com.bccv.boxcomic.tool.AppManager;
import com.bccv.boxcomic.tool.RoundedImageView;
import com.bccv.boxcomic.tool.GlobalParams;
import com.nostra13.universalimageloader.core.ImageLoader;

@SuppressLint("NewApi")
public class MainFragment extends FragmentActivity implements OnClickListener {
	private ViewPager mViewPager;
	private ImageView hotIm, updateIm, BookIm;
	// 全部频道和个人中心
	private RelativeLayout LeftIm;
	private RelativeLayout rightIm;
	private RoundedImageView userImageView;
	private FragmentManager fragmentManager;
	private MainChildFragment ChildFragment;
	private ReadBookFragment BookChild;
	private HomeFragmentPagerAdapter homeFragmentPagerAdapter;
	private ArrayList<Fragment> fragmentList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub

		setContentView(R.layout.fragment_main);
		AppManager.getAppManager().addActivity(this);
		LeftIm = (RelativeLayout) findViewById(R.id.frament_main_leftIm);
		rightIm = (RelativeLayout) findViewById(R.id.frament_main_rightIm);
		userImageView = (RoundedImageView) findViewById(R.id.frament_main_userIm);
		mViewPager = (ViewPager) findViewById(R.id.fragment_main_viewpager);
		// 全部频道
		LeftIm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent aIntent = new Intent(MainFragment.this,
						ChannelActivity.class);
				startActivity(aIntent);

			}
		});
		// 个人中心
		rightIm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent aIntent = new Intent(MainFragment.this,
						UserCenterActivity.class);
				startActivity(aIntent);
			}
		});

		hotIm = (ImageView) findViewById(R.id.frament_main_hot);
		updateIm = (ImageView) findViewById(R.id.frament_main_fresh);
	
		BookIm = (ImageView) findViewById(R.id.frament_main_book);
		if(GlobalParams.hasBook){
			BookIm.setVisibility(View.VISIBLE);			
			
		}else{
			
			BookIm.setVisibility(View.GONE);
		}
		
		
		
		hotIm.setOnClickListener(this);
		updateIm.setOnClickListener(this);
		BookIm.setOnClickListener(this);
		setDefaultFragment();

	}

	private void setDefaultFragment() {
		// TODO Auto-generated method stub
		fragmentManager = getSupportFragmentManager();

		fragmentList = new ArrayList<Fragment>();

		if(GlobalParams.hasBook){
			for (int i = 0; i < 3; i++) {
				ChildFragment = new MainChildFragment();
				BookChild = new ReadBookFragment();
				switch (i) {
				case 0:
					ChildFragment.setType(2);
					fragmentList.add(ChildFragment);
					break;
				case 1:
					ChildFragment.setType(1);
					fragmentList.add(ChildFragment);
					break;
				case 2:

					fragmentList.add(BookChild);
					break;
				default:
					break;
				}

			}
			
		}else{
			for (int i = 0; i < 2; i++) {
				ChildFragment = new MainChildFragment();

				switch (i) {
				case 0:
					ChildFragment.setType(2);
					break;
				case 1:
					ChildFragment.setType(1);
				default:
					break;
				}

				fragmentList.add(ChildFragment);
			}
		}
		
		
		

		homeFragmentPagerAdapter = new HomeFragmentPagerAdapter(
				fragmentManager, fragmentList);
		mViewPager.setAdapter(homeFragmentPagerAdapter);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mViewPager.setCurrentItem(0);
		mViewPager.setOffscreenPageLimit(2);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {

		case R.id.frament_main_hot:
			hotIm.setBackgroundResource(R.drawable.remen_select);
			updateIm.setBackgroundResource(R.drawable.zuijin);
			BookIm.setBackgroundResource(R.drawable.xiaoshuo);
			mViewPager.setCurrentItem(1);
			break;
		case R.id.frament_main_fresh:
			hotIm.setBackgroundResource(R.drawable.remen);
			updateIm.setBackgroundResource(R.drawable.zuijin_select);
			BookIm.setBackgroundResource(R.drawable.xiaoshuo);
			mViewPager.setCurrentItem(0);
			break;

		case R.id.frament_main_book:

			BookIm.setBackgroundResource(R.drawable.xiaoshuo_select);
			updateIm.setBackgroundResource(R.drawable.zuijin);
			hotIm.setBackgroundResource(R.drawable.remen);
			mViewPager.setCurrentItem(3);
			break;
		default:
			break;

		}

	}

	private void resetTags() {
		// TODO Auto-generated method stub
		updateIm.setBackgroundResource(R.drawable.zuijin);
		hotIm.setBackgroundResource(R.drawable.remen);
		BookIm.setBackgroundResource(R.drawable.xiaoshuo);
	}

	private class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int position) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			// 滑动ViewPager 回调 处理
			resetTags();
			switch (position) {
			case 1:
				hotIm.setBackgroundResource(R.drawable.remen_select);

				break;
			case 0:

				updateIm.setBackgroundResource(R.drawable.zuijin_select);

				break;
			case 2:

				BookIm.setBackgroundResource(R.drawable.xiaoshuo_select);
				break;
			default:
				break;
			}
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(UserInfoManager.getUserIcon(), userImageView,
				GlobalParams.headOptions);

	}
}
