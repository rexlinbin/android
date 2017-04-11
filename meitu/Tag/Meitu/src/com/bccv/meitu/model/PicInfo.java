package com.bccv.meitu.model;

import java.util.List;

/**
 *  专辑照片信息
 * 
 * @author liukai
 *	
 */
public class PicInfo {
	
	private String pic;
	private int is_free;
	
	
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public int getIs_free() {
		return is_free;
	}
	public void setIs_free(int is_free) {
		this.is_free = is_free;
	}
	
	
	@Override
	public String toString() {
		return "PicInfos [pic=" + pic + ", is_free=" + is_free
				+  "]";
	}
	
}
