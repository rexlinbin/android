package com.bccv.boxcomic.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.adapter.InfoAnalysisAdapter;
import com.bccv.boxcomic.api.AnalysisApi;
import com.bccv.boxcomic.modal.InfoAnalysis;
import com.bccv.boxcomic.net.NetUtil;
import com.bccv.boxcomic.pulltorefresh.FooterLoadingLayout;
import com.bccv.boxcomic.pulltorefresh.PullToRefreshBase;
import com.bccv.boxcomic.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.bccv.boxcomic.pulltorefresh.PullToRefreshListView;
import com.bccv.boxcomic.tool.BaseActivity;
import com.bccv.boxcomic.tool.Callback;
import com.bccv.boxcomic.tool.GlobalParams;
import com.bccv.boxcomic.tool.PromptManager;

public class InfoAnalysisListActivity extends BaseActivity {
	
	private PullToRefreshListView pullToRefreshListView;
	private InfoAnalysisAdapter adapter;
	private List<InfoAnalysis> list;
	private List<InfoAnalysis> getAnalysis;
	private Activity activity;
	
	private String comic_id;
	private int pageNum = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = this;
		setContentView(R.layout.activity_infoanalysislist);
	
		comic_id = getIntent().getExtras().getString("comic_id");
		if (comic_id == null) {
			comic_id = "";
		}
		setBack();
		
		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.infoanalysis_pullToRefreshListView);
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if (!NetUtil.isNetworkAvailable(GlobalParams.context)) {
					// 提示网络不给力,直接完成刷新
					PromptManager.showToast(GlobalParams.context,
							"网络不给力");
					pullToRefreshListView.onPullDownRefreshComplete();
				} else {
					fetchServiceData(false, false);
				}
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if (NetUtil.isNetworkAvailable(GlobalParams.context)) {
					((FooterLoadingLayout)pullToRefreshListView.getFooterLoadingLayout()).getmHintView()
							.setText("数据加载中...");
					
					fetchServiceData(true, false);
				} else {
					PromptManager.showToast(GlobalParams.context,
							"网络不给力");
					pullToRefreshListView.onPullUpRefreshComplete();
				}
			}
		});
		list = new ArrayList<InfoAnalysis>();
		adapter = new InfoAnalysisAdapter(getApplicationContext(), list);
		pullToRefreshListView.getRefreshableView().setAdapter(adapter);
		pullToRefreshListView.getRefreshableView().setSelector(R.drawable.listitem_analysis);
		pullToRefreshListView.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(activity, InfoAnalysisContentActivity.class);
				intent.putExtra("advices_id", list.get(position).getAdvices_id());
				startActivity(intent);
			}
		});
		fetchServiceData(false, false);
		
	}
	
	
	
	private void fetchServiceData(final boolean isLoadMore, boolean canProgress) {
		boolean isNetworkAvailable = NetUtil.isNetworkAvailable(activity);
		if (!isNetworkAvailable) {
			PromptManager.showToast(activity, "网络不给力");
			return;
		}
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (result.equals("refresh")) {
					if (getAnalysis != null) {
						list.clear();
						list.addAll(getAnalysis);
					}
					
					pullToRefreshListView.onPullDownRefreshComplete();
				} else if (result.equals("more")) {
					if (getAnalysis != null) {
						list.addAll(getAnalysis);
					}
					pullToRefreshListView.onPullUpRefreshComplete();
					
				}
				
				adapter.notifyDataSetChanged();
			}
		};

		new DataAsyncTask(callback, canProgress) {

			

			@Override
			protected String doInBackground(String... params) {
				AnalysisApi analysisApi = new AnalysisApi();
				if (isLoadMore) {
					pageNum++;
				}else {
					pageNum = 1;
				}
				getAnalysis = analysisApi.getInfoAnalysisList(comic_id, pageNum + "", "10");
				if (isLoadMore) {
					return "more";
				}else {
					return "refresh";
				}
			}
		}.executeProxy("");

	}
	
	private void setBack(){
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.back_relativeLayout);
		relativeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
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
