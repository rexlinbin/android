package com.boxuu.gamebox.sns.bean;

import java.io.Serializable;

public class SNSUserInfo implements Serializable {

	private static final long serialVersionUID = 3970376836574641985L;
	
	public int snsType; //sns类型
						//	ISNSLoginManager.SINA_WEIBO_TYPE
						//	ISNSLoginManager.TENCENT_WEIBO_TYPE
						//	ISNSLoginManager.TENCENT_QQ_TYPE
						//	ISNSLoginManager.TENCENT_WEIXIN_TYPE
	public String nickName; //昵称
	public String headPic; //头像url  需要判断是否为http://开头 若不是 则无头像
	public String accessToken; //token
	public String expiresIn; //expiresIn
	public String expiresTime; //expiresTime
	public String thirdPlatformID; //三方id
	public String unionid;//微信unionid

	@Override
	public String toString() {
		return "SNSUserInfo [snsType=" + snsType + ", nickName=" + nickName
				+ ", headPic=" + headPic + ", accessToken=" + accessToken
				+ ", expiresIn=" + expiresIn + ", expiresTime=" + expiresTime
				+ ", thirdPlatformID=" + thirdPlatformID + "]";
	}
}