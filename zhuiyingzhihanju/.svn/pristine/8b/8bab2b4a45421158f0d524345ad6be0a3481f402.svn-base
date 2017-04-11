package com.bccv.zhuiyingzhihanju.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.impl.client.EntityEnclosingRequestWrapper;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.activity.VideoInfoActivity;
import com.bccv.zhuiyingzhihanju.adapter.FilterAdapter;
import com.bccv.zhuiyingzhihanju.adapter.FilterInfoAdapter;
import com.bccv.zhuiyingzhihanju.adapter.FilterInfoAdapter2;
import com.bccv.zhuiyingzhihanju.api.FilterApi;
import com.bccv.zhuiyingzhihanju.model.Filter;
import com.bccv.zhuiyingzhihanju.model.FilterInfo;
import com.bccv.zhuiyingzhihanju.model.Movie;
import com.utils.net.NetUtil;
import com.utils.pulltorefresh.FooterLoadingLayout;
import com.utils.pulltorefresh.PullToRefreshBase;
import com.utils.pulltorefresh.PullToRefreshListView;
import com.utils.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.utils.tools.BaseFragment;
import com.utils.tools.Callback;
import com.utils.tools.GlobalParams;
import com.utils.tools.PromptManager;
import com.utils.tools.StringUtils;
import com.utils.views.HorizontalListView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class FilterInfoFragment extends BaseFragment {
	View view;
	private int page = 1, count = 15;
	private String type_id;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_filterinfo, container, false);
			Bundle args = getArguments();
			type_id = args.getString("type_id");
			if (StringUtils.isEmpty(type_id)) {
				type_id = "";
			}
			initView();
			getFilterData();
		}
		return view;
	}

	private PullToRefreshListView pullToRefreshListView;
	private ListView listView;
	private LinearLayout filterLayout;
	private List<Movie> list, getList;
	private FilterInfoAdapter adapter;
	TextView filterTextView;

	private void initView() {
		filterLayout = (LinearLayout) view.findViewById(R.id.filter_layout);
		filterLayout.setVisibility(View.GONE);
		filterTextView = (TextView) view.findViewById(R.id.filter_textView);
		initHead();

	}
	
	private void initList(){
		pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.filter_pullToRefreshListView);
		listView = pullToRefreshListView.getRefreshableView();
		listView.addHeaderView(headView);
		listView.setVerticalScrollBarEnabled(false);
		listView.setDividerHeight(0);
		list = new ArrayList<Movie>();
		adapter = new FilterInfoAdapter(getActivity(), list, this);
		if (type_id.equals("tv")) {
			adapter.isTV();
		}
		if (type_id.equals("tv") || type_id.equals("variety") || type_id.equals("movie")) {
			adapter.isThree(true);
		}else{
			adapter.isThree(false);
		}
		
		listView.setAdapter(adapter);
		listView.setSelector(new ColorDrawable(android.R.color.transparent));
		pullToRefreshListView.setPullLoadEnabled(true);
		pullToRefreshListView.setPullRefreshEnabled(true);
		pullToRefreshListView.getRefreshableView().setSelector(new ColorDrawable(android.R.color.transparent));
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if (!NetUtil.isNetworkAvailable(GlobalParams.context)) {
					// 提示网络不给力,直接完成刷新
					PromptManager.showToast(GlobalParams.context, "网络不给力");

					pullToRefreshListView.onPullDownRefreshComplete();
				} else {
					page = 1;
					getData(true);

				}
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if (NetUtil.isNetworkAvailable(GlobalParams.context)) {
					((FooterLoadingLayout) pullToRefreshListView.getFooterLoadingLayout()).getmHintView()
							.setText("数据加载中...");
					getData(false);
				} else {
					PromptManager.showToast(GlobalParams.context, "网络不给力");
					pullToRefreshListView.onPullUpRefreshComplete();
				}
			}
		});
		
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (firstVisibleItem > 0) {
					filterLayout.setVisibility(View.VISIBLE);
					String filter;
					if (type_id.equals("tv") || type_id.equals("variety")) {
						filter = orderList.get(order).getName() + "  ·  " + isFinishList.get(isFinish).getName() + "  ·  " + typeList.get(type).getName() + "  ·  " + yearList.get(year).getName();
					}else{
						filter = orderList.get(order).getName() + "  ·  " + typeList.get(type).getName() + "  ·  " + yearList.get(year).getName();
					}
					
					filterTextView.setText(filter);
				}else{
					filterLayout.setVisibility(View.GONE);
				}
			}
		});
	}

	public void goInfo(int position){
		Intent intent = new Intent(getActivity().getApplicationContext(), VideoInfoActivity.class);
		intent.putExtra("movie_id", list.get(position).getId());
		intent.putExtra("type_id", list.get(position).getType_id());
		intent.putExtra("episodes_id", "0");
		startActivity(intent);
	}
	
	private void getData(final boolean isRefresh) {
		if (isRefresh) {
			page = 1;
		}
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (getList != null && getList.size() > 0) {
					if (isRefresh) {
						list.clear();
					}
					list.addAll(getList);
					adapter.notifyDataSetChanged();
					page++;
				}else{
					Toast.makeText(getActivity().getApplicationContext(), "未获取到数据", 1).show();
				}
				if (isRefresh) {
					pullToRefreshListView.onPullDownRefreshComplete();
				}else{
					pullToRefreshListView.onPullUpRefreshComplete();
				}
			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				FilterApi filterApi = new FilterApi();
				if (isFinishList.size() > 0) {
					getList = filterApi.getFilterList(type_id, orderList.get(order).getName(), isFinishList.get(isFinish).getName(), typeList.get(type).getName(), yearList.get(year).getName(), page + "", count + "");
				}else{
					getList = filterApi.getFilterList(type_id, orderList.get(order).getName(), "", typeList.get(type).getName(), yearList.get(year).getName(), page + "", count + "");
				}
				
				return null;
			}
		}.execute("");
	}
	
	private FilterInfo filterInfo;
	private View headView;
	private HorizontalListView orderGridView, isFinishGridView, typeGridView, yearGridView;
	private List<Filter> orderList, isFinishList, typeList, yearList;
	private FilterAdapter orderAdapter, isFinishAdapter, typeAdapter, yearAdapter;
	private int order, isFinish, type, year;
	private LinearLayout isFinishLayout;

	private void initHead() {
		headView = View.inflate(getContext(), R.layout.view_head_filterinfo, null);

		orderGridView = (HorizontalListView) headView.findViewById(R.id.order_gridView);
		orderList = new ArrayList<>();
		orderAdapter = new FilterAdapter(getContext(), orderList);
		orderGridView.setAdapter(orderAdapter);
		orderGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				orderList.get(order).setSelect(false);
				orderList.get(position).setSelect(true);
				order = position;
				orderAdapter.notifyDataSetChanged();
				getData(true);
			}
		});

		isFinishLayout = (LinearLayout) headView.findViewById(R.id.isFinish_layout);
		if (type_id.equals("tv") || type_id.equals("variety")) {
			isFinishLayout.setVisibility(View.VISIBLE);
		}else{
			isFinishLayout.setVisibility(View.GONE);
		}
		isFinishGridView = (HorizontalListView) headView.findViewById(R.id.isFinish_gridView);
		isFinishList = new ArrayList<>();
		isFinishAdapter = new FilterAdapter(getContext(), isFinishList);
		isFinishGridView.setAdapter(isFinishAdapter);
		isFinishGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				isFinishList.get(isFinish).setSelect(false);
				isFinishList.get(position).setSelect(true);
				isFinish = position;
				isFinishAdapter.notifyDataSetChanged();
				getData(true);
			}
		});

		typeGridView = (HorizontalListView) headView.findViewById(R.id.type_gridView);
		typeList = new ArrayList<>();
		typeAdapter = new FilterAdapter(getContext(), typeList);
		typeGridView.setAdapter(typeAdapter);
		typeGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				typeList.get(type).setSelect(false);
				typeList.get(position).setSelect(true);
				type = position;
				typeAdapter.notifyDataSetChanged();
				getData(true);
			}
		});

		yearGridView = (HorizontalListView) headView.findViewById(R.id.years_gridView);
		yearList = new ArrayList<>();
		yearAdapter = new FilterAdapter(getContext(), yearList);
		yearGridView.setAdapter(yearAdapter);
		yearGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				yearList.get(year).setSelect(false);
				yearList.get(position).setSelect(true);
				year = position;
				yearAdapter.notifyDataSetChanged();
				getData(true);
			}
		});
	}

	

	private void getFilterData() {
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (filterInfo != null) {
					if (filterInfo.getIs_finish() != null && filterInfo.getIs_finish().size() > 0) {
						isFinishList.addAll(filterInfo.getIs_finish());
						isFinishList.get(0).setSelect(true);
						isFinishAdapter.notifyDataSetChanged();
					}
					if (filterInfo.getOrder() != null && filterInfo.getOrder().size() > 0) {
						orderList.addAll(filterInfo.getOrder());
						orderList.get(0).setSelect(true);
						orderAdapter.notifyDataSetChanged();
					}
					if (filterInfo.getType() != null && filterInfo.getType().size() > 0) {
						typeList.addAll(filterInfo.getType());
						typeList.get(0).setSelect(true);
						typeAdapter.notifyDataSetChanged();
					}
					if (filterInfo.getYear() != null && filterInfo.getYear().size() > 0) {
						yearList.addAll(filterInfo.getYear());
						yearList.get(0).setSelect(true);
						yearAdapter.notifyDataSetChanged();
					}
					initList();
					getData(true);
				}
			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				FilterApi filterApi = new FilterApi();
				filterInfo = filterApi.getFilterInfo(type_id);
				return null;
			}
		}.execute("");
	}
}
