package com.bccv.threedimensionalworld.adapter;

import java.util.List;

import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.adapter.MovieListAdapter.ViewHolder;
import com.bccv.threedimensionalworld.model.App;
import com.bccv.threedimensionalworld.tool.GlobalParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class AppListAdapter extends BaseAdapter {
	private Context context;
	private List<App> list;

	public AppListAdapter(Context context, List<App> list) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_applist, null);
			viewHolder.layout = (LinearLayout) convertView
					.findViewById(R.id.item_layout);
			viewHolder.appNameTextView = (TextView) convertView
					.findViewById(R.id.appName_textView);
			viewHolder.scoreTitleTextView = (TextView) convertView
					.findViewById(R.id.scoretitle_textView);
			viewHolder.scoreTextView = (TextView) convertView
					.findViewById(R.id.score_textView);
			viewHolder.introTextView = (TextView) convertView
					.findViewById(R.id.intro_textView);
			viewHolder.appImageView = (ImageView) convertView
					.findViewById(R.id.app_ImageView);
			viewHolder.appBgImageView = (ImageView) convertView
					.findViewById(R.id.appbg_imageView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
//		viewHolder.layout.setScaleX(0.5f);
		
		viewHolder.appNameTextView.setText(list.get(position).getName());
		viewHolder.appNameTextView.setScaleX(0.5f);
		AssetManager mgr = context.getAssets();// 得到AssetManager
		Typeface tf = Typeface.createFromAsset(mgr, "fonts/Georgia.ttf");// 根据路径得到Typeface
		viewHolder.scoreTextView.setTypeface(tf);// 设置字体
		viewHolder.scoreTextView.setText(list.get(position).getApp_score_interface() + "");
		viewHolder.scoreTextView.setScaleX(0.5f);
		viewHolder.scoreTitleTextView.setScaleX(0.5f);
		viewHolder.introTextView.setText(list.get(position).getIntro());
		viewHolder.introTextView.setScaleX(0.5f);
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(list.get(position).getImages(),
				viewHolder.appImageView, GlobalParams.appIconOptions);

		return convertView;
	}

	class ViewHolder {
		LinearLayout layout;
		ImageView appImageView;
		ImageView appBgImageView;
		TextView appNameTextView;
		TextView scoreTitleTextView;
		TextView scoreTextView;
		TextView introTextView;
	}

}
