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

public class CollectListAdapter extends BaseAdapter {
	private Activity activity;
	private List<Collect> list;

	public CollectListAdapter(Activity activity, List<Collect> list) {
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
			convertView = View.inflate(activity.getApplicationContext(), R.layout.listitem_collect, null);
			viewHolder.selectImageView = (ImageView) convertView.findViewById(R.id.select_imageView);
			viewHolder.roundedImageView = (RoundedImageView) convertView.findViewById(R.id.roundedImageView);
			viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.title_textView);
			viewHolder.introTextView = (TextView) convertView.findViewById(R.id.intro_textView);
			viewHolder.playTextView = (TextView) convertView.findViewById(R.id.play_textView);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.playTextView.setVisibility(View.VISIBLE);
		Collect item=list.get(position);
		viewHolder.titleTextView.setText(item.getTitle());
		
		viewHolder.selectImageView.setSelected(item.isSelect());
		if (item.isEdit()) {
			viewHolder.selectImageView.setVisibility(View.VISIBLE);
		}else{
			viewHolder.selectImageView.setVisibility(View.GONE);
		}
		
		String images = (String) viewHolder.roundedImageView.getTag();
		if (images != null && images.equals(item.getBimages())) {
//			viewHolder.roundedImageView.setTag(item.getBimages());
//			ImageLoader imageLoader = ImageLoader.getInstance();
//			imageLoader.displayImage(item.getBimages(), viewHolder.roundedImageView, GlobalParams.movieOptions);
		}else{
			viewHolder.roundedImageView.setTag(item.getBimages());
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage(item.getBimages(), viewHolder.roundedImageView, GlobalParams.movieOptions);
		}
		
		viewHolder.introTextView.setText(item.getDes());
		viewHolder.playTextView.setVisibility(View.GONE);
		viewHolder.playTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent aIntent = new Intent(activity, Video2DPlayerActivity.class);

				aIntent.putExtra("movie_id", list.get(position).getId());
				aIntent.putExtra("type_id", list.get(position).getType());
				if (list.get(position).getType().equals("7") || list.get(position).getType().equals("8") ||list.get(position).getType().equals("9") ||list.get(position).getType().equals("10")) {
					aIntent.putExtra("isEpisode", true);
				}else{
					aIntent.putExtra("isEpisode", false);
				}
				aIntent.putExtra("episodes_id", "0");
				activity.startActivity(aIntent);
			}
		});
		
		return convertView;
	}
	
	class ViewHolder {
		RoundedImageView roundedImageView;
		TextView titleTextView;
		TextView introTextView;
		TextView playTextView;
		ImageView selectImageView;
	}

}
