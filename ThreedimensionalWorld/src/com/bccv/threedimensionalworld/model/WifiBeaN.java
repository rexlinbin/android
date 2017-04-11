package com.bccv.threedimensionalworld.model;

public class WifiBeaN {
	private String WifiName;
	private String WifiInfo;
	private boolean isWifiPassWord;
	private String wifiStrong;
	private int level;
	
	
	
	
	
	
	
	public WifiBeaN(String wifiName, String wifiInfo, int level) {
		super();
		WifiName = wifiName;
		WifiInfo = wifiInfo;
		this.level = level;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getWifiName() {
		return WifiName;
	}
	public void setWifiName(String wifiName) {
		WifiName = wifiName;
	}
	public String getWifiInfo() {
		return WifiInfo;
	}
	public void setWifiInfo(String wifiInfo) {
		WifiInfo = wifiInfo;
	}
	public boolean isWifiPassWord() {
		return isWifiPassWord;
	}
	public void setWifiPassWord(boolean isWifiPassWord) {
		this.isWifiPassWord = isWifiPassWord;
	}
	public String getWifiStrong() {
		return wifiStrong;
	}
	public void setWifiStrong(String wifiStrong) {
		this.wifiStrong = wifiStrong;
	}

}
