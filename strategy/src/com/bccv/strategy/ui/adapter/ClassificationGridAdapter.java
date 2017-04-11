package com.bccv.strategy.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bccv.strategy.model.CategoryResBean.CategoryBean;
import com.bccv.strategy.R;

public class ClassificationGridAdapter extends BaseAdapter {

	
	private Context mContext;
	private ArrayList<CategoryBean> data;
	
	public ClassificationGridAdapter(Context context,ArrayList<CategoryBean> data){
		this.mContext = context;
		this.data = data;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data==null?0:data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder;
		if(convertView==null){
			convertView = View.inflate(mContext, R.layout.classification_item_view, null);
			holder = new Holder();
			holder.tv = (TextView) convertView.findViewById(R.id.classification_item_tv);
		}else{
			holder = (Holder) convertView.getTag();
		}
		
		String name = data.get(position).getName();
		
		if(name!=null&&name.length()==4){
			int pick = name.length()/2;
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(name.substring(0, pick));
			stringBuilder.append("\n");
			stringBuilder.append(name.substring(pick));
			name = stringBuilder.toString();
		}else if(name!=null&&name.length()>4){
			name = data.get(position).getF_name();
		}
		
		holder.tv.setText(name);
		
		convertView.setTag(holder);
		return convertView;
	}
	
	private class Holder{
		TextView tv;
	}

}
