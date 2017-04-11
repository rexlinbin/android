package com.bccv.threedimensionalworld.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bccv.threedimensionalworld.Net.HttpClientUtil;
import com.bccv.threedimensionalworld.model.FengXingMovie;
import com.bccv.threedimensionalworld.model.Movie;
import com.bccv.threedimensionalworld.model.Movie2D;
import com.bccv.threedimensionalworld.model.MovieClassification;
import com.bccv.threedimensionalworld.model.MovieUrl;
import com.bccv.threedimensionalworld.tool.StringUtils;

public class MovieApi extends AppApi {
	public List<MovieClassification> getMovieClassificationList() {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		String result = util.sendGet(Url.movieTypeList_url, params);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {

						List<MovieClassification> list = null;
						list = JSON.parseArray(rtnStr,
								MovieClassification.class);

						return list;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public List<Movie> getMovieList(String type_id, int page, int count) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("type_id", type_id);
		params.put("page", page + "");
		params.put("count", count + "");
		String result = util.sendGet(Url.movieList_url, params);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {

						List<Movie> list = null;
						list = JSON.parseArray(rtnStr, Movie.class);

						return list;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public List<Movie> getSearchMovieList(String type_id, String area,
			String year, String genres) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("type_id", type_id);
		params.put("area", area);
		params.put("year", year);
		params.put("genres", genres);
		String result = util.sendGet(Url.movieSearchList_url, params);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {

						List<Movie> list = null;
						list = JSON.parseArray(rtnStr, Movie.class);

						return list;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public List<Movie> getInputSearchMovieList(String name) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("head_title", name);
		
		String result = util.sendGet(Url.movieInputSearchList_url, params);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						List<Movie> list = null;
						list = JSON.parseArray(rtnStr, Movie.class);
						return list;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public MovieUrl getMovieUrl(String movie_id, boolean is2D) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("movie_id", movie_id);
		String result;
		if (is2D) {
			result = util.sendGet(Url.other_movieUrl_url, params);
		} else {
			result = util.sendGet(Url.movieUrl_url, params);
		}
		if (result != null) {
			Log.e("getMovieUrl", result);
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						MovieUrl movieUrl = JSON.parseObject(rtnStr,
								MovieUrl.class);
						return movieUrl;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public List<Movie2D> getMovie2DUrl(String movie_id, boolean is2D) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("movie_id", movie_id);
		String result;
		if (is2D) {
			result = util.sendGet(Url.other_movieUrl_url, params);
		} else {
			result = util.sendGet(Url.movieUrl_url, params);
		}
		if (result != null) {
			Log.e("getMovieUrl", result);
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						List<Movie2D> list = JSON.parseArray(rtnStr, Movie2D.class);
						return list;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public MovieUrl getFengXingMovieUrl(String movie_id, boolean is2D) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		String result;
		if (is2D) {
			String urlString = "http://pm.funshion.com/v5/media/play?id="
					+ movie_id + "&cl=aphone&ve=2.4.1.2&mac=8c34fd7c7d51&uc=1";
			result = util.sendGet(urlString, params);
		} else {
			result = util.sendGet(Url.movieUrl_url, params);
		}
		if (result != null) {
			Log.e("getFengXingMovieUrl", result);
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				String retmsg = jsonObject.getString("retmsg");
				if (retmsg != null && retmsg.equals("ok")) {
					String rtnStr = jsonObject.getString("mp4");
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						List<FengXingMovie> movie = JSON.parseArray(rtnStr,
								FengXingMovie.class);
						String fengUrlString = movie.get(movie.size() - 1)
								.getHttp();

						MovieUrl movieUrl = getFengXingMovieMp4(fengUrlString);
						return movieUrl;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public String getFengXingMovieUrlString(String movie_id, boolean is2D) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		String result;
		if (is2D) {
			String urlString = "http://pm.funshion.com/v5/media/play?id="
					+ movie_id + "&cl=aphone&ve=2.4.1.2&mac=8c34fd7c7d51&uc=1";
			result = util.sendGet(urlString, params);
		} else {
			result = util.sendGet(Url.movieUrl_url, params);
		}
		if (result != null) {
			Log.e("getFengXingMovieUrl", result);
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				String retmsg = jsonObject.getString("retmsg");
				if (retmsg != null && retmsg.equals("ok")) {
					String rtnStr = jsonObject.getString("mp4");
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						List<FengXingMovie> movie = JSON.parseArray(rtnStr,
								FengXingMovie.class);
						String fengUrlString = movie.get(movie.size() - 1)
								.getHttp();

						return fengUrlString;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	
	public MovieUrl getFengXingMovieMp4(String url) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		String result = util.sendGet(url, params);

		if (result != null) {
			Log.e("getFengXingMovieUrl", result);
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				String retmsg = jsonObject.getString("return");
				if (retmsg != null && retmsg.equals("succ")) {
					String rtnStr = jsonObject.getString("playlist");
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						JSONArray movie = JSON.parseArray(rtnStr);
						com.alibaba.fastjson.JSONObject movieJsonObject = movie
								.getJSONObject(0);
						String movieString = movieJsonObject.getString("urls");
						List<String> urlStrings = JSON.parseArray(movieString,
								String.class);
						MovieUrl movieUrl = new MovieUrl();
						if (urlStrings.size() > 0) {
							movieUrl.setUrl_1(urlStrings.get(0));
						}
						if (result != null) {
							Log.e("getFengXingMovieUrl", urlStrings.get(0));
						}
						return movieUrl;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public Movie getMovieInfo(String id, boolean is2D) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("movie_id", id);
		String result;
		if (is2D) {
			result = util.sendGet(Url.other_movieInfo_url, params);
		} else {
			result = util.sendGet(Url.movieInfo_url, params);
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						Movie movie = JSON.parseObject(rtnStr, Movie.class);
						return movie;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
