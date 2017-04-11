package com.bccv.threedimensionalworld.model;

import java.io.Serializable;

import android.graphics.Bitmap;

public class Movie implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*id-----------------电影ID
    title--------------名称
    original_title-----原名
    directors----------导演
    casts--------------演员
    genres-------------分类ID(可能属于多个分类)
    genres_name--------分类名称
    year---------------影片上映时间
    countries----------所属国家
    rating-------------豆瓣评分
    summary------------简介
    images-------------封面图*/
	private String id;
	private String title;
	private String original_title;
	private String directors;
	private String casts;
	private String genres;
	private String genres_name;
	private String year;
	private String countries;
	private float rating;
	private String summary;
	private String images;
	private String url;
	private String background;
	private boolean is2D = false;
	
	
	public boolean isIs2D() {
		return is2D;
	}
	public void setIs2D(boolean is2d) {
		is2D = is2d;
	}
	public String getBackground() {
		return background;
	}
	public void setBackground(String background) {
		this.background = background;
	}
	public Bitmap getBit() {
		return bit;
	}
	public void setBit(Bitmap bit) {
		this.bit = bit;
	}
	private Bitmap bit;
	
	public String getId() {
		return id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	public String getOriginal_title() {
		return original_title;
	}
	public void setOriginal_title(String original_title) {
		this.original_title = original_title;
	}
	public String getDirectors() {
		return directors;
	}
	public void setDirectors(String directors) {
		this.directors = directors;
	}
	public String getCasts() {
		return casts;
	}
	public void setCasts(String casts) {
		this.casts = casts;
	}
	public String getGenres() {
		return genres;
	}
	public void setGenres(String genres) {
		this.genres = genres;
	}
	public String getGenres_name() {
		return genres_name;
	}
	public void setGenres_name(String genres_name) {
		this.genres_name = genres_name;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getCountries() {
		return countries;
	}
	public void setCountries(String countries) {
		this.countries = countries;
	}
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getImages() {
		return images;
	}
	public void setImages(String images) {
		this.images = images;
	}
	
	
	
	
}
