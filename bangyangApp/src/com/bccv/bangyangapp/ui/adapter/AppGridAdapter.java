package com.bccv.bangyangapp.ui.adapter;

import java.util.ArrayList;

import com.bccv.bangyangapp.R;
import com.bccv.bangyangapp.model.InstallAppInfo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppGridAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<InstallAppInfo> data;
	
	public AppGridAdapter(Context context,ArrayList<InstallAppInfo> data){
		this.mContext = context;
		this.data = data;
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
		holder.appName.setText(data.get(position).getAppName());
		holder.appIcon.setImageDrawable(data.get(position).getIcon());
		convertView.setTag(holder);
		return convertView;
	}

	
	private class Holder{
		public ImageView appIcon;
		public TextView appName;
	}
	
}
