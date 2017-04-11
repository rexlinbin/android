package com.bccv.ebook.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.bccv.ebook.utils.FileUtils;
import com.bccv.ebook.utils.L;
import com.bccv.ebook.utils.SystemUtil;

import android.content.Context;

public class BookInfo implements Serializable{
	
	private static final long serialVersionUID = 5294349855940807114L;

	private static final String TAG = "BookInfo";
	
	private int id;
	private String name;
	private String path;
	private String zippath;	//压缩文件
	private String account; //目录文件
	private String cover;	//封面
	private ArrayList<ChapterInfo> chapterInfos;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public ArrayList<ChapterInfo> getChapterInfos() {
		return chapterInfos;
	}
	
	public void setChapterInfos(ArrayList<ChapterInfo> chapterInfos) {
		this.chapterInfos = chapterInfos;
	}
	
	public String getZippath() {
		return zippath;
	}

	public void setZippath(String zippath) {
		this.zippath = zippath;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	@Override
	public String toString() {
		return "BookInfo [id=" + id + ", name=" + name + ", path=" + path
				+ ", zippath=" + zippath + ", account=" + account + ", cover="
				+ cover + ", chapterInfos=" + chapterInfos + "]";
	}

	/**
	 * 解析章节信息
	 * 
	 * @param account	章节配置文件
	 */
	public void parser(Context context,String account,int bookId){
		InputStream inStream = null;
		try {
			inStream = context.getAssets().open(account);
			String data = FileUtils.readInStream(inStream);
			JSONObject jsonObject = new JSONObject(data);
			JSONArray jsonArray = jsonObject.getJSONArray("chapters");
			if(chapterInfos==null){
				chapterInfos = new ArrayList<ChapterInfo>();
			}
			chapterInfos.clear();
			
			String eBookCacheFolder = SystemUtil.getEBookCacheFolder(SystemUtil.UNZIP_BOOK_TYPE);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject chapterJson = jsonArray.getJSONObject(i);
				ChapterInfo chapterInfo = new ChapterInfo();
				chapterInfo.setName(chapterJson.getString("name"));
				chapterInfo.setFileName(chapterJson.getString("filename"));
				chapterInfo.setId(chapterJson.getInt("id"));
				chapterInfo.setIndex(i);
				chapterInfo.setPath(eBookCacheFolder+File.separator+bookId+File.separator+chapterInfo.getFileName());
				chapterInfos.add(chapterInfo);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			L.e(TAG, "parser", e.getMessage());
		}finally{
			if(inStream!=null){
				try {
					inStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
}
