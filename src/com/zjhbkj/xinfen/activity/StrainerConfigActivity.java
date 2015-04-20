package com.zjhbkj.xinfen.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.zjhbkj.xinfen.R;

public class StrainerConfigActivity extends Activity implements OnClickListener {

	private EditText mEdtChuxiao;
	private EditText mEdtChuchen;
	private EditText mEdtGaoxiao;
	private View mEditView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_strainer_config);
		initVariables();
		initViews();
	}

	private void initVariables() {
	}

	private void initViews() {
		TextView tvTitle = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		TextView tvBack = (TextView) findViewById(R.id.tv_title_with_back_left);
		TextView tvSetting = (TextView) findViewById(R.id.tv_title_with_right);
		tvBack.setText("返回");
		tvSetting.setText("设置");
		tvTitle.setText(R.string.title_strainer_config);
		tvSetting.setOnClickListener(this);
		tvBack.setOnClickListener(this);
		tvSetting.setBackgroundResource(R.drawable.tongyong_button_bg_shape);
		tvBack.setBackgroundResource(R.drawable.tongyong_button_bg_shape);
		mEditView = findViewById(R.id.ll_edit_view);
		mEdtChuxiao = (EditText) findViewById(R.id.edt_chuxiao);
		mEdtChuchen = (EditText) findViewById(R.id.edt_chuchen);
		mEdtGaoxiao = (EditText) findViewById(R.id.edt_gaoxiao);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_title_with_back_left:
				finish();
				break;
			case R.id.tv_title_with_right:
				// TODO 显示是否编辑
				break;
			case R.id.btn_set_config_info:
				break;

			default:
				break;
		}
	}

	private void checkConfigInfo() {
	}
}
