package com.bccv.zhuiyingzhihanju.adapter;

import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.model.Movie;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.tools.GlobalParams;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class FoundAdapter extends BaseAdapter {

	private List<Movie> data;
	private Context context;
private boolean isNews;
	public FoundAdapter(List<Movie> data, Context context) {
		super();
		this.data = data;
		this.context = context;
	}

	public boolean isNews() {
		return isNews;
	}

	public void setNews(boolean isNews) {
		this.isNews = isNews;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
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

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder viewHolder = null;

		if (view == null) {

			viewHolder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.griditem_foundlist, null);

			viewHolder.image = (ImageView) view.findViewById(R.id.found_item_image);
	viewHolder.titleText = (TextView) view.findViewById(R.id.found_item_title);

	viewHolder.time=(TextView) view.findViewById(R.id.found_item_item);
	viewHolder.TotalTime=(TextView) view.findViewById(R.id.found_item_long);
	viewHolder.Dtext=(TextView) view.findViewById(R.id.found_item_context);
	viewHolder.moreRe=(RelativeLayout) view.findViewById(R.id.found_item_moreRe);
	viewHolder.moreText=(TextView) view.findViewById(R.id.found_item_moreText);
	
	
	
	
	
	
	
	
	
			view.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) view.getTag();

		}

		try {
			Movie item = data.get(position);

		viewHolder.titleText.setText(item.getTitle());

//		
		
		viewHolder.Dtext.setText(item.getShort_summary());
	if(isNews){
		viewHolder.time.setVisibility(View.VISIBLE);
		viewHolder.time.setText(item.getCtime());
		
		viewHolder.TotalTime.setText(item.getCtime());
	}else{
		viewHolder.TotalTime.setText(item.getEpisode());
		viewHolder.time.setVisibility(View.GONE);
	}
		
			String images = (String) viewHolder.image.getTag();
			if (images != null && images.equals(item.getImages())) {
				// viewHolder.imageView.setTag(item.getImages());
				// ImageLoader imageLoader = ImageLoader.getInstance();
				// imageLoader.displayImage(item.getImages(),
				// viewHolder.imageView, GlobalParams.movieOptions);
			} else {
				viewHolder.image.setTag(item.getImages());

				ImageLoader imageLoader = ImageLoader.getInstance();
				imageLoader.displayImage(item.getImages(), viewHolder.image, GlobalParams.foundOptions);

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.toString();
		}

		return view;
	}

	class ViewHolder {

	TextView titleText;
		ImageView image;
		
		TextView time;
		TextView TotalTime;
		TextView Dtext;
		TextView moreText;
		RelativeLayout moreRe;
		

	}

}
