package com.bccv.boxcomic.download;

import com.bccv.boxcomic.modal.Chapter;
import com.lidroid.xutils.db.annotation.Transient;
import com.lidroid.xutils.http.HttpHandler;

import java.io.File;

/**
 * Author: wyouflf
 * Date: 13-11-10
 * Time: 下午8:11
 */
public class DownloadInfo {

    public DownloadInfo() {
    }

    private long id;

    @Transient
    private HttpHandler<File> handler;

    private HttpHandler.State state;

    private String downloadUrl;

    private String fileName;

    private String fileSavePath;

    private long progress;

    private long fileLength;

    private boolean autoResume;

    private boolean autoRename;
    
    private String app_idString;

    private String app_urlString;

    private String app_titleString;
    
    private String chapter_id;
    private String chapter_name;
    private String chapter_size;
    
    private String pageNum;
    
    
    
    public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public String getApp_titleString() {
		return app_titleString;
	}

	public void setApp_titleString(String app_titleString) {
		this.app_titleString = app_titleString;
	}

	public String getApp_urlString() {
		return app_urlString;
	}

	public void setApp_urlString(String app_urlString) {
		this.app_urlString = app_urlString;
	}

	public String getApp_idString() {
		return app_idString;
	}

	public void setApp_idString(String app_idString) {
		this.app_idString = app_idString;
	}

	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public HttpHandler<File> getHandler() {
        return handler;
    }

    public void setHandler(HttpHandler<File> handler) {
        this.handler = handler;
    }

    public HttpHandler.State getState() {
        return state;
    }

    public void setState(HttpHandler.State state) {
        this.state = state;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSavePath() {
        return fileSavePath;
    }

    public void setFileSavePath(String fileSavePath) {
        this.fileSavePath = fileSavePath;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public boolean isAutoResume() {
        return autoResume;
    }

    public void setAutoResume(boolean autoResume) {
        this.autoResume = autoResume;
    }

    public boolean isAutoRename() {
        return autoRename;
    }

    public void setAutoRename(boolean autoRename) {
        this.autoRename = autoRename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DownloadInfo)) return false;

        DownloadInfo that = (DownloadInfo) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

	public String getChapter_id() {
		return chapter_id;
	}

	public void setChapter_id(String chapter_id) {
		this.chapter_id = chapter_id;
	}

	public String getChapter_name() {
		return chapter_name;
	}

	public void setChapter_name(String chapter_name) {
		this.chapter_name = chapter_name;
	}

	public String getChapter_size() {
		return chapter_size;
	}

	public void setChapter_size(String chapter_size) {
		this.chapter_size = chapter_size;
	}

	
}
