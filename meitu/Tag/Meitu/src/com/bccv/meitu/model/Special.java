package com.bccv.meitu.model;

import java.util.List;

/**
 * 专辑详情
 * @author liukai
 *
 * @version 2014-11-11  上午10:28:00
 */
public class Special {
	
	private int special_id;
	private String special_name;
	private String special_names;//分享语
	private String special_pic;
	private int care_num;
	private int zan_num;
	private int comment_num;
	private int pic_num;
	private int special_pic_h;
	private int special_pic_w;
	private int author_id;
	private String author_name;
	private String author_icon;
	private String ctime;
	public String getCtime() {
		return ctime;
	}
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}
	private List<Tag> tag;
	
	public int getSpecial_id() {
		return special_id;
	}
	public void setSpecial_id(int special_id) {
		this.special_id = special_id;
	}
	public String getSpecial_name() {
		return special_name;
	}
	public void setSpecial_name(String special_name) {
		this.special_name = special_name;
	}
	public String getSpecial_pic() {
		return special_pic;
	}
	public void setSpecial_pic(String special_pic) {
		this.special_pic = special_pic;
	}
	public int getCare_num() {
		return care_num;
	}
	public void setCare_num(int care_num) {
		this.care_num = care_num;
	}
	public int getZan_num() {
		return zan_num;
	}
	public void setZan_num(int zan_num) {
		this.zan_num = zan_num;
	}
	public int getComment_num() {
		return comment_num;
	}
	public void setComment_num(int comment_num) {
		this.comment_num = comment_num;
	}
	public int getPic_num() {
		return pic_num;
	}
	public void setPic_num(int pic_num) {
		this.pic_num = pic_num;
	}
	public int getSpecial_pic_h() {
		return special_pic_h;
	}
	public void setSpecial_pic_h(int special_pic_h) {
		this.special_pic_h = special_pic_h;
	}
	public int getSpecial_pic_w() {
		return special_pic_w;
	}
	public void setSpecial_pic_w(int special_pic_w) {
		this.special_pic_w = special_pic_w;
	}
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
	public List<Tag> getTag() {
		return tag;
	}
	public void setTag(List<Tag> tag) {
		this.tag = tag;
	}
	public String getSpecial_names() {
		return special_names;
	}
	public void setSpecial_names(String special_names) {
		this.special_names = special_names;
	}
	
	@Override
	public String toString() {
		return "Special [special_id=" + special_id + ", special_name="
				+ special_name + ", special_names=" + special_names
				+ ", special_pic=" + special_pic + ", care_num=" + care_num
				+ ", zan_num=" + zan_num + ", comment_num=" + comment_num
				+ ", pic_num=" + pic_num + ", special_pic_h=" + special_pic_h
				+ ", special_pic_w=" + special_pic_w + ", author_id="
				+ author_id + ", author_name=" + author_name + ", author_icon="
				+ author_icon + ", ctime=" + ctime + ", tag=" + tag + "]";
	}
	
	
}
