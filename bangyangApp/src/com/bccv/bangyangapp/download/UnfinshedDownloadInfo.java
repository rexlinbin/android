package com.bccv.bangyangapp.download;

import java.io.Serializable;

public class UnfinshedDownloadInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2836320557541948123L;
	
	/**
	 * 创建时间
	 */
	private long creatTime ;
	
	// 下载URL
	private String mDownloadUrl="";
	// 下载应用的名称
	private String Name;
	// 下载文件名称
	private String mDownloadFilePackageName;
	// 下载apk版本
	private int mDownloadFileVer;
	// 是否自动安装
	private boolean isAutoInstall;
	//是否安装后打开
	private boolean isOpen;
	
	public String getmDownloadUrl() {
		return mDownloadUrl;
	}
	public void setmDownloadUrl(String mDownloadUrl) {
		this.mDownloadUrl = mDownloadUrl;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getmDownloadFilePackageName() {
		return mDownloadFilePackageName;
	}
	public void setmDownloadFilePackageName(String mDownloadFilePackageName) {
		this.mDownloadFilePackageName = mDownloadFilePackageName;
	}
	public int getmDownloadFileVer() {
		return mDownloadFileVer;
	}
	public void setmDownloadFileVer(int mDownloadFileVer) {
		this.mDownloadFileVer = mDownloadFileVer;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public long getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(long creatTime) {
		this.creatTime = creatTime;
	}
	public boolean isAutoInstall() {
		return isAutoInstall;
	}
	public void setAutoInstall(boolean isAutoInstall) {
		this.isAutoInstall = isAutoInstall;
	}
	public boolean isOpen() {
		return isOpen;
	}
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	@Override
	public boolean equals(Object o) {
		
		if(o instanceof UnfinshedDownloadInfo){
			return ((UnfinshedDownloadInfo)o).getmDownloadUrl().equals(this.mDownloadUrl);
		}
		return false;
	}

}
