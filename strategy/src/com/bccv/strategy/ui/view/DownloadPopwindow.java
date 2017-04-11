package com.bccv.strategy.ui.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.bccv.strategy.api.DownLoadAPI;
import com.bccv.strategy.model.DownloadBean;
import com.bccv.strategy.R;

public class DownloadPopwindow implements OnClickListener, OnItemClickListener {

	private Activity activity;
	private PopupWindow popuWindow;
	private GridView download_gv;
	private View cancel_btn;
	private View shadowView;
	private List<DownloadBean> downloads;
	private MyAdapter mAdapter;
	private int versionCode;
	private String pkgName;
	private String appName;
	
	public DownloadPopwindow(Activity activity,View shadowView) {
		this.activity = activity;
		this.shadowView = shadowView;
		initView();
	}

	private void initView() {
		Context context = activity.getApplicationContext();
		View shareView = View.inflate(context, R.layout.downloadpop, null);
		download_gv = (GridView) shareView.findViewById(R.id.download_ll_gv);
		cancel_btn = shareView.findViewById(R.id.cancel_btn);
		
		mAdapter = new MyAdapter();
		download_gv.setAdapter(mAdapter);
		download_gv.setOnItemClickListener(this);
		cancel_btn.setOnClickListener(this);
		
		popuWindow = new PopupWindow(shareView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		popuWindow.setAnimationStyle(R.style.anim_share_select);
		popuWindow.setOutsideTouchable(true);
		popuWindow.setFocusable(true);
		ColorDrawable cd = new ColorDrawable(-0000);
		popuWindow.setBackgroundDrawable(cd);

		popuWindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				if(shadowView!=null){
					shadowView.setVisibility(View.GONE);
				}
			}
		});
		
	}

	/**
	 * 弹出popwindow
	 * 
	 * @param view
	 */
	public void show(View view, List<DownloadBean> downloadurls,int versionCode,String pkgName,String appName) {
		this.appName = appName;
		this.pkgName = pkgName;
		this.versionCode = versionCode;
		this.downloads = downloadurls;
		popuWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
		if(shadowView!=null){
			shadowView.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 隐藏popwindow
	 */
	public void dismiss() {
		if (popuWindow != null && popuWindow.isShowing()) {
			popuWindow.dismiss();
		}
	}

	public boolean isShowing() {
		if (popuWindow != null) {
			
			return popuWindow.isShowing();
		}
		return false;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.cancel_btn:
			dismiss();
			break;

		default:
			break;
		}

	}
	
	public void clearViews(){
		shadowView = null;
	}

	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return downloads==null?0:downloads.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return downloads.get(position);
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
			if(convertView==null){
				holder = new Holder();
				convertView = View.inflate(activity, R.layout.download_gv_item, null);
				holder.icon = (ImageView) convertView.findViewById(R.id.download_iv);
				holder.name = (TextView) convertView.findViewById(R.id.download_tv);
			}else{
				holder = (Holder) convertView.getTag();
			}
			//TODO 设置数据
			DownloadBean downloadBean = downloads.get(position);
			String type = downloadBean.getType();
			if("QQ".equalsIgnoreCase(type)){
				holder.icon.setBackgroundResource(R.drawable.yingyongbao_item_selector);
				holder.name.setText("应用宝");
			}else if("360".equalsIgnoreCase(type)){
				holder.icon.setBackgroundResource(R.drawable._360_item_selector);
				holder.name.setText("360手机助手");
			}else if("googleplay".equalsIgnoreCase(type)){
				holder.icon.setBackgroundResource(R.drawable.google_play_item_selector);
				holder.name.setText("Google play");
			}else if("local".equalsIgnoreCase(type)){
				holder.icon.setBackgroundResource(R.drawable.local_download_item_selector);
				holder.name.setText("本地下载");
			}
		
			convertView.setTag(holder);
			return convertView;
		}
		
		private class Holder{
			ImageView icon;
			TextView name;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
			DownloadBean downloadBean = downloads.get(position);
			DownLoadAPI.downLoadApk(downloadBean.getDownload(), appName, pkgName, versionCode, true, true);
			dismiss();
	}
	
}
