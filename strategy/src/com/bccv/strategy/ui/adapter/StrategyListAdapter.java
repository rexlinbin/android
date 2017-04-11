package com.bccv.strategy.ui.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bccv.strategy.R;
import com.bccv.strategy.model.StrategyListBean.GameStrategyInfo;
import com.bccv.strategy.utils.ImageLoaderUtil;

public class StrategyListAdapter extends BaseAdapter {

	public static final String TAG = "StrategyListAdapter";

	private Activity mActivity;
	private ArrayList<GameStrategyInfo> dataList;
	private LayoutInflater inflater;
	
	public StrategyListAdapter(Activity activity,ArrayList<GameStrategyInfo> dataList){
		this.mActivity = activity;
		this.dataList = dataList;
		inflater = LayoutInflater.from(mActivity);
	}
	
	@Override
	public int getCount() {
		return dataList==null?0:dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		
		if(convertView==null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.strategy_list_item, null);
			holder.strategy_item_iv = (ImageView) convertView.findViewById(R.id.strategy_item_iv);
			holder.strategy_item_title = (TextView) convertView.findViewById(R.id.strategy_item_title);
			holder.strategy_item_intro = (TextView) convertView.findViewById(R.id.strategy_item_intro);
			holder.strategy_item_zan = (TextView) convertView.findViewById(R.id.strategy_item_zan);
			holder.strategy_item_pinglun = (TextView) convertView.findViewById(R.id.strategy_item_pinglun);
			
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		//TODO 设置数据
		GameStrategyInfo gameStrategyInfo = dataList.get(position);
		ImageLoaderUtil.getInstance(mActivity).displayImage(gameStrategyInfo.getNews_titlepic(), 
				holder.strategy_item_iv,ImageLoaderUtil.getStrategyshowImageOptions());
		holder.strategy_item_title.setText(gameStrategyInfo.getNews_title());
		holder.strategy_item_intro.setText(gameStrategyInfo.getNews_ftitle());
		holder.strategy_item_zan.setText(String.valueOf(gameStrategyInfo.getNews_digg()));
		holder.strategy_item_pinglun.setText(String.valueOf(gameStrategyInfo.getNews_comment()));
		
		convertView.setTag(holder);
		return convertView;
	}

	
	private static class ViewHolder{
		ImageView strategy_item_iv;
		TextView strategy_item_title;
		TextView strategy_item_intro;
		TextView strategy_item_zan;
		TextView strategy_item_pinglun;
	}
	
}
