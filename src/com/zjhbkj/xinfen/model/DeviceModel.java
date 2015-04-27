package com.zjhbkj.xinfen.model;

import com.zjhbkj.xinfen.orm.BaseModel;

public class DeviceModel extends BaseModel {
	private static final long serialVersionUID = 438886563533003934L;
	public static final String ID_VALUE = "ID_VALUE"; // idValue
	private String idValue; // idValue

	public String getIdValue() {
		return null == idValue ? "" : idValue;
	}

	public void setIdValue(String idValue) {
		this.idValue = idValue;
	}

}
