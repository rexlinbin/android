package com.bccv.strategy.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.bccv.strategy.network.NetResBean;
import com.bccv.strategy.utils.JsonObjectUitl;

public class AppDetailsBean extends NetResBean{

	private int game_id;        				// APP ID
	private long news_ctime;
    private String game_title;  				// APP 名称
//    private String ftitle; 	 	 		// APP 副标题
//    private String introduce; 	 		// APP 介绍
    private String game_icon;					// APP 图标
    private String url;
//    private List<String> pics;			// APP 推荐图
//    private int choice;		 	 		// 是否推荐 1为推荐
//    private String times;		 		// 发布时间
//    private int creator_id; 	 		// 创建者ID
//    private String creator_name; 		// 创建者名称
//    private String creator_icon; 		// 创建者头像
    private List<CommentBean> comment;  // 评论列表
    private String news_comment;
    private boolean is_raid_digg;			// 是否对该应用点赞 1 true 0 false
    private boolean is_raid_focus;
    private String game_an_down;
    private String news_title;
    private String news_ftitle;
    private String news_titlepic;
//    private boolean is_like;			// 是否对该应用收藏 1 true 0 false
//    private int now_p;                  // 当前页
//    private int total_p;                // 总页数
//    private int price;                  // 是否免费
//    private String type_name;           // 应用类型  应用/游戏
//    private String url; 			    // 分享的链接
//    private List<DownloadBean> download;// 下载链接
//    private String pkgname;             // 下载包名
//    private int versioncode;            // 下载版本
    
//	public String getPkgname() {
//		return pkgname;
//	}
//	public void setPkgname(String pkgname) {
//		this.pkgname = pkgname;
//	}
//	public int getVersioncode() {
//		return versioncode;
//	}
//	public void setVersioncode(int versioncode) {
//		this.versioncode = versioncode;
//	}
//	public List<DownloadBean> getDownload() {
//		return download;
//	}
//	public void setDownload(List<DownloadBean> download) {
//		this.download = download;
//	}
//	public int getPrice() {
//		return price;
//	}
//	public void setPrice(int price) {
//		this.price = price;
//	}
//	public String getType_name() {
//		return type_name;
//	}
//	public void setType_name(String type_name) {
//		this.type_name = type_name;
//	}
//	public String getUrl() {
//		return url;
//	}
//	public void setUrl(String url) {
//		this.url = url;
//	}
//	public int getNow_p() {
//		return now_p;
//	}
//	public void setNow_p(int now_p) {
//		this.now_p = now_p;
//	}
//	public int getTotal_p() {
//		return total_p;
//	}
//	public void setTotal_p(int total_p) {
//		this.total_p = total_p;
//	}
     
