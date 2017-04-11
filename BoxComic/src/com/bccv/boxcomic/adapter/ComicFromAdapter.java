package com.bccv.boxcomic.adapter;

import java.util.List;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.modal.OnlineFrom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ComicFromAdapter extends BaseAdapter {

	private Context context;
	private List<OnlineFrom> list;
	private TextView textView;
	
	public ComicFromAdapter(Context context, List<OnlineFrom> list, TextView textView){
		this.context = context;
		this.list = list;
		this.textView = textView;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list != null ? list.size():0;
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

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(context, R.layout.listitem_comicfrom, null);
			viewHolder.textView = (TextView) convertView.findViewById(R.id.title_textView);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.textView.setText(list.get(position).getResource_name());
		if (list.get(position).equals(textView.getText().toString())) {
//			convertView.setSelected(true);
			if (position == list.size() - 1) {
				convertView.setBackgroundResource(R.drawable.comicfrom_select_last);
			}else {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					convertView.setBackground(new ColorDrawable(context.getResources().getColor(R.color.halflightgray)));
			    } else {
			    	convertView.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.halflightgray)));
			    }
				
			}
			
		}else {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				convertView.setBackground(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
		    } else {
		    	convertView.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
		    }
		}
		
		return convertView;
	}

	class ViewHolder{
		private TextView textView;
	}
}
