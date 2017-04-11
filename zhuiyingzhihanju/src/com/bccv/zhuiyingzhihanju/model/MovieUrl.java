package com.bccv.zhuiyingzhihanju.model;

import java.io.Serializable;

public class MovieUrl implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * "source_id": "http://www.fun.tv/vplay/g-77558/", "id": "2",
	 * "website_name": "风行", "type_name": "fengxing", "website_type": "Website"
	 */

	String source_id;
	String id;
	String website_name;
	String type_name;
	String website_type;
	boolean isSelect = false;
	String source_url;
	String source_name;

	public String getSource_url() {
		return source_url;
	}

	public void setSource_url(String source_url) {
		this.source_url = source_url;
	}

	public String getSource_name() {
		return source_name;
	}

	public void setSource_name(String source_name) {
		this.source_name = source_name;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	public String getSource_id() {
		return source_id;
	}

	public void setSource_id(String source_id) {
		this.source_id = source_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWebsite_name() {
		return website_name;
	}

	public void setWebsite_name(String website_name) {
		this.website_name = website_name;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public String getWebsite_type() {
		return website_type;
	}

	public void setWebsite_type(String website_type) {
		this.website_type = website_type;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (o instanceof MovieUrl) {
			MovieUrl u = (MovieUrl) o;
			return this.source_id.equals(u.source_id);
		}
		return super.equals(o);
	}
}
