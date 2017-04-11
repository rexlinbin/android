package com.bccv.bangyangapp.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bccv.bangyangapp.R;
import com.bccv.bangyangapp.model.QuestionInfoBean;
import com.bccv.bangyangapp.utils.ImageLoaderUtil;
import com.bccv.bangyangapp.utils.StringUtil;

public class QAListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<QuestionInfoBean> data;
	private OnBtnClickListener onBtnClickListener;

	public QAListAdapter(Context context, ArrayList<QuestionInfoBean> data,
			OnBtnClickListener onBtnClickListener) {
		this.mContext = context;
		this.data = data;
		this.onBtnClickListener = onBtnClickListener;
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.qa_list_item, null);
			holder = new Holder();
			holder.usericon = (ImageView) convertView
					.findViewById(R.id.item_usericon);
			holder.username = (TextView) convertView
					.findViewById(R.id.item_username);
			holder.time = (TextView) convertView.findViewById(R.id.item_time);
			holder.question = (TextView) convertView
					.findViewById(R.id.question_tv);
			holder.reply = (TextView) convertView.findViewById(R.id.reply_tv);
			holder.answercount = (TextView) convertView
					.findViewById(R.id.commentcount_tv);

		} else {
			holder = (Holder) convertView.getTag();
		}
		// TODO 设置数据 设置监听
		final QuestionInfoBean questionInfoBean = data.get(position);
		ImageLoaderUtil.getInstance(mContext).displayImage(
				questionInfoBean.getUser_icon(), holder.usericon,
				ImageLoaderUtil.getUserIconImageOptions());
		
		holder.username.setText(questionInfoBean.getUser_name());
		holder.time.setText(StringUtil.formatLongDate(questionInfoBean.getTimes(), "yyyy-MM-dd"));
		holder.question.setText(questionInfoBean.getContent());
		holder.answercount.setText(questionInfoBean.getReply_num());

		OnClickListener onClickListenernew = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (onBtnClickListener != null) {

					switch (v.getId()) {
					case R.id.reply_tv:
						onBtnClickListener.onReplyBtnClick(position);
						break;
					case R.id.item_usericon:
						onBtnClickListener.onUserIconClick(questionInfoBean.getUser_id());
						break;

					default:
						break;
					}

				}
			}
		};

		holder.reply.setOnClickListener(onClickListenernew);
		holder.usericon.setOnClickListener(onClickListenernew);

		convertView.setTag(holder);
		return convertView;
	}

	private class Holder {
		ImageView usericon;
		TextView username;
		TextView time;
		TextView question;
		TextView reply;
		TextView answercount;
	}

	public interface OnBtnClickListener {
		void onReplyBtnClick(int position);

		void onUserIconClick(String userId);
	}

}
