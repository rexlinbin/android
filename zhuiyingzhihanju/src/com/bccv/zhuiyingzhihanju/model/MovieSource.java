package com.bccv.zhuiyingzhihanju.model;

import java.io.Serializable;
import java.util.List;

public class MovieSource implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String episodes_id;
	String des;
	String periods;
	List<MovieUrl> source_text;

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getPeriods() {
		return periods;
	}

	public void setPeriods(String periods) {
		this.periods = periods;
	}

	public String getEpisodes_id() {
		return episodes_id;
	}

	public void setEpisodes_id(String episodes_id) {
		this.episodes_id = episodes_id;
		if (episodes_id == null) {
			this.episodes_id = "";
		}
	}

	public List<MovieUrl> getSource_text() {
		return source_text;
	}

	public void setSource_text(List<MovieUrl> source_text) {
		this.source_text = source_text;
	}

}
