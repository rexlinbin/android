package com.bccv.strategy.ui.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bccv.strategy.api.NetWorkAPI;
import com.bccv.strategy.model.MoreDiggiconResBean;
import com.bccv.strategy.model.MoreDiggiconResBean.Diggicon;
import com.bccv.strategy.network.HttpCallback;
import com.bccv.strategy.network.NetResBean;
import com.bccv.strategy.ui.view.BackGroundView;
import com.bccv.strategy.ui.view.XListView;
import com.bccv.strategy.ui.view.XListView.IXListViewListener;
import com.bccv.strategy.utils.ImageLoaderUtil;
import com.bccv.strategy.utils.L;
import com.bccv.strategy.utils.SystemUtil;
import com.bccv.strategy.R;

public class MoreZanListActivity extends BaseActivity implements
		IXListViewListener, OnItemClickListener {

	public static final String COMMENT_ID_KEY = "comment_id_key";

	private BackGroundView background_view;
	private View errorView, loadingView;
	private View common_title_back_btn, common_title_right_btn;
	private TextView common_title_tv;
	private XListView xListView;

	private ArrayList<Diggicon> digList = new ArrayList<Diggicon>();
	private int[] background;

	private MyAdapter mAdapter;

	private String comment_id;
	private int currentPageId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more_zan_list);
		Intent intent = getIntent();
		background = intent.getIntArrayExtra(BackGroundView.BACKGROUND_COLOR);
		comment_id = intent.getStringExtra(COMMENT_ID_KEY);
		initView();
		initData();
	}

	private void initView() {
		background_view = (BackGroundView) findViewById(R.id.background_view);
		common_title_back_btn = findViewById(R.id.common_title_back_btn);
		common_title_right_btn = findViewById(R.id.common_title_right_btn);
		common_title_tv = (TextView) findViewById(R.id.common_title_tv);
		xListView = (XListView) findViewById(R.id.xlistview_include);
		loadingView = findViewById(R.id.more_zan_list_loading);
		errorView = findViewById(R.id.more_zan_list_error);

		common_title_tv.setText("更多赞过的用户");
		background_view.setGradient(background[0], background[1]);

		common_title_back_btn.setOnClickListener(this);
		common_title_right_btn.setOnClickListener(this);

		xListView.setOverScrollMode(android.view.View.OVER_SCROLL_NEVER);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(false);
		xListView.setEnableDragLoadMore(false);
		xListView.setXListViewListener(this);
		xListView.setOnItemClickListener(this);

		mAdapter = new MyAdapter();
		xListView.setAdapter(mAdapter);

	}

	private void initData() {
		requestData(1);
	}

	private void requestData(int p) {

		if (SystemUtil.isNetOkWithToast(mContext)) {
			NetWorkAPI.more_diggicon(mContext, p, comment_id,
					new HttpCallback() {

						@Override
						public void onResult(NetResBean response) {
							// TODO Auto-generated method stub
							L.i(TAG, "onResult", response.toString());
							if (response instanceof MoreDiggiconResBean) {
								MoreDiggiconResBean data = (MoreDiggiconResBean) response;
								ArrayList<Diggicon> diggicons = data
										.getDiggicons();
								int now_p = data.getNow_p();
								int total_p = data.getTotal_p();
								if (diggicons != null && diggicons.size() > 0) {
									if (now_p == 1) {
										digList.clear();
									}
									digList.addAll(diggicons);
									currentPageId = now_p;
									mAdapter.notifyDataSetChanged();
								} else {
									showShortToast("数据请求失败");
								}
								xListView.stopRefresh();
								xListView.stopLoadMore();
								if (currentPageId >= total_p) {
									xListView.setPullLoadEnable(false);
									xListView.setEnableDragLoadMore(false);
								} else {
									xListView.setPullLoadEnable(true);
									xListView.setEnableDragLoadMore(true);
								}
								loadingView.setVisibility(View.GONE);
							} else {
								xListView.stopRefresh();
								xListView.stopLoadMore();
								showShortToast("数据请求失败");
								loadingView.setVisibility(View.GONE);
							}

						}

						@Override
						public void onError(String errorMsg) {
							xListView.stopRefresh();
							xListView.stopLoadMore();
							loadingView.setVisibility(View.GONE);
							showShortToast("数据请求失败");
						}

						@Override
						public void onCancel() {
						}
					});
		}

	}

	@Override
	public void onRefresh() {
		// TODO 刷新数据
		if (digList.size() == 0) {
			loadingView.setVisibility(View.VISIBLE);
		}
		requestData(1);
	}

	@Override
	public void onLoadMore() {
		// TODO 加载更多
		requestData(currentPageId + 1);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent personalIntent = new Intent(MoreZanListActivity.this,
				PersonalZoneActivity.class);
		personalIntent.putExtra(BackGroundView.BACKGROUND_COLOR,
				background_view.getGradientColor());
		personalIntent.putExtra(PersonalZoneActivity.ZONE_ID_KEY,
				digList.get(position - 1).getUser_id());
		startActivity(personalIntent);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.common_title_back_btn:
			finish();
			break;

		case R.id.common_title_right_btn:
			Intent appReleaseIntent = new Intent(this, AppReleaseActivity.class);
			appReleaseIntent.putExtra(BackGroundView.BACKGROUND_COLOR,
					background_view.getGradientColor());
			startActivity(appReleaseIntent);
			overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_none);
			break;
		default:
			break;
		}
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return digList == null ? 0 : digList.size();
		}

		@Override
		public Object getItem(int position) {
			return digList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			Holder mHolder;
			if (convertView == null) {
				mHolder = new Holder();
				convertView = View.inflate(mContext,
						R.layout.more_zan_list_item, null);
				mHolder.icon = (ImageView) convertView
						.findViewById(R.id.morezan_item_icon);
				mHolder.name = (TextView) convertView
						.findViewById(R.id.morezan_item_name);
			} else {
				mHolder = (Holder) convertView.getTag();
			}
			// TODO 设置数据
			Diggicon diggicon = digList.get(position);
			ImageLoaderUtil.getInstance(mContext).displayImage(
					diggicon.getUser_icon(), mHolder.icon,
					ImageLoaderUtil.getUserIconImageOptions());
			mHolder.name.setText(diggicon.getUser_name());

			if (position % 2 == 0) {
				convertView
				.setBackgroundResource(R.drawable.more_zan_item_dark2light_selector);
			} else {
				convertView
				.setBackgroundResource(R.drawable.more_zan_item_light2dark_selector);
			}
			convertView.setTag(mHolder);
			return convertView;
		}

		private class Holder {
			ImageView icon;
			TextView name;
		}

	}

}
