package com.bccv.meitu.view;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bccv.meitu.ApplicationManager;
import com.bccv.meitu.R;
import com.bccv.meitu.api.NetWorkAPI;
import com.bccv.meitu.model.GetTaglistResBean;
import com.bccv.meitu.model.Tag;
import com.bccv.meitu.network.HttpCallback;
import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.ui.activity.SearchResultActivity;
import com.bccv.meitu.utils.Logger;
import com.bccv.meitu.utils.SystemUtil;

public class MenuPopwindow implements OnClickListener {

	private static final String TAG = "MenuPopwindow";
	
	private PopupWindow mPopupWindow;
	private View popupWindowView;
	
	private View left_buton;
	private GridView menu_gv;
	private View ll_no_result;
	
	private List<Tag> tags;
	private MyAdapter adapter;
	
	private Activity activity;
	
	@SuppressWarnings("deprecation")
	public MenuPopwindow (Activity activity){
		
		this.activity = activity;
		
		
		View v = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
		Rect rect = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		
		LayoutInflater layoutInflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popupWindowView = layoutInflater.inflate(R.layout.home_menu_view, null);
		mPopupWindow = new PopupWindow(popupWindowView, v.getWidth(), v.getHeight());
		mPopupWindow.setAnimationStyle(R.style.anim_share_select);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(true);
		initView();
		initData();
	}
	
	private void initView(){
		ll_no_result = popupWindowView.findViewById(R.id.ll_no_result);
		left_buton = popupWindowView.findViewById(R.id.left_buton);
		menu_gv = (GridView) popupWindowView.findViewById(R.id.menu_gv);
		left_buton.setOnClickListener(this);
		ll_no_result.setOnClickListener(this);
		menu_gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//TODO 跳转到指定页面
					Intent intent = new Intent(activity, SearchResultActivity.class);
					intent.putExtra("tag_id", tags.get(position).getTag_id());
					intent.putExtra("tag_name", tags.get(position).getTag_name());
					activity.startActivity(intent);
//					dismiss();
			}
		});
		
	}
	
	private void initData(){
		
		tags = new ArrayList<Tag>();
		adapter = new MyAdapter();
		menu_gv.setAdapter(adapter);
		
	}
	
	private synchronized void reFreshData(){
		
		//TODO 获取标签
		NetWorkAPI.getTaglist(activity.getApplicationContext(), new HttpCallback() {
			
			@Override
			public void onResult(NetResBean response) {
				
				Logger.v(TAG, "getTaglist  onResult", "response.success : " + response.success);
				
				if(response.success && response instanceof GetTaglistResBean){
					GetTaglistResBean tagListBean = (GetTaglistResBean) response;
					Logger.v(TAG, "getTaglist  onResult", "tagListBean : " + tagListBean);
					tags = tagListBean.getTaglist();
					adapter.notifyDataSetChanged();
				}else{
					if(tags==null && tags.size()==0){
						//TODO 取消loading
						ll_no_result.setVisibility(View.VISIBLE);
					}
				}
			}
			
			@Override
			public void onError(String errorMsg) {
				Logger.v(TAG, "getTaglist  onError", "errorMsg : " + errorMsg);
				//TODO 取消loading
				ll_no_result.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onCancel() {	
			}
		});
		
		adapter.notifyDataSetChanged();
		
	}
	
	/**
	 * 弹出popwindow
	 * 
	 * @param view
	 */
	public void show(View view) {
		if(SystemUtil.isNetOkWithToast(ApplicationManager.getGlobalContext())){
			reFreshData();
		}
		mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
	}
	
	/**
	 * 隐藏popwindow
	 */
	public void dismiss() {
		if (mPopupWindow != null && mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		}
	}
	
	public boolean isShowing() {
		if (mPopupWindow != null) {
			return mPopupWindow.isShowing();
		}
		return false;
	}
	
	
	private class MyAdapter extends BaseAdapter{
		
		LayoutInflater layoutInflater;
		public MyAdapter(){
			layoutInflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public int getCount() {
			return tags==null?0:tags.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			Holder holder;
			if(convertView == null){
				holder = new Holder();
				convertView = layoutInflater.inflate(R.layout.menu_item_view, null);
				holder.text = (TextView) convertView.findViewById(R.id.menu_item_tv);
			}else{
				holder = (Holder) convertView.getTag();
			}
			holder.text.setText(tags.get(position).getTag_name());
			convertView.setTag(holder);
			return convertView;
		}

		private class Holder{
			public TextView text;
		}
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.left_buton:
			dismiss();
			break;
		case R.id.ll_no_result:
			//TODO 刷新数据
			reFreshData();
			break;

		default:
			break;
		}
		
	}
	
}
