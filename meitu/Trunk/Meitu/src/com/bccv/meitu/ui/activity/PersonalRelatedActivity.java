package com.bccv.meitu.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bccv.meitu.R;
import com.bccv.meitu.api.NetWorkAPI;
import com.bccv.meitu.db.StorageModule;
import com.bccv.meitu.model.GetuserlistResBean;
import com.bccv.meitu.model.Special;
import com.bccv.meitu.network.HttpCallback;
import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.sns.UserInfoManager;
import com.bccv.meitu.ui.adapter.PersonalRelatedListAdapter;
import com.bccv.meitu.ui.adapter.PersonalRelatedListAdapter.ViewHolder;
import com.bccv.meitu.utils.Logger;
import com.bccv.meitu.utils.SystemUtil;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.huewu.pla.lib.internal.PLA_AdapterView.OnItemClickListener;
import com.me.maxwin.view.XListView;
import com.me.maxwin.view.XListView.IXListViewListener;

public class PersonalRelatedActivity extends BaseActivity implements IXListViewListener  {
	
	public static final String TYPE_KEY = "type_key";
	public static final int EXCLUSIVE = 0; 	// 专属
	public static final int FAVORITE = 1;	// 喜欢
	public static final int PSERSONAL = 3;	// 我的专辑
	
	public static final int MODE_NORMAL = 0;
	public static final int MODE_EDIT = 1;
	
	private int mode = MODE_NORMAL;

	private int type = -1;
	
	private	View backBtn;
	private	View editBtn;
	private TextView title;
	private TextView right_tv;
	
	private XListView listView;
	private View changeBtns;
	private View select_all;
	private View delete_all;
	
	private View noresultView;
	private View waitting_layout;
	
	private List<Special> dataList = new ArrayList<Special>();
	
	private int currentPage = 0;
	private int totalPage = 0;
	
