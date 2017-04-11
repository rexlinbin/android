package com.bccv.zhuiyingzhihanju.adapter;


import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.model.Star;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.tools.GlobalParams;
import com.utils.views.CircleImageView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CircleAdapter extends BaseAdapter{

	private Context context;
	private List<Star> list;

	public CircleAdapter(Context context, List<Star> list) {
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
			convertView = View.inflate(context, R.layout.fancy_circle, null);
			viewHolder.circleImageView = (CircleImageView) convertView.findViewById(R.id.circle_image);
			viewHolder.numTextView = (TextView) convertView.findViewById(R.id.circle_num);
			viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.circle_textView);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(list.get(position).getAvatar(), viewHolder.circleImageView, GlobalParams.iconOptions);
		
		viewHolder.numTextView.setText(position + 1 + "");
		if (position == 0) {
			viewHolder.numTextView.setTextColor(context.getResources().getColor(R.color.numred));
			viewHolder.numTextView.setBackgroundResource(R.drawable.red);
		}else if (position == 1) {
			viewHolder.numTextView.setTextColor(context.getResources().getColor(R.color.numyellow));
			viewHolder.numTextView.setBackgroundResource(R.drawable.yellow);
		}else if (position == 2) {
			viewHolder.numTextView.setTextColor(context.getResources().getColor(R.color.green));
			viewHolder.numTextView.setBackgroundResource(R.drawable.green);
		}else{
			viewHolder.numTextView.setTextColor(context.getResources().getColor(R.color.numgray));
			viewHolder.numTextView.setBackgroundResource(R.drawable.gray);
		}
		
		viewHolder.nameTextView.setText(list.get(position).getName());
		return convertView;
	}

	class ViewHolder{
		CircleImageView circleImageView;
		TextView numTextView;
		TextView nameTextView;
	}
	
	
	
	
	
	
	
	
}
