package com.bccv.bangyangapp.ui.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bccv.bangyangapp.R;
import com.bccv.bangyangapp.ui.activity.MainActivity;
import com.bccv.bangyangapp.ui.adapter.TopHotPagerAdapter;
import com.bccv.bangyangapp.ui.view.HotFreshChildPage;

/**
 * 最新最热
 * 
 * @author liukai
 * 
 * @version 2015-3-27 上午10:23:03
 */
public class TopHotFramgent extends PrimaryFramgent implements OnClickListener,
		OnPageChangeListener {

	private static final String TAG = "HomePageFramgent";

	private static final int HOT_INDEX = 0;
	private static final int FRESH_INDEX = 1;

	private View mView;
	private View title, titleMenuBtn, titleRightBtn;
	private View hotBtn, freshBtn;
	private TextView hotTv, freshTv;
	private ViewPager viewPager;

	private TopHotPagerAdapter mAdapter;
	private ArrayList<HotFreshChildPage> pageList;

	private MainActivity mActivity;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mActivity = (MainActivity) getActivity();
		// TODO 初始化视图
		if (mView != null) {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
		} else {
			mView = inflater.inflate(R.layout.fragment_tophot, null);
			
			title = mView.findViewById(R.id.tophot_title);
			titleMenuBtn = title.findViewById(R.id.common_title_menu_btn);
			titleRightBtn = title.findViewById(R.id.common_title_right_btn);
			hotBtn = title.findViewById(R.id.hot_rl);
			freshBtn = title.findViewById(R.id.fresh_rl);
			hotTv = (TextView) title.findViewById(R.id.hot_tv);
			freshTv = (TextView) title.findViewById(R.id.fresh_tv);
			
			titleMenuBtn.setOnClickListener(mActivity);
			titleRightBtn.setOnClickListener(mActivity);
			hotBtn.setOnClickListener(this);
			freshBtn.setOnClickListener(this);
			
			viewPager = (ViewPager) mView.findViewById(R.id.tophot_vp);
			
			initChilds();
			viewPager.setOnPageChangeListener(this);
			mAdapter = new TopHotPagerAdapter(pageList);
			viewPager.setAdapter(mAdapter);
		}

		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// TODO 初始化数据
	}

	private void initChilds() {
		// 初始化各个孩子fragment;
		// cate 查询分类id（0 最热 1 最新）
		pageList = new ArrayList<HotFreshChildPage>();
		for (int i = 0; i < 2; i++) {
			HotFreshChildPage hotFreshChildPage = new HotFreshChildPage(mActivity);
			switch (i) {
			case 0:// 最新
				hotFreshChildPage.setCate(HotFreshChildPage.HOT_TYPE);
				break;
			case 1:// 热门
				hotFreshChildPage.setCate(HotFreshChildPage.FRESH_TYPE);
				break;
			default:
				break;
			}

			pageList.add(hotFreshChildPage);
		}
	}

	private void changePageTag(int page) {
		switch (page) {
		case HOT_INDEX:
			hotBtn.setBackgroundResource(R.drawable.change_left_select);
			hotTv.setTextColor(0Xffffffff);
			freshBtn.setBackgroundResource(R.drawable.change_right);
			freshTv.setTextColor(0X4cffffff);
			break;
		case FRESH_INDEX:
			hotBtn.setBackgroundResource(R.drawable.change_left);
			hotTv.setTextColor(0X4cffffff);
			freshBtn.setBackgroundResource(R.drawable.change_right_select);
			freshTv.setTextColor(0Xffffffff);
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.hot_rl:
			viewPager.setCurrentItem(HOT_INDEX);
			break;
		case R.id.fresh_rl:
			viewPager.setCurrentItem(FRESH_INDEX);
			break;

		default:
			break;
		}
	}

	@Override
	public void onPageSelected(int position) {
		changePageTag(position);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	@Override
	public void onMenuTransform(float percentOpen) {
		// TODO Auto-generated method stub
		title.setAlpha(percentOpen);
	}

	@Override
	public void onMenuOpened() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMenuClosed() {
		// TODO Auto-generated method stub

	}

//	@Override
//	public void onDetach() {
//		super.onDetach();
//		try {
//			Field childFragmentManager = Fragment.class
//					.getDeclaredField("mChildFragmentManager");
//			childFragmentManager.setAccessible(true);
//			childFragmentManager.set(this, null);
//
//		} catch (NoSuchFieldException e) {
//			throw new RuntimeException(e);
//		} catch (IllegalAccessException e) {
//			throw new RuntimeException(e);
//		}
//
//	}

}
