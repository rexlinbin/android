package com.bccv.strategy.ui.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.strategy.R;
import com.bccv.strategy.api.NetWorkAPI;
import com.bccv.strategy.db.StorageModule;
import com.bccv.strategy.model.StrategyListBean;
import com.bccv.strategy.model.StrategyListBean.GameInfo;
import com.bccv.strategy.model.StrategyListBean.GameStrategyInfo;
import com.bccv.strategy.network.HttpCallback;
import com.bccv.strategy.network.NetResBean;
import com.bccv.strategy.sns.UserInfoManager;
import com.bccv.strategy.ui.adapter.StrategyListAdapter;
import com.bccv.strategy.ui.view.BackGroundView;
import com.bccv.strategy.ui.view.XListView;
import com.bccv.strategy.ui.view.XListView.IXListViewListener;
import com.bccv.strategy.utils.ImageLoaderUtil;
import com.bccv.strategy.utils.L;
import com.bccv.strategy.utils.SystemUtil;

public class StrategyListActivity extends BaseActivity implements
		IXListViewListener, OnItemClickListener {

	public static final String GAME_ID = "GAME_ID";

	private BackGroundView background_view;
	private View errorView, loadingView;
	private XListView xListView;

	private View headView;
	private ImageView app_logo;
	private TextView app_name, app_category;
	private ImageView iv_back, iv_down, iv_share, iv_collect, iv_like;

	private int[] background;

	private StrategyListAdapter dataListAdapter;
	private GameInfo gameinfo;
	private ArrayList<GameStrategyInfo> dataList = new ArrayList<GameStrategyInfo>();

	private int currentPageId = 0;

	private String gameId;
	private StorageModule sModule;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_strategy_list);

		Intent intent = getIntent();
		background = intent.getIntArrayExtra(BackGroundView.BACKGROUND_COLOR);
		gameId = intent.getStringExtra(GAME_ID);
		sModule = StorageModule.getInstance();
		initView();
		initData();
	}

	private void initView() {
		background_view = (BackGroundView) findViewById(R.id.background_view);
		loadingView = findViewById(R.id.strategy_list_loading);
		errorView = findViewById(R.id.strategy_list_error);
		xListView = (XListView) findViewById(R.id.xlistview_include);

		background_view.setGradient(background[0], background[1]);
		headView = LayoutInflater.from(mContext).inflate(
				R.layout.common_app_intro, null);

		iv_back = (ImageView)findViewById(R.id.iv_back);
		iv_down = (ImageView)findViewById(R.id.iv_down);
		iv_share = (ImageView)findViewById(R.id.iv_share);
		iv_collect = (ImageView)findViewById(R.id.iv_collect);
		iv_like = (ImageView)findViewById(R.id.iv_like);

		iv_down.setVisibility(View.INVISIBLE);
		iv_share.setVisibility(View.INVISIBLE);

		iv_back.setOnClickListener(this);
//		iv_share.setOnClickListener(this);
		iv_collect.setOnClickListener(this);
		iv_like.setOnClickListener(this);

		// private ImageView app_logo;
		// private TextView app_name,app_time;

		app_logo = (ImageView) headView.findViewById(R.id.app_logo);
		app_name = (TextView) headView.findViewById(R.id.app_name);
		app_category = (TextView) headView.findViewById(R.id.app_time);
		app_name.setText("");
		app_category.setText("");

		xListView.setOverScrollMode(android.view.View.OVER_SCROLL_NEVER);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(false);
		xListView.setEnableDragLoadMore(false);
		xListView.setXListViewListener(this);
		xListView.setOnItemClickListener(this);
		xListView.addHeaderView(headView);

		dataListAdapter = new StrategyListAdapter(this, dataList);
		xListView.setAdapter(dataListAdapter);

	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		String id = intent.getStringExtra(GAME_ID);
		if(id==null||id.equals(gameId)){
			return;
		}else{
			gameId = id;
			app_logo.setImageResource(R.drawable.default_icon);
			app_name.setText("");
			app_category.setText("");
			iv_collect.setImageResource(R.drawable.collect_2);
			iv_like.setImageResource(R.drawable.like);
			dataList.clear();
			dataListAdapter.notifyDataSetChanged();
			requestData(1);
		}
	
	}

	private void initData() {
		requestData(1);
	}

	/**
	 * @param p
	 *            请求页号
	 */
	private void requestData(int p) {
		if (SystemUtil.isNetOkWithToast(mContext)) {
			NetWorkAPI.raid_list(mContext, gameId, p, new MyHttpCallBack());
		}
	}

	@Override
	public void onRefresh() {
		// TODO 刷新数据
		if(dataList.size()==0){
			loadingView.setVisibility(View.VISIBLE);
		}
		requestData(1);
	}

	@Override
	public void onLoadMore() {
		// TODO 加载更多
		requestData(currentPageId+1);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position <= 1) {
			return;
		}
		L.v(TAG, "onItemClick", " position : " + position);
		GameStrategyInfo gameStrategyInfo = dataList.get(position-2);
		Intent appDetailsIntent = new Intent(this,
				StrategyDetailsActivity.class);
		appDetailsIntent.putExtra(BackGroundView.BACKGROUND_COLOR,
				this.background_view.getGradientColor());
		appDetailsIntent.putExtra(StrategyDetailsActivity.GAME_ID,gameId);
		appDetailsIntent.putExtra(StrategyDetailsActivity.RAID_ID,gameStrategyInfo.getId());
		// appDetailsIntent.putExtra(StrategyDetailsActivity.APP_ID,
		// dataList.get(position - 1).getCreator_id());
		startActivity(appDetailsIntent);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.iv_share:
			// TODO 分享
			break;
		case R.id.iv_collect:
			// TODO 收藏
				if(gameinfo!=null && !gameinfo.is_game_focus()){
					iv_collect.setEnabled(false);
					if (UserInfoManager.isLogin()) {
						NetWorkAPI.focus_g(mContext, gameId, new HttpCallback() {
							
							@Override
							public void onResult(NetResBean response) {
								
								if(response.success){
									gameinfo.set_game_focus(true);
									iv_collect.setImageResource(R.drawable.collect_2_press);
									if (!sModule.isLiked(gameId)) {
										sModule.addLocalLike(gameId);
									}
								}
								iv_collect.setEnabled(true);
							}
							
							@Override
							public void onError(String errorMsg) {
								iv_collect.setEnabled(true);
							}
							
							@Override
							public void onCancel() {}
						} );
					}else {
						if (!sModule.isLiked(gameId)) {
							sModule.addLocalLike(gameId);
							gameinfo.set_game_focus(true);
							iv_collect.setImageResource(R.drawable.collect_2_press);
							iv_collect.setEnabled(true);
						}
					}
				}else{
					Toast.makeText(mContext, "已关注", Toast.LENGTH_SHORT).show();
				}
			break;
		case R.id.iv_like:
			//TODO 顶游戏
			if (UserInfoManager.isLogin()) {
				if(gameinfo!=null && !gameinfo.is_game_digg()){
					iv_like.setEnabled(false);
					NetWorkAPI.digg_g(mContext, gameId, new HttpCallback() {
						
						@Override
						public void onResult(NetResBean response) {
							
							if(response.success){
								gameinfo.set_game_digg(true);
								iv_like.setImageResource(R.drawable.like_press);
							}
							iv_like.setEnabled(true);
						}
						
						@Override
						public void onError(String errorMsg) {
							iv_like.setEnabled(true);
						}
						
						@Override
						public void onCancel() {}
					});
				}
			}else {
				if (!sModule.isDigg(gameId)) {
					sModule.addLocalDigg(gameId);
					iv_like.setImageResource(R.drawable.like_press);
					Toast.makeText(mContext, "已顶", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(mContext, "已顶", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		default:
			break;
		}

	}

	private class MyHttpCallBack implements HttpCallback {

		@Override
		public void onResult(NetResBean response) {
			L.i(TAG, "onResult", response.toString());
			if (response instanceof StrategyListBean) {
				StrategyListBean data = (StrategyListBean) response;
				GameInfo info = data.getGameinfo();
				if (info != null) {
					gameinfo = info;
					ImageLoaderUtil.getInstance(mContext).displayImage(
							gameinfo.getGame_icon(), app_logo,
							ImageLoaderUtil.getAppIconImageOptions());
					app_name.setText(gameinfo.getGame_title());
					app_category.setText(gameinfo.getCat_title());
					if (UserInfoManager.isLogin()) {
						if(gameinfo.is_game_digg()){
							iv_like.setImageResource(R.drawable.like_press);
						}else{
							iv_like.setImageResource(R.drawable.like);
						}
					}else {
						if (sModule.isDigg(gameId)) {
							iv_like.setImageResource(R.drawable.like_press);
						}else{
							iv_like.setImageResource(R.drawable.like);
						}
					}
					if (UserInfoManager.isLogin()) {
						if(gameinfo.is_game_focus()){
							iv_collect.setImageResource(R.drawable.collect_2_press);
						}else{
							iv_collect.setImageResource(R.drawable.collect_2);
						}
					}else {
						if (sModule.isLiked(gameId)) {
							iv_collect.setImageResource(R.drawable.collect_2_press);
							gameinfo.set_game_focus(true);
						}else {
							iv_collect.setImageResource(R.drawable.collect_2);
							gameinfo.set_game_focus(false);
						}
					}
				}

				ArrayList<GameStrategyInfo> gameStrategyList = data.getGameStrategyList();
				int now_p = data.getNow_p();
				int total_p = data.getTotal_p();
				if (gameStrategyList != null && gameStrategyList.size() > 0) {
					if (now_p == 1) {
						dataList.clear();
					}
					dataList.addAll(gameStrategyList);
					currentPageId = now_p;
					dataListAdapter.notifyDataSetChanged();
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
				showShortToast("数据请求失败");
			}
			loadingView.setVisibility(View.GONE);
			xListView.stopRefresh();
			xListView.stopLoadMore();
		}

		@Override
		public void onCancel() {
		}

		@Override
		public void onError(String errorMsg) {
			showShortToast("数据请求失败");
			loadingView.setVisibility(View.GONE);
			xListView.stopRefresh();
			xListView.stopLoadMore();
		}

	}

}
