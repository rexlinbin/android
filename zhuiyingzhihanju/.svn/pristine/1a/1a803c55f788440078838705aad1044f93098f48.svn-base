package com.bccv.zhuiyingzhihanju.adapter;

import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.model.Msg;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.tools.GlobalParams;
import com.utils.tools.StringUtils;
import com.utils.views.CircleImageView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter {
	private Context context;
	private List<Msg> list;

	public CommentAdapter(Context context, List<Msg> list) {
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
			convertView = View.inflate(context, R.layout.listview_comment, null);
			viewHolder.likeImageView = (ImageView) convertView.findViewById(R.id.like_imageView);
			viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.title_textView);
			viewHolder.timeTextView = (TextView) convertView.findViewById(R.id.time_textView);
			viewHolder.contentTextView = (TextView) convertView.findViewById(R.id.content_textView);
			viewHolder.likeNumTextView = (TextView) convertView.findViewById(R.id.likeNum_textView);
			viewHolder.replyTextView = (TextView) convertView.findViewById(R.id.reply_textView);
			viewHolder.otherTextView = (TextView) convertView.findViewById(R.id.other_textView);
			viewHolder.headImageView = (CircleImageView) convertView.findViewById(R.id.circleImageView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final Msg item = list.get(position);

		viewHolder.titleTextView.setText(item.getUser_info().getNick_name());
		viewHolder.contentTextView.setText(item.getContent());

		Long time = item.getCtime();
		viewHolder.timeTextView.setText(StringUtils.friendly_time(time * 1000) + "");



		ImageLoader iconLoader = ImageLoader.getInstance();

		iconLoader.displayImage(item.getUser_info().getAvatars(), viewHolder.headImageView, GlobalParams.iconOptions);

	


		if (item.getF_user_info() == null) {
			viewHolder.replyTextView.setVisibility(View.GONE);
			viewHolder.otherTextView.setVisibility(View.GONE);
		} else {
			viewHolder.replyTextView.setVisibility(View.VISIBLE);
			viewHolder.otherTextView.setVisibility(View.VISIBLE);
			viewHolder.otherTextView.setText("@" + item.getF_user_info().getNick_name());
		}

		return convertView;
	}

	class ViewHolder {
		CircleImageView headImageView;
		ImageView likeImageView;

		TextView titleTextView;
		TextView contentTextView;
		TextView timeTextView;
		TextView likeNumTextView;
		TextView replyTextView;
		TextView otherTextView;
	}

}
