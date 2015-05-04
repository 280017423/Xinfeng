package com.zjhbkj.xinfen.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zjhbkj.xinfen.R;
import com.zjhbkj.xinfen.activity.AboutActivity;
import com.zjhbkj.xinfen.activity.ConfigActivity;
import com.zjhbkj.xinfen.activity.DeviceListActivity;
import com.zjhbkj.xinfen.activity.IdConfigActivity;
import com.zjhbkj.xinfen.activity.StrainerConfigActivity;
import com.zjhbkj.xinfen.app.XinfengApplication;
import com.zjhbkj.xinfen.commom.Global;
import com.zjhbkj.xinfen.util.DBUtil;
import com.zjhbkj.xinfen.util.SharedPreferenceUtil;
import com.zjhbkj.xinfen.util.StringUtil;

public class MoreFragment extends FragmentBase implements OnClickListener {

	private View mViewIdConfig;
	private View mViewStrainer;
	private View mViewWifiConfig;
	private View mViewAbout;
	private Button mBtnLogout;

	public static final MoreFragment newInstance() {
		MoreFragment fragment = new MoreFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		initVariables();
		super.onCreate(savedInstanceState);
	}

	private void initVariables() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View layout = inflater.inflate(R.layout.activity_more, container, false);
		initViews(layout);
		setListener();
		return layout;
	}

	private void setListener() {
		mViewIdConfig.setOnClickListener(this);
		mViewWifiConfig.setOnClickListener(this);
		mViewStrainer.setOnClickListener(this);
		mViewAbout.setOnClickListener(this);
		mBtnLogout.setOnClickListener(this);
	}

	private void initViews(View layout) {
		TextView tvTitle = (TextView) layout.findViewById(R.id.title_with_back_title_btn_mid);
		tvTitle.setText(R.string.bottom_tab_more);
		mViewIdConfig = layout.findViewById(R.id.rl_id_config);
		mViewAbout = layout.findViewById(R.id.rl_id_about);
		mViewWifiConfig = layout.findViewById(R.id.rl_config);
		mViewStrainer = layout.findViewById(R.id.rl_strainer_config);
		mBtnLogout = (Button) layout.findViewById(R.id.btn_logout);
		String deviceName = SharedPreferenceUtil.getStringValueByKey(XinfengApplication.CONTEXT,
				Global.CONFIG_FILE_NAME, Global.CURRENT_DEVICE_ID);
		if (!StringUtil.isNullOrEmpty(deviceName)) {
			mBtnLogout.setText("退出" + deviceName);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.rl_config:
				startActivity(new Intent(getActivity(), ConfigActivity.class));
				break;
			case R.id.rl_id_about:
				startActivity(new Intent(getActivity(), AboutActivity.class));
				break;
			case R.id.rl_strainer_config:
				startActivity(new Intent(getActivity(), StrainerConfigActivity.class));
				break;
			case R.id.rl_id_config:
				startActivity(new Intent(getActivity(), IdConfigActivity.class));
				break;
			case R.id.btn_logout:
				SharedPreferenceUtil.clearObject(XinfengApplication.CONTEXT, Global.CONFIG_FILE_NAME);
				DBUtil.clearAllTables();
				startActivity(new Intent(getActivity(), DeviceListActivity.class));
				getActivity().finish();
				break;
			default:
				break;
		}
	}
}
