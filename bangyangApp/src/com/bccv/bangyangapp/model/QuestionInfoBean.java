package com.bccv.bangyangapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class QuestionInfoBean implements Parcelable{
	
//    id------------疑问 ID
//    user_id-------创建者ID
//    user_name-----创建者名称
//    user_icon-----创建者头像
//    content-------创建者评论
//    times---------创建时间
//    replay_num----回复数量
	
	public QuestionInfoBean(){}
	private QuestionInfoBean(Parcel in){
		
		id = in.readString();
		user_id = in.readString();
		user_name = in.readString();
		user_icon = in.readString();
		content = in.readString();
		reply_num = in.readString();
		times = in.readLong();
	}
	
	private String id;
	private String user_id;
	private String user_name;
	private String user_icon;
	private String content;
	private String reply_num;
	private long times;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_icon() {
		return user_icon;
	}
	public void setUser_icon(String user_icon) {
		this.user_icon = user_icon;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getTimes() {
		return times;
	}
	public void setTimes(long times) {
		this.times = times;
	}
	public String getReply_num() {
		return reply_num;
	}
	public void setReply_num(String reply_num) {
		this.reply_num = reply_num;
	}
	
	@Override
	public String toString() {
		return "QuestionInfoBean [id=" + id + ", user_id=" + user_id
				+ ", user_name=" + user_name + ", user_icon=" + user_icon
				+ ", content=" + content + ", times=" + times + ", reply_num="
				+ reply_num + "]";
	}
	
	public static final Parcelable.Creator<QuestionInfoBean> CREATOR = new Parcelable.Creator<QuestionInfoBean>() {
		@Override
		public QuestionInfoBean createFromParcel(Parcel in) {
			return new QuestionInfoBean(in);
		}

		@Override
		public QuestionInfoBean[] newArray(int size) {
			return new QuestionInfoBean[size];
		}
	};
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		out.writeString(id);
		out.writeString(user_id);
		out.writeString(user_name);
		out.writeString(user_icon);
		out.writeString(content);
		out.writeString(reply_num);
		out.writeLong(times);
		
	}
	
}
