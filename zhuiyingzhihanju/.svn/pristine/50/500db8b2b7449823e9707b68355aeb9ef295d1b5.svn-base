package com.bccv.zhuiyingzhihanju.fragment;

import java.util.ArrayList;
import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.activity.CollectActivity;
import com.bccv.zhuiyingzhihanju.activity.DownloadActivity;
import com.bccv.zhuiyingzhihanju.activity.HistoryActivity;
import com.bccv.zhuiyingzhihanju.activity.LoginActivity;
import com.bccv.zhuiyingzhihanju.activity.VideoInfoActivity;
import com.bccv.zhuiyingzhihanju.adapter.FoundAdapter;
import com.bccv.zhuiyingzhihanju.api.FoundApi;
import com.bccv.zhuiyingzhihanju.model.FoundModel;
import com.bccv.zhuiyingzhihanju.model.Movie;
import com.bccv.zhuiyingzhihanju.model.MovieNews;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tendcloud.tenddata.TCAgent;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;
import com.utils.tools.GlobalParams;
import com.utils.tools.ScreenUtil;
import com.utils.views.MyScrollView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextPaint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class FoundFragment extends BaseActivity {

	private List<Movie> data;
	private List<Movie> list;
	private List<Movie> news, newsList;
	private List<Movie> tv, TvList;
	private List<Movie> movie, MovieList;
	private List<Movie> Music, MusicList;
	private List<Movie> topList;
	FoundModel ModelList;
	private int topNum=0;
	private GridView gridView, gridView1, gridView2, gridView3, gridView4;
	private FoundAdapter adapter, adapter1, adapter2, adapter3, adapter4;
	private ImageView image1, image2;
	private TextView des1, des2, title1, title2;

	private List<Movie> ZList, MList;

	private RelativeLayout MMoreRe, TMoreRe, muMoreRe, vMoreRe, NMoreRe;
	private RelativeLayout mChangeRe, TCRe, mCRe, vCRe, nCRe;
	MovieNews Mnews;
	private int NcurrPage, currTVPage, currVPage, currMPage, currMusicPage;
	private int totalPage, totalTVPage, totalVPage, totalMPage, totalMusicPage;
	private PullToRefreshScrollView scrollView;
	private boolean isStop = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_found);

		scrollView = (PullToRefreshScrollView) findViewById(R.id.scrollView1);
		ImageButton collectBtn = (ImageButton) findViewById(R.id.title_search_edit);

		ImageButton DownBtn = (ImageButton) findViewById(R.id.title_search_btnDown);

		DownBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (GlobalParams.hasLogin) {
					startActivityWithSlideAnimation(DownloadActivity.class);
				} else {

					Bundle bundle = new Bundle();
					bundle.putString("type", "D");
					startActivityForResultSlideAnimation(LoginActivity.class, bundle);
				}
			}
		});
		collectBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (GlobalParams.hasLogin) {
					startActivityWithSlideAnimation(CollectActivity.class);
				} else {

					Bundle bundle = new Bundle();
					bundle.putString("type", "C");
					startActivityForResultSlideAnimation(LoginActivity.class, bundle);
				}
			}
		});
		ImageButton hisBtn=(ImageButton) findViewById(R.id.title_search_btnhis);
			hisBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivityWithSlideAnimation(HistoryActivity.class);

				}
			});
		image1 = (ImageView) findViewById(R.id.found_imagezong);

		image2 = (ImageView) findViewById(R.id.found_imagemovie);

		title1 = (TextView) findViewById(R.id.found_text_zong);
		title2 = (TextView) findViewById(R.id.found_text_movie);

		des1 = (TextView) findViewById(R.id.found_text_desz);
		des2 = (TextView) findViewById(R.id.found_text_desm);
		gridView = (GridView) findViewById(R.id.found_grid);
		gridView1 = (GridView) findViewById(R.id.found_grid1);
		gridView2 = (GridView) findViewById(R.id.found_grid2);
		gridView3 = (GridView) findViewById(R.id.found_grid3);
		gridView4 = (GridView) findViewById(R.id.found_grid4);
		data = new ArrayList<Movie>();

		news = new ArrayList<Movie>();
		tv = new ArrayList<Movie>();
		movie = new ArrayList<Movie>();

		Music = new ArrayList<Movie>();

		adapter = new FoundAdapter(news, this);

		adapter1 = new FoundAdapter(tv, this);
		adapter2 = new FoundAdapter(data, this);
		adapter3 = new FoundAdapter(movie, this);

		adapter4 = new FoundAdapter(Music, this);
		gridView.setAdapter(adapter);
		gridView1.setAdapter(adapter1);
		gridView2.setAdapter(adapter2);
		gridView3.setAdapter(adapter3);
		gridView4.setAdapter(adapter4);

		ll_dots = (LinearLayout) findViewById(R.id.ll_point_group);
		headerViewPager = (ViewPager) findViewById(R.id.fancy_viewPager);
		headerViewPager.setOnPageChangeListener(new MyPageChangeListener());
		Ptext = (TextView) findViewById(R.id.fancy_viewPager_text);
		TextPaint tp = Ptext.getPaint(); 
		tp.setFakeBoldText(true);
		getData();
		getOnClick();
		setMore();
		scrollView.setOnRefreshListener(new OnRefreshListener<MyScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<MyScrollView> refreshView) {
				// TODO Auto-generated method stub
				getData();

			}
		});
		Thread myThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (!isStop) {
					// 每个两秒钟发一条消息到主线程，更新viewpager界面
					SystemClock.sleep(5000);
					runOnUiThread(new Runnable() {
						public void run() {
							// 此方法在主线程中执行
							int newindex = headerViewPager.getCurrentItem() + 1;
							headerViewPager.setCurrentItem(newindex);

						}
					});
				}
			}
		});
		myThread.start(); 
	}

	@SuppressLint("CutPasteId")
	private void setMore() {

		MMoreRe = (RelativeLayout) findViewById(R.id.found_moreRe);
		TMoreRe = (RelativeLayout) findViewById(R.id.found_moreRe1);
		muMoreRe = (RelativeLayout) findViewById(R.id.found_moreRe2);
		vMoreRe = (RelativeLayout) findViewById(R.id.found_moreRe3);
		NMoreRe = (RelativeLayout) findViewById(R.id.found_moreRe4);

		nCRe = (RelativeLayout) findViewById(R.id.found_changeRe);
		TCRe = (RelativeLayout) findViewById(R.id.found_changeRe1);
		vCRe = (RelativeLayout) findViewById(R.id.found_changeRe2);
		mCRe = (RelativeLayout) findViewById(R.id.found_changeRe3);

		mChangeRe = (RelativeLayout) findViewById(R.id.found_changeRe4);

		MMoreRe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityWithSlideAnimation(FilterFragment.class);
			}
		});

		TMoreRe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityWithSlideAnimation(FilterFragment.class);
			}
		});
		vMoreRe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityWithSlideAnimation(FilterFragment.class);
			}
		});
		NMoreRe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityWithSlideAnimation(FilterFragment.class);
			}
		});

		muMoreRe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityWithSlideAnimation(FilterFragment.class);
			}
		});

		mChangeRe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ChangeDate("music");
			}
		});
		TCRe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ChangeDate("tv");
			}
		});
		mCRe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ChangeDate("movie");
			}
		});
		vCRe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ChangeDate("variety");
			}
		});

		nCRe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ChangeDate("news");
			}

		});

	}

	private void ChangeDate(final String type_id) {
		// TODO Auto-generated method stub

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (result.equals("true")) {

					if (type_id.equals("news")) {
						news.clear();
						// ModelList.setNews(Mnews.getList());
						newsList = ModelList.getNews();

						for (int i = NcurrPage * 6; i < 6 * (NcurrPage + 1); i++) {

							if (i < newsList.size()) {
								news.add(newsList.get(i));
							}
						}
						NcurrPage++;
						if (NcurrPage >= totalPage) {
							NcurrPage = 0;
						}

						adapter.notifyDataSetChanged();
					}
					if (type_id.equals("tv")) {
						tv.clear();
						// ModelList.setTv(Mnews.getList());
						TvList = ModelList.getTv();

						for (int i = currTVPage * 6; i < 6 * (currTVPage + 1); i++) {
							if (i < TvList.size()) {
								tv.add(TvList.get(i));
							}
						}
						currTVPage++;
						if (currTVPage >= totalTVPage) {
							currTVPage = 0;
						}

						adapter1.notifyDataSetChanged();

					}
					if (type_id.equals("variety")) {
						data.clear();

						// ModelList.setVariety(Mnews);

						list = ModelList.getVariety().getList();
						for (int i = currVPage * 6; i < 6 * (currVPage + 1); i++) {
							if (i < list.size()) {
								data.add(list.get(i));
							}
						}
						currVPage++;
						if (currVPage >= totalVPage) {
							currVPage = 0;
						}

						adapter2.notifyDataSetChanged();

					}

					if (type_id.equals("movie")) {
						movie.clear();

						// ModelList.setMovie(Mnews);
						MovieList = ModelList.getMovie().getList();
						for (int i = currMPage * 6; i < 6 * (currMPage + 1); i++) {
							if (i < MovieList.size()) {
								movie.add(MovieList.get(i));
							}
						}
						currMPage++;
						if (currMPage >= totalMPage) {
							currMPage = 0;
						}

						adapter3.notifyDataSetChanged();

					}
					if (type_id.equals("music")) {
						Music.clear();
						// ModelList.setMusic(Mnews.getList());
						MusicList = ModelList.getMusic();
						for (int i = currMusicPage * 6; i < 6 * (currMusicPage + 1); i++) {
							if (i < MusicList.size()) {
								Music.add(MusicList.get(i));
							}
						}
						currMusicPage++;
						if (currMusicPage >= totalMusicPage) {
							currMusicPage = 0;
						}

						adapter4.notifyDataSetChanged();

					}

				} else {
					showLongToast("换取失败");
				}

			}

		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {

				// FancyApi api = new FancyApi();
				try {
					// Mnews = api.getMoreNews(type_id);
					return "true";
				} catch (Exception e) {
					// TODO: handle exception

				}

				// Log.e("movieDodel", movieDodel.toString());
				// topList = api.getTopList();
				return "false";

			}
		}.execute("");

	}

	private void getOnClick() {
		// TODO Auto-generated method stub
		gridView.setSelector(new ColorDrawable(android.R.color.transparent));
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent aIntent = new Intent(FoundFragment.this, VideoInfoActivity.class);

				aIntent.putExtra("movie_id", news.get(position).getId());
				aIntent.putExtra("type_id", news.get(position).getType_id());
				aIntent.putExtra("episodes_id", "0");

				startActivity(aIntent);

			}
		});

		gridView1.setSelector(new ColorDrawable(android.R.color.transparent));
		gridView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent aIntent = new Intent(FoundFragment.this, VideoInfoActivity.class);

				aIntent.putExtra("movie_id", tv.get(position).getId());
				aIntent.putExtra("type_id", tv.get(position).getType_id());
				aIntent.putExtra("episodes_id", "0");

				startActivity(aIntent);

			}
		});

		gridView2.setSelector(new ColorDrawable(android.R.color.transparent));
		gridView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent aIntent = new Intent(FoundFragment.this, VideoInfoActivity.class);

				aIntent.putExtra("movie_id", data.get(position).getId());
				aIntent.putExtra("type_id", data.get(position).getType_id());
				aIntent.putExtra("episodes_id", "0");

				startActivity(aIntent);

			}
		});
		gridView3.setSelector(new ColorDrawable(android.R.color.transparent));
		gridView3.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent aIntent = new Intent(FoundFragment.this, VideoInfoActivity.class);

				aIntent.putExtra("movie_id", movie.get(position).getId());
				aIntent.putExtra("type_id", movie.get(position).getType_id());
				aIntent.putExtra("episodes_id", "0");

				startActivity(aIntent);

			}
		});

		gridView4.setSelector(new ColorDrawable(android.R.color.transparent));
		gridView4.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent aIntent = new Intent(FoundFragment.this, VideoInfoActivity.class);

				aIntent.putExtra("movie_id", Music.get(position).getId());
				aIntent.putExtra("type_id", Music.get(position).getType_id());
				aIntent.putExtra("episodes_id", "0");

				startActivity(aIntent);

			}
		});

	}

	public void getData() {

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				scrollView.onRefreshComplete();
				topNum++;
				if (data != null) {
					data.clear();

				}
				if(news!=null){
					news.clear();
				}
				if(tv!=null){
					tv.clear();
				}
				if(movie!=null){
					movie.clear();
				}
				if(Music!=null){
					Music.clear();
				}
				
				if (ModelList != null) {

					list = ModelList.getVariety().getList();
					newsList = ModelList.getNews();
					TvList = ModelList.getTv();
					MovieList = ModelList.getMovie().getList();
					MusicList = ModelList.getMusic();

					totalPage = newsList.size() / 6 + (newsList.size() % 6 == 0 ? 0 : 1);
					for (int i = NcurrPage * 6; i < 6 * (NcurrPage + 1); i++) {
						if (i < newsList.size()) {
							news.add(newsList.get(i));
						}

					}

					NcurrPage++;
					if (NcurrPage >= totalPage) {
						NcurrPage=0;
					}

					totalTVPage = TvList.size() / 6 + (TvList.size() % 6 == 0 ? 0 : 1);
					for (int i = currTVPage * 6; i < 6 * (currTVPage + 1); i++) {
						if (i < TvList.size()) {
							tv.add(TvList.get(i));
						}

					}
					currTVPage++;

					if (currTVPage >= totalTVPage) {
						currTVPage=0;
					}

					totalVPage = list.size() / 6 + (list.size() % 6 == 0 ? 0 : 1);

					for (int i = currVPage * 6; i < 6 * (currVPage + 1); i++) {
						if (i < list.size()) {
							data.add(list.get(i));
						}

					}
					currVPage++;

					if (currVPage >= totalVPage) {
						currVPage=0;
					}
					

						totalMPage = MovieList.size() / 6 + (MovieList.size() % 6 == 0 ? 0 : 1);
						for (int i = currMPage * 6; i < 6 * (currMPage + 1); i++) {
							if (i < MovieList.size()) {
								movie.add(MovieList.get(i));
							}

						

						currMPage++;

						if (currMPage >= totalMPage) {
							currMPage=0;
						}

					}

					totalMusicPage = MusicList.size() / 6 + (MusicList.size() % 6 == 0 ? 0 : 1);
					for (int i = currMusicPage * 6; i < 6 * (currMusicPage + 1); i++) {
						if (i < MusicList.size()) {
							Music.add(MusicList.get(i));
						}

					}
					currMusicPage++;
					if (currMusicPage >= totalMusicPage) {
						currMusicPage=0;
					}
					adapter.notifyDataSetChanged();

					adapter1.notifyDataSetChanged();
					adapter2.notifyDataSetChanged();

					adapter3.notifyDataSetChanged();

					adapter4.notifyDataSetChanged();
					
					if(topNum==1){
						topList = ModelList.getSlide();
						getImage(ModelList);
					if (topList != null) {
					
						setTopView(topList);
					}
					}
				}

			}

		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {

				FoundApi api = new FoundApi();

				ModelList = api.getTypeList();

				return "true";
			}
		}.execute("");
	}

	private void getImage(FoundModel modelList) {
		// TODO Auto-generated method stub
		ZList = modelList.getVariety().getBig();

		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(ZList.get(0).getImages(), image1, GlobalParams.movieOptions);
		title1.setText(ZList.get(0).getTitle());

		des1.setText(ZList.get(0).getShort_summary());

		MList = modelList.getMovie().getBig();
		ImageLoader imageLoader1 = ImageLoader.getInstance();
		imageLoader1.displayImage(MList.get(0).getImages(), image2, GlobalParams.movieOptions);
		title2.setText(MList.get(0).getTitle());

		des2.setText(MList.get(0).getShort_summary());

		image1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent aIntent = new Intent(FoundFragment.this, VideoInfoActivity.class);

				aIntent.putExtra("movie_id", ZList.get(0).getId());
				aIntent.putExtra("type_id", ZList.get(0).getType_id());
				aIntent.putExtra("episodes_id", "0");

				startActivity(aIntent);
			}
		});
		image2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent aIntent = new Intent(FoundFragment.this, VideoInfoActivity.class);

				aIntent.putExtra("movie_id", MList.get(0).getId());
				aIntent.putExtra("type_id", MList.get(0).getType_id());
				aIntent.putExtra("episodes_id", "0");

				startActivity(aIntent);
			}
		});

	}

	@SuppressWarnings("unused")
	private static final int CHANGE_VIEWPAGER = 1;
	private List<ImageView> dots; // 图片标题正文的那些点
	private List<String> headpicUrls;// 图片地址
	private LinearLayout ll_dots;

	private ViewPager headerViewPager;
	private int currentItem;
	private MyAdapter myAdapter;
	private TextView Ptext;
	private List<String> titleText;

	private void setTopView(List<Movie> topData2) {
		// TODO Auto-generated method stub
		if (dots == null) {
			dots = new ArrayList<ImageView>();
		}

		if (headpicUrls == null) {
			headpicUrls = new ArrayList<String>();
		}

		if (titleText == null) {
			titleText = new ArrayList<String>();
		}
		if (myAdapter == null) {
			myAdapter = new MyAdapter(topData2);

		} else {
			myAdapter.topData.clear();
			myAdapter.topData.addAll(topData2);
		}

		int temp = topData2.size() > 0 ? (myAdapter.getCount()) / 2 % (topData2.size()) : 0;
		currentItem = (myAdapter.getCount()) / 2 - temp;
		initHeadData(topData2);
		headerViewPager.setAdapter(myAdapter);
		headerViewPager.setCurrentItem(0);

	}

	/**
	 * 初始化头部viewPager
	 * 
	 * @param freshList
	 */
	private void initHeadData(List<Movie> modelList) {

		ll_dots.removeAllViews();
		dots.clear();

		headpicUrls.clear();
		titleText.clear();
	
		for (Movie model : modelList) {
			ImageView dot = addDot(ll_dots);

			dots.add(dot);

			// 初始化图片url
			headpicUrls.add(model.getImages());
			titleText.add(model.getTitle());

		}
		dots.get(currentItem % (dots.size())).setImageResource(R.drawable.dot_select);
		Ptext.setText(titleText.get(currentItem % (dots.size())));

	}

	/**
	 * 添加滑动点
	 * 
	 * @param ll_dots
	 */
	private ImageView addDot(LinearLayout ll_dots) {
		// 初始化圆点

		ImageView view = new ImageView(FoundFragment.this);
		view.setImageResource(R.drawable.dot);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		mLayoutParams.rightMargin = ScreenUtil.dp2px(4);
		mLayoutParams.leftMargin = ScreenUtil.dp2px(4);
		mLayoutParams.topMargin = ScreenUtil.dp2px(12);
		mLayoutParams.bottomMargin = ScreenUtil.dp2px(7);
		ll_dots.addView(view, mLayoutParams);
		return view;
	}

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = currentItem;

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
		
			currentItem = position;
			Ptext.setText(titleText.get(position % (dots.size())));
			dots.get(oldPosition % (dots.size())).setImageResource(R.drawable.dot);
			dots.get(position % (dots.size())).setImageResource(R.drawable.dot_select);
			oldPosition = position;

		}

		public void onPageScrollStateChanged(int position) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	/**
	 * 填充ViewPager页面的适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyAdapter extends PagerAdapter {
		private List<Movie> topData;

		public MyAdapter(List<Movie> topData) {
			// TODO Auto-generated constructor stub
			this.topData = topData;
		}

		@Override
		public int getCount() {
			if (topData.size() == 1) {
				return 1;
			}
			return Integer.MAX_VALUE;
		}

		@Override
		public Object instantiateItem(View container, int progress) {
			int curretIndex = (progress) % topData.size();
			ImageView imageView = new ImageView(FoundFragment.this);
			imageView.setScaleType(ScaleType.CENTER_CROP);

			if (topData != null && topData.size() > curretIndex) {

				setOnClick4headPic(imageView, topData.get(curretIndex));
			}

			((ViewPager) container).addView(imageView);

			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage(headpicUrls.get(curretIndex), imageView, GlobalParams.bannerOptions);

			return imageView;
		}

		@Override
		public void destroyItem(View container, int arg1, Object arg2) {
			((ViewPager) container).removeView((View) arg2);
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

		@Override
		public void finishUpdate(View arg0) {

		}
	}

	/**
	 * 
	 * /** 为头部viewPager设置点击事件
	 * 
	 * @param view
	 * @param fresh
	 */
	private void setOnClick4headPic(View view, final Movie model) {

		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int curretIndex = (currentItem) % topList.size();
				Intent aIntent = new Intent(FoundFragment.this, VideoInfoActivity.class);

				aIntent.putExtra("movie_id", topList.get(curretIndex).getId());
				aIntent.putExtra("type_id", topList.get(curretIndex).getType_id());
				aIntent.putExtra("episodes_id", "0");

				startActivity(aIntent);

			}
		});

	}

	@Override
	protected void onDestroy() {
		isStop = true;
		super.onDestroy();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		TCAgent.onPageStart(getApplicationContext(), "FoundFragment");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		TCAgent.onPageEnd(getApplicationContext(), "FoundFragment");
	}
}
