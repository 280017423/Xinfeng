package com.zjhbkj.xinfen.model;

import com.zjhbkj.xinfen.orm.BaseModel;
import com.zjhbkj.xinfen.util.EvtLog;

public class ConfigModel extends BaseModel {
	private static final long serialVersionUID = 6802278372378499454L;
	private String ssid; // wifi名字
	private String wifiPassword; // wifi密码
	private String serverIp; // 服务器ip
	private String serverPort; // 服务器端口
	private int hasSent; // 是否已经发送

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public String getWifiPassword() {
		return wifiPassword;
	}

	public void setWifiPassword(String wifiPassword) {
		this.wifiPassword = wifiPassword;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getServerPort() {
		return serverPort;
	}

	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}

	public int isHasSent() {
		return hasSent;
	}

	public void setHasSent(int hasSent) {
		this.hasSent = hasSent;
	}

	public String getSendInfio() {
		String result = "@ssid=" + ssid + "," + wifiPassword + ";server=udp:" + serverIp + ":" + serverPort;
		EvtLog.d("aaa", result);
		return result;
	}
}
