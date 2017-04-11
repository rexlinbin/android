package com.boxuu.gamebox.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.boxuu.gamebox.R;
import com.boxuu.gamebox.model.GameInfo;
import com.boxuu.gamebox.model.GameList;
import com.boxuu.gamebox.network.HttpCallback;
import com.boxuu.gamebox.network.HttpReq.ReqestMethod;
import com.boxuu.gamebox.network.HttpReqImpl;
import com.boxuu.gamebox.network.HttpService;
import com.boxuu.gamebox.network.NetResBean;
import com.boxuu.gamebox.ui.adapter.MyGridViewAdapter;
import com.boxuu.gamebox.ui.view.PullToRefreshView;
import com.boxuu.gamebox.ui.view.PullToRefreshView.OnFooterRefreshListener;
import com.boxuu.gamebox.ui.view.PullToRefreshView.OnHeaderRefreshListener;

public class OfflineFragment extends Fragment implements  OnHeaderRefreshListener, OnFooterRefreshListener{

	private GridView gridview;
	private int total_page;
	private int cur_page = 0;
	private int status;
	private PullToRefreshView mPullToRefreshView;
	private MyGridViewAdapter adapter ;
	private List<GameInfo> infos ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = findView(inflater, container);
		infos = new ArrayList<GameInfo>();
		getlist(getActivity(),++cur_page,18, new HttpCallback() {
			
			@Override
			public void onResult(NetResBean response) {
				if ( response.success && response instanceof GameList ) {
					GameList list = (GameList) response;
					List<GameInfo> gameList = list.getList();
					total_page = list.getTotal();
					status = list.getStatus();
					for(GameInfo info :  gameList) {
						infos.add(info);
						System.out.println(info.toString());
					}
					adapter.setData(infos);
				}
			}
			
			@Override
			public void onError(String errorMsg) {
				Log.e("GetInfo", errorMsg);
			}
			
			@Override
			public void onCancel() {
				Log.e("GetInfo", "cancle");
			}
		});
		return view;
	}
	
	private View findView(LayoutInflater inflater, ViewGroup container){
		View view = inflater.inflate(R.layout.fragment_offline, null);
		mPullToRefreshView = (PullToRefreshView) view.findViewById(R.id.main_pull_refresh_view);
		gridview = (GridView) view.findViewById(R.id.gridview);
		adapter = new MyGridViewAdapter(getActivity(),false);
		gridview.setAdapter(adapter);
		gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		return view;
	}
	
	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (cur_page+1 > total_page) {
					Toast.makeText(getActivity(), "没有新数据了", Toast.LENGTH_SHORT).show();
					mPullToRefreshView.onFooterRefreshComplete();
				}else {
					getlist(getActivity(),++cur_page,18, new HttpCallback() {
						
						@Override
						public void onResult(NetResBean response) {
							if ( response.success && response instanceof GameList ) {
								GameList list = (GameList) response;
								List<GameInfo> gameList = list.getList();
								total_page = list.getTotal();
								status = list.getStatus();
								for(GameInfo info :  gameList) {
									infos.add(info);
								}
								for (GameInfo info :  infos) {
									System.out.println(info.toString());
								}
								adapter.setData(infos);
							}
							mPullToRefreshView.onFooterRefreshComplete();
						}
						
						@Override
						public void onError(String errorMsg) {
							Log.e("GetInfo", errorMsg);
							mPullToRefreshView.onFooterRefreshComplete();
						}
						
						@Override
						public void onCancel() {
							Log.e("GetInfo", "cancle");
							mPullToRefreshView.onFooterRefreshComplete();
						}
					});
				}
			}
		}, 1000);
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {

			@Override
			public void run() {
				cur_page = 0;
				getlist(getActivity(),++cur_page,18, new HttpCallback() {
					
					@Override
					public void onResult(NetResBean response) {
						if ( response.success && response instanceof GameList ) {
							GameList list = (GameList) response;
							List<GameInfo> gameList = list.getList();
							total_page = list.getTotal();
							status = list.getStatus();
							infos.clear();
							for(GameInfo info :  gameList) {
								infos.add(info);
							}
							for (GameInfo info :  infos) {
								System.out.println(info.toString());
							}
							adapter.setData(infos);
						}
						mPullToRefreshView.onHeaderRefreshComplete();
					}
					
					@Override
					public void onError(String errorMsg) {
						Log.e("GetInfo", errorMsg);
						mPullToRefreshView.onHeaderRefreshComplete();
					}
					
					@Override
					public void onCancel() {
						Log.e("GetInfo", "cancle");
						mPullToRefreshView.onHeaderRefreshComplete();
					}
				});
			}
		}, 1000);
	}
	
	public static void getlist(Context context,int p , int num,HttpCallback callBack){
		String url = "http://m.cgame.cn/smallgame/index/down";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("p", p);
		params.put("num", num);
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new GameList());
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	public void notifyAdapter(boolean edit) {
		if (adapter == null) {
			adapter = new MyGridViewAdapter(getActivity(), edit);
			gridview.setAdapter(adapter);
		}else {
			adapter.setEdit(edit);
		}
	}
	
}
