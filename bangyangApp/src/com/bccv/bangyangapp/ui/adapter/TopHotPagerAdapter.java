package com.bccv.bangyangapp.ui.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bccv.bangyangapp.ui.view.HotFreshChildPage;

public class TopHotPagerAdapter extends PagerAdapter {

	private ArrayList<HotFreshChildPage> pageList;
	
	public TopHotPagerAdapter(ArrayList<HotFreshChildPage> pageList){
		this.pageList = pageList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pageList==null?0:pageList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		View view = pageList.get(position).getView();
		pageList.get(position).onCreate();
		container.addView(view);
		
		return view;
	}
	
    @Override  
    public void destroyItem(ViewGroup container, int position,  
            Object object)  
    {  
        container.removeView(pageList.get(position).getView());  
    }  
    
}
