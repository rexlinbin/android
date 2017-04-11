package com.bccv.meitu.model;

/**
 * 评论
 * 
 * @author liukai
 *
 * @version 2014-11-11  上午10:36:09
 */
public class Comment {

	int id;
	String comments_content;
	int support;
	int ctime;
	String user_name;
	String user_icon;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getComments_content() {
		return comments_content;
	}
	public void setComments_content(String comments_content) {
		this.comments_content = comments_content;
	}
	public int getSupport() {
		return support;
	}
	public void setSupport(int support) {
		this.support = support;
	}
	public int getCtime() {
		return ctime;
	}
	public void setCtime(int ctime) {
		this.ctime = ctime;
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
	@Override
	public String toString() {
		return "Comments [id=" + id + ", comments_content="
				+ comments_content + ", support=" + support + ", ctime="
				+ ctime + ", user_name=" + user_name + ", user_icon="
				+ user_icon + "]";
	}
	
	
}
