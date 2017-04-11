package com.bccv.zhuiying.adapter;

import java.util.List;

import com.bccv.zhuiying.R;
import com.bccv.zhuiying.model.HotSearch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class HotSearchAdapter extends BaseAdapter {

	private Context context;
	private List<HotSearch> list;
	
	public HotSearchAdapter(Context context, List<HotSearch> list){
		this.context = context;
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
		return null;
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
			convertView = View.inflate(context, R.layout.listitem_hotsearch, null);
			viewHolder.numTextView = (TextView) convertView.findViewById(R.id.num_textView);
			viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.name_textView);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if (position == 0) {
			viewHolder.numTextView.setBackgroundColor(0xffff5b43);
		}else if (position == 1) {
			viewHolder.numTextView.setBackgroundColor(0xffff8a43);
		}else if (position == 2) {
			viewHolder.numTextView.setBackgroundColor(0xffffcc43);
		}else {
			viewHolder.numTextView.setBackgroundColor(0x20ffffff);
		}
		viewHolder.numTextView.setText(position + 1 + "");
		
		viewHolder.nameTextView.setText(list.get(position).getName());
		return convertView;
	}

	class ViewHolder{
		TextView numTextView;
		TextView nameTextView;
	}
}
