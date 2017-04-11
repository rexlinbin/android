package com.bccv.zhuiyingzhihanju.activity;

import java.util.ArrayList;
import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.adapter.MoreVedioAdapter;
import com.bccv.zhuiyingzhihanju.api.MovieListApi;
import com.bccv.zhuiyingzhihanju.fragment.FancyFragment;
import com.bccv.zhuiyingzhihanju.fragment.FilterFragment;
import com.bccv.zhuiyingzhihanju.fragment.YMyFrament;
import com.bccv.zhuiyingzhihanju.model.Movie;

import com.utils.net.NetUtil;
import com.utils.pulltorefresh.FooterLoadingLayout;
import com.utils.pulltorefresh.PullToRefreshBase;
import com.utils.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.utils.pulltorefresh.PullToRefreshListView;
import com.utils.tools.AppConfig;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;
import com.utils.tools.GlobalParams;
import com.utils.tools.PromptManager;
import com.utils.tools.StringUtils;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MoreVideoActivity extends BaseActivity {

	PullToRefreshListView MoreListView;
	private ListView listView;
	private List<Movie> list, getList;
	MoreVedioAdapter adapter;
	private String type;
	private int page = 1, count = 15;
	private ImageButton back;
	
	private RelativeLayout s;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_morevideo);

		back = (ImageButton) findViewById(R.id.titelMore_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	
		 s = (RelativeLayout) findViewById(R.id.more_re);
//		t.setSelected(true);
//		t.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				t.setSelected(true);
//				s.setSelected(false);
//			}
//		});

		s.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
			
				
				
				Intent aIntent = new Intent(MoreVideoActivity.this, FilterFragment.class);
				aIntent.putExtra("isSelect", "S");
				startActivity(aIntent);
				
			}
		});

		MoreListView = (PullToRefreshListView) findViewById(R.id.MoreVedio_listView);

		listView = MoreListView.getRefreshableView();

		listView.setVerticalScrollBarEnabled(false);
		listView.setDividerHeight(0);
		list = new ArrayList<Movie>();

		adapter = new MoreVedioAdapter(getApplication(), list, MoreVideoActivity.this);

		type = getIntent().getStringExtra("type");

		listView.setAdapter(adapter);

		MoreListView.setPullLoadEnabled(true);
		MoreListView.setPullRefreshEnabled(true);
		MoreListView.getRefreshableView().setSelector(new ColorDrawable(android.R.color.transparent));
		MoreListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if (!NetUtil.isNetworkAvailable(GlobalParams.context)) {
					// 提示网络不给力,直接完成刷新
					PromptManager.showToast(GlobalParams.context, "网络不给力");

					MoreListView.onPullDownRefreshComplete();
				} else {
					page = 1;
					getData(true);

				}
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if (NetUtil.isNetworkAvailable(GlobalParams.context)) {
					((FooterLoadingLayout) MoreListView.getFooterLoadingLayout()).getmHintView().setText("数据加载中...");
					getData(false);
				} else {
					PromptManager.showToast(GlobalParams.context, "网络不给力");
					MoreListView.onPullUpRefreshComplete();
				}
			}
		});
//
//		listView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				// TODO Auto-generated method stub
//				Intent aIntent = new Intent(MoreVideoActivity.this, VideoInfoActivity.class);
//
//				aIntent.putExtra("movie_id", list.get(position).getId());
//				aIntent.putExtra("type_id", list.get(position).getType_id());
//				aIntent.putExtra("episodes_id", "0");
//
//				
//				
//				
//				
//				
//				startActivity(aIntent);
//			}
//		});

		
		
		
		getData(true);

	}
	public void goMoreInfo(int position){
		Intent intent = new Intent(getApplicationContext(), VideoInfoActivity.class);
		intent.putExtra("movie_id", list.get(position).getId());
		intent.putExtra("type_id", list.get(position).getType_id());
		intent.putExtra("episodes_id", "0");
		startActivity(intent);
	}	
	
	private void getData(final boolean isRefresh) {

		if (isRefresh) {
			page = 1;
		}

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {

				// TODO Auto-generated method stub
				if (getList != null && getList.size() > 0) {
					if (isRefresh) {
						list.clear();
					}
					list.addAll(getList);
					adapter.notifyDataSetChanged();
					page++;
				} else {

					showShortToast("已加载全部数据");
				}
				if (isRefresh) {
					MoreListView.onPullDownRefreshComplete();
				} else {
					MoreListView.onPullUpRefreshComplete();
				}

			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				MovieListApi moreApi = new MovieListApi();
				getList = moreApi.getMoreList(type, page + "", count + "");
				return null;
			}
		}.execute("");

	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//	
//
//			if (resultCode == 0) {
//				
//				
//				
//				
//				s.setSelected(false);
//				
//				
//				
//				
//			}
//			
//
//		super.onActivityResult(requestCode, resultCode, data);
//	}
//	
	
}
