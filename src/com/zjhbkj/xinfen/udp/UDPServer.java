package com.zjhbkj.xinfen.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import android.util.Log;

import com.zjhbkj.xinfen.app.XinfengApplication;
import com.zjhbkj.xinfen.commom.Global;
import com.zjhbkj.xinfen.db.DBMgr;
import com.zjhbkj.xinfen.model.ConfigModel;
import com.zjhbkj.xinfen.model.RcvComsModel;
import com.zjhbkj.xinfen.model.SendComsModel;
import com.zjhbkj.xinfen.model.SendConfigModel;
import com.zjhbkj.xinfen.model.StrainerModel;
import com.zjhbkj.xinfen.model.StrainerSendModel;
import com.zjhbkj.xinfen.util.CommandUtil;
import com.zjhbkj.xinfen.util.EvtLog;
import com.zjhbkj.xinfen.util.SharedPreferenceUtil;
import com.zjhbkj.xinfen.util.StringUtil;

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
				byte[] data = datagramPacket.getData();
				if (null == data || Global.COMMAND_LENGTH != data.length) {
					EventBus.getDefault().post("非法数据");
					continue;
				}
				String msgHeader = CommandUtil.bytesToHexString(data[0]); // 报文头40
				if (StringUtil.isNullOrEmpty(msgHeader) || !msgHeader.equalsIgnoreCase(Global.MSG_HEADER)) {
					EventBus.getDefault().post("非法报文头" + msgHeader);
					continue;
				}
				String commandNum = CommandUtil.bytesToHexString(data[1]); // 指令号
				if (StringUtil.isNullOrEmpty(commandNum)) {
					EventBus.getDefault().post("非法指令号");
					continue;
				}
				String msgTrailer = CommandUtil.bytesToHexString(data[21]); // 报文尾AB
				if (StringUtil.isNullOrEmpty(msgTrailer) || !msgTrailer.equalsIgnoreCase(Global.MSG_TRAILER)) {
					EventBus.getDefault().post("非法报文尾" + msgTrailer);
					continue;
				}
				// TODO 判断收到的是什么数据
				if (commandNum.equalsIgnoreCase(Global.COMMAND_NUM_HEART_BEATS)) {
					EvtLog.d("bbb", "收到心跳指令");
					RcvComsModel rcvComsModel = new RcvComsModel();
					boolean isValid = rcvComsModel.receiveCommand(data);
					datagramPacket.setLength(msg.length); // 重设数据包的长度
					if (isValid) {
						DBMgr.saveModel(rcvComsModel);
						EventBus.getDefault().post(rcvComsModel);
						if (SharedPreferenceUtil.getBooleanValueByKey(XinfengApplication.CONTEXT,
								Global.CONFIG_FILE_NAME, Global.HAS_STRAINER_INFO)) {
							EvtLog.d("aaa", "发送滤网指令");
							sendStrainerConfig(datagramPacket);
						} else if (!sendConfigData(datagramPacket)) {
							// 没有配置信息发送才发心跳包
							EvtLog.d("aaa", "发送心跳包配置指令");
							sendHeartBeats(datagramPacket);
						}
					}
				} else if (commandNum.equalsIgnoreCase(Global.COMMAND_NUM_STRAINER)) {
					EvtLog.d("bbb", "收到滤网指令");
					StrainerModel strainerModel = new StrainerModel();
					boolean isValid = strainerModel.receiveCommand(data);
					datagramPacket.setLength(msg.length); // 重设数据包的长度
					if (isValid) {
						EventBus.getDefault().post(strainerModel);
					}
				} else if (commandNum.equalsIgnoreCase(Global.COMMAND_NUM_CONFIG)) {

				} else {
					EventBus.getDefault().post("非法指令号");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 发送配置包信息到服务器
	 */
	public boolean sendConfigData(DatagramPacket datagramPacket) {
		ConfigModel configModel = DBMgr.getConfigModel("0");
		if (null == configModel || StringUtil.isNullOrEmpty(configModel.getSsid())) {
			return false;
		}
		EvtLog.d("aaa", "发送wifi配置指令");
		String info = configModel.getSendInfio();
		int left = info.length() % 17;
		int count = info.length() / 17;
		if (left > 0) {
			count++;
		}
		char[] src = info.toCharArray();
		char[] dest = new char[17 * count];
		for (int i = 0; i < src.length; i++) {
			dest[i] = src[i];
		}
		for (int i = 0; i < count; i++) {
			SendConfigModel sendConfigModel = new SendConfigModel();
			if (i == count - 1) {
				sendConfigModel.setCommand1(Integer.toHexString(255));
			} else {
				sendConfigModel.setCommand1(Integer.toHexString(i));
			}
			sendConfigModel.setCommand2(Integer.toHexString(dest[i * 17 + 0]));
			sendConfigModel.setCommand3(Integer.toHexString(dest[i * 17 + 1]));
			sendConfigModel.setCommand4(Integer.toHexString(dest[i * 17 + 2]));
			sendConfigModel.setCommand5(Integer.toHexString(dest[i * 17 + 3]));
			sendConfigModel.setCommand6(Integer.toHexString(dest[i * 17 + 4]));
			sendConfigModel.setCommand7(Integer.toHexString(dest[i * 17 + 5]));
			sendConfigModel.setCommand8(Integer.toHexString(dest[i * 17 + 6]));
			sendConfigModel.setCommand9(Integer.toHexString(dest[i * 17 + 7]));
			sendConfigModel.setCommand10(Integer.toHexString(dest[i * 17 + 8]));
			sendConfigModel.setCommand11(Integer.toHexString(dest[i * 17 + 9]));
			sendConfigModel.setCommand12(Integer.toHexString(dest[i * 17 + 10]));
			sendConfigModel.setCommand13(Integer.toHexString(dest[i * 17 + 11]));
			sendConfigModel.setCommand14(Integer.toHexString(dest[i * 17 + 12]));
			sendConfigModel.setCommand15(Integer.toHexString(dest[i * 17 + 13]));
			sendConfigModel.setCommand16(Integer.toHexString(dest[i * 17 + 14]));
			sendConfigModel.setCommand17(Integer.toHexString(dest[i * 17 + 15]));
			sendConfigModel.setCommand18(Integer.toHexString(dest[i * 17 + 16]));
			byte[] sendConfigCommands = CommandUtil.getCommand(sendConfigModel.toString());
			DatagramPacket sendPacket = new DatagramPacket(
					sendConfigCommands, sendConfigCommands.length, datagramPacket.getAddress(),
					datagramPacket.getPort());
			try {
				mSendSocket.send(sendPacket);
				EventBus.getDefault().post("发送配置信息成功" + datagramPacket.getAddress() + ":" + datagramPacket.getPort());
				try {
					Thread.sleep(800);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
				EventBus.getDefault().post("发送配置信息失败." + e.toString());
			}
		}
		// 更新状态
		configModel.setHasSent(1);
		DBMgr.saveModel(configModel);
		return true;
	}

	/**
	 * 发送信息到服务器
	 */
	public void sendHeartBeats(DatagramPacket datagramPacket) {
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

	/**
	 * 发送信息到服务器
	 */
	public void sendStrainerConfig(DatagramPacket datagramPacket) {

		StrainerSendModel model = DBMgr.getHistoryData(StrainerSendModel.class, "8A");
		if (null != model) {
			byte[] commands = CommandUtil.getCommand(model.toString());
			DatagramPacket sendPacket = new DatagramPacket(
					commands, commands.length, datagramPacket.getAddress(), datagramPacket.getPort());
			try {
				mSendSocket.send(sendPacket);
				EventBus.getDefault().post("滤网信息发送成功" + datagramPacket.getAddress() + ":" + datagramPacket.getPort());
			} catch (IOException e) {
				e.printStackTrace();
				EventBus.getDefault().post("滤网信息发送失败." + e.toString());
			}
		}
		SharedPreferenceUtil.saveValue(XinfengApplication.CONTEXT, Global.CONFIG_FILE_NAME, Global.HAS_STRAINER_INFO,
				false);
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

	public void stopAcceptMessage() {
		onGoinglistner = false;
	}
}