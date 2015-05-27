package com.zjhbkj.xinfen.commom;

import java.util.Locale;

import com.zjhbkj.xinfen.app.XinfengApplication;
import com.zjhbkj.xinfen.util.AppUtil;
import com.zjhbkj.xinfen.util.SharedPreferenceUtil;

public class Global {

	public static String SSID = "ZJXFJ";
	public static String PASSWORD = "987654321";
	public static int DEVICE_PORT = 5000;

	public static String SERVER_IP = "115.159.71.155";
	public static int SERVER_PORT = 5000;

	public static int COMMAND_LENGTH = 22; // 数据长度
	public static String MSG_HEADER = "40"; // 报文头
	public static String MSG_TRAILER = "AB"; // 报文头
	public static String COMMAND_NUM_HEART_BEATS = "DA"; // 心跳指令号
	public static String COMMAND_NUM_STRAINER = "9A"; // 过滤网指令号
	public static String COMMAND_NUM_CONFIG = "CA"; // 配置信息指令号

	public static int CHUXIAO_LIFE = 2160; // 初效寿命
	public static int CHUCHEN_LIFE = 1440; // 静电除尘寿命
	public static int GAOXIAO_LIFE = 8640; // 高效寿命

	public static String GLOBAL_FILE_NAME = "GLOBAL_FILE_NAME"; // 登出不删除
	public static String CONFIG_FILE_NAME = "CONFIG_FILE_NAME"; // 配置文件名，登出就删除
	public static String HAS_STRAINER_INFO = "HAS_STRAINER_INFO"; // 有滤网设置指令
	public static String HAS_SETTING_INFO = "HAS_SETTING_INFO"; // 有设置指令

	public static String CURRENT_DEVICE_ID = "CURRENT_DEVICE_ID"; // 当前设备ID
	public static String HAS_TOAST_OUT_OF_DATE = "HAS_TOAST_OUT_OF_DATE"; // 是否已经谈过过期提示
	public static String IS_FIRST_TIME = "IS_FIRST_TIME"; // 是否第一次
	public static String IS_WIFI_MODE = "IS_WIFI_MODE"; // 是否是外网
	public static String IS_LOCK_OPENED = "IS_LOCK_OPENED"; // 是否童锁打开
	public static String IS_TIMER_OPENED = "IS_TIMER_OPENED"; // 是否定时器

	public static String getGraphicUrl(String action) {
		String deviceName = SharedPreferenceUtil.getStringValueByKey(XinfengApplication.CONTEXT,
				Global.CONFIG_FILE_NAME, Global.CURRENT_DEVICE_ID);
		return AppUtil.getMetaDataByKey(XinfengApplication.CONTEXT, "direct_line_graphic_url")
				+ formateIdHexString(Integer.parseInt(deviceName)) + "-" + action;
	}

	public static String formateIdHexString(int id) {
		String temp = Integer.toHexString(id).toUpperCase(Locale.getDefault());
		String str = String.format("%6s", temp);
		str = str.replaceAll("\\s", "0");
		return str;
	}
}
