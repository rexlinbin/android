package com.bccv.boxcomic.modal;

import java.io.Serializable;

public class MainItem implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int comic_id;
	private String comic_titlepic;
	private String comic_title;
	private int comic_finish;
	private String comic_last_chaptitle;
	public int getComic_id() {
		return comic_id;
	}
	public void setComic_id(int comic_id) {
		this.comic_id = comic_id;
	}
	public String getComic_titlepic() {
		return comic_titlepic;
	}
	public void setComic_titlepic(String comic_titlepic) {
		this.comic_titlepic = comic_titlepic;
	}
	public String getComic_title() {
		return comic_title;
	}
	public void setComic_title(String comic_title) {
		this.comic_title = comic_title;
	}
	public int getComic_finish() {
		return comic_finish;
	}
	public void setComic_finish(int comic_finish) {
		this.comic_finish = comic_finish;
	}
	public String getComic_last_chaptitle() {
		return comic_last_chaptitle;
	}
	public void setComic_last_chaptitle(String comic_last_chaptitle) {
		this.comic_last_chaptitle = comic_last_chaptitle;
	}
	
	
	
	
	
	
}
