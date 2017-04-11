package com.bccv.threedimensionalworld.model;

import java.io.Serializable;
import java.util.List;

public class App implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * id--------------------游戏ID
           name------------------名称
           game_categories-------所属分类ID
           genres_name-----------分类名称
           images----------------图片
           pics------------------游戏截图
           intro-----------------简介
           game_score_interface--评分
           game_size-------------游戏大小
           language--------------语言
	 */
	private String id;
	private String name;
	private String images;
	private String genres_name;
	private String app_categories;
	private List<String> pics;
	private String language;
	private String app_size;
	private String intro;
	private float app_score_interface;
	private String app_download;
	private String app_package;
	
	
	public String getApp_package() {
		return app_package;
	}
	public void setApp_package(String app_package) {
		this.app_package = app_package;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImages() {
		return images;
	}
	public void setImages(String images) {
		this.images = images;
	}
	public String getGenres_name() {
		return genres_name;
	}
	public void setGenres_name(String genres_name) {
		this.genres_name = genres_name;
	}
	
	
	public String getApp_download() {
		return app_download;
	}
	public void setApp_download(String app_download) {
		this.app_download = app_download;
	}
	public List<String> getPics() {
		return pics;
	}
	public void setPics(List<String> pics) {
		this.pics = pics;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getApp_categories() {
		return app_categories;
	}
	public void setApp_categories(String app_categories) {
		this.app_categories = app_categories;
	}
	public String getApp_size() {
		return app_size;
	}
	public void setApp_size(String app_size) {
		this.app_size = app_size;
	}
	public float getApp_score_interface() {
		return app_score_interface;
	}
	public void setApp_score_interface(float app_score_interface) {
		this.app_score_interface = app_score_interface;
	}
	
	
	
	
}
