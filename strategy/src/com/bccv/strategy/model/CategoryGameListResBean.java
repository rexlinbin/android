package com.bccv.strategy.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bccv.strategy.network.NetResBean;
import com.bccv.strategy.utils.L;

public class CategoryGameListResBean extends NetResBean {

	// private int now_p; //当前页
	// private int total_p; //总共页
	// private ArrayList<AppInfoItemBean> appInfoItemBeans;
	//
	// public int getNow_p() {
	// return now_p;
	// }
	//
	// public void setNow_p(int now_p) {
	// this.now_p = now_p;
	// }
	//
	// public int getTotal_p() {
	// return total_p;
	// }
	//
	// public void setTotal_p(int total_p) {
	// this.total_p = total_p;
	// }
	//
	// public ArrayList<AppInfoItemBean> getAppInfoItemBeans() {
	// return appInfoItemBeans;
	// }
	//
	// public void setAppInfoItemBeans(ArrayList<AppInfoItemBean>
	// appInfoItemBeans) {
	// this.appInfoItemBeans = appInfoItemBeans;
	// }

	// status----------系统状态码
	// game_id---------游戏ID
	// game_title------游戏标题
	// game_icon-------游戏图标
	//

	public static class CategoryGameInfo {
		private String game_id;
		private String game_title;
		private String game_icon;

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

		@Override
		public String toString() {
			return "CategoryGameInfo [game_id=" + game_id + ", game_title="
					+ game_title + ", game_icon=" + game_icon + "]";
		}

	}

	private ArrayList<CategoryGameInfo> gameInfoList;

	public ArrayList<CategoryGameInfo> getGameInfoList() {
		return gameInfoList;
	}

	public void setGameInfoList(ArrayList<CategoryGameInfo> gameInfoList) {
		this.gameInfoList = gameInfoList;
	}

	@Override
	public String toString() {
		return "CategoryGameListResBean [gameInfoList=" + gameInfoList
				+ ", success=" + success + ", status_code=" + status_code + "]";
	}

	@Override
	public void parseData(String jsonData) {
		L.v(TAG, "parseData", "jsonData: " + jsonData);
		if (success) {
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				gameInfoList = new ArrayList<CategoryGameInfo>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject dataObject = jsonArray.getJSONObject(i);
					CategoryGameInfo gameInfo = new CategoryGameInfo();
					gameInfo.game_id = dataObject.optString("game_id");
					gameInfo.game_title = dataObject.optString("game_title");
					gameInfo.game_icon = dataObject.optString("game_icon");
					gameInfoList.add(gameInfo);
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
