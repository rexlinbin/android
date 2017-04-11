package com.bccv.zhuiyingzhihanju.adapter;

import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.model.MovieEpisode;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoEpisodeAdapter extends BaseAdapter {
	Context context;
	List<MovieEpisode> list;
	int gridNum;

	public InfoEpisodeAdapter(Context context, List<MovieEpisode> list) {
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

	public void setGridNum(int gridNum){
		this.gridNum = gridNum;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(context, R.layout.griditem_info_episode, null);
			viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (gridNum == 2) {
			viewHolder.textView.setText(list.get(position).getPeriods() + "");
		}else if (gridNum == 3) {
			viewHolder.textView.setText(list.get(position).getDes() + "");
		}else{
			viewHolder.textView.setText(list.get(position).getId() + "");
		}
		viewHolder.textView.setSelected(list.get(position).isSelect());
		
		
		return convertView;
	}

	class ViewHolder{
		TextView textView;
	}
}
