package com.bccv.zhuiyingzhihanju.adapter;

import java.util.List;

import com.bccv.zhuiyingzhihanju.R;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.model.DownloadMovie;
import com.utils.tools.GlobalParams;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DownloadAdapter extends BaseAdapter {
	private Context context;
	private List<DownloadMovie> list;
	
	public DownloadAdapter(Context context, List<DownloadMovie> list) {
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
			convertView = View.inflate(context, R.layout.listitem_download, null);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
			viewHolder.selectImageView = (ImageView) convertView.findViewById(R.id.select_imageView);
			viewHolder.topTextView = (TextView) convertView.findViewById(R.id.top_textView);
			viewHolder.bottomTextView = (TextView) convertView.findViewById(R.id.bottom_textView);
			viewHolder.rightTextView = (TextView) convertView.findViewById(R.id.right_textView);
			viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		DownloadMovie item = list.get(position);
		if (item.isEpisode()) {
			if (item.getEpisode_id() == 0) {
				viewHolder.topTextView.setText(item.getTitle());
			} else {
				viewHolder.topTextView.setText(item.getTitle() + " 第" + item.getEpisode_id() + "集");
			}
		} else {
			viewHolder.topTextView.setText(item.getTitle());
		}

		viewHolder.selectImageView.setSelected(item.isSelect());
//		Log.e("选中", position+"是否选中"+item.isSelect()+"");
		
		
		if (item.isEdit()) {

			viewHolder.selectImageView.setVisibility(View.VISIBLE);
		} else {
			viewHolder.selectImageView.setVisibility(View.GONE);
		}

		if (item.isDownloading()) {
			viewHolder.progressBar.setVisibility(View.VISIBLE);
			if (item.isM3U8()) {
				viewHolder.progressBar.setProgress((int) item.getProgress());
			} else {
				viewHolder.progressBar.setProgress((int) item.getLastSize());
			}

		} else {
			viewHolder.progressBar.setVisibility(View.GONE);
		}

		viewHolder.bottomTextView.setText(item.getSize());
		viewHolder.rightTextView.setText(item.getSpeed());

		String images = (String) viewHolder.imageView.getTag();
		if (images != null && images.equals(item.getImageUrl())) {
			// viewHolder.imageView.setTag(item.getImages());
			// ImageLoader imageLoader = ImageLoader.getInstance();
			// imageLoader.displayImage(item.getImages(), viewHolder.imageView,
			// GlobalParams.movieOptions);
		} else {
			viewHolder.imageView.setTag(item.getImageUrl());
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage(item.getImageUrl(), viewHolder.imageView, GlobalParams.downloadOptions);
		}

		return convertView;
	}

	class ViewHolder {
		ImageView imageView;
		TextView topTextView;
		ProgressBar progressBar;
		TextView rightTextView;
		TextView bottomTextView;
		ImageView selectImageView;

	}

}
