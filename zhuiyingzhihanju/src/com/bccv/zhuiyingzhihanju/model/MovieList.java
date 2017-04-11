package com.bccv.zhuiyingzhihanju.model;

public class MovieList {

	private String id;
	private String title;
	private String images;
	/**-简介***/
	private String short_summary;
	private String type_id;
	
	private String rating;
	private String cur_epsisode;
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
	public String getShort_summary() {
		return short_summary;
	}
	public void setShort_summary(String short_summary) {
		this.short_summary = short_summary;
	}
	public String getType_id() {
		return type_id;
	}
	public void setType_id(String type_id) {
		this.type_id = type_id;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getCur_epsisode() {
		return cur_epsisode;
	}
	public void setCur_epsisode(String cur_epsisode) {
		this.cur_epsisode = cur_epsisode;
	}
	
	
}