	private PersonalRelatedListAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_related_view);
		initView();
		initData();
	}
	
	private void initView(){
		backBtn = findViewById(R.id.left_buton);
		editBtn = findViewById(R.id.right_buton);
		editBtn.setVisibility(View.VISIBLE);
		title = (TextView) findViewById(R.id.title_text);
		listView = (XListView) findViewById(R.id.personal_related_xlv);
		noresultView = findViewById(R.id.ll_no_result);
		waitting_layout = findViewById(R.id.waitting_layout);
		right_tv = (TextView) findViewById(R.id.right_tv);
		changeBtns = findViewById(R.id.personal_related_change_btns);
		
		select_all = changeBtns.findViewById(R.id.select_all);
		delete_all = changeBtns.findViewById(R.id.delete_all);
		
		backBtn.setOnClickListener(this);
		editBtn.setOnClickListener(this);
		
		select_all.setOnClickListener(this);
		delete_all.setOnClickListener(this);
		
		// 创建Adapter  设置adapter
		mAdapter = new PersonalRelatedListAdapter(mContext, dataList);
		listView.setAdapter(mAdapter);
		
		listView.setPullRefreshEnable(true);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		listView.hideFooter();
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(PLA_AdapterView<?> parent, View view,
					int position, long id) {

				if(mode == MODE_NORMAL){
					// 跳转到专辑详情页
					Intent intent = new Intent(PersonalRelatedActivity.this, AlbumActivity.class);
					intent.putExtra("special_id", String.valueOf(dataList.get(position-1).getSpecial_id()));
					startActivity(intent);
				}else if(type == FAVORITE){
					
					ViewHolder holder = (ViewHolder) view.getTag();
					Special special = dataList.get(position-1);
					special.setIschecked(!special.isIschecked());
					
					if(special.isIschecked()){
						holder.iv.setVisibility(View.VISIBLE);
					}else{
						holder.iv.setVisibility(View.GONE);
					}
				}
			}
		});
	}
	
	private void initData(){
		getIntentData();
		if(type==FAVORITE){
			title.setText("我的喜欢");
			editBtn.setVisibility(View.VISIBLE);
		}else if(type==EXCLUSIVE){
			title.setText("我的专属");
			editBtn.setVisibility(View.GONE);
		}else if(type==PSERSONAL){
			title.setText("我的专辑");
			editBtn.setVisibility(View.GONE);
		}
		refreshData();
	}

	private void getIntentData(){
		//获取intent信息
		Intent intent = getIntent();
		type = intent.getIntExtra(TYPE_KEY, -1);
	}
	
	/**
	 * 刷新数据
	 */
	public void refreshData(){
		if(type==-1){
			noresultView.setVisibility(View.VISIBLE);
			return;
		}
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
		
		int requestType =2;
		switch (type) {
		case PSERSONAL:
			requestType =7;
			break;
		case EXCLUSIVE:
			requestType =5;
			break;
		case FAVORITE:
			requestType =2;
			break;

		default:
			break;
		}
		
		if(SystemUtil.isNetOkWithToast(mContext)){
			
			if(type == EXCLUSIVE || type == PSERSONAL || (type == FAVORITE && UserInfoManager.isLogin())){
				NetWorkAPI.getuserlist(mContext,requestType, page, new HttpCallback() {
					
					@Override
					public void onResult(NetResBean response) {
						
						if(response.success && response instanceof GetuserlistResBean){
							
							GetuserlistResBean data = (GetuserlistResBean) response;
							Logger.v(TAG, "getNetData onResult", " data : " + data);
							currentPage = data.getPage();
							totalPage = data.getPage_total();
							List<Special> list = data.getList();
							
							if(isRefresh){
								dataList.clear();
								if(list!=null){
									dataList.addAll(list);
								}
								mAdapter = new PersonalRelatedListAdapter(mContext, dataList);
								listView.setAdapter(mAdapter);
							}else{
								if(list!=null){
									dataList.addAll(list);
								}
								mAdapter.notifyDataSetChanged();
							}
						}
						
						if(dataList.size()==0){
							noresultView.setVisibility(View.VISIBLE);
						}
						
						listView.stopRefresh();
						listView.stopLoadMore();
						if(totalPage>currentPage){
							listView.showFooter();
						}else{
							listView.hideFooter();
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
			}else if(type == FAVORITE && !UserInfoManager.isLogin()){
				// 获取本地数据
				ArrayList<String> localLikeList = StorageModule.getInstance().getLocalLikeList();
				if(localLikeList!=null&&localLikeList.size()>0){
					NetWorkAPI.getLocalLikeList(mContext, localLikeList, new HttpCallback() {
						
						@Override
						public void onResult(NetResBean response) {
							if(response.success && response instanceof GetuserlistResBean){
								
								GetuserlistResBean data = (GetuserlistResBean) response;
								Logger.v(TAG, "getNetData onResult", " data : " + data);
								currentPage = data.getPage();
								totalPage = data.getPage_total();
								List<Special> list = data.getList();
								
								dataList.clear();
								if(list!=null){
									dataList.addAll(list);
								}
								mAdapter = new PersonalRelatedListAdapter(mContext, dataList);
								listView.setAdapter(mAdapter);
							}
							
							if(dataList.size()==0){
								noresultView.setVisibility(View.VISIBLE);
							}
							
							listView.stopRefresh();
							listView.stopLoadMore();
							if(totalPage>currentPage){
								listView.showFooter();
							}else{
								listView.hideFooter();
							}
							showWaiting(false);
							
						}
						
						@Override
						public void onError(String errorMsg) {
							Logger.e(TAG, "getNetData  onError", errorMsg+"");
							if(dataList.size()==0){
								noresultView.setVisibility(View.VISIBLE);
							}
							listView.stopRefresh();
							listView.stopLoadMore();
							if(totalPage>currentPage){
								listView.showFooter();
							}else{
								listView.hideFooter();
							}
							showWaiting(false);
						}
						
						@Override
						public void onCancel() {}
					});
				}else{
					dataList.clear();
					mAdapter = new PersonalRelatedListAdapter(mContext, dataList);
					listView.setAdapter(mAdapter);
					listView.stopRefresh();
					listView.stopLoadMore();
					listView.hideFooter();
					noresultView.setVisibility(View.VISIBLE);
					showWaiting(false);
				}
			}
		}else {
			listView.stopRefresh();
			listView.stopLoadMore();
			if(dataList.size()==0){
				noresultView.setVisibility(View.VISIBLE);
			}
			showWaiting(false);
		}
	}
	
	
	private void showWaiting(boolean show){
		if(show){
			waitting_layout.setVisibility(View.VISIBLE);
		}else{
			waitting_layout.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_buton:
			finish();
			break;
		case R.id.right_buton:
			// 转换模式
			if(mode == MODE_NORMAL){
				mode = MODE_EDIT;
				right_tv.setText("取消");
				// 显示出全选 删除pop
				changeBtns.setVisibility(View.VISIBLE);
				
			}else{
				mode = MODE_NORMAL;
				right_tv.setText("编辑");
				// 隐藏出全选 删除pop
				changeBtns.setVisibility(View.GONE);
				changeCheckedstate(false);
				mAdapter.notifyDataSetChanged();
			}
			break;
		case R.id.select_all:
			// 全选
			changeCheckedstate(true);
			mAdapter.notifyDataSetChanged();
			break;
		case R.id.delete_all:
			// 删除
			deleteSelected();
			break;	
		
		default:
			break;
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
		listView.showFooter();
		if(currentPage>=totalPage){
			listView.stopLoadMore();
			listView.hideFooter();
			return;
		}
		getNetData(++currentPage, false);
		
	}
	
	/**
	 * 更改全部状态
	 * @param isAllChecked
	 */
	private void changeCheckedstate(boolean isAllChecked){
		for (int i = 0; i < dataList.size(); i++) {
			dataList.get(i).setIschecked(isAllChecked);
		}
	}
	
	/**
	 * 删除选中
	 */
	private void deleteSelected(){
		
		List<Special> waitDeleteList = new ArrayList<Special>();
		
		for (int i = 0; i < dataList.size(); i++) {
			if(dataList.get(i).isIschecked()){
				waitDeleteList.add(dataList.get(i));
			}
		}
		
		if(waitDeleteList.size()>0){
			
			if(UserInfoManager.isLogin()){//如果用户登录 操作网络数据
				NetWorkAPI.unzan(mContext, waitDeleteList, new HttpCallback() {
					
					@Override
					public void onResult(NetResBean response) {
						if(response.success){
							showWaiting(true);
							refreshData();
							showShortToast("删除成功，重新获取数据");
						}else{
							showShortToast("删除失败");
						}
						showWaiting(false);
					}
					
					@Override
					public void onError(String errorMsg) {
						showShortToast("删除失败");
						showWaiting(false);
					}
					
					@Override
					public void onCancel() {}
				});
			}else{//如果没有登录 操作本地数据
				showWaiting(true);
				ArrayList<String> albumIDs = new ArrayList<String>();
				for (Special special : waitDeleteList) {
					albumIDs.add(String.valueOf(special.getSpecial_id()));
				}
				StorageModule.getInstance().removeLocalLike(albumIDs);
				
				refreshData();
				showShortToast("删除成功，重新获取数据");
				showWaiting(false);
			}
			
		}
		
	}
	
	
}
