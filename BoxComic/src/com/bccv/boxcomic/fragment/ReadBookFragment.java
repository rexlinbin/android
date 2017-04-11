package com.bccv.boxcomic.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.adapter.MyListBookViewAdapter;
import com.bccv.boxcomic.api.EbookApi;
import com.bccv.boxcomic.ebook.BookInfo;
import com.bccv.boxcomic.net.NetUtil;
import com.bccv.boxcomic.pulltorefresh.FooterLoadingLayout;
import com.bccv.boxcomic.pulltorefresh.PullToRefreshBase;
import com.bccv.boxcomic.pulltorefresh.PullToRefreshListView;
import com.bccv.boxcomic.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.bccv.boxcomic.tool.GlobalParams;
import com.bccv.boxcomic.tool.PromptManager;
import com.bccv.boxcomic.tool.SerializationUtil;

import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class ReadBookFragment extends Fragment {
	private View mView;
	private PullToRefreshListView gridview;
	private List<BookInfo> data;
	private MyListBookViewAdapter adapter;
	private int page = 1;
	private int pageNum=15;
	private String result;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_main_child, null);

		data = new ArrayList<BookInfo>();
		gridview = (PullToRefreshListView) mView
				.findViewById(R.id.frament_main_listView);

		adapter = new MyListBookViewAdapter(data, getActivity(), getActivity());

		gridview.getRefreshableView().setAdapter(adapter);
		gridview.setPullLoadEnabled(true);
		gridview.setPullRefreshEnabled(true);
		gridview.getRefreshableView().setSelector(
				new ColorDrawable(android.R.color.transparent));
		// gridview.doPullRefreshing(true, 100);

		gridview.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if (!NetUtil.isNetworkAvailable(GlobalParams.context)) {
					// 提示网络不给力,直接完成刷新
					PromptManager.showToast(GlobalParams.context, "网络不给力");
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

		result = "refresh";
		new MainTask().execute(result);
		return mView;

	}

	class MainTask extends AsyncTask<String, Void, List<BookInfo>> {

		@Override
		protected List<BookInfo> doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String re = arg0[0];
			if (re.equals("refresh")) {
//				pageNum=18;
				page = 1;
				data.clear();
			} else if (re.equals("more")) {
//				pageNum=12;
				page++;

			}

			EbookApi ebookApi = new EbookApi();
			List<BookInfo> list = ebookApi.getUpdate("111", "222", page, pageNum);

			return list;
		}

		@Override
		protected void onPostExecute(List<BookInfo> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				if (result != null) {
					SerializationUtil.wirteBookInfoSerialization(getActivity()
							.getApplicationContext(), (Serializable) result);
					data.addAll(result);
					adapter.notifyDataSetChanged();
				} else if (result == null && page == 1) {

					result =SerializationUtil.readBookInfoCache(getActivity().getApplicationContext());
					data.addAll(result);
					adapter.notifyDataSetChanged();
				} else {
					Toast.makeText(getActivity(), "没有更多小说了", Toast.LENGTH_SHORT)
							.show();

				}

				if (page == 1) {
					gridview.onPullDownRefreshComplete();

				} else {
					gridview.onPullUpRefreshComplete();

				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			

		}

	}

}
