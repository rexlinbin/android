package com.bccv.bangyangapp.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bccv.bangyangapp.R;
import com.bccv.bangyangapp.model.AppInfoItemBean;
import com.bccv.bangyangapp.utils.ImageLoaderUtil;

public class RoundTabAdapter implements IRoundTabAdapter {

	private ArrayList<AppInfoItemBean> images;
	private Context context;

	public RoundTabAdapter(ArrayList<AppInfoItemBean> images, Context context) {
		this.context = context;
		this.images = images;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return images == null ? 0 : images.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return images.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(context,
					R.layout.homepage_icon_item_view, null);
			holder.icon = (ImageView) convertView
					.findViewById(R.id.round_tab_icon);
		} else {
			holder = (Holder) convertView.getTag();
		}
		// TODO 设置数据
		if (position < images.size()) {
			AppInfoItemBean homeItemBean = images.get(position);
			ImageLoaderUtil.getInstance(context).displayImage(
					homeItemBean.getIcon(), holder.icon,
					ImageLoaderUtil.getAppIconImageOptions());
		}
		convertView.setTag(holder);
		return convertView;
	}

	@Override
	public void getData(View view, int position) {

		if (position < images.size()) {
			Holder holder = (Holder) view.getTag();
			AppInfoItemBean homeItemBean = images.get(position);
			ImageLoaderUtil.getInstance(context).displayImage(
					homeItemBean.getIcon(), holder.icon,
					ImageLoaderUtil.getAppIconImageOptions());
		}
	}

	private class Holder {
		ImageView icon;
	}

}
