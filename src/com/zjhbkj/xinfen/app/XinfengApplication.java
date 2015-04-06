package com.zjhbkj.xinfen.app;

import android.app.Application;

import com.zjhbkj.xinfen.util.DBUtil;

public class XinfengApplication extends Application {

	public static XinfengApplication CONTEXT;

	@Override
	public void onCreate() {
		CONTEXT = this;
		super.onCreate();
		// 打开数据库
		new Thread(new Runnable() {
			@Override
			public void run() {
				DBUtil.getDataManager().firstOpen();

			}
		}).start();
	}

}
