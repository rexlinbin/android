package com.bccv.ebook.model;

import java.io.Serializable;

public class ChapterInfo implements Serializable{
	
	private static final long serialVersionUID = 8215348727959572682L;

	@SuppressWarnings("unused")
	private static final String TAG = "ChapterInfo";
	
	private int id;				//章节id
	private String name;		//章节名称
	private String fileName;	//章节文件名
	private String path;		//解压后的文件路径
	private int index;			//章节下标
	
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
		return "ChapterInfo [id=" + id + ", name=" + name + ", fileName="
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
