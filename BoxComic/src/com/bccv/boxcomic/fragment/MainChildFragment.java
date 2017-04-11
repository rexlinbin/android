package com.bccv.boxcomic.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.activity.ComicInfoActivity;
import com.bccv.boxcomic.activity.ComicInfoLocalActivity;
import com.bccv.boxcomic.adapter.HomeHeardAdapter;
import com.bccv.boxcomic.adapter.MainListViewAdapter;
import com.bccv.boxcomic.api.HomeHotApi;
import com.bccv.boxcomic.ebook.BookInfo;
import com.bccv.boxcomic.ebook.EbookActivity;
import com.bccv.boxcomic.ebook.PageActivity;
import com.bccv.boxcomic.modal.Comic;
import com.bccv.boxcomic.modal.MainItem;
import com.bccv.boxcomic.net.NetUtil;
import com.bccv.boxcomic.pulltorefresh.FooterLoadingLayout;
import com.bccv.boxcomic.pulltorefresh.PullToRefreshBase;
import com.bccv.boxcomic.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.bccv.boxcomic.pulltorefresh.PullToRefreshListView;
import com.bccv.boxcomic.tool.GlobalParams;
import com.bccv.boxcomic.tool.Logger;
import com.bccv.boxcomic.tool.PromptManager;
import com.bccv.boxcomic.tool.SerializationUtil;

