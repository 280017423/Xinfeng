package com.zjhbkj.xinfen.model;

import com.zjhbkj.xinfen.orm.BaseModel;

public class IdConfigModel extends BaseModel {
	private static final long serialVersionUID = -9047719872716555337L;
	private String idValue; // idValue
	private int hasSent; // 是否已经发送

	public String getIdValue() {
		return idValue;
	}

	public void setIdValue(String idValue) {
		this.idValue = idValue;
	}

	public int getHasSent() {
		return hasSent;
	}

	public void setHasSent(int hasSent) {
		this.hasSent = hasSent;
	}

}
