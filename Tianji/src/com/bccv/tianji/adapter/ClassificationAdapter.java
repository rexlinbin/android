package com.bccv.tianji.adapter;

import com.bccv.tianji.R;
import com.bccv.tianji.model.Classification;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.tools.GlobalParams;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ClassificationAdapter extends BaseAdapter {
	private Context context;
	private List<Classification> list;

	public ClassificationAdapter(Context context, List<Classification> list) {
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
			convertView = View.inflate(context, R.layout.griditem_classification, null);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
			viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(list.get(position).getType_image(), viewHolder.imageView, GlobalParams.typeOptions);
		
		viewHolder.textView.setText(list.get(position).getTitle());
		
		return convertView;
	}
	
	class ViewHolder{
		ImageView imageView;
		TextView textView;
	}

}
