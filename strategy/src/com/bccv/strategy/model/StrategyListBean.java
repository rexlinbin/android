package com.bccv.strategy.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bccv.strategy.network.NetResBean;
import com.bccv.strategy.utils.L;

public class StrategyListBean extends NetResBean {

	// status----------系统状态码
	// now_p-----------当前页码
	// total_p---------总页码
	// gameinfo--------该游戏详情
	// game_id---------游戏ID
	// game_title------游戏标题
	// game_icon-------游戏图标、
	// cat_title-------所属分类名称
	// data------------攻略列表
	// id--------------攻略ID
	// news_click------点击量
	// news_digg-------点赞数量
	// news_comment----评论数量
	// news_focus------关注量
	// creator_id------攻略创建者ID
	// creator_name----攻略创建者名称
	// creator_icon----攻略创建者头像
	// news_ctime------攻略发布时间
	// game_icon-------游戏图标
	// news_title------攻略标题
	// news_titlepic---攻略标题图
	// news_ftitle-----攻略副标题

	private int now_p; // 当前页
	private int total_p; // 总共页
	private GameInfo gameinfo;
	private ArrayList<GameStrategyInfo> gameStrategyList;

	public static class GameInfo {
		private String game_id;
		private String game_title;
		private String game_icon;
		private String cat_title;
		private boolean is_game_focus;
		private boolean is_game_digg;

		public String getGame_id() {
			return game_id;
		}

		public void setGame_id(String game_id) {
			this.game_id = game_id;
		}

		public String getGame_title() {
			return game_title;
		}

		public void setGame_title(String game_title) {
			this.game_title = game_title;
		}

		public String getGame_icon() {
			return game_icon;
		}

		public void setGame_icon(String game_icon) {
			this.game_icon = game_icon;
		}

		public String getCat_title() {
			return cat_title;
		}

		public void setCat_title(String cat_title) {
			this.cat_title = cat_title;
		}

		public boolean is_game_focus() {
			return is_game_focus;
		}

		public void set_game_focus(boolean is_game_focus) {
			this.is_game_focus = is_game_focus;
		}

		public boolean is_game_digg() {
			return is_game_digg;
		}

		public void set_game_digg(boolean is_game_digg) {
			this.is_game_digg = is_game_digg;
		}

		@Override
		public String toString() {
			return "GameInfo [game_id=" + game_id + ", game_title="
					+ game_title + ", game_icon=" + game_icon + ", cat_title="
					+ cat_title + ", is_game_focus=" + is_game_focus
					+ ", is_game_digg=" + is_game_digg + "]";
		}
		
	}

	public static class GameStrategyInfo {

		private String id;
		private String news_click;
		private int news_digg;
		private int news_comment;
		private String news_focus;
		private String creator_id;
		private String creator_name;
		private String creator_icon;
		private String news_ctime;
		private String game_icon;
		private String news_title;
		private String news_titlepic;
		private String news_ftitle;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getNews_click() {
			return news_click;
		}

		public void setNews_click(String news_click) {
			this.news_click = news_click;
		}

		public int getNews_digg() {
			return news_digg;
		}

		public void setNews_digg(int news_digg) {
			this.news_digg = news_digg;
		}

		public int getNews_comment() {
			return news_comment;
		}

		public void setNews_comment(int news_comment) {
			this.news_comment = news_comment;
		}

		public String getNews_focus() {
			return news_focus;
		}

