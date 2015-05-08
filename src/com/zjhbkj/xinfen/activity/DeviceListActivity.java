package com.zjhbkj.xinfen.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.update.UmengUpdateAgent;
import com.zjhbkj.xinfen.R;
import com.zjhbkj.xinfen.adapter.DeviceAdapter;
import com.zjhbkj.xinfen.app.XinfengApplication;
import com.zjhbkj.xinfen.commom.Global;
import com.zjhbkj.xinfen.db.DBMgr;
import com.zjhbkj.xinfen.listener.StartWifiApListener;
import com.zjhbkj.xinfen.model.DeviceModel;
import com.zjhbkj.xinfen.util.DBUtil;
import com.zjhbkj.xinfen.util.SharedPreferenceUtil;
import com.zjhbkj.xinfen.util.StringUtil;
import com.zjhbkj.xinfen.util.WifiApUtil;
import com.zjhbkj.xinfen.widget.LoadingUpView;

public class DeviceListActivity extends BaseActivity implements OnClickListener {

	private long mExitTime;
	private List<DeviceModel> mDeviceModels;
	private ListView mLvDeviceList;
	private EditText mEdtId;
	private DeviceAdapter mDeviceAdapter;
	private LoadingUpView mLoadingUpView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_list);
		initVariables();
		initViews();
		getDeviceList();
		UmengUpdateAgent.update(this);
		UmengUpdateAgent.setUpdateOnlyWifi(false);
	}

	private void initVariables() {
		mLoadingUpView = new LoadingUpView(this, true);
		mDeviceModels = new ArrayList<DeviceModel>();
		mDeviceAdapter = new DeviceAdapter(this, mDeviceModels, this);
	}

	private void initViews() {
		TextView tvTitle = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		tvTitle.setText(R.string.title_device_list);

		mLvDeviceList = (ListView) findViewById(R.id.lv_device_list);
		mEdtId = (EditText) findViewById(R.id.edt_id);
		mLvDeviceList.setAdapter(mDeviceAdapter);
		mLvDeviceList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				DeviceModel model = (DeviceModel) parent.getAdapter().getItem(position);
				if (null != model && !StringUtil.isNullOrEmpty(model.getIdValue())) {
					SharedPreferenceUtil.saveValue(XinfengApplication.CONTEXT, Global.CONFIG_FILE_NAME,
							Global.CURRENT_DEVICE_ID, model.getIdValue());
					startWifiAp();
				}
			}
		});
	}

	private void getDeviceList() {
		List<DeviceModel> models = DBMgr.getBaseModel(DeviceModel.class);
		if (null != models) {
			mDeviceModels.clear();
			mDeviceModels.addAll(models);
			mDeviceAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_set_config_info:
				addDevice();
				break;
			case R.id.btn_del_device:
				delDevice((DeviceModel) v.getTag());
				break;
			default:
				break;
		}
	}

	private void delDevice(DeviceModel model) {
		if (null == model) {
			return;
		}
		DBMgr.deleteModel(model);
		getDeviceList();
	}

	private void addDevice() {
		String id = mEdtId.getText().toString().trim();
		if (StringUtil.isNullOrEmpty(id)) {
			Toast.makeText(this, "设备ID不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		int idValue = Integer.parseInt(id);
		if (idValue > 16777215) {
			Toast.makeText(this, "ID范围太大", Toast.LENGTH_LONG).show();
			return;
		}
		for (int i = 0; i < mDeviceModels.size(); i++) {
			if (id.equals(mDeviceModels.get(i).getIdValue())) {
				Toast.makeText(this, "设备已添加", Toast.LENGTH_LONG).show();
				return;
			}
		}
		DeviceModel model = new DeviceModel();
		model.setIdValue(id);
		DBMgr.saveModel(model, DeviceModel.ID_VALUE + " = ?", id);
		getDeviceList();
		mEdtId.setText("");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_LONG).show();
				mExitTime = System.currentTimeMillis();
			} else {
				WifiApUtil.closeWifiAp(this);
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void startWifiAp() {
		if (null != mLoadingUpView && !mLoadingUpView.isShowing()) {
			mLoadingUpView.showPopup("正在开启热点");
		}
		WifiApUtil wifiAp = new WifiApUtil(this);
		wifiAp.startWifiAp(Global.SSID, Global.PASSWORD, new StartWifiApListener() {

			@Override
			public void enableWifiApSuccess() {
				DeviceListActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						DBUtil.clearAllTables();
						startActivity(new Intent(DeviceListActivity.this, MainActivity.class));
						finish();
					}
				});
			}

			@Override
			public void enableWifiApFail() {
				DeviceListActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(DeviceListActivity.this, "热点开启失败，请重试", Toast.LENGTH_LONG).show();
					}
				});
			}
		});
	}
}
