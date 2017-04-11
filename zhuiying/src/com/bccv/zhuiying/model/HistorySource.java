package com.bccv.zhuiying.model;

import java.io.Serializable;

public class HistorySource implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;
	private String type_name;
	private int play_time = 0;
	private String episode_id;

	public String getEpisode_id() {
		return episode_id;
	}

	public void setEpisode_id(String episode_id) {
		this.episode_id = episode_id;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public int getPlay_time() {
		return play_time;
	}

	public void setPlay_time(int play_time) {
		this.play_time = play_time;
	}

}
