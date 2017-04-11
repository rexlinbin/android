package com.bccv.zhuiyingzhihanju.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.activity.CollectActivity;
import com.bccv.zhuiyingzhihanju.activity.DownloadActivity;
import com.bccv.zhuiyingzhihanju.activity.HistoryActivity;
import com.bccv.zhuiyingzhihanju.activity.LoginActivity;
import com.bccv.zhuiyingzhihanju.activity.MoreVideoActivity;
import com.bccv.zhuiyingzhihanju.activity.MovieSearchActivity;
import com.bccv.zhuiyingzhihanju.activity.StarInfoActivity;
import com.bccv.zhuiyingzhihanju.activity.Video2DPlayerActivity;
import com.bccv.zhuiyingzhihanju.activity.VideoInfoActivity;
import com.bccv.zhuiyingzhihanju.adapter.CircleAdapter;
import com.bccv.zhuiyingzhihanju.adapter.MovieListAdapter;
import com.bccv.zhuiyingzhihanju.api.FancyApi;
import com.bccv.zhuiyingzhihanju.api.SearchApi;
import com.bccv.zhuiyingzhihanju.model.HotSearch;
import com.bccv.zhuiyingzhihanju.model.Movie;
import com.bccv.zhuiyingzhihanju.model.MovieModel;
import com.bccv.zhuiyingzhihanju.model.MovieNews;
import com.bccv.zhuiyingzhihanju.model.Star;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tendcloud.tenddata.TCAgent;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;
import com.utils.tools.GlobalParams;
import com.utils.tools.ScreenUtil;
import com.utils.tools.SerializationUtil;
import com.utils.tools.StringUtils;
import com.utils.views.HorizontalListView;
import com.utils.views.MyGridViewNoMove;
import com.utils.views.MyScrollView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextPaint;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FancyFragment extends BaseActivity {

	private MyGridViewNoMove gridView, gridView1, gridView2, gridView3;
	private MovieListAdapter adapter, adapter1, adapter2, adapter3;
	MovieModel movieDodel, movieDodel1;
	ImageButton collectBtn;
	private List<Movie> data, tvData;
	private List<Movie> list, tvList;
	private List<Movie> topList;
	private ImageButton DownBtn,hisBtn;
	private EditText main_search;
	private boolean isStop = false;

	private List<Movie> sideLightList, siList;
	private List<Movie> hightList, hiList;
	private ImageView image1, image2, image3;
	private TextView des1, des2, des3, title1, title2, title3;
	private List<Movie> siBigList, hiBigList, comBigList;
	private RelativeLayout hotMoreRe, sMoreRe, hMoreRe, tMoreRe;
	private RelativeLayout hotChangeRe, sCRe, hCRe, tCRe;
	MovieNews news = null;
	private List<HotSearch> hotSearchList, getHotSearchList;
	private int currPage, currTVPage, currSiPage, currHiPage;
	private int totalPage, totalTVPage, totalSiPage, totalHiPage;
	private List<Movie> HlistData;
	RelativeLayout reH;
	RelativeLayout clear;
	TextView hisContext;
	Animation slateAnimation2;
	private PullToRefreshScrollView scrollView;
	private boolean isClear = false;
	private static int timeNum = 10;
	private static Timer timer = new Timer();
	private int topNum=0;
	// RelativeLayout errRE;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_fancy);

		scrollView = (PullToRefreshScrollView) findViewById(R.id.scrollView1);

		reH = (RelativeLayout) findViewById(R.id.fancy_his_re);
		clear = (RelativeLayout) findViewById(R.id.fancy_his_close);
		hisContext = (TextView) findViewById(R.id.fancy_his);

		main_search = (EditText) findViewById(R.id.main_search);
		collectBtn = (ImageButton) findViewById(R.id.title_search_edit);

		DownBtn = (ImageButton) findViewById(R.id.title_search_btnDown);
      
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
		 hisBtn=(ImageButton) findViewById(R.id.title_search_btnhis);
		hisBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityWithSlideAnimation(HistoryActivity.class);

			}
		});
		
		
		
		
		
		
		
		
		
		
		main_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivityWithSlideAnimation(MovieSearchActivity.class);

			}
		});

		gridView = (MyGridViewNoMove) findViewById(R.id.fancy_grid);
		gridView.setSelector(new ColorDrawable(android.R.color.transparent));

		gridView1 = (MyGridViewNoMove) findViewById(R.id.fancy1_grid);
		gridView1.setSelector(new ColorDrawable(android.R.color.transparent));
		gridView2 = (MyGridViewNoMove) findViewById(R.id.fancy2_grid);
		gridView2.setSelector(new ColorDrawable(android.R.color.transparent));

		gridView3 = (MyGridViewNoMove) findViewById(R.id.fancy3_grid);
		gridView3.setSelector(new ColorDrawable(android.R.color.transparent));

		siList = new ArrayList<Movie>();
		hiList = new ArrayList<Movie>();
		data = new ArrayList<Movie>();
		tvData = new ArrayList<Movie>();

		// list = SerializationUtil.readMainSerialization(getApplication());
		if (list != null) {
			data.addAll(list);
		}

		adapter = new MovieListAdapter(this, data);

		// tvList = SerializationUtil.readMainTVSerialization(getApplication());

		if (tvList != null) {
			tvData.addAll(tvList);
		}

		adapter1 = new MovieListAdapter(this, tvData);
		adapter2 = new MovieListAdapter(this, siList);

		adapter3 = new MovieListAdapter(this, hiList);

		gridView.setAdapter(adapter);
		gridView1.setAdapter(adapter1);
		gridView2.setAdapter(adapter2);
		gridView3.setAdapter(adapter3);

		ll_dots = (LinearLayout) findViewById(R.id.ll_point_group);

		headerViewPager = (ViewPager) findViewById(R.id.fancy_viewPager);
		headerViewPager.setOnPageChangeListener(new MyPageChangeListener());
		Ptext = (TextView) findViewById(R.id.fancy_viewPager_text);
		TextPaint tp = Ptext.getPaint(); 
		tp.setFakeBoldText(true);
		getImage();

		getData();
		setOnClick();
		setMore();
		getHot();
		
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(0);
			}
		};
		timer = new Timer();
		timer.schedule(task, 0, 1000);
		
