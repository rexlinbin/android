package com.bccv.strategy.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.cookie.SM;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.strategy.R;
import com.bccv.strategy.api.NetWorkAPI;
import com.bccv.strategy.db.StorageModule;
import com.bccv.strategy.model.AppInfoItemBean;
import com.bccv.strategy.model.CollectListResBean;
import com.bccv.strategy.model.CollectListResBean2;
import com.bccv.strategy.network.HttpCallback;
import com.bccv.strategy.network.NetResBean;
import com.bccv.strategy.sns.UserInfoManager;
import com.bccv.strategy.ui.activity.MainActivity;
import com.bccv.strategy.ui.activity.PersonalZoneActivity;
import com.bccv.strategy.ui.activity.StrategyDetailsActivity;
import com.bccv.strategy.ui.adapter.CollectionListAdapter;
import com.bccv.strategy.ui.adapter.CollectionListAdapter.OnBtnClickListener;
import com.bccv.strategy.ui.view.BackGroundView;
import com.bccv.strategy.ui.view.XListView;
import com.bccv.strategy.ui.view.XListView.IXListViewListener;
import com.bccv.strategy.utils.L;
import com.bccv.strategy.utils.SystemUtil;

/**
 * 我的收藏
 * 
 * @author liukai
 * 
 * @version 2015-3-27 上午10:21:14
 */
