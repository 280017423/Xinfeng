package com.zjhbkj.xinfen.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhbkj.xinfen.R;
import com.zjhbkj.xinfen.commom.Global;
import com.zjhbkj.xinfen.listener.StartWifiApListener;
import com.zjhbkj.xinfen.util.WifiApUtil;

public class LoadingActivity extends Activity implements OnClickListener {

	private ImageView mIvLogo;
	private TextView mTvStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		initViews();
		startWifiAp();
	}

	private void initViews() {
		mIvLogo = (ImageView) findViewById(R.id.iv_logo);
		mTvStatus = (TextView) findViewById(R.id.tv_status);
		mIvLogo.setEnabled(false);
	}

	private void startWifiAp() {
		mTvStatus.setText("正在开启热点...");
		mIvLogo.setEnabled(false);
		WifiApUtil wifiAp = new WifiApUtil(this);
		wifiAp.startWifiAp(Global.SSID, Global.PASSWORD, new StartWifiApListener() {

			@Override
			public void enableWifiApSuccess() {
				LoadingActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						startActivity(new Intent(LoadingActivity.this, MainActivity.class));
						finish();
					}
				});
			}

			@Override
			public void enableWifiApFail() {
				LoadingActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						mIvLogo.setEnabled(true);
						mTvStatus.setText("点击上面图标重新开启热点");
					}
				});
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_logo:
				startWifiAp();
				break;

			default:
				break;
		}
	}
}
