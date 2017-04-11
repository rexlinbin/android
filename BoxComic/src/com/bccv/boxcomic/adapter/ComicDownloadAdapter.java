package com.bccv.boxcomic.adapter;

import java.util.List;

import u.aly.v;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.modal.Comic;
import com.bccv.boxcomic.tool.GlobalParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ComicDownloadAdapter extends BaseAdapter {

	private Context context;
	private List<Comic> list;
	
	public ComicDownloadAdapter(Context context, List<Comic> list){
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
			convertView = View.inflate(context, R.layout.listitem_comicdownload, null);
			viewHolder.comicTitletextView = (TextView) convertView.findViewById(R.id.comicTitle_text);
			viewHolder.downloadStateTextView = (TextView) convertView.findViewById(R.id.downloadState_text);
			viewHolder.comicImageView = (ImageView) convertView.findViewById(R.id.comic_imageView);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.comicTitletextView.setText(list.get(position).getComic_title());
		viewHolder.downloadStateTextView.setText(list.get(position).getDownloadStateString());
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		String urlString = GlobalParams.imageUrlString;
		
		
		imageLoader.displayImage(urlString + list.get(position).getComic_titlepic(), viewHolder.comicImageView, GlobalParams.frameHomeoptions);
		
		return convertView;
	}

	class ViewHolder{
		TextView comicTitletextView;
		TextView downloadStateTextView;
		ImageView comicImageView;
	}
	
}
