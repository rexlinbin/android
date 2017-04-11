package com.bccv.strategy.ui.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.strategy.R;
import com.bccv.strategy.api.NetWorkAPI;
import com.bccv.strategy.model.CommentInfoResBean2.CommentInfo;
import com.bccv.strategy.network.HttpCallback;
import com.bccv.strategy.network.NetResBean;
import com.bccv.strategy.sns.UserInfoManager;
import com.bccv.strategy.ui.activity.WebCommentActivity;
import com.bccv.strategy.utils.ImageLoaderUtil;
import com.bccv.strategy.utils.StringUtil;
import com.bccv.strategy.utils.SystemUtil;

public class WebCommentAdapter extends BaseAdapter {

	private WebCommentActivity context;
	private List<CommentInfo> comments;
	private static final int TYPE_ZERO = 0;
	private static final int TYPE_ONE = 1;
	
	public WebCommentAdapter(WebCommentActivity context) {
		this.context = context;
	}

	public void setList(List<CommentInfo> list) {
		this.comments = list;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return comments == null ? 0 : comments.size();
	}

	@Override
	public Object getItem(int position) {
		return comments == null ? 0 : comments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	@Override
	public int getItemViewType(int position) {
		return position == 0 ? TYPE_ZERO : TYPE_ONE;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
//		View view = null;
		LayoutInflater inflater = LayoutInflater.from(context);
		final ViewHolder holder ;
		int type = getItemViewType(position);
		if (convertView == null) {
			holder = new ViewHolder();
			switch (type) {
			case TYPE_ZERO:	
				convertView = inflater.inflate(R.layout.first_publish_item, null);
				holder.first_iv_icon = (ImageView) convertView.findViewById(R.id.first_iv_icon);
				holder.first_tv_name = (TextView) convertView.findViewById(R.id.first_tv_name);
				holder.first_tv_time = (TextView) convertView.findViewById(R.id.first_tv_time);
				holder.first_tv_comments_content = (TextView) convertView.findViewById(R.id.first_tv_comments_content);
				holder.first_iv_divider_line_two = convertView.findViewById(R.id.first_iv_divider_line_two);
				holder.first_tv_like_num = (TextView) convertView.findViewById(R.id.first_tv_like_num);
				holder.first_iv_like = (ImageView) convertView.findViewById(R.id.first_iv_like);
				holder.first_iv_pinglun = (ImageView) convertView.findViewById(R.id.first_iv_pinglun);
				holder.first_tv_pinglun_num = (TextView) convertView.findViewById(R.id.first_tv_pinglun_num);
				break;
			case TYPE_ONE:
				convertView = inflater.inflate(R.layout.comment_item_view, null);
				holder.icon = (ImageView) convertView.findViewById(R.id.comment_item_icon);
				holder.name = (TextView) convertView.findViewById(R.id.comment_item_name);
				holder.time = (TextView) convertView.findViewById(R.id.comment_item_time);
				holder.content = (TextView) convertView.findViewById(R.id.comment_item_content);
				holder.iv_zan = (ImageView) convertView.findViewById(R.id.comment_item_zan);
				holder.iv_pinglun = (ImageView) convertView.findViewById(R.id.comment_item_pinglun);
				holder.zan_num = (TextView) convertView.findViewById(R.id.comment_item_zan_num);
				holder.pinglun_num = (TextView) convertView.findViewById(R.id.comment_item_pinglun_num);
				break;
			default:
				break;
			}
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
			
		if (position != 0) {
			holder.name.setText(comments.get(position).user_name);
			holder.time.setText(StringUtil.formatLongDate((comments.get(position).times*1000),"yyyy-mm-dd"));
			holder.content.setText(comments.get(position).comment);
			holder.zan_num.setText(String.valueOf(comments.get(position).digg));
			holder.pinglun_num.setText(String.valueOf(comments.get(position).is_reply));
			ImageLoaderUtil.getInstance(context).displayImage(comments.get(position).user_icon, holder.icon
					,ImageLoaderUtil.getUserIconImageOptions());
			holder.icon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					context.intent2Person(String.valueOf(comments.get(position).user_id));
				}
			});
			
			if (comments.get(position).is_digg) {
				holder.iv_zan.setBackgroundResource(R.drawable.zan_press);
			}else {
				holder.iv_zan.setBackgroundResource(R.drawable.zan);
			}
			
			holder.iv_zan.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (UserInfoManager.isLogin()) {
						if (!comments.get(position).is_digg){
							holder.iv_zan.setBackgroundResource(R.drawable.zan_press);
							holder.zan_num.setText(String.valueOf(Integer.valueOf(holder.zan_num.getText().toString())+1));
							comments.get(position).is_digg = true;
							digg(comments.get(position).id);
						}else {
							Toast.makeText(context, "已赞", Toast.LENGTH_SHORT).show();
						}
					}else {
						Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}else {
			final CommentInfo comBean  = comments.get(position);
			ImageLoaderUtil.getInstance(context).displayImage(comBean.user_icon, holder.first_iv_icon,
					ImageLoaderUtil.getUserIconImageOptions());
			holder.first_tv_name.setText(comBean.user_name);
			holder.first_tv_time.setText(StringUtil.formatLongDate((comBean.times*1000),"yyyy-mm-dd"));
			holder.first_tv_comments_content.setText(comBean.comment);
			holder.first_tv_pinglun_num.setText(""+comBean.is_reply);
			holder.first_tv_like_num.setText(String.valueOf(comBean.digg));
			if (comBean.is_digg) {
				holder.first_iv_like.setBackgroundResource(R.drawable.zan_press);
			}else {
				holder.first_iv_like.setBackgroundResource(R.drawable.zan);
			}
			holder.first_iv_like.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (UserInfoManager.isLogin()) {
						if (!comBean.is_digg) {
							holder.first_iv_like.setBackgroundResource(R.drawable.zan_press);
							comBean.is_digg = true;
							holder.first_tv_like_num.setText(String.valueOf(
									Integer.valueOf(holder.first_tv_like_num.getText().toString())+1));
							digg(comBean.id);
						}else {
							Toast.makeText(context, "已赞", Toast.LENGTH_SHORT).show();
						}
					}else
						Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
				}
			});
			
			holder.first_iv_icon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					context.intent2Person(String.valueOf(comBean.user_id));
				}
			});
			
			holder.first_iv_divider_line_two.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	public class ViewHolder{
		ImageView first_iv_like;
		ImageView first_iv_pinglun;
		ImageView iv_zan;
		ImageView iv_pinglun;
		TextView zan_num;
		TextView pinglun_num;
		ImageView icon;
		TextView time;
		TextView name;
		TextView content;
		
		TextView first_tv_name;
		TextView first_tv_time;
		TextView first_tv_comments_content;
		View first_iv_divider_line_two;
		TextView first_tv_like_num;
		TextView first_tv_pinglun_num;
		ImageView first_iv_icon;
	}
	
	private void digg(String comment_id) {
		if (SystemUtil.isNetOkWithToast(context)) {
			NetWorkAPI.digg_c(context, String.valueOf(comment_id), new HttpCallback() {
				
				@Override
				public void onResult(NetResBean response) {
					if (response.success) {
						Toast.makeText(context, "Digg succeed!", Toast.LENGTH_SHORT).show();
					}else {
						Toast.makeText(context, "Digg failed!", Toast.LENGTH_SHORT).show();
					}
				}
				
				@Override
				public void onError(String errorMsg) {
				}
				
				@Override
				public void onCancel() {
				}
			});
		}
	}
}
