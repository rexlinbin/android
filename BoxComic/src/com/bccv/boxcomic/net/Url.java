package com.bccv.boxcomic.net;

public class Url {
	public static String Host_url = "http://comicapi.boxuu.com/";

	public static String Test_rul = "https://comic.bccv.com/api/Index/index";

	// 1． 登陆
	public static String Login_url = Host_url + "v1/comic/login";
	// 修改个人信息
	public static String UpdateUser = Host_url + "v1/comic/updateuserinfo";
	// 获取章节漫画
	public static String ComicChapterContent_url = Host_url
			+ "v1/comic/chapter";

	// 获取章节漫画
		public static String ComicOnlineChapterContent_url = Host_url
				+ "v1/comic/url_chapter";
	
	// 获取频道列表
	public static String ChannelList_url = Host_url + "v1/comic/list_nav";

	// 获取频道列表
	public static String BookChannelList_url = Host_url + "v1/comic/book_nav";

	// 按栏目获取列表
	public static String CatList_url = Host_url + "v1/comic/list_cat";

	// 获取情报分析列表
	public static String InfoAnalysisList_url = Host_url
			+ "v1/comic/list_advices";

	// 获取情报分析内容
	public static String InfoAnalysis_url = Host_url + "v1/comic/info_advices";

	// 获取评论列表
	public static String CommentList_url = Host_url
			+ "v1/comic/comic_comment_list";

	// 获取我的评论列表
	public static String MyCommentList_url = Host_url
			+ "v1/comic/user_comment_list";

	// 获取回复列表
	public static String ReplyList_url = Host_url
			+ "v1/comic/report_comment_list/";

	// 发表评论
	public static String Comment_url = Host_url + "v1/comic/comment/";

	// 发表反馈
	public static String Feedback_url = Host_url + "v1/comic/feedback/";

	// 获取漫画详情
	public static String ComicInfo_url = Host_url + "v1/comic/content/";

	// 获取漫画来源详情
	public static String ComicOnlineInfo_url = Host_url + "v1/comic/url_content/";
	
	// 赞
	public static String Zan_url = Host_url + "v1/comic/digg/";

	// 获取最新漫画内容
	public static String listNew_url = Host_url + "v1/comic/list_new";
	// 获取最热漫画内容
	public static String listHot_url = Host_url + "v1/comic/list_hot";
	// 获取搜索列表
	public static String listSearch_list = Host_url + "v1/comic/search_list";
	// 搜索
	public static String Search = Host_url + "v1/comic/search";

	// 搜索
	public static String SystemInfo = Host_url + "v1/update/get_info";
	
	// 发送缺失页
	public static String SendLosePage = Host_url + "v1/comic/comic_miss";

	// 图书内容
	public static String EbookText = Host_url + "v1/comic/book_text";

	// 图书章节
	public static String EbookChapters = Host_url + "v1/comic/book_chapter";
	// 获取最新漫画内容
	public static String EbookContent = Host_url + "v1/comic/book_new";

}
