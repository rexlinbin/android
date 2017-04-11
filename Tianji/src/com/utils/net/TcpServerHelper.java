package com.utils.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import android.os.Handler;
import android.os.Message;

public class TcpServerHelper {
	private List<Machine> clientsList;
	private boolean isStarting = false;
	private ServerSocket ss = null;

	public TcpServerHelper() {
		clientsList = new ArrayList<Machine>();
	}

	@SuppressWarnings("resource")
	public boolean startServer(final int port, final Handler handler) {
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
						new Thread(new ServerThread(machine, handler)).start();
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
		private PrintWriter mPrintWriter = null;
		private Handler handler;

		// 定义该线程所处理的Socket所对应的输入流

		public ServerThread(Machine machine, Handler handler) throws IOException {
			this.machine = machine;
			this.mSocket = machine.getSocket();
			this.handler = handler;
			// 初始化该Socket对应的输入流
			inputStream = mSocket.getInputStream();
			mPrintWriter = new PrintWriter(mSocket.getOutputStream(), true);
		}

		public void run() {
			while (true) {
				 try {
					Thread.sleep(200);
				    byte[] buffer = new byte[inputStream.available()];
				    while(inputStream.read(buffer)>0){
				    	String data = new String(buffer); 
				    	if (handler != null) {
							Message message = handler.obtainMessage();
							message.what = 3;
							JSONObject jsonObject = JSON.parseObject(data);
							machine.setGameBoxName(jsonObject.getString("GameBoxName"));
							handler.sendMessage(message);
						}
				    	System.out.println(new String(buffer));
				    }
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
