package com.zjhbkj.xinfen.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.zjhbkj.xinfen.R;
import com.zjhbkj.xinfen.app.XinfengApplication;
import com.zjhbkj.xinfen.commom.Global;
import com.zjhbkj.xinfen.db.DBMgr;
import com.zjhbkj.xinfen.model.StrainerModel;
import com.zjhbkj.xinfen.model.StrainerSendModel;
import com.zjhbkj.xinfen.util.CommandUtil;
import com.zjhbkj.xinfen.util.SharedPreferenceUtil;

import de.greenrobot.event.EventBus;

public class StrainerConfigActivity extends Activity implements OnClickListener {

	private View mEditView;
	private View mNormalView;

	TextView mTvChuxiaoLife;
	TextView mTvChuxiaoUsed;
	TextView mTvChuxiaoStatus;
	TextView mTvChuchenLife;
	TextView mTvChuchenUsed;
	TextView mTvChuchenStatus;
	TextView mTvGaoxiaoLife;
	TextView mTvGaoxiaoUsed;
	TextView mTvGaoxiaoStatus;

	private EditText mEdtChuxiao;
	private EditText mEdtChuchen;
	private EditText mEdtGaoxiao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_strainer_config);
		initVariables();
		initViews();
	}

	private void initVariables() {
		SharedPreferenceUtil.saveValue(XinfengApplication.CONTEXT, Global.CONFIG_FILE_NAME, Global.HAS_STRAINER_INFO,
				true);
		EventBus.getDefault().register(this);
		List<StrainerSendModel> configModels = DBMgr.getBaseModel(StrainerSendModel.class);
		if (null == configModels || configModels.isEmpty()) {
			StrainerSendModel model = new StrainerSendModel();
			String[] chuxiao = CommandUtil.formateHexString(Global.CHUXIAO_LIFE);
			String[] chuchen = CommandUtil.formateHexString(Global.CHUCHEN_LIFE);
			String[] gaoxiao = CommandUtil.formateHexString(Global.GAOXIAO_LIFE);
			model.setCommand1(chuxiao[0]);
			model.setCommand2(chuxiao[1]);
			model.setCommand3(chuchen[0]);
			model.setCommand4(chuchen[1]);
			model.setCommand5(gaoxiao[0]);
			model.setCommand6(gaoxiao[1]);
			model.setCommand7("1");
			model.setCommand8("1");
			model.setCommand9("1");
			DBMgr.saveModel(model);
		}
	}

	private void initViews() {
		initTitle();
		mEditView = findViewById(R.id.ll_edit_view);
		mNormalView = findViewById(R.id.ll_normal_view);
		mEdtChuxiao = (EditText) findViewById(R.id.edt_chuxiao);
		mEdtChuchen = (EditText) findViewById(R.id.edt_chuchen);
		mEdtGaoxiao = (EditText) findViewById(R.id.edt_gaoxiao);

		mTvChuxiaoLife = (TextView) findViewById(R.id.tv_chuxiao_life);
		mTvChuxiaoUsed = (TextView) findViewById(R.id.tv_chuxiao_used);
		mTvChuxiaoStatus = (TextView) findViewById(R.id.tv_chuxiao_status);
		mTvChuchenLife = (TextView) findViewById(R.id.tv_chuchen_life);
		mTvChuchenUsed = (TextView) findViewById(R.id.tv_chuchen_used);
		mTvChuchenStatus = (TextView) findViewById(R.id.tv_chuchen_status);
		mTvGaoxiaoLife = (TextView) findViewById(R.id.tv_gaoxiao_life);
		mTvGaoxiaoUsed = (TextView) findViewById(R.id.tv_gaoxiao_used);
		mTvGaoxiaoStatus = (TextView) findViewById(R.id.tv_gaoxiao_status);

		mTvChuxiaoLife.setText(getString(R.string.chuxiao_life, Global.CHUXIAO_LIFE));
		mTvChuchenLife.setText(getString(R.string.chuchen_life, Global.CHUCHEN_LIFE));
		mTvGaoxiaoLife.setText(getString(R.string.gaoxiao_life, Global.GAOXIAO_LIFE));
	}

	private void initTitle() {
		TextView tvTitle = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		TextView tvBack = (TextView) findViewById(R.id.tv_title_with_back_left);
		TextView tvSetting = (TextView) findViewById(R.id.tv_title_with_right);
		tvBack.setText("返回");
		tvSetting.setText("设置");
		tvSetting.setVisibility(View.GONE);
		tvTitle.setText(R.string.title_strainer_config);
		tvSetting.setOnClickListener(this);
		tvBack.setOnClickListener(this);
		tvSetting.setBackgroundResource(R.drawable.tongyong_button_bg_shape);
		tvBack.setBackgroundResource(R.drawable.tongyong_button_bg_shape);
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
				SharedPreferenceUtil.saveValue(XinfengApplication.CONTEXT, Global.CONFIG_FILE_NAME,
						Global.HAS_STRAINER_INFO, true);
				break;

			default:
				break;
		}
	}

	private void checkConfigInfo() {
	}

	/**
	 * 收到指令方法
	 * 
	 * @param model
	 *            指令数据
	 */
	public void onEventMainThread(StrainerModel model) {
		if (null == model) {
			return;
		}
		int chuxiaoYear = CommandUtil.hexStringToInt(model.getCommand2() + model.getCommand1());
		int chuchenYear = CommandUtil.hexStringToInt(model.getCommand4() + model.getCommand3());
		int gaoxiaoYear = CommandUtil.hexStringToInt(model.getCommand6() + model.getCommand5());
		mTvChuxiaoUsed.setText(getString(R.string.has_used, chuxiaoYear));
		mTvChuchenUsed.setText(getString(R.string.has_used, chuchenYear));
		mTvGaoxiaoUsed.setText(getString(R.string.has_used, gaoxiaoYear));

		mTvChuxiaoStatus.setText("1".equals(model.getCommand7()) ? "有效" : "过期");
		mTvChuchenStatus.setText("1".equals(model.getCommand8()) ? "有效" : "过期");
		mTvGaoxiaoStatus.setText("1".equals(model.getCommand9()) ? "有效" : "过期");
		mTvChuxiaoStatus.setVisibility(View.VISIBLE);
		mTvChuchenStatus.setVisibility(View.VISIBLE);
		mTvGaoxiaoStatus.setVisibility(View.VISIBLE);
	}

	@Override
	public void onDestroy() {
		try {
			EventBus.getDefault().unregister(this);
		} catch (Exception e) {
		}
		super.onDestroy();
	}
}
