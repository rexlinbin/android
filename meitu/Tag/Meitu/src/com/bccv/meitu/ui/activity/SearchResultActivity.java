package com.bccv.meitu.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bccv.meitu.R;
import com.bccv.meitu.api.NetWorkAPI;
import com.bccv.meitu.model.GetTaginfoResBean;
import com.bccv.meitu.model.Special;
import com.bccv.meitu.network.HttpCallback;
import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.ui.adapter.HomeListAdapter;
import com.bccv.meitu.utils.Logger;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.huewu.pla.lib.internal.PLA_AdapterView.OnItemClickListener;
import com.me.maxwin.view.XListView;
import com.me.maxwin.view.XListView.IXListViewListener;

public class SearchResultActivity extends BaseActivity implements IXListViewListener {

	private static final String TAG = "SearchResultActivity"; 
	
	private View backBtn;
	private TextView title;
	private XListView listView;
	private View noresultView;
	
	private HomeListAdapter adapter;
	
	private int tagID = -1;
	private String tagName;
	
	private int currentPage = 0;
	private int totalPage = 0;
	
	private List<Special> dataList;
	
	private View waitting_layout;
	
	//TODO 数据集合
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_result_activity_view);
		iniView();
		iniData();
	}
	
	private void iniView(){
		backBtn = findViewById(R.id.left_buton);
		title = (TextView) findViewById(R.id.title_text);
		listView = (XListView) findViewById(R.id.search_activity_xlv);
		noresultView = findViewById(R.id.ll_no_result);
		waitting_layout = findViewById(R.id.waitting_layout);
		
		backBtn.setOnClickListener(this);
		
		dataList = new ArrayList<Special>();
		adapter = new HomeListAdapter(getApplicationContext(), dataList);
		listView.setAdapter(adapter);
		listView.setPullRefreshEnable(true);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		listView.hideFooter();
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(PLA_AdapterView<?> parent, View view,
					int position, long id) {

				//TODO 跳转到专辑详情页
				Intent intent = new Intent(SearchResultActivity.this, AlbumActivity.class);
				intent.putExtra("special_id", String.valueOf(dataList.get(position-1).getSpecial_id()));
				startActivity(intent);
				
			}
		});
		
	}
	
	private void iniData(){
		//TODO 获取数据
		getIntentData();
		refreshData();
	}

	private void getIntentData(){
		Intent intent = getIntent();
		tagID = intent.getIntExtra("tag_id",-1);
		tagName = intent.getStringExtra("tag_name");
		title.setText(tagName);
	}
	
	/**
	 * 刷新数据
	 */
	public void refreshData(){
		if(tagID==-1){
			noresultView.setVisibility(View.VISIBLE);
			return;
		}
		noresultView.setVisibility(View.GONE);
		if(dataList.size()==0){
			//TODO 显示loading
			showWaiting(true);
		}
		
		getNetData(1,true);
	}
	
	private void showWaiting(boolean show){
		if(show){
			waitting_layout.setVisibility(View.VISIBLE);
		}else{
			waitting_layout.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 获取指定页的数据
	 * 
	 * @param page  页码
	 */
	private void getNetData(int page,final boolean isRefresh){
		//TODO 通过tag 和  页码  请求服务 
		NetWorkAPI.getTaginfo(mContext, tagID, page, new HttpCallback() {
			
			@Override
			public void onResult(NetResBean response) {
				
				Logger.v(TAG, "getNetData onResult", " response.success : " + response.success);
				
				if(response.success&&response instanceof GetTaginfoResBean){
					
					GetTaginfoResBean data = (GetTaginfoResBean) response;
					Logger.v(TAG, "getNetData onResult", " data : " + data);
					currentPage = data.getPage();
					totalPage = data.getPage_total();
					List<Special> list = data.getList();
					if(list!=null){
						if(isRefresh){
							dataList.clear();
						}
						dataList.addAll(list);
						adapter.notifyDataSetChanged();
					}
				}else{
					
					if(dataList.size()==0){
						noresultView.setVisibility(View.VISIBLE);
					}
					
				}
				listView.stopRefresh();
				listView.stopLoadMore();
				if(totalPage>currentPage){
					listView.showFooter();
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
	
	@Override
	public void onRefresh() {
		// TODO 刷新数据
		refreshData();
		
	}

	@Override
	public void onLoadMore() {
		// TODO 加载更多
		listView.showFooter();
		if(currentPage>=totalPage){
			listView.stopLoadMore();
			listView.hideFooter();
			return;
		}
		getNetData(++currentPage, false);
	}
	
}
