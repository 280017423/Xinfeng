package com.zjhbkj.xinfen.model;

import com.zjhbkj.xinfen.orm.BaseModel;
import com.zjhbkj.xinfen.util.CommandUtil;

/**
 * 滤网设备指令
 */
public class StrainerSendModel extends BaseModel {

	private static final long serialVersionUID = 5745366813639864256L;
	private String msgHeader = "AA"; // 报文头
	private String commandNum = "8A"; // 指令号
	private String command1; // 指令1 初效过滤寿命单位：小时低字节
	private String command2; // 指令2 初效过滤寿命单位：小时高字节
	private String command3; // 指令3 静电除尘寿命单位：小时低字节
	private String command4; // 指令4 静电除尘寿命单位：小时高字节
	private String command5; // 指令5 高效过滤寿命单位：小时低字节
	private String command6; // 指令6 高效过滤寿命单位：小时高字节
	private String command7; // 指令7 当前初效过滤是否过期1：有效
	private String command8; // 指令8 当前静电除尘是否过期1：有效
	private String command9; // 指令9 当前高效过滤是否过期1：有效
	private String command10 = "0"; // 指令10
	private String command11 = "0"; // 指令11
	private String command12 = "0"; // 指令12
	private String command13 = "0"; // 指令13
	private String command14 = "0"; // 指令14
	private String command15 = "0"; // 指令15
	private String command16 = "0"; // 指令16 地址字节的最低位
	private String command17 = "0"; // 指令17 地址字节的中间位
	private String command18 = "0"; // 指令18 地址字节的最高位
	private String checkSum; // 校验和 数据1+…数据18 和取一个字节
	private String msgTrailer = "AB"; // 报文尾 AB

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

	@Override
	public String toString() {
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
