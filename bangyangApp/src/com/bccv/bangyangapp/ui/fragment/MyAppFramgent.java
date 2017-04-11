package com.bccv.bangyangapp.ui.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.bccv.bangyangapp.R;
import com.bccv.bangyangapp.model.InstallAppInfo;
import com.bccv.bangyangapp.ui.activity.AppReleaseActivity;
import com.bccv.bangyangapp.ui.activity.MainActivity;
import com.bccv.bangyangapp.ui.adapter.AppGridAdapter;
import com.bccv.bangyangapp.ui.view.BackGroundView;
import com.bccv.bangyangapp.utils.SystemUtil;

/**
 * 我的应用
 * @author liukai
 *
 * @version 2015-3-27  上午10:22:15
 */
public class MyAppFramgent extends PrimaryFramgent implements OnItemClickListener, OnClickListener {
	
	private static final String TAG = "MyAppFramgent";
	
	private static final int INIT_DATA_MSG = 1;
	private static final int REDRESH_DATA_MSG = 2;

	private View mView;
	private View titleMenuBtn,titleRightBtn,title;
	private TextView titleTv;
	private View loadingView;
	private View errorView;
	private GridView gridView;
	private View refreshView;
	
	
	private MainActivity mActivity;
	private ArrayList<InstallAppInfo> data = new ArrayList<InstallAppInfo>();
	
	private AppGridAdapter mAdapter;
	
	private Handler mHandler;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mActivity = (MainActivity) getActivity();
		if(mHandler == null){
			initHandler();
		}
		// TODO 初始化视图
		if (mView != null) {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
			return mView;
		} else {
			mView = inflater.inflate(R.layout.fragment_myapp, null);
		}
		
		title = mView.findViewById(R.id.myapp_title);
		titleMenuBtn = title.findViewById(R.id.common_title_menu_btn);
		titleRightBtn = title.findViewById(R.id.common_title_right_btn);
		titleTv = (TextView) title.findViewById(R.id.common_title_tv);
		titleTv.setText("我的应用");
		
		titleMenuBtn.setOnClickListener(mActivity);
		titleRightBtn.setOnClickListener(mActivity);
		
		refreshView = mView.findViewById(R.id.myapp_refresh);
		
		loadingView = mView.findViewById(R.id.myapp_loading);
		errorView = mView.findViewById(R.id.myapp_error);
		
		gridView = (GridView) mView.findViewById(R.id.myapp_gridview);
		gridView.setOverScrollMode(android.view.View.OVER_SCROLL_NEVER);
		
		refreshView.setOnClickListener(this);
		gridView.setOnItemClickListener(this);
		
		mAdapter = new AppGridAdapter(mActivity, data);
		gridView.setAdapter(mAdapter);
		
		return mView;
	}
	

	@SuppressLint("HandlerLeak")
	private void initHandler(){
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {

				switch (msg.what) {
				case INIT_DATA_MSG:
					requestData();
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
		if(data.size()==0){
			mHandler.sendEmptyMessageDelayed(INIT_DATA_MSG, 500l);
		}
	}
	
	private void requestData(){
		
		new Thread(){
			public void run() {
				//TODO 请求数据
				ArrayList<InstallAppInfo> installAppInfos = SystemUtil.getInstallAppInfos(mActivity);
				data.clear();
				data.addAll(installAppInfos);
				mHandler.sendEmptyMessage(REDRESH_DATA_MSG);
			};
		}.start();
		
	}
	
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.myapp_refresh:
			//TODO 刷新
			loadingView.setVisibility(View.VISIBLE);
			requestData();
			break;

		default:
			break;
		}
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		if(position>=data.size()){
			return;
		}
		Intent appReleaseIntent = new Intent(mActivity, AppReleaseActivity.class);
		appReleaseIntent.putExtra(BackGroundView.BACKGROUND_COLOR, mActivity.backGroundView.getGradientColor());
		appReleaseIntent.putExtra(AppReleaseActivity.START_WITH_APP, data.get(position));
		startActivity(appReleaseIntent);
		mActivity.overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_none);
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


}
