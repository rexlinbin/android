package com.bccv.meitu.sns.bean;

import java.io.Serializable;



public class ShareInfo implements Serializable {

	private static final long serialVersionUID = -9081492232939488049L;

	public ShareInfo(int SNSType){
		this.SNSType = SNSType;
	}
	
	public ShareInfo(){}
	
	public void setSNSType(int SNSType){
		this.SNSType = SNSType;
	}
	
	public int SNSType; //分享的平台
	
	public String title;//标题   QQ空间分享 微信分享  必须有
	public String titleUrl;//标题的超链接   QQ空间分享   微信分享 必须有
	public String text;//分享内容文本 
	public String imagePath;//本地图片路径
	public String imageUrl;//网络图片路径
	public String url;//分享链接链接

	@Override
	public String toString() {
		return "ShareInfo [SNSType=" + SNSType  + ", title=" + title + ", titleUrl=" + titleUrl
				+ ", text=" + text + ", imagePath=" + imagePath + ", imageUrl="	+ imageUrl + ", url=" + url + "]";
	}
	

}
