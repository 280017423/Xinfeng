package com.zjhbkj.xinfen.activity;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zjhbkj.xinfen.R;
import com.zjhbkj.xinfen.db.DBMgr;
import com.zjhbkj.xinfen.model.IdConfigModel;
import com.zjhbkj.xinfen.util.StringUtil;

public class IdConfigActivity extends BaseActivity implements OnClickListener {

	private EditText mEdtId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_id_config);
		initVariables();
		initViews();
	}

	private void initVariables() {
	}

	private void initViews() {
		TextView tvTitle = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		tvTitle.setText(R.string.title_id_config);

		TextView tvTitleLeft = (TextView) findViewById(R.id.tv_title_with_back_left);
		tvTitleLeft.setBackgroundResource(R.drawable.btn_back_bg);
		findViewById(R.id.title_with_back_title_btn_left).setOnClickListener(this);
		
		mEdtId = (EditText) findViewById(R.id.edt_id);
		List<IdConfigModel> configModels = DBMgr.getBaseModel(IdConfigModel.class);
		if (null != configModels && !configModels.isEmpty()) {
			IdConfigModel model = configModels.get(0);
			if (null != model) {
				mEdtId.setText(model.getIdValue());
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_set_config_info:
				checkConfigInfo();
				break;
			case R.id.title_with_back_title_btn_left:
				finish();
				break;
			default:
				break;
		}
	}

	private void checkConfigInfo() {
		String id = mEdtId.getText().toString().trim();
		if (StringUtil.isNullOrEmpty(id)) {
			Toast.makeText(IdConfigActivity.this, "设备ID不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		int idValue = Integer.parseInt(id);
		if (idValue > 16777215) {
			Toast.makeText(IdConfigActivity.this, "ID范围太大", Toast.LENGTH_LONG).show();
			return;
		}
		IdConfigModel model = new IdConfigModel();
		model.setIdValue(id);
		model.setHasSent(0);
		DBMgr.deleteTableFromDb(IdConfigModel.class);
		DBMgr.saveModel(model);
		Toast.makeText(IdConfigActivity.this, "保存成功", Toast.LENGTH_LONG).show();
	}
}
