package com.bccv.bangyangapp.ui.view;

import com.bccv.bangyangapp.ui.utils.ViewPagerScroller;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

public class HomePageViewPager extends ViewPager {

	public ViewPagerScroller mViewPagerScroller;
	
	public HomePageViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public HomePageViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public boolean setItemByUser = false;
	public void setCurrentItemByUser(int item) {
		setItemByUser = true;
		setCurrentItem(item);
		setItemByUser = false;
	}

}