		public void setNews_focus(String news_focus) {
			this.news_focus = news_focus;
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

		public String getNews_ctime() {
			return news_ctime;
		}

		public void setNews_ctime(String news_ctime) {
			this.news_ctime = news_ctime;
		}

		public String getGame_icon() {
			return game_icon;
		}

		public void setGame_icon(String game_icon) {
			this.game_icon = game_icon;
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

		public String getNews_ftitle() {
			return news_ftitle;
		}

		public void setNews_ftitle(String news_ftitle) {
			this.news_ftitle = news_ftitle;
		}

		@Override
		public String toString() {
			return "GameStrategyInfo [id=" + id + ", news_click=" + news_click
					+ ", news_digg=" + news_digg + ", news_focus=" + news_focus
					+ ", creator_id=" + creator_id + ", creator_name="
					+ creator_name + ", creator_icon=" + creator_icon
					+ ", news_ctime=" + news_ctime + ", game_icon=" + game_icon
					+ ", news_title=" + news_title + ", news_titlepic="
					+ news_titlepic + ", news_ftitle=" + news_ftitle + "]";
		}

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


	public GameInfo getGameinfo() {
		return gameinfo;
	}


	public void setGameinfo(GameInfo gameinfo) {
		this.gameinfo = gameinfo;
	}


	public ArrayList<GameStrategyInfo> getGameStrategyList() {
		return gameStrategyList;
	}


	public void setGameStrategyList(ArrayList<GameStrategyInfo> gameStrategyList) {
		this.gameStrategyList = gameStrategyList;
	}


	@Override
	public String toString() {
		return "StrategyListBean [now_p=" + now_p + ", total_p=" + total_p
				+ ", gameinfo=" + gameinfo + ", gameStrategyList="
				+ gameStrategyList + ", success=" + success + ", status_code="
				+ status_code + "]";
	}


	@Override
	public void parseData(String jsonData) {
		L.v(TAG, "parseData", "jsonData: " + jsonData);
		if (success) {
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				now_p = jsonObject.optInt("now_p");
				total_p = jsonObject.optInt("total_p");
				
				// id--------------攻略ID
				// news_click------点击量
				// news_digg-------副标题
				// news_focus------关注量
				// creator_id------攻略创建者ID
				// creator_name----攻略创建者名称
				// creator_icon----攻略创建者头像
				// news_ctime------攻略发布时间
				// game_icon-------游戏图标
				// news_title------攻略标题
				// news_titlepic---攻略标题图
				// news_ftitle-----攻略副标题
				
				
				JSONObject gameinfoObj = jsonObject.getJSONObject("gameinfo");
				gameinfo = new GameInfo();
				gameinfo.game_id = gameinfoObj.optString("game_id");
				gameinfo.game_title = gameinfoObj.optString("game_title");
				gameinfo.game_icon = gameinfoObj.optString("game_icon");
				gameinfo.cat_title = gameinfoObj.optString("cat_title");
				gameinfo.is_game_focus = gameinfoObj.optInt("is_game_focus") == 1;
				gameinfo.is_game_digg = gameinfoObj.optInt("is_game_digg") == 1;
				
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				gameStrategyList = new ArrayList<GameStrategyInfo>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject dataObject = jsonArray.getJSONObject(i);
					GameStrategyInfo gameStrategyInfo = new GameStrategyInfo();
					gameStrategyInfo.id = dataObject.optString("id");
					gameStrategyInfo.news_click = dataObject.optString("news_click");
					gameStrategyInfo.news_digg = dataObject.optInt("news_digg");
					gameStrategyInfo.news_comment = dataObject.optInt("news_comment");
					gameStrategyInfo.news_focus = dataObject.optString("news_focus");
					gameStrategyInfo.creator_id = dataObject.optString("creator_id");
					gameStrategyInfo.creator_name = dataObject.optString("creator_name");
					gameStrategyInfo.creator_icon = dataObject.optString("creator_icon");
					gameStrategyInfo.news_ctime = dataObject.optString("news_ctime");
					gameStrategyInfo.game_icon = dataObject.optString("game_icon");
					gameStrategyInfo.news_title = dataObject.optString("news_title");
					gameStrategyInfo.news_titlepic = dataObject.optString("news_titlepic");
					gameStrategyInfo.news_ftitle = dataObject.optString("news_ftitle");
					gameStrategyList.add(gameStrategyInfo);
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				success = false;
				e.printStackTrace();
				L.e(TAG, "parseData", e.getMessage());
			}
		}
	}

}
