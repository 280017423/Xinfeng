package com.zjhbkj.xinfen.commom;

public class Global {

	public static String SSID = "ZJXFJ";
	public static String PASSWORD = "987654321";
	public static int DEVICE_PORT = 5000;

	public static int COMMAND_LENGTH = 22; // 数据长度
	public static String MSG_HEADER = "40"; // 报文头
	public static String MSG_TRAILER = "AB"; // 报文头
	public static String COMMAND_NUM_HEART_BEATS = "DA"; // 心跳指令号
	public static String COMMAND_NUM_STRAINER = "9A"; // 过滤网指令号
	public static String COMMAND_NUM_CONFIG = "CA"; // 配置信息指令号

	public static int CHUXIAO_LIFE = 2160; // 初效寿命
	public static int CHUCHEN_LIFE = 1440; // 静电除尘寿命
	public static int GAOXIAO_LIFE = 8640; // 高效寿命

	public static String CONFIG_FILE_NAME = "CONFIG_FILE_NAME"; // 配置文件名
	public static String HAS_STRAINER_INFO = "HAS_STRAINER_INFO"; // 高效寿命
	
	public static String CURRENT_DEVICE_ID = "CURRENT_DEVICE_ID"; // 当前设备ID

}
