package com.bccv.strategy.model;

public class DownloadBean {
	
	private String download;
	private String type;
	
	public String getDownload() {
		return download;
	}
	public void setDownload(String download) {
		this.download = download;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "DownloadBean [dowanload=" + download + ", type=" + type + "]";
	}
}
