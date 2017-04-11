package com.bccv.strategy.model;


public class CommentBean {

//	private int id;					 // 评论ID
    private int user_id;			 // 用户ID
    private String user_name;		 // 用户名称
    private String user_icon;		 // 用户头像
    private String comment;			 // 评论内容
//    private int digg;				 // 评论点赞数量
//    private String times;			 // 评论时间
//    private List<ReplyBean2> reply; // 回复列表
//    private int is_more_comment;     // 是否首发
//    private int is_digg;         // 当前用户是否对该评论点赞
//    public List<ReplyBean2> getReply() {
//		return reply;
//	}
//	public void setReply(List<ReplyBean2> reply) {
//		this.reply = reply;
//	}
//	public int getIs_digg() {
//		return is_digg;
//	}
//	public void setIs_digg(int is_digg) {
//		this.is_digg = is_digg;
//	}
//	public int getIs_more_comment() {
//		return is_more_comment;
//	}
//	public void setIs_more_comment(int is_more_comment) {
//		this.is_more_comment = is_more_comment;
//	}
//	public int getId() {
//		return id;
//	}
//	public void setId(int id) {
//		this.id = id;
//	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
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
//	public int getDigg() {
//		return digg;
//	}
//	public void setDigg(int digg) {
//		this.digg = digg;
//	}
//	public String getTimes() {
//		return times;
//	}
//	public void setTimes(String times) {
//		this.times = times;
//	}
//	@Override
//	public String toString() {
//		return "CommentBean [id=" + id + ", user_id=" + user_id
//				+ ", user_name=" + user_name + ", user_icon=" + user_icon
//				+ ", comment=" + comment + ", digg=" + digg + ", times="
//				+ times + ", repaly=" + reply + ", is_more_comment="
//				+ is_more_comment + "]";
//	}
}
