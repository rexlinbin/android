package com.bccv.bangyangapp.ui.adapter;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bccv.bangyangapp.R;
import com.bccv.bangyangapp.model.QuestionInfoBean;
import com.bccv.bangyangapp.ui.activity.PersonalZoneActivity;
import com.bccv.bangyangapp.utils.ImageLoaderUtil;
import com.bccv.bangyangapp.utils.StringUtil;

public class MyQuestionAdapter extends BaseAdapter {

	private PersonalZoneActivity mContext;
	private ArrayList<QuestionInfoBean> questions;
	
	public MyQuestionAdapter(PersonalZoneActivity context,ArrayList<String> data){
		this.mContext = context;
	}
	
	@Override
	public int getCount() {
		return questions==null?0:questions.size();
	}
	
	@Override
	public Object getItem(int position) {
		return questions == null ? null : questions.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if(convertView == null){
			convertView = View.inflate(mContext, R.layout.my_qa_list_item, null);
			holder = new Holder();
			holder.usericon = (ImageView) convertView.findViewById(R.id.my_question_item_usericon);
			holder.username = (TextView) convertView.findViewById(R.id.my_question_item_username);
			holder.time = (TextView) convertView.findViewById(R.id.my_question_item_time);
			holder.question = (TextView) convertView.findViewById(R.id.my_question_question_tv);
			holder.reply = (TextView) convertView.findViewById(R.id.my_question_reply_tv);
			holder.comment_count = (TextView) convertView.findViewById(R.id.my_question_commentcount_tv);
			
		}else{
			holder = (Holder) convertView.getTag();
		}
		
		if (questions != null && questions.size() > 0) {
			QuestionInfoBean infoBean = questions.get(position);
			ImageLoaderUtil.getInstance(mContext).displayImage(infoBean.getUser_icon(), holder.usericon);
			holder.username.setText(infoBean.getUser_name());
			holder.question.setText(infoBean.getContent());
			holder.comment_count.setText(infoBean.getReply_num());
			holder.time.setText(StringUtil.formatLongDate(infoBean.getTimes(), "yyyy-mm-dd"));
		}
		
		//TODO 设置数据  设置监听
		holder.reply.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mContext.getLayout().setVisibility(View.VISIBLE);
			}
		});
		
		
		convertView.setTag(holder);
		return convertView;
	}

	
	private class Holder{
		ImageView usericon;
		TextView username;
		TextView time;
		TextView question;
		TextView reply;
		TextView comment_count;
	}


	public void setList(ArrayList<QuestionInfoBean> questions) {
		this.questions = questions;
	}
	
}
