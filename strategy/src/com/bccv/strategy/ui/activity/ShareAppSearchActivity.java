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
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.bccv.strategy.api.NetWorkAPI;
import com.bccv.strategy.common.GlobalConstants;
import com.bccv.strategy.model.InstallAppInfo;
import com.bccv.strategy.model.Search4ShareListResBean;
import com.bccv.strategy.network.HttpCallback;
import com.bccv.strategy.network.NetResBean;
import com.bccv.strategy.ui.view.BackGroundView;
import com.bccv.strategy.utils.ImageLoaderUtil;
import com.bccv.strategy.utils.L;
import com.bccv.strategy.utils.SystemUtil;
import com.bccv.strategy.R;

public class ShareAppSearchActivity extends BaseActivity {

	private static final String TAG = "ShareAppSearchActivity";
	
	public static final int SHOW_LOCAL_RESULT = 1;
	public static final int SHOW_NET_RESULT = 2;
	
	private BackGroundView background_view;
	private View iv_common_left;
	private EditText search_edit_et;
	private View search_edit_clear;
	
	private View errorView,loadingView;
	private GridView search_gridview;
	
	private ArrayList<InstallAppInfo> searchResult = new ArrayList<InstallAppInfo>();
	private AppGridAdapter resultAdapter;
	
	private int[] background;
	
	private Handler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shareapp_search);
		Intent intent = getIntent();
		background = intent.getIntArrayExtra(BackGroundView.BACKGROUND_COLOR);
		initHandler();
		initView();
		initData();
		
	}
	
	@SuppressLint("HandlerLeak")
	private void initHandler(){
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				
				switch (msg.what) {
				case SHOW_LOCAL_RESULT:
					resultAdapter.notifyDataSetChanged();
					loadingView.setVisibility(View.GONE);
					
					break;
				case SHOW_NET_RESULT:
					resultAdapter.notifyDataSetChanged();
					loadingView.setVisibility(View.GONE);
					
					break;

				default:
					break;
				}
				
			}
			
		};
	}
	
	private void initView(){
		background_view = (BackGroundView) findViewById(R.id.background_view);
		iv_common_left = findViewById(R.id.iv_common_left);
		search_edit_et = (EditText) findViewById(R.id.search_edit_et);
		search_edit_clear = findViewById(R.id.search_edit_clear);
		search_gridview = (GridView) findViewById(R.id.search_gridview);
		
		loadingView = findViewById(R.id.search_loading);
		errorView = findViewById(R.id.search_error);
		
		search_gridview.setOverScrollMode(android.view.View.OVER_SCROLL_NEVER);
		resultAdapter = new AppGridAdapter();
		search_gridview.setAdapter(resultAdapter);
		
		background_view.setGradient(background[0], background[1]);
		
		iv_common_left.setOnClickListener(this);
		search_edit_clear.setOnClickListener(this);
		
		search_edit_et.setOnEditorActionListener(onEditorActionListener);
		
		search_gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					//TODO 点击gridView Item
				
                Intent data = new Intent();
                data.putExtra(AppReleaseActivity.RESULT_STRING, searchResult.get(position));
                setResult(GlobalConstants.SEARCH4RESULT, data); //其中resultCode为响应码 int数值
                finish(); 
				
			}
			
		});
		
	}
	
	
	private void initData(){
		
		new Thread(){
			public void run() {
				//TODO 请求数据
				ArrayList<InstallAppInfo> installAppInfos = SystemUtil.getInstallAppInfos(mContext);
				searchResult.clear();
				searchResult.addAll(installAppInfos);
				mHandler.sendEmptyMessage(SHOW_LOCAL_RESULT);
			};
		}.start();
	}
	

	private void doSearch(String key){
		if (SystemUtil.isNetOkWithToast(this.getApplicationContext())) {
		
			String searchText = search_edit_et.getText().toString().trim();
			
			if(TextUtils.isEmpty(searchText)){
				showShortToast("请输入应用名称");
				return;
			}
			
			loadingView.setVisibility(View.VISIBLE);
			/* 隐藏软键盘 */
			hideSoftInput();
			NetWorkAPI.search4share(mContext, key, new HttpCallback() {
				@Override
				public void onResult(NetResBean response) {
					// TODO Auto-generated method stub
					L.i(TAG, "onResult", response.toString());
					if (response instanceof Search4ShareListResBean) {
						Search4ShareListResBean data = (Search4ShareListResBean) response;
						ArrayList<InstallAppInfo> appInfoItemBeans = data.getAppInfoItemBeans();
						if (appInfoItemBeans != null && appInfoItemBeans.size() > 0) {
							searchResult.clear();
							searchResult.addAll(appInfoItemBeans);
							resultAdapter.notifyDataSetChanged();
						} else {
							showShortToast("暂无该应用信息");
						}
						loadingView.setVisibility(View.GONE);
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
				public void onCancel() {}
			});
		
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
	
	public class AppGridAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return searchResult==null?0:searchResult.size();
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
			if(convertView == null){
				convertView = View.inflate(mContext, R.layout.app_item, null);
				holder = new Holder();
				holder.appIcon = (ImageView) convertView.findViewById(R.id.app_icon);
				holder.appName = (TextView) convertView.findViewById(R.id.app_name);
			}else{
				holder = (Holder) convertView.getTag();
			}
			//TODO 设置数据
			holder.appName.setText(searchResult.get(position).getAppName());
			
			if(searchResult.get(position).getIcon()!=null){
				holder.appIcon.setImageDrawable(searchResult.get(position).getIcon());
			}else{
				//TODO 添加网络图片
				ImageLoaderUtil.getInstance(mContext).displayImage(searchResult.get(position).getIconUrl(), holder.appIcon, ImageLoaderUtil.getAppIconImageOptions());
			}
			
			convertView.setTag(holder);
			return convertView;
		}

		
		private class Holder{
			public ImageView appIcon;
			public TextView appName;
		}
		
	}
	
	
	
	private OnEditorActionListener onEditorActionListener = new OnEditorActionListener() {  
        
		   @Override  
           public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {  
			   
               if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE){  
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
