package com.bccv.tianji.model;

import java.io.Serializable;
import java.util.List;

public class CommentReply implements Serializable{
	/***
	 * status----------系统状态码
	 *  total-----------总页数 
	 *  data------------分类列表
	 * comment_id-------------评论ID 
	 * user_name------用户名称 
	 * user_icon------用户头像
	 * comment--------评论内容 
	 * times----------评论时间 
	 * reply_more-----是否还有更多评论
	 * reply----------本条的回复 如果有 只显示一条！ 
	 * id---------评论ID 
	 * user_name--用户名称
	 * comment----回复内容
	 ****/

	private static final long serialVersionUID = 1L;
	private String comment_id;
	private String user_name;
	private String user_icon;
	private String comment;
	private String times;
	private String reply_more;
	private String nick_name;
	private List<Reply> reply;
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
	public String getReply_more() {
		return reply_more;
	}
	public void setReply_more(String reply_more) {
		this.reply_more = reply_more;
	}
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}
	public List<Reply> getReply() {
		return reply;
	}
	public void setReply(List<Reply> reply) {
		this.reply = reply;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
