package com.zjhbkj.xinfen.util;

import android.content.Context;

import com.zjhbkj.xinfen.app.XinfengApplication;
import com.zjhbkj.xinfen.model.ConfigModel;
import com.zjhbkj.xinfen.model.RcvComsModel;
import com.zjhbkj.xinfen.model.SendComsModel;
import com.zjhbkj.xinfen.orm.DataManager;
import com.zjhbkj.xinfen.orm.DatabaseBuilder;

/**
 * 数据库初始化类
 * 
 * @author Wang.xy
 * @since 2013-03-28 zeng.ww 添加SpecialDishModel表初始化操作
 */
public class DBUtil {
	private static final String TAG = "pmr.DBUtil";

	private static DatabaseBuilder DATABASE_BUILDER;
	private static PMRDataManager INSTANCE;

	// 获取数据库实例
	static {
		EvtLog.d(TAG, "DBUtil static, pmr");

		if (DATABASE_BUILDER == null) {
			DATABASE_BUILDER = new DatabaseBuilder(PackageUtil.getConfigString("db_name"));
			DATABASE_BUILDER.addClass(SendComsModel.class);
			DATABASE_BUILDER.addClass(RcvComsModel.class);
			DATABASE_BUILDER.addClass(ConfigModel.class);
		}
	}

	/**
	 * 清除所有的数据表
	 */
	public static void clearAllTables() {
		if (null != DATABASE_BUILDER) {
			String[] tables = DATABASE_BUILDER.getTables();
			for (int i = 0; i < tables.length; i++) {
				// DBMgr.deleteTableFromDb(tables[i]);
			}
		}
	}

	/**
	 * 
	 * @return 数据库管理器
	 */
	public static DataManager getDataManager() {
		EvtLog.d(TAG, "PMRDataManager getDataManager, pmr");
		if (INSTANCE == null) {
			INSTANCE = new PMRDataManager(XinfengApplication.CONTEXT, DATABASE_BUILDER);
		}
		return INSTANCE;
	}

	static class PMRDataManager extends DataManager {
		protected PMRDataManager(Context context, DatabaseBuilder databaseBuilder) {
			super(context, PackageUtil.getConfigString("db_name"), PackageUtil.getConfigInt("db_version"), databaseBuilder);
			EvtLog.d(TAG, "PMRDataManager, pmr");
		}
	}
}
