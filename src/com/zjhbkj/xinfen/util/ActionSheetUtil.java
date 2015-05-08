package com.zjhbkj.xinfen.util;

import android.content.Context;

import com.zjhbkj.xinfen.R;
import com.zjhbkj.xinfen.widget.ActionSheet;
import com.zjhbkj.xinfen.widget.ActionSheet.ActionSheetClickListener;

public class ActionSheetUtil {

	public static void showFunctionalSwitchActionSheet(Context context, ActionSheetClickListener listener) {
		context.setTheme(R.style.ActionSheetStyleIOS7);
		ActionSheet menuView = new ActionSheet(context);
		menuView.setCancelButtonTitle("取消");
		menuView.addItems("开", "关");
		menuView.setItemClickListener(listener);
		menuView.setCancelableOnTouchMenuOutside(true);
		menuView.showMenu();
	}

	public static void showShutStartActionSheet(Context context, ActionSheetClickListener listener) {
		context.setTheme(R.style.ActionSheetStyleIOS7);
		ActionSheet menuView = new ActionSheet(context);
		menuView.setCancelButtonTitle("取消");
		menuView.addItems("开机", "关机");
		menuView.setItemClickListener(listener);
		menuView.setCancelableOnTouchMenuOutside(true);
		menuView.showMenu();
	}

	public static void showWifiModeActionSheet(Context context, ActionSheetClickListener listener) {
		context.setTheme(R.style.ActionSheetStyleIOS7);
		ActionSheet menuView = new ActionSheet(context);
		menuView.setCancelButtonTitle("取消");
		menuView.addItems("内网", "外网");
		menuView.setItemClickListener(listener);
		menuView.setCancelableOnTouchMenuOutside(true);
		menuView.showMenu();
	}

}
