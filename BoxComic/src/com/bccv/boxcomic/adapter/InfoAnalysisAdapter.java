package com.bccv.boxcomic.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.modal.InfoAnalysis;
import com.bccv.boxcomic.tool.StringUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InfoAnalysisAdapter extends BaseAdapter {

	private Context context;
	private List<InfoAnalysis> list;
	
	public InfoAnalysisAdapter(Context context, List<InfoAnalysis> list){
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
			convertView = View.inflate(context, R.layout.listitem_infoanalysislist, null);
			viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.title_textView);
			viewHolder.timeTextView = (TextView) convertView.findViewById(R.id.time1_textView);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.titleTextView.setText(list.get(position).getAdvices_title());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(list.get(position).getCtimes() * 1000L);
		viewHolder.timeTextView.setText(simpleDateFormat.format(date));
		return convertView;
	}
	
	class ViewHolder{
		TextView titleTextView;
		TextView timeTextView;
	}

}
