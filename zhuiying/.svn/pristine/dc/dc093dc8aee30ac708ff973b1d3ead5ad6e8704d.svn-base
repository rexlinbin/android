package com.bccv.zhuiying.adapter;

import java.util.List;

import com.bccv.zhuiying.R;
import com.bccv.zhuiying.model.Movie;
import com.bccv.zhuiying.model.SearchInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.tools.GlobalParams;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchAdapter extends BaseAdapter {
	private Context context;
	private List<SearchInfo> list;

	public SearchAdapter(Context context, List<SearchInfo> list) {
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
			convertView = View.inflate(context, R.layout.griditem_movielist, null);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
			viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
			viewHolder.scoreTextView = (TextView) convertView.findViewById(R.id.score_textView);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		SearchInfo item=list.get(position);
		viewHolder.textView.setText(item.getTitle());
		
		String images = (String) viewHolder.imageView.getTag();
		if (images != null && images.equals(item.getImage())) {
//			viewHolder.imageView.setTag(item.getImages());
//			ImageLoader imageLoader = ImageLoader.getInstance();
//			imageLoader.displayImage(item.getImages(), viewHolder.imageView, GlobalParams.movieOptions);
		}else{
			viewHolder.imageView.setTag(item.getImage());
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage(item.getImage(), viewHolder.imageView, GlobalParams.movieOptions);
		}
		
		
		viewHolder.scoreTextView.setText(item.getRating());
		
		
		return convertView;
	}
	
	class ViewHolder {
		ImageView imageView;
		TextView textView;
		TextView scoreTextView;
	}

}
