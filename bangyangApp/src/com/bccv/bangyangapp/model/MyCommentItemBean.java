package com.bccv.bangyangapp.model;

public class MyCommentItemBean {

	private String id;			//应用评论ID  / 疑问ID
	private String comment;		//评论内容
	private String title;		//appname
	private String icon;		//appIcon
	private String app_id;		//appId
	private int reply;			//评论类型  1评论回复 0评论应用
	
	private String questions;   //疑问内容
	private String answer;      //疑问回答内容
	private String user_icon;   //提问者头像
	private String user_id;   	//提问者id
	private String user_name;   //提问者id
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	
	public int getReply() {
		return reply;
	}

	public void setReply(int reply) {
		this.reply = reply;
	}

	
	
	
	public String getQuestions() {
		return questions;
	}

	public void setQuestions(String questions) {
		this.questions = questions;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getUser_icon() {
		return user_icon;
	}

	public void setUser_icon(String user_icon) {
		this.user_icon = user_icon;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	@Override
	public String toString() {
		return "MyCommentItemBean [id=" + id + ", comment=" + comment
				+ ", title=" + title + ", icon=" + icon + ", app_id=" + app_id
				+ ", reply=" + reply + ", questions=" + questions + ", answer="
				+ answer + ", user_icon=" + user_icon + ", user_id=" + user_id
				+ ", user_name=" + user_name + "]";
	}


}
