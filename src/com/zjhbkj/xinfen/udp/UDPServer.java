package com.zjhbkj.xinfen.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.zjhbkj.xinfen.model.MsgInfo;
import com.zjhbkj.xinfen.util.CommandUtil;
import com.zjhbkj.xinfen.wifihot.Global;

/**
 * UDP服务器类
 */
public class UDPServer implements Runnable {
	private byte[] msg = new byte[19];
	private boolean onGoinglistner = true;
	private DataRecvListener mDataRecvListener;
	private DatagramSocket dSocket;
	private ClientMsgListener mClientMsgListener;

	public UDPServer(DataRecvListener listener, ClientMsgListener clientMsgListener) {
		mDataRecvListener = listener;
		mClientMsgListener = clientMsgListener;
		init();
	}

	private void init() {
		try {
			dSocket = new DatagramSocket(Global.PORT);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void run() {

		DatagramPacket dPacket = new DatagramPacket(msg, msg.length);
		while (onGoinglistner) {
			try {
				if (null == dSocket) {
					init();
				}
				dSocket.receive(dPacket);
				MsgInfo info = new MsgInfo(CommandUtil.bytesToHexString(dPacket.getData()));
				info.setName(dPacket.getAddress().getHostAddress() + ":" + dPacket.getPort());
				mDataRecvListener.onRecv(info);
				dPacket.setLength(msg.length); // 重设数据包的长度
				send(dPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private byte[] getCommand() {
		String command = "AA EA 02 28 01 01 03 04 05 06 07 11 12 13 14 01 78 BA AB";
		return CommandUtil.getCommand(command);
	}

	/**
	 * 发送信息到服务器
	 */
	public void send(DatagramPacket dPacket) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		InetAddress ipCliente = dPacket.getAddress();
		int portaCliente = dPacket.getPort();

		byte[] commands = getCommand();
		DatagramPacket sendPacket = new DatagramPacket(commands, commands.length, ipCliente, portaCliente);
		try {
			dSocket.send(sendPacket);
			mClientMsgListener.handlerHotMsg("消息发送成功" + ipCliente + ":" + portaCliente);
		} catch (IOException e) {
			e.printStackTrace();
			mClientMsgListener.handlerErorMsg("消息发送失败.");
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