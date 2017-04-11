package com.bccv.boxcomic.ebook;

import java.io.Serializable;

public class ChapterInfo implements Serializable{
	
	private static final long serialVersionUID = 8215348727959572682L;

	@SuppressWarnings("unused")
	private static final String TAG = "ChapterInfo";
	
	private int id;				//章节id
	private String title;		//章节名称
	private String fileName;	//章节文件名
	private String path;		//解压后的文件路径
	private int index;			//章节下标
	private String download;
	private boolean isDownload;
	
	
	
	public boolean isDownload() {
		return isDownload;
	}
	public void setDownload(boolean isDownload) {
		this.isDownload = isDownload;
	}
	public String getDownload() {
		return download;
	}
	public void setDownload(String download) {
		this.download = download;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
	@Override
	public String toString() {
		return "ChapterInfo [id=" + id + ", name=" + title + ", fileName="
				+ fileName + ", path=" + path + ", index=" + index + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		ChapterInfo info = null;
		if(obj instanceof ChapterInfo){
			info = (ChapterInfo) obj;
			return id == info.getId();
		}
		return false;
	}
	
}
