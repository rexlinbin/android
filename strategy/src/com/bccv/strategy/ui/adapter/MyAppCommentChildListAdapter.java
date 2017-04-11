package com.bccv.strategy.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bccv.strategy.model.MyCommentItemBean;
import com.bccv.strategy.utils.ImageLoaderUtil;
import com.bccv.strategy.R;

public class MyAppCommentChildListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<MyCommentItemBean> data;
	private OnBtnClickListener onBtnClickListener;
	
	public MyAppCommentChildListAdapter(Context context,ArrayList<MyCommentItemBean> data){
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
			convertView = View.inflate(mContext, R.layout.myapp_comment_item, null);
			holder = new Holder();
			holder.icon = (ImageView) convertView.findViewById(R.id.myapp_comment_iv);
			holder.comment = (TextView) convertView.findViewById(R.id.myapp_comment_tv);
			
		}else{
			holder = (Holder) convertView.getTag();
		}
		
		//TODO 设置数据
		final MyCommentItemBean myCommentItemBean = data.get(position);
		ImageLoaderUtil.getInstance(mContext).displayImage(myCommentItemBean.getGame_icon(), holder.icon, ImageLoaderUtil.getAppIconImageOptions());
		holder.comment.setText(myCommentItemBean.getComment());
		
		holder.icon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(onBtnClickListener!=null){
					onBtnClickListener.onAppIconClick(myCommentItemBean.getGame_id());
				}
				
			}
		});
		
		convertView.setTag(holder);
		return convertView;
	}

	
	private class Holder{
		ImageView icon;
		TextView comment;
	}
	
	public interface OnBtnClickListener{
		void onAppIconClick(String appid);
	}
	
}
