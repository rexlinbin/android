package com.bccv.meitu.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bccv.meitu.R;
import com.bccv.meitu.ui.fragment.ProAlbumFragment;
import com.bccv.meitu.ui.fragment.ProCommentFragment;
import com.bccv.meitu.utils.SystemUtil;

public class ProZoneActivity3 extends FragmentActivity implements OnClickListener{
	private ScrollView mScrollView;
	private TextView album_name;
	private ImageView iv_zoom;
	private LinearLayout ll_pro_left;
	private LinearLayout ll_pro_right;
	private ViewPager viewPager;
	private Fragment view1, view2;// 各个页卡
	private List<Fragment> views;// Tab页面列表
	private int currIndex = 0;// 当前页卡编号
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SystemUtil.SetFullScreen(this);
		setContentView(R.layout.activity_promulgator_zone);
		album_name = (TextView) findViewById(R.id.album_name);
		iv_zoom = (ImageView) findViewById(R.id.iv_zoom);
		mScrollView = (ScrollView) findViewById(R.id.scroll_view);
		ll_pro_left = (LinearLayout) findViewById(R.id.ll_pro_left);
		ll_pro_right = (LinearLayout) findViewById(R.id.ll_pro_right);
		viewPager = (ViewPager) findViewById(R.id.pro_viewpager);
		initViewPager();
		getPagerHight();
		//左边专辑按钮
		ll_pro_left.setOnClickListener(this);
		//右边留言按钮
		ll_pro_right.setOnClickListener(this);
		//大图
		iv_zoom.setOnClickListener(this);
	}
	
	private void getPagerHight(){
		final int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		final int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		viewPager.measure(w, h);
		ViewTreeObserver vto = viewPager.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				viewPager.getViewTreeObserver().removeGlobalOnLayoutListener(
						this);
				View view = viewPager.getChildAt(viewPager.getCurrentItem());
				view.measure(w, h);
				LayoutParams params = new LayoutParams(
						android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				params.height = view.getMeasuredHeight()+10;
				viewPager.setLayoutParams(params);
			}
		});
	}
	
	// 初始化页卡内容
	private void initViewPager() {
		views = new ArrayList<Fragment>();
		view1 = new ProAlbumFragment();
		view2 = new ProCommentFragment();
		views.add(view2);
		views.add(view1);
		viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new PageChangeLisener());
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_pro_left://左边专辑按钮
			
			break;
		case R.id.ll_pro_right://右边留言按钮

			break;
		case R.id.iv_zoom:
			
			break;
		default:
			break;
		}
	}
	
	class ViewPagerAdapter extends FragmentPagerAdapter {

		private List<Fragment> list_views;

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
			list_views = views;
		}

		@Override
		public Fragment getItem(int arg0) {
			return list_views.get(arg0);
		}

		@Override
		public int getCount() {
			return list_views.size();
		}

	}

	class PageChangeLisener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
//			Animation animation = new TranslateAnimation(one * currIndex, one
//					* arg0, 0, 0);
//			currIndex = arg0;
//			animation.setFillAfter(true);
//			animation.setDuration(300);
//			imageView.startAnimation(animation);
			getPagerHight();
			viewPager.invalidate();
		}
	}
}
