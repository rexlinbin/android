package com.bccv.threedimensionalworld.model;

import java.io.Serializable;

public class MusicBean implements Serializable{

	/**
	 * status-------------系统状态码
       data---------------详情
           id--------------------ID
           name------------------歌曲名称
           author----------------表演者
           image-----------------封面图
           play_url--------------音乐链接
	 */
	private static final long serialVersionUID = 1L;




	private String image;
	private String author;
	private String play_url;

	private String name;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPlay_url() {
		return play_url;
	}

	public void setPlay_url(String play_url) {
		this.play_url = play_url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
	
	
	
	
}
