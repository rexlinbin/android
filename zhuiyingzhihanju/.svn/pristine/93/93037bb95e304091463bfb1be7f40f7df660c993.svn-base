package com.bccv.zhuiyingzhihanju.adapter;

import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.fragment.HJFragment;
import com.bccv.zhuiyingzhihanju.model.Movie;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.tools.GlobalParams;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SpecialAdapter extends BaseAdapter {
	private List<Movie> data;
	private Context context;
	HJFragment fragment;
	

	public SpecialAdapter(List<Movie> data, Context context, HJFragment fragment) {
		super();
		this.data = data;
		this.context = context;
		this.fragment = fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (data.size() > 0) {
			if (data.size() % 2 != 0) {
				return data.size() / 2 + 1;
			} else {
				return data.size() / 2;
			}

		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	private boolean isTV = false;

	public void isTV(boolean isTV){
		this.isTV = isTV;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder viewHolder = null;
		int rowNum = 2;

		ImageView image;

		TextView text;
		TextView numT;
		TextView update;
		if (convertView == null) {

			viewHolder = new ViewHolder();

			convertView = LayoutInflater.from(context).inflate(R.layout.listitem_special, null);

			viewHolder.first = convertView.findViewById(R.id.select_first_layout);
			viewHolder.second = convertView.findViewById(R.id.select_second_layout);
			convertView.setTag(viewHolder);

			convertView.setTag(viewHolder);

		} else {

			viewHolder = (ViewHolder) convertView.getTag();

		}

		for (int i = 0; i < rowNum; i++) {
			final int num = position * rowNum + i;
			View view = null;
			if (i == 0) {
				view = viewHolder.first;
			} else if (i == 1) {
				view = viewHolder.second;
			}
			if (data.size() > num) {
				view.setVisibility(View.VISIBLE);
				view.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						fragment.goInfo(num);
					}
				});
			}else{
				view.setVisibility(View.GONE);
				continue;
			}
			
			
			
			image = (ImageView) view.findViewById(R.id.item_special_image);
			text = (TextView) view.findViewById(R.id.item_special_titletext);

			numT = (TextView) view.findViewById(R.id.item_special_num);
			update = (TextView) view.findViewById(R.id.item_special_update);

			Movie item = data.get(num);
//			if (isTV) {
//				update.setText(item.getEpisode());
//			}
			
			update.setText(item.getEpisode());
			text.setText(item.getTitle());
			
			numT.setText(item.getShort_summary());

			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage(item.getImages(), image, GlobalParams.movieOptions);

		}

		return convertView;
	}

	class ViewHolder {

		View first;
		View second;

	}

}
