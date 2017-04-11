package com.bccv.strategy.ui.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bccv.strategy.R;
import com.bccv.strategy.model.GameInfoBean;
import com.bccv.strategy.ui.activity.MainActivity;
import com.bccv.strategy.ui.activity.StrategyListActivity;
import com.bccv.strategy.ui.view.BackGroundView;
import com.bccv.strategy.ui.view.MyGridView;
import com.bccv.strategy.utils.ImageLoaderUtil;

public class MyGameListAdapter extends BaseAdapter {

	private Context mContext;
	private List<GameInfoBean> data;
	private boolean mDFlag = false;

	public MyGameListAdapter(Context context, List<GameInfoBean> data) {
		this.mContext = context;
		this.data = data;
	}

	public void setData(List<GameInfoBean> d) {
		if (data != null) {
			data = null;
			if (d != null && data.size() > 0) {
				data = d;
			}
			notifyDataSetChanged();
		}
	}
	public void setData(GameInfoBean b) {
		if (data != null) {
			data.remove(b);
			notifyDataSetChanged();
		}
	}
	
	public List<GameInfoBean> getData() {
		return this.data == null ? null : this.data;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data == null ? 0 : data.size() / 4 + 1;
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

	public void setMDFlage(boolean md) {
		this.mDFlag = md;
		notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			MyGridView gridView = new MyGridView(mContext);
			convertView = gridView;
			gridView.setNumColumns(4);
			holder.onItemClickListener = new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					GameGridAdapter adapter = (GameGridAdapter) parent
							.getAdapter();
					GameInfoBean item = (GameInfoBean) adapter
							.getItem(position);
					Intent strategyListIntent = new Intent(mContext, StrategyListActivity.class);
					strategyListIntent.putExtra(StrategyListActivity.GAME_ID, item.getId());
					strategyListIntent.putExtra(BackGroundView.BACKGROUND_COLOR, MainActivity.backGroundView.getGradientColor());
					mContext.startActivity(strategyListIntent);

				}
			};
			gridView.setOnItemClickListener(holder.onItemClickListener);
		} else {
			holder = (Holder) convertView.getTag();
		}
		// TODO 设置数据
		int startIndex = position * 4;
		int endIndex = startIndex + 4;
		if (endIndex > data.size()) {
			endIndex = data.size();
		}
		GameGridAdapter adapter = new GameGridAdapter(mContext, data.subList(
				startIndex, endIndex),this);
		adapter.setDFlag(mDFlag);
		MyGridView gv = (MyGridView) convertView;
		gv.setAdapter(adapter);
		convertView.setTag(holder);
		return convertView;
	}

	private static class Holder {
		public OnItemClickListener onItemClickListener;

	}

	private static class GameGridAdapter extends BaseAdapter {

		private Context mContext;
		private List<GameInfoBean> data;
		private boolean dFlag = false;
		private MyGameListAdapter listAdapter;
		public GameGridAdapter(Context context, List<GameInfoBean> data,MyGameListAdapter listAdapter) {
			this.mContext = context;
			this.data = data;
			this.listAdapter = listAdapter;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data == null ? 0 : data.size();
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
		
		public void setDFlag(boolean d) {
			this.dFlag = d;
			notifyDataSetChanged();
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			GridViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mContext, R.layout.app_item, null);
				holder = new GridViewHolder();
				holder.appIcon = (ImageView) convertView
						.findViewById(R.id.app_icon);
				holder.appName = (TextView) convertView
						.findViewById(R.id.app_name);
				holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
			} else {
				holder = (GridViewHolder) convertView.getTag();
			}
			// TODO 设置数据
			
			if (dFlag) {
				holder.iv_delete.setVisibility(View.VISIBLE);
			}else {
				holder.iv_delete.setVisibility(View.GONE);
			}
			
			holder.iv_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//					data.remove(position);
					listAdapter.setData(data.get(position));
//					notifyDataSetChanged();
				}
			});
			GameInfoBean gameInfoBean = data.get(position);
			holder.appName.setText(gameInfoBean.getGame_title());
			System.out.println("icon : " + gameInfoBean.getGame_icon());
			ImageLoaderUtil.getInstance(mContext).displayImage(
					gameInfoBean.getGame_icon(), holder.appIcon,
					ImageLoaderUtil.getAppIconImageOptions());
			convertView.setTag(holder);
			return convertView;
		}
	}

	private static class GridViewHolder {
		public ImageView appIcon;
		public TextView appName;
		public ImageView iv_delete;
	}

}
