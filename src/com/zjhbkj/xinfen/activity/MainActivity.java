package com.zjhbkj.xinfen.activity;

import java.util.List;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhbkj.xinfen.R;
import com.zjhbkj.xinfen.fragment.HomeFragment;
import com.zjhbkj.xinfen.fragment.MoreFragment;
import com.zjhbkj.xinfen.fragment.SettingFragment;
import com.zjhbkj.xinfen.util.WifiApUtil;

public class MainActivity extends BaseFragmentActivity implements OnClickListener {

	private static final int DIALOG_EXIT_APP = 1;
	public static int mTempJumpToIndex = -1;
	private int mCurrentTabIndex;
	private FragmentManager fragmentManager;
	private HomeFragment mHomeFragment;
	private SettingFragment mSettingFragment;
	private MoreFragment mMoreFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initVariables();
		initViews();
		setTabSelection(TabHomeIndex.HOME_INDEX, true);
	}

	private void initVariables() {
		fragmentManager = getSupportFragmentManager();
	}

	private void initViews() {
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mTempJumpToIndex != -1) {
			setTabSelection(mTempJumpToIndex, false);
		}
	}

	/**
	 * 切换到指定的Fragment
	 * 
	 * @param viewId
	 */
	public void setTabSelection(int tabHomeIndex, boolean isFromOncreate) {
		if (mCurrentTabIndex == tabHomeIndex) {
			return;
		}
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 设置切换动画
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);
		switch (tabHomeIndex) {
			case TabHomeIndex.HOME_INDEX:
				if (mHomeFragment == null) {
					// 如果mDiscoverFragment为空，则创建一个并添加到界面上
					mHomeFragment = HomeFragment.newInstance();
					transaction.add(R.id.content, mHomeFragment);
				} else {
					// 如果mDiscoverFragment不为空，则直接将它显示出来
					transaction.show(mHomeFragment);
				}
				setHomeClickedStatus();
				break;
			case TabHomeIndex.SETTING_INDEX:
				if (mSettingFragment == null) {
					// 如果mDiscoverFragment为空，则创建一个并添加到界面上
					mSettingFragment = SettingFragment.newInstance();
					// mMessageFragment = new MessageFragment();
					transaction.add(R.id.content, mSettingFragment);
				} else {
					// 如果mDiscoverFragment不为空，则直接将它显示出来
					transaction.show(mSettingFragment);
				}
				setActivityClickedStatus();
				break;
			case TabHomeIndex.MORE_INDEX:
				if (mMoreFragment == null) {
					// 如果mDiscoverFragment为空，则创建一个并添加到界面上
					mMoreFragment = MoreFragment.newInstance();
					transaction.add(R.id.content, mMoreFragment);
				} else {
					transaction.show(mMoreFragment);
				}
				setMyCenterClickedStatus();
				break;
			default:

				break;
		}
		mCurrentTabIndex = tabHomeIndex;
		if (!isFromOncreate) {
			mTempJumpToIndex = -1;
		}
		// 当activity再次被恢复时commit之后的状态将丢失,所以这里不能用commit
		transaction.commitAllowingStateLoss();
	}

	private void hideFragments(FragmentTransaction transaction) {
		List<Fragment> list = fragmentManager.getFragments();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				transaction.hide(list.get(i));
			}
		}
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

	public class TabHomeIndex {
		public static final int HOME_INDEX = 1; // 首页
		public static final int SETTING_INDEX = 2; // 设置
		public static final int MORE_INDEX = 3; // 更多
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.rl_home_tab_layout:
				setTabSelection(TabHomeIndex.HOME_INDEX, false);
				break;
			case R.id.rl_setting_tab_layout:
				setTabSelection(TabHomeIndex.SETTING_INDEX, false);
				break;
			case R.id.rl_more_tab_layout:
				setTabSelection(TabHomeIndex.MORE_INDEX, false);
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

}
