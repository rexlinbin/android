package com.bccv.strategy.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bccv.strategy.model.AppInfoItemBean;
import com.bccv.strategy.utils.ImageLoaderUtil;
import com.bccv.strategy.utils.StringUtil;
import com.bccv.strategy.R;

public class CollectionListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<AppInfoItemBean> data;
	private OnBtnClickListener onBtnClickListener;
	
	public CollectionListAdapter(Context context,ArrayList<AppInfoItemBean> data,OnBtnClickListener onBtnClickListener){
		this.mContext = context;
		this.data = data;
		this.onBtnClickListener = onBtnClickListener;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data==null?0:data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder;
		if(convertView == null){
			convertView = View.inflate(mContext, R.layout.homepage_item_vpager, null);
			holder = new Holder();
			
//			holder.handpick = (ImageView) convertView.findViewById(R.id.common_app_item_handpick);
			holder.usericon = (ImageView) convertView.findViewById(R.id.homepage_item_usericon);
			holder.username = (TextView) convertView.findViewById(R.id.homepage_item_username);
//			holder.time = (TextView) convertView.findViewById(R.id.common_app_item_time);
			holder.app_poster = (ImageView) convertView.findViewById(R.id.homepage_item_app_poster);
//			holder.app_icon = (ImageView) convertView.findViewById(R.id.common_app_item_app_icon);
			holder.appname = (TextView) convertView.findViewById(R.id.homepage_item_appname);
			holder.appintro = (TextView) convertView.findViewById(R.id.homepage_item_appname);
//			holder.appcolumn = (TextView) convertView.findViewById(R.id.common_app_item_appcolumn);
//			holder.appcharge = (TextView) convertView.findViewById(R.id.common_app_item_appcharge);
			holder.app_introduce = (TextView) convertView.findViewById(R.id.homepage_item_app_introduce);
//			holder.sharetype_tv = (TextView) convertView.findViewById(R.id.sharetype_tv);
//			holder.carecount_tv = (TextView) convertView.findViewById(R.id.carecount_tv);
//			holder.commentcount_tv = (TextView) convertView.findViewById(R.id.commentcount_tv);
			holder.delete = (RelativeLayout)convertView.findViewById(R.id.rl_delete);
		}else{
			holder = (Holder) convertView.getTag();
		}
		//TODO 设置数据
		if (dFlag) {
			holder.delete.setVisibility(View.VISIBLE);
		}else {
			holder.delete.setVisibility(View.GONE);
		}
		
		holder.delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				data.remove(position);
				notifyDataSetChanged();
			}
		});
		
		final AppInfoItemBean appInfoItemBean = data.get(position);

//		if (appInfoItemBean.getChoice() == 1) {
//			holder.handpick.setVisibility(View.VISIBLE);
//		} else {
//			holder.handpick.setVisibility(View.GONE);
//		}
//
		ImageLoaderUtil.getInstance(mContext).displayImage(
				appInfoItemBean.getCreator_icon(), holder.usericon,
				ImageLoaderUtil.getUserIconImageOptions());
		
		ImageLoaderUtil.getInstance(mContext).displayImage(
				appInfoItemBean.getNews_titlepic(), holder.app_poster,
				ImageLoaderUtil.getAppHomeShowImageOptions());
		
//		ImageLoaderUtil.getInstance(mContext).displayImage(
//				appInfoItemBean.getGame_icon(), holder.app_icon,
//				ImageLoaderUtil.getAppIconImageOptions());
		
		holder.username.setText("-"+appInfoItemBean.getCreator_name());
//		holder.time.setText(StringUtil.formatLongDate(appInfoItemBean.getNews_ctime(),"yyyy-MM-dd"));
		holder.appname.setText(appInfoItemBean.getNews_title());
		holder.appintro.setText(appInfoItemBean.getNews_ftitle());
		
//		//TODO 
//		String type_name = appInfoItemBean.getCat_title();
//		if(TextUtils.isEmpty(type_name)){
//			holder.appcolumn.setText("未知");
//		}else{
//			holder.appcolumn.setText(type_name);
//		}
//		String price = appInfoItemBean.getPrice();
//		if(price!=null&&!price.equals("0")){
//			holder.appcharge.setText("收费");
//			holder.appcharge.setBackgroundResource(R.drawable.mark_shoufei);
//		}else{
//			holder.appcharge.setText("免费");
//			holder.appcharge.setBackgroundResource(R.drawable.mark_mianfei);
//			
//		}
//		
		holder.app_introduce.setText(appInfoItemBean.getNews_introduce());
//		holder.sharetype_tv.setText(String.valueOf(appInfoItemBean.getNews_focus()));
//		holder.carecount_tv.setText(String.valueOf(appInfoItemBean.getNews_digg()));
//		holder.commentcount_tv.setText(String.valueOf(appInfoItemBean.getNews_comment()));
		
		
		holder.usericon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(onBtnClickListener!=null){
					onBtnClickListener.onUsericonClick(appInfoItemBean.getCreator_id());
				}
				
			}
		});
		
		
		
		convertView.setTag(holder);
		return convertView;
	}

	public ArrayList<AppInfoItemBean> getData() {
		return data == null ? null : data;
	}
	
	private class Holder{
//		ImageView handpick;
		ImageView usericon;
		TextView username;
//		TextView time;
		ImageView app_poster;
//		ImageView app_icon;
		TextView appname;
		TextView appintro;//app副标题
//		TextView appcolumn;//app分类
//		TextView appcharge;//app是否收费
		TextView app_introduce;//app介绍
//		TextView sharetype_tv;//app是否是首发
//		TextView carecount_tv;//app关注数量
//		TextView commentcount_tv;//app评论数量
		RelativeLayout delete;
	}
	
	public interface OnBtnClickListener{
		//TODO
		void onUsericonClick(String userId);
	}
	private boolean dFlag = false;
	public void setDeleteFlag(boolean flag) {
		this.dFlag = flag;
		notifyDataSetChanged();
	}
	
}
