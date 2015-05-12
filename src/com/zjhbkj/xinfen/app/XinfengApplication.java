package com.zjhbkj.xinfen.app;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;
import com.zjhbkj.xinfen.util.DBUtil;

public class XinfengApplication extends Application {

	public static XinfengApplication CONTEXT;

	@Override
	public void onCreate() {
		CONTEXT = this;
		super.onCreate();
		String appId = "900003121"; // 上Bugly(bugly.qq.com)注册产品获取的AppId
		boolean isDebug = false; // true代表App处于调试阶段，false代表App发布阶段
		CrashReport.initCrashReport(this, appId, isDebug); // 初始化SDK
		// 打开数据库
		new Thread(new Runnable() {
			@Override
			public void run() {
				DBUtil.getDataManager().firstOpen();
			}
		}).start();
	}

}
