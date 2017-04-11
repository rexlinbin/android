package com.bccv.zhuiyingzhihanju.fragment;

import java.util.ArrayList;
import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.activity.VideoInfoActivity;
import com.bccv.zhuiyingzhihanju.adapter.SpecialAdapter;
import com.bccv.zhuiyingzhihanju.api.SpecialApi;
import com.bccv.zhuiyingzhihanju.model.Movie;
import com.bccv.zhuiyingzhihanju.model.MovieModel;
import com.bccv.zhuiyingzhihanju.model.MovieNews;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tendcloud.tenddata.TCAgent;
import com.utils.net.NetUtil;
import com.utils.pulltorefresh.FooterLoadingLayout;
import com.utils.pulltorefresh.PullToRefreshBase;
import com.utils.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.utils.pulltorefresh.PullToRefreshListView;
import com.utils.tools.BaseFragment;
import com.utils.tools.Callback;
import com.utils.tools.GlobalParams;
import com.utils.tools.PromptManager;
import com.utils.tools.ScreenUtil;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class HJFragment extends BaseFragment {
	View view;

	private ListView list;
	private List<Movie> data;
	private List<Movie> SList;

	private SpecialAdapter adapter;
	MovieNews sp;
	private PullToRefreshListView pullToRefreshListView;
	private List<Movie> topList = new ArrayList<Movie>();

	boolean isStop = true;
	private String type;
	private View headView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (view == null) {
			view = inflater.inflate(R.layout.special_item, container, false);
			Bundle args = getArguments();
			Boolean isSelect = args.getBoolean("ifSelect", false);
			type = args.getString("type");

			data = new ArrayList<Movie>();

			pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.special_listView);

			list = pullToRefreshListView.getRefreshableView();
			headView = View.inflate(getContext(), R.layout.select_headview, null);

			imageView = (ImageView) headView.findViewById(R.id.select_fancy_ImageView);

			Ptext = (TextView) headView.findViewById(R.id.fancy_viewPager_text);

			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (topList != null && topList.size() > 0) {
						Intent aIntent = new Intent(getActivity(), VideoInfoActivity.class);

						aIntent.putExtra("movie_id", topList.get(0).getId());
						aIntent.putExtra("type_id", topList.get(0).getType_id());
						aIntent.putExtra("episodes_id", "0");

						startActivity(aIntent);
					}

				}
			});

			list.addHeaderView(headView);
			adapter = new SpecialAdapter(data, getActivity(), this);
			if (type.equals("hj")) {
				adapter.isTV(true);
			}

			list.setVerticalScrollBarEnabled(false);
			list.setDividerHeight(0);
			list.setAdapter(adapter);
			list.setSelector(new ColorDrawable(android.R.color.transparent));
			pullToRefreshListView.setPullLoadEnabled(true);
			pullToRefreshListView.setPullRefreshEnabled(true);
			pullToRefreshListView.getRefreshableView().setSelector(new ColorDrawable(android.R.color.transparent));

			list.setVerticalScrollBarEnabled(false);

			// list.setOnItemClickListener(new OnItemClickListener() {
			//
			// @Override
			// public void onItemClick(AdapterView<?> parent, View view, int
			// position, long id) {
			// // TODO Auto-generated method stub
			// Intent aIntent = new Intent(getActivity(),
			// VideoInfoActivity.class);
			//
			// aIntent.putExtra("movie_id", data.get(position).getId());
			// aIntent.putExtra("type_id", data.get(position).getType_id());
			// aIntent.putExtra("episodes_id", "1");
			//
			// startActivity(aIntent);
			//
			// }
			// });

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

		}

		Ptext = (TextView) view.findViewById(R.id.fancy_viewPager_text);
		getData(true);
		return view;

	}

	private int page = 1, count = 6;

	public void getData(final boolean isRefresh) {
		if (isRefresh) {
			page = 1;
		}
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (sp != null) {
					if (isRefresh) {
						data.clear();
						topList.clear();
						topList = sp.getBig();
						init(topList);
					}

					SList = sp.getList();

					data.addAll(SList);
					adapter.notifyDataSetChanged();

				}
				if (isRefresh) {
					pullToRefreshListView.onPullDownRefreshComplete();
				} else {
					pullToRefreshListView.onPullUpRefreshComplete();
				}

			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {

				SpecialApi api = new SpecialApi();
				if (type.equals("hj")) {
					sp = api.getThemList("tv", page + "", count + "");
				} else if (type.equals("hz")) {
					sp = api.getThemList("variety", page + "", count + "");
				} else if (type.equals("hy")) {
					sp = api.getThemList("movie", page + "", count + "");
				} else if (type.equals("news")) {
					sp = api.getThemList("news", page + "", count + "");
				} else if (type.equals("music")) {
					sp = api.getThemList("music", page + "", count + "");
				}

				page++;
				return "true";
			}
		}.execute("");
	}

	public void goInfo(int position) {
		Intent intent = new Intent(getActivity().getApplicationContext(), VideoInfoActivity.class);
		intent.putExtra("movie_id", data.get(position).getId());
		intent.putExtra("type_id", data.get(position).getType_id());
		intent.putExtra("episodes_id", "0");
		startActivity(intent);
	}

	private ImageView imageView;
	private TextView Ptext;

	private void init(final List<Movie> movieDodel) {

		if (movieDodel != null && movieDodel.size() > 0 && movieDodel.get(0) != null) {
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage(movieDodel.get(0).getImages(), imageView, GlobalParams.movieOptions);
			Ptext.setText(movieDodel.get(0).getTitle());
		}

	}

}
