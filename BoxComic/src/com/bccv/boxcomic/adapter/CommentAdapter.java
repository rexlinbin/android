package com.bccv.boxcomic.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import u.aly.v;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.modal.Comment;
import com.bccv.boxcomic.tool.CircleImageView;
import com.bccv.boxcomic.tool.GlobalParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter {
	private Context context;
	private List<Comment> list;
	public CommentAdapter(Context context, List<Comment> list){
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
			convertView = View.inflate(context, R.layout.listitem_comment, null);
			viewHolder.contentTextView = (TextView) convertView.findViewById(R.id.comment_textView);
			viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.name_textView);
			viewHolder.timeTextView = (TextView) convertView.findViewById(R.id.time_textView);
			viewHolder.replyTextView = (TextView) convertView.findViewById(R.id.reply_textView);
			viewHolder.headCircleImageView = (CircleImageView) convertView.findViewById(R.id.head_circleImageView);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.nameTextView.setText(list.get(position).getUser_name());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String timeString = simpleDateFormat.format(new Date(list.get(position).getComment_date() * 1000L));
		viewHolder.timeTextView.setText(timeString);
		viewHolder.replyTextView.setText(list.get(position).getReport_num());
		viewHolder.contentTextView.setText(list.get(position).getComment_content());
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(list.get(position).getUser_icon(), viewHolder.headCircleImageView, GlobalParams.headOptions);
		
		return convertView;
	}

	class ViewHolder{
		CircleImageView headCircleImageView;
		TextView nameTextView;
		TextView timeTextView;
		TextView replyTextView;
		TextView contentTextView;
	}
}
