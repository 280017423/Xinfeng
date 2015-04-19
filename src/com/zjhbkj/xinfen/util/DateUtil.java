package com.zjhbkj.xinfen.util;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class DateUtil {

	public static int getDayNum(int year, int month) {
		String[] big_months = { "1", "3", "5", "7", "8", "10", "12" };
		String[] little_months = { "4", "6", "9", "11" };
		final List<String> list_big = Arrays.asList(big_months);
		final List<String> list_little = Arrays.asList(little_months);
		// 判断大小月及是否闰年,用来确定"日"的数据
		if (list_big.contains(String.valueOf(month + 1))) {
			return 31;
		} else if (list_little.contains(String.valueOf(month + 1))) {
			return 30;
		} else {
			// 闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				return 29;
			else {
				return 28;
			}
		}
	}

	public static int getDateTime(int filed) {
		Calendar calendar = Calendar.getInstance();
		switch (filed) {
			case Calendar.YEAR:
				return calendar.get(Calendar.YEAR) - 2000;
			case Calendar.MONTH:
				return calendar.get(Calendar.MONTH) + 1;
			case Calendar.DAY_OF_MONTH:
				return calendar.get(Calendar.DAY_OF_MONTH);
			case Calendar.HOUR_OF_DAY:
				return calendar.get(Calendar.HOUR_OF_DAY);
			case Calendar.MINUTE:
				return calendar.get(Calendar.MINUTE);
			default:
				return 0;
		}
	}
}
