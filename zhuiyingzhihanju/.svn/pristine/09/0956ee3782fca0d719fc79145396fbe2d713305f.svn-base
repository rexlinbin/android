package com.bccv.zhuiyingzhihanju.fragment;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Text;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.activity.MoreVideoActivity;
import com.bccv.zhuiyingzhihanju.activity.MovieSearchActivity;
import com.tendcloud.tenddata.TCAgent;
import com.utils.tools.BaseFragmentActivity;
import com.utils.tools.Callback;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FilterFragment extends BaseFragmentActivity {

	String isSelect;
	private Button SelectBtn;
	private EditText edit;
	private RelativeLayout titleSelect, sp_re;
	private LinearLayout title;
	private TextView titleName;
	private int RESULT=0;
	private void tcStart() {
		TCAgent.onPageStart(getApplicationContext(), "FilterFragment");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		TCAgent.onPageEnd(getApplicationContext(), "FilterFragment");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tcStart();
		setContentView(R.layout.fragment_special);

		isSelect = getIntent().getStringExtra("isSelect");
		SelectBtn = (Button) findViewById(R.id.main_selectBtn);
		SelectBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent aIntent = new Intent(FilterFragment.this, FilterFragment.class);

				aIntent.putExtra("isSelect", false);
				startActivity(aIntent);
			}
		});

		ImageButton collectBtn = (ImageButton) findViewById(R.id.title_search_edit);

		ImageButton DownBtn = (ImageButton) findViewById(R.id.title_search_btnDown);

		collectBtn.setVisibility(View.INVISIBLE);

		DownBtn.setVisibility(View.INVISIBLE);

		edit = (EditText) findViewById(R.id.main_search);
		edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent aIntent = new Intent(FilterFragment.this, MovieSearchActivity.class);

				startActivity(aIntent);
			}
		});

		titleSelect = (RelativeLayout) findViewById(R.id.title_search_layout);
		title = (LinearLayout) findViewById(R.id.title);
		titleName = (TextView) findViewById(R.id.titleName_textView);
		sp_re = (RelativeLayout) findViewById(R.id.sp_re);
		View sp_view = findViewById(R.id.sp_view);
		

		
		
		if (isSelect.equals("S")) {
			title.setVisibility(View.VISIBLE);
			title.setBackgroundColor(getResources().getColor(R.color.white));
			titleSelect.setVisibility(View.GONE);
		
			titleName.setText("筛选");
			titleName.setVisibility(View.VISIBLE);
			sp_re.setVisibility(View.GONE);
			sp_view.setVisibility(View.VISIBLE);
			ImageButton backBtn = (ImageButton) findViewById(R.id.titel_back);
			backBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});

		} 

		else {

			title.setVisibility(View.GONE);
			sp_re.setVisibility(View.VISIBLE);
		
			titleSelect.setVisibility(View.VISIBLE);
			sp_view.setVisibility(View.GONE);
		}

		InitImage(5);
		InitTextView();
		InitViewPager();

	}

	public void getData() {

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub

			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {

				return "true";
			}
		}.execute("");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getData();

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
		mPager = (ViewPager) findViewById(R.id.viewpager);
		fragmentList = new ArrayList<Fragment>();
		FilterInfoFragment hjFragment = new FilterInfoFragment();
		Bundle hjBundle = new Bundle();

		hjBundle.putString("type_id", "tv");
		hjFragment.setArguments(hjBundle);

		fragmentList.add(hjFragment);

		FilterInfoFragment hzFragment = new FilterInfoFragment();

		Bundle hzBundle = new Bundle();

		hzBundle.putString("type_id", "variety");
		hzFragment.setArguments(hzBundle);

		fragmentList.add(hzFragment);

		FilterInfoFragment tuijianFragment = new FilterInfoFragment();
		Bundle hyBundle = new Bundle();

		hyBundle.putString("type_id", "movie");
		tuijianFragment.setArguments(hyBundle);

		fragmentList.add(tuijianFragment);

		FilterInfoFragment newsFragment = new FilterInfoFragment();
		Bundle NewsBundle = new Bundle();

		NewsBundle.putString("type_id", "news");
		newsFragment.setArguments(NewsBundle);

		fragmentList.add(newsFragment);

		FilterInfoFragment musicFragment = new FilterInfoFragment();
		Bundle musicBundle = new Bundle();

		musicBundle.putString("type_id", "music");
		musicFragment.setArguments(musicBundle);
		fragmentList.add(musicFragment);

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
		view1 = (TextView) findViewById(R.id.hj_textView);
		view2 = (TextView) findViewById(R.id.hz_textView);
		view3 = (TextView) findViewById(R.id.hy_textView);
		view4 = (TextView) findViewById(R.id.news_textView);
		view5 = (TextView) findViewById(R.id.music_textView);

		view1.setSelected(true);

		view1.setVisibility(View.VISIBLE);
		view2.setVisibility(View.VISIBLE);
		view3.setVisibility(View.VISIBLE);
		view4.setVisibility(View.VISIBLE);
		view5.setVisibility(View.VISIBLE);
		view1.setTextSize(15);
		view2.setTextSize(14);
		view3.setTextSize(14);
		view4.setTextSize(14);
		view5.setTextSize(14);

		view1.setOnClickListener(new txListener(0));
		textList.add(view1);
		view2.setOnClickListener(new txListener(1));
		textList.add(view2);
		view3.setOnClickListener(new txListener(2));
		textList.add(view3);
		view4.setOnClickListener(new txListener(3));
		textList.add(view4);
		view5.setOnClickListener(new txListener(4));
		textList.add(view5);

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
		offset = (screenW / num - bmpW) / 2;

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

			textList.get(currIndex).setSelected(false);

			textList.get(currIndex).setTextSize(14);
			Animation animation = new TranslateAnimation(currIndex * one + offset, arg0 * one + offset, 0, 0);// 平移动画
			currIndex = arg0;
			animation.setFillAfter(true);// 动画终止时停留在最后一帧，不然会回到没有执行前的状态
			animation.setDuration(200);// 动画持续时间0.2秒
			imageView.clearAnimation();
			imageView.startAnimation(animation);// 是用ImageView来显示动画的

			textList.get(currIndex).setSelected(true);

			textList.get(currIndex).setTextSize(15);
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
	// @Override
		public void finish() {
			// 数据是使用Intent返回
			Intent intent = new Intent();
			// 把返回数据存入Intent
			intent.putExtra("result", "Is user's data changed?");
			// 设置返回数据
			this.setResult(RESULT, intent);
			super.finish();
		}

}
