package com.bccv.meitu.ui.activity;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.bccv.meitu.R;
import com.bccv.meitu.ui.fragment.ProAlbumFragment;
import com.bccv.meitu.ui.fragment.ProCommentFragment;
import com.bccv.meitu.utils.SystemUtil;
import com.bccv.meitu.view.PullToZoomListView;
import com.bccv.meitu.view.PullToZoomListView2;

public class ProZoneActivity2 extends BaseFragmentActivity implements
		OnClickListener {
	private PullToZoomListView2 mListView;
	private TextView album_name;
	private LinearLayout ll_pro_left;
	private LinearLayout ll_pro_right;
	private ViewPager viewPager;
	private Fragment view1, view2;// 各个页卡
	private List<Fragment> views;// Tab页面列表
	private int currIndex = 0;// 当前页卡编号
	private View view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SystemUtil.SetFullScreen(this);
		setContentView(R.layout.activity_promulgator_zone2);
		album_name = (TextView) findViewById(R.id.album_name);
		mListView = (PullToZoomListView2) findViewById(R.id.list_view);
		view = LayoutInflater.from(this).inflate(
				R.layout.promulgator_content_view, null);
		viewPager = (ViewPager) view.findViewById(R.id.pro_viewpager);
		initViewPager();
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
				params.height = view.getMeasuredHeight();
				viewPager.setLayoutParams(params);
			}
		});
		mListView.addFooterView(view);
		mListView.setAdapter(new MyListAdapter());
		// 左边专辑按钮
		// ll_pro_left.setOnClickListener(this);
		// 右边留言按钮
		// ll_pro_right.setOnClickListener(this);
	}

	// BroadcastReceiver receiver = new BroadcastReceiver() {
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// mListView.addFooterView(view);
	// mListView.setAdapter(new MyListAdapter());
	// }
	// };
	// 初始化页卡内容
	private void initViewPager() {
		views = new ArrayList<Fragment>();
		view1 = new ProAlbumFragment();
		view2 = new ProCommentFragment();
		views.add(view1);
		views.add(view2);
		viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new PageChangeLisener());
	}

	class MyListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 1;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(ProZoneActivity2.this,
					R.layout.promulgator_list_item, null);
			return view;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_pro_left:// 左边专辑按钮

			break;
		case R.id.ll_pro_right:// 右边留言按钮

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
			// Animation animation = new TranslateAnimation(one * currIndex, one
			// * arg0, 0, 0);
			// currIndex = arg0;
			// animation.setFillAfter(true);
			// animation.setDuration(300);
			// imageView.startAnimation(animation);
		}

	}

}
