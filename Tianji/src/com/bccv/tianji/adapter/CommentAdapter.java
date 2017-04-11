package com.bccv.tianji.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.tianji.R;
import com.bccv.tianji.activity.ReplyActivity;
import com.bccv.tianji.activity.dialog.ReplyDialog;
import com.bccv.tianji.activity.dialog.ReplyDialog.ReplayDialogcallback;
import com.bccv.tianji.api.CommentApi;
import com.bccv.tianji.model.Comment;
import com.bccv.tianji.model.CommentReply;
import com.bccv.tianji.model.Reply;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.tools.GlobalParams;
import com.utils.tools.StringUtils;

public class CommentAdapter extends BaseAdapter {
	private Context context;
	private List<CommentReply> list;
	private Activity activity;

	private String GameId;
	Completecallback comCallback;

	public CommentAdapter(Context context, List<CommentReply> list,
			Activity activity, String gameId) {
		super();
		this.context = context;
		this.list = list;
		this.activity = activity;
		GameId = gameId;
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
			arg1 = View.inflate(context, R.layout.listitem_comment, null);

			viewHolder.commentTitleTextView = (TextView) arg1
					.findViewById(R.id.commentTitle_textView);
			viewHolder.commentTimeTextView = (TextView) arg1
					.findViewById(R.id.time_textView);
			viewHolder.commentContentTextView = (TextView) arg1
					.findViewById(R.id.content_textView);
			viewHolder.replyLayout = (LinearLayout) arg1
					.findViewById(R.id.reply_layout);
			viewHolder.replyNameTextView = (TextView) arg1
					.findViewById(R.id.replyName_textView);
			viewHolder.replyContentTextView = (TextView) arg1
					.findViewById(R.id.replyContent_textView);
			viewHolder.lineView = arg1.findViewById(R.id.line_view);
			viewHolder.moreTextView = (TextView) arg1
					.findViewById(R.id.more_textView);
			viewHolder.icon = (ImageView) arg1
					.findViewById(R.id.commentIcon_imageView);
			viewHolder.ReplyBtn = (Button) arg1
					.findViewById(R.id.commentReply_btn);
			arg1.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) arg1.getTag();
		}

		arg1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		viewHolder.commentTitleTextView
				.setTextColor(viewHolder.commentTitleTextView.getResources()
						.getColor(R.color.titleColor));

		viewHolder.commentTimeTextView
				.setTextColor(viewHolder.commentTimeTextView.getResources()
						.getColor(R.color.timeColor));

		viewHolder.replyContentTextView
				.setTextColor(viewHolder.replyContentTextView.getResources()
						.getColor(R.color.replyColor));
		viewHolder.commentContentTextView
				.setTextColor(viewHolder.commentContentTextView.getResources()
						.getColor(R.color.contentColor));

		final CommentReply comment = list.get(arg0);

		viewHolder.ReplyBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (GlobalParams.hasLogin) {
					ReplyDialog replyDia = new ReplyDialog(context);
					replyDia.setReply(true);
					replyDia.setCommentId(comment.getComment_id());
					replyDia.setDialogCallback(dialogcallback);
					replyDia.show();
				} else {
					Toast.makeText(context, "对不起，您没有登录不能回复评论",
							Toast.LENGTH_SHORT).show();

				}

			}
		});
		if (!comment.getReply_more().equals("0")) {

			viewHolder.commentTitleTextView.setText(comment.getNick_name());
			viewHolder.commentTimeTextView.setText(StringUtils.getFormateTime(
					Long.parseLong(comment.getTimes()) * 1000L, "yyyy-MM-dd"));
			viewHolder.commentContentTextView.setText(comment.getComment());

			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage(comment.getUser_icon(), viewHolder.icon,
					GlobalParams.iconOptions);

			List<Reply> listReplies = comment.getReply();

			if (listReplies != null && listReplies.size() > 0) {

				viewHolder.replyLayout.setVisibility(View.VISIBLE);
				if (comment.getReply_more().equals("1")) {

					final Comment commItem = new Comment();
					commItem.setComment(comment.getComment());
					commItem.setComment_id(comment.getComment_id());
					commItem.setNick_name(comment.getNick_name());
					commItem.setReply_list(listReplies);
					commItem.setReply_more(comment.getReply_more());
					commItem.setTimes(comment.getTimes());
					commItem.setUser_icon(comment.getUser_icon());
					commItem.setUser_name(comment.getUser_name());

					Reply reply = listReplies.get(0);
					viewHolder.replyNameTextView.setText(reply.getNick_name()
							+ "：");
					viewHolder.replyContentTextView.setText(reply.getComment());
					viewHolder.lineView.setVisibility(View.VISIBLE);
					viewHolder.moreTextView.setVisibility(View.VISIBLE);
					viewHolder.moreTextView
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Intent intent = new Intent(context,
											ReplyActivity.class);
									intent.putExtra("isMy", false);
									intent.putExtra("comment", commItem);
									activity.startActivity(intent);
								}
							});
				} else {
					Reply reply = listReplies.get(0);
					viewHolder.replyNameTextView.setText(reply.getNick_name()
							+ "：");
					viewHolder.replyContentTextView.setText(reply.getComment());
					viewHolder.lineView.setVisibility(View.GONE);
					viewHolder.moreTextView.setVisibility(View.GONE);
				}
			} else {
				viewHolder.replyLayout.setVisibility(View.GONE);
			}

		} else {

			viewHolder.commentTitleTextView.setText(comment.getNick_name());
			viewHolder.commentTimeTextView.setText(StringUtils.getFormateTime(
					Long.parseLong(comment.getTimes()) * 1000L, "yyyy-MM-dd"));
			viewHolder.commentContentTextView.setText(comment.getComment());
			viewHolder.replyLayout.setVisibility(View.GONE);
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage(comment.getUser_icon(), viewHolder.icon,
					GlobalParams.iconOptions);
		}

		return arg1;
	}

	class ViewHolder {

		TextView commentTitleTextView;
		TextView commentTimeTextView;
		TextView commentContentTextView;
		LinearLayout replyLayout;
		TextView replyNameTextView;
		TextView replyContentTextView;
		View lineView;
		TextView moreTextView;
		ImageView icon;
		Button ReplyBtn;
	}

	ReplayDialogcallback dialogcallback = new ReplayDialogcallback() {

		@Override
		public void dialogdo(String replyContent, int arg1, boolean isReply,
				String commentID) {
			// TODO Auto-generated method stub
			if (isReply) {
				new ReplyTask().execute(replyContent, commentID);
			}

		}

	};

	class ReplyTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {

			String isFresh = params[0];
			String point = params[1];
			String userID = GlobalParams.user.getUser_id();
			CommentApi comment = new CommentApi();

			String result = comment.ReplyList(GameId, point, isFresh, userID);

			return result;

		}

		@Override
		protected void onPostExecute(String isStar) {
			super.onPostExecute(isStar);

			if (isStar.equals("1")) {

				Toast.makeText(context, "回复完成", Toast.LENGTH_SHORT).show();
				comCallback.commplete(true);
			}

		}

	}

	public interface Completecallback {
		public void commplete(boolean isComple);
	}

	public Completecallback getComCallback() {
		return comCallback;
	}

	public void setComCallback(Completecallback comCallback) {
		this.comCallback = comCallback;
	}

}
