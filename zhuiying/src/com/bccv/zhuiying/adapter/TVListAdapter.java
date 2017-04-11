package com.bccv.zhuiying.adapter;

import java.util.List;

import com.bccv.zhuiying.R;
import com.bccv.zhuiying.model.Movie;
import com.bccv.zhuiying.model.Report;
import com.bccv.zhuiying.model.TV;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.tools.GlobalParams;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TVListAdapter extends BaseAdapter {
	private Context context;
	private List<TV> list;

	public TVListAdapter(Context context, List<TV> list) {
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
			convertView = View.inflate(context, R.layout.listitem_tvlist, null);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
			viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
			viewHolder.liveTextView = (TextView) convertView.findViewById(R.id.live_textView);
			viewHolder.nextTextView = (TextView) convertView.findViewById(R.id.next_textView);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		TV item=list.get(position);
		viewHolder.textView.setText(item.getTitle());
		
		String images = (String) viewHolder.imageView.getTag();
		if (images != null && images.equals(item.getPic())) {
//			viewHolder.imageView.setTag(item.getImages());
//			ImageLoader imageLoader = ImageLoader.getInstance();
//			imageLoader.displayImage(item.getImages(), viewHolder.imageView, GlobalParams.movieOptions);
		}else{
			viewHolder.imageView.setTag(item.getPic());
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage(item.getPic(), viewHolder.imageView, GlobalParams.movieOptions);
		}
		
		List<Report> reports = item.getReport();
		if (reports != null && reports.size() > 0) {
			if (reports.size() < 2) {
				viewHolder.liveTextView.setText(reports.get(0).getTitle());
			}else{
				viewHolder.liveTextView.setText(reports.get(0).getTitle());
				viewHolder.nextTextView.setText(reports.get(1).getTitle());
			}
		}
		
		return convertView;
	}
	
	class ViewHolder {
		ImageView imageView;
		TextView textView;
		TextView liveTextView;
		TextView nextTextView;
	}

}
