package com.bccv.bangyangapp.ui.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.bangyangapp.R;
import com.bccv.bangyangapp.api.NetWorkAPI;
import com.bccv.bangyangapp.model.CommentBean;
import com.bccv.bangyangapp.model.ReplyBean2;
import com.bccv.bangyangapp.network.HttpCallback;
import com.bccv.bangyangapp.network.NetResBean;
import com.bccv.bangyangapp.sns.UserInfoManager;
import com.bccv.bangyangapp.ui.activity.AppDetailsActivity;
import com.bccv.bangyangapp.utils.ImageLoaderUtil;
import com.bccv.bangyangapp.utils.StringUtil;
import com.bccv.bangyangapp.utils.SystemUtil;

public class AppDetailsAdapter extends BaseAdapter {

	private AppDetailsActivity context;
	private List<CommentBean> comments;
	
	public AppDetailsAdapter(AppDetailsActivity context) {
		this.context = context;
	}

	public void setList(List<CommentBean> list) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = null;
		LayoutInflater inflater = LayoutInflater.from(context);
		if (position == 0) {
			view = inflater.inflate(R.layout.first_publish_item, null);
			final CommentBean comBean  = comments.get(position);
			List<ReplyBean2> reply  = comBean.getReply();
			//head
			ImageView first_iv_icon = (ImageView) view.findViewById(R.id.first_iv_icon);
			TextView first_tv_name = (TextView) view.findViewById(R.id.first_tv_name);
			TextView first_tv_time = (TextView) view.findViewById(R.id.first_tv_time);
			TextView first_tv_comments_content = (TextView) view.findViewById(R.id.first_tv_comments_content);
			ImageView first_iv_divider_line = (ImageView) view.findViewById(R.id.first_iv_divider_line);
			View first_iv_divider_line_two = view.findViewById(R.id.first_iv_divider_line_two);
			
			ImageLoaderUtil.getInstance(context).displayImage(comBean.getUser_icon(), first_iv_icon,
					ImageLoaderUtil.getUserIconImageOptions());
			first_tv_name.setText(comBean.getUser_name());
			first_tv_time.setText(StringUtil.formatLongDate(
					Integer.valueOf(comBean.getTimes())*1000,"yyyy-mm-dd"));
			first_tv_comments_content.setText(comBean.getComment());
			
			final TextView first_tv_like_num = (TextView) view.findViewById(R.id.first_tv_like_num);
			first_tv_like_num.setText(String.valueOf(comBean.getDigg()));
			final ImageView first_iv_like = (ImageView) view.findViewById(R.id.first_iv_like);
			if (comBean.getIs_digg() != 0) {
				first_iv_like.setBackgroundResource(R.drawable.zan_press);
//				first_tv_like_num.setText(String.valueOf(comBean.getDigg()+1));
			}else {
				first_iv_like.setBackgroundResource(R.drawable.zan);
			}
			first_iv_like.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (UserInfoManager.isLogin()) {
						if (comBean.getIs_digg() == 0) {
//						first_iv_like.setBackgroundResource(R.drawable.zan);
//						comBean.setIs_digg(0);
//						first_tv_like_num.setText(String.valueOf(
//								Integer.valueOf(first_tv_like_num.getText().toString())-1));
							first_iv_like.setBackgroundResource(R.drawable.zan_press);
							comBean.setIs_digg(1);
							first_tv_like_num.setText(String.valueOf(
									Integer.valueOf(first_tv_like_num.getText().toString())+1));
							digg(comBean.getId());
						}else {
							Toast.makeText(context, "已赞", Toast.LENGTH_SHORT).show();
						}
					}else
						Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
				}
			});
			
			first_iv_icon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					context.intent2Person(String.valueOf(comBean.getUser_id()));
				}
			});
			
			if (reply != null && reply.size() > 0) {
				first_iv_divider_line.setVisibility(View.VISIBLE);
				if (reply.size() >= 3) {
					LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_reply_more);
					ll.setVisibility(View.VISIBLE);
				}
				for (int i = 0; i < reply.size(); i++) {
					final ReplyBean2 replyBean = reply.get(i);
					switch (i) {
					case 0:
						//one
						RelativeLayout first_reply = (RelativeLayout) view.findViewById(R.id.first_reply);
						ImageView comment_item_icon = (ImageView) view.findViewById(R.id.comment_item_icon);
						ImageLoaderUtil.getInstance(context).displayImage(replyBean.getUser_icon(), comment_item_icon
								,ImageLoaderUtil.getUserIconImageOptions());
						comment_item_icon.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								context.intent2Person(String.valueOf(replyBean.getUser_id()));
							}
						});
						TextView comment_item_name = (TextView) view.findViewById(R.id.comment_item_name);
						comment_item_name.setText(replyBean.getUser_name());
						TextView comment_item_time = (TextView) view.findViewById(R.id.comment_item_time);
						comment_item_time.setText(StringUtil.formatLongDate(
										Integer.valueOf(replyBean.getTimes())*1000,"yyyy-mm-dd"));
						TextView comment_item_content = (TextView) view.findViewById(R.id.comment_item_content);
						comment_item_content.setText(replyBean.getComment());
						final TextView comment_item_zan_num = (TextView) view.findViewById(R.id.comment_item_zan_num);
						comment_item_zan_num.setText(String.valueOf(replyBean.getDigg()));
						final ImageView comment_item_zan = (ImageView) view.findViewById(R.id.comment_item_zan);
						if (replyBean.getIs_digg() != 0) {
							comment_item_zan.setBackgroundResource(R.drawable.zan_press);
//							comment_item_zan_num.setText(String.valueOf(replyBean.getDigg()+1));
						}else {
							comment_item_zan.setBackgroundResource(R.drawable.zan);
						}
						comment_item_zan.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								if (UserInfoManager.isLogin()) {
									if (replyBean.getIs_digg() == 0) {
//									comment_item_zan.setBackgroundResource(R.drawable.zan);
//									replyBean.setIs_digg(0);
//									comment_item_zan_num.setText(String.valueOf
//											(Integer.valueOf(comment_item_zan_num.getText().toString())-1));
										comment_item_zan.setBackgroundResource(R.drawable.zan_press);
										replyBean.setIs_digg(1);
										comment_item_zan_num.setText(String.valueOf
												(Integer.valueOf(comment_item_zan_num.getText().toString())+1));
										digg(replyBean.getId());
									}else {
										Toast.makeText(context, "已赞", Toast.LENGTH_SHORT).show();
									}
								}else {
									Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
								}
							}
						});
						first_reply.setVisibility(View.VISIBLE);
						break;
					case 1:
						RelativeLayout second_reply = (RelativeLayout) view.findViewById(R.id.second_reply);
						ImageView comment_item_icon_two = (ImageView) view.findViewById(R.id.comment_item_icon_two);
						ImageLoaderUtil.getInstance(context).displayImage(replyBean.getUser_icon(), comment_item_icon_two
								,ImageLoaderUtil.getUserIconImageOptions());
						comment_item_icon_two.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								context.intent2Person(String.valueOf(replyBean.getUser_id()));
							}
						});
						TextView comment_item_name_two = (TextView) view.findViewById(R.id.comment_item_name_two);
						comment_item_name_two.setText(replyBean.getUser_name());
						TextView comment_item_time_two = (TextView) view.findViewById(R.id.comment_item_time_two);
						comment_item_time_two.setText(StringUtil.formatLongDate(
								Integer.valueOf(replyBean.getTimes())*1000,"yyyy-mm-dd"));
						TextView comment_item_content_two = (TextView) view.findViewById(R.id.comment_item_content_two);
						comment_item_content_two.setText(replyBean.getComment());
						final TextView comment_item_zan_num_two = (TextView) view.findViewById(R.id.comment_item_zan_num_two);
						comment_item_zan_num_two.setText(String.valueOf(replyBean.getDigg()));
						final ImageView comment_item_zan_two = (ImageView) view.findViewById(R.id.comment_item_zan_two);
						if (replyBean.getIs_digg() != 0) {
							comment_item_zan_two.setBackgroundResource(R.drawable.zan_press);
//							comment_item_zan_num_two.setText(String.valueOf(replyBean.getDigg()+1));
						}else {
							comment_item_zan_two.setBackgroundResource(R.drawable.zan);
						}
						comment_item_zan_two.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								if (UserInfoManager.isLogin()) {
									if (replyBean.getIs_digg() == 0) {
//									comment_item_zan_two.setBackgroundResource(R.drawable.zan);
//									replyBean.setIs_digg(0);
//									comment_item_zan_num_two.setText(String.valueOf
//											(Integer.valueOf(comment_item_zan_num_two.getText().toString())-1));
										comment_item_zan_two.setBackgroundResource(R.drawable.zan_press);
										replyBean.setIs_digg(1);
										comment_item_zan_num_two.setText(String.valueOf
												(Integer.valueOf(comment_item_zan_num_two.getText().toString())+1));
										digg(replyBean.getId());
									}else {
										Toast.makeText(context, "已赞", Toast.LENGTH_SHORT).show();
									}
								}else {
									Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
								}
							}
						});
						second_reply.setVisibility(View.VISIBLE);
						break;
					case 2:
						RelativeLayout third_reply = (RelativeLayout) view.findViewById(R.id.third_reply);
						ImageView comment_item_icon_three = (ImageView) view.findViewById(R.id.comment_item_icon_three);
						ImageLoaderUtil.getInstance(context).displayImage(replyBean.getUser_icon(), comment_item_icon_three
								,ImageLoaderUtil.getUserIconImageOptions());
						comment_item_icon_three.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								context.intent2Person(String.valueOf(replyBean.getUser_id()));
							}
						});
						TextView comment_item_name_three = (TextView) view.findViewById(R.id.comment_item_name_three);
						comment_item_name_three.setText(replyBean.getUser_name());
						TextView comment_item_time_three = (TextView) view.findViewById(R.id.comment_item_time_three);
						comment_item_time_three.setText(StringUtil.formatLongDate(
								Integer.valueOf(replyBean.getTimes())*1000,"yyyy-mm-dd"));
						TextView comment_item_content_three = (TextView) view.findViewById(R.id.comment_item_content_three);
						comment_item_content_three.setText(replyBean.getComment());
						final TextView comment_item_zan_num_three = (TextView) view.findViewById(R.id.comment_item_zan_num_three);
						comment_item_zan_num_three.setText(String.valueOf(replyBean.getDigg()));
						final ImageView comment_item_zan_three = (ImageView) view.findViewById(R.id.comment_item_zan_three);
						if (replyBean.getIs_digg() != 0) {
							comment_item_zan_three.setBackgroundResource(R.drawable.zan_press);
//							comment_item_zan_num_three.setText(String.valueOf(replyBean.getDigg()+1));
						}else {
							comment_item_zan_three.setBackgroundResource(R.drawable.zan);
						}
						comment_item_zan_three.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								if (UserInfoManager.isLogin()) {
//									comment_item_zan_three.setBackgroundResource(R.drawable.zan);
//									replyBean.setIs_digg(0);
//									comment_item_zan_num_three.setText(String.valueOf
//											(Integer.valueOf(comment_item_zan_num_three.getText().toString())-1));
									if (replyBean.getIs_digg() == 0) {
										comment_item_zan_three.setBackgroundResource(R.drawable.zan_press);
										replyBean.setIs_digg(1);
										comment_item_zan_num_three.setText(String.valueOf
												(Integer.valueOf(comment_item_zan_num_three.getText().toString())+1));
										digg(replyBean.getId());
									}else {
										Toast.makeText(context, "已赞", Toast.LENGTH_SHORT).show();
									}
								}else {
									Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
								}
							}
						});
						third_reply.setVisibility(View.VISIBLE);
						break;
					default:
