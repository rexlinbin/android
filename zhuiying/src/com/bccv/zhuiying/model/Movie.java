package com.bccv.zhuiying.model;

import java.io.Serializable;
import java.util.List;

public class Movie implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/***
	 *   status-------------系统状态码
       data---------------电影详情列表
           id-----------------电影ID
           title--------------名称
           images-------------封面图
           type_id------------所属分类ID
	 * **/
	
	
	private String id;
	private String title;
	private String images;
	private String rating;
	private String type_id;
	private long play_Time;
	private long play_Date;
	private String episode_id;
	private String episode_num;
	private int currSourceNum;
	private List<MovieUrl> sourceList;
	private List<MovieSource> movieSourceList;
	
	public int getCurrSourceNum() {
		return currSourceNum;
	}
	public void setCurrSourceNum(int currSourceNum) {
		this.currSourceNum = currSourceNum;
	}
	public List<MovieUrl> getSourceList() {
		return sourceList;
	}
	public void setSourceList(List<MovieUrl> sourceList) {
		this.sourceList = sourceList;
	}
	public List<MovieSource> getMovieSourceList() {
		return movieSourceList;
	}
	public void setMovieSourceList(List<MovieSource> movieSourceList) {
		this.movieSourceList = movieSourceList;
	}
	public String getEpisode_num() {
		return episode_num;
	}
	public void setEpisode_num(String episode_num) {
		this.episode_num = episode_num;
	}

	public String getEpisode_id() {
		return episode_id;
	}
	public void setEpisode_id(String episode_id) {
		this.episode_id = episode_id;
	}
	public long getPlay_Date() {
		return play_Date;
	}
	public void setPlay_Date(long play_Date) {
		this.play_Date = play_Date;
	}
	public long getPlay_Time() {
		return play_Time;
	}
	public void setPlay_Time(long play_Time) {
		this.play_Time = play_Time;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
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
	public String getImages() {
		return images;
	}
	public void setImages(String images) {
		this.images = images;
	}
	public String getType_id() {
		return type_id;
	}
	public void setType_id(String type_id) {
		this.type_id = type_id;
	}
	
	
	
	
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (this == o)
			return true;
		if (!(o instanceof Movie))
			return false;

		Movie that = (Movie) o;

		if (this.id.equals(that.id) && this.type_id.equals(that.type_id))
			return true;

		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
}
