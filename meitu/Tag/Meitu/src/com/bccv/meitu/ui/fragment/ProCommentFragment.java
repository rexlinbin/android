package com.bccv.meitu.ui.fragment;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bccv.meitu.R;
import com.bccv.meitu.model.Comment;
import com.bccv.meitu.ui.activity.ProZoneActivity;
import com.bccv.meitu.view.ListViewForScrollView;

public class ProCommentFragment extends Fragment {
	private ListViewForScrollView pro_comment_listView;
	private MyAdapter myAdapter;
	private Activity context;
	private List<Comment> commentsList;
	@Override
	public void onAttach(Activity activity) {
		this.context = activity;
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.promulgator_comment_list, container, false);
		pro_comment_listView = (ListViewForScrollView) view.findViewById(R.id.pro_comment_lv);
		myAdapter = new MyAdapter();
		pro_comment_listView.setAdapter(myAdapter);
//		ScreenUtil.setListViewHeightBasedOnChildren(pro_comment_listView);
		return view;
	}
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return commentsList == null ? 1 : commentsList.size();
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
			View view;
			ViewHolder holder;
			if(convertView != null){
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}else{
				holder = new ViewHolder();
				view = View.inflate(context, R.layout.promulgator_comment_list_item, null);
				holder.pro_iv_comment = (ImageView) view.findViewById(R.id.pro_iv_comment_item);
				holder.pro_tv_comment_name = (TextView) view.findViewById(R.id.pro_tv_comment_name);
				holder.pro_tv_comment_time = (TextView) view.findViewById(R.id.pro_tv_comment_time);
				holder.pro_tv_comment_datails = (TextView) view.findViewById(R.id.pro_tv_comment_datails);
				holder.pro_rl_comment = (RelativeLayout) view.findViewById(R.id.pro_rl_comment);
				holder.pro_tv_no_comment = (TextView) view.findViewById(R.id.pro_tv_no_comment);
				view.setTag(holder);
			}
			if(commentsList == null || commentsList.size() == 0){
				holder.pro_rl_comment.setVisibility(View.GONE);
				holder.pro_tv_no_comment.setVisibility(View.VISIBLE);
			}else{
				holder.pro_rl_comment.setVisibility(View.VISIBLE);
				holder.pro_tv_no_comment.setVisibility(View.GONE);
				// TODO 增加用户模块后加入评论
			}
			return view;
		}
	}
	
	public void setList(List<Comment> list,ProZoneActivity act){
		this.commentsList = list;
		myAdapter.notifyDataSetChanged();
		act.callBack();
	}
	
	class ViewHolder{
		ImageView pro_iv_comment;
		TextView pro_tv_comment_name;
		TextView pro_tv_comment_datails;
		TextView pro_tv_comment_time;
		RelativeLayout pro_rl_comment;
		TextView pro_tv_no_comment;
	}
	
}
