package com.zjhbkj.xinfen.model;

import com.zjhbkj.xinfen.orm.BaseModel;
import com.zjhbkj.xinfen.util.CommandUtil;

import de.greenrobot.event.EventBus;

/**
 * 设备->APP
 */
public class RcvComsModel extends BaseModel {
	private static final long serialVersionUID = -3053454329426253777L;
	private String msgHeader; // 报文头 40
	private String commandNum; // 指令号 DA
	private String command1; // 指令1
	private String command2; // 指令2 甲醛ppm一个字节
	private String command3; // 指令3 模式1 自动：2 手动：3睡眠：
	private String command4; // 指令4 模式开关 开：1 关：2
	private String command5; // 指令5 pm2.5室外 低字节
	private String command6; // 指令6 pm2.5室外 高字节
	private String command7; // 指令7 pm2.5室内低
	private String command8; // 指令8 pm2.5室内高
	private String command9; // 指令9 CO2:ppm 低
	private String command10; // 指令10 CO2:ppm 高
	private String command11; // 指令11 室内进风温度
	private String command12; // 指令12 室内出风温度
	private String command13; // 指令13 室外进风温度
	private String command14; // 指令14 室外出风温度
	private String command15; // 指令15 湿度
	private String command16; // 指令16 地址字节的最低位
	private String command17; // 指令17 地址字节的中间位
	private String command18; // 指令18 地址字节的最高位
	private String checkSum; // 校验和 数据1+…数据18 和取一个字节
	private String msgTrailer; // 报文尾 AB

	public String getMsgHeader() {
		return msgHeader;
	}

	public void setMsgHeader(String msgHeader) {
		this.msgHeader = msgHeader;
	}

	public String getCommandNum() {
		return commandNum;
	}

	public void setCommandNum(String commandNum) {
		this.commandNum = commandNum;
	}

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

	public String getCommand16() {
		return command16;
	}

	public void setCommand16(String command16) {
		this.command16 = command16;
	}

	public String getCommand17() {
		return command17;
	}

	public void setCommand17(String command17) {
		this.command17 = command17;
	}

	public String getCommand18() {
		return command18;
	}

	public void setCommand18(String command18) {
		this.command18 = command18;
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

	public boolean receiveCommand(byte[] data) {
		msgHeader = CommandUtil.bytesToHexString(data[0]); // 报文头 40
		commandNum = CommandUtil.bytesToHexString(data[1]); // 指令号 DA
		command1 = CommandUtil.bytesToHexString(data[2]); // 指令1
		command2 = CommandUtil.bytesToHexString(data[3]); // 指令2 甲醛ppm一个字节
		command3 = CommandUtil.bytesToHexString(data[4]); // 指令3 模式1 自动：2
															// 手动：3睡眠：
		command4 = CommandUtil.bytesToHexString(data[5]); // 指令4 模式开关 开：1 关：2
		command5 = CommandUtil.bytesToHexString(data[6]); // 指令5 pm2.5室外 低字节
		command6 = CommandUtil.bytesToHexString(data[7]); // 指令6 pm2.5室外 高字节
		command7 = CommandUtil.bytesToHexString(data[8]); // 指令7 pm2.5室内低
		command8 = CommandUtil.bytesToHexString(data[9]); // 指令8 pm2.5室内高
		command9 = CommandUtil.bytesToHexString(data[10]); // 指令9 CO2:ppm 低
		command10 = CommandUtil.bytesToHexString(data[11]); // 指令10 CO2:ppm 高
		command11 = CommandUtil.bytesToHexString(data[12]); // 指令11 室内进风温度
		command12 = CommandUtil.bytesToHexString(data[13]); // 指令12 室内出风温度
		command13 = CommandUtil.bytesToHexString(data[14]); // 指令13 室外进风温度
		command14 = CommandUtil.bytesToHexString(data[15]); // 指令14 室外出风温度
		command15 = CommandUtil.bytesToHexString(data[16]); // 指令15 湿度
		command16 = CommandUtil.bytesToHexString(data[17]); // 指令16 地址字节的最低位
		command17 = CommandUtil.bytesToHexString(data[18]); // 指令17 地址字节的中间位
		command18 = CommandUtil.bytesToHexString(data[19]); // 指令18 地址字节的最高位
		checkSum = CommandUtil.bytesToHexString(data[20]); // 校验和 数据1+…数据18
															// 和取一个字节
		msgTrailer = CommandUtil.bytesToHexString(data[21]); // 报文尾 AB
		// 检验checkSum值
		String calcCheckSum = CommandUtil.getCheckSum(getCheckSumString());
		if (!checkSum.equalsIgnoreCase(calcCheckSum)) {
			EventBus.getDefault().post("checkSum不一致" + checkSum + "====" + calcCheckSum);
			return false;
		}
		return true;
	}

	private String getCheckSumString() {
		return command1 + " " + command2 + " " + command3 + " " + command4 + " " + command5 + " " + command6 + " "
				+ command7 + " " + command8 + " " + command9 + " " + command10 + " " + command11 + " " + command12
				+ " " + command13 + " " + command14 + " " + command15 + " " + command16 + " " + command17 + " "
				+ command18;
	}

	@Override
	public String toString() {
		return "RcvComsModel [msgHeader=" + msgHeader + ", commandNum=" + commandNum + ", command1=" + command1
				+ ", command2=" + command2 + ", command3=" + command3 + ", command4=" + command4 + ", command5="
				+ command5 + ", command6=" + command6 + ", command7=" + command7 + ", command8=" + command8
				+ ", command9=" + command9 + ", command10=" + command10 + ", command11=" + command11 + ", command12="
				+ command12 + ", command13=" + command13 + ", command14=" + command14 + ", command15=" + command15
				+ ", command16=" + command16 + ", command17=" + command17 + ", command18=" + command18 + ", checkSum="
				+ checkSum + ", msgTrailer=" + msgTrailer + "]";
	}

	public String getDisplayPm2dotOut() {
		int pm2dot = CommandUtil.hexStringToInt(command5) + CommandUtil.hexStringToInt(command6) * 256;
		return pm2dot + "";
	}

	public String getDisplayPm2dotIn() {
		int pm2dot = CommandUtil.hexStringToInt(command7) + CommandUtil.hexStringToInt(command8) * 256;
		return pm2dot + "";
	}

	public String getDisplayCo2() {
		int pm2dot = CommandUtil.hexStringToInt(command9) + CommandUtil.hexStringToInt(command10) * 256;
		return pm2dot + "";
	}

}
