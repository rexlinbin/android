package com.bccv.strategy.ui.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.bccv.strategy.R;
import com.bccv.strategy.api.NetWorkAPI;
import com.bccv.strategy.model.CategoryGameListResBean;
import com.bccv.strategy.model.CategoryGameListResBean.CategoryGameInfo;
import com.bccv.strategy.network.HttpCallback;
import com.bccv.strategy.network.NetResBean;
import com.bccv.strategy.ui.adapter.GameGridAdapter;
import com.bccv.strategy.ui.view.BackGroundView;
import com.bccv.strategy.utils.L;
import com.bccv.strategy.utils.SystemUtil;

public class GameListActivity extends BaseActivity implements OnItemClickListener {

	public static final String CATEGORY_ID_KEY = "CATEGORY_ID_KEY";
	public static final String CATEGORY_NAME_KEY = "CATEGORY_NAME_KEY";
	
	private BackGroundView background_view;
	private View titleBackBtn,titleRightBtn,title;
	private TextView titleTv;
	private View loadingView;
	private View errorView;
	private GridView gridView;
	
	private int[] background;
	
	private ArrayList<CategoryGameInfo> gameInfoList = new ArrayList<CategoryGameInfo>();
	
	private GameGridAdapter mAdapter;
	
	private String titleStr,categoryId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_list);
		Intent intent = getIntent();
		background = intent.getIntArrayExtra(BackGroundView.BACKGROUND_COLOR);
		titleStr = intent.getStringExtra(CATEGORY_NAME_KEY);
		categoryId = intent.getStringExtra(CATEGORY_ID_KEY);
		initView();
		initData();
	}
	
	private void initView(){
		background_view = (BackGroundView) findViewById(R.id.background_view);
		
		titleBackBtn =  findViewById(R.id.common_title_back_btn);
		titleRightBtn = findViewById(R.id.common_title_right_btn);
		titleTv = (TextView) findViewById(R.id.common_title_tv);
		loadingView = findViewById(R.id.gamelist_loading);
		errorView = findViewById(R.id.gamelist_error);
		gridView = (GridView) findViewById(R.id.gamelist_gridview);
		
		background_view.setGradient(background[0], background[1]);
		
		titleTv.setText(titleStr);
		
		errorView.setOnClickListener(this);
		titleBackBtn.setOnClickListener(this);
		titleRightBtn.setOnClickListener(this);
		gridView.setOnItemClickListener(this);
		
		mAdapter = new GameGridAdapter(this, gameInfoList);
		gridView.setAdapter(mAdapter);
		
	}
	
	private void initData(){
		requestData();
	}
	

	
	private void requestData(){
		//TODO 请求数据
		if(SystemUtil.isNetOkWithToast(mContext)){
			loadingView.setVisibility(View.VISIBLE);
			errorView.setVisibility(View.GONE);
			NetWorkAPI.cat(mContext, categoryId, new CategoryGameListCallBack());
		}else if(gameInfoList.size()<=0){
			errorView.setVisibility(View.VISIBLE);
			loadingView.setVisibility(View.GONE);
		}
	}
	
	private class CategoryGameListCallBack implements HttpCallback{

		@Override
		public void onResult(NetResBean response) {
			// TODO Auto-generated method stub
			L.i(TAG, "CategoryGameListCallBack  onResult", "response : " + response);
			if(response!=null && response instanceof CategoryGameListResBean){
				CategoryGameListResBean data = (CategoryGameListResBean) response;
				
				if(data.success&&data.getGameInfoList()!=null){
					gameInfoList.clear();
					gameInfoList.addAll(data.getGameInfoList());
					mAdapter.notifyDataSetChanged();
					loadingView.setVisibility(View.GONE);
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
		case R.id.common_title_back_btn:
			finish();
			break;
		case R.id.gamelist_error:
			requestData();
			break;
		case R.id.common_title_right_btn:
			break;
		default:
			break;
		}
		
	}
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		CategoryGameInfo categoryGameInfo = gameInfoList.get(position);
		Intent strategyListIntent = new Intent(this, StrategyListActivity.class);
		strategyListIntent.putExtra(StrategyListActivity.GAME_ID, categoryGameInfo.getGame_id());
		strategyListIntent.putExtra(BackGroundView.BACKGROUND_COLOR, background);
		startActivity(strategyListIntent);
	}
	
}
