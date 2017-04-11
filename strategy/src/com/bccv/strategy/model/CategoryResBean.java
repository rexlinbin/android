package com.bccv.strategy.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bccv.strategy.network.NetResBean;
import com.bccv.strategy.utils.L;

public class CategoryResBean extends NetResBean {
	
	
	private ArrayList<CategoryBean> categorys;

	public ArrayList<CategoryBean> getCategorys() {
		return categorys;
	}

	public void setCategorys(ArrayList<CategoryBean> categorys) {
		this.categorys = categorys;
	}

	public class CategoryBean{
		
		private String id;
		private String name;
		private String f_name;
		
		public String getF_name() {
			return f_name;
		}

		public void setF_name(String f_name) {
			this.f_name = f_name;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "CategoryBean [id=" + id + ", name=" + name + "]";
		}

	}

	@Override
	public String toString() {
		return "CategoryResBean [categorys=" + categorys + ", success="
				+ success + ", status_code=" + status_code + "]";
	}

	@Override
	public void parseData(String jsonData) {
		L.v(TAG, "parseData", "jsonData: " + jsonData);
		if(success){
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				categorys = new ArrayList<CategoryBean>();
				for (int i = 0; i < jsonArray.length(); i++) {
					 JSONObject categoryObject = jsonArray.getJSONObject(i);
					CategoryBean categoryBean = new CategoryBean();
					categoryBean.id = categoryObject.optString("id");
					categoryBean.name = categoryObject.optString("cat_title");
					categoryBean.f_name = categoryObject.optString("f_name");
					categorys.add(categoryBean);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				L.e(TAG, "parseData", e.getMessage());
			}
		}

	}

	
}
