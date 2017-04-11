package com.bccv.meitu.ui.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {

	private ArrayList<Fragment> fragmentList;
	
	public HomeFragmentPagerAdapter(FragmentManager fm,ArrayList<Fragment> fragmentList) {
		super(fm);
		this.fragmentList = fragmentList;
	}

	@Override
	public Fragment getItem(int position) {
		
		return fragmentList == null? null : fragmentList.get(position);
	}

	@Override
	public int getCount() {
		return fragmentList == null ? 0 : fragmentList.size();
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position)
	{
		return super.instantiateItem(container, position);
	}

	
	public void removeViews(){
		fragmentList = null;
	}
	
}
