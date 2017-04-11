package com.bccv.meitu.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bccv.meitu.R;
import com.bccv.meitu.model.Comment;
import com.bccv.meitu.ui.activity.CommentActivity;
import com.bccv.meitu.utils.GetTimeUtil;
import com.bccv.meitu.utils.StringUtil;
import com.nostra13.universalimageloader.utils.ImageLoaderUtil;

public class CommentListAdapter extends BaseAdapter {
	private CommentActivity activity;
	private List<Comment> comList = new ArrayList<Comment>();
	public CommentListAdapter(CommentActivity act) {
		this.activity = act;
	}
	
	@Override
	public int getCount() {
		return comList == null ? 1 : comList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			view = View.inflate(activity, R.layout.comment_list_item, null);
			holder.rl_comment = (RelativeLayout) view.findViewById(R.id.rl_comment);
			holder.comment_item_iv_icon = (ImageView) view.findViewById(R.id.comment_item_iv_icon);
			holder.comment_item_see_more = (ImageView) view.findViewById(R.id.comment_item_see_more);
			holder.comment_item_tv_name = (TextView) view.findViewById(R.id.comment_item_tv_name);
			holder.comment_item_tv_comment = (TextView) view.findViewById(R.id.comment_item_tv_comment);
			holder.comment_item_tv_time = (TextView) view.findViewById(R.id.comment_item_tv_time);
			holder.comment_nothing = (TextView) view.findViewById(R.id.comment_nothing);
			view.setTag(holder);
		}else{
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		
		if (comList == null || comList.size() == 0) {
			holder.rl_comment.setVisibility(View.GONE);
//			holder.comment_nothing.setVisibility(View.VISIBLE);
		}else{
			getData(holder,position);
			ImageLoaderUtil.getInstance(activity).displayImage(
					holder.icon, holder.comment_item_iv_icon, ImageLoaderUtil.getRoundedImageOptions());
			holder.comment_item_tv_name.setText(holder.user_name);
//			holder.comment_item_tv_time.setText(StringUtil.friendly_time(String.valueOf(holder.ctime)));
			holder.comment_item_tv_time.setText(GetTimeUtil.getTime(holder.ctime));
			holder.comment_item_tv_comment.setText(holder.content);
		}
		return view;
	}
	
	private void getData(ViewHolder holder,int position) {
		Comment comment = comList.get(position);
		if (comment != null) {
			holder.content = comment.getComments_content();
			holder.ctime = comment.getCtime();
			holder.id = comment.getId();
			holder.icon = comment.getUser_icon();
			holder.user_name = comment.getUser_name();
		}
	}

	class ViewHolder{
		RelativeLayout rl_comment;
		ImageView comment_item_iv_icon;
		ImageView comment_item_see_more;
		TextView comment_item_tv_name;
		TextView comment_item_tv_comment;
		TextView comment_item_tv_time;
		TextView comment_nothing;
		String content;
		int ctime;
		int id;
		String icon;
		String user_name;
	}

	public void setList(List<Comment> commentList,boolean isRefresh) {
		if(commentList != null && commentList.size() > 0){
			
			if (isRefresh) {
				this.comList.clear();
			}
			if (comList == null) {
				comList = new ArrayList<Comment>();
			}
			this.comList.addAll(commentList);
			
			this.notifyDataSetChanged();
			
		}
	}
}
