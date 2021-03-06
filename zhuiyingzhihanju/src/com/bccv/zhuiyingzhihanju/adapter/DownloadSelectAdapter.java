package com.bccv.zhuiyingzhihanju.adapter;

import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.model.MovieUrl;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DownloadSelectAdapter extends BaseAdapter {

	private Context context;
	private List<MovieUrl> list;
	
	public DownloadSelectAdapter(Context context, List<MovieUrl> list) {
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
			convertView = View.inflate(context, R.layout.listitem_downloadselect, null);
			viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.textView.setText(list.get(position).getWebsite_name());

		viewHolder.textView.setSelected(list.get(position).isSelect());

		return convertView;
	}

	class ViewHolder {
		TextView textView;
	}

}
