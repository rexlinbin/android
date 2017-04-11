package com.bccv.zhuiyingzhihanju.adapter;

import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.model.Movie;
import com.bccv.zhuiyingzhihanju.model.SearchInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.tools.GlobalParams;
import com.utils.views.RoundedImageView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StarWorksAdapter extends BaseAdapter {
	private Activity activity;
	private List<SearchInfo> list;

	public StarWorksAdapter(Activity activity, List<SearchInfo> list) {
		// TODO Auto-generated constructor stub
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
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(activity.getApplicationContext(), R.layout.listitem_star_works, null);
			viewHolder.roundedImageView = (RoundedImageView) convertView.findViewById(R.id.roundedImageView);
			viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.title_textView);
			viewHolder.introTextView = (TextView) convertView.findViewById(R.id.intro_textView);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		SearchInfo item=list.get(position);
		viewHolder.titleTextView.setText(item.getTitle());
		
		String images = (String) viewHolder.roundedImageView.getTag();
		if (images != null && images.equals(item.getImage())) {
			viewHolder.roundedImageView.setTag(item.getImage());
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage(item.getImage(), viewHolder.roundedImageView, GlobalParams.movieOptions);
		}else{
			viewHolder.roundedImageView.setTag(item.getImage());
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage(item.getImage(), viewHolder.roundedImageView, GlobalParams.movieOptions);
		}
		
		
		viewHolder.introTextView.setText(item.getDes());
		
		
		return convertView;
	}
	
	class ViewHolder {
		RoundedImageView roundedImageView;
		TextView titleTextView;
		TextView introTextView;
	}

}
