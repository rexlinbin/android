package com.bccv.strategy.ui.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;

import com.bccv.strategy.ui.view.SmoothImageView;
import com.bccv.strategy.ui.view.SmoothImageView.TransformListener;
import com.bccv.strategy.utils.ImageLoaderUtil;
import com.bccv.strategy.R;

public class ScreenShotActivity extends BaseActivity {
	ViewPager viewPager;
	ArrayList<View> listViews;
	boolean isStart;
	private int size = 0;
	private boolean isDefault = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isStart = true;
		setContentView(R.layout.activity_screenshot);
		ArrayList<String> mDatas = (ArrayList<String>) getIntent().getSerializableExtra("images");
		int mPosition = getIntent().getIntExtra("position", 0);
		int mLocationX = getIntent().getIntExtra("locationX", 0);
		int mLocationY = getIntent().getIntExtra("locationY", 0);
		int mWidth = getIntent().getIntExtra("width", 0);
		int mHeight = getIntent().getIntExtra("height", 0);

		if (mDatas != null && mDatas.size() > 0) {
			size = mDatas.size();
			isDefault = false;
		}else {
			size = 2;
			isDefault = true;
		}
		
		// setContentView(imageView);
		listViews = new ArrayList<View>();

		for (int i = 0; i < size; i++) {
			final SmoothImageView imageView = new SmoothImageView(this);
			imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
			imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
			imageView.setScaleType(ScaleType.FIT_CENTER);
			listViews.add(imageView);

			if (!isDefault) {
				ImageLoaderUtil.getInstance(mContext).displayImage(mDatas.get(i), imageView, ImageLoaderUtil.getAutoRotateImageOptions());
			}else {
				imageView.setImageResource(R.drawable.default_320568);
			}
//			ImageLoader.getInstance().displayImage(mDatas.get(i), imageView);
			if (mPosition == i) {
				imageView.transformIn();
			}

			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					imageView.transformOut();
					imageView.setOnTransformListener(new TransformListener() {
						@Override
						public void onTransformComplete(int mode) {
							finish();
							overridePendingTransition(R.anim.all, R.anim.al);
						}
					});

				}
			}); 
		}
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPager.setAdapter(new MyPageAdapter(listViews));
		viewPager.setCurrentItem(mPosition);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				if (isStart) {
					isStart = false;
				} else {
					for (View view : listViews) {
						SmoothImageView imageView = (SmoothImageView) view;
						if (imageView.getDrawable() == null) {
							imageView.setImageDrawable(getResources()
									.getDrawable(R.drawable.details_default));
						}
					}
				}

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {}
		});
	}
	

	private class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> list;

		private MyPageAdapter(ArrayList<View> listViews) {
			this.list = listViews;
		}

		@Override
		public void destroyItem(View view, int position, Object arg2) {
			ViewPager pViewPager = ((ViewPager) view);
			pViewPager.removeView(list.get(position));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object instantiateItem(View view, int position) {
			ViewPager pViewPager = ((ViewPager) view);
			pViewPager.addView(list.get(position));
			return list.get(position);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

	}

	
}
