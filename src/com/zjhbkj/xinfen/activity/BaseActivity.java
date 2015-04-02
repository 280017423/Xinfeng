package com.zjhbkj.xinfen.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.zjhbkj.xinfen.listener.IDialogProtocol;
import com.zjhbkj.xinfen.util.DialogManager;
import com.zjhbkj.xinfen.widget.CustomDialog.Builder;

class BaseActivity extends Activity implements IDialogProtocol {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (getParent() != null) {
			return getParent().onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 处理ActivityResult方法
	 * 
	 * @param requestCode
	 *            请求码
	 * @param resultCode
	 *            结果码
	 * @param data
	 *            intent数据
	 * 
	 */
	public void handleActivityResult(int requestCode, int resultCode, Intent data) {
	}

	@Override
	public Builder createDialogBuilder(Context context, String title, String message, String positiveBtnName,
			String negativeBtnName) {
		return DialogManager
				.createMessageDialogBuilder(context, title, message, positiveBtnName, negativeBtnName, this);
	}

	@Override
	public void onPositiveBtnClick(int id, DialogInterface dialog, int which) {

	}

	@Override
	public void onNegativeBtnClick(int id, DialogInterface dialog, int which) {

	}
}
