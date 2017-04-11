package com.bccv.tianji.adapter;

import java.util.List;

import com.bccv.tianji.R;
import com.bccv.tianji.activity.ReplyActivity;
import com.bccv.tianji.model.Comment;
import com.bccv.tianji.model.Reply;
import com.utils.tools.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyCommentsAdapter extends BaseAdapter {
	private Context context;
	private List<Comment> list;
	private Activity activity;
	
	public MyCommentsAdapter(Context context, Activity activity, List<Comment> list){
		this.context = context;
		this.list = list;
		this.activity = activity;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (arg1 == null) {
			viewHolder = new ViewHolder();
			arg1 = View.inflate(context, R.layout.listitem_mycomments, null);
			viewHolder.commentTypeTextView = (TextView) arg1.findViewById(R.id.commentType_textView);
			viewHolder.commentTitleTextView = (TextView) arg1.findViewById(R.id.commentTitle_textView);
			viewHolder.commentTimeTextView = (TextView) arg1.findViewById(R.id.time_textView);
			viewHolder.commentContentTextView = (TextView) arg1.findViewById(R.id.content_textView);
			viewHolder.replyLayout = (LinearLayout) arg1.findViewById(R.id.reply_layout);
			viewHolder.replyNameTextView = (TextView) arg1.findViewById(R.id.replyName_textView);
			viewHolder.replyContentTextView = (TextView) arg1.findViewById(R.id.replyContent_textView);
			viewHolder.lineView = arg1.findViewById(R.id.line_view);
			viewHolder.moreTextView = (TextView) arg1.findViewById(R.id.more_textView);
			
			arg1.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) arg1.getTag();
		}
		
		arg1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		final Comment comment = list.get(arg0);
		if (comment.getReply().equals("0")) {
			viewHolder.commentTypeTextView.setText("评论了：");
			viewHolder.commentTitleTextView.setText(comment.getTitle());
			viewHolder.commentTimeTextView.setText(StringUtils.getFormateTime(Long.parseLong(comment.getTimes()) * 1000L, "yyyy-MM-dd"));
			viewHolder.commentContentTextView.setText(comment.getComment());
			
			List<Reply> listReplies = comment.getReply_list();
			if (listReplies != null && listReplies.size() > 0) {
				viewHolder.replyLayout.setVisibility(View.VISIBLE);
				if (comment.getReply_more().equals("1")) {
					Reply reply = listReplies.get(0);
					viewHolder.replyNameTextView.setText(reply.getNick_name() + "：");
					viewHolder.replyContentTextView.setText(reply.getComment());
					viewHolder.lineView.setVisibility(View.VISIBLE);
					viewHolder.moreTextView.setVisibility(View.VISIBLE);
					viewHolder.moreTextView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(context, ReplyActivity.class);
							intent.putExtra("isMy", true);
							intent.putExtra("comment", comment);
							activity.startActivity(intent);
						}
					});
				}else {
					Reply reply = listReplies.get(0);
					viewHolder.replyNameTextView.setText(reply.getNick_name() + "：");
					viewHolder.replyContentTextView.setText(reply.getComment());
					viewHolder.lineView.setVisibility(View.GONE);
					viewHolder.moreTextView.setVisibility(View.GONE);
				}
			}else {
				viewHolder.replyLayout.setVisibility(View.GONE);
			}
			
		}else {
			viewHolder.commentTypeTextView.setText("回复了：");
			viewHolder.commentTitleTextView.setText(comment.getTitle());
			viewHolder.commentTimeTextView.setText(StringUtils.getFormateTime(Long.parseLong(comment.getTimes()) * 1000L, "yyyy-MM-dd"));
			viewHolder.commentContentTextView.setText(comment.getComment());
			viewHolder.replyLayout.setVisibility(View.GONE);
		}
		
		return arg1;
	}
	
	class ViewHolder{
		TextView commentTypeTextView;
		TextView commentTitleTextView;
		TextView commentTimeTextView;
		TextView commentContentTextView;
		LinearLayout replyLayout;
		TextView replyNameTextView;
		TextView replyContentTextView;
		View lineView;
		TextView moreTextView;
	}

}
