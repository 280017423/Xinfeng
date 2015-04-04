package com.zjhbkj.xinfen.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjhbkj.xinfen.R;

public class SettingFragment extends FragmentBase implements OnClickListener {

	public static final SettingFragment newInstance() {
		SettingFragment fragment = new SettingFragment();
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
		View layout = inflater.inflate(R.layout.activity_setting, container, false);
		initViews(layout);
		return layout;
	}

	private void initViews(View layout) {
		TextView tvTitle = (TextView) layout.findViewById(R.id.title_with_back_title_btn_mid);
		tvTitle.setText(R.string.bottom_tab_setting);
	}

	@Override
	public void onClick(View v) {

	}

}
