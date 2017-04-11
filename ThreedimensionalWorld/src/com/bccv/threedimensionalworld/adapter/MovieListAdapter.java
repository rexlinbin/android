package com.bccv.threedimensionalworld.adapter;

import java.util.List;

import com.baidu.cyberplayer.core.BVideoView;
import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.model.Movie;
import com.bccv.threedimensionalworld.tool.GlobalParams;
import com.bccv.threedimensionalworld.tool.HorizontalListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class MovieListAdapter extends BaseAdapter {
	private Context context;
	private List<Movie> list;
	private int selectNum = 0;
	private boolean isLeft;
	
	public MovieListAdapter(Context context, List<Movie> list) {
		this.context = context;
		this.list = list;
	}
	
	public MovieListAdapter(Context context, List<Movie> list, boolean isLeft) {
		this.context = context;
		this.list = list;
		this.isLeft = isLeft;
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

	public void setSelect(int selectNum) {
		if (selectNum != this.selectNum) {
			this.selectNum = selectNum;
			notifyDataSetChanged();
		}
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_movielist, null);
			viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.item_layout);
			viewHolder.movieNameTextView = (TextView) convertView.findViewById(R.id.movieName_textView);
			viewHolder.movieImageView = (ImageView) convertView
					.findViewById(R.id.movie_ImageView);
			viewHolder.movieBgImageView = (ImageView) convertView.findViewById(R.id.moviebg_imageView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.movieNameTextView.setText(list.get(position).getTitle());
		viewHolder.movieNameTextView.setScaleX(0.5f);
		if (position == selectNum) {
			viewHolder.movieBgImageView.setSelected(true);
//			if (isLeft) {
//				LayoutParams params = (LayoutParams) viewHolder.layout.getLayoutParams();
//				params.leftMargin = params.leftMargin + 1;
//				viewHolder.layout.setLayoutParams(params);
//			}
//			
//			convertView.setScaleX(1.1f);
//			convertView.setScaleY(1.1f);
		} else {
			viewHolder.movieBgImageView.setSelected(false);
//			if (isLeft) {
//				LayoutParams params = (LayoutParams) viewHolder.layout.getLayoutParams();
//				params.leftMargin = 0;
//				viewHolder.layout.setLayoutParams(params);
//			}
//			
//			convertView.setScaleX(1.0f);
//			convertView.setScaleY(1.0f);
		}
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		if (isLeft) {
			if (position == -10) {
				imageLoader.displayImage("http://www.boxuu.com/cleft1.png", viewHolder.movieImageView, GlobalParams.options);
			}else {
				imageLoader.displayImage(list.get(position).getImages(), viewHolder.movieImageView, GlobalParams.options);
			}
		}else {
			if (position == -10) {
				imageLoader.displayImage("http://www.boxuu.com/cright1.png", viewHolder.movieImageView, GlobalParams.options);
			}else {
				imageLoader.displayImage(list.get(position).getImages(), viewHolder.movieImageView, GlobalParams.options);
			}
		}
		
		
		return convertView;
	}

	class ViewHolder {
		LinearLayout layout;
		ImageView movieImageView;
		ImageView movieBgImageView;
		TextView movieNameTextView;
	}
}
