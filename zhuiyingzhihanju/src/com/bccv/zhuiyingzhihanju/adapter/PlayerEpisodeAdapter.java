package com.bccv.zhuiyingzhihanju.adapter;

import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.model.MovieEpisode;
import com.utils.tools.DimensionPixelUtil;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PlayerEpisodeAdapter extends BaseAdapter {
	Context context;
	List<MovieEpisode> list;
	int gridNum;

	public PlayerEpisodeAdapter(Context context, List<MovieEpisode> list) {
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
			convertView = View.inflate(context, R.layout.listitem_playerepisode, null);
			viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if (position == 0) {
			Drawable img_on;
			Resources res = context.getResources();
			img_on = res.getDrawable(R.drawable.player_new);
			//调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
			img_on.setBounds(0, 0, img_on.getMinimumWidth(), img_on.getMinimumHeight());
			viewHolder.textView.setCompoundDrawables(null, null, img_on, null);
			viewHolder.textView.setCompoundDrawablePadding((int) DimensionPixelUtil.dip2px(context, 5));
		}else{
			viewHolder.textView.setCompoundDrawables(null, null, null, null);
		}
		
		if (gridNum == 2) {
			viewHolder.textView.setText(list.get(position).getPeriods() + "");
		}else if (gridNum == 3) {
			viewHolder.textView.setText(list.get(position).getDes() + "");
		}else{
			try {
				int number=Integer.parseInt(list.get(position).getId());
				  String numberString = String.format("%02d", number);
				viewHolder.textView.setText(numberString + "");
			} catch (Exception e) {
				// TODO: handle exception
				viewHolder.textView.setText(list.get(position).getId() + "");
			}
			  
		}
		viewHolder.textView.setSelected(list.get(position).isSelect());
		
		
		
		return convertView;
	}

	class ViewHolder{
		TextView textView;
	}
}
