package com.bccv.zhuiyingzhihanju.adapter;

import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.activity.TypeInfoActivity;
import com.bccv.zhuiyingzhihanju.activity.Video2DPlayerActivity;
import com.bccv.zhuiyingzhihanju.activity.VideoInfoActivity;
import com.bccv.zhuiyingzhihanju.model.Comment;
import com.bccv.zhuiyingzhihanju.model.Msg;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.tools.GlobalParams;
import com.utils.tools.StringUtils;
import com.utils.views.CircleImageView;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TypeInfoCommentAdapter extends BaseAdapter {
	private Context context;
	private List<Comment> list;
	private Activity activity;
	private boolean isInfo=false;

	public TypeInfoCommentAdapter(Context context, Activity activity, List<Comment> list) {
		super();
		this.context = context;
		this.list = list;
		this.activity = activity;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub0
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

	public boolean isInfo() {
		return isInfo;
	}

	public void setInfo(boolean isInfo) {
		this.isInfo = isInfo;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			
		if(isInfo){
			
			convertView = View.inflate(context, R.layout.listitem_videoinfo_comm, null);
		}else{
			
			convertView = View.inflate(context, R.layout.listitem_typeinfo_comm, null);
		}
			
			viewHolder.likeImageView = (ImageView) convertView.findViewById(R.id.like_imageView);
			viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.title_textView);
			viewHolder.timeTextView = (TextView) convertView.findViewById(R.id.time_textView);
			viewHolder.contentTextView = (TextView) convertView.findViewById(R.id.content_textView);
			viewHolder.likeNumTextView = (TextView) convertView.findViewById(R.id.likeNum_textView);
			viewHolder.replyTextView = (TextView) convertView.findViewById(R.id.reply_textView);
			viewHolder.otherImageView = (ImageView) convertView.findViewById(R.id.other_imageView);
			viewHolder.headImageView = (CircleImageView) convertView.findViewById(R.id.circleImageView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final Comment item = list.get(position);

		viewHolder.titleTextView.setText(item.getUser_info().getNick_name());
		viewHolder.contentTextView.setText(item.getContent());

		Long time = item.getCtime();
		viewHolder.timeTextView.setText(item.getLocaltion()+"  "+item.getPlatform()+"   "+StringUtils.friendly_time(time * 1000) + "");

		ImageLoader iconLoader = ImageLoader.getInstance();
		iconLoader.displayImage(item.getUser_info().getAvatars(), viewHolder.headImageView, GlobalParams.iconOptions);

		if (item.getF_user_info() == null) {
			viewHolder.replyTextView.setVisibility(View.GONE);
		} else {
			viewHolder.replyTextView.setVisibility(View.VISIBLE);
			String reply = "<font color='#44b549'>" + item.getF_user_info().getNick_name() + "</font>:"
					+ item.getF_comment_info();
			viewHolder.replyTextView.setText(Html.fromHtml(reply));
		}
		
		viewHolder.likeNumTextView.setText(item.getDigg() + "");
		viewHolder.likeImageView.setSelected(item.isSelect());
		final ImageView imageView = viewHolder.likeImageView;
		final TextView textView = viewHolder.likeNumTextView;
		viewHolder.likeImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!imageView.isSelected()) {
					imageView.setSelected(true);
					item.setSelect(true);
					item.setDigg(item.getDigg() + 1);
					textView.setText(item.getDigg() + "");
					if(isInfo){
						((VideoInfoActivity) activity).diggMovie(item.getId());
					}else{
						((TypeInfoActivity) activity).diggMovie(item.getId());
					}
					
				}
			}
		});

		viewHolder.otherImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(isInfo){
					((VideoInfoActivity) activity).showDot(position);
				}
				else{
					((TypeInfoActivity) activity).showDot(position);
				}
				
			}
		});
		return convertView;
	}

	class ViewHolder {
		CircleImageView headImageView;
		ImageView likeImageView;

		TextView titleTextView;
		TextView contentTextView;
		TextView timeTextView;
		TextView likeNumTextView;
		TextView replyTextView;
		ImageView otherImageView;
	}

}
