package com.utils.download;



import com.lidroid.xutils.db.annotation.Transient;
import com.lidroid.xutils.http.HttpHandler;

import java.io.File;
import java.io.Serializable;

import org.xutils.common.Callback.Cancelable;

/**
 * Author: wyouflf
 * Date: 13-11-10
 * Time: 下午8:11
 */
public class DownloadInfo implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public DownloadInfo() {
    }

    private long id;

    @Transient
    private HttpHandler<File> handler;
    
    private Cancelable cancelable;

    public Cancelable getCancelable() {
		return cancelable;
	}

	public void setCancelable(Cancelable cancelable) {
		this.cancelable = cancelable;
	}

	private HttpHandler.State state;

    private String downloadUrl;

    private String fileName;

    private String fileSavePath;

    private long progress;

    private long fileLength;

    private boolean autoResume;

    private boolean autoRename;
    
    private String model_id;
    private String modelType_id;
    private String modelImage_url;
    private String model_title;
    private boolean isEpisode;
    private int episode_id;
    private boolean isSelect = false;
    private boolean isEdit = false;
    
    private String localUrl;
    private long lastProgress;
    private String source_id;
    private String source_name;
	private int hd;
	private boolean isM3U8 = true;
	private boolean isSuccess = false;
	private long totalSize = 0;
	

	

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public boolean isM3U8() {
		return isM3U8;
	}

	public void setM3U8(boolean isM3U8) {
		this.isM3U8 = isM3U8;
	}

	public String getSource_name() {
		return source_name;
	}

	public void setSource_name(String source_name) {
		this.source_name = source_name;
	}

	public int getHd() {
		return hd;
	}

	public void setHd(int hd) {
		this.hd = hd;
	}

	public String getSource_id() {
		return source_id;
	}

	public void setSource_id(String source_id) {
		this.source_id = source_id;
	}

	public long getLastProgress() {
		return lastProgress;
	}

	public void setLastProgress(long lastProgress) {
		this.lastProgress = lastProgress;
	}

	public String getLocalUrl() {
		return localUrl;
	}

	public void setLocalUrl(String localUrl) {
		this.localUrl = localUrl;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	public boolean isEdit() {
		return isEdit;
	}

	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}

	public String getModel_id() {
		return model_id;
	}

	public void setModel_id(String model_id) {
		this.model_id = model_id;
	}

	public String getModelType_id() {
		return modelType_id;
	}

	public void setModelType_id(String modelType_id) {
		this.modelType_id = modelType_id;
	}

	public String getModelImage_url() {
		return modelImage_url;
	}

	public void setModelImage_url(String modelImage_url) {
		this.modelImage_url = modelImage_url;
	}

	public String getModel_title() {
		return model_title;
	}

	public void setModel_title(String model_title) {
		this.model_title = model_title;
	}

	public boolean isEpisode() {
		return isEpisode;
	}

	public void setEpisode(boolean isEpisode) {
		this.isEpisode = isEpisode;
	}

	public int getEpisode_id() {
		return episode_id;
	}

	public void setEpisode_id(int episode_id) {
		this.episode_id = episode_id;
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

        if (this.model_id.equals(that.model_id) && this.modelType_id.equals(that.modelType_id) && episode_id == that.episode_id)
			return true;

        return false;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
