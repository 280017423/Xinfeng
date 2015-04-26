package com.zjhbkj.xinfen.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainTabAdapter extends FragmentPagerAdapter {

	private List<Fragment> mFragments;

	public MainTabAdapter(FragmentManager fm) {
		super(fm);
	}

	public MainTabAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.mFragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		if (null != mFragments) {
			return mFragments.get(position);
		}
		return null;
	}

	@Override
	public int getCount() {
		if (null != mFragments) {
			return mFragments.size();
		}
		return 0;
	}

}
