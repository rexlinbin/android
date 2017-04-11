package com.bccv.strategy.ui.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.bccv.strategy.api.NetWorkAPI;
import com.bccv.strategy.model.AppInfoItemBean;
import com.bccv.strategy.model.SearchAppListResBean;
import com.bccv.strategy.model.SearchRecommendListResBean;
import com.bccv.strategy.network.HttpCallback;
import com.bccv.strategy.network.NetResBean;
import com.bccv.strategy.ui.view.BackGroundView;
import com.bccv.strategy.utils.ImageLoaderUtil;
import com.bccv.strategy.utils.L;
import com.bccv.strategy.utils.SystemUtil;
import com.bccv.strategy.R;

public class SearchActivity extends BaseActivity {

	public static final int SHOW_HOT_WORD = 1;
	public static final int SHOW_RESULT = 2;

	private BackGroundView background_view;
	private View iv_common_left;
	private EditText search_edit_et;
	private View search_edit_clear;

	private View errorView, loadingView;

	private ListView listview_include;
	private GridView search_gridview;

	private ArrayList<String> hotwords = new ArrayList<String>();
	private ArrayList<AppInfoItemBean> searchResult = new ArrayList<AppInfoItemBean>();

	private HotWordAdapter hotwordAdapter;
	private AppGridAdapter resultAdapter;

	private int[] background;

	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		Intent intent = getIntent();
		background = intent.getIntArrayExtra(BackGroundView.BACKGROUND_COLOR);
		initHandler();
		initView();
		initData();
	}

	private void initView() {
		background_view = (BackGroundView) findViewById(R.id.background_view);
		iv_common_left = findViewById(R.id.iv_common_left);
		search_edit_et = (EditText) findViewById(R.id.search_edit_et);
		search_edit_clear = findViewById(R.id.search_edit_clear);
		listview_include = (ListView) findViewById(R.id.listview_include);
		search_gridview = (GridView) findViewById(R.id.search_gridview);

		loadingView = findViewById(R.id.search_loading);
		errorView = findViewById(R.id.search_error);

		listview_include.setOverScrollMode(android.view.View.OVER_SCROLL_NEVER);
		hotwordAdapter = new HotWordAdapter();
		listview_include.setAdapter(hotwordAdapter);

		search_gridview.setOverScrollMode(android.view.View.OVER_SCROLL_NEVER);
		resultAdapter = new AppGridAdapter();
		search_gridview.setAdapter(resultAdapter);

		background_view.setGradient(background[0], background[1]);

		iv_common_left
				.setBackgroundResource(R.drawable.title_menu_left_down_selector);

		iv_common_left.setOnClickListener(this);
		search_edit_clear.setOnClickListener(this);

		search_edit_et.setOnEditorActionListener(onEditorActionListener);

		listview_include.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				doSearch(hotwords.get(position));
			}
		});

		search_gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 点击gridView Item
				
				Intent appDetailsIntent = new Intent(SearchActivity.this,
						StrategyListActivity.class);
				appDetailsIntent.putExtra(BackGroundView.BACKGROUND_COLOR,
						background_view.getGradientColor());