public class CollectionFramgent extends PrimaryFramgent implements
		IXListViewListener, OnItemClickListener {

	private static final String TAG = "CollectionFramgent";

	private View mView;
	private View titleMenuBtn, titleRightBtn, title;
	private TextView titleTv;
	private TextView common_title_right_tv;
	private View loadingView;
	private View errorView;
	private XListView listView;

	private ArrayList<AppInfoItemBean> dataList = new ArrayList<AppInfoItemBean>();

	private CollectionListAdapter mAdapter;
	private MainActivity mActivity;
	private StorageModule sModule;

	private OnBtnClickListener onItemBtnClickListener;

	private int currentPageId = 0;
	private boolean isDataSuccess = false;
	private boolean isDFlag = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mActivity = (MainActivity) getActivity();
		// TODO 初始化视图
		if (mView != null) {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
			return mView;
		} else {
			mView = inflater.inflate(R.layout.fragment_collection, null);
		}
		sModule = StorageModule.getInstance();
		title = mView.findViewById(R.id.collection_title);
		titleMenuBtn = title.findViewById(R.id.common_title_menu_btn);
		titleRightBtn = title.findViewById(R.id.common_title_right_btn);
		titleTv = (TextView) title.findViewById(R.id.common_title_tv);
		titleTv.setText("我的收藏");
		common_title_right_tv = (TextView) title.findViewById(R.id.common_title_right_tv);
		common_title_right_tv.setVisibility(View.VISIBLE);
		common_title_right_tv.setText("编辑");
		
		common_title_right_tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isDFlag) {
					common_title_right_tv.setText("保存");
					mAdapter.setDeleteFlag(true);
					listView.setPullLoadEnable(false);
					listView.setPullRefreshEnable(false);
					isDFlag = true;
				}else {
					common_title_right_tv.setText("编辑");
					isDFlag = false;
					mAdapter.setDeleteFlag(false);
					listView.setPullLoadEnable(true);
					listView.setPullRefreshEnable(true);
					dataList = mAdapter.getData();
					ArrayList<String> raids = new ArrayList<String>();
					if (dataList != null && dataList.size() >0) {
						for (int i = 0; i < dataList.size(); i++) {
							raids.add(dataList.get(i).getId());
						}
					}
					sModule.removeAllLocalCare();
					sModule.addLocalCare(raids);
					postCareData(raids);
				}
			}
		});
		titleMenuBtn.setOnClickListener(mActivity);
		titleRightBtn.setOnClickListener(mActivity);

		loadingView = mView.findViewById(R.id.collection_loading);
		errorView = mView.findViewById(R.id.collection_error);

		listView = (XListView) mView.findViewById(R.id.xlistview_include);

		//
		// dataList.add("1111");
		// dataList.add("1111");
		// dataList.add("1111");
		// dataList.add("1111");
		// dataList.add("1111");
		// dataList.add("1111");
		// dataList.add("1111");

		onItemBtnClickListener = new OnBtnClickListener() {

			@Override
			public void onUsericonClick(String userId) {
				// TODO Auto-generated method stub
				Intent personalIntent = new Intent(mActivity,
						PersonalZoneActivity.class);
				personalIntent.putExtra(PersonalZoneActivity.ZONE_ID_KEY,
						userId);
				personalIntent.putExtra(BackGroundView.BACKGROUND_COLOR,
						mActivity.backGroundView.getGradientColor());
				mActivity.startActivity(personalIntent);
			}
		};

		mAdapter = new CollectionListAdapter(mActivity, dataList,
				onItemBtnClickListener);
		listView.setAdapter(mAdapter);

		listView.setOverScrollMode(android.view.View.OVER_SCROLL_NEVER);
		listView.setPullRefreshEnable(true);
		listView.setPullLoadEnable(false);
		listView.setEnableDragLoadMore(false);
		listView.setXListViewListener(this);
		listView.setOnItemClickListener(this);

		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// TODO 初始化数据
		if (!isDataSuccess || dataList.size() == 0) {
			requestData(1);
		}

	}
	
	private void postCareData(ArrayList<String> raids) {
		if (UserInfoManager.isLogin()) {
			loadingView.setVisibility(View.VISIBLE);
			if (SystemUtil.isNetOkWithToast(mActivity)) {
				NetWorkAPI.likes_raid(mActivity,raids,new HttpCallback(){
					@Override
					public void onResult(NetResBean response) {
						if (response.success) {
							Log.e(TAG, "数据保存成功");
						}
					}
					@Override
					public void onCancel() {}
					@Override
					public void onError(String errorMsg) {}
				});
			}
			loadingView.setVisibility(View.GONE);
		}
	}

	private void requestData(int p) {

		if (SystemUtil.isNetOkWithToast(mActivity)) {
			if(UserInfoManager.isLogin()){
				//如果用户登陆
//				if (sModule.getLocalCareList() != null &&
//						sModule.getLocalCareList().size() > 0) {
//					postCareData(sModule.getLocalCareList());
//				}
				loadingView.setVisibility(View.VISIBLE);
				NetWorkAPI.user_focus_r(mActivity, p, new CollectionCallBack());
			}else{
				//如果用户没有登陆
				ArrayList<String> localCareList = StorageModule.getInstance().getLocalCareList();
				if(localCareList!=null && localCareList.size()>0){
					NetWorkAPI.getlocallist(mActivity, localCareList, new CollectionCallBack());
				}else{
					listView.setPullLoadEnable(false);
					listView.setEnableDragLoadMore(false);
					loadingView.setVisibility(View.GONE);
				}
			}
		}

	}

	@Override
	public void onMenuTransform(float percentOpen) {
		// TODO Auto-generated method stub
		title.setAlpha(percentOpen);
	}

	@Override
	public void onMenuOpened() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMenuClosed() {
		// TODO Auto-generated method stub

	}

	public void refreshData() {
		if (mAdapter != null && dataList != null) {
			dataList.clear();
			mAdapter.notifyDataSetChanged();
			if (dataList.size() == 0) {
				loadingView.setVisibility(View.VISIBLE);
			}
			requestData(1);
		}
	}

	@Override
	public void onRefresh() {
		// TODO 刷新数据
//		System.out.println("111111111111");
		if(UserInfoManager.isLogin()){
			if (dataList.size() == 0) {
				loadingView.setVisibility(View.VISIBLE);
			}
			requestData(1);
		}else{
//			requestData(1);
//			listView.stopRefresh();
			dataList.clear();
			requestData(1);
			listView.stopLoadMore();
		}
	}

	@Override
	public void onLoadMore() {
		// TODO 加载更多
		requestData(currentPageId + 1);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		if (position < 1) {
			return;
		}
		// TODO Auto-generated method stub
		Intent appDetailsIntent = new Intent(mActivity,
				StrategyDetailsActivity.class);
		appDetailsIntent.putExtra(BackGroundView.BACKGROUND_COLOR,
				mActivity.backGroundView.getGradientColor());
		appDetailsIntent.putExtra(StrategyDetailsActivity.RAID_ID,
				dataList.get(position - 1).getId());
		appDetailsIntent.putExtra(StrategyDetailsActivity.GAME_ID,
				dataList.get(position - 1).getGame_id());
		mActivity.startActivity(appDetailsIntent);
	}

	private class CollectionCallBack implements HttpCallback {
		@Override
		public void onResult(NetResBean response) {
			// TODO Auto-generated method stub
			L.i(TAG, "onResult", response.toString());
			if (response instanceof CollectListResBean) {
				CollectListResBean data = (CollectListResBean) response;
				ArrayList<AppInfoItemBean> appInfoItemBeans = data
						.getAppInfoItemBeans();
				int now_p = data.getNow_p();
				int total_p = data.getTotal_p();
				if (appInfoItemBeans != null && appInfoItemBeans.size() > 0) {
					if (now_p == 1) {
						dataList.clear();
					}
					dataList.addAll(appInfoItemBeans);
					currentPageId = now_p;
					mAdapter.notifyDataSetChanged();
					isDataSuccess = true;
				}
				listView.stopRefresh();
				listView.stopLoadMore();
				if (currentPageId >= total_p) {
					listView.setPullLoadEnable(false);
					listView.setEnableDragLoadMore(false);
				} else {
					listView.setPullLoadEnable(true);
					listView.setEnableDragLoadMore(true);
				}
				loadingView.setVisibility(View.GONE);
			} else {
				listView.stopRefresh();
				listView.stopLoadMore();
				Toast.makeText(mActivity, "数据请求失败", Toast.LENGTH_SHORT).show();
				loadingView.setVisibility(View.GONE);
			}
		}

		@Override
		public void onError(String errorMsg) {
			// TODO Auto-generated method stub
			listView.stopRefresh();
			listView.stopLoadMore();
			Toast.makeText(mActivity, "数据请求失败", Toast.LENGTH_SHORT).show();
			loadingView.setVisibility(View.GONE);
		}

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
			listView.stopRefresh();
			listView.stopLoadMore();
			Toast.makeText(mActivity, "数据请求失败", Toast.LENGTH_SHORT).show();
			loadingView.setVisibility(View.GONE);
		}

	}

}
