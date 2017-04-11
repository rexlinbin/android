package com.bccv.meitu.model;

public class AuthorInfo {

	private int author_id;
	private String author_name;
	private String author_icon;
	private int fans_num;
	private int special_num;
	
	public int getAuthor_id() {
		return author_id;
	}
	public void setAuthor_id(int author_id) {
		this.author_id = author_id;
	}
	public String getAuthor_name() {
		return author_name;
	}
	public void setAuthor_name(String author_name) {
		this.author_name = author_name;
	}
	public String getAuthor_icon() {
		return author_icon;
	}
	public void setAuthor_icon(String author_icon) {
		this.author_icon = author_icon;
	}
	public int getFans_num() {
		return fans_num;
	}
	public void setFans_num(int fans_num) {
		this.fans_num = fans_num;
	}
	public int getSpecial_num() {
		return special_num;
	}
	public void setSpecial_num(int special_num) {
		this.special_num = special_num;
	}
	
	@Override
	public String toString() {
		return "AuthorInfo [author_id=" + author_id + ", author_name="
				+ author_name + ", author_icon=" + author_icon + ", fans_num="
				+ fans_num + ", special_num=" + special_num + "]";
	}
	
	private boolean isAttention = true; // 是否关注  默认为true

	public boolean isAttention() {
		return isAttention;
	}
	public void setAttention(boolean isAttention) {
		this.isAttention = isAttention;
	}
	
	
}
