package com.bccv.zhuiyingzhihanju.model;

public class Comment {
/**
 * status----------系统状态码
             total-----------总页数
             data------------分类列表
                 id-------------评论ID
                 nick_name------用户昵称
                 user_icon------用户头像
                 comment--------评论内容
                 times----------评论时间
 */
	
	private String id;
	private String content;
	private long ctime;
	private int digg;
	private User user_info;
	private User f_user_info;
	private String f_comment_info;
	private boolean isSelect;

	private String platform;
	private String localtion;
	public boolean isSelect() {
		return isSelect;
	}
	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}
	public String getF_comment_info() {
		return f_comment_info;
	}
	public void setF_comment_info(String f_comment_info) {
		this.f_comment_info = f_comment_info;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getCtime() {
		return ctime;
	}
	public void setCtime(long ctime) {
		this.ctime = ctime;
	}
	public int getDigg() {
		return digg;
	}
	public void setDigg(int digg) {
		this.digg = digg;
	}
	public User getUser_info() {
		return user_info;
	}
	public void setUser_info(User user_info) {
		this.user_info = user_info;
	}
	public User getF_user_info() {
		return f_user_info;
	}
	public void setF_user_info(User f_user_info) {
		this.f_user_info = f_user_info;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getLocaltion() {
		return localtion;
	}
	public void setLocaltion(String localtion) {
		this.localtion = localtion;
	}


	
	
	
}
