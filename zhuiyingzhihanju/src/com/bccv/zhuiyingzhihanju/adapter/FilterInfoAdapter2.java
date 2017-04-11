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

public class FilterInfoAdapter2 extends BaseAdapter {
	private Activity activity;
	private List<Movie> list;

	public FilterInfoAdapter2(Activity activity, List<Movie> list) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list.size() > 0) {
			if (list.size() % 2 != 0) {
				return list.size() / 2 + 1;
			}else{
				return list.size() / 2;
			}
			
		} else {
			return 0;
		}

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
			convertView = View.inflate(activity.getApplicationContext(), R.layout.griditem_filterinfo2, null);
			viewHolder.first = convertView.findViewById(R.id.first_layout);
			viewHolder.second = convertView.findViewById(R.id.second_layout);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		for (int i = 0; i < 2; i++) {
			int num = position * 2 + i;
			View view = null;
			if (i == 0) {
				view = viewHolder.first;
			} else if (i == 1) {
				view = viewHolder.second;
			}
			if (list.size() > num) {
				view.setVisibility(View.VISIBLE);
			}else{
				view.setVisibility(View.GONE);
				continue;
			}
			Movie item = list.get(num);
			RoundedImageView roundedImageView = (RoundedImageView) view.findViewById(R.id.roundedImageView);
			TextView titleTextView = (TextView) view.findViewById(R.id.title_textView);
			TextView introTextView = (TextView) view.findViewById(R.id.intro_textView);
			TextView scoreTextView = (TextView) view.findViewById(R.id.score_textView);
			scoreTextView.setText(item.getRating());
			titleTextView.setText(item.getTitle());

			String images = (String) roundedImageView.getTag();
			if (images != null && images.equals(item.getImages())) {
				roundedImageView.setTag(item.getImages());
				ImageLoader imageLoader = ImageLoader.getInstance();
				imageLoader.displayImage(item.getImages(), roundedImageView, GlobalParams.movieOptions);
			} else {
				roundedImageView.setTag(item.getImages());
				ImageLoader imageLoader = ImageLoader.getInstance();
				imageLoader.displayImage(item.getImages(), roundedImageView, GlobalParams.movieOptions);
			}

			introTextView.setText(item.getShort_summary());
		}

		return convertView;
	}

	class ViewHolder {
		View first;
		View second;
	}

}
