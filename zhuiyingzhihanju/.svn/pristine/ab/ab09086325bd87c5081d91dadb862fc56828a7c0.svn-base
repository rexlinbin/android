package com.bccv.zhuiyingzhihanju.adapter;

import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.adapter.MovieLikeAdapter.ViewHolder;
import com.bccv.zhuiyingzhihanju.model.Movie;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.tools.GlobalParams;
import com.utils.views.RoundedImageView;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoInfoLikeAdapter extends BaseAdapter{
	private Activity activity;
	private List<Movie> list;

	public VideoInfoLikeAdapter(Activity activity, List<Movie> list) {
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
			convertView = View.inflate(activity.getApplicationContext(), R.layout.listview_videoinfo, null);
			viewHolder.roundedImageView = (RoundedImageView) convertView.findViewById(R.id.listitem_Vinfo_image);
			viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.listitem_Vinfo_title);
			viewHolder.introTextView = (TextView) convertView.findViewById(R.id.listitem_Vinfo_des);
			viewHolder.numTextView=(TextView) convertView.findViewById(R.id.listitem_Vinfo_num);
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
		viewHolder.numTextView.setText(item.getEpisode());
		
		return convertView;
	}
	
	class ViewHolder {
		RoundedImageView roundedImageView;
		TextView titleTextView;
		TextView introTextView;
		TextView numTextView;
	}

}