	public boolean isIs_raid_digg() {
		return is_raid_digg;
	}
	public String getNews_title() {
		return news_title;
	}
	public void setNews_title(String news_title) {
		this.news_title = news_title;
	}
	public String getNews_ftitle() {
		return news_ftitle;
	}
	public void setNews_ftitle(String news_ftitle) {
		this.news_ftitle = news_ftitle;
	}
	public String getNews_titlepic() {
		return news_titlepic;
	}
	public void setNews_titlepic(String news_titlepic) {
		this.news_titlepic = news_titlepic;
	}
	public String getGame_an_down() {
		return game_an_down;
	}
	public void setGame_an_down(String game_an_down) {
		this.game_an_down = game_an_down;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getNews_comment() {
		return news_comment;
	}
	public void setNews_comment(String news_comment) {
		this.news_comment = news_comment;
	}
	public boolean isIs_raid_focus() {
		return is_raid_focus;
	}
	public void setIs_raid_focus(boolean is_raid_focus) {
		this.is_raid_focus = is_raid_focus;
	}
	public long getNews_ctime() {
		return news_ctime;
	}
	public void setNews_ctime(long news_ctime) {
		this.news_ctime = news_ctime;
	}
	public void setIs_raid_digg(boolean is_raid_digg) {
		this.is_raid_digg = is_raid_digg;
	}
//	public boolean isIs_like() {
//		return is_like;
//	}
//	public void setIs_like(boolean is_like) {
//		this.is_like = is_like;
//	}
	public int getGame_id() {
		return game_id;
	}
	public void setGame_id(int game_id) {
		this.game_id = game_id;
	}
	public String getGame_title() {
		return game_title;
	}
	public void setGame_title(String game_title) {
		this.game_title = game_title;
	}
//	public String getFtitle() {
//		return ftitle;
//	}
//	public void setFtitle(String ftitle) {
//		this.ftitle = ftitle;
//	}
//	public String getIntroduce() {
//		return introduce;
//	}
//	public void setIntroduce(String introduce) {
//		this.introduce = introduce;
//	}
	public String getGame_icon() {
		return game_icon;
	}
	public void setGame_icon(String game_icon) {
		this.game_icon = game_icon;
	}
//	public List<String> getPics() {
//		return pics;
//	}
//	public void setPics(List<String> pics) {
//		this.pics = pics;
//	}
//	public int getChoice() {
//		return choice;
//	}
//	public void setChoice(int choice) {
//		this.choice = choice;
//	}
//	public String getTimes() {
//		return times;
//	}
//	public void setTimes(String times) {
//		this.times = times;
//	}
//	public int getCreator_id() {
//		return creator_id;
//	}
//	public void setCreator_id(int creator_id) {
//		this.creator_id = creator_id;
//	}
//	public String getCreator_name() {
//		return creator_name;
//	}
//	public void setCreator_name(String creator_name) {
//		this.creator_name = creator_name;
//	}
//	public String getCreator_icon() {
//		return creator_icon;
//	}
//	public void setCreator_icon(String creator_icon) {
//		this.creator_icon = creator_icon;
//	}
	public List<CommentBean> getComment() {
		return comment;
	}
	public void setComment(List<CommentBean> comment) {
		this.comment = comment;
	}
//	@Override
//	public String toString() {
//		return "AppDetailsBean [id=" + id + ", title=" + title + ", ftitle="
//				+ ftitle + ", introduce=" + introduce + ", icon=" + icon
//				+ ", pics=" + pics + ", choice=" + choice + ", times=" + times
//				+ ", creator_id=" + creator_id + ", creator_name="
//				+ creator_name + ", creator_icon=" + creator_icon
//				+ ", comment=" + comment + "]";
//	}
	@Override
	public void parseData(String jsonData) {
		try {
			JSONObject object = new JSONObject(jsonData);
			JSONObject object3 = new JSONObject(JsonObjectUitl.getString(object, "data"));
			JSONObject object4 = new JSONObject(object3.getString("raidinfo"));
			setGame_id(JsonObjectUitl.getInt(object, "game_id"));
			setGame_title(JsonObjectUitl.getString(object, "game_title"));
			setGame_an_down(JsonObjectUitl.getString(object, "game_an_down"));
			setGame_icon(JsonObjectUitl.getString(object, "game_icon"));
			setNews_ctime(object4.optLong("news_ctime"));
			setNews_comment(JsonObjectUitl.getString(object4, "news_comment"));
			setUrl(JsonObjectUitl.getString(object4, "url"));
			setNews_title(object4.optString("news_title"));
			setNews_ftitle(object4.optString("news_ftitle"));
			setNews_titlepic(object4.optString("news_titlepic"));
			setIs_raid_digg(JsonObjectUitl.getInt(object3, "is_raid_digg") == 1 ? true : false);
			setIs_raid_focus(JsonObjectUitl.getInt(object3, "is_raid_focus") == 1 ? true : false);
//			setFtitle(JsonObjectUitl.getString(object, "ftitle"));
//			setIntroduce(JsonObjectUitl.getString(object, "introduce"));
//			setChoice(JsonObjectUitl.getInt(object, "choice"));
//			setTimes(JsonObjectUitl.getString(object, "times"));
//			setCreator_id(JsonObjectUitl.getInt(object, "creator_id"));
//			setCreator_name(JsonObjectUitl.getString(object, "creator_name"));
//			setCreator_icon(JsonObjectUitl.getString(object, "creator_icon"));
//			setIs_like(JsonObjectUitl.getInt(object, "is_like") == 1 ? true : false);
//			setNow_p(JsonObjectUitl.getInt(object, "now_p"));
//			setTotal_p(JsonObjectUitl.getInt(object, "total_p"));
//			setPrice(JsonObjectUitl.getInt(object, "price"));
//			setType_name(JsonObjectUitl.getString(object, "type_name"));
//			setUrl(JsonObjectUitl.getString(object, "url"));
//			setPkgname(JsonObjectUitl.getString(object, "pkgname"));
//			setVersioncode(JsonObjectUitl.getInt(object, "versioncode"));
			
//			setPics(JSON.parseArray(JsonObjectUitl.getString(object, "pics"),String.class));
//			setDownload(JSON.parseArray(JsonObjectUitl.getString(object, "download_list"),DownloadBean.class));
			
//			String comment_str = JsonObjectUitl.getString(object3, "comment");
//			List<String> parse = JSON.parseArray(comment_str,String.class);
//			if (parse != null && parse.size() >0) {
//				comment = new ArrayList<CommentBean>();
//			}
//			for (int i = 0; i < parse.size(); i++) {
//				CommentBean bean = new CommentBean();
//				String str = parse.get(i);
//				JSONObject object2 = new JSONObject(str);
//				bean.setComment(JsonObjectUitl.getString(object2, "comment"));
//				bean.setDigg(JsonObjectUitl.getInt(object2, "digg"));
//				bean.setId(JsonObjectUitl.getInt(object2, "id"));
//				bean.setIs_more_comment(JsonObjectUitl.getInt(object2,"is_more_comment"));
//				bean.setTimes(JsonObjectUitl.getString(object2, "times"));
//				bean.setUser_icon(JsonObjectUitl.getString(object2, "user_icon"));
//				bean.setUser_id(JsonObjectUitl.getInt(object2, "user_id"));
//				bean.setUser_name(JsonObjectUitl.getString(object2, "user_name"));
//				bean.setIs_digg(JsonObjectUitl.getInt(object2, "is_digg"));
//				String reply = JsonObjectUitl.getString(object2, "reply");
//				if (reply != null && !reply.equals("")) {
//					List<ReplyBean2> array = JSON.parseArray(reply, ReplyBean2.class);
//					bean.setReply(array);
//				}
//				comment.add(bean);
//			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
