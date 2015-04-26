package com.zjhbkj.xinfen.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhbkj.xinfen.R;
import com.zjhbkj.xinfen.adapter.MainTabAdapter;
import com.zjhbkj.xinfen.db.DBMgr;
import com.zjhbkj.xinfen.fragment.HomeFragment;
import com.zjhbkj.xinfen.fragment.MoreFragment;
import com.zjhbkj.xinfen.fragment.SettingFragment;
import com.zjhbkj.xinfen.model.RcvComsModel;
import com.zjhbkj.xinfen.model.SendComsModel;
import com.zjhbkj.xinfen.util.WifiApUtil;

public class MainActivity extends BaseFragmentActivity implements OnClickListener {

	private static final int DIALOG_EXIT_APP = 1;
	private FragmentManager fragmentManager;
	private HomeFragment mHomeFragment;
	private SettingFragment mSettingFragment;
	private MoreFragment mMoreFragment;
	private ViewPager mViewPager;
	private MainTabAdapter mAdapter;
	private List<Fragment> mFragments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initVariables();
		initViews();
	}

	private void initVariables() {
		initSendData();
		mFragments = new ArrayList<Fragment>();
		fragmentManager = getSupportFragmentManager();
		mHomeFragment = HomeFragment.newInstance();
		mSettingFragment = SettingFragment.newInstance();
		mMoreFragment = MoreFragment.newInstance();
		mFragments.clear();
		mFragments.add(mHomeFragment);
		mFragments.add(mSettingFragment);
		mFragments.add(mMoreFragment);
		mAdapter = new MainTabAdapter(fragmentManager, mFragments);
	}

	private void initViews() {
		hideFragments();
		setHomeClickedStatus();
		mViewPager = (ViewPager) findViewById(R.id.viewpager_main);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				switch (position) {
					case 0:
						hideFragments();
						setHomeClickedStatus();
						break;
					case 1:
						hideFragments();
						setActivityClickedStatus();
						break;
					case 2:
						hideFragments();
						setMyCenterClickedStatus();
						break;

					default:
						break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	private void hideFragments() {
		setHomeUnClickedStatus();
		setMyCenterUnClickedStatus();
		setActivityUnClickedStatus();
	}

	private void setHomeUnClickedStatus() {
		ImageView homeImage = (ImageView) this.findViewById(R.id.iv_home_tab);
		TextView homeText = (TextView) this.findViewById(R.id.tv_home_tab);
		homeText.setTextColor(getResources().getColor(R.color.tab_text_color));
		homeImage.setImageDrawable(getResources().getDrawable(R.drawable.tab_home_normal));
	}

	private void setHomeClickedStatus() {
		ImageView homeImage = (ImageView) this.findViewById(R.id.iv_home_tab);
		TextView homeText = (TextView) this.findViewById(R.id.tv_home_tab);
		homeText.setTextColor(getResources().getColor(R.color.tab_select_text_color));
		homeImage.setImageDrawable(getResources().getDrawable(R.drawable.tab_home_pressed));
	}

	private void setActivityUnClickedStatus() {
		ImageView homeImage = (ImageView) this.findViewById(R.id.iv_setting_tab);
		TextView homeText = (TextView) this.findViewById(R.id.tv_setting_tab);
		homeText.setTextColor(getResources().getColor(R.color.tab_text_color));
		homeImage.setImageDrawable(getResources().getDrawable(R.drawable.tab_setting_normal));
	}

	private void setActivityClickedStatus() {
		ImageView homeImage = (ImageView) this.findViewById(R.id.iv_setting_tab);
		TextView homeText = (TextView) this.findViewById(R.id.tv_setting_tab);
		homeText.setTextColor(getResources().getColor(R.color.tab_select_text_color));
		homeImage.setImageDrawable(getResources().getDrawable(R.drawable.tab_setting_pressed));
	}

	private void setMyCenterUnClickedStatus() {
		ImageView homeImage = (ImageView) this.findViewById(R.id.iv_more_tab);
		TextView homeText = (TextView) this.findViewById(R.id.tv_more_tab);
		homeText.setTextColor(getResources().getColor(R.color.tab_text_color));
		homeImage.setImageDrawable(getResources().getDrawable(R.drawable.tab_more_normal));
	}

	private void setMyCenterClickedStatus() {
		ImageView homeImage = (ImageView) this.findViewById(R.id.iv_more_tab);
		TextView homeText = (TextView) this.findViewById(R.id.tv_more_tab);
		homeText.setTextColor(getResources().getColor(R.color.tab_select_text_color));
		homeImage.setImageDrawable(getResources().getDrawable(R.drawable.tab_more_pressed));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.rl_home_tab_layout:
				mViewPager.setCurrentItem(0);
				break;
			case R.id.rl_setting_tab_layout:
				mViewPager.setCurrentItem(1);
				break;
			case R.id.rl_more_tab_layout:
				mViewPager.setCurrentItem(2);
				break;
			default:
				break;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DIALOG_EXIT_APP:
				return createDialogBuilder(this, getString(R.string.button_text_tips),
						getString(R.string.exit_dialog_title), getString(R.string.button_text_no),
						getString(R.string.button_text_yes)).create(id);

			default:
				break;
		}
		return super.onCreateDialog(id);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showDialog(DIALOG_EXIT_APP);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onNegativeBtnClick(int id, DialogInterface dialog, int which) {
		switch (id) {
			case DIALOG_EXIT_APP:
				WifiApUtil.closeWifiAp(this);
				finish();
				break;
			default:
				break;
		}
		super.onNegativeBtnClick(id, dialog, which);
	}

	private void initSendData() {
		// 第一次进来拿到设备发过来的初始数据，初始化设置界面
		SendComsModel mSendComsModel = DBMgr.getHistoryData(SendComsModel.class, "EA");
		if (null == mSendComsModel) {
			mSendComsModel = new SendComsModel();
			RcvComsModel model = DBMgr.getHistoryData(RcvComsModel.class, "DA");
			mSendComsModel.setCommand1(Integer.toHexString(3)); // 上报时间为3秒
			mSendComsModel.setCommand2(Integer.toHexString(10));
			mSendComsModel.setCommand3(Integer.toHexString(1));
			mSendComsModel.setCommand4(Integer.toHexString(1));
			if (null != model) {
				mSendComsModel.setCommand3(model.getCommand3());
				mSendComsModel.setCommand4(model.getCommand4());
			}
			mSendComsModel.setCommand10(Integer.toHexString(19));
			mSendComsModel.setCommand11(Integer.toHexString(14));
			mSendComsModel.setCommand12(Integer.toHexString(30));
			mSendComsModel.setCommand13(Integer.toHexString(20));
			mSendComsModel.setCommand14(Integer.toHexString(02));
			mSendComsModel.setCommand15(Integer.toHexString(100));
			mSendComsModel.send();
		}
	}

}
