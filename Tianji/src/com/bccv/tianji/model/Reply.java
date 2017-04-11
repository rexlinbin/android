package com.bccv.tianji.model;

import java.io.Serializable;

public class Reply implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * comment_id---------评论ID
	             user_name----------用户名称
	             user_icon----------用户头像
	             comment------------评论内容
	             times----------评论内容
	             reply----------若为0   则用户发表的此条评论是主楼，该条的reply_list均为回复用户的评论
	                            若不为0 则用户发表的此条评论是回复其他用的回复，该条的reply_list有切只有一条，也就是该用户          回复评论的回复
	             title----------游戏名称
	             reply_more-----是否还有更多评论
	             reply_list-----回复列表（回复列表内不会再有reply字段）
	 */
		
		private String comment_id;
		private String user_name;
		private String user_icon;
		private String comment;
		private String times;
		private String reply;
		private String title;
		private String nick_name;
		public String getComment_id() {
			return comment_id;
		}
		public void setComment_id(String comment_id) {
			this.comment_id = comment_id;
		}
		public String getUser_name() {
			return user_name;
		}
		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}
		public String getUser_icon() {
			return user_icon;
		}
		public void setUser_icon(String user_icon) {
			this.user_icon = user_icon;
		}
		public String getComment() {
			return comment;
		}
		public void setComment(String comment) {
			this.comment = comment;
		}
		public String getTimes() {
			return times;
		}
		public void setTimes(String times) {
			this.times = times;
		}
		public String getReply() {
			return reply;
		}
		public void setReply(String reply) {
			this.reply = reply;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getNick_name() {
			return nick_name;
		}
		public void setNick_name(String nick_name) {
			this.nick_name = nick_name;
		}
		
		
}
