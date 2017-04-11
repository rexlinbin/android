package com.bccv.threedimensionalworld.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.adapter.GralleryAdapter;
import com.bccv.threedimensionalworld.adapter.InfinitePagerAdapter;
import com.bccv.threedimensionalworld.model.PictureBean;
import com.bccv.threedimensionalworld.view.InfiniteViewPager;

public class GalleryActivity extends Activity{
	private InfiniteViewPager gallery;
	private GralleryAdapter adapter;
	private List<PictureBean>data;
	private int position;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grallery);
		
		
		position = getIntent().getIntExtra("position", -1);
		gallery=(InfiniteViewPager) findViewById(R.id.activity_grallery);
		data=new ArrayList<PictureBean>();
		data = (List<PictureBean>) getIntent().getSerializableExtra("list");
		
	
		
		
		adapter=new GralleryAdapter(data, this);
//		gallery.setAdapter(adapter);
//		gallery.setSelection(position);
		
		
	    PagerAdapter wrappedAdapter = new InfinitePagerAdapter(adapter);

   
	    gallery.setAdapter(wrappedAdapter);
//   gallery.setPageMargin(-200);
//    gallery.setOffscreenPageLimit(data.size()-1);
		gallery.setCurrentItem(position);
		
		
		
		
		
		
		
		
		
	}
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}


}
