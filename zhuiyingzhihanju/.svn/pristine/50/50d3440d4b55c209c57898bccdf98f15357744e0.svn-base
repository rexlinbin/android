package com.bccv.zhuiyingzhihanju.activity;

import java.util.ArrayList;
import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.adapter.AreaAdapter;
import com.bccv.zhuiyingzhihanju.adapter.MovieListAdapter;
import com.bccv.zhuiyingzhihanju.adapter.TypeAdapter;
import com.bccv.zhuiyingzhihanju.adapter.YearAdapter;
import com.bccv.zhuiyingzhihanju.api.FoundApi;
import com.bccv.zhuiyingzhihanju.model.Movie;
import com.bccv.zhuiyingzhihanju.model.MovieArea;
import com.bccv.zhuiyingzhihanju.model.MovieGenres;
import com.bccv.zhuiyingzhihanju.model.MovieType;
import com.bccv.zhuiyingzhihanju.model.MovieYear;
import com.tendcloud.tenddata.TCAgent;
import com.utils.net.NetUtil;
import com.utils.pulltorefresh.FooterLoadingLayout;
import com.utils.pulltorefresh.PullToRefreshBase;
import com.utils.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.utils.pulltorefresh.PullToRefreshGridView;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;
import com.utils.tools.GlobalParams;
import com.utils.tools.PromptManager;
import com.utils.views.HorizontalListView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MovieListActivity extends BaseActivity {
	private LinearLayout sortLayout;
	private HorizontalListView areaListView, genresListView, yearListView;
	private List<MovieArea> areaList;
	private List<MovieGenres> genresList;
	private List<MovieYear> yearList;
	
	private AreaAdapter areaAdapter;
	private TypeAdapter typeAdapter;
	private YearAdapter yearAdapter;
	
	private PullToRefreshGridView pullToRefreshGridView;
	private GridView gridView;
	
	private List<Movie> list, getList;
	private MovieListAdapter adapter;
	
	private String type_id, title;//0--搜索  
	private String area = "全部", genera = "全部", year = "全部";
	private int currArea = 0, currGenres = 0, currYear = 0;
	private MovieType movieType;
	
	boolean isSortShow = false;
	
	private void tcStart(){
		TCAgent.onPageStart(getApplicationContext(), "MovieListActivity");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		TCAgent.onPageEnd(getApplicationContext(), "MovieListActivity");
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tcStart();
		setContentView(R.layout.activity_movielist);
		type_id = getIntent().getStringExtra("type_id");
		title = getIntent().getStringExtra("title");
		setTitle();
		initView();
		getData(true);
	}
	
	private void setTitle() {
		ImageButton back = (ImageButton) findViewById(R.id.titel_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		ImageButton search = (ImageButton) findViewById(R.id.titel_search);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		
		ImageButton sort = (ImageButton) findViewById(R.id.titel_sort);
		sort.setVisibility(View.VISIBLE);
		sort.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isSortShow = !isSortShow;
				if (!isSortShow) {
					sortLayout.setVisibility(View.GONE);
				}else{
					sortLayout.setVisibility(View.VISIBLE);
				}
				
			}
		});
		
		TextView titleName = (TextView) findViewById(R.id.titleName_textView);
		titleName.setVisibility(View.VISIBLE);
		titleName.setText(title);
	}
	
	private void initView(){
		sortLayout = (LinearLayout) findViewById(R.id.sort_layout);
		areaListView = (HorizontalListView) findViewById(R.id.countries_horizontalListView);
		genresListView = (HorizontalListView) findViewById(R.id.type_horizontalListView);
		yearListView = (HorizontalListView) findViewById(R.id.year_horizontalListView);
		
		areaList = new ArrayList<MovieArea>();
		genresList = new ArrayList<MovieGenres>();
		yearList = new ArrayList<MovieYear>();
		
		areaAdapter = new AreaAdapter(getApplicationContext(), areaList);
		typeAdapter = new TypeAdapter(getApplicationContext(), genresList);
		yearAdapter = new YearAdapter(getApplicationContext(), yearList);
		
		areaListView.setAdapter(areaAdapter);
		genresListView.setAdapter(typeAdapter);
		yearListView.setAdapter(yearAdapter);
		
		areaListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				areaList.get(currArea).setSelect(false);
				areaList.get(position).setSelect(true);
				currArea = position;
				area = areaList.get(position).getCountries();
				areaAdapter.notifyDataSetChanged();
				getData(true);
			}
		});
		
		genresListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				genresList.get(currGenres).setSelect(false);
				genresList.get(position).setSelect(true);
				currGenres = position;
				genera = genresList.get(position).getName();
				typeAdapter.notifyDataSetChanged();
				getData(true);
			}
		});
		
		yearListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				yearList.get(currYear).setSelect(false);
				yearList.get(position).setSelect(true);
				currYear = position;
				year = yearList.get(position).getYear();
				yearAdapter.notifyDataSetChanged();
				getData(true);
			}
		});
		
		pullToRefreshGridView = (PullToRefreshGridView) findViewById(R.id.pullToRefreshGridView);
		gridView = pullToRefreshGridView.getRefreshableView();
		gridView.setNumColumns(3);
		gridView.setVerticalScrollBarEnabled(false);
		list = new ArrayList<Movie>();
		adapter = new MovieListAdapter(getApplicationContext(), list);
		gridView.setAdapter(adapter);
		gridView.setSelector(new ColorDrawable(android.R.color.transparent));
		pullToRefreshGridView.setPullLoadEnabled(true);
		pullToRefreshGridView.setPullRefreshEnabled(true);
		pullToRefreshGridView.getRefreshableView().setSelector(
				new ColorDrawable(android.R.color.transparent));
		pullToRefreshGridView.setOnRefreshListener(new OnRefreshListener<GridView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				// TODO Auto-generated method stub
				if (!NetUtil.isNetworkAvailable(GlobalParams.context)) {
					// 提示网络不给力,直接完成刷新
					PromptManager.showToast(GlobalParams.context, "网络不给力");

					pullToRefreshGridView.onPullDownRefreshComplete();
				} else {
					getData(true);

				}
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				// TODO Auto-generated method stub
				if (NetUtil.isNetworkAvailable(GlobalParams.context)) {
					((FooterLoadingLayout) pullToRefreshGridView.getFooterLoadingLayout())
							.getmHintView().setText("数据加载中...");
					getData(false);
				} else {
					PromptManager.showToast(GlobalParams.context, "网络不给力");
					pullToRefreshGridView.onPullUpRefreshComplete();
				}
			}

		});
		
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), MovieInfoActivity.class);
				intent.putExtra("movie", list.get(position));
				startActivity(intent);
			}
		});
	}
	
	private int page = 1, count = 12;
	private void getData(final boolean isRefresh) {
		if (isRefresh) {
			page = 1;
		}
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (movieType != null && areaList.size() <= 0) {
					MovieArea movieArea = new MovieArea();
					movieArea.setCountries("全部");
					movieArea.setSelect(true);
					movieType.getArea().add(0, movieArea);
					areaList.addAll(movieType.getArea());
					MovieGenres movieGenres = new MovieGenres();
					movieGenres.setName("全部");
					movieGenres.setSelect(true);
					movieType.getType().add(0, movieGenres);
					genresList.addAll(movieType.getType());
					MovieYear movieYear = new MovieYear();
					movieYear.setYear("全部");
					movieYear.setSelect(true);
					movieType.getYear().add(0, movieYear);
					yearList.addAll(movieType.getYear());
					
					areaAdapter.notifyDataSetChanged();
					typeAdapter.notifyDataSetChanged();
					yearAdapter.notifyDataSetChanged();
				}
				
				if (getList != null) {
					if (isRefresh) {
						list.clear();
					}
					
					for (int i = 0; i < getList.size(); i++) {
						Movie movie = getList.get(i);
						if (!list.contains(movie)) {
							list.add(movie);
						}
					}
					
//					list.addAll(getList);
					adapter.notifyDataSetChanged();
				}
				if (isRefresh) {
					pullToRefreshGridView.onPullDownRefreshComplete();
				} else {
					pullToRefreshGridView.onPullUpRefreshComplete();
				}
			}
		};

		new DataAsyncTask(callback, true) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				FoundApi foundApi = new FoundApi();
				
				if (areaList.size() <= 0) {
					movieType = foundApi.getFindTypeList(type_id);
				}
				
				getList = foundApi.getFindList(type_id, area, genera, year, page + "", count + "");
				page++;
				return null;
			}
		}.execute("");
	}
}
