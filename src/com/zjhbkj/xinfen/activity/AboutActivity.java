package com.zjhbkj.xinfen.activity;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.zjhbkj.xinfen.R;

public class AboutActivity extends BaseActivity implements OnClickListener {

	private String mVersionName;
	private View mIvLogo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		initVariables();
		initView();
	}

	private void initVariables() {
		try {
			mVersionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void initView() {

		TextView titleTextView = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		titleTextView.setText(R.string.about);
		TextView tvVersion = (TextView) findViewById(R.id.tv_version_code);
		tvVersion.setText(getString(R.string.text_version_code, mVersionName));
		mIvLogo = findViewById(R.id.iv_logo);
		// UIUtil.setViewWidth(mIvLogo, UIUtil.getScreenWidth(this) / 4);
		// UIUtil.setViewHeight(mIvLogo, UIUtil.getScreenWidth(this) / 4);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			default:
				break;
		}
	}
}