//		slateAnimation2 = AnimationUtils.loadAnimation(this, R.anim.popup_exit);// 加载Xml文件中的动画
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
		myThread.start(); // 用来更细致的划分 比如页面失去焦点时候停止子线程恢复焦点时再开启
		scrollView.setOnRefreshListener(new OnRefreshListener<MyScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<MyScrollView> refreshView) {
				// TODO Auto-generated method stub
				getData();

			}
		});

	}
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
			if (timeNum == 0) {
				reH.setVisibility(View.GONE);
				isClear = true;
				timer.cancel();
//				reH.startAnimation(slateAnimation2);
			}
			timeNum--;
		};
	};

	private void getHistory() {
		// TODO Auto-generated method stub

		HlistData = SerializationUtil.readHistoryCache(this.getApplicationContext());
		if (HlistData != null && HlistData.size() > 0) {
			if (!isClear) {
				reH.setVisibility(View.VISIBLE);
			}

			String htext = HlistData.get(0).getTitle();
			String htime = HlistData.get(0).getEpisode_id();
			long time = HlistData.get(0).getPlay_Time();

			if (StringUtils.isEmpty(time + "")) {
				hisContext.setText("您上次看到《" + htext + "》第" + htime + "集" + StringUtils.toDate(time + ""));

			} else {
				hisContext.setText("您上次看到" + "《" + htext + "》" + "第" + htime + "集");

			}
		} else {
			reH.setVisibility(View.GONE);
		}
		clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				reH.setVisibility(View.GONE);
				isClear = true;
			}
		});
		reH.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent aIntent = new Intent(FancyFragment.this, Video2DPlayerActivity.class);

				aIntent.putExtra("movie_id", HlistData.get(0).getId());
				aIntent.putExtra("type_id", HlistData.get(0).getType_id());
				aIntent.putExtra("episodes_id", HlistData.get(0).getEpisode_id());

				startActivity(aIntent);
			}
		});

	

	}

	private void setMore() {
		// TODO Auto-generated method stub

		hotMoreRe = (RelativeLayout) findViewById(R.id.fanny_moreRe);
		sMoreRe = (RelativeLayout) findViewById(R.id.fanny_moreRe1);
		hMoreRe = (RelativeLayout) findViewById(R.id.fanny_moreRe2);
		tMoreRe = (RelativeLayout) findViewById(R.id.fanny_moreRe3);

		hotChangeRe = (RelativeLayout) findViewById(R.id.fanny_changeRe);
		sCRe = (RelativeLayout) findViewById(R.id.fanny_changeRe1);
		hCRe = (RelativeLayout) findViewById(R.id.fanny_changeRe2);
		tCRe = (RelativeLayout) findViewById(R.id.fanny_changeRe3);

		hotMoreRe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				

				Bundle bundle = new Bundle();
				bundle.putString("type", "hot");
				startActivityForResultSlideAnimation(MoreVideoActivity.class, bundle);
				
				
			}
		});

		sMoreRe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putString("type", "coming");
				startActivityForResultSlideAnimation(MoreVideoActivity.class, bundle);
			}
		});
		hMoreRe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putString("type", "sidelight");
				startActivityForResultSlideAnimation(MoreVideoActivity.class, bundle);
			}
		});
		tMoreRe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putString("type", "high");
				startActivityForResultSlideAnimation(MoreVideoActivity.class, bundle);
			}
		});

		hotChangeRe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ChangeDate("hot");
			}
		});
		sCRe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ChangeDate("comming");
			}
		});
		hCRe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ChangeDate("sidelight");
			}
		});
		tCRe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ChangeDate("high");
			}
		});

	}

	private void ChangeDate(final String type_id) {

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (result.equals("true")) {
					if (type_id.equals("hot")) {
						data.clear();
						// movieDodel.setHot(news.getList());

						list = movieDodel.getHot();

						for (int i = currPage * 6; i < 6 * (currPage + 1); i++) {
							if (i < list.size()) {
								data.add(list.get(i));
							}

						}
						currPage++;
						if (currPage >= totalPage) {
							currPage = 0;
						}

						// SerializationUtil.wirteMainSerialization(getApplication(),
						// (Serializable) data);

						adapter.notifyDataSetChanged();
					}
					if (type_id.equals("comming")) {
						tvData.clear();
						// movieDodel.setComing(news);
						tvList = movieDodel.getComing().getList();

						for (int i = currTVPage * 6; i < 6 * (currTVPage + 1); i++) {
							if (i < tvList.size()) {
								tvData.add(tvList.get(i));
							}

						}
						currTVPage++;
						if (currTVPage >= totalTVPage) {
							currTVPage = 0;
						}

						// SerializationUtil.wirteMainTVSerialization(getApplicationContext(),
						// (Serializable) tvData);
						adapter1.notifyDataSetChanged();

					}
					if (type_id.equals("sidelight")) {
						siList.clear();
						// movieDodel.setSidelight(news);
						sideLightList = movieDodel.getSidelight().getList();
						for (int i = currSiPage * 6; i < 6 * (currSiPage + 1); i++) {
							if (i < sideLightList.size()) {
								siList.add(sideLightList.get(i));
							}

						}
						currSiPage++;
						if (currSiPage >= totalSiPage) {
							currSiPage = 0;
						}
						adapter2.notifyDataSetChanged();

					}

					if (type_id.equals("high")) {
						hiList.clear();
						// movieDodel.setHigh(news);
						hightList = movieDodel.getHigh().getList();
						for (int i = currHiPage * 6; i < 6 * (currHiPage + 1); i++) {
							if (i < hightList.size()) {
								hiList.add(hightList.get(i));
							}

						}
						currHiPage++;
						if (currHiPage >= totalHiPage) {
							currHiPage = 0;
						}
						adapter3.notifyDataSetChanged();
						hiBigList = movieDodel.getHigh().getBig();

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
					// news = api.getMoreNews(type_id);
					// Log.e("movieDodel", movieDodel.toString());
					// topList = api.getTopList();
					return "true";
				} catch (Exception e) {
					// TODO: handle exception
				e.printStackTrace();
				}
				return "false";

			}
		}.execute("");

	}

	private void setOnClick() {
		// TODO Auto-generated method stub
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent aIntent = new Intent(FancyFragment.this, VideoInfoActivity.class);

				aIntent.putExtra("movie_id", data.get(position).getId());
				aIntent.putExtra("type_id", data.get(position).getType_id());
				aIntent.putExtra("episodes_id", "0");

				startActivity(aIntent);
			}
		});

		gridView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
			
				Intent aIntent = new Intent(FancyFragment.this, VideoInfoActivity.class);

				aIntent.putExtra("movie_id", tvData.get(position).getId());
				aIntent.putExtra("type_id", tvData.get(position).getType_id());
				aIntent.putExtra("episodes_id", "0");

				startActivity(aIntent);
				
			}
		});

		gridView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent aIntent = new Intent(FancyFragment.this, VideoInfoActivity.class);

				aIntent.putExtra("movie_id", siList.get(position).getId());
				aIntent.putExtra("type_id", siList.get(position).getType_id());
				aIntent.putExtra("episodes_id", "0");

				startActivity(aIntent);
			}
		});
		gridView3.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent aIntent = new Intent(FancyFragment.this, VideoInfoActivity.class);

				aIntent.putExtra("movie_id", hiList.get(position).getId());
				aIntent.putExtra("type_id", hiList.get(position).getType_id());
				aIntent.putExtra("episodes_id", "0");

				startActivity(aIntent);
			}
		});

	}

	private void getImage() {
		// TODO Auto-generated method stub
		image1 = (ImageView) findViewById(R.id.fancy_imageshang);

		image2 = (ImageView) findViewById(R.id.fancy_imagehua);
		image3 = (ImageView) findViewById(R.id.fancy_imageTian);

		title1 = (TextView) findViewById(R.id.fancy_text_shang);
		title2 = (TextView) findViewById(R.id.fancy_text_hua);

		title3 = (TextView) findViewById(R.id.fancy_text_tian);

		des1 = (TextView) findViewById(R.id.fancy_text_dess);
		des2 = (TextView) findViewById(R.id.fancy_text_desh);
		des3 = (TextView) findViewById(R.id.fancy_text_des);

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
				if (tvData != null) {
					tvData.clear();
				}
				if(list!=null){
					list.clear();
				}
				if(siList!=null){
					siList.clear();
				}
				if(hiList!=null){
					hiList.clear();
				}
				
				
				if (movieDodel != null) {

					list = movieDodel.getHot();

					sideLightList = movieDodel.getSidelight().getList();
					hightList = movieDodel.getHigh().getList(); 

					totalPage = list.size() / 6 + (list.size() % 6 == 0 ? 0 : 1);
					for (int i = currPage * 6; i < 6 * (currPage + 1); i++) {
						if (i < list.size()) {
							data.add(list.get(i));
						}

					}
					currPage++;
					if (currPage >= totalPage) {
						currPage=0;
					}

				
					adapter.notifyDataSetChanged();
					tvList = movieDodel.getComing().getList();

					totalTVPage = tvList.size() / 6 + (tvList.size() % 6 == 0 ? 0 : 1);
					for (int i = currTVPage * 6; i < 6 * (currTVPage + 1); i++) {
						if (i < tvList.size()) {
							tvData.add(tvList.get(i));
						}

					}
					currTVPage++;
					if (currTVPage >= totalTVPage) {
						currTVPage=0;
					}
				
					adapter1.notifyDataSetChanged();

					if (sideLightList != null) {
						totalSiPage = sideLightList.size() / 6 + (sideLightList.size() % 6 == 0 ? 0 : 1);
						for (int i = currSiPage * 6; i < 6 * (currSiPage + 1); i++) {
							if (i < sideLightList.size()) {
								siList.add(sideLightList.get(i));
							}

						}
						currSiPage++;
						if (currSiPage >= totalSiPage) {
							currSiPage=0;
						}
					} else {
						gridView2.setVisibility(View.GONE);
					}
					if (hightList != null) {
						totalHiPage = hightList.size() / 6 + (hightList.size() % 6 == 0 ? 0 : 1);
						for (int i = currHiPage * 6; i < 6 * (currHiPage + 1); i++) {
							if (i < hightList.size()) {
								hiList.add(hightList.get(i));
							}

						}
						currHiPage++;
						if (currHiPage >= totalHiPage) {
							currHiPage=0;
						}
					} else {
						gridView3.setVisibility(View.GONE);
					}
					adapter2.notifyDataSetChanged();
					adapter3.notifyDataSetChanged();
					if(topNum==1){
						setImagedata(movieDodel);
						setStart(movieDodel);
					topList = movieDodel.getSlide();
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

				FancyApi api = new FancyApi();

				movieDodel = api.getMovielist();
				// Log.e("movieDodel", movieDodel.toString());
				// topList = api.getTopList();

				return "true";
			}
		}.execute("");
	}

	private void setStart(MovieModel movieDodel) {
		// TODO Auto-generated method stub
		HorizontalListView horizontalListView = (HorizontalListView) findViewById(R.id.fancy_horizontalListView);
		final List<Star> starlist = movieDodel.getStar();
		if (starlist != null) {
			CircleAdapter circleAdapter = new CircleAdapter(FancyFragment.this, starlist);
			horizontalListView.setAdapter(circleAdapter);
		} else {
			horizontalListView.setVisibility(View.GONE);
		}

		horizontalListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), StarInfoActivity.class);
				intent.putExtra("star_id", starlist.get(position).getId());
				startActivity(intent);
			}
		});
	}

	private void setImagedata(MovieModel movieDodel) {
		if(movieDodel.getComing()!=null){
		comBigList = movieDodel.getComing().getBig();

		if (comBigList != null) {

			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage(comBigList.get(0).getImages(), image1, GlobalParams.movieOptions);
			title1.setText(comBigList.get(0).getTitle());
			des1.setText(comBigList.get(0).getShort_summary());
		} else {
			image1.setVisibility(View.GONE);
			title1.setVisibility(View.GONE);
			des1.setVisibility(View.GONE);
		}
}

if(movieDodel.getSidelight()!=null){
		siBigList = movieDodel.getSidelight().getBig();
		if (siBigList != null) {

			ImageLoader imageLoader1 = ImageLoader.getInstance();
			imageLoader1.displayImage(siBigList.get(0).getImages(), image2, GlobalParams.movieOptions);
			title2.setText(siBigList.get(0).getTitle());
			des2.setText(siBigList.get(0).getShort_summary());

		} else {
			image2.setVisibility(View.GONE);
			title2.setVisibility(View.GONE);
			des2.setVisibility(View.GONE);
		}
}
if(movieDodel.getHigh()!=null){
		hiBigList = movieDodel.getHigh().getBig();
		if (hiBigList != null) {

			ImageLoader imageLoader2 = ImageLoader.getInstance();
			imageLoader2.displayImage(hiBigList.get(0).getImages(), image3, GlobalParams.movieOptions);
			title3.setText(hiBigList.get(0).getTitle());

			des3.setText(hiBigList.get(0).getShort_summary());
		} else {
			image3.setVisibility(View.GONE);
			title3.setVisibility(View.GONE);
			des3.setVisibility(View.GONE);
		}
		if (hiBigList == null && movieDodel.getHigh().getList() == null) {

			LinearLayout gaoMore = (LinearLayout) findViewById(R.id.fancy_gaoMore);

			LinearLayout gao = (LinearLayout) findViewById(R.id.fancy_gao);
			gaoMore.setVisibility(View.GONE);
			gao.setVisibility(View.GONE);
		}
}
		image1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent aIntent = new Intent(FancyFragment.this, VideoInfoActivity.class);

				aIntent.putExtra("movie_id", comBigList.get(0).getId());
				aIntent.putExtra("type_id", comBigList.get(0).getType_id());
				aIntent.putExtra("episodes_id", "0");

				startActivity(aIntent);
			}
		});

		image2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent aIntent = new Intent(FancyFragment.this, VideoInfoActivity.class);

				aIntent.putExtra("movie_id", siBigList.get(0).getId());
				aIntent.putExtra("type_id", siBigList.get(0).getType_id());
				aIntent.putExtra("episodes_id", "0");

				startActivity(aIntent);
			}
		});

		image3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent aIntent = new Intent(FancyFragment.this, VideoInfoActivity.class);

				aIntent.putExtra("movie_id", hiBigList.get(0).getId());
				aIntent.putExtra("type_id", hiBigList.get(0).getType_id());
				aIntent.putExtra("episodes_id", "0");

				startActivity(aIntent);
			}
		});

	}

	public void getHot() {

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
			    Random random=new Random();
				hotSearchList = new ArrayList<HotSearch>();
				if (getHotSearchList != null && getHotSearchList.size() > 0) {
					hotSearchList.addAll(getHotSearchList);
					 int randomInt=random.nextInt(getHotSearchList.size());
					main_search.setText("大家都在搜：" + hotSearchList.get(randomInt).getName());
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

		ImageView view = new ImageView(FancyFragment.this);
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
			ImageView imageView = new ImageView(FancyFragment.this);
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
				Intent aIntent = new Intent(FancyFragment.this, VideoInfoActivity.class);

				aIntent.putExtra("movie_id", topList.get(curretIndex).getId());
				aIntent.putExtra("type_id", topList.get(curretIndex).getType_id());
				aIntent.putExtra("episodes_id", "0");

				startActivity(aIntent);
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getHistory();
		TCAgent.onPageStart(getApplicationContext(), "FancyFragment");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		TCAgent.onPageEnd(getApplicationContext(), "FancyFragment");
	}

	@Override
	protected void onDestroy() {
		isStop = true;
		timer.cancel();
		super.onDestroy();
	}

}