//						TextView tv_more = (TextView) view.findViewById(R.id.details_reply_more);
//						tv_more.setOnClickListener(new OnClickListener() {
//							@Override
//							public void onClick(View v) {
//								
//							}
//						});
						break;
					}
				}
			}else {
				first_iv_divider_line_two.setVisibility(View.VISIBLE);
			}
		}else {
			final ViewHolder holder ;
			if (convertView != null && convertView.getTag() != null) {
				view = convertView;
				holder = (ViewHolder) convertView.getTag();
			}else {
				holder = new ViewHolder();
				view = inflater.inflate(R.layout.comment_item_view, null);
				holder.icon = (ImageView) view.findViewById(R.id.comment_item_icon);
				holder.name = (TextView) view.findViewById(R.id.comment_item_name);
				holder.time = (TextView) view.findViewById(R.id.comment_item_time);
				holder.content = (TextView) view.findViewById(R.id.comment_item_content);
				holder.iv_zan = (ImageView) view.findViewById(R.id.comment_item_zan);
				holder.zan_num = (TextView) view.findViewById(R.id.comment_item_zan_num);
				view.setTag(holder);
			}
			
			holder.name.setText(comments.get(position).getUser_name());
			holder.time.setText(StringUtil.formatLongDate(
					Integer.valueOf(comments.get(position).getTimes())*1000,"yyyy-mm-dd"));
			holder.content.setText(comments.get(position).getComment());
			holder.zan_num.setText(String.valueOf(comments.get(position).getDigg()));
			ImageLoaderUtil.getInstance(context).displayImage(comments.get(position).getUser_icon(), holder.icon
					,ImageLoaderUtil.getUserIconImageOptions());
			holder.icon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					context.intent2Person(String.valueOf(comments.get(position).getUser_id()));
				}
			});
			
			if (comments.get(position).getIs_digg() != 0) {
				holder.iv_zan.setBackgroundResource(R.drawable.zan_press);
			}else {
				holder.iv_zan.setBackgroundResource(R.drawable.zan);
			}
			
			holder.iv_zan.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (UserInfoManager.isLogin()) {
						if (comments.get(position).getIs_digg() == 0){
							holder.iv_zan.setBackgroundResource(R.drawable.zan_press);
							holder.zan_num.setText(String.valueOf(Integer.valueOf(holder.zan_num.getText().toString())+1));
							comments.get(position).setIs_digg(1);
							digg(comments.get(position).getId());
						}else {
							Toast.makeText(context, "已赞", Toast.LENGTH_SHORT).show();
						}
					}else {
						Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
		return view;
	}

	public class ViewHolder{
		ImageView first_iv_like;
		ImageView iv_zan;
		TextView zan_num;
		ImageView icon;
		TextView time;
		TextView name;
		TextView content;
	}
	
	private void digg(int comment_id) {
		if (SystemUtil.isNetOkWithToast(context)) {
			NetWorkAPI.digg(context, String.valueOf(comment_id), new HttpCallback() {
				
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
