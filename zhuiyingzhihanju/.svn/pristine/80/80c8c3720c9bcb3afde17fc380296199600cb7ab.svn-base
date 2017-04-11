package com.bccv.zhuiyingzhihanju.adapter;

import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.activity.CollectActivity;
import com.bccv.zhuiyingzhihanju.activity.Video2DPlayerActivity;
import com.bccv.zhuiyingzhihanju.model.Collect;
import com.bccv.zhuiyingzhihanju.model.Movie;
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

public class MovieLikeAdapter extends BaseAdapter {
	private Activity activity;
	private List<Movie> list;

	public MovieLikeAdapter(Activity activity, List<Movie> list) {
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
			convertView = View.inflate(activity.getApplicationContext(), R.layout.listitem_info_like, null);
			viewHolder.roundedImageView = (RoundedImageView) convertView.findViewById(R.id.roundedImageView);
			viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.title_textView);
			viewHolder.introTextView = (TextView) convertView.findViewById(R.id.intro_textView);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		Movie item=list.get(position);
		viewHolder.titleTextView.setText(item.getTitle());
		
		String images = (String) viewHolder.roundedImageView.getTag();
		if (images != null && images.equals(item.getBimages())) {
			viewHolder.roundedImageView.setTag(item.getBimages());
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage(item.getBimages(), viewHolder.roundedImageView, GlobalParams.movieOptions);
		}else{
			viewHolder.roundedImageView.setTag(item.getBimages());
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage(item.getBimages(), viewHolder.roundedImageView, GlobalParams.movieOptions);
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
