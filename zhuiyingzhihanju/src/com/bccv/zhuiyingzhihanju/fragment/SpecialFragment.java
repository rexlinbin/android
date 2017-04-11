package com.bccv.zhuiyingzhihanju.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.activity.CollectActivity;
import com.bccv.zhuiyingzhihanju.activity.DownloadActivity;
import com.bccv.zhuiyingzhihanju.activity.HistoryActivity;
import com.bccv.zhuiyingzhihanju.activity.LoginActivity;
import com.bccv.zhuiyingzhihanju.activity.MovieSearchActivity;
import com.bccv.zhuiyingzhihanju.api.SearchApi;
import com.bccv.zhuiyingzhihanju.model.HotSearch;
import com.tendcloud.tenddata.TCAgent;
import com.utils.tools.BaseFragmentActivity;
import com.utils.tools.Callback;
import com.utils.tools.GlobalParams;

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

public class SpecialFragment extends BaseFragmentActivity {

	Boolean isSelect = false;
	private Button SelectBtn;
	private EditText edit;
	private RelativeLayout titleSelect, sp_re;
	private LinearLayout title;
	private TextView titleName;
	private List<HotSearch> hotSearchList, getHotSearchList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_special);

		isSelect = getIntent().getBooleanExtra("isSelect", false);
		SelectBtn = (Button) findViewById(R.id.main_selectBtn);

		
	
		SelectBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent aIntent = new Intent(SpecialFragment.this, FilterFragment.class);

				aIntent.putExtra("isSelect", "S");
				startActivity(aIntent);
			}
		});

		ImageButton collectBtn = (ImageButton) findViewById(R.id.title_search_edit);

		ImageButton DownBtn = (ImageButton) findViewById(R.id.title_search_btnDown);

		collectBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (GlobalParams.hasLogin) {
					startActivityWithSlideAnimation(CollectActivity.class);
				} else {

					Intent aIntent = new Intent(SpecialFragment.this, LoginActivity.class);

					aIntent.putExtra("type", "C");
					startActivity(aIntent);
				}
			}
		});

		DownBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (GlobalParams.hasLogin) {
					startActivityWithSlideAnimation(DownloadActivity.class);
				} else {

					Intent aIntent = new Intent(SpecialFragment.this, LoginActivity.class);

					aIntent.putExtra("type", "D");
					startActivity(aIntent);
				}

			}
		});
		ImageButton hisBtn = (ImageButton) findViewById(R.id.title_search_btnhis);
		hisBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityWithSlideAnimation(HistoryActivity.class);

			}
		});
		edit = (EditText) findViewById(R.id.main_search);
		edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent aIntent = new Intent(SpecialFragment.this, MovieSearchActivity.class);

				startActivity(aIntent);
			}
		});

		titleSelect = (RelativeLayout) findViewById(R.id.title_search_layout);
		title = (LinearLayout) findViewById(R.id.title);
		titleName = (TextView) findViewById(R.id.titleName_textView);
		sp_re = (RelativeLayout) findViewById(R.id.sp_re);
		View sp_view = findViewById(R.id.sp_view);
		if (!isSelect) {
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

		} else {

			title.setVisibility(View.GONE);
			sp_re.setVisibility(View.VISIBLE);

			titleSelect.setVisibility(View.VISIBLE);
			sp_view.setVisibility(View.GONE);
		}

		InitImage(5);
		InitTextView();
		InitViewPager();
		getHot();
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
		TCAgent.onPageStart(getApplicationContext(), "SpecialFragment");
	}

	public void getHot() {

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				hotSearchList = new ArrayList<HotSearch>();
				if (getHotSearchList != null && getHotSearchList.size() > 0) {
					hotSearchList.addAll(getHotSearchList);

					edit.setText("大家都在搜：" + hotSearchList.get(0).getName());
				}

			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				SearchApi searchApi = new SearchApi();
				getHotSearchList = searchApi.getHotSearchList("10");

				return null;
			}
		}.execute("");

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
		HJFragment hjFragment = new HJFragment();
		Bundle hjBundle = new Bundle();
		hjBundle.putString("type", "hj");
		hjBundle.putBoolean("ifSelect", isSelect);
		hjFragment.setArguments(hjBundle);

		fragmentList.add(hjFragment);

		HJFragment hzFragment = new HJFragment();

		Bundle hzBundle = new Bundle();
		hzBundle.putString("type", "hz");
		hzBundle.putBoolean("ifSelect", isSelect);
		hzFragment.setArguments(hzBundle);

		fragmentList.add(hzFragment);

		HJFragment tuijianFragment = new HJFragment();
		Bundle hyBundle = new Bundle();
		hyBundle.putString("type", "hy");
		hyBundle.putBoolean("ifSelect", isSelect);
		tuijianFragment.setArguments(hyBundle);

		fragmentList.add(tuijianFragment);

		HJFragment newsFragment = new HJFragment();
		Bundle NewsBundle = new Bundle();
		NewsBundle.putString("type", "news");
		NewsBundle.putBoolean("ifSelect", isSelect);
		newsFragment.setArguments(NewsBundle);

		fragmentList.add(newsFragment);

		HJFragment musicFragment = new HJFragment();
		Bundle musicBundle = new Bundle();
		musicBundle.putString("type", "music");
		musicBundle.putBoolean("ifSelect", isSelect);
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
		if (isSelect) {
			view1.setSelected(true);
		}

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
		Random random = new Random();

		public txListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mPager.setCurrentItem(index);
			if (getHotSearchList != null) {
				int randomInt = random.nextInt(getHotSearchList.size());

				edit.setText("大家都在搜：" + hotSearchList.get(randomInt).getName());
			} else {
				edit.setText("大家都在搜：" + "心灵的声音");
			}
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
		Random random = new Random();

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
			if (isSelect) {
				textList.get(currIndex).setSelected(false);
			}
			if (getHotSearchList != null) {
				int randomInt = random.nextInt(getHotSearchList.size());

				edit.setText("大家都在搜：" + hotSearchList.get(randomInt).getName());
			} else {
				edit.setText("大家都在搜：" + "心灵的声音");
			}

			textList.get(currIndex).setTextSize(14);
			Animation animation = new TranslateAnimation(currIndex * one + offset, arg0 * one + offset, 0, 0);// 平移动画
			currIndex = arg0;
			animation.setFillAfter(true);// 动画终止时停留在最后一帧，不然会回到没有执行前的状态
			animation.setDuration(200);// 动画持续时间0.2秒
			imageView.clearAnimation();
			imageView.startAnimation(animation);// 是用ImageView来显示动画的
			if (isSelect) {
				textList.get(currIndex).setSelected(true);
			}

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
	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		TCAgent.onPageEnd(getApplicationContext(), "SpecialFragment");
	}
}
