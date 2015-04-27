package com.zjhbkj.xinfen.db;

import java.util.List;

import com.zjhbkj.xinfen.model.ConfigModel;
import com.zjhbkj.xinfen.model.IdConfigModel;
import com.zjhbkj.xinfen.orm.BaseModel;
import com.zjhbkj.xinfen.orm.DataAccessException;
import com.zjhbkj.xinfen.orm.DataManager;
import com.zjhbkj.xinfen.util.DBUtil;
import com.zjhbkj.xinfen.util.StringUtil;

/**
 * 数据库管理类
 * 
 * @author zou.sq
 */
public class DBMgr {
	private DBMgr() {
	}

	/**
	 * 
	 * @Name saveModelAsync
	 * @Description 保存model
	 * @param model
	 *            需保存的对象
	 * @param <T>
	 *            泛型
	 * 
	 */
	public static <T extends BaseModel> void saveModel(final T model) {
		if (null == model) {
			return;
		}
		DataManager dataManager = DBUtil.getDataManager();
		try {
			dataManager.save(model);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

	public static <T extends BaseModel> void deleteModel(final T model) {
		if (null == model) {
			return;
		}
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.delete(model.getClass(), model.getID());
	}

	public static <T extends BaseModel> void saveModel(final T model, String primaryKey, String primaryValue) {
		if (null == model) {
			return;
		}
		DataManager dataManager = DBUtil.getDataManager();
		try {
			dataManager.save(model, primaryKey, primaryValue);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Name saveModelAsync
	 * @Description 保存models
	 * @param model
	 *            需保存的对象
	 * @param <T>
	 *            泛型
	 * 
	 */
	public static <T extends BaseModel> void saveModels(final List<T> models) {
		if (null == models || models.isEmpty()) {
			return;
		}
		DataManager dataManager = DBUtil.getDataManager();
		try {
			for (int i = 0; i < models.size(); i++) {
				dataManager.save(models.get(i));
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Name getLocalNewsModel
	 * @Description 获取BaseModel本地记录，倒序
	 * @param type
	 *            model类型
	 * @param <T>
	 *            BaseModel类型子类
	 * @return List<T> BaseModel类型集合
	 * 
	 */
	public static <T extends BaseModel> List<T> getBaseModel(Class<T> type) {
		List<T> results = null;
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.open();
		try {
			results = dataManager.getList(type, null, null, "_id desc", null);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		dataManager.close();
		return results;
	}

	public static <T extends BaseModel> T getHistoryData(Class<T> type, String commandNum) {
		T results = null;
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.open();
		try {
			results = dataManager.get(type, "COMMAND_NUM = ?", new String[] { commandNum });
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		dataManager.close();
		return results;
	}

	public static ConfigModel getConfigModel(String hasSent) {
		ConfigModel results = null;
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.open();
		try {
			results = dataManager.get(ConfigModel.class, "HAS_SENT = ?", new String[] { hasSent });
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		dataManager.close();
		return results;
	}

	public static IdConfigModel getIdConfigModel(String hasSent) {
		IdConfigModel results = null;
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.open();
		try {
			results = dataManager.get(IdConfigModel.class, "HAS_SENT = ?", new String[] { hasSent });
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		dataManager.close();
		return results;
	}

	/**
	 * 
	 * @Name deleteTeachersFromDb
	 * @Description 删除老师信息
	 * @param type
	 *            指定的model
	 * @param <T>
	 *            BaseModel的子类
	 * @return boolean true 删除成功，false 删除失败
	 * 
	 */
	public static <T extends BaseModel> boolean deleteTableFromDb(Class<T> type) {
		boolean isSucces = false;
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.open();
		isSucces = dataManager.delete(type, null, null);
		dataManager.close();
		return isSucces;
	}

	/**
	 * 删除数据库的表
	 * 
	 * @param tableName
	 *            表名字
	 * @param <T>
	 *            BaseModel的子类
	 * @return boolean 是否删除成功
	 */
	public static <T extends BaseModel> boolean deleteTableFromDb(String tableName) {
		boolean isSucces = false;
		if (StringUtil.isNullOrEmpty(tableName)) {
			return isSucces;
		}
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.open();
		isSucces = dataManager.delete(tableName, null, null);
		dataManager.close();
		return isSucces;
	}

}
