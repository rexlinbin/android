package com.bccv.boxcomic.modal;

import java.io.Serializable;

public class OnlineFrom implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String menu_id;
	String resource_name;
	String resource_id;
	
	public String getResource_id() {
		return resource_id;
	}
	public void setResource_id(String resource_id) {
		this.resource_id = resource_id;
	}
	public String getMenu_id() {
		return menu_id;
	}
	public void setMenu_id(String menu_id) {
		this.menu_id = menu_id;
	}
	public String getResource_name() {
		return resource_name;
	}
	public void setResource_name(String resource_name) {
		this.resource_name = resource_name;
	}
	
	
}
