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
import com.bccv.bangyangapp.api.NetWorkAPI;
import com.bccv.bangyangapp.model.CategoryResBean;
import com.bccv.bangyangapp.model.CategoryResBean.CategoryBean;
import com.bccv.bangyangapp.network.HttpCallback;
import com.bccv.bangyangapp.network.NetResBean;
import com.bccv.bangyangapp.ui.activity.ClassificationActivity;
import com.bccv.bangyangapp.ui.activity.MainActivity;
import com.bccv.bangyangapp.ui.adapter.ClassificationGridAdapter;
import com.bccv.bangyangapp.ui.view.BackGroundView;
import com.bccv.bangyangapp.utils.L;
import com.bccv.bangyangapp.utils.SystemUtil;

/**
 * 查看分类
 * 
 * @author liukai
 *
 * @version 2015-3-27  上午10:20:52
 */
public class ClassifcationFramgent extends PrimaryFramgent implements OnClickListener, OnItemClickListener {

	public static final String TAG = "ClassifcationFramgent";
	
	public static final int REQUEST_DATA = 1;
	
	private View mView;
	
	private View title,titleMenuBtn,titleRightBtn,loadingView,errorView;
	private TextView titleTv;
	private GridView gridView;
	
	private ArrayList<CategoryBean> categoryList = new ArrayList<CategoryBean>();
	private ClassificationGridAdapter classificationGridAdapter;
	
	private MainActivity mActivity;
	
	private boolean isDataSuccess;
	
	private Handler mHandler;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mActivity = (MainActivity) getActivity();
		if (mView != null) {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
		} else {
			initHandler();
			mView = initView();
		}
		return mView;
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// TODO 初始化数据
		if(!isDataSuccess || categoryList.size()==0){
			mHandler.sendEmptyMessageDelayed(REQUEST_DATA, 300l);
		}
	}
	
	@SuppressLint("HandlerLeak")
	private void initHandler(){
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				
				switch (msg.what) {
				case REQUEST_DATA:
					requestData();
					break;

				default:
					break;
				}
			}
			
		};
	}
	
	private	View initView(){
		
		View view = View.inflate(getActivity(), R.layout.fragment_classifcation, null);
		
		title = view.findViewById(R.id.classifcation_title);
		titleMenuBtn = title.findViewById(R.id.common_title_menu_btn);
		titleRightBtn = title.findViewById(R.id.common_title_right_btn);
		titleTv = (TextView) title.findViewById(R.id.common_title_tv);
		titleTv.setText("查看分类");
		
		loadingView = view.findViewById(R.id.classifcation_loading);
		errorView = view.findViewById(R.id.classifcation_error);
		
		titleMenuBtn.setOnClickListener(mActivity);
		titleRightBtn.setOnClickListener(mActivity);
		errorView.setOnClickListener(this);
		
		gridView = (GridView) view.findViewById(R.id.classifcation_gridview);
		
		gridView.setOnItemClickListener(this);
		
		classificationGridAdapter = new ClassificationGridAdapter(mActivity, categoryList);
		gridView.setAdapter(classificationGridAdapter);
		
		return view;
	}

	
	private void requestData(){
		//TODO 请求数据
		if(SystemUtil.isNetOkWithToast(mActivity)){
			loadingView.setVisibility(View.VISIBLE);
			errorView.setVisibility(View.GONE);
			NetWorkAPI.app_nav(mActivity, new CategoryListCallBack());
		}else if(categoryList.size()<=0){
			errorView.setVisibility(View.VISIBLE);
			loadingView.setVisibility(View.GONE);
		}
	}
	
	private class CategoryListCallBack implements HttpCallback{

		@Override
		public void onResult(NetResBean response) {
			// TODO Auto-generated method stub
			L.i(TAG, "CategoryListCallBack  onResult", "response : " + response);
			if(response!=null && response instanceof CategoryResBean){
				CategoryResBean data = (CategoryResBean) response;
				
				if(data.success&&data.getCategorys()!=null){
					categoryList.clear();
					categoryList.addAll(data.getCategorys());
					classificationGridAdapter.notifyDataSetChanged();
					loadingView.setVisibility(View.GONE);
					isDataSuccess = true;
				}else{
					loadingView.setVisibility(View.GONE);
					errorView.setVisibility(View.VISIBLE);
				}
				
			}else{
				loadingView.setVisibility(View.GONE);
				errorView.setVisibility(View.VISIBLE);
			}
		}
		
		@Override
		public void onError(String errorMsg) {
			// TODO Auto-generated method stub
			L.i(TAG, "CategoryListCallBack  errorMsg", "errorMsg : " + errorMsg);
			loadingView.setVisibility(View.GONE);
			errorView.setVisibility(View.VISIBLE);
			
		}

		@Override
		public void onCancel() {}
	}
	
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.classifcation_error:
			//TODO 请求数据
			requestData();
			break;

		default:
			break;
		}
	}
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
			//TODO 点击条目
		
		Intent searchIntent = new Intent(mActivity, ClassificationActivity.class);
		searchIntent.putExtra(BackGroundView.BACKGROUND_COLOR, mActivity.backGroundView.getGradientColor());
		searchIntent.putExtra(ClassificationActivity.CATEGORY_ID_KEY, categoryList.get(position).getId());
		searchIntent.putExtra(ClassificationActivity.CATEGORY_NAME_KEY, categoryList.get(position).getName());
		startActivity(searchIntent);
		
	}


	@Override
	public void onMenuTransform(float percentOpen) {
//		title.setAlpha(percentOpen);
		mView.setAlpha(percentOpen);
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
