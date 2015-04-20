package com.zjhbkj.xinfen.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjhbkj.xinfen.R;
import com.zjhbkj.xinfen.activity.ConfigActivity;
import com.zjhbkj.xinfen.activity.StrainerConfigActivity;

public class MoreFragment extends FragmentBase implements OnClickListener {

	private View mViewCheck;
	private View mViewStrainer;
	private View mViewWifiConfig;
	private View mViewAbout;

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
		mViewCheck.setOnClickListener(this);
		mViewWifiConfig.setOnClickListener(this);
		mViewStrainer.setOnClickListener(this);
		mViewAbout.setOnClickListener(this);
	}

	private void initViews(View layout) {
		TextView tvTitle = (TextView) layout.findViewById(R.id.title_with_back_title_btn_mid);
		tvTitle.setText(R.string.bottom_tab_more);
		mViewCheck = layout.findViewById(R.id.rl_check_new_version);
		mViewWifiConfig = layout.findViewById(R.id.rl_config);
		mViewStrainer = layout.findViewById(R.id.rl_strainer_config);
		mViewAbout = layout.findViewById(R.id.rl_about);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.rl_config:
				startActivity(new Intent(getActivity(), ConfigActivity.class));
				break;
			case R.id.rl_strainer_config:
				startActivity(new Intent(getActivity(), StrainerConfigActivity.class));
				break;
			case R.id.rl_check_new_version:
				toast("开发中...");
				break;
			case R.id.rl_about:
				toast("开发中...");
				break;

			default:
				break;
		}
	}

}
