package com.utils.model;

import java.io.Serializable;

public class DownloadMovie implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private String imageUrl;
	private String size;
	private String speed;
	private long speedNum;
	private boolean isSelect = false;
	private boolean isEdit = false;
	private boolean isDownloading = false;
	private String model_id;
	private String modelType_id;
	private boolean isEpisode = false;
	private int episode_id;
	private long progress;
	private long fileSize;
	private long fileNum;
	private String localUrl;
	private long lastSize;
	private String source_id;
	private String source_name;
	private int hd;
	private boolean isM3U8 = true;
	private boolean isSuccess = false;
	

	public long getSpeedNum() {
		return speedNum;
	}

	public void setSpeedNum(long speedNum) {
		this.speedNum = speedNum;
	}

	public long getFileNum() {
		return fileNum;
	}

	public void setFileNum(long fileNum) {
		this.fileNum = fileNum;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
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

	public long getLastSize() {
		return lastSize;
	}

	public void setLastSize(long lastSize) {
		this.lastSize = lastSize;
	}

	public String getLocalUrl() {
		return localUrl;
	}

	public void setLocalUrl(String localUrl) {
		this.localUrl = localUrl;
	}

	public long getProgress() {
		return progress;
	}

	public void setProgress(long progress) {
		this.progress = progress;
	}

	public boolean isDownloading() {
		return isDownloading;
	}

	public void setDownloading(boolean isDownloading) {
		this.isDownloading = isDownloading;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof DownloadMovie))
			return false;

		DownloadMovie that = (DownloadMovie) o;

		if (this.model_id.equals(that.model_id) && this.modelType_id.equals(that.modelType_id))
			return true;

		return false;
	}

}
