package com.bccv.zhuiyingzhihanju.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.adapter.CircleAdapter;
import com.bccv.zhuiyingzhihanju.adapter.HotSearchAdapter;
import com.bccv.zhuiyingzhihanju.adapter.SearchRecomAdapter;
import com.bccv.zhuiyingzhihanju.api.SearchApi;
import com.bccv.zhuiyingzhihanju.model.HotSearch;
import com.bccv.zhuiyingzhihanju.model.Search;
import com.bccv.zhuiyingzhihanju.model.SearchInfo;
import com.bccv.zhuiyingzhihanju.model.SearchRecom;
import com.bccv.zhuiyingzhihanju.model.SearchStar;
import com.bccv.zhuiyingzhihanju.model.SearchType;
import com.bccv.zhuiyingzhihanju.model.Star;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tendcloud.tenddata.TCAgent;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;
import com.utils.tools.GlobalParams;
import com.utils.tools.InputTools;
import com.utils.tools.SerializationUtil;
import com.utils.tools.StringUtils;
import com.utils.views.HorizontalListView;
import com.utils.views.MyScrollViewh;
import com.utils.views.RoundedImageView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class MovieSearchActivity extends BaseActivity {
	private ListView listView, hotListView;
	private List<String> historyList;
	private List<HotSearch> hotSearchList, getHotSearchList;
	private List<TextView> hotTextViews;
	private List<SearchRecom> hotList, getHotList;
	private HotSearchAdapter adapter;
	private SearchRecomAdapter hotAdapter;
	private TextView clearTextView, searchTextView, hotTextView1, hotTextView2, hotTextView3, hotTextView4,
			hotTextView5, hotTextView6;
	private EditText searchEditText;
	private LinearLayout searchInfoLayout, hotLayout1, hotLayout2;
	private TextView imageView;

	private void tcStart(){
		TCAgent.onPageStart(getApplicationContext(), "MovieSearchActivity");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		TCAgent.onPageEnd(getApplicationContext(), "MovieSearchActivity");
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tcStart();
		setContentView(R.layout.activity_search);
		setBack();
		setView();
		getData();
	}

	private ImageButton backTextView;

	private void setBack() {
		backTextView = (ImageButton) findViewById(R.id.back_textView);
		backTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void setView() {
		searchInfoLayout = (LinearLayout) findViewById(R.id.searchInfo_layout);
		listView = (ListView) findViewById(R.id.listView);
		hotSearchList = new ArrayList<HotSearch>();
		historyList = SerializationUtil.readSelectHistoryCache(getApplicationContext());
		if (historyList == null) {
			historyList = new ArrayList<String>();
		}

		adapter = new HotSearchAdapter(getApplicationContext(), historyList);
		listView.setAdapter(adapter);
		listView.setSelector(new ColorDrawable(android.R.color.transparent));
		listView.setDividerHeight(0);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				String searchString = historyList.get(position);
				searchInfoLayout.setVisibility(View.GONE);
				getSearchData(searchString);
			}
		});

		searchEditText = (EditText) findViewById(R.id.search_editText);
		searchTextView = (TextView) findViewById(R.id.search_textView);
		clearTextView = (TextView) findViewById(R.id.clear_textView);

		searchTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String searchString = searchEditText.getText().toString();
				if (!StringUtils.isEmpty(searchString)) {
					if (!historyList.contains(searchString)) {
						historyList.add(0, searchString);
						adapter.notifyDataSetChanged();
						SerializationUtil.wirteSelectHistorySerialization(getApplicationContext(),
								(Serializable) historyList);
					}

				}
				searchInfoLayout.setVisibility(View.GONE);
				getSearchData(searchEditText.getText().toString());
				hideKeyboard();
			}
		});

		clearTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				historyList.clear();
				adapter.notifyDataSetChanged();
				SerializationUtil.wirteSelectHistorySerialization(getApplicationContext(), (Serializable) historyList);
			}
		});

		searchEditText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int actionId, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					String searchString = searchEditText.getText().toString();
					if (!StringUtils.isEmpty(searchString)) {
						if (!historyList.contains(searchString)) {
							historyList.add(0, searchString);
							adapter.notifyDataSetChanged();
							SerializationUtil.wirteSelectHistorySerialization(getApplicationContext(),
									(Serializable) historyList);
						}
					}
					searchInfoLayout.setVisibility(View.GONE);
					getSearchData(searchEditText.getText().toString());
					hideKeyboard();
				}
				return false;
			}
		});

		hotLayout1 = (LinearLayout) findViewById(R.id.hot_layout1);
		hotLayout2 = (LinearLayout) findViewById(R.id.hot_layout2);
		hotTextView1 = (TextView) findViewById(R.id.hot_textView1);
		hotTextView2 = (TextView) findViewById(R.id.hot_textView2);
		hotTextView3 = (TextView) findViewById(R.id.hot_textView3);
		hotTextView4 = (TextView) findViewById(R.id.hot_textView4);
		hotTextView5 = (TextView) findViewById(R.id.hot_textView5);
		hotTextView6 = (TextView) findViewById(R.id.hot_textView6);
		hotTextViews = new ArrayList<>();
		hotTextViews.add(hotTextView1);
		hotTextViews.add(hotTextView2);
		hotTextViews.add(hotTextView3);
		hotTextViews.add(hotTextView4);
		hotTextViews.add(hotTextView5);
		hotTextViews.add(hotTextView6);

		initSearchList();

		imageView = (TextView) findViewById(R.id.none_imageView);
		imageView.setVisibility(View.GONE);
		
		hotListView = (ListView) findViewById(R.id.hot_listView);
		hotList = new ArrayList<>();
		hotAdapter = new SearchRecomAdapter(this, hotList);
		hotListView.setAdapter(hotAdapter);
		hotListView.setSelector(new ColorDrawable(android.R.color.transparent));
		hotListView.setDividerHeight(0);
		hotListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), VideoInfoActivity.class);
				intent.putExtra("movie_id", hotList.get(position).getId());
				intent.putExtra("type_id", hotList.get(position).getType_id());
				intent.putExtra("episodes_id", "0");
				startActivity(intent);
			}
		});
	}

	private MyScrollViewh scrollView;
	private LinearLayout movieLayout, starLayout, tvLayout, varietyLayout, musicLayout, newsLayout;

	private void initSearchList() {
		scrollView = (MyScrollViewh) findViewById(R.id.scrollView);
		movieLayout = (LinearLayout) findViewById(R.id.movie_layout);
		starLayout = (LinearLayout) findViewById(R.id.star_layout);
		tvLayout = (LinearLayout) findViewById(R.id.tv_layout);
		varietyLayout = (LinearLayout) findViewById(R.id.variety_layout);
		musicLayout = (LinearLayout) findViewById(R.id.music_layout);
		newsLayout = (LinearLayout) findViewById(R.id.news_layout);
	}

	private void setSearchList1(){
		final SearchType movie = search.getMovie();
		TextView movieMoreTextView = (TextView) findViewById(R.id.movie_more_layout);
		setSearchView("movie", "韩影", movie, movieLayout, movieMoreTextView);

		final SearchType tv = search.getTv();
		TextView tvMoreTextView = (TextView) findViewById(R.id.tv_more_layout);
		setSearchView("tv", "韩剧", tv, tvLayout, tvMoreTextView);

		final SearchType variety = search.getVariety();
		TextView varietyMoreTextView = (TextView) findViewById(R.id.variety_more_layout);
		setSearchView("variety", "韩综", variety, varietyLayout, varietyMoreTextView);

		final SearchType music = search.getMusic();
		TextView musicMoreTextView = (TextView) findViewById(R.id.music_more_layout);
		setSearchView("music", "音乐", music, musicLayout, musicMoreTextView);

		final SearchType news = search.getNews();
		TextView newsMoreTextView = (TextView) findViewById(R.id.news_more_layout);
		setSearchView("news", "资讯", news, newsLayout, newsMoreTextView);
		
		SearchStar searchStar = search.getStar();
		if (searchStar != null && searchStar.getTotal() > 0) {
			starLayout.setVisibility(View.VISIBLE);
			HorizontalListView horizontalListView = (HorizontalListView) findViewById(R.id.horizontalListView);
			final List<Star> list = searchStar.getResult();
			CircleAdapter circleAdapter = new CircleAdapter(getApplicationContext(), list);
			horizontalListView.setAdapter(circleAdapter);
			horizontalListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getApplicationContext(), StarInfoActivity.class);
					intent.putExtra("star_id", list.get(position).getId());
					startActivity(intent);
				}
			});
		} else {
			starLayout.setVisibility(View.GONE);
		}
	}
	
	private void setSearchList() {
		ImageLoader imageLoader = ImageLoader.getInstance();
		final SearchType movie = search.getMovie();
		if (movie != null && movie.getTotal() > 0) {
			movieLayout.setVisibility(View.VISIBLE);
			if (movieLayout.getChildCount() > 2) {
				movieLayout.removeViews(1, movieLayout.getChildCount() - 2);
			}
			int num = movie.getNum();
			int total = movie.getTotal();
			for (int i = 0; i < num; i++) {
				final SearchInfo searchInfo = movie.getResult().get(i);
				View view = View.inflate(getApplicationContext(), R.layout.listitem_collect, null);
				RoundedImageView roundedImageView = (RoundedImageView) view.findViewById(R.id.roundedImageView);
				imageLoader.displayImage(searchInfo.getImage(), roundedImageView, GlobalParams.bannerOptions);

				if (!StringUtils.isEmpty(searchInfo.getEpisode())) {
					View view2 = view.findViewById(R.id.black_view);
					TextView episodeTextView = (TextView) view.findViewById(R.id.episode_textView);
					episodeTextView.setText(searchInfo.getEpisode());
					episodeTextView.setVisibility(View.VISIBLE);
					view2.setVisibility(View.VISIBLE);
				}
				
				
				TextView titleTextView = (TextView) view.findViewById(R.id.title_textView);
				titleTextView.setText(searchInfo.getTitle());

				TextView introTextView = (TextView) view.findViewById(R.id.intro_textView);
				introTextView.setText(searchInfo.getDes());

//				TextView playTextView = (TextView) view.findViewById(R.id.play_textView);
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getApplicationContext(), VideoInfoActivity.class);
						intent.putExtra("movie_id", searchInfo.getId());
						intent.putExtra("type_id", searchInfo.getType_id());
						intent.putExtra("episodes_id", "0");
						startActivity(intent);
					}
				});
			

				
				movieLayout.addView(view, i + 1);
			}

			TextView moreTextView = (TextView) findViewById(R.id.movie_more_layout);
			if (total > num) {
				moreTextView.setVisibility(View.VISIBLE);
				moreTextView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getApplicationContext(), SearchMoreActivity.class);
						intent.putExtra("title", "韩影");
						intent.putExtra("type_id", "movie");
						intent.putExtra("keyword", searchString);
						intent.putExtra("list", (Serializable) movie.getResult());
						startActivity(intent);
					}
				});
			} else {
				moreTextView.setVisibility(View.GONE);
			}

		} else {
			movieLayout.setVisibility(View.GONE);
		}

		final SearchType tv = search.getTv();
		if (tv != null && tv.getTotal() > 0) {
			tvLayout.setVisibility(View.VISIBLE);
			if (tvLayout.getChildCount() > 2) {
				tvLayout.removeViews(1, tvLayout.getChildCount() - 2);
			}
			int num = tv.getNum();
			int total = tv.getTotal();
			for (int i = 0; i < num; i++) {
				final SearchInfo searchInfo = tv.getResult().get(i);
				View view = View.inflate(getApplicationContext(), R.layout.listitem_collect, null);
				RoundedImageView roundedImageView = (RoundedImageView) view.findViewById(R.id.roundedImageView);
				imageLoader.displayImage(searchInfo.getImage(), roundedImageView, GlobalParams.bannerOptions);
				TextView episodeTextView = (TextView) view.findViewById(R.id.episode_textView);
				episodeTextView.setText(searchInfo.getEpisode());
				episodeTextView.setVisibility(View.VISIBLE);
				TextView titleTextView = (TextView) view.findViewById(R.id.title_textView);
				titleTextView.setText(searchInfo.getTitle());

				TextView introTextView = (TextView) view.findViewById(R.id.intro_textView);
				introTextView.setText(searchInfo.getDes());

//				TextView playTextView = (TextView) view.findViewById(R.id.play_textView);
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getApplicationContext(), VideoInfoActivity.class);
						intent.putExtra("movie_id", searchInfo.getId());
						intent.putExtra("type_id", searchInfo.getType_id());
						intent.putExtra("episodes_id", "0");
						startActivity(intent);
					}
				});

				tvLayout.addView(view, i + 1);
			}
			TextView moreTextView = (TextView) findViewById(R.id.tv_more_layout);
			if (total > num) {
				moreTextView.setVisibility(View.VISIBLE);
				moreTextView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getApplicationContext(), SearchMoreActivity.class);
						intent.putExtra("title", "韩剧");
						intent.putExtra("type_id", "tv");
						intent.putExtra("keyword", searchString);
						intent.putExtra("list", (Serializable) tv.getResult());
						startActivity(intent);
					}
				});
			} else {
				moreTextView.setVisibility(View.GONE);
			}

		} else {
			tvLayout.setVisibility(View.GONE);
		}

		final SearchType variety = search.getVariety();
		if (variety != null && variety.getTotal() > 0) {
			varietyLayout.setVisibility(View.VISIBLE);
			if (varietyLayout.getChildCount() > 2) {
				varietyLayout.removeViews(1, varietyLayout.getChildCount() - 2);
			}
			int num = variety.getNum();
			int total = variety.getTotal();
			for (int i = 0; i < num; i++) {
				final SearchInfo searchInfo = variety.getResult().get(i);
				View view = View.inflate(getApplicationContext(), R.layout.listitem_collect, null);
				RoundedImageView roundedImageView = (RoundedImageView) view.findViewById(R.id.roundedImageView);
				imageLoader.displayImage(searchInfo.getImage(), roundedImageView, GlobalParams.bannerOptions);
				TextView episodeTextView = (TextView) view.findViewById(R.id.episode_textView);
				episodeTextView.setText(searchInfo.getEpisode());
				episodeTextView.setVisibility(View.VISIBLE);
				TextView titleTextView = (TextView) view.findViewById(R.id.title_textView);
				titleTextView.setText(searchInfo.getTitle());

				TextView introTextView = (TextView) view.findViewById(R.id.intro_textView);
				introTextView.setText(searchInfo.getDes());

//				TextView playTextView = (TextView) view.findViewById(R.id.play_textView);
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getApplicationContext(), VideoInfoActivity.class);
						intent.putExtra("movie_id", searchInfo.getId());
						intent.putExtra("type_id", searchInfo.getType_id());
						intent.putExtra("episodes_id", "0");
						startActivity(intent);
					}
				});

				varietyLayout.addView(view, i + 1);
			}
			TextView moreTextView = (TextView) findViewById(R.id.variety_more_layout);
			if (total > num) {
				moreTextView.setVisibility(View.VISIBLE);
				moreTextView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getApplicationContext(), SearchMoreActivity.class);
						intent.putExtra("title", "韩综");
						intent.putExtra("type_id", "variety");
						intent.putExtra("keyword", searchString);
						intent.putExtra("list", (Serializable) variety.getResult());
						startActivity(intent);
					}
				});
			} else {
				moreTextView.setVisibility(View.GONE);
			}

		} else {
			varietyLayout.setVisibility(View.GONE);
		}

		final SearchType music = search.getMusic();
		if (music != null && music.getTotal() > 0) {
			musicLayout.setVisibility(View.VISIBLE);
			if (musicLayout.getChildCount() > 2) {
				musicLayout.removeViews(1, musicLayout.getChildCount() - 2);
			}
			int num = music.getNum();
			int total = music.getTotal();
			for (int i = 0; i < num; i++) {
				final SearchInfo searchInfo = music.getResult().get(i);
				View view = View.inflate(getApplicationContext(), R.layout.listitem_collect, null);
				RoundedImageView roundedImageView = (RoundedImageView) view.findViewById(R.id.roundedImageView);
				imageLoader.displayImage(searchInfo.getImage(), roundedImageView, GlobalParams.bannerOptions);
				TextView episodeTextView = (TextView) view.findViewById(R.id.episode_textView);
				episodeTextView.setText(searchInfo.getEpisode());
				episodeTextView.setVisibility(View.VISIBLE);
				TextView titleTextView = (TextView) view.findViewById(R.id.title_textView);
				titleTextView.setText(searchInfo.getTitle());

				TextView introTextView = (TextView) view.findViewById(R.id.intro_textView);
				introTextView.setText(searchInfo.getDes());

//				TextView playTextView = (TextView) view.findViewById(R.id.play_textView);
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getApplicationContext(), VideoInfoActivity.class);
						intent.putExtra("movie_id", searchInfo.getId());
						intent.putExtra("type_id", searchInfo.getType_id());
						intent.putExtra("episodes_id", "0");
						startActivity(intent);
					}
				});
		
				musicLayout.addView(view, i + 1);
			}
			TextView moreTextView = (TextView) findViewById(R.id.music_more_layout);
			if (total > num) {
				moreTextView.setVisibility(View.VISIBLE);
				moreTextView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getApplicationContext(), SearchMoreActivity.class);
						intent.putExtra("title", "音乐");
						intent.putExtra("type_id", "music");
						intent.putExtra("keyword", searchString);
						intent.putExtra("list", (Serializable) music.getResult());
						startActivity(intent);
					}
				});
			} else {
				moreTextView.setVisibility(View.GONE);
			}

		} else {
			musicLayout.setVisibility(View.GONE);
		}

		final SearchType news = search.getNews();
		if (news != null && news.getTotal() > 0) {
			newsLayout.setVisibility(View.VISIBLE);
			if (newsLayout.getChildCount() > 2) {
				newsLayout.removeViews(1, newsLayout.getChildCount() - 2);
			}
			int num = news.getNum();
			int total = news.getTotal();
			for (int i = 0; i < num; i++) {
				final SearchInfo searchInfo = news.getResult().get(i);
				View view = View.inflate(getApplicationContext(), R.layout.listitem_collect, null);
				RoundedImageView roundedImageView = (RoundedImageView) view.findViewById(R.id.roundedImageView);
				imageLoader.displayImage(searchInfo.getImage(), roundedImageView, GlobalParams.bannerOptions);
				TextView episodeTextView = (TextView) view.findViewById(R.id.episode_textView);
				episodeTextView.setText(searchInfo.getEpisode());
				episodeTextView.setVisibility(View.VISIBLE);
				TextView titleTextView = (TextView) view.findViewById(R.id.title_textView);
				titleTextView.setText(searchInfo.getTitle());

				TextView introTextView = (TextView) view.findViewById(R.id.intro_textView);
				introTextView.setText(searchInfo.getDes());

//				TextView playTextView = (TextView) view.findViewById(R.id.play_textView);
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getApplicationContext(), VideoInfoActivity.class);
						intent.putExtra("movie_id", searchInfo.getId());
						intent.putExtra("type_id", searchInfo.getType_id());
						intent.putExtra("episodes_id", "0");
						startActivity(intent);
					}
				});
				
				newsLayout.addView(view, i + 1);
			}
			
			
			TextView moreTextView = (TextView) findViewById(R.id.news_more_layout);
			if (total > num) {
				moreTextView.setVisibility(View.VISIBLE);
				moreTextView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getApplicationContext(), SearchMoreActivity.class);
						intent.putExtra("title", "资讯");
						intent.putExtra("type_id", "news");
						intent.putExtra("keyword", searchString);
						intent.putExtra("list", (Serializable) news.getResult());
						startActivity(intent);
					}
				});
			} else {
				moreTextView.setVisibility(View.GONE);
			}

		} else {
			newsLayout.setVisibility(View.GONE);
		}

		SearchStar searchStar = search.getStar();
		if (searchStar != null && searchStar.getTotal() > 0) {
			starLayout.setVisibility(View.VISIBLE);
			HorizontalListView horizontalListView = (HorizontalListView) findViewById(R.id.horizontalListView);
			final List<Star> list = searchStar.getResult();
			CircleAdapter circleAdapter = new CircleAdapter(getApplicationContext(), list);
			horizontalListView.setAdapter(circleAdapter);
			horizontalListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getApplicationContext(), StarInfoActivity.class);
					intent.putExtra("star_id", list.get(position).getId());
					startActivity(intent);
				}
			});
		} else {
			starLayout.setVisibility(View.GONE);
		}

	}

	
	private void setSearchView(final String type_id, final String title, final SearchType searchType,
			LinearLayout layout, TextView moreTextView){
		ImageLoader imageLoader = ImageLoader.getInstance();

		if (searchType != null && searchType.getTotal() > 0) {
			layout.setVisibility(View.VISIBLE);
			if (layout.getChildCount() > 2) {
				layout.removeViews(1, layout.getChildCount() - 2);
			}
			int num = searchType.getNum();
			int total = searchType.getTotal();
			for (int i = 0; i < num; i++) {
				final SearchInfo searchInfo = searchType.getResult().get(i);
				View view = View.inflate(getApplicationContext(), R.layout.listitem_collect, null);
				RoundedImageView roundedImageView = (RoundedImageView) view.findViewById(R.id.roundedImageView);
				imageLoader.displayImage(searchInfo.getImage(), roundedImageView, GlobalParams.bannerOptions);

				if (!StringUtils.isEmpty(searchInfo.getEpisode())) {
					View view2 = view.findViewById(R.id.black_view);
					TextView episodeTextView = (TextView) view.findViewById(R.id.episode_textView);
					episodeTextView.setText(searchInfo.getEpisode());
					episodeTextView.setVisibility(View.VISIBLE);
					view2.setVisibility(View.VISIBLE);
				}
				
				
				TextView titleTextView = (TextView) view.findViewById(R.id.title_textView);
				titleTextView.setText(searchInfo.getTitle());

				TextView introTextView = (TextView) view.findViewById(R.id.intro_textView);
				introTextView.setText(searchInfo.getDes());

//				TextView playTextView = (TextView) view.findViewById(R.id.play_textView);
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getApplicationContext(), VideoInfoActivity.class);
						intent.putExtra("movie_id", searchInfo.getId());
						intent.putExtra("type_id", searchInfo.getType_id());
						intent.putExtra("episodes_id", "0");
						startActivity(intent);
					}
				});
			

				
				layout.addView(view, i + 1);
			}

			if (total > num) {
				moreTextView.setVisibility(View.VISIBLE);
				moreTextView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getApplicationContext(), SearchMoreActivity.class);
						intent.putExtra("title", title);
						intent.putExtra("type_id", type_id);
						intent.putExtra("keyword", searchString);
						intent.putExtra("list", (Serializable) searchType.getResult());
						startActivity(intent);
					}
				});
			} else {
				moreTextView.setVisibility(View.GONE);
			}

		} else {
			layout.setVisibility(View.GONE);
		}
	}
	
	private Search search;
	private String searchString="";

	private void getSearchData(final String string) {
		searchString = string;
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (search != null) {
					imageView.setVisibility(View.GONE);
					scrollView.setVisibility(View.VISIBLE);
					setSearchList1();
				} else {
					imageView.setVisibility(View.VISIBLE);
					scrollView.setVisibility(View.GONE);
					Toast.makeText(getApplicationContext(), "未搜索到相关信息", 1).show();
				}

			}
		};

		new DataAsyncTask(callback, true) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				SearchApi searchApi = new SearchApi();
				search = searchApi.getSearchList(string, "1", "100000");
				return null;
			}
		}.execute("");
	}

	private void getData() {
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (getHotSearchList != null && getHotSearchList.size() > 0) {
					hotSearchList.addAll(getHotSearchList);
					hotLayout1.setVisibility(View.VISIBLE);
					if (hotSearchList.size() > 3) {
						hotLayout2.setVisibility(View.VISIBLE);
					}
					for (int i = 0; i < 6; i++) {
						TextView textView = hotTextViews.get(i);
						if (i < hotSearchList.size()) {
							textView.setVisibility(View.VISIBLE);
							final HotSearch hotSearch = hotSearchList.get(i);
							textView.setText(hotSearch.getName());
							textView.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									String searchString = hotSearch.getName();
									searchInfoLayout.setVisibility(View.GONE);
									getSearchData(searchString);
								}
							});
						} else {
							textView.setVisibility(View.INVISIBLE);
						}

					}
				}
				
				if (getHotList != null) {
					hotList.addAll(getHotList);
					hotAdapter.notifyDataSetChanged();
				}
			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				SearchApi searchApi = new SearchApi();
				getHotSearchList = searchApi.getHotSearchList("10");
				getHotList = searchApi.getRecomSearchList("5");
				
				return null;
			}
		}.execute("");
	}

	private void hideKeyboard() {
		InputTools.HideKeyboard(searchEditText);
	}
}
