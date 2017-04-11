package com.bccv.boxcomic.modal;

import java.io.Serializable;

public class Channel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int cat_id;
	private String cat_title;
	public int getCat_id() {
		return cat_id;
	}
	public void setCat_id(int cat_id) {
		this.cat_id = cat_id;
	}
	public String getCat_title() {
		return cat_title;
	}
	public void setCat_title(String cat_title) {
		this.cat_title = cat_title;
	}
	
}
