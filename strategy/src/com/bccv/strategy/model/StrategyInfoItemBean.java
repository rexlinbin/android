package com.bccv.strategy.model;

public class StrategyInfoItemBean {
	
//    id--------------APP ID
//    title-----------APP名称
//    ftitle----------副标题
//    introduce-------介绍
//    icon------------图标
//    pic-------------推荐图
//    choice----------是否推荐 1为推荐
//    times-----------发布时间
//    creator_id------创建者ID
//    creator_name----创建者名称
//    creator_icon----创建者头像
//    like_num--------关注该应用的人数
//    comment_num-----评论该应用的人数
//    comment---------创建者评论
//    is_first--------发表次序 1为首发  以此类推
//    comment_id------评论ID

	private String id;
	private String title;
	private String ftitle;
	private String introduce;
	private String icon;
	private String pic;
	private int choice;
	private long times;
	private String creator_id;
	private String creator_name;
	private String creator_icon;
	private String comment;
	
	private int like_num;
	private int comment_num;
	
	private int is_first;
	private String comment_id;
	
	private String price;  
	private String type_name;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFtitle() {
		return ftitle;
	}

	public void setFtitle(String ftitle) {
		this.ftitle = ftitle;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public int getChoice() {
		return choice;
	}

	public void setChoice(int choice) {
		this.choice = choice;
	}

	public long getTimes() {
		return times;
	}

	public void setTimes(long times) {
		this.times = times;
	}

	public String getCreator_id() {
		return creator_id;
	}

	public void setCreator_id(String creator_id) {
		this.creator_id = creator_id;
	}

	public String getCreator_name() {
		return creator_name;
	}

	public void setCreator_name(String creator_name) {
		this.creator_name = creator_name;
	}

	public String getCreator_icon() {
		return creator_icon;
	}

	public void setCreator_icon(String creator_icon) {
		this.creator_icon = creator_icon;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getLike_num() {
		return like_num;
	}

	public void setLike_num(int like_num) {
		this.like_num = like_num;
	}

	public int getComment_num() {
		return comment_num;
	}

	public void setComment_num(int comment_num) {
		this.comment_num = comment_num;
	}

	
	public int getIs_first() {
		return is_first;
	}

	public void setIs_first(int is_first) {
		this.is_first = is_first;
	}

	public String getComment_id() {
		return comment_id;
	}

	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	@Override
	public String toString() {
		return "AppInfoItemBean [id=" + id + ", title=" + title + ", ftitle="
				+ ftitle + ", introduce=" + introduce + ", icon=" + icon
				+ ", pic=" + pic + ", choice=" + choice + ", times=" + times
				+ ", creator_id=" + creator_id + ", creator_name="
				+ creator_name + ", creator_icon=" + creator_icon
				+ ", comment=" + comment + ", like_num=" + like_num
				+ ", comment_num=" + comment_num + ", is_first=" + is_first
				+ ", comment_id=" + comment_id + ", price=" + price
				+ ", type_name=" + type_name + "]";
	}

}