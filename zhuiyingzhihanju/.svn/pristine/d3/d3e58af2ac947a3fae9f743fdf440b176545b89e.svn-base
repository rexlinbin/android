package com.bccv.zhuiyingzhihanju.adapter;

import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.model.Msg;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.tools.GlobalParams;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MSGAdapter extends BaseAdapter{
	private Context context;
	private List<Msg> list;
	
	
	public MSGAdapter(Context context, List<Msg> list) {
		super();
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
			convertView = View.inflate(context, R.layout.listview_msg, null);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.msg_image);
			viewHolder.MSGView = (TextView) convertView.findViewById(R.id.msg_sign);
			viewHolder.timeTextView = (TextView) convertView.findViewById(R.id.msg_time);
			viewHolder.dotImage=(ImageView) convertView.findViewById(R.id.msg_dot);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		try {
			Msg item=list.get(position);
			
			Long time=item.getCtime();
			viewHolder.timeTextView.setText(time+"");
			
			viewHolder.MSGView.setText(item.getContent());
			
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
		ImageView dotImage;
		TextView MSGView;
		TextView timeTextView;
	}

}
