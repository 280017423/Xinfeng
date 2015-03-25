package com.zjhbkj.xinfen.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.util.Log;

import com.zjhbkj.xinfen.model.MsgInfo;
import com.zjhbkj.xinfen.wifihot.Global;

/**
 * UDP服务器类
 */
public class UDPServer implements Runnable {
	private byte[] msg = new byte[1024];
	private boolean onGoinglistner = true;
	private DataRecvListener listener;
	private InetAddress mInetAddress;
	private int mPort;
	private DatagramSocket dSocket;

	public UDPServer(DataRecvListener listener) {
		this.listener = listener;
		try {
			dSocket = new DatagramSocket(Global.SERVER_PORT);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void run() {

		DatagramPacket dPacket = new DatagramPacket(msg, msg.length);
		while (onGoinglistner) {
			try {
				dSocket.receive(dPacket);
				Log.i("msg sever received", new String(dPacket.getData(), 0, dPacket.getLength()));
				MsgInfo info = new MsgInfo(new String(dPacket.getData(), 0, dPacket.getLength()));
				info.setName(dPacket.getAddress().getHostAddress());
				listener.onRecv(info);
				mInetAddress = dPacket.getAddress();
				mPort = dPacket.getPort();
				dPacket.setLength(msg.length); // 重设数据包的长度
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 发送信息到服务器
	 */
	public void send(byte[] data, ClientMsgListener mClientListener) {
		int msgLength = data.length;
		if (null == dSocket || null == mInetAddress || 0 == mPort) {
			mClientListener.handlerErorMsg("热点未连接");
			return;
		}
		DatagramPacket dPacket = new DatagramPacket(data, msgLength, mInetAddress, Global.SERVER_PORT);
		try {
			dSocket.send(dPacket);
			mClientListener.handlerHotMsg("消息发送成功" + mInetAddress + ":" + mPort);
		} catch (IOException e) {
			e.printStackTrace();
			mClientListener.handlerErorMsg("消息发送失败.");
		}
	}

	public void closeConnection() {
		if (dSocket != null) {
			dSocket.close();
			dSocket = null;
		}
	}

	public static interface DataRecvListener {
		public void onRecv(MsgInfo info);
	};

	public void stopAcceptMessage() {
		onGoinglistner = false;
	}

	public static interface ClientMsgListener {

		public void handlerErorMsg(String errorMsg);

		public void handlerHotMsg(String hotMsg);

	}
}