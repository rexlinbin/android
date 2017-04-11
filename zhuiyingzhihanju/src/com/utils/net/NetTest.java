package com.utils.net;

public class NetTest {
	public void tcpTest() {
		TcpServerHelper tcpServerHelper = new TcpServerHelper();
		tcpServerHelper.startServer(12345, null,
				"http://sh.ctfs.ftn.caomijuan.com/ftn_handler/904b898a2804c174b724e24c139d7fb9fe3f08be0f5fb00d455134802029b13ec2d874579272272d91919d16ae33cdb13ce49700c86dbcb41079bf08344b8a6d/fname=m.mp4");

		TcpClientHelper tcpClientHelper = new TcpClientHelper("127.0.0.1", 12345);
		tcpClientHelper.createClientSocket();
	}
}
