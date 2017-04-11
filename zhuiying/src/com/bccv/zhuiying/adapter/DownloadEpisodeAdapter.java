package com.bccv.zhuiying.adapter;

import java.util.List;

import com.bccv.zhuiying.R;
import com.bccv.zhuiying.model.MovieEpisode;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DownloadEpisodeAdapter extends BaseAdapter {
	Context context;
	List<MovieEpisode> list;
	int gridViewNum = 5;

	public DownloadEpisodeAdapter(Context context, List<MovieEpisode> list) {
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

	public void setGridViewNum(int gridViewNum){
		this.gridViewNum = gridViewNum;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(context, R.layout.griditem_downloadselect, null);
			viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (gridViewNum == 5) {
			viewHolder.textView.setText(list.get(position).getId() + "");
		}else if (gridViewNum == 2) {
			viewHolder.textView.setText(list.get(position).getPeriods() + "");
		}else{
			viewHolder.textView.setText(list.get(position).getDes() + "");
		}
		
		if (list.get(position).isSelect()) {
			viewHolder.imageView.setVisibility(View.VISIBLE);
		}else {
			viewHolder.imageView.setVisibility(View.GONE);
		}
		
		return convertView;
	}

	class ViewHolder{
		TextView textView;
		ImageView imageView;
	}
}
