package com.bccv.zhuiyingzhihanju.model;

import com.utils.model.DownloadMovie;

public class Collect {
/**
 * type_id------分类ID
         video_id-----视频ID
         times--------收藏时间
         info---------视频详情
             title-----标题
             images----图片
             rating----评分
             summary---简介
             short_summary---一句话简介
 */
	
	private String type;
	private String id;
	private String title;
	private String images;
	private String bimages;
	private String des;
	private boolean isSelect;
	private boolean isEdit = false;
	
	
	
	
	public String getDes() {
		return des;
	}



	public void setDes(String des) {
		this.des = des;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getImages() {
		return images;
	}



	public void setImages(String images) {
		this.images = images;
	}



	public String getBimages() {
		return bimages;
	}



	public void setBimages(String bimages) {
		this.bimages = bimages;
	}



	public boolean isEdit() {
		return isEdit;
	}



	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}



	public boolean isSelect() {
		return isSelect;
	}



	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}






	


	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Collect))
			return false;

		Collect that = (Collect) o;

		if (this.id.equals(that.id) && this.type.equals(that.type))
			return true;

		return false;
	}
	

	public class CollectInfo {
		private String title;
		private String images;
		private String rating;
		private String summary;
		private String short_summary;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getImages() {
			return images;
		}
		public void setImages(String images) {
			this.images = images;
		}
		public String getRating() {
			return rating;
		}
		public void setRating(String rating) {
			this.rating = rating;
		}
		public String getSummary() {
			return summary;
		}
		public void setSummary(String summary) {
			this.summary = summary;
		}
		public String getShort_summary() {
			return short_summary;
		}
		public void setShort_summary(String short_summary) {
			this.short_summary = short_summary;
		}
		
	}
}
