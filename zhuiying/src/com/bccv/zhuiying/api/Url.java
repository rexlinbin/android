package com.bccv.zhuiying.api;

import com.utils.tools.GlobalParams;

public class Url {

	public static String Host_url = GlobalParams.dataUrl + "/an/v1/index?opt=";

	// 1.获取影视分类列表 (发现页面)
	public static String TypeList_url = Host_url + "type_list";
	public static String SearchMore_list = Host_url + "search_more";

	// 2.获取分类下电影列表
	public static String Movie_Type = Host_url + "movie_type";

	// 3.获取视频详情 (影片内容页)
	public static String Info = Host_url + "info";

	// 4.原来‘今日热门列表’ 现电影类别最热
	public static String Today_list = Host_url + "today_list";

	// 5.精选轮播列表 (首页轮播)
	public static String Top_list = Host_url + "top_list";

	// 6.专题列表 (专题列表页面)
	public static String Theme_list = Host_url + "theme_list";

	// 7. 专题下电影列表
	public static String ThemeMovielist = Host_url + "theme_movielist";

	// 8.获取视频筛选列表
	public static String VideoFindList = Host_url + "find_list";

	// 9.筛选
	public static String VideoFind = Host_url + "find";

	// 10.获取影片连接
	public static String GetMovie_url = Host_url + "get_url";
	// 10.获取影片连接
	public static String GetMovie_url_new = Host_url + "get_url_new";
	// 10.获取电视剧连接
	public static String GetVideo_url = Host_url + "get_url_an";
	// 10.获取电视剧连接
	public static String GetReal_url = GlobalParams.videoUrl + "/v2/moviean/play/?p=an";
	public static String GetTV_url = GlobalParams.videoUrl + "/v1/moviean/live.html?p=an";

	// 11.搜索推荐
	public static String Search_list = Host_url + "search_list";

	// 12.搜索影片
	public static String Search = Host_url + "search";

	// 13.获取最热下电影列表
	public static String MovieHot_url = Host_url + "hot_list";
	// 14.获取每个类型16个推荐影片
	public static String Video_16 = Host_url + "video_16";
	// 15.意见反馈
	public static String FeedBack = Host_url + "feedback";

	// 16.大家都在看

	public static String Moreurl = Host_url + "watching";

	// 17.直播
	public static String TVurl = Host_url + "zhibo";

}
