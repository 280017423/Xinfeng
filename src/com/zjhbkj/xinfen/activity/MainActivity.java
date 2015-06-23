package com.zjhbkj.xinfen.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Dialog;
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
import android.widget.Toast;

import com.zjhbkj.xinfen.R;
import com.zjhbkj.xinfen.adapter.MainTabAdapter;
import com.zjhbkj.xinfen.app.XinfengApplication;
import com.zjhbkj.xinfen.commom.Global;
import com.zjhbkj.xinfen.db.DBMgr;
import com.zjhbkj.xinfen.fragment.HomeFragment;
import com.zjhbkj.xinfen.fragment.MoreFragment;
import com.zjhbkj.xinfen.fragment.SettingFragment;
import com.zjhbkj.xinfen.listener.NetWorkChangeBroadcastReceiver;
import com.zjhbkj.xinfen.model.RcvComsModel;
import com.zjhbkj.xinfen.model.SendComsModel;
import com.zjhbkj.xinfen.udp.UDPClient;
import com.zjhbkj.xinfen.udp.UDPClient.ClientMsgListener;
import com.zjhbkj.xinfen.udp.UDPServer;
import com.zjhbkj.xinfen.util.EvtLog;
import com.zjhbkj.xinfen.util.SharedPreferenceUtil;
import com.zjhbkj.xinfen.util.TimerUtil;
import com.zjhbkj.xinfen.util.WifiApUtil;

public class MainActivity extends BaseFragmentActivity implements OnClickListener {
	public static final String TAG = MainActivity.class.getName();
	private UDPServer mUdpServer;
	private UDPClient mUdpClient;
	private FragmentManager fragmentManager;
	private HomeFragment mHomeFragment;
	private SettingFragment mSettingFragment;
	private MoreFragment mMoreFragment;
	private ViewPager mViewPager;
	private MainTabAdapter mAdapter;
	private List<Fragment> mFragments;
	private long mExitTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initVariables();
		initViews();
	}

	private void initVariables() {
		SharedPreferenceUtil.saveValue(XinfengApplication.CONTEXT, Global.GLOBAL_FILE_NAME, Global.NO_RECEIVE_MSG,
				false);
		// 默认标记为内网
		SharedPreferenceUtil.saveValue(XinfengApplication.CONTEXT, Global.CONFIG_FILE_NAME, Global.IS_WIFI_MODE, 0);
		SharedPreferenceUtil.saveValue(XinfengApplication.CONTEXT, Global.CONFIG_FILE_NAME, Global.IS_FIRST_TIME, 2);
		initSendData();
		initUdp();
		initFragment();
	}

	private void initFragment() {
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

	private void initUdp() {
		ExecutorService exec = Executors.newCachedThreadPool();
		mUdpServer = new UDPServer();
		exec.execute(mUdpServer);

		mUdpClient = UDPClient.newInstance(new ClientMsgListener() {

			@Override
			public void handlerErorMsg(String errorMsg) {
				EvtLog.d("aaa", errorMsg);
			}
		});
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_LONG).show();
				mExitTime = System.currentTimeMillis();
			} else {
				WifiApUtil.closeWifiAp(this);
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initSendData() {
		// 第一次进来拿到设备发过来的初始数据，初始化设置界面
		SendComsModel mSendComsModel = DBMgr.getHistoryData(SendComsModel.class, "EA");
		String deviceName = SharedPreferenceUtil.getStringValueByKey(XinfengApplication.CONTEXT,
				Global.CONFIG_FILE_NAME, Global.CURRENT_DEVICE_ID);
		Integer command15 = SharedPreferenceUtil.getIntegerValueByKey(XinfengApplication.CONTEXT,
				Global.GLOBAL_FILE_NAME, deviceName);
		if (-1 == command15) {
			command15 = 100;
		}

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
			mSendComsModel.setCommand15(Integer.toHexString(command15));
			mSendComsModel.send(false);
		}
	}

	public void showMyDialog() {
		int hasToast = SharedPreferenceUtil.getIntegerValueByKey(XinfengApplication.CONTEXT, Global.CONFIG_FILE_NAME,
				Global.HAS_TOAST_OUT_OF_DATE);
		if (1 == hasToast) {
			return;
		}
		SharedPreferenceUtil.saveValue(XinfengApplication.CONTEXT, Global.CONFIG_FILE_NAME,
				Global.HAS_TOAST_OUT_OF_DATE, 1);
		Dialog dialog = createDialogBuilder(this, "提示", "滤芯过期，请更换", "确定", "").create(0);
		dialog.show();
	}

	@Override
	protected void onDestroy() {
		TimerUtil.stopTimer(NetWorkChangeBroadcastReceiver.class.getName());
		if (null != mUdpServer) {
			EvtLog.d("aaa", "断开udp");
			mUdpServer.stopAcceptMessage();
			mUdpServer.closeConnection();
		}
		if (null != mUdpClient) {
			EvtLog.d("aaa", "断开udp");
			mUdpClient.stopAcceptMessage();
			mUdpClient.closeConnection();
		}
		super.onDestroy();
	}

}
