package com.bccv.zhuiyingzhihanju.activity;

import java.util.ArrayList;
import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.fragment.CommentFragment;
import com.bccv.zhuiyingzhihanju.fragment.MsgFragment;
import com.tendcloud.tenddata.TCAgent;
import com.utils.tools.BaseFragmentActivity;
import com.utils.tools.DimensionPixelUtil;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MoreListActivity extends BaseFragmentActivity {
	private void tcStart() {
		TCAgent.onPageStart(getApplicationContext(), "MoreListActivity");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		TCAgent.onPageEnd(getApplicationContext(), "MoreListActivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_morelist);
		tcStart();
		ImageButton backBtn = (ImageButton) findViewById(R.id.titel_back);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		InitImage(2);
		InitTextView();
		InitViewPager();

	}

	// viewpager+fragment
	private int currIndex;// 当前页卡编号
	private int bmpW;// 横线图片宽度
	private int offset;// 图片移动的偏移量
	private ImageView imageView;
	private ViewPager mPager;
	private ArrayList<Fragment> fragmentList;
	private TextView view1, view2, view3, view4, view5;
	private List<TextView> textList;

	/*
	 * 初始化ViewPager 在image后调用
	 */
	public void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.msg_viewpager);
		fragmentList = new ArrayList<Fragment>();
		MsgFragment hjFragment = new MsgFragment();

		fragmentList.add(hjFragment);

		CommentFragment hzFragment = new CommentFragment();

		fragmentList.add(hzFragment);

		// 给ViewPager设置适配器
		mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
		mPager.setCurrentItem(0);// 设置当前显示标签页为第一页
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());// 页面变化时的监听器
		mPager.setOffscreenPageLimit(5);

	}

	/*
	 * 初始化标签名
	 */
	public void InitTextView() {
		textList = new ArrayList<>();
		view2 = (TextView) findViewById(R.id.comment_textView);
		view1 = (TextView) findViewById(R.id.msg_textView);

		view1.setVisibility(View.VISIBLE);
		view2.setVisibility(View.VISIBLE);

		view1.setOnClickListener(new txListener(0));
		textList.add(view1);
		view2.setOnClickListener(new txListener(1));
		textList.add(view2);

	}

	public class txListener implements View.OnClickListener {
		private int index = 0;

		public txListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mPager.setCurrentItem(index);

		}
	}

	/*
	 * 初始化图片的位移像素 num为页面数
	 */
	public void InitImage(int num) {

		imageView = (ImageView) findViewById(R.id.sp_cursor);
		imageView.setVisibility(View.VISIBLE);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.line).getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		offset = (int) ((DimensionPixelUtil.dip2px(getApplicationContext(), 150) / num - bmpW) / 2);

		// imgageview设置平移，使下划线平移到初始位置（平移一个offset）
		Animation animation = new TranslateAnimation(0, offset, 0, 0);// 平移动画
		animation.setFillAfter(true);// 动画终止时停留在最后一帧，不然会回到没有执行前的状态
		animation.setDuration(0);// 动画持续时间0.2秒
		imageView.clearAnimation();
		imageView.startAnimation(animation);// 是用ImageView来显示动画的
		// Matrix matrix = new Matrix();
		// matrix.postTranslate(offset, 0);
		// imageView.setImageMatrix(matrix);
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		private int one = offset * 2 + bmpW;// 两个相邻页面的偏移量

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub

			textList.get(currIndex).setTextSize(14);
			Animation animation = new TranslateAnimation(currIndex * one + offset, arg0 * one + offset, 0, 0);// 平移动画
			currIndex = arg0;
			animation.setFillAfter(true);// 动画终止时停留在最后一帧，不然会回到没有执行前的状态
			animation.setDuration(200);// 动画持续时间0.2秒
			imageView.clearAnimation();
			imageView.startAnimation(animation);// 是用ImageView来显示动画的

		}
	}

	public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
		ArrayList<Fragment> list;

		public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
			super(fm);
			this.list = list;

		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Fragment getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			return super.instantiateItem(container, position);
		}
	}

}
