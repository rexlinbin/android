package com.bccv.zhuiying.adapter;

import java.util.List;

import com.bccv.zhuiying.R;
import com.bccv.zhuiying.model.MovieUrl;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SourceAdapter extends BaseAdapter {
	Context context;
	List<MovieUrl> list;

	public SourceAdapter(Context context, List<MovieUrl> list) {
		// TODO Auto-generated constructor stub
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
			convertView = View.inflate(context, R.layout.listitem_source, null);
			viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.textView.setText(list.get(position).getSource_name());
		viewHolder.textView.setSelected(list.get(position).isSelect());
		if (list.get(position).isSelect()) {
			viewHolder.imageView.setVisibility(View.VISIBLE);
		}else{
			viewHolder.imageView.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

	class ViewHolder{
		TextView textView;
		ImageView imageView;
	}
}
