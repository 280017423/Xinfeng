package com.zjhbkj.xinfen.util;

public class StringUtil {
	
	/**
	 * 判断字符串是否为null或者空字符串
	 * 
	 * @param input
	 *            输入的字符串
	 * @return 如果为null或者空字符串，返回true；否则返回false
	 */
	public static boolean isNullOrEmpty(String input) {
		if (null == input || "".equals(input)) {
			return true;
		}
		return false;
	}

}
