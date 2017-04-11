package com.bccv.strategy.model;

import org.json.JSONObject;

public class MyCommentItemBean {

	private String id;			//应用评论ID  / 疑问ID
	private String comment;		//评论内容
	private String news_title;		//appname
	private String game_icon;		//appIcon
	private String game_id;		//appId
	private String game_title;		//appId
	private String raid_id;
	private int reply;			//评论类型  1评论回复 0评论应用
	
	private String questions;   //疑问内容
	private String answer;      //疑问回答内容
	private String answer_id;
	private String user_icon;   //提问者头像
	private String user_id;   	//提问者id
	private String user_name;   //提问者id
	private boolean digg;
	private String questions_id;
	
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

	public String getAnswer_id() {
		return answer_id;
	}

	public void setAnswer_id(String answer_id) {
		this.answer_id = answer_id;
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

	public boolean isDigg() {
		return digg;
	}

	public void setDigg(boolean digg) {
		this.digg = digg;
	}

	public String getQuestions_id() {
		return questions_id;
	}

	public void setQuestions_id(String questions_id) {
		this.questions_id = questions_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getReply() {
		return reply;
	}

	public void setReply(int reply) {
		this.reply = reply;
	}

	public String getNews_title() {
		return news_title;
	}

	public void setNews_title(String news_title) {
		this.news_title = news_title;
	}

	public String getGame_icon() {
		return game_icon;
	}

	public void setGame_icon(String game_icon) {
		this.game_icon = game_icon;
	}

	public String getGame_id() {
		return game_id;
	}

	public void setGame_id(String game_id) {
		this.game_id = game_id;
	}

	public String getGame_title() {
		return game_title;
	}

	public void setGame_title(String game_title) {
		this.game_title = game_title;
	}

	public String getRaid_id() {
		return raid_id;
	}

	public void setRaid_id(String raid_id) {
		this.raid_id = raid_id;
	}

	@Override
	public String toString() {
		return "MyCommentItemBean [id=" + id + ", comment=" + comment
				+ ", news_title=" + news_title + ", game_icon=" + game_icon
				+ ", game_id=" + game_id + ", game_title=" + game_title
				+ ", raid_id=" + raid_id + ", reply=" + reply + "]";
	}
	
	public void parseComment(JSONObject dataObject) {
		setId(dataObject.optString("id"));
		setComment(dataObject.optString("comment"));
		setNews_title(dataObject.optString("news_title"));
		setGame_title(dataObject.optString("game_title"));
		setGame_icon(dataObject.optString("game_icon"));
		setGame_id(dataObject.optString("game_id"));
		setRaid_id(dataObject.optString("raid_id"));
		setReply(dataObject.optInt("reply"));
	}
	public void parseAnswer(JSONObject dataObject) {
		setId(dataObject.optString("id"));
		setQuestions(dataObject.optString("questions"));
		setQuestions_id(dataObject.optString("questions_id"));
		setAnswer(dataObject.optString("answer"));
		setAnswer_id(dataObject.optString("answer_id"));
		setUser_icon(dataObject.optString("user_icon"));
		setUser_id(dataObject.optString("user_id"));
		setUser_name(dataObject.optString("user_name"));
		setDigg(dataObject.optInt("digg") == 1 ? true:false);
	}
}
