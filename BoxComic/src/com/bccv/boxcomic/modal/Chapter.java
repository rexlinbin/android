package com.bccv.boxcomic.modal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;

public class Chapter implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String chapter_title;
	private String download = "dmappimg.boxuu.com:8080/mhimg/b/bszn/jd1_800.zip";
	private String chapter_id;
	private String comic_id;
	private boolean isSelect;
	private int bookmarkNum;
	private int chapter_count;
	private String chapter_size;
	
	private List<ComicPic> comicPics = new ArrayList<ComicPic>();
	
	
	
	public List<ComicPic> getComicPics() {
		return comicPics;
	}
	public void setComicPics(List<ComicPic> comicPics) {
		this.comicPics = comicPics;
	}
	public String getChapter_size() {
		return chapter_size;
	}
	public void setChapter_size(String chapter_size) {
		this.chapter_size = chapter_size;
	}
	public int getChapter_count() {
		return chapter_count;
	}
	public void setChapter_count(int chapter_count) {
		this.chapter_count = chapter_count;
	}
	public int getBookmarkNum() {
		return bookmarkNum;
	}
	public void setBookmarkNum(int bookmarkNum) {
		this.bookmarkNum = bookmarkNum;
	}
	
	
	public String getDownload() {
		return download;
	}
	public void setDownload(String download) {
		this.download = download;
	}
	public boolean isSelect() {
		return isSelect;
	}
	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}
	public String getChapter_title() {
		return chapter_title;
	}
	public void setChapter_title(String chapter_title) {
		this.chapter_title = chapter_title;
	}
	public String getChapter_id() {
		return chapter_id;
	}
	public void setChapter_id(String chapter_id) {
		this.chapter_id = chapter_id;
	}
	public String getComic_id() {
		return comic_id;
	}
	public void setComic_id(String comic_id) {
		this.comic_id = comic_id;
	}
	
}
