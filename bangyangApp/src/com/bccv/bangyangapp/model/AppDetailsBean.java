package com.bccv.bangyangapp.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.bccv.bangyangapp.network.NetResBean;
import com.bccv.bangyangapp.utils.JsonObjectUitl;

public class AppDetailsBean extends NetResBean{

	private int id;        				// APP ID
    private String title;  				// APP 名称
    private String ftitle; 	 	 		// APP 副标题
    private String introduce; 	 		// APP 介绍
    private String icon;         		// APP 图标
    private List<String> pics;			// APP 推荐图
    private int choice;		 	 		// 是否推荐 1为推荐
    private String times;		 		// 发布时间
    private int creator_id; 	 		// 创建者ID
    private String creator_name; 		// 创建者名称
    private String creator_icon; 		// 创建者头像
    private List<CommentBean> comment;  // 评论列表
    private boolean is_digg;			// 是否对该应用点赞 1 true 0 false
    private boolean is_like;			// 是否对该应用收藏 1 true 0 false
    private int now_p;                  // 当前页
    private int total_p;                // 总页数
    private int price;                  // 是否免费
    private String type_name;           // 应用类型  应用/游戏
    private String url; 			    // 分享的链接
    private List<DownloadBean> download;// 下载链接
    private String pkgname;             // 下载包名
    private int versioncode;            // 下载版本
    
	public String getPkgname() {
		return pkgname;
	}
	public void setPkgname(String pkgname) {
		this.pkgname = pkgname;
	}
	public int getVersioncode() {
		return versioncode;
	}
	public void setVersioncode(int versioncode) {
		this.versioncode = versioncode;
	}
	public List<DownloadBean> getDownload() {
		return download;
	}
	public void setDownload(List<DownloadBean> download) {
		this.download = download;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getNow_p() {
		return now_p;
	}
	public void setNow_p(int now_p) {
		this.now_p = now_p;
	}
	public int getTotal_p() {
		return total_p;
	}
	public void setTotal_p(int total_p) {
		this.total_p = total_p;
	}
	public boolean isIs_digg() {
		return is_digg;
	}
	public void setIs_digg(boolean is_digg) {
		this.is_digg = is_digg;
	}
	public boolean isIs_like() {
		return is_like;
	}
	public void setIs_like(boolean is_like) {
		this.is_like = is_like;
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
	public List<String> getPics() {
		return pics;
	}
	public void setPics(List<String> pics) {
		this.pics = pics;
	}
	public int getChoice() {
		return choice;
	}
	public void setChoice(int choice) {
		this.choice = choice;
	}
	public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
	}
	public int getCreator_id() {
		return creator_id;
	}
	public void setCreator_id(int creator_id) {
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
	public List<CommentBean> getComment() {
		return comment;
	}
	public void setComment(List<CommentBean> comment) {
		this.comment = comment;
	}
	@Override
	public String toString() {
		return "AppDetailsBean [id=" + id + ", title=" + title + ", ftitle="
				+ ftitle + ", introduce=" + introduce + ", icon=" + icon
				+ ", pics=" + pics + ", choice=" + choice + ", times=" + times
				+ ", creator_id=" + creator_id + ", creator_name="
				+ creator_name + ", creator_icon=" + creator_icon
				+ ", comment=" + comment + "]";
	}
	@Override
	public void parseData(String jsonData) {
		try {
			JSONObject object = new JSONObject(JsonObjectUitl.getString(new JSONObject(jsonData), "data"));
			setId(JsonObjectUitl.getInt(object, "id"));
			setTitle(JsonObjectUitl.getString(object, "title"));
			setFtitle(JsonObjectUitl.getString(object, "ftitle"));
			setIntroduce(JsonObjectUitl.getString(object, "introduce"));
			setIcon(JsonObjectUitl.getString(object, "icon"));
			setChoice(JsonObjectUitl.getInt(object, "choice"));
			setTimes(JsonObjectUitl.getString(object, "times"));
			setCreator_id(JsonObjectUitl.getInt(object, "creator_id"));
			setCreator_name(JsonObjectUitl.getString(object, "creator_name"));
			setCreator_icon(JsonObjectUitl.getString(object, "creator_icon"));
			setIs_digg(JsonObjectUitl.getInt(object, "is_digg") == 1 ? true : false);
			setIs_like(JsonObjectUitl.getInt(object, "is_like") == 1 ? true : false);
			setNow_p(JsonObjectUitl.getInt(object, "now_p"));
			setTotal_p(JsonObjectUitl.getInt(object, "total_p"));
			setPrice(JsonObjectUitl.getInt(object, "price"));
			setType_name(JsonObjectUitl.getString(object, "type_name"));
			setUrl(JsonObjectUitl.getString(object, "url"));
			setPkgname(JsonObjectUitl.getString(object, "pkgname"));
			setVersioncode(JsonObjectUitl.getInt(object, "versioncode"));
			
			setPics(JSON.parseArray(JsonObjectUitl.getString(object, "pics"),String.class));
			setDownload(JSON.parseArray(JsonObjectUitl.getString(object, "download_list"),DownloadBean.class));
			
			String comment_str = JsonObjectUitl.getString(object, "comment");
			List<String> parse = JSON.parseArray(comment_str,String.class);
			if (parse != null && parse.size() >0) {
				comment = new ArrayList<CommentBean>();
			}
			for (int i = 0; i < parse.size(); i++) {
				CommentBean bean = new CommentBean();
				String str = parse.get(i);
				JSONObject object2 = new JSONObject(str);
				bean.setComment(JsonObjectUitl.getString(object2, "comment"));
				bean.setDigg(JsonObjectUitl.getInt(object2, "digg"));
				bean.setId(JsonObjectUitl.getInt(object2, "id"));
				bean.setIs_more_comment(JsonObjectUitl.getInt(object2,"is_more_comment"));
				bean.setTimes(JsonObjectUitl.getString(object2, "times"));
				bean.setUser_icon(JsonObjectUitl.getString(object2, "user_icon"));
				bean.setUser_id(JsonObjectUitl.getInt(object2, "user_id"));
				bean.setUser_name(JsonObjectUitl.getString(object2, "user_name"));
				bean.setIs_digg(JsonObjectUitl.getInt(object2, "is_digg"));
				String reply = JsonObjectUitl.getString(object2, "reply");
				if (reply != null && !reply.equals("")) {
					List<ReplyBean2> array = JSON.parseArray(reply, ReplyBean2.class);
					bean.setReply(array);
				}
				comment.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
