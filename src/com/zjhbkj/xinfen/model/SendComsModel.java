package com.zjhbkj.xinfen.model;

import java.util.Calendar;

import com.zjhbkj.xinfen.app.XinfengApplication;
import com.zjhbkj.xinfen.commom.Global;
import com.zjhbkj.xinfen.db.DBMgr;
import com.zjhbkj.xinfen.orm.BaseModel;
import com.zjhbkj.xinfen.util.CommandUtil;
import com.zjhbkj.xinfen.util.DateUtil;
import com.zjhbkj.xinfen.util.SharedPreferenceUtil;

/**
 * APP->设备
 */
public class SendComsModel extends BaseModel {

	private static final long serialVersionUID = 5745366813639864256L;
	private String msgHeader = "AA"; // 报文头 APP->设备：AA
	public String commandNum = "EA"; // 指令号 APP->设备：EA
	private String command1 = "0"; // 指令1 设置上报间隔 单位：秒
	private String command2 = "0"; // 指令2 设置频率 单位: Hz（只对手动有效）
	private String command3 = "0"; // 指令3 设置模式1 自动：2 手动：3睡眠：
	private String command4 = "0"; // 指令4 设置功能开关 开：1 关：2
	private String command5 = "0"; // 指令5 设置时间 分钟
	private String command6 = "0"; // 指令6 设置时间 小时
	private String command7 = "0"; // 指令7 设置时间 日
	private String command8 = "0"; // 指令8 设置时间 月
	private String command9 = "0"; // 指令9 设置时间 年
	private String command10 = "0"; // 指令10 设置开机 分钟
	private String command11 = "0"; // 指令11 设置开机小时
	private String command12 = "0"; // 指令12 设置关机分钟
	private String command13 = "0"; // 指令13 设置关机小时
	private String command14 = "0"; // 指令14 设置开/关机0:开机1：关机2：链接内网3：链接外网
	private String command15 = "0"; // 指令15 设置自动模式静电除尘开启的户外PM2.5
	private String command16 = "0"; // 指令16 地址字节的最低位
	private String command17 = "0"; // 指令17 地址字节的中间位
	private String command18 = "0"; // 指令18 地址字节的最高位
	private String checkSum; // 校验和 数据1+…数据18 和取一个字节
	private String msgTrailer = "AB"; // 报文尾 AB

	public String getCommand1() {
		return command1;
	}

	public void setCommand1(String command1) {
		this.command1 = command1;
	}

	public String getCommand2() {
		return command2;
	}

	public void setCommand2(String command2) {
		this.command2 = command2;
	}

	public String getCommand3() {
		return command3;
	}

	public void setCommand3(String command3) {
		this.command3 = command3;
	}

	public String getCommand4() {
		return command4;
	}

	public void setCommand4(String command4) {
		this.command4 = command4;
	}

	public String getCommand5() {
		return command5;
	}

	public void setCommand5(String command5) {
		this.command5 = command5;
	}

	public String getCommand6() {
		return command6;
	}

	public void setCommand6(String command6) {
		this.command6 = command6;
	}

	public String getCommand7() {
		return command7;
	}

	public void setCommand7(String command7) {
		this.command7 = command7;
	}

	public String getCommand8() {
		return command8;
	}

	public void setCommand8(String command8) {
		this.command8 = command8;
	}

	public String getCommand9() {
		return command9;
	}

	public void setCommand9(String command9) {
		this.command9 = command9;
	}

	public String getCommand10() {
		return command10;
	}

	public void setCommand10(String command10) {
		this.command10 = command10;
	}

	public String getCommand11() {
		return command11;
	}

	public void setCommand11(String command11) {
		this.command11 = command11;
	}

	public String getCommand12() {
		return command12;
	}

	public void setCommand12(String command12) {
		this.command12 = command12;
	}

	public String getCommand13() {
		return command13;
	}

	public void setCommand13(String command13) {
		this.command13 = command13;
	}

	public String getCommand14() {
		return command14;
	}

	public void setCommand14(String command14) {
		this.command14 = command14;
	}

	public String getCommand15() {
		return command15;
	}

	public void setCommand15(String command15) {
		this.command15 = command15;
	}

	public String getCheckSum() {
		return checkSum;
	}

	public void setCheckSum(String checkSum) {
		this.checkSum = checkSum;
	}

	public String getMsgTrailer() {
		return msgTrailer;
	}

	public void setMsgTrailer(String msgTrailer) {
		this.msgTrailer = msgTrailer;
	}

	public void send(boolean needDelay) {
		DBMgr.saveModel(this, "COMMAND_NUM = ?", "EA");
		// 一旦有设置，就三个周期不要更改本地数据，保护本地数据
		if (needDelay) {
			SharedPreferenceUtil.saveValue(XinfengApplication.CONTEXT, Global.CONFIG_FILE_NAME,
					Global.HAS_SETTING_INFO, 1);
		}
	}

	@Override
	public String toString() {
		String deviceName = SharedPreferenceUtil.getStringValueByKey(XinfengApplication.CONTEXT,
				Global.CONFIG_FILE_NAME, Global.CURRENT_DEVICE_ID);
		// 童锁和定时器逻辑
		boolean isLockOpened = SharedPreferenceUtil.getBooleanValueByKey(XinfengApplication.CONTEXT,
				Global.GLOBAL_FILE_NAME_LOCK, deviceName);
		boolean isTimerOpened = SharedPreferenceUtil.getBooleanValueByKey(XinfengApplication.CONTEXT,
				Global.GLOBAL_FILE_NAME_TIMER, deviceName);
		int lockMode = CommandUtil.hexStringToInt(command3);
		if (isLockOpened) {
			command3 = Integer.toHexString(lockMode % 10 + 10);
		} else {
			command3 = Integer.toHexString(lockMode % 10);
		}
		int timerMode = CommandUtil.hexStringToInt(command4);
		if (isTimerOpened) {
			command4 = Integer.toHexString(timerMode % 10 + 10);
		} else {
			command4 = Integer.toHexString(timerMode % 10);
		}

		String[] idHex = CommandUtil.formateIdHexString(Integer.parseInt(deviceName));
		command16 = idHex[0];
		command17 = idHex[1];
		command18 = idHex[2];
		command5 = Integer.toHexString(DateUtil.getDateTime(Calendar.MINUTE));
		command6 = Integer.toHexString(DateUtil.getDateTime(Calendar.HOUR_OF_DAY));
		command7 = Integer.toHexString(DateUtil.getDateTime(Calendar.DAY_OF_MONTH));
		command8 = Integer.toHexString(DateUtil.getDateTime(Calendar.MONTH));
		command9 = Integer.toHexString(DateUtil.getDateTime(Calendar.YEAR));
		checkSum = CommandUtil.getCheckSum(command1 + " " + command2 + " " + command3 + " " + command4 + " " + command5
				+ " " + command6 + " " + command7 + " " + command8 + " " + command9 + " " + command10 + " " + command11
				+ " " + command12 + " " + command13 + " " + command14 + " " + command15 + " " + command16 + " "
				+ command17 + " " + command18);
		return msgHeader + " " + commandNum + " " + command1 + " " + command2 + " " + command3 + " " + command4 + " "
				+ command5 + " " + command6 + " " + command7 + " " + command8 + " " + command9 + " " + command10 + " "
				+ command11 + " " + command12 + " " + command13 + " " + command14 + " " + command15 + " " + command16
				+ " " + command17 + " " + command18 + " " + checkSum + " " + msgTrailer;
	}
}
