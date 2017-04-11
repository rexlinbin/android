package com.bccv.zhuiyingzhihanju.adapter;

import java.util.List;
import java.util.Map;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.model.MovieEpisode;
import com.utils.download.DownloadManager;
import com.utils.download.DownloadService;
import com.utils.download.DownloadUtil;
import com.utils.model.DownloadMovie;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DownloadEpisodeAdapter extends BaseAdapter {
	Context context;
	List<MovieEpisode> list;
	private DownloadManager downloadManager;
	String movie_id = "", type_id = "";
	int gridNum;
	public DownloadEpisodeAdapter(Context context, List<MovieEpisode> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
		downloadManager = DownloadUtil.getDownloadManager();
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

	public void setMovieInfo(String movie_id, String type_id){
		this.movie_id = movie_id;
		this.type_id = type_id;
	}
	
	public void setGridNum(int gridNum){
		this.gridNum = gridNum;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(context, R.layout.griditem_downloadselect, null);
			viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (gridNum == 2) {
			viewHolder.textView.setText(list.get(position).getPeriods() + "");
		}else if (gridNum == 3) {
			viewHolder.textView.setText(list.get(position).getDes() + "");
		}else{
			viewHolder.textView.setText(list.get(position).getId() + "");
		}
		
		Map<String, DownloadMovie> movieMap = downloadManager.getMovieMap();
		DownloadMovie downloadMovie = movieMap.get(movie_id + "," + type_id + ","
				+ list.get(position).getId());
		if (downloadMovie != null && downloadMovie.isSuccess()) {
			viewHolder.imageView.setVisibility(View.VISIBLE);
			viewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.download_complete));
			if (list.get(position).isSelect()) {
				viewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.jishu_select));
			}
		}else{
			if (list.get(position).isSelect()) {
				viewHolder.imageView.setVisibility(View.VISIBLE);
				viewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.jishu_select));
			} else {
				viewHolder.imageView.setVisibility(View.GONE);
			}
		}

		

		return convertView;
	}

	class ViewHolder {
		TextView textView;
		ImageView imageView;
	}
}
