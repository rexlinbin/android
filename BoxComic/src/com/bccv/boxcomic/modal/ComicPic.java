package com.bccv.boxcomic.modal;

import java.io.Serializable;

public class ComicPic implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String url = "";
	int width;
	int height;
	boolean isLocal = false;
	String chapteridString = "";
	
	
	
	public String getChapteridString() {
		return chapteridString;
	}
	public void setChapteridString(String chapteridString) {
		this.chapteridString = chapteridString;
	}
	public boolean isLocal() {
		return isLocal;
	}
	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
}
