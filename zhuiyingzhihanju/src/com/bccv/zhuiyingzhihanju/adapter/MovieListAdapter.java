package com.bccv.zhuiyingzhihanju.adapter;

import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.model.Movie;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.tools.GlobalParams;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MovieListAdapter extends BaseAdapter {
	private Context context;
	private List<Movie> list;

	public MovieListAdapter(Context context, List<Movie> list) {
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
			viewHolder.contextTe = (TextView) convertView.findViewById(R.id.textView1);
			viewHolder.textMore=(TextView) convertView.findViewById(R.id.fanny_item_moreText);
			viewHolder.moreRe=(RelativeLayout) convertView.findViewById(R.id.found_item_moreRe);

			if(position==4){
				viewHolder.moreRe.setVisibility(View.VISIBLE);
			
			}

			if(position==5){
				viewHolder.moreRe.setVisibility(View.VISIBLE);
				viewHolder.textMore.setText("换一批看看");
			}
		
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Movie item = list.get(position);
		viewHolder.textView.setText(item.getTitle());
		viewHolder.contextTe.setText(item.getShort_summary());

		viewHolder.imageView.setTag(item.getImages());
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(item.getImages(), viewHolder.imageView, GlobalParams.movieOptions);

		viewHolder.scoreTextView.setText(item.getEpisode());

		
		
		
		
		
		
		
		
		return convertView;
	}

	class ViewHolder {
		ImageView imageView;
		TextView textView;
		TextView scoreTextView;
		TextView contextTe;
		TextView textMore;
		RelativeLayout moreRe;
	}

}
