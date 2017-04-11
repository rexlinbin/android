package com.boxuu.gamebox.model;



public class GameInfo {

	private String download;
	private String icon;
	private int id;
	private String name;
	public String getDownload() {
		return download;
	}
	public void setDownload(String download) {
		this.download = download;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "GameInfo [download=" + download + ", icon=" + icon + ", id="
				+ id + ", name=" + name + "]";
	}
}
