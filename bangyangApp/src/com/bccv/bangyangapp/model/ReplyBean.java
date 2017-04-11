package com.bccv.bangyangapp.model;

public class ReplyBean {

	private String id;				//回复ID
    private int user_id;			// 用户ID
    private String user_name;		// 用户名称
    private String user_icon;		// 用户头像
    private String comment;			// 评论内容
    private int digg;				// 评论点赞数量
    private long times;			// 评论时间
    
    private boolean isZan = false ; //是否赞过  默认false   用于逻辑处理  没有网络数据与之对应
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public int getDigg() {
		return digg;
	}
	public void setDigg(int digg) {
		this.digg = digg;
	}
	public long getTimes() {
		return times;
	}
	public void setTimes(long times) {
		this.times = times;
	}
	
	
	public boolean isZan() {
		return isZan;
	}
	public void setZan(boolean isZan) {
		this.isZan = isZan;
	}
	
	
	@Override
	public String toString() {
		return "ReplyBean [user_id=" + user_id + ", user_name=" + user_name
				+ ", user_icon=" + user_icon + ", comment=" + comment
				+ ", digg=" + digg + ", times=" + times + "]";
	}
}
