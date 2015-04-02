package com.zjhbkj.xinfen.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.zjhbkj.xinfen.R;

public class MoreFragment extends FragmentBase implements OnClickListener {

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
		return layout;
	}

	private void initViews(View layout) {

	}

	@Override
	public void onClick(View v) {

	}

}
