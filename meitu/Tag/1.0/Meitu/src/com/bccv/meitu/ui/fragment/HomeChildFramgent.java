package com.bccv.meitu.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bccv.meitu.ApplicationManager;
import com.bccv.meitu.R;
import com.bccv.meitu.api.NetWorkAPI;
import com.bccv.meitu.model.GetlistResBean;
import com.bccv.meitu.model.Special;
import com.bccv.meitu.network.HttpCallback;
import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.sns.UserInfoManager;
import com.bccv.meitu.ui.activity.AlbumActivity;
import com.bccv.meitu.ui.adapter.HomeListAdapter;
import com.bccv.meitu.utils.Logger;
import com.bccv.meitu.utils.SystemUtil;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.huewu.pla.lib.internal.PLA_AdapterView.OnItemClickListener;
import com.me.maxwin.view.XListView;
import com.me.maxwin.view.XListView.IXListViewListener;

public class HomeChildFramgent extends Fragment implements IXListViewListener {
	
	private static final String TAG = "HomeChildFramgent";
	
	private View mView;
	private XListView listView;
	private LinearLayout ll_no_result;
	private View waitting_layout;
	
	
	private boolean isFirst = true;
	public boolean isRefresh;
	
	private HomeListAdapter homeListAdapter;
	
	//  cate 查询分类id（0 关注，1热门，2最新）
	private int cate;
	
	private int currentPage =0;
	private int totalPage =0;
	private List<Special> dataList;
	
	// 数据List
	public static final int ATTENTION = 3;
	public static final int HOT = 1;
	public static final int FRESH = 2;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mView != null) {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
			return mView;
		} else {
			mView = inflater.inflate(R.layout.homefragment_child, null);
		}
		
		listView = (XListView) mView.findViewById(R.id.home_fragment_xlv);
		ll_no_result = (LinearLayout) mView.findViewById(R.id.ll_no_result);
		waitting_layout = mView.findViewById(R.id.waitting_layout);
		
		listView.setPullRefreshEnable(true);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		listView.hideFooter();
		initFragment();
		return mView;
	}
	
	/**
	 * 设置类型 
	 * @param cate 查询分类id（3 关注，1热门，2最新）
	 */
	public void setCate(int cate){
		this.cate = cate;
	}
	
	public void initFragment(){
		dataList = new ArrayList<Special>();
		// 传递数据
		homeListAdapter = new HomeListAdapter(getActivity(), dataList);
		listView.setAdapter(homeListAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(PLA_AdapterView<?> parent, View view,
					int position, long id) {
				// 跳转到专辑详情页
				if(position!=0){
					Intent intent = new Intent(getActivity(), AlbumActivity.class);
					intent.putExtra("special_id", String.valueOf(dataList.get(position-1).getSpecial_id()));
					getActivity().startActivity(intent);
				}
			}
		});
		
	};

//	@Override
//	public void onStart() {
//		super.onStart();
//		Logger.v(TAG, "---- onStart ---- 11 cate  : " + cate);
//		
//		if(isFirst && 
//				SystemUtil.isNetOkWithToast(ApplicationManager.getGlobalContext())){
//			Logger.v(TAG, "---- onStart ---- 22 cate  : " + cate);
//			if(cate != 3 || (cate == 3 && UserInfoManager.isLogin())){
//				Logger.v(TAG, "---- onStart ---- 33 cate  : " + cate);
//				initData();
//				isFirst = false;
//			}
//		}
//	}
	
	@Override
	public void onResume() {
		super.onResume();
		Logger.v(TAG, "---- onResume ----  cate  : " + cate);
		
		if(isFirst && 
				SystemUtil.isNetOkWithToast(ApplicationManager.getGlobalContext())){
			if(cate != 3 || (cate == 3 && UserInfoManager.isLogin())){
				initData();
				isFirst = false;
			}
		}
	}
	
	private void initData(){
		refreshData();
	}
	
	/**
	 * 刷新数据
	 */
	public void refreshData(){
		ll_no_result.setVisibility(View.GONE);
		if(dataList.size()==0){
			// 显示loading
			showWaiting(true);
		}
		getNetData(1,true);
	}
	
	/**
	 * 获取指定页的数据
	 * 
	 * @param page  页码
	 */
	private synchronized void getNetData(int page,final boolean isRefresh){
		
		if(SystemUtil.isNetOkWithToast(getActivity().getApplicationContext())){
			
			NetWorkAPI.getlist(getActivity().getApplicationContext(), cate, page, new HttpCallback() {
				
				@Override
				public void onResult(NetResBean response) {
					
					Logger.v(TAG, "getNetData  onResult", "response.success : " + response.success);
					if(response.success){
						GetlistResBean data = (GetlistResBean)response;
						Logger.v(TAG, "getNetData  onResult", data.toString());
						currentPage = data.getPage();
						totalPage = data.getPage_total();
						List<Special> list = data.getList();
						if(list!=null){
							if(isRefresh){
								dataList.clear();
								dataList.addAll(list);
								homeListAdapter = new HomeListAdapter(getActivity(), dataList);
								listView.setAdapter(homeListAdapter);
							}else{
								dataList.addAll(list);
								homeListAdapter.notifyDataSetChanged();
							}
						}
						listView.stopRefresh();
						listView.stopLoadMore();
						if(totalPage>currentPage){
							listView.showFooter();
						}else{
							listView.hideFooter();
						}
					}else{
						// 获取数据失败
						listView.stopRefresh();
						listView.stopLoadMore();
						Toast.makeText(ApplicationManager.getGlobalContext(), "数据获取失败", Toast.LENGTH_SHORT).show();
					}
					showWaiting(false);
					
					if(dataList.size()==0){
						ll_no_result.setVisibility(View.VISIBLE);
					}
				}
				
				@Override
				public void onError(String errorMsg) {
					Logger.e(TAG, "getNetData  onError", errorMsg+"");
					listView.stopRefresh();
					listView.stopLoadMore();
					Toast.makeText(ApplicationManager.getGlobalContext(), "数据获取失败", Toast.LENGTH_SHORT).show();
					showWaiting(false);
				}
				
				@Override
				public void onCancel() {}
			});
			
		}else{
			if(dataList.size()==0){
				ll_no_result.setVisibility(View.VISIBLE);
			}
		}
	}
	
	private void showWaiting(boolean show){
		if(show){
			waitting_layout.setVisibility(View.VISIBLE);
		}else{
			waitting_layout.setVisibility(View.GONE);
		}
	}
	
	
	@Override
	public void onRefresh() {
		// 刷新数据
		refreshData();
	}

	@Override
	public void onLoadMore() {
		// 加载更多
		listView.showFooter();
		if(currentPage>=totalPage){
			listView.stopLoadMore();
			listView.hideFooter();
			return;
		}
		getNetData(++currentPage, false);
	}

	/**
	 * 重置
	 */
	public void reset(){
		isFirst = true;
		if(dataList!=null){
			dataList.clear();
		}
		if(homeListAdapter!=null){
			homeListAdapter.notifyDataSetChanged();
		}
	}
	
}
