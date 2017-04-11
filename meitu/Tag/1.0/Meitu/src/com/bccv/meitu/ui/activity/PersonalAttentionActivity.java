package com.bccv.meitu.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.bccv.meitu.R;
import com.bccv.meitu.api.NetWorkAPI;
import com.bccv.meitu.model.AuthorInfo;
import com.bccv.meitu.model.GetAttendedList;
import com.bccv.meitu.network.HttpCallback;
import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.ui.adapter.PersonalAttentionListAdapter;
import com.bccv.meitu.utils.Logger;
import com.me.maxwin.view.NormalXListView;
import com.me.maxwin.view.NormalXListView.IXListViewListener;

public class PersonalAttentionActivity extends BaseActivity implements IXListViewListener {

	
	private View left_buton;
	private TextView title_text;
	
	private NormalXListView mListView;
	
	private View noresultView;
	private View waitting_layout;
	
	private PersonalAttentionListAdapter mAdapter;
	
	private List<AuthorInfo> dataList;
	
	private int currentPage = 0;
	private int totalPage = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attention_view);
		initView();
		initData();
	}
	
	private void initView(){
		dataList = new ArrayList<AuthorInfo>();
		
		noresultView = findViewById(R.id.ll_no_result);
		waitting_layout = findViewById(R.id.waitting_layout);
		
		left_buton = findViewById(R.id.left_buton);
		title_text = (TextView) findViewById(R.id.title_text);
		title_text.setText("我的关注");
		
		left_buton.setOnClickListener(this);
		
		mListView = (NormalXListView) findViewById(R.id.attention_xlv);
		mAdapter = new PersonalAttentionListAdapter(mContext, dataList);
		
		mListView.setAdapter(mAdapter);
		
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.hideFooter();
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(PersonalAttentionActivity.this,
						ProZoneActivity.class);
				intent.putExtra("author_id", dataList.get(position-1).getAuthor_id());
				startActivity(intent);
			}
		});
	}
	
	private void initData(){
		refreshData();
	}
	
	/**
	 * 刷新数据
	 */
	public void refreshData(){
		noresultView.setVisibility(View.GONE);
		if(dataList.size()==0){
			// 显示loading
			showWaiting(true);
		}
		// 获取数据
		getNetData(1,true);
	}
	
	/**
	 * 获取指定页的数据
	 * 
	 * @param page  页码
	 */
	private void getNetData(int page,final boolean isRefresh){
		
		NetWorkAPI.getAttendedList(mContext, page, new HttpCallback() {
					
					@Override
					public void onResult(NetResBean response) {
		
						if(response.success && response instanceof GetAttendedList){
							
							GetAttendedList data = (GetAttendedList) response;
							Logger.v(TAG, "getNetData onResult", " data : " + data);
							currentPage = data.getPage();
							totalPage = data.getPage_total();
							List<AuthorInfo> list = data.getList();
							if(isRefresh){
								dataList.clear();
							}
							if(list!=null){
								dataList.addAll(list);
							}
							mAdapter.notifyDataSetChanged();
						}
						if(dataList.size()==0){
							noresultView.setVisibility(View.VISIBLE);
						}
						
						mListView.stopRefresh();
						mListView.stopLoadMore();
						if(totalPage>currentPage){
							mListView.showFooter();
						}
						showWaiting(false);
					}
					
					@Override
					public void onError(String errorMsg) {
						Logger.e(TAG, "getNetData  onError", errorMsg+"");
						if(dataList.size()==0){
							noresultView.setVisibility(View.VISIBLE);
						}
						showWaiting(false);
					}
					
					@Override
					public void onCancel() {}
				});
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
		mListView.showFooter();
		if(currentPage>=totalPage){
			mListView.stopLoadMore();
			mListView.hideFooter();
			return;
		}
		getNetData(++currentPage, false);
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.left_buton:
			finish();
			break;

		default:
			break;
		}
		
	}
	
}
