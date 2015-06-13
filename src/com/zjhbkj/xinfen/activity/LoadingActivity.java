package com.zjhbkj.xinfen.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.zjhbkj.xinfen.R;

public class LoadingActivity extends BaseActivity implements OnClickListener {

	private static final int DISPLAY_TIME = 3000;
	private ImageView mIvLogo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		initViews();
		jumpToMain();
	}

	private void initViews() {
		mIvLogo = (ImageView) findViewById(R.id.iv_logo);
		mIvLogo.setEnabled(false);
	}

	private void jumpToMain() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				startActivity(new Intent(LoadingActivity.this, DeviceListActivity.class));
				finish();
			}
		}, DISPLAY_TIME);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

			default:
				break;
		}
	}
}
