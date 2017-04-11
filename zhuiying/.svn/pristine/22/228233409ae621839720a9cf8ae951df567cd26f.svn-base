package com.bccv.zhuiying.api;




import org.json.JSONException;
import org.json.JSONObject;

/**
 * 与服务器交互数据接口。
 * @author WuYelin
 *
 */
public class AppApi {
	
	@SuppressWarnings("unused")
	private static final String TAG = "AppApi";

	/**
	 * 检验服务器返回的json是否获取数据成功。
	 * @param jsonObject
	 * @return
	 */
	public static boolean checkResponse(JSONObject jsonObject) {
		try {
			if (jsonObject.has("status")) {
				String response = jsonObject.getString("status");
				if (!"1".equals(response)) {
//					String errorInfo = jsonObject.getString("error");
					return false;
				} else {
					return true;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;

	}
}
