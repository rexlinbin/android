package com.bccv.tianji.adapter;

import java.util.List;

import com.bccv.tianji.R;
import com.bccv.tianji.model.Comment;
import com.bccv.tianji.model.Reply;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyReplyAdapter extends BaseAdapter {
	private Context context;
	private List<Reply> list;
	
	public MyReplyAdapter(Context context, List<Reply> list){
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
			convertView = View.inflate(context, R.layout.listitem_reply, null);
			viewHolder.replyNameTextView = (TextView) convertView.findViewById(R.id.replyName_textView);
			viewHolder.replyContenTextView = (TextView) convertView.findViewById(R.id.replyContent_textView);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Reply reply = list.get(position);
		viewHolder.replyNameTextView.setText(reply.getUser_name() + "：");
		viewHolder.replyContenTextView.setText(reply.getComment());
		
		return convertView;
	}
	
	class ViewHolder{
		TextView replyNameTextView;
		TextView replyContenTextView;
	}

}