@SuppressLint("NewApi")
public class MainChildFragment extends Fragment {
	private View mView, view;
	private PullToRefreshListView gridview;
	private List<Comic> data;
	private MainListViewAdapter adapter;
	private LinearLayout linear;
	private ListView list;
	private int type;
	private int page = 1;
	private int pageNum=15;
	private String result;
	private HomeHeardAdapter homeAdapter;
	private List<Comic> homeData;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_main_child, null);
		view = inflater.inflate(R.layout.home_heard_view, null);
		ListView headList = (ListView) view
				.findViewById(R.id.home_heard_listview);
		headList.setSelector(new ColorDrawable(android.R.color.transparent));
		homeData = new ArrayList<Comic>();

		homeAdapter = new HomeHeardAdapter(homeData, getActivity());

		headList.setAdapter(homeAdapter);

		headList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				if(!homeData.get(arg2).getComic_id().equals("-1")){
				
				if (homeData.get(arg2).isComic()) { 

					if (homeData.get(arg2).isLocal()) {

						Intent aintent = new Intent(getActivity(),
								ComicInfoLocalActivity.class);

						aintent.putExtra("mainitem", homeData.get(arg2)
								.getComic_id());

						startActivity(aintent);

					} else {

						Intent aintent = new Intent(getActivity(),
								ComicInfoActivity.class);

						aintent.putExtra("mainitem", homeData.get(arg2)
								.getComic_id());

						startActivity(aintent); 

					}

				} else {

					Comic comic = homeData.get(arg2);
					BookInfo info = new BookInfo();
					info.setBook_id(Integer.parseInt(comic.getComic_id()));
					info.setBook_author(comic.getComic_author());
					info.setBook_title(comic.getComic_title());
					info.setBook_titlepic(comic.getComic_titlepic());

					info.setBook_last_chaptitle(comic.getComic_last_chaptitle());

					Intent aintent = new Intent(getActivity(),
							EbookActivity.class);

					aintent.putExtra(EbookActivity.BOOK_INFO_KEY, info);
					startActivity(aintent);

				}

			}
			}
		});

		gridview = (PullToRefreshListView) mView
				.findViewById(R.id.frament_main_listView);
		data = new ArrayList<Comic>();
		list = gridview.getRefreshableView();

		adapter = new MainListViewAdapter(data, getActivity(), getActivity());
		if (type == 1) {
			// Logger.e("MainChildFragment", type+"");
		} else {
			// Logger.e("MainChildFragment", type+"");

			list.addHeaderView(view);

		}
		list.setDivider(new ColorDrawable(android.R.color.transparent));
		gridview.getRefreshableView().setAdapter(adapter);
		gridview.setPullLoadEnabled(true);
		gridview.setPullRefreshEnabled(true);
		gridview.getRefreshableView().setSelector(
				new ColorDrawable(android.R.color.transparent));
		gridview.doPullRefreshing(true, 100);
		gridview.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if (!NetUtil.isNetworkAvailable(GlobalParams.context)) {
					// 提示网络不给力,直接完成刷新
					PromptManager.showToast(GlobalParams.context, "网络不给力");
					result = "refresh";
					new MainTask().execute(result);
					gridview.onPullDownRefreshComplete();
				} else {
					result = "refresh";
					new MainTask().execute(result);

				}
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if (NetUtil.isNetworkAvailable(GlobalParams.context)) {
					((FooterLoadingLayout) gridview.getFooterLoadingLayout())
							.getmHintView().setText("数据加载中...");
					result = "more";
					new MainTask().execute(result);
				} else {
					PromptManager.showToast(GlobalParams.context, "网络不给力");
					gridview.onPullUpRefreshComplete();
				}
			}

		});

		// gridview.getRefreshableView().setOnItemClickListener(
		// new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1,
		// int arg2, long arg3) {
		// // TODO Auto-generated method stub
		// Intent aintent = new Intent(getActivity(),
		// ComicInfoActivity.class);
		//
		// aintent.putExtra("comic", data.get(arg2));
		//
		// startActivity(aintent);
		//
		// }
		// });

		result = "refresh";

		return mView;

	}

	private void setData() {
		List<Comic> comicList = SerializationUtil
				.readHistoryCache(getActivity().getApplicationContext());
		if (comicList != null) {

			if (comicList.size() == 0) {

				comicList = new ArrayList<Comic>();
				Comic comic = new Comic();
				comic.setComic_id("-1");
				comic.setComic_title("亲,外星来的吧");
				comicList.add(comic);
			}

		} else {
			comicList = new ArrayList<Comic>();
			Comic comic = new Comic();
			comic.setComic_id("-1");
			comic.setComic_title("亲,外星来的吧");
			comicList.add(comic);

		}

		homeData.clear();
		homeData.addAll(comicList);

		homeAdapter.notifyDataSetChanged();

	}

	class MainTask extends AsyncTask<String, Void, List<Comic>> {

		@Override
		protected List<Comic> doInBackground(String... params) {
			String re = params[0];

			if (re.equals("refresh")) {
//				pageNum=18;
				page = 1;
				data.clear();
			} else if (re.equals("more")) {
//				pageNum=12;

				page++;

			}

			HomeHotApi hot = new HomeHotApi();
			if (type == 1) {
				List<Comic> list = hot.getHome("111", "222", page, pageNum);
				

				return list;
			} else {

				List<Comic> list = hot.getUpdate("111", "222", page, pageNum);
				

				return list;
			}

		}

		@Override
		protected void onPostExecute(List<Comic> list) {
			super.onPostExecute(list);

			try {

				if (list == null) {

					if (type == 1) {
						list = SerializationUtil.readHomeCache(getActivity()
								.getApplicationContext());

					} else {
						list = SerializationUtil.readUpdateCache(getActivity()
								.getApplicationContext());

					}
				
					data.addAll(list);
					adapter.notifyDataSetChanged();
				} else {
					if (type == 1) {
						SerializationUtil.wirteHomeSerialization(getActivity()
								.getApplicationContext(), (Serializable) list);
						Logger.e("数据的type ", list.size() + "");
					} else {

						SerializationUtil.wirteUPDATeSerialization(
								getActivity().getApplicationContext(),
								(Serializable) list);
					}
					//删除相同的数据
//					List<Comic> deleteComic = new ArrayList<Comic>();
//					for (Comic item1 : data) {
//					
//						for (Comic item2 : list) {
//						
//							if (item2.getComic_id().equals( item1.getComic_id())) {
//							
//								deleteComic.add(item2);
//							}
//
//						}
//
//					}
//					for (Comic item : deleteComic) {
//
//						list.remove(item);
//
//					}
					
					data.addAll(list);
					adapter.notifyDataSetChanged();
				}

				if (page == 1) {
					gridview.onPullDownRefreshComplete();

				} else {
					gridview.onPullUpRefreshComplete();

				}

			} catch (Exception e) {
				// TODO: handle exception
				Log.e("MainChildFragment", e.toString());
			}
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setData();
		Log.e("onResume", "MainChildFragment.............onResume");
	}

}
