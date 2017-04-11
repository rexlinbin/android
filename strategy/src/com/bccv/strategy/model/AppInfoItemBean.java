package com.bccv.strategy.model;

import org.json.JSONObject;

public class AppInfoItemBean {
	
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
//	  "cat_title": "网络游戏",
//    "creator_id": "1",
//    "creator_name": "煭鎽",
//    "game_icon": "http://i1.boxuu.com/handbook/anhei3.png",
//    "game_title": "暗黑破坏神3",
//    "id": "1",
//    "news_click": "0",
//    "news_ctime": "0",
//    "news_digg": "0",
//    "news_focus": "0",
//    "news_ftitle": "暗黑3攻略  国服",
//    "news_good": "0",
//    "news_path": "",
//    "news_title": "暗黑3攻略",
//    "news_titlepic": ""

	private String cat_title;
	private String creator_id;
	private String creator_name;
	private String creator_icon;
	private String game_icon;
	private String game_title;
	private String id;
	private String game_id;
	private String news_click;
	private long news_ctime;
	private int news_digg;
	private int news_focus;
	private String news_ftitle;
	private String news_good;
	private String news_path;
	private String news_title;
	private String news_titlepic;
	private String news_introduce;
	private String news_comment;
	private int choice;
//	private int like_num;
//	private int comment_num;
//	
//	private int is_first;
//	private String comment_id;
//	
//	private String price;  
//	private String type_name;
	
	public String getId() {
		return id;
	}

	public int getChoice() {
		return choice;
	}

	public void setChoice(int choice) {
		this.choice = choice;
	}

	public String getNews_comment() {
		return news_comment;
	}

	public void setNews_comment(String news_comment) {
		this.news_comment = news_comment;
	}

	public String getNews_introduce() {
		return news_introduce;
	}

	public void setNews_introduce(String news_introduce) {
		this.news_introduce = news_introduce;
	}

	public String getCreator_icon() {
		return creator_icon;
	}

	public void setCreator_icon(String creator_icon) {
		this.creator_icon = creator_icon;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getCat_title() {
		return cat_title;
	}

	public void setCat_title(String cat_title) {
		this.cat_title = cat_title;
	}

	public String getGame_icon() {
		return game_icon;
	}

	public void setGame_icon(String game_icon) {
		this.game_icon = game_icon;
	}

	public String getGame_title() {
		return game_title;
	}

	public void setGame_title(String game_title) {
		this.game_title = game_title;
	}

	public String getNews_click() {
		return news_click;
	}

	public void setNews_click(String news_click) {
		this.news_click = news_click;
	}

	public long getNews_ctime() {
		return news_ctime;
	}

	public void setNews_ctime(long news_ctime) {
		this.news_ctime = news_ctime;
	}

	public int getNews_digg() {
		return news_digg;
	}

	public void setNews_digg(int news_digg) {
		this.news_digg = news_digg;
	}

	public int getNews_focus() {
		return news_focus;
	}

	public void setNews_focus(int news_focus) {
		this.news_focus = news_focus;
	}

	public String getNews_ftitle() {
		return news_ftitle;
	}

	public void setNews_ftitle(String news_ftitle) {
		this.news_ftitle = news_ftitle;
	}

	public String getNews_good() {
		return news_good;
	}

	public void setNews_good(String news_good) {
		this.news_good = news_good;
	}

	public String getNews_path() {
		return news_path;
	}

	public void setNews_path(String news_path) {
		this.news_path = news_path;
	}

	public String getNews_title() {
		return news_title;
	}

	public void setNews_title(String news_title) {
		this.news_title = news_title;
	}

	public String getNews_titlepic() {
		return news_titlepic;
	}

	public void setNews_titlepic(String news_titlepic) {
		this.news_titlepic = news_titlepic;
	}
	
	public String getGame_id() {
		return game_id;
	}

	public void setGame_id(String game_id) {
		this.game_id = game_id;
	}

	public void parse(JSONObject dataObject){
		this.cat_title = dataObject.optString("cat_title");
		this.creator_id = dataObject.optString("creator_id");
		this.creator_name = dataObject.optString("creator_name");
		this.game_icon = dataObject.optString("game_icon");
		this.game_title = dataObject.optString("game_title");
		this.id = dataObject.optString("id");
		this.game_id = dataObject.optString("game_id");
		this.news_ctime = dataObject.optLong("news_ctime")*1000l;
		this.news_focus = dataObject.optInt("news_focus");
		this.news_digg = dataObject.optInt("news_digg");
		this.news_ftitle = dataObject.optString("news_ftitle");
		this.news_good = dataObject.optString("news_good");
		this.news_path = dataObject.optString("news_path");
		this.news_title = dataObject.optString("news_title");
		this.news_titlepic = dataObject.optString("news_titlepic");
		this.news_introduce = dataObject.optString("news_introduce");
		this.news_comment = dataObject.optString("news_comment");
		this.choice = dataObject.optInt("choice");
	}

	@Override
	public String toString() {
		return "AppInfoItemBean [cat_title=" + cat_title + ", creator_id="
				+ creator_id + ", creator_name=" + creator_name
				+ ", game_icon=" + game_icon + ", game_title=" + game_title
				+ ", id=" + id + ", news_click=" + news_click + ", news_ctime="
				+ news_ctime + ", news_digg=" + news_digg + ", news_focus="
				+ news_focus + ", news_ftitle=" + news_ftitle + ", news_good="
				+ news_good + ", news_path=" + news_path + ", news_title="
				+ news_title + ", news_titlepic=" + news_titlepic + "]";
	}

	
	
}