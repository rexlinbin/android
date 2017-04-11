package com.bccv.boxcomic.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;

import com.bccv.boxcomic.modal.Chapter;
import com.bccv.boxcomic.modal.Comic;
import com.bccv.boxcomic.modal.ComicPic;
import com.bccv.boxcomic.modal.OnlineFrom;
import com.bccv.boxcomic.net.HttpClientUtil;
import com.bccv.boxcomic.net.Url;
import com.bccv.boxcomic.tool.AppApi;
import com.bccv.boxcomic.tool.GlobalParams;
import com.bccv.boxcomic.tool.Logger;
import com.bccv.boxcomic.tool.StringUtils;

public class ComicApi extends AppApi {
	public List<ComicPic> getComicChapterContent(String comicId){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", comicId);
		String result = util.sendGet(Url.ComicChapterContent_url, params);
		Logger.e("getComicChapterContent", result);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					JSONObject comicJsonObject = jsonObject.getJSONObject("data");
					GlobalParams.nextComicString = comicJsonObject.getString("next");
					GlobalParams.preComicString = comicJsonObject.getString("pre");
					JSONObject chapterJsonObject = comicJsonObject.getJSONObject("chapter");
					
					String rtnStr = chapterJsonObject.getString("chapter_pics_hd");
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						
						List<ComicPic> list = null;
						list = JSON.parseArray(rtnStr, ComicPic.class);

						return list;
					}else {
						List<ComicPic> list = null;
						return list;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public List<ComicPic> getOnlineComicChapterContent(String comicId, String menu_id){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", comicId);
		params.put("menu_id", menu_id);
		String result = util.sendGet(Url.ComicOnlineChapterContent_url, params);
		Logger.e("getComicChapterContent",comicId + "," + menu_id+":"+result);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					JSONObject comicJsonObject = jsonObject.getJSONObject("data");
					GlobalParams.nextComicString = comicJsonObject.getString("next");
					GlobalParams.preComicString = comicJsonObject.getString("pre");
					JSONObject chapterJsonObject = comicJsonObject.getJSONObject("chapter");
					
					String rtnStr = chapterJsonObject.getString("chapter_pics_hd");
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						
						List<ComicPic> list = null;
						list = JSON.parseArray(rtnStr, ComicPic.class);

						return list;
					}else {
						List<ComicPic> list = null;
						return list;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public Chapter getComicDownloadChapterContent(String comicId, String menu_id){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", comicId);
		params.put("menu_id", menu_id);
		String result = util.sendGet(Url.ComicOnlineChapterContent_url, params);
		Logger.e("getComicChapterContent", result);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					JSONObject comicJsonObject = jsonObject.getJSONObject("data");
					GlobalParams.nextComicString = comicJsonObject.getString("next");
					GlobalParams.preComicString = comicJsonObject.getString("pre");
					Chapter chapter = null;
					JSONObject chapterJsonObject = comicJsonObject.getJSONObject("chapter");

					chapter = JSON.parseObject(chapterJsonObject.toString(), Chapter.class);
					String rtnStr = chapterJsonObject.getString("chapter_pics_hd");
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						
						List<ComicPic> listp = null;
						listp = JSON.parseArray(rtnStr, ComicPic.class);
						chapter.setComicPics(listp);
					}
					return chapter;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public Comic getComicInfo(String comic_id){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", comic_id);
		String result = util.sendGet(Url.ComicInfo_url, params);
		Logger.e("getComicInfo", result);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					JSONObject comicJsonObject = jsonObject.getJSONObject("data");
					
					String rtnStr = comicJsonObject.getString("comic");
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						
						Comic comic = null;
						comic = JSON.parseObject(rtnStr, Comic.class);
						
						String chapterNum = comicJsonObject.getString("chapter");
						List<Chapter> list;
						if (StringUtils.isEmpty(chapterNum) || chapterNum.equals("0")) {
							list = new ArrayList<Chapter>();
						}else {
							list = JSON.parseArray(chapterNum, Chapter.class);
						}
						comic.setChapters(list);
						return comic;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public Comic getComicOnlineInfo(String comic_id, String menu_id){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", comic_id);
		params.put("menu_id", menu_id);
		String result = util.sendGet(Url.ComicOnlineInfo_url, params);
		Logger.e("getComicInfo", result);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					JSONObject comicJsonObject = jsonObject.getJSONObject("data");
					
					String rtnStr = comicJsonObject.getString("comic");
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						
						Comic comic = null;
						comic = JSON.parseObject(rtnStr, Comic.class);
						
						String chapterNum = comicJsonObject.getString("chapter");
						List<Chapter> list;
						if (StringUtils.isEmpty(chapterNum) || chapterNum.equals("0")) {
							list = new ArrayList<Chapter>();
						}else {
							list = JSON.parseArray(chapterNum, Chapter.class);
						}
						comic.setChapters(list);
						
						String onlineNum = comicJsonObject.getString("online");
						List<OnlineFrom> onList;
						if (StringUtils.isEmpty(onlineNum) || onlineNum.equals("0")) {
							onList = new ArrayList<OnlineFrom>();
						}else {
							onList = JSON.parseArray(onlineNum, OnlineFrom.class);
						}
						comic.setOnlineFroms(onList);
						
						return comic;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public void setZan(String comic_id){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("comic_id", comic_id);
		String result = util.sendGet(Url.Zan_url, params);
		Logger.e("setZan", result);
		
		
	}
}
