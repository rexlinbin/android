package com.bccv.boxcomic.adapter;

import java.util.List;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.modal.Channel;
import com.bccv.boxcomic.tool.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChannelAdapter extends BaseAdapter {
	private Activity activity;
	private List<Channel> list;
	
	
	public ChannelAdapter(Activity activity, List<Channel> list){
		this.activity = activity;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(activity.getApplicationContext(), R.layout.griditem_channel, null);
			viewHolder.channelTextView = (TextView) convertView.findViewById(R.id.channel_textView);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if (!StringUtils.isEmpty(list.get(position).getCat_title())) {
			viewHolder.channelTextView.setText(list.get(position).getCat_title());
		}
		
		return convertView;
	}

	class ViewHolder{
		TextView channelTextView;
	}
}
