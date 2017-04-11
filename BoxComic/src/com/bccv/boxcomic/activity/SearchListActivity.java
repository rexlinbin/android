package com.bccv.boxcomic.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.adapter.MainGridviewAdapter;
import com.bccv.boxcomic.api.ChannelApi;
import com.bccv.boxcomic.api.SearchApi;
import com.bccv.boxcomic.modal.Comic;
import com.bccv.boxcomic.net.NetUtil;
import com.bccv.boxcomic.pulltorefresh.FooterLoadingLayout;
import com.bccv.boxcomic.pulltorefresh.PullToRefreshBase;
import com.bccv.boxcomic.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.bccv.boxcomic.pulltorefresh.PullToRefreshGridView;
import com.bccv.boxcomic.tool.BaseActivity;
import com.bccv.boxcomic.tool.GlobalParams;
import com.bccv.boxcomic.tool.PromptManager;

public class SearchListActivity extends BaseActivity {

	private PullToRefreshGridView gridView;
	private List<Comic> data;
	private MainGridviewAdapter adapter;
	private int page = 1;
	private String keyword;
	private String result;
	private RelativeLayout back;
	private String type;
	String title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchlist);
	
String title=getIntent().getStringExtra("textTitle");


TextView titleView=(TextView) findViewById(R.id.search_textView1);
titleView.setText(title);
		gridView = (PullToRefreshGridView) findViewById(R.id.searchlist_pullToRefreshGridView);
		back = (RelativeLayout) findViewById(R.id.back_relativeLayout);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finishActivityWithAnim();
			}
		});
		data = new ArrayList<Comic>();
		keyword = getIntent().getStringExtra("key");
		type = getIntent().getStringExtra("type");
		adapter = new MainGridviewAdapter(data, SearchListActivity.this);
		if(!type.equals("Channel")){
			adapter.Sate(1);
		}

		gridView.getRefreshableView().setAdapter(adapter);
		gridView.setPullLoadEnabled(true);
		gridView.setPullRefreshEnabled(true);
		gridView.getRefreshableView().setNumColumns(3);
	
		gridView.getRefreshableView().setSelector(
				new ColorDrawable(android.R.color.transparent));

		gridView.setOnRefreshListener(new OnRefreshListener<GridView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				// TODO Auto-generated method stub
				if (!NetUtil.isNetworkAvailable(GlobalParams.context)) {
					// 提示网络不给力,直接完成刷新
					PromptManager.showToast(GlobalParams.context, "网络不给力");
					gridView.onPullDownRefreshComplete();
				} else {
					result = "refresh";
					new MainTask().execute(result);
				}
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				// TODO Auto-generated method stub
				if (NetUtil.isNetworkAvailable(GlobalParams.context)) {
					((FooterLoadingLayout) gridView.getFooterLoadingLayout())
							.getmHintView().setText("数据加载中...");
					result = "more";
					new MainTask().execute(result);

				} else {
					PromptManager.showToast(GlobalParams.context, "网络不给力");
					gridView.onPullUpRefreshComplete();
				}
			}
		});

		gridView.getRefreshableView().setOnItemClickListener(
				new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub

						Intent aintent = new Intent(SearchListActivity.this,
								ComicInfoActivity.class);

						aintent.putExtra("mainitem", data.get(arg2).getComic_id());

						startActivity(aintent);

					}
				});
		result = "refresh";
		new MainTask().execute(result);

	}

	class MainTask extends AsyncTask<String, Void, List<Comic>> {

		@Override
		protected List<Comic> doInBackground(String... params) {
			String re = params[0];

			if (re.equals("refresh")) {

				page = 1;
				data.clear();
			} else if (re.equals("more")) {

				page++;

			}

			if (type.equals("Channel")) {

				ChannelApi channelApi = new ChannelApi();

				List<Comic> list = channelApi.getCatList("", "", page, 12,keyword);

				return list;
			} else if (type.equals("EbookChannel")) {

				ChannelApi channelApi = new ChannelApi();

				List<Comic> list = channelApi.getCatList("", "", page, 12,keyword);

				return list;
			} else {

				SearchApi searchapi = new SearchApi();

				List<Comic> list = searchapi.getSearchList("", "", page, 12,keyword);

				return list;
			}

			

		}

		@Override
		protected void onPostExecute(List<Comic> list) {
			super.onPostExecute(list);

			if(list==null){
				
				Toast.makeText(SearchListActivity.this, "对不起，没有找到搜索内容", Toast.LENGTH_SHORT).show();
				
				
			}
			
			
			try {

				data.addAll(list);
				adapter.notifyDataSetChanged();
				if (page == 1) {
					gridView.onPullDownRefreshComplete();

				} else {
					gridView.onPullUpRefreshComplete();

				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e("MainChildFragment", e.toString());
			}
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}


}
