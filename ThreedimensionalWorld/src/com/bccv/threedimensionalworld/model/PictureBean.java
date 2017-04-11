package com.bccv.threedimensionalworld.model;

import java.io.Serializable;

public class PictureBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int image;
	String title;
	private String picPath;

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

}
