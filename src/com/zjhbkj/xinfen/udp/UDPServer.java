package com.zjhbkj.xinfen.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import android.util.Log;

import com.zjhbkj.xinfen.commom.Global;
import com.zjhbkj.xinfen.db.DBMgr;
import com.zjhbkj.xinfen.model.MsgInfo;
import com.zjhbkj.xinfen.model.RcvComsModel;
import com.zjhbkj.xinfen.model.SendComsModel;
import com.zjhbkj.xinfen.util.CommandUtil;

import de.greenrobot.event.EventBus;

/**
 * UDP服务器类
 */
public class UDPServer implements Runnable {
	private byte[] msg = new byte[Global.COMMAND_LENGTH];
	private boolean onGoinglistner = true;
	private DatagramSocket mReceiveSocket;
	private DatagramSocket mSendSocket;

	public UDPServer() {
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
				RcvComsModel model = new RcvComsModel();
				model.receiveCommand(datagramPacket.getData());
				DBMgr.saveModel(model);
				EventBus.getDefault().post(model);
				datagramPacket.setLength(msg.length); // 重设数据包的长度
				send(datagramPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 发送信息到服务器
	 */
	public void send(DatagramPacket datagramPacket) {
		SendComsModel model = DBMgr.getHistoryData(SendComsModel.class, "EA");
		if (null != model) {
			byte[] commands = CommandUtil.getCommand(model.toString());
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