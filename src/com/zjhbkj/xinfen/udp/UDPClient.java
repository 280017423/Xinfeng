package com.zjhbkj.xinfen.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.util.Log;

import com.zjhbkj.xinfen.app.XinfengApplication;
import com.zjhbkj.xinfen.commom.Global;
import com.zjhbkj.xinfen.db.DBMgr;
import com.zjhbkj.xinfen.model.RcvComsModel;
import com.zjhbkj.xinfen.model.SendComsModel;
import com.zjhbkj.xinfen.util.CommandUtil;
import com.zjhbkj.xinfen.util.EvtLog;
import com.zjhbkj.xinfen.util.SharedPreferenceUtil;
import com.zjhbkj.xinfen.util.StringUtil;

import de.greenrobot.event.EventBus;

/**
 * udp客户端
 */
public class UDPClient {
	private byte[] buffer = new byte[22];
	private static UDPClient mUdpClient;
	private DatagramSocket mDatagramSocket;
	private ClientMsgListener mClientListener;
	private InetAddress mInetAddress;
	private boolean onGoinglistner = true;

	private UDPClient(ClientMsgListener clientListener) {
		mClientListener = clientListener;
	}

	public static synchronized UDPClient newInstance(ClientMsgListener clientListener) {
		if (mUdpClient == null) {
			mUdpClient = new UDPClient(clientListener);
		}
		return mUdpClient;
	}

	public void connectServer() {
		onGoinglistner = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mInetAddress = InetAddress.getByName(Global.SERVER_IP); // 本机测试
					if (mDatagramSocket == null) {
						mDatagramSocket = new DatagramSocket(null);
						mDatagramSocket.setReuseAddress(true);
						mDatagramSocket.bind(new InetSocketAddress(Global.SERVER_PORT));
					}
					sendToServer();
					receiveMsg();
				} catch (UnknownHostException e) {
					mInetAddress = null;
					mClientListener.handlerErorMsg("没有找到服务器");
					e.printStackTrace();
				} catch (SocketException e) {
					mDatagramSocket = null;
					mClientListener.handlerErorMsg("服务器连接失败");
					e.printStackTrace();
				}
			}
		}).start();
	}

	protected void receiveMsg() {
		DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
		while (onGoinglistner) {
			try {
				Log.d("aaa", "等待消息来啊.....");
				mDatagramSocket.receive(datagramPacket);
				Log.d("aaa", "来消息来了....." + CommandUtil.bytesToHexString(datagramPacket.getData()));
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
				// 判断收到的是什么数据
				if (commandNum.equalsIgnoreCase(Global.COMMAND_NUM_HEART_BEATS)) {
					EvtLog.d("bbb", "收到心跳指令");
					// 判断是否是当前设备发送的
					String deviceName = CommandUtil.bytesToHexString(data[19]) + CommandUtil.bytesToHexString(data[18])
							+ CommandUtil.bytesToHexString(data[17]);
					String localName = SharedPreferenceUtil.getStringValueByKey(XinfengApplication.CONTEXT,
							Global.CONFIG_FILE_NAME, Global.CURRENT_DEVICE_ID);
					if (CommandUtil.hexStringToInt(deviceName) != Integer.parseInt(localName)) {
						EventBus.getDefault().post(
								"不是同一台设备" + CommandUtil.hexStringToInt(deviceName) + "======"
										+ Integer.parseInt(localName));
						continue;
					}
					RcvComsModel rcvComsModel = new RcvComsModel();
					boolean isValid = rcvComsModel.receiveCommand(data);
					datagramPacket.setLength(buffer.length); // 重设数据包的长度
					if (isValid) {
						DBMgr.saveModel(rcvComsModel);
						EventBus.getDefault().post(rcvComsModel);
						// 更新模式状态
						SendComsModel mSendComsModel = DBMgr.getHistoryData(SendComsModel.class, "EA");
						int count = SharedPreferenceUtil.getIntegerValueByKey(XinfengApplication.CONTEXT,
								Global.CONFIG_FILE_NAME, Global.HAS_SETTING_INFO);
						if (null != mSendComsModel && count <= 0) {
							mSendComsModel.setCommand3(rcvComsModel.getCommand3());
							mSendComsModel.setCommand1(rcvComsModel.getCommand1());
							DBMgr.saveModel(mSendComsModel);
						}
					}
				}
			} catch (IOException e) {
				Log.d("aaa", e.toString());
				e.printStackTrace();
			}
		}
	}

	public void sendToServer() {
		if (null == mInetAddress || null == mDatagramSocket) {
			mClientListener.handlerErorMsg("服务器未连接");
			return;
		}
		SendComsModel model = DBMgr.getHistoryData(SendComsModel.class, "EA");
		if (null != model) {
			byte[] commands = CommandUtil.getCommand(model.toString());
			DatagramPacket dPacket = new DatagramPacket(commands, commands.length, mInetAddress, Global.SERVER_PORT);
			try {
				EvtLog.d("aaa", "发送消息到服务器:" + model.toString());
				mDatagramSocket.send(dPacket);
				int count = SharedPreferenceUtil.getIntegerValueByKey(XinfengApplication.CONTEXT,
						Global.CONFIG_FILE_NAME, Global.HAS_SETTING_INFO);
				if (count > 0) {
					SharedPreferenceUtil.saveValue(XinfengApplication.CONTEXT, Global.CONFIG_FILE_NAME,
							Global.HAS_SETTING_INFO, count - 1);
				}
			} catch (IOException e) {
				e.printStackTrace();
				EventBus.getDefault().post("消息发送失败." + e.toString());
			}
		}
	}

	public void closeConnection() {
		if (mDatagramSocket != null) {
			mDatagramSocket.disconnect();
			mDatagramSocket.close();
			mDatagramSocket = null;
		}
	}

	public boolean isConnected() {
		if (null != mDatagramSocket) {
			return mDatagramSocket.isConnected();
		}
		return false;
	}

	public void stopAcceptMessage() {
		onGoinglistner = false;
	}

	public static interface ClientMsgListener {
		public void handlerErorMsg(String errorMsg);
	}

}