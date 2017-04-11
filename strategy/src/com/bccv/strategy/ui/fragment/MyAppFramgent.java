package com.bccv.strategy.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.strategy.R;
import com.bccv.strategy.api.NetWorkAPI;
import com.bccv.strategy.db.StorageModule;
import com.bccv.strategy.model.GameInfoBean;
import com.bccv.strategy.model.MyGameListResBean;
import com.bccv.strategy.network.HttpCallback;
import com.bccv.strategy.network.NetResBean;
import com.bccv.strategy.sns.UserInfoManager;
import com.bccv.strategy.ui.activity.MainActivity;
import com.bccv.strategy.ui.adapter.MyGameListAdapter;
import com.bccv.strategy.ui.view.XListView;
import com.bccv.strategy.ui.view.XListView.IXListViewListener;
import com.bccv.strategy.utils.L;
import com.bccv.strategy.utils.SystemUtil;

/**
 * 我的应用
 * @author liukai
 *
 * @version 2015-3-27  上午10:22:15
 */
public class MyAppFramgent extends PrimaryFramgent implements OnClickListener, IXListViewListener {
	
	private static final String TAG = "MyAppFramgent";
	
	private static final int INIT_DATA_MSG = 1;
	private static final int REDRESH_DATA_MSG = 2;

	private View mView;
	private View titleMenuBtn,titleRightBtn,title;
	private TextView titleTv;
	private View loadingView;
	private View errorView;
	private XListView listView;
	private View refreshView;
	private TextView common_title_right_tv;
	private boolean isDFlag = false;
	
	private MainActivity mActivity;
	private List<GameInfoBean> dataList = new ArrayList<GameInfoBean>();
	
	private MyGameListAdapter mAdapter;
	
	private Handler mHandler;
	
	private int currentPageId = 0;
	private StorageModule sModule;
	private boolean isDataSuccess = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mActivity = (MainActivity) getActivity();
		if(mHandler == null){
			initHandler();
		}
		// TODO 初始化视图
		sModule = StorageModule.getInstance();
		if (mView != null) {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
			return mView;
		} else {
			mView = inflater.inflate(R.layout.fragment_mygame, null);
		}
		
		title = mView.findViewById(R.id.myapp_title);
		titleMenuBtn = title.findViewById(R.id.common_title_menu_btn);
		titleRightBtn = title.findViewById(R.id.common_title_right_btn);
		titleTv = (TextView) title.findViewById(R.id.common_title_tv);
		titleTv.setText("我的游戏");
		common_title_right_tv = (TextView) title.findViewById(R.id.common_title_right_tv);
		common_title_right_tv.setVisibility(View.VISIBLE);
		common_title_right_tv.setText("编辑");
		