//				appDetailsIntent.putExtra(StrategyDetailsActivity.RAID_ID,
//						searchResult.get(position).getId());
				appDetailsIntent.putExtra(StrategyListActivity.GAME_ID,
						searchResult.get(position).getGame_id());
				startActivity(appDetailsIntent);
			}

		});

	}

	private void initData() {
		
		if (SystemUtil.isNetOkWithToast(this.getApplicationContext())) {
			NetWorkAPI.search_list(mContext, new HttpCallback() {
				
				@Override
				public void onResult(NetResBean response) {
					// TODO Auto-generated method stub
					L.i(TAG, "onResult", response.toString());
					if (response instanceof SearchRecommendListResBean) {
						SearchRecommendListResBean data = (SearchRecommendListResBean) response;
						ArrayList<String> recommendList = data.getRecommendList();
						if (recommendList != null && recommendList.size() > 0) {
							hotwords.addAll(recommendList);
							hotwordAdapter.notifyDataSetChanged();
							loadingView.setVisibility(View.GONE);
						} else {
							showShortToast("热词加载失败");
							loadingView.setVisibility(View.GONE);
						}
					} else {
						showShortToast("热词加载失败");
						loadingView.setVisibility(View.GONE);
					}
				}
				
				@Override
				public void onError(String errorMsg) {
					showShortToast("热词加载失败");
					loadingView.setVisibility(View.GONE);
				}
				
				@Override
				public void onCancel() {
				}
			});
		}

	}

	@SuppressLint("HandlerLeak")
	private void initHandler() {
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				switch (msg.what) {
				case SHOW_HOT_WORD:
					loadingView.setVisibility(View.GONE);
					hotwordAdapter.notifyDataSetChanged();
					break;
				case SHOW_RESULT:
					loadingView.setVisibility(View.GONE);
					listview_include.setVisibility(View.GONE);
					search_gridview.setVisibility(View.VISIBLE);

					break;

				default:
					break;
				}

			}

		};
	}

	private void doSearch(String key) {
		if (SystemUtil.isNetOkWithToast(this.getApplicationContext())) {
			
			if (TextUtils.isEmpty(key)) {
				showShortToast("请输入搜索关键字");
				return;
			}
			
			loadingView.setVisibility(View.VISIBLE);
			/* 隐藏软键盘 */
			hideSoftInput();
			// TODO 搜索
			NetWorkAPI.search(mContext, key, new HttpCallback() {
				
				@Override
				public void onResult(NetResBean response) {
					// TODO Auto-generated method stub
					L.i(TAG, "onResult", response.toString());
					if (response instanceof SearchAppListResBean) {
						SearchAppListResBean data = (SearchAppListResBean) response;
						ArrayList<AppInfoItemBean> appInfoItemBeans = data
								.getAppInfoItemBeans();
						if (appInfoItemBeans != null && appInfoItemBeans.size() > 0) {
							searchResult.clear();
							searchResult.addAll(appInfoItemBeans);
							resultAdapter.notifyDataSetChanged();
						} else {
							showShortToast("暂无该应用信息");
						}
						loadingView.setVisibility(View.GONE);
						listview_include.setVisibility(View.GONE);
						search_gridview.setVisibility(View.VISIBLE);
					} else {
						showShortToast("数据请求失败");
						loadingView.setVisibility(View.GONE);
					}
				}
				
				@Override
				public void onError(String errorMsg) {
					// TODO Auto-generated method stub
					showShortToast("数据请求失败");
					loadingView.setVisibility(View.GONE);
				}
				
				@Override
				public void onCancel() {
				}
			});
			
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.iv_common_left:
			finish();
			break;
		case R.id.search_edit_clear:
			search_edit_et.setText("");
			break;

		default:
			break;
		}

	}

	private void hideSoftInput() {
		/* 隐藏软键盘 */
		InputMethodManager imm = (InputMethodManager) search_edit_et
				.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(
					search_edit_et.getApplicationWindowToken(), 0);
		}
	}

	@Override
	public void finish() {
		hideSoftInput();
		super.finish();
		overridePendingTransition(R.anim.in_from_none, R.anim.out_to_bottom);
	}

	private class HotWordAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return hotwords == null ? 0 : hotwords.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return hotwords.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			HotWordHolder holder;
			if (convertView == null) {
				holder = new HotWordHolder();
				convertView = View.inflate(mContext,
						R.layout.search_hotword_item, null);
				holder.tv = (TextView) convertView
						.findViewById(R.id.hotword_tv);
			} else {
				holder = (HotWordHolder) convertView.getTag();
			}
			holder.tv.setText(hotwords.get(position));

			convertView.setTag(holder);
			return convertView;
		}

		private class HotWordHolder {
			TextView tv;
		}

	}

	public class AppGridAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return searchResult == null ? 0 : searchResult.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return searchResult.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Holder holder;
			if (convertView == null) {
				convertView = View.inflate(mContext, R.layout.app_item, null);
				holder = new Holder();
				holder.appIcon = (ImageView) convertView
						.findViewById(R.id.app_icon);
				holder.appName = (TextView) convertView
						.findViewById(R.id.app_name);
			} else {
				holder = (Holder) convertView.getTag();
			}
			
			
			// TODO 设置数据
			AppInfoItemBean appInfoItemBean = searchResult.get(position);
			ImageLoaderUtil.getInstance(mContext).displayImage(
					appInfoItemBean.getGame_icon(), holder.appIcon,
					ImageLoaderUtil.getAppIconImageOptions());
			holder.appName.setText(appInfoItemBean.getGame_title());

			convertView.setTag(holder);
			return convertView;
		}

		private class Holder {
			public ImageView appIcon;
			public TextView appName;
		}

	}

	private OnEditorActionListener onEditorActionListener = new OnEditorActionListener() {

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

			if (actionId == EditorInfo.IME_ACTION_NEXT
					|| actionId == EditorInfo.IME_ACTION_DONE) {

				String key = search_edit_et.getText().toString().trim();
				if (TextUtils.isEmpty(key)) {
					showShortToast("请输入搜索关键字");
					return true;
				}
				doSearch(key);
				return true;
			}
			return false;
		}
	};

}
