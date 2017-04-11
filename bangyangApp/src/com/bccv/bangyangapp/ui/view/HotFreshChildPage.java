package com.bccv.bangyangapp.ui.view;

import java.util.ArrayList;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.bccv.bangyangapp.R;
import com.bccv.bangyangapp.api.NetWorkAPI;
import com.bccv.bangyangapp.model.AppInfoItemBean;
import com.bccv.bangyangapp.model.HotFreshListResBean;
import com.bccv.bangyangapp.network.HttpCallback;
import com.bccv.bangyangapp.network.NetResBean;
import com.bccv.bangyangapp.ui.activity.AppDetailsActivity;
import com.bccv.bangyangapp.ui.activity.MainActivity;
import com.bccv.bangyangapp.ui.activity.PersonalZoneActivity;
import com.bccv.bangyangapp.ui.adapter.HotFreshChildListAdapter;
import com.bccv.bangyangapp.ui.adapter.HotFreshChildListAdapter.OnBtnClickListener;
import com.bccv.bangyangapp.ui.view.XListView.IXListViewListener;
import com.bccv.bangyangapp.utils.L;
import com.bccv.bangyangapp.utils.SystemUtil;

public class HotFreshChildPage implements OnClickListener, IXListViewListener, OnItemClickListener {

	public static final String TAG = "HotFreshChildPage";
	
	public static final int HOT_TYPE = 1;
	public static final int FRESH_TYPE = 2;
	
	private View mView;
	private View loadingView,errorView;
	private XListView listView;
	
	private int cate = 1;
	
	private ArrayList<AppInfoItemBean> dataList = new ArrayList<AppInfoItemBean>();
	private HotFreshChildListAdapter mAdapter;
	
	private MainActivity mActivity;
	
	private OnBtnClickListener onItemBtnClickListener;
	
	private int currentPageId = 0;

	private boolean isDataSuccess = false;

	public HotFreshChildPage(final MainActivity mActivity){
		this.mActivity = mActivity;
		if (mView != null) {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
		} else {
			mView = View.inflate(mActivity, R.layout.page_hot_fresh, null);
		}
		listView = (XListView) mView.findViewById(R.id.xlistview_include);
		loadingView = mView.findViewById(R.id.fragment_hot_fresh_loading);
		errorView = mView.findViewById(R.id.fragment_hot_fresh_error);
		errorView.setOnClickListener(this);

		onItemBtnClickListener = new OnBtnClickListener() {
			
			@Override
			public void onUsericonClick(String userId) {
				// TODO Auto-generated method stub
				Intent personalIntent = new Intent(mActivity, PersonalZoneActivity.class);
				personalIntent.putExtra(PersonalZoneActivity.ZONE_ID_KEY, userId);
				personalIntent.putExtra(BackGroundView.BACKGROUND_COLOR, mActivity.backGroundView.getGradientColor());
				mActivity.startActivity(personalIntent);
			}
		};
		
		mAdapter = new HotFreshChildListAdapter(mActivity, dataList,onItemBtnClickListener);
		
		//TODO adapter
		listView.setAdapter(mAdapter);
		
		listView.setOverScrollMode(android.view.View.OVER_SCROLL_NEVER);
		listView.setPullRefreshEnable(true);
		listView.setPullLoadEnable(false);
		listView.setEnableDragLoadMore(false);
		listView.setXListViewListener(this);
		listView.setOnItemClickListener(this);
	}

	public void onCreate(){
		if(!isDataSuccess || dataList.size()==0){
			requestData(1);
		}
	}
	
	public View getView(){
		return mView;
	}
	
	@Override
	public void onRefresh() {
		// TODO 刷新数据
		if(dataList.size()==0){
			loadingView.setVisibility(View.VISIBLE);
		}
		requestData(1);
	}

	@Override
	public void onLoadMore() {
		// TODO 加载更多
		requestData(currentPageId+1);
	}
	
	public void setCate(int cate){
		this.cate = cate;
	}
	
	/**
	 * @param p 请求页号
	 */
	private void requestData(int p){
		if(SystemUtil.isNetOkWithToast(mActivity)){
			switch (cate) {
			case HOT_TYPE:
				//TODO 请求最热
				NetWorkAPI.app_hot(mActivity, p, new MyHttpCallBack());
				break;
			case FRESH_TYPE:
				//TODO 请求最新
				NetWorkAPI.app_new(mActivity, p, new MyHttpCallBack());
				break;
				
			default:
				break;
			}
		}
	}
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.fragment_hot_fresh_error:
			
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent appDetailsIntent = new Intent(mActivity, AppDetailsActivity.class);
		appDetailsIntent.putExtra(BackGroundView.BACKGROUND_COLOR, mActivity.backGroundView.getGradientColor());
		appDetailsIntent.putExtra(AppDetailsActivity.APP_ID, dataList.get(position-1).getId());
		mActivity.startActivity(appDetailsIntent);
		
	}
	
	
	private class MyHttpCallBack implements HttpCallback{

		@Override
		public void onResult(NetResBean response) {
			L.i(TAG, "onResult", response.toString());
			if(response instanceof HotFreshListResBean){
				HotFreshListResBean data = (HotFreshListResBean) response;
				ArrayList<AppInfoItemBean> appInfoItemBeans = data.getAppInfoItemBeans();
				int now_p = data.getNow_p();
				int total_p = data.getTotal_p();
				if(appInfoItemBeans!=null&&appInfoItemBeans.size()>0){
					if(now_p==1){
						dataList.clear();
					}
					dataList.addAll(appInfoItemBeans);
					currentPageId = now_p;
					mAdapter.notifyDataSetChanged();
					isDataSuccess = true;
				}
				listView.stopRefresh();
				listView.stopLoadMore();
				if(currentPageId>=total_p){
					listView.setPullLoadEnable(false);
					listView.setEnableDragLoadMore(false);
				}else{
					listView.setPullLoadEnable(true);
					listView.setEnableDragLoadMore(true);
				}
				loadingView.setVisibility(View.GONE);
			}else{
				Toast.makeText(mActivity, "数据请求失败", Toast.LENGTH_SHORT).show();
				loadingView.setVisibility(View.GONE);
				listView.stopRefresh();
				listView.stopLoadMore();
			}
		}

		@Override
		public void onCancel() {}

		@Override
		public void onError(String errorMsg) {
			Toast.makeText(mActivity, "数据请求失败", Toast.LENGTH_SHORT).show();
			loadingView.setVisibility(View.GONE);
			listView.stopRefresh();
			listView.stopLoadMore();
		}
		
	}
	
}
