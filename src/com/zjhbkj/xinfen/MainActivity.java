package com.zjhbkj.xinfen;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.zjhbkj.xinfen.wifihot.Global;
import com.zjhbkj.xinfen.wifihot.WifiHotAdmin;

public class MainActivity extends Activity implements OnClickListener {
	private static final String TAG = "MainActivity";
	private TextView mTvStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
	}

	private void initViews() {
		mTvStatus = (TextView) findViewById(R.id.tv_status);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_create_hot:
				WifiHotAdmin wifiAp = new WifiHotAdmin(MainActivity.this);
				wifiAp.startWifiAp(Global.SSID, Global.PASSWORD);
				break;

			default:
				break;
		}
	}
}