		common_title_right_tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isDFlag) {
					common_title_right_tv.setText("保存");
					isDFlag = true;
					mAdapter.setMDFlage(true);
					listView.setPullLoadEnable(false);
					listView.setPullRefreshEnable(false);
				}else {
					common_title_right_tv.setText("编辑");
					isDFlag = false;
					mAdapter.setMDFlage(false);
					listView.setPullLoadEnable(true);
					listView.setPullRefreshEnable(true);
					dataList = mAdapter.getData();
					ArrayList<String> games = new ArrayList<String>();
					if (dataList != null && dataList.size() >0 ) {
						for (int i = 0; i < dataList.size(); i++) {
							games.add(dataList.get(i).getId());
						}
					}
					sModule.removeAllLocalLike();
					sModule.addLocalLike(games);
					postLikeData(games);
				}
			}
		});
		
		titleMenuBtn.setOnClickListener(mActivity);
		titleRightBtn.setOnClickListener(mActivity);
		
		refreshView = mView.findViewById(R.id.myapp_refresh);
		
		loadingView = mView.findViewById(R.id.myapp_loading);
		errorView = mView.findViewById(R.id.myapp_error);
		
		listView = (XListView) mView.findViewById(R.id.xlistview_include);
		listView.setOverScrollMode(android.view.View.OVER_SCROLL_NEVER);
		listView.setPullRefreshEnable(true);
		listView.setPullLoadEnable(false);
		listView.setEnableDragLoadMore(false);
		listView.setXListViewListener(this);
		
		refreshView.setOnClickListener(this);
		
		mAdapter = new MyGameListAdapter(mActivity, dataList);
		listView.setAdapter(mAdapter);
		
		return mView;
	}
	

	private void postLikeData(ArrayList<String> games) {
		if (UserInfoManager.isLogin()) {
			loadingView.setVisibility(View.VISIBLE);
			if (SystemUtil.isNetOkWithToast(mActivity)) {
				NetWorkAPI.likes_game(mActivity,games,new HttpCallback(){
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
	
	@SuppressLint("HandlerLeak")
	private void initHandler(){
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {

				switch (msg.what) {
				case INIT_DATA_MSG:
//					requestData();
					break;
				case REDRESH_DATA_MSG:
					mAdapter.notifyDataSetChanged();
					loadingView.setVisibility(View.GONE);
					break;

				default:
					break;
				}
			}
		};
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// TODO 初始化数据
//		if(dataList.size()==0){
//			mHandler.sendEmptyMessageDelayed(INIT_DATA_MSG, 500l);
//		}
//		if(!isDataSuccess || dataList.size()==0){
//			requestData(1);
//		}
	}
	
//	private void requestData(){
//		
//		new Thread(){
//			public void run() {
//				//TODO 请求数据
//				ArrayList<InstallAppInfo> installAppInfos = SystemUtil.getInstallAppInfos(mActivity);
//				dataList.clear();
//				dataList.addAll(installAppInfos);
//				mHandler.sendEmptyMessage(REDRESH_DATA_MSG);
//			};
//		}.start();
//		
//	}
	
	private void requestData(int p){
		if(SystemUtil.isNetOkWithToast(mActivity)){
			if (UserInfoManager.isLogin()) {
//				if (sModule.getLocalLikeList() != null && sModule.getLocalLikeList().size() > 0) {
//					postLikeData(sModule.getLocalLikeList());
//				}
				loadingView.setVisibility(View.VISIBLE);
				NetWorkAPI.user_focus_g(mActivity, p, new MyHttpCallBack());
			}else {
				//如果用户没有登陆
				ArrayList<String> localLikeList = StorageModule.getInstance().getLocalLikeList();
				if(localLikeList!=null && localLikeList.size()>0){
					NetWorkAPI.getlocallist_game(mActivity, localLikeList, new MyHttpCallBack());
				}else{
					listView.setPullLoadEnable(false);
					listView.setEnableDragLoadMore(false);
					loadingView.setVisibility(View.GONE);
				}
			}
		}
	}
	
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.myapp_refresh:
			//TODO 刷新
//			loadingView.setVisibility(View.VISIBLE);
//			requestData();
			break;

		default:
			break;
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


	@Override
	public void onRefresh() {
		// TODO 刷新数据
		isRefresh = true;
		if (UserInfoManager.isLogin()) {
			if(dataList.size()==0){
				loadingView.setVisibility(View.VISIBLE);
			}
			requestData(1);
		}else{
			requestData(1);
//			listView.stopRefresh();
//			listView.stopLoadMore();
		}
	}


	@Override
	public void onLoadMore() {
		// TODO 加载更多
		requestData(currentPageId+1);
	}
private boolean isRefresh = true;
	
@Override
public void onResume() {
	// TODO Auto-generated method stub
	isRefresh = true;
	requestData(1);
	super.onResume();
}

	private class MyHttpCallBack implements HttpCallback{

		@Override
		public void onResult(NetResBean response) {
			L.i(TAG, "onResult", response.toString());
			if(response instanceof MyGameListResBean){
				MyGameListResBean data = (MyGameListResBean) response;
				ArrayList<GameInfoBean> appInfoItemBeans = data.getAppInfoItemBeans();
				int now_p = data.getNow_p();
				int total_p = data.getTotal_p();
				if(appInfoItemBeans!=null&&appInfoItemBeans.size()>0){
					if(now_p==1){
						dataList.clear();
					}
					if (isRefresh) {
						dataList.clear();
						isRefresh = false;
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
