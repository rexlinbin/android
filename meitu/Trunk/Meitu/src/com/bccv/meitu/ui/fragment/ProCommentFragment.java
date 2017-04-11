package com.bccv.meitu.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.meitu.R;
import com.bccv.meitu.model.Comment;
import com.bccv.meitu.ui.activity.ProZoneActivity;
import com.bccv.meitu.utils.GetTimeUtil;
import com.bccv.meitu.view.ListViewForScrollView;
import com.nostra13.universalimageloader.utils.ImageLoaderUtil;

public class ProCommentFragment extends Fragment {
	private ListViewForScrollView pro_comment_listView;
	private MyAdapter myAdapter;
	private Activity context;
	private ProZoneActivity activity;
	private List<Comment> commentsList = new ArrayList<Comment>();
	private ImageView pro_tv_get_more;
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
		
		View viewFooter = inflater.inflate(R.layout.pro_album_list_footer, null);
		pro_tv_get_more = (ImageView) viewFooter.findViewById(R.id.pro_tv_get_more);
		pro_tv_get_more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(totalPage - curPage > 0){
					activity.getComments(curPage + 1);
				}else{
					Toast.makeText(activity, "暂无更多数据", Toast.LENGTH_SHORT).show();
				}
			}
		});
		if(curPage == -1 || totalPage == -1){
			pro_tv_get_more.setVisibility(View.GONE);
		}
		pro_comment_listView.addFooterView(viewFooter);
		
		myAdapter = new MyAdapter();
		pro_comment_listView.setAdapter(myAdapter);
		return view;
	}
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if (commentsList == null || commentsList.size() == 0) {
				return 1;
			}else {
				return commentsList.size();
			}
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
				getAllData(holder,position);
				ImageLoaderUtil.getInstance(getActivity()).displayImage(holder.user_icon, holder.pro_iv_comment
						,ImageLoaderUtil.getRoundedImageOptions());
				holder.pro_tv_comment_name.setText(holder.user_name);
				holder.pro_tv_comment_datails.setText(String.valueOf(holder.content));
				holder.pro_tv_comment_time.setText(GetTimeUtil.getTime(holder.ctime));
			}
			return view;
		}
		
		private void getAllData(ViewHolder holder,int position){
			holder.content = commentsList.get(position).getComments_content();
			holder.ctime = commentsList.get(position).getCtime();
			holder.id = commentsList.get(position).getId();
			holder.user_icon = commentsList.get(position).getUser_icon();
			holder.user_name = commentsList.get(position).getUser_name();
		}
	}
	
	class ViewHolder{
		String content;
		int ctime;
		int id;
		String user_icon;
		String user_name;
		ImageView pro_iv_comment;
		TextView pro_tv_comment_name;
		TextView pro_tv_comment_datails;
		TextView pro_tv_comment_time;
		RelativeLayout pro_rl_comment;
		TextView pro_tv_no_comment;
	}
	
	private int curPage = -1;
	private int totalPage = -1;
	public void setList(List<Comment> list,int curPage2,int totalPage2,ProZoneActivity act){
		if (list != null && list.size() > 0) {
			
			this.commentsList.addAll(list);
			
			if (myAdapter != null) {
				myAdapter.notifyDataSetChanged();
			}else{
				Log.e("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!", "adapter is null");
				return ;
			}
			activity = act;
			curPage = curPage2;
			totalPage = totalPage2;
			activity.callBack();
			if(curPage == -1 || totalPage == -1 || curPage == totalPage){
				pro_tv_get_more.setVisibility(View.GONE);
			}else{
				pro_tv_get_more.setVisibility(View.VISIBLE);
			}
		}
	}
	
}
