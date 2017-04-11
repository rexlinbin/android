package com.bccv.zhuiyingzhihanju.model;

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
	
	/**-简介***/
	private String short_summary;
	private String type_id;
	
	
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
