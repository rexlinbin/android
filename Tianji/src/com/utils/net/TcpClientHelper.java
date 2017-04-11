package com.utils.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.utils.tools.StringUtils;

import android.os.Handler;
import android.os.Message;

public class TcpClientHelper {
	private Socket mSocket;
	private InputStream inputStream = null;
	private PrintWriter mPrintWriter = null;
	private String ip;
	private int port;
	private Handler handler;
	
	private boolean isPCConnect = false;
	
	
	public TcpClientHelper(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	/**
	 * 创建客户端socket
	 * @return
	 */
	public boolean createClientSocket() {
		try {
			if (mSocket == null) {
				mSocket = new Socket(InetAddress.getByName(ip), port);
				inputStream = mSocket.getInputStream();
				mPrintWriter = new PrintWriter(mSocket.getOutputStream(), true);
				startClientReceive();
			}else {
				mSocket.close();
				mSocket = new Socket(InetAddress.getByName(ip), port);
				inputStream = mSocket.getInputStream();
				mPrintWriter = new PrintWriter(mSocket.getOutputStream(), true);
				startClientReceive();
			}
			return true;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public void closeSocket() {
		if (mSocket != null) {
			try {
				mSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 设置回调
	 * @param handler
	 */
	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	//用户下载列表{"cmd":"task_list","user_id":"1","auth_id":"085029c223cea0b7f00fbc405cf489c3"}
	//单个下载{"cmd":"task_list","user_id":"1","auth_id":"085029c223cea0b7f00fbc405cf489c3","task_id":"1"}
	//
	public void sendClientMessage(String message) {
		if (mSocket != null && mPrintWriter != null) {
			if (mSocket.isConnected()) {
				mPrintWriter.print(message);
				mPrintWriter.flush();
			}
		}
	}
	private Thread clientReceiveThread;
	private boolean isClientReceiveThreadStop = false;
	public void startClientReceive() {
		isClientReceiveThreadStop = false;
		clientReceiveThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				while (true) {
					if (isClientReceiveThreadStop) {
						break;
					}
					 try {
						Thread.sleep(200);
					    byte[] buffer = new byte[inputStream.available()];
					    while(inputStream.read(buffer)>0){
					    	String data = new String(buffer);
					    	JSONObject jsonObject = JSON.parseObject(data);
					    	String flag = jsonObject.getString("flat");
					    	if (flag != null && flag.equals("login_notice")) {
								JSONObject jsonObject2 = jsonObject.getJSONObject("info");
								String pc = jsonObject2.getJSONObject("online").getString("pc");
								if (StringUtils.isEmpty(pc)) {
									isPCConnect = false;
								}else {
									isPCConnect = true;
								}
							}else {
								if (handler != null) {
									Message message = handler.obtainMessage();
									message.what = 1;
									message.obj = data;
									handler.sendMessage(message);
								}
							}
					    	
					    	System.out.println(new String(buffer));
					    }
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		clientReceiveThread.start();
	}
	
	public void stopClientReceive(){
		isClientReceiveThreadStop = true;
	}
	
	public boolean getIsPCConnect(){
		return isPCConnect;
	}

}
