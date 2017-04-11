package com.bccv.bangyangapp.ui.adapter;

import java.util.ArrayList;

import com.bccv.bangyangapp.R;
import com.bccv.bangyangapp.model.AppInfoItemBean;
import com.bccv.bangyangapp.utils.ImageLoaderUtil;
import com.bccv.bangyangapp.utils.StringUtil;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonalZoneAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<AppInfoItemBean> sharedApps;

	public PersonalZoneAdapter(Context context,ArrayList<String> data){
		this.mContext = context;
	}
	
	@Override
	public int getCount() {
		return sharedApps==null?0:sharedApps.size();
	}

	@Override
	public Object getItem(int position) {
		return sharedApps == null ? null : sharedApps.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if(convertView == null){
			convertView = View.inflate(mContext, R.layout.common_app_list_item, null);
			holder = new Holder();
			holder.handpick = (ImageView) convertView.findViewById(R.id.common_app_item_handpick);
			holder.usericon = (ImageView) convertView.findViewById(R.id.common_app_item_usericon);
			holder.username = (TextView) convertView.findViewById(R.id.common_app_item_username);
			holder.time = (TextView) convertView.findViewById(R.id.common_app_item_time);
			holder.app_poster = (ImageView) convertView.findViewById(R.id.common_app_item_app_poster);
			holder.app_icon = (ImageView) convertView.findViewById(R.id.common_app_item_app_icon);
			holder.appname = (TextView) convertView.findViewById(R.id.common_app_item_appname);
			holder.appintro = (TextView) convertView.findViewById(R.id.common_app_item_appintro);
			holder.appcolumn = (TextView) convertView.findViewById(R.id.common_app_item_appcolumn);
			holder.appcharge = (TextView) convertView.findViewById(R.id.common_app_item_appcharge);
			holder.app_introduce = (TextView) convertView.findViewById(R.id.common_app_item_app_introduce);
			holder.sharetype_tv = (TextView) convertView.findViewById(R.id.sharetype_tv);
			holder.carecount_tv = (TextView) convertView.findViewById(R.id.carecount_tv);
			holder.commentcount_tv = (TextView) convertView.findViewById(R.id.commentcount_tv);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		
		if (sharedApps != null && sharedApps.size() > 0) {
			AppInfoItemBean bean = sharedApps.get(position);
			ImageLoaderUtil.getInstance(mContext).displayImage(bean.getCreator_icon(), holder.usericon
					,ImageLoaderUtil.getUserIconImageOptions());
			ImageLoaderUtil.getInstance(mContext).displayImage(bean.getIcon(), holder.app_icon);
			if (bean.getChoice() == 1) {
				holder.handpick.setVisibility(View.VISIBLE);
			}else
				holder.handpick.setVisibility(View.INVISIBLE);
			
			holder.username.setText(bean.getCreator_name());
			holder.time.setText(StringUtil.formatLongDate(bean.getTimes(), "yyyy-mm-dd"));
			holder.appname.setText(bean.getTitle());
			holder.appintro.setText(bean.getFtitle());
			holder.appcolumn.setText(bean.getType_name());
			holder.appcharge.setText((Integer.valueOf(bean.getPrice()) == 0 ?"免费":"付费"));
			holder.app_introduce.setText(bean.getIntroduce());
			holder.sharetype_tv.setText(bean.getIs_first() == 1 ? "首发":String.valueOf(bean.getIs_first()));
			holder.carecount_tv.setText(String.valueOf(bean.getLike_num()));
			holder.commentcount_tv.setText(String.valueOf(bean.getComment_num()));
			ImageLoaderUtil.getInstance(mContext).displayImage(bean.getPic(), holder.app_poster);
		}
		return convertView;
	}

	
	private class Holder{
		ImageView handpick;
		ImageView usericon;
		TextView username;
		TextView time;
		ImageView app_poster;
		ImageView app_icon;
		TextView appname;
		TextView appintro;//app副标题
		TextView appcolumn;//app分类
		TextView appcharge;//app是否收费
		TextView app_introduce;//app介绍
		TextView sharetype_tv;//app是否是首发
		TextView carecount_tv;//app关注数量
		TextView commentcount_tv;//app评论数量
	}

	public void setList(ArrayList<AppInfoItemBean> sharedApps) {
		this.sharedApps = sharedApps;
	}
}