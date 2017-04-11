package com.utils.net;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.utils.tools.FileUtils;
import com.utils.tools.Logger;

import android.os.Handler;
import android.os.Message;

public class TcpServerHelper {
	private List<Machine> clientsList;
	private boolean isStarting = false;
	private ServerSocket ss = null;

	public TcpServerHelper() {
		clientsList = new ArrayList<Machine>();
	}

	/**
	 * 开启服务
	 * @param port 端口
	 * @param handler 回调 1--连接成功   3--返回数据
	 * @return
	 */
	public boolean startServer(final int port, final Handler handler, final String url) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				isStarting = true;
				try {
					ss = new ServerSocket(port);
					// 阻塞，等待连接
					while (true) {

						final Socket s = ss.accept();
						InetAddress inetAddress = s.getInetAddress();
						Machine machine = new Machine();
						machine.setSocket(s);
						machine.setGameBoxIp(inetAddress.getHostAddress());
						machine.setGameBoxName(inetAddress.getHostName());
						clientsList.add(machine);
						if (handler != null) {
							handler.sendEmptyMessage(1);
						}
						new Thread(new ServerThread(machine, handler, url)).start();
					}

					// // 每当客户端连接后启动一条ServerThread线程为该客户端服务
					// new Thread(new ServerThread(s, handler)).start();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();

		return true;
	}

	public boolean getIsStarting() {
		return isStarting;
	}

	public void closeServer() {
		if (ss != null) {
			try {
				ss.close();
				isStarting = false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public List<Machine> getClientSockets() {
		return clientsList;
	}

	public boolean send(Socket socket, String message) {
		try {
			if (socket.isConnected()) {
				// PrintWriter mPrintWriter = new
				// PrintWriter(socket.getOutputStream(), true);
				//
				// mPrintWriter.print(message);
				// mPrintWriter.flush();

				byte[] bytes = message.getBytes();
				byte[] b2 = new byte[bytes.length + 1];
				for (int i = 0; i < bytes.length; i++) {
					b2[i] = bytes[i];
				}
				OutputStream os = socket.getOutputStream();
				os.write(b2);
				os.flush();

				return true;
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	class ServerThread implements Runnable {
		// 定义当前线程负责处理的Socket
		private Machine machine;
		private Socket mSocket;
		private InputStream inputStream = null;
		private OutputStream outputStream = null;
		@SuppressWarnings("unused")
		private PrintWriter mPrintWriter = null;
		private Handler handler;
		private String url;

		// 定义该线程所处理的Socket所对应的输入流

		public ServerThread(Machine machine, Handler handler, String url) throws IOException {
			this.machine = machine;
			this.mSocket = machine.getSocket();
			this.handler = handler;
			this.url = url;
			// 初始化该Socket对应的输入流
			inputStream = mSocket.getInputStream();
//			mPrintWriter = new PrintWriter(mSocket.getOutputStream(), true);
			outputStream = mSocket.getOutputStream();
		}

		public void run() {
			while (true) {
				 try {
//					Thread.sleep(200);
					System.setProperty("http.keepAlive", "false");
					 HttpURLConnection conn;
						try {
//							conn = (HttpURLConnection) new URL(url).openConnection();
//							conn.setRequestProperty("User-agent",
//									"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.215 Safari/535.1");
//							conn.setRequestProperty("accept-language", "zh-CN");
//							conn.setRequestProperty("Cookie", "FTN5K=1542fc15");
//							conn.setConnectTimeout(5 * 1000);// 5秒的链接超时
//							conn.setReadTimeout(5 * 1000);// 设置从主机读取数据超时（单位：毫秒）
//							conn.setInstanceFollowRedirects(true);
//							conn.setRequestMethod("GET");
//							conn.connect();
							DataOutputStream out = new DataOutputStream(new BufferedOutputStream(  
				                    outputStream));
							FileInputStream fileInputStream = new FileInputStream("/storage/sdcard0/Zhuiying/ZhuiYingMovie/The Boys 现场混合 饭制版/音悦Tai/1832.mp4");
							
//							InputStream urlInputStream = conn.getInputStream();
							byte[] buffer = new byte[128];
							while (fileInputStream.read(buffer)>0) {
//								outputStream.write(buffer);
								out.write(buffer);
								out.flush();
							}
							out.close();
//							mPrintWriter.close();
//							outputStream.flush();
//							outputStream.close();
//							conn.disconnect();
							break;
						}  catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
//				    byte[] buffer = new byte[inputStream.available()];
//				    while(inputStream.read(buffer)>0){
//				    	String data = new String(buffer); 
//				    	if (handler != null) {
//							Message message = handler.obtainMessage();
//							message.what = 3;
//							JSONObject jsonObject = JSON.parseObject(data);
//							machine.setGameBoxName(jsonObject.getString("GameBoxName"));
//							handler.sendMessage(message);
//						}
//				    	System.out.println(new String(buffer));
//				    }
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

	}

	public class Machine {

		private String gameBoxIp;
		private int port;
		private String gameBoxName;
		private Socket socket;

		public Socket getSocket() {
			return socket;
		}

		public void setSocket(Socket socket) {
			this.socket = socket;
		}

		public String getGameBoxIp() {
			return gameBoxIp;
		}

		public void setGameBoxIp(String gameBoxIp) {
			this.gameBoxIp = gameBoxIp;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

		public String getGameBoxName() {
			return gameBoxName;
		}

		public void setGameBoxName(String gameBoxName) {
			this.gameBoxName = gameBoxName;
		}

	}
}
