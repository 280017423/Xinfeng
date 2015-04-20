package com.zjhbkj.xinfen.util;

import java.util.Locale;

public class CommandUtil {

	public static byte[] getCommand(String command) {
		if (StringUtil.isNullOrEmpty(command)) {
			return new byte[] {};
		}
		try {
			String[] array = command.split(" +");
			byte[] commandArray = new byte[array.length];
			for (int i = 0; i < array.length; i++) {
				commandArray[i] = (byte) Integer.parseInt(array[i], 16);
			}
			return commandArray;
		} catch (Exception e) {
			return new byte[] {};
		}
	}

	/**
	 * 数组转换成十六进制字符串
	 * 
	 * @param byte[]
	 * @return HexString
	 */
	public static final String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase(Locale.getDefault()) + " ");
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	public static final String bytesToHexString(byte data) {
		String sTemp = "";
		sTemp = Integer.toHexString(0xFF & data).toUpperCase(Locale.getDefault());
		if (sTemp.length() < 2) {
			sTemp = "0" + sTemp;
		}
		return sTemp;
	}

	public static final int hexStringToInt(String data) {
		int result = 0;
		try {
			result = Integer.parseInt(data, 16);
		} catch (Exception e) {
		}
		return result;
	}

	public static String getCheckSum(String command) {
		int checkSum = 0;
		String[] array = command.split(" +");
		for (int i = 0; i < array.length; i++) {
			checkSum += Integer.parseInt(array[i], 16);
		}
		String result = Integer.toHexString(checkSum);
		if (result.length() > 2) {
			result = result.substring(result.length() - 2);
		}
		if (result.length() < 2) {
			result = "0" + result;
		}
		return result;
	}

	public static String toHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str += s4 + " ";
		}
		return str.trim();
	}

	/**
	 * @Description 0x0870=2160 高字节是08 低字节是70
	 * @param year
	 * @return String[]
	 */
	public static String[] formateHexString(int year) {
		String[] values = new String[2];
		String temp = Integer.toHexString(year);
		String str = String.format("%4s", temp);
		str = str.replaceAll("\\s", "0");
		values[0] = str.substring(2);
		values[1] = str.substring(0, 2);
		return values;
	}
}
