package com.bccv.bangyangapp.ui.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.bccv.bangyangapp.R;
import com.bccv.bangyangapp.api.NetWorkAPI;
import com.bccv.bangyangapp.model.AppInfoItemBean;
import com.bccv.bangyangapp.model.CategoryAppListResBean;
import com.bccv.bangyangapp.network.HttpCallback;
import com.bccv.bangyangapp.network.NetResBean;
import com.bccv.bangyangapp.ui.adapter.HotFreshChildListAdapter;
import com.bccv.bangyangapp.ui.adapter.HotFreshChildListAdapter.OnBtnClickListener;
import com.bccv.bangyangapp.ui.view.BackGroundView;
import com.bccv.bangyangapp.ui.view.XListView;
import com.bccv.bangyangapp.ui.view.XListView.IXListViewListener;
import com.bccv.bangyangapp.utils.L;
import com.bccv.bangyangapp.utils.SystemUtil;

public class ClassificationActivity extends BaseActivity implements IXListViewListener, OnItemClickListener {

	public static final int DATA_UPDATE = 1;
	public static final String CATEGORY_ID_KEY = "category_id_key";
	public static final String CATEGORY_NAME_KEY = "category_name_key";
	
	private BackGroundView background_view;
	private View errorView, loadingView;
	private View common_title_back_btn, common_title_right_btn;
	private TextView common_title_tv;
	private XListView xListView;
	
	private int[] background;

	private Handler mHandler;
	private ArrayList<AppInfoItemBean> appList = new ArrayList<AppInfoItemBean>();
	
	private OnBtnClickListener onItemBtnClickListener;
	
	private HotFreshChildListAdapter mAdapter;
	
	private String category_id = null;
	private int currentPageId = 0;
	private String categoryName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_classification_list);
		
		Intent intent = getIntent();
		background = intent.getIntArrayExtra(BackGroundView.BACKGROUND_COLOR);
		category_id = intent.getStringExtra(CATEGORY_ID_KEY);
		categoryName = intent.getStringExtra(CATEGORY_NAME_KEY);
		initHandler();
		initView();
		initData();
	}
	
	private void initView(){
		background_view = (BackGroundView) findViewById(R.id.background_view);
		common_title_back_btn = findViewById(R.id.common_title_back_btn);
		common_title_right_btn = findViewById(R.id.common_title_right_btn);
		common_title_tv = (TextView) findViewById(R.id.common_title_tv);
		xListView = (XListView) findViewById(R.id.xlistview_include);
		loadingView = findViewById(R.id.classification_list_loading);
		errorView = findViewById(R.id.classification_list_error);
		
		common_title_tv.setText(categoryName);
		background_view.setGradient(background[0], background[1]);
		
		common_title_back_btn.setOnClickListener(this);
		common_title_right_btn.setOnClickListener(this);
		
		xListView.setOverScrollMode(android.view.View.OVER_SCROLL_NEVER);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(false);
		xListView.setEnableDragLoadMore(false);
		xListView.setXListViewListener(this);
		xListView.setOnItemClickListener(this);
		
		onItemBtnClickListener = new OnBtnClickListener() {
			
			@Override
			public void onUsericonClick(String userId) {
				// TODO Auto-generated method stub
				Intent personalIntent = new Intent(ClassificationActivity.this, PersonalZoneActivity.class);
				personalIntent.putExtra(PersonalZoneActivity.ZONE_ID_KEY, userId);
				personalIntent.putExtra(BackGroundView.BACKGROUND_COLOR, background_view.getGradientColor());
				startActivity(personalIntent);
			}
		};
		
		mAdapter = new HotFreshChildListAdapter(this, appList,onItemBtnClickListener);
		xListView.setAdapter(mAdapter);
		
	}
	
	private void initData() {
		requestData(1);
	}

	@SuppressLint("HandlerLeak")
	private void initHandler() {
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO
				switch (msg.what) {
				case DATA_UPDATE:
					mAdapter.notifyDataSetChanged();
					loadingView.setVisibility(View.GONE);
					break;

				default:
					break;
				}
			}

		};
	}
	
	/**
	 * @param p 请求页下标
	 */
	private void requestData(int p){
		if(SystemUtil.isNetOkWithToast(this.getApplicationContext())){
			NetWorkAPI.app_cat(mContext, category_id, p, new HttpCallback(){
			
				@Override
				public void onResult(NetResBean response) {
					// TODO Auto-generated method stub
					L.i(TAG, "onResult", response.toString());
					if(response instanceof CategoryAppListResBean){
						CategoryAppListResBean data = (CategoryAppListResBean) response;
						ArrayList<AppInfoItemBean> appInfoItemBeans = data.getAppInfoItemBeans();
						int now_p = data.getNow_p();
						int total_p = data.getTotal_p();
						if(appInfoItemBeans!=null&&appInfoItemBeans.size()>0){
							if(now_p==1){
								appList.clear();
							}
							appList.addAll(appInfoItemBeans);
							currentPageId = now_p;
							mAdapter.notifyDataSetChanged();
						}
						xListView.stopRefresh();
						xListView.stopLoadMore();
						if(currentPageId>=total_p){
							xListView.setPullLoadEnable(false);
							xListView.setEnableDragLoadMore(false);
						}else{
							xListView.setPullLoadEnable(true);
							xListView.setEnableDragLoadMore(true);
						}
						loadingView.setVisibility(View.GONE);
					}else{
						xListView.stopRefresh();
						xListView.stopLoadMore();
						showShortToast("数据请求失败");
						loadingView.setVisibility(View.GONE);
					}
				}
			
				@Override
				public void onCancel() {}
			
				@Override
				public void onError(String errorMsg) {
					xListView.stopRefresh();
					xListView.stopLoadMore();
					loadingView.setVisibility(View.GONE);
					showShortToast("数据请求失败");
				}
				
			});
		}
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.common_title_back_btn:
			finish();
			break;
			
		case R.id.common_title_right_btn:
			Intent appReleaseIntent = new Intent(this, AppReleaseActivity.class);
			appReleaseIntent.putExtra(BackGroundView.BACKGROUND_COLOR, background_view.getGradientColor());
			startActivity(appReleaseIntent);
			overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_none);
			break;

		default:
			break;
		}
	}


	@Override
	public void onRefresh() {
		// TODO 刷新数据
		if(appList.size()==0){
			loadingView.setVisibility(View.VISIBLE);
		}
		requestData(1);
	}

	@Override
	public void onLoadMore() {
		// TODO 加载更多
		requestData(currentPageId+1);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int headerViewsCount = xListView.getHeaderViewsCount();
		if(position<headerViewsCount || position>=(headerViewsCount+appList.size())){
			return ;
		}
		//TODO 
		Intent appDetailsIntent = new Intent(this, AppDetailsActivity.class);
		appDetailsIntent.putExtra(BackGroundView.BACKGROUND_COLOR, this.background_view.getGradientColor());
		appDetailsIntent.putExtra(AppDetailsActivity.APP_ID, appList.get(position-1).getId());
		startActivity(appDetailsIntent);
		
	}
}
