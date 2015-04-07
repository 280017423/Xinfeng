package com.zjhbkj.xinfen.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import android.util.Log;

import com.zjhbkj.xinfen.commom.Global;
import com.zjhbkj.xinfen.model.MsgInfo;
import com.zjhbkj.xinfen.util.CommandUtil;

import de.greenrobot.event.EventBus;

/**
 * UDP服务器类
 */
public class UDPServer implements Runnable {
	private byte[] msg = new byte[22];
	private boolean onGoinglistner = true;
	private DataRecvListener mDataRecvListener;
	private DatagramSocket mReceiveSocket;
	private DatagramSocket mSendSocket;

	public UDPServer(DataRecvListener listener) {
		mDataRecvListener = listener;
		init();
	}

	private void init() {
		try {
			if (mReceiveSocket == null) {
				mReceiveSocket = new DatagramSocket(null);
				mReceiveSocket.setReuseAddress(true);
				mReceiveSocket.bind(new InetSocketAddress(Global.DEVICE_PORT));
			}
			mSendSocket = new DatagramSocket();
		} catch (SocketException e1) {
			Log.d("aaa", e1.toString());
			e1.printStackTrace();
		}
	}

	public void run() {
		DatagramPacket datagramPacket = new DatagramPacket(msg, msg.length);
		while (onGoinglistner) {
			try {
				mReceiveSocket.receive(datagramPacket);
				MsgInfo info = new MsgInfo(CommandUtil.bytesToHexString(datagramPacket.getData()));
				info.setName(datagramPacket.getAddress().getHostAddress() + ":" + datagramPacket.getPort());
				mDataRecvListener.onRecv(info);
				datagramPacket.setLength(msg.length); // 重设数据包的长度
				send(datagramPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private byte[] getCommand() {
		String command = "AA EA 02 28 01 01 03 04 05 06 07 11 12 13 14 01 78 00 00 00 BA AB";
		return CommandUtil.getCommand(command);
	}

	/**
	 * 发送信息到服务器
	 */
	public void send(DatagramPacket datagramPacket) {
		byte[] commands = getCommand();
		DatagramPacket sendPacket = new DatagramPacket(
				commands, commands.length, datagramPacket.getAddress(), datagramPacket.getPort());
		try {
			mSendSocket.send(sendPacket);
			EventBus.getDefault().post("消息发送成功" + datagramPacket.getAddress() + ":" + datagramPacket.getPort());
		} catch (IOException e) {
			e.printStackTrace();
			EventBus.getDefault().post("消息发送失败." + e.toString());
		}
	}

	public void closeConnection() {
		if (mReceiveSocket != null) {
			mReceiveSocket.close();
			mReceiveSocket = null;
		}
		if (mSendSocket != null) {
			mSendSocket.close();
			mSendSocket = null;
		}
	}

	public static interface DataRecvListener {
		public void onRecv(MsgInfo info);
	};

	public void stopAcceptMessage() {
		onGoinglistner = false;
	}
}