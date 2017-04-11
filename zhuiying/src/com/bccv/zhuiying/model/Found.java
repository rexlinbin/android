package com.bccv.zhuiying.model;

public class Found {
/**
 *    status----------系统状态码
         data------------列表
             id----------分类ID
             title-------名称
             images------图片地址
 * 
 * 
 * **/
	
	private String id;
	private String title;
	private String images;
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
	
	
	
}
