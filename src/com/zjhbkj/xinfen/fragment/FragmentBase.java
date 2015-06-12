package com.zjhbkj.xinfen.fragment;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.zjhbkj.xinfen.util.StringUtil;

public abstract class FragmentBase extends Fragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * 默认的toast方法，该方法封装下面的两点特性：<br>
	 * 1、只有当前activity所属应用处于顶层时，才会弹出toast；<br>
	 * 2、默认弹出时间为 Toast.LENGTH_SHORT;
	 * 
	 * @param msg
	 *            弹出的信息内容
	 */
	public void toast(final String msg) {
		if (!isAdded()) {
			return;
		}
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (!StringUtil.isNullOrEmpty(msg)) {
					Toast toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
					TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
					// 用来防止某些系统自定义了消息框
					if (tv != null) {
						tv.setGravity(Gravity.CENTER);
					}
					toast.show();
				}
			}
		});
	}

	/**
	 * 
	 * @Method: onPositiveBtnClick
	 * @Description: 确定按钮回调
	 * @param id
	 *            当前对话框对象的ID
	 * @param dialog
	 *            DialogInterface 对象
	 * @param which
	 *            dialog ID
	 */
	public void onPositiveBtnClick(int id, DialogInterface dialog, int which) {
	}

	/**
	 * 
	 * @Method: onPositiveBtnClick
	 * @Description: 取消按钮回调
	 * @param id
	 *            当前对话框对象的ID
	 * @param dialog
	 *            DialogInterface 对象
	 * @param which
	 *            dialog ID
	 */
	public void onNegativeBtnClick(int id, DialogInterface dialog, int which) {
	}

	public boolean isLandscape() {
		return Configuration.ORIENTATION_LANDSCAPE == getResources().getConfiguration().orientation;
	}

}
