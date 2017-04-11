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
import com.bccv.bangyangapp.model.MyCommentItemBean;
import com.bccv.bangyangapp.utils.ImageLoaderUtil;

public class MyQACommentChildListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<MyCommentItemBean> data;
	private OnBtnClickListener onBtnClickListener;
	
	public MyQACommentChildListAdapter(Context context,ArrayList<MyCommentItemBean> data){
		this.mContext = context;
		this.data = data;
	}
	
	public void setOnBtnClickListener(OnBtnClickListener onBtnClickListener){
		this.onBtnClickListener = onBtnClickListener;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data==null?0:data.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder;
		if(convertView == null){
			convertView = View.inflate(mContext, R.layout.myqa_comment_item_view, null);
			holder = new Holder();
			holder.icon = (ImageView) convertView.findViewById(R.id.qa_item_iv);
			holder.question = (TextView) convertView.findViewById(R.id.qa_item_question_tv);
			holder.answer = (TextView) convertView.findViewById(R.id.qa_item_answer_tv);
		}else{
			holder = (Holder) convertView.getTag();
		}
		//TODO 设置数据
		final MyCommentItemBean myCommentItemBean = data.get(position);
		ImageLoaderUtil.getInstance(mContext).displayImage(myCommentItemBean.getUser_icon(), holder.icon, ImageLoaderUtil.getUserIconImageOptions());
		holder.question.setText(myCommentItemBean.getQuestions());
		holder.answer.setText(myCommentItemBean.getAnswer());
		
		
		holder.icon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(onBtnClickListener!=null){
					onBtnClickListener.onUserIconClick(myCommentItemBean.getUser_id());
				}
			}
		});
		
		convertView.setTag(holder);
		return convertView;
	}

	
	private class Holder{
		ImageView icon;
		TextView question;
		TextView answer;
	}
	
	public interface OnBtnClickListener{
		void onUserIconClick(String userId);
	}
}
