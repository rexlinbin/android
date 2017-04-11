package com.bccv.tianji.api;

import com.utils.tools.GlobalParams;
import com.utils.tools.MD5Util;

public class TcpApi {
	
	public void login(String user_id){
		String message = "||{ \"cmd\":\"login\",\"os\":\"iphone\",\"user_id\":\""+user_id+"\",\"md5\":\""+MD5Util.string2MD5(user_id + "iphone")+"\"}";
//		String testmessage = "{ \"cmd\":\"login\",\"os\":\"iphone\",\"user_id\":\""+user_id+"\",\"md5\":\""+"e63569d37e4a964ba56863557fb2548e"+"\"}";
		GlobalParams.tcpClientHelper.sendClientMessage(message);
	}
	
	public void download(String task_id, String user_id, String game_id, String auth_id){
		String testmessage = "||{ \"cmd\":\"download\",\"game_id\":\""+game_id+"\",\"auth_id\":\""+auth_id+"\",\"user_id\":\""+user_id+"\"}";
		GlobalParams.tcpClientHelper.sendClientMessage(testmessage);
	}
	
	public void startDownload(String task_id, String user_id, String game_id, String auth_id){
		String testmessage = "||{ \"cmd\":\"task\",\"operation\":\"start\",\"task_id\":\""+task_id+"\",\"game_id\":\""+game_id+"\",\"auth_id\":\""+auth_id+"\",\"user_id\":\""+user_id+"\"}";
		GlobalParams.tcpClientHelper.sendClientMessage(testmessage);
	}
	
	public void pauseDownload(String task_id, String user_id, String game_id, String auth_id){
		String testmessage = "||{ \"cmd\":\"task\",\"operation\":\"stop\",\"task_id\":\""+task_id+"\",\"game_id\":\""+game_id+"\",\"auth_id\":\""+auth_id+"\",\"user_id\":\""+user_id+"\"}";
		GlobalParams.tcpClientHelper.sendClientMessage(testmessage);
	}
	
	public void deleteDownload(String task_id, String user_id, String game_id, String auth_id){
		String testmessage = "||{ \"cmd\":\"task\",\"operation\":\"delete\",\"task_id\":\""+task_id+"\",\"game_id\":\""+game_id+"\",\"auth_id\":\""+auth_id+"\",\"user_id\":\""+user_id+"\"}";
		GlobalParams.tcpClientHelper.sendClientMessage(testmessage);
	}
	
	public void getDownloadGameState(String task_id, String user_id, String auth_id){
		String testmessage = "||{ \"cmd\":\"task_list\",\"task_id\":\""+task_id+"\",\"auth_id\":\""+auth_id+"\",\"user_id\":\""+user_id+"\"}";
		GlobalParams.tcpClientHelper.sendClientMessage(testmessage);
	}
	
	public void getDownloadGameListState(String user_id, String auth_id){
		String testmessage = "||{ \"cmd\":\"task_list\",\"auth_id\":\""+auth_id+"\",\"user_id\":\""+user_id+"\"}";
		GlobalParams.tcpClientHelper.sendClientMessage(testmessage);
	}
}
