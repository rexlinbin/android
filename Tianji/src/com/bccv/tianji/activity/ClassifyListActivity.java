package com.bccv.tianji.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bccv.tianji.R;
import com.bccv.tianji.adapter.ClassifyListAdapter;
import com.bccv.tianji.adapter.MyDownloadAdapter;
import com.bccv.tianji.api.GameApi;
import com.bccv.tianji.api.TcpApi;
import com.bccv.tianji.model.Game;
import com.utils.net.NetUtil;
import com.utils.pulltorefresh.FooterLoadingLayout;
import com.utils.pulltorefresh.PullToRefreshBase;
import com.utils.pulltorefresh.PullToRefreshListView;
import com.utils.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;
import com.utils.tools.GlobalParams;
import com.utils.tools.Logger;
import com.utils.tools.PromptManager;
import com.utils.tools.StringUtils;

public class ClassifyListActivity extends BaseActivity {
	private PullToRefreshListView pullToRefreshListView;
	private ClassifyListAdapter adapter;
	private List<Game> list, getList;
	
	private int page = 1, count = 10;
	private String classify_id, classify_name;
	
	private Timer timer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_classifylist);
		classify_id = getIntent().getStringExtra("classify_id");
		classify_name = getIntent().getStringExtra("classify_name");
		setBack();
		setView();
	}
	
	private ImageView backTextView;

	private void setBack() {
		backTextView = (ImageView) findViewById(R.id.back_textView);
		backTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();		
			}
		});
	}
	
	private void setView(){
		TextView titleTextView = (TextView) findViewById(R.id.title_textView);
		titleTextView.setText(classify_name);
		
		list = new ArrayList<Game>();
		adapter = new ClassifyListAdapter(getApplicationContext(), list);
		
		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView);
		pullToRefreshListView.getRefreshableView().setAdapter(adapter);
		pullToRefreshListView.setPullLoadEnabled(true);
		pullToRefreshListView.setPullRefreshEnabled(true);
		pullToRefreshListView.getRefreshableView().setSelector(
				new ColorDrawable(android.R.color.transparent));
		pullToRefreshListView.getRefreshableView().setDividerHeight(0);
		pullToRefreshListView.doPullRefreshing(true, 100);
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if (!NetUtil.isNetworkAvailable(GlobalParams.context)) {
					// 提示网络不给力,直接完成刷新
					PromptManager.showToast(GlobalParams.context, "网络不给力");
					
					pullToRefreshListView.onPullDownRefreshComplete();
				} else {
					getData(true);
					

				}
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if (NetUtil.isNetworkAvailable(GlobalParams.context)) {
					((FooterLoadingLayout) pullToRefreshListView.getFooterLoadingLayout())
							.getmHintView().setText("数据加载中...");
					getData(false);
				} else {
					PromptManager.showToast(GlobalParams.context, "网络不给力");
					pullToRefreshListView.onPullUpRefreshComplete();
				}
			}

		});
		
		pullToRefreshListView.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent aItent = new Intent(ClassifyListActivity.this,
						DetailsActivity.class);

				aItent.putExtra("gameID", list.get(position).getGame_id());

				startActivity(aItent);
			}
		});
	}
	
	private void getData(final boolean isRefresh){
		if (isRefresh) {
			page = 1;
		}
		Callback callback = new Callback() {
			
			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (getList != null) {
					if (isRefresh) {
						list.clear();
					}
					list.addAll(getList);
					adapter.notifyDataSetChanged();
				}
				
				if (isRefresh) {
					pullToRefreshListView.onPullDownRefreshComplete();
				}else {
					pullToRefreshListView.onPullUpRefreshComplete();
				}
				
				if (timer != null) {
					timer.cancel();
					timer = new Timer();
				}else{
					timer = new Timer();
				}
				TimerTask task = new TimerTask() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						TcpApi tcpApi = new TcpApi();
						if (!StringUtils.isEmpty(GlobalParams.auth_id)) {
							tcpApi.getDownloadGameListState(GlobalParams.user.getUser_id(), GlobalParams.auth_id);
						}
						
					}
				};
				
				if (GlobalParams.user != null && !StringUtils.isEmpty(GlobalParams.auth_id)) {
					timer.schedule(task, 1000, 10000);
				}
			}
		};
		
		new DataAsyncTask(callback, false) {
			
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				GlobalParams.tcpClientHelper.setHandler(handler);
				GameApi gameApi = new GameApi();
				getList = gameApi.getClassifyListList(classify_id, page + "", count + "");
				page++;
				return null;
			}
		}.execute("");
	}
	
	private Handler handler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				String data = (String) msg.obj;
				Logger.e("data111", data);
				if (!StringUtils.isEmpty(data)) {
					try {
						JSONObject jsonObject = JSON.parseObject(data);
						String flag = jsonObject.getString("flag");
						if (flag.equals("task_list")) {
							JSONArray jsonArray = jsonObject.getJSONArray("info");
							for (int i = 0; i < list.size(); i++) {
								Game game = list.get(i);
								for (int j = 0; j < jsonArray.size(); j++) {
									JSONObject jsonObject2 = jsonArray.getJSONObject(j);
									if (game.getGame_id().equals(jsonObject2.getString("game_id"))) {
										game.setStatus(jsonObject2.getString("download_status"));
										game.setDownload_size(jsonObject2.getFloatValue("download_size"));
									}
								}
							}
							
							
							adapter.notifyDataSetChanged();
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;

			default:
				break;
			}
		}
	};
}
