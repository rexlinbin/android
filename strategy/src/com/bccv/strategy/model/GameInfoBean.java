package com.bccv.strategy.model;

import org.json.JSONObject;

public class GameInfoBean {

	private String game_icon;
	private String game_title;
	private String id;
	public String getGame_icon() {
		return game_icon;
	}
	public void setGame_icon(String game_icon) {
		this.game_icon = game_icon;
	}
	public String getGame_title() {
		return game_title;
	}
	public void setGame_title(String game_title) {
		this.game_title = game_title;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public void parse(JSONObject object) {
		setGame_icon(object.optString("game_icon"));
		setGame_title(object.optString("game_title"));
		setId(object.optString("id"));
	}
}
