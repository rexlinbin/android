package com.bccv.zhuiyingzhihanju.adapter;

import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.model.SearchInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.tools.GlobalParams;
import com.utils.tools.StringUtils;
import com.utils.views.RoundedImageView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchAdapter extends BaseAdapter {
	private Context context;
	private List<SearchInfo> list;

	public SearchAdapter(Context context, List<SearchInfo> list) {
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
			convertView = View.inflate(context, R.layout.listitem_collect, null);
			viewHolder.roundedImageView = (RoundedImageView) convertView.findViewById(R.id.roundedImageView);
			viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.title_textView);
			viewHolder.introTextView = (TextView) convertView.findViewById(R.id.intro_textView);
			viewHolder.view = convertView.findViewById(R.id.black_view);
			viewHolder.episodeTextView = (TextView) convertView.findViewById(R.id.episode_textView);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		SearchInfo searchInfo = list.get(position);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(searchInfo.getImage(), viewHolder.roundedImageView, GlobalParams.bannerOptions);
		viewHolder.titleTextView.setText(searchInfo.getTitle());
		viewHolder.introTextView.setText(searchInfo.getDes());
		if (!StringUtils.isEmpty(searchInfo.getEpisode())) {

			viewHolder.episodeTextView.setText(searchInfo.getEpisode());
			viewHolder.episodeTextView.setVisibility(View.VISIBLE);
			viewHolder.view.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	class ViewHolder{
		RoundedImageView roundedImageView;
		TextView titleTextView;
		TextView introTextView;
		View view;
		TextView episodeTextView;
	}
}
