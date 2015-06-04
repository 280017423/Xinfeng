package com.zjhbkj.xinfen.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zjhbkj.xinfen.R;
import com.zjhbkj.xinfen.app.XinfengApplication;
import com.zjhbkj.xinfen.commom.Global;
import com.zjhbkj.xinfen.db.DBMgr;
import com.zjhbkj.xinfen.model.ConfigModel;
import com.zjhbkj.xinfen.util.SharedPreferenceUtil;
import com.zjhbkj.xinfen.util.StringUtil;

public class ConfigActivity extends BaseActivity implements OnClickListener {

	private EditText mEdtSsid;
	private EditText mEdtPassword;
	private EditText mEdtServerIp;
	private EditText mEdtServerPort;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);
		initVariables();
		initViews();
	}

	private void initVariables() {
	}

	private void initViews() {
		TextView tvTitle = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		tvTitle.setText(R.string.title_server_config);
		mEdtSsid = (EditText) findViewById(R.id.edt_ssid);
		mEdtPassword = (EditText) findViewById(R.id.edt_password);
		mEdtServerIp = (EditText) findViewById(R.id.edt_server_address);
		mEdtServerPort = (EditText) findViewById(R.id.edt_server_port);
		List<ConfigModel> configModels = DBMgr.getBaseModel(ConfigModel.class);
		if (null != configModels && !configModels.isEmpty()) {
			ConfigModel model = configModels.get(0);
			if (null != model) {
				mEdtSsid.setText(model.getSsid());
				mEdtPassword.setText(model.getWifiPassword());
				mEdtServerIp.setText(model.getServerIp());
				mEdtServerPort.setText(model.getServerPort());
			}
		} else {
			mEdtServerIp.setText(SharedPreferenceUtil.getStringValueByKey(XinfengApplication.CONTEXT,
					Global.GLOBAL_FILE_NAME, Global.GLOBAL_SERVER_IP));
			mEdtServerPort.setText(SharedPreferenceUtil.getStringValueByKey(XinfengApplication.CONTEXT,
					Global.GLOBAL_FILE_NAME, Global.GLOBAL_SERVER_PORT));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_set_config_info:
				checkConfigInfo();
				break;

			default:
				break;
		}
	}

	private void checkConfigInfo() {
		String ssid = mEdtSsid.getText().toString().trim();
		String password = mEdtPassword.getText().toString().trim();
		String serverIp = mEdtServerIp.getText().toString().trim();
		String serverPort = mEdtServerPort.getText().toString().trim();
		if (StringUtil.isNullOrEmpty(ssid)) {
			Toast.makeText(ConfigActivity.this, "wifi名字不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		if (StringUtil.isNullOrEmpty(password)) {
			Toast.makeText(ConfigActivity.this, "wifi密码不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		if (StringUtil.isNullOrEmpty(serverIp)) {
			Toast.makeText(ConfigActivity.this, "服务器Ip不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		if (StringUtil.isNullOrEmpty(serverPort)) {
			Toast.makeText(ConfigActivity.this, "服务器端口不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		ConfigModel model = new ConfigModel();
		model.setSsid(ssid);
		model.setWifiPassword(password);
		model.setServerIp(serverIp);
		model.setServerPort(serverPort);
		model.setHasSent(0);
		DBMgr.deleteTableFromDb(ConfigModel.class);
		DBMgr.saveModel(model);
		SharedPreferenceUtil.saveValue(XinfengApplication.CONTEXT, Global.GLOBAL_FILE_NAME, Global.GLOBAL_SERVER_IP,
				serverIp);
		SharedPreferenceUtil.saveValue(XinfengApplication.CONTEXT, Global.GLOBAL_FILE_NAME, Global.GLOBAL_SERVER_PORT,
				serverPort);
		Toast.makeText(ConfigActivity.this, "保存成功", Toast.LENGTH_LONG).show();
	}
}
