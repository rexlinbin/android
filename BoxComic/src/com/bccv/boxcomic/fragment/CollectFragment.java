package com.bccv.boxcomic.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;


import com.bccv.boxcomic.R;

import com.bccv.boxcomic.adapter.HomeFragmentPagerAdapter;
import com.bccv.boxcomic.tool.AppManager;

@SuppressLint("NewApi")
public class CollectFragment extends FragmentActivity {
	private ViewPager mViewPager;
	private FragmentManager fragmentManager;
	RelativeLayout collectRe, passRe, bookMarkRe;
	private HomeFragmentPagerAdapter homeFragmentPagerAdapter;
	private ArrayList<Fragment> fragmentList;
	private String tag = "CollectFragment";
	private LocalFramgent localFramgent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_collect);
		AppManager.getAppManager().addActivity(this);
		collectRe = (RelativeLayout) findViewById(R.id.activity_collect_collect);
		passRe = (RelativeLayout) findViewById(R.id.activity_collect_history);
		bookMarkRe = (RelativeLayout) findViewById(R.id.activity_collect_bookmark);
		mViewPager = (ViewPager) findViewById(R.id.home_vp);

		collectRe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mViewPager.setCurrentItem(0);
			}
		});
		passRe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mViewPager.setCurrentItem(1);
			}
		});
		bookMarkRe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mViewPager.setCurrentItem(2);
			}
		});

		

		initChilds();

		
	}

	private void initChilds() {
		// TODO Auto-generated method stub

		fragmentManager = getSupportFragmentManager();
		fragmentList = new ArrayList<Fragment>();

		
		for (int i = 0; i < 3; i++) {
			CollectChildFramgent homeChildFramgent = new CollectChildFramgent();
			localFramgent = new LocalFramgent();
			switch (i) {
			

			case 0:// 收藏
				homeChildFramgent.setCate(CollectChildFramgent.COLLECT);
				fragmentList.add(homeChildFramgent);
				break;
			case 1:// 本地

				fragmentList.add(localFramgent);

				break;
			case 2:// 历史
				homeChildFramgent.setCate(CollectChildFramgent.PASS);
				fragmentList.add(homeChildFramgent);
				break;

			default:
				break;
			}

		}
		homeFragmentPagerAdapter = new HomeFragmentPagerAdapter(
				fragmentManager, fragmentList);
		mViewPager.setAdapter(homeFragmentPagerAdapter);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mViewPager.setCurrentItem(1);
		mViewPager.setOffscreenPageLimit(2);
	}

	private void resetTags() {
		// TODO Auto-generated method stub

		collectRe.setBackgroundResource(R.drawable.col_tab_left);

		passRe.setBackgroundResource(R.drawable.col_tab_center);

		bookMarkRe.setBackgroundResource(R.drawable.col_tab_right);

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
			case 0:
				collectRe.setBackgroundResource(R.drawable.col_tab_left_select);

				break;
			case 1:
				passRe.setBackgroundResource(R.drawable.col_tab_center_select);

				break;
			case 2:
				bookMarkRe
						.setBackgroundResource(R.drawable.col_tab_right_select);

				break;

			default:
				break;
			}
		}

	}

	// @Override
	// public void onResume() {
	//
	// if (mViewPager.getCurrentItem() == COLLECT_SHOU) {
	// mViewPager.setCurrentItem(COLLECT_HISTORY);
	// }
	// CollectChildFramgent fragment = (CollectChildFramgent) fragmentList
	// .get(COLLECT_SHOU);
	// fragment.reset();
	//
	// super.onResume();
	// }

}
