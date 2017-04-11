package com.bccv.zhuiying.adapter;

import java.util.List;
import com.bccv.zhuiying.R;
import com.bccv.zhuiying.model.Movie;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.tools.GlobalParams;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HistoryListAdapter extends BaseAdapter{
	private Context context;
	private List<Movie> list;

	public HistoryListAdapter(Context context, List<Movie> list) {
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
			convertView = View.inflate(context, R.layout.listitem_history, null);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
			viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
		viewHolder.textView2=(TextView) convertView.findViewById(R.id.textView2);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		try {
			Movie item=list.get(position);
			if(item.getEpisode_id()!=null&&!item.getEpisode_id().equals("")){
				viewHolder.textView.setText(item.getTitle()+"   第"+item.getEpisode_id()+"集");
			}else{
				viewHolder.textView.setText(item.getTitle());
			}
			
				if(item.getPlay_Time()==0){
					viewHolder.textView2.setText("未播放");
				}else{
					
					
					long time = item.getPlay_Time(); 
					viewHolder.textView2.setText(toTime((int) time)); 
					
				}
			
			
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage(item.getImages(), viewHolder.imageView, GlobalParams.movieOptions);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	
	
		
		
		return convertView;
	}
	
	public String toTime(int time) {
		int minute = time / 60;
		int hour = minute / 60;
		int second = time % 60;
		minute %= 60;
		if (hour > 0) {
			return String.format("%02d:%02d:%02d", hour, minute, second);
		} else {
			return String.format("%02d:%02d", minute, second);
		}
	}
	
	class ViewHolder {
		ImageView imageView;
		TextView textView;
		TextView textView2;
	}


}
