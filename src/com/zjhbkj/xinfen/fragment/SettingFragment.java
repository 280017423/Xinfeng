package com.zjhbkj.xinfen.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.zjhbkj.xinfen.R;
import com.zjhbkj.xinfen.app.XinfengApplication;
import com.zjhbkj.xinfen.commom.Global;
import com.zjhbkj.xinfen.db.DBMgr;
import com.zjhbkj.xinfen.listener.StartWifiApListener;
import com.zjhbkj.xinfen.model.RcvComsModel;
import com.zjhbkj.xinfen.model.SendComsModel;
import com.zjhbkj.xinfen.util.ActionSheetUtil;
import com.zjhbkj.xinfen.util.CommandUtil;
import com.zjhbkj.xinfen.util.EvtLog;
import com.zjhbkj.xinfen.util.SharedPreferenceUtil;
import com.zjhbkj.xinfen.util.TimerUtil;
import com.zjhbkj.xinfen.util.TimerUtil.TimerActionListener;
import com.zjhbkj.xinfen.util.WheelViewUtil;
import com.zjhbkj.xinfen.util.WifiApUtil;
import com.zjhbkj.xinfen.widget.ActionSheet.ActionSheetClickListener;
import com.zjhbkj.xinfen.widget.LoadingUpView;
import com.zjhbkj.xinfen.widget.SlipButton;
import com.zjhbkj.xinfen.widget.SlipButton.OnChangedListener;

import de.greenrobot.event.EventBus;

public class SettingFragment extends FragmentBase implements OnClickListener, OnCheckedChangeListener {

	public static final String TAG = SettingFragment.class.getName();
	public static final int CODE_GET_DATE = 100;
	public static final int CODE_GET_TIME = 101;
	public static final int CODE_GET_START_TIME = 102;
	public static final int CODE_GET_SHUT_TIME = 103;
	private TextView mTvReportTime;
	private TextView mTvHz;
	private Button mBtnAddHz;
	private Button mBtnSubHz;
	private RadioGroup mRgMode;
	private SlipButton mSbtnSetFunction;
	private View mViewSetStartShut;
	private View mViewSetStartTime;
	private View mViewSetShutTime;
	private View mViewSetPm2dot5;
	private TextView mTvSetStartShut;
	private TextView mTvSetPm2dot5;
	private TextView mTvStartTime;
	private TextView mTvShutTime;
	private CheckBox mCbLock;
	private CheckBox mCbTimer;

	private SendComsModel mSendComsModel;
	private LoadingUpView mLoadingUpView;
	private CompoundButton.OnCheckedChangeListener mLockListener = new CompoundButton.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (R.id.cb_lock == buttonView.getId()) {
				SharedPreferenceUtil.saveValue(XinfengApplication.CONTEXT, Global.CONFIG_FILE_NAME,
						Global.IS_LOCK_OPENED, isChecked);
			} else if (R.id.cb_timer == buttonView.getId()) {
				SharedPreferenceUtil.saveValue(XinfengApplication.CONTEXT, Global.CONFIG_FILE_NAME,
						Global.IS_TIMER_OPENED, isChecked);
			}
			send();
		}
	};
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case CODE_GET_START_TIME:
					String startTime = (String) msg.obj;
					mTvStartTime.setText(startTime);
					String[] startTiems = startTime.split(":");
					if (null != startTiems && 2 == startTiems.length) {
						mSendComsModel.setCommand10(Integer.toHexString(Integer.parseInt(startTiems[1])));
						mSendComsModel.setCommand11(Integer.toHexString(Integer.parseInt(startTiems[0])));
						send();
					}
					break;
				case CODE_GET_SHUT_TIME:
					String shutTime = (String) msg.obj;
					mTvShutTime.setText(shutTime);
					String[] shutTiems = shutTime.split(":");
					if (null != shutTiems && 2 == shutTiems.length) {
						mSendComsModel.setCommand12(Integer.toHexString(Integer.parseInt(shutTiems[1])));
						mSendComsModel.setCommand13(Integer.toHexString(Integer.parseInt(shutTiems[0])));
						send();
					}
					break;
				default:
					break;
			}
			super.handleMessage(msg);
		}
	};

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
		EventBus.getDefault().register(this);
		mLoadingUpView = new LoadingUpView(getActivity(), true);
		// 第一次进来拿到设备发过来的初始数据，初始化设置界面
		mSendComsModel = DBMgr.getHistoryData(SendComsModel.class, "EA");
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
			send();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View layout = inflater.inflate(R.layout.activity_setting, container, false);
		initViews(layout);
		setListener();
		return layout;
	}

	private void initViews(View layout) {
		TextView tvTitle = (TextView) layout.findViewById(R.id.title_with_back_title_btn_mid);
		tvTitle.setText(R.string.bottom_tab_setting);
		mRgMode = (RadioGroup) layout.findViewById(R.id.rg_mode);
		mTvReportTime = (TextView) layout.findViewById(R.id.tv_report_time);
		mTvHz = (TextView) layout.findViewById(R.id.tv_hz);
		mBtnAddHz = (Button) layout.findViewById(R.id.btn_add_hz);
		mBtnSubHz = (Button) layout.findViewById(R.id.btn_sub_hz);
		mSbtnSetFunction = (SlipButton) layout.findViewById(R.id.sb_set_functional_switch);
		mTvSetPm2dot5 = (TextView) layout.findViewById(R.id.tv_set_pm_2_5);
		mViewSetStartShut = layout.findViewById(R.id.rl_set_shut_down_start_up);
		mViewSetPm2dot5 = layout.findViewById(R.id.rl_set_pm2_5);
		mViewSetStartTime = layout.findViewById(R.id.rl_set_start_up_time);
		mViewSetShutTime = layout.findViewById(R.id.rl_set_shut_down_time);
		mTvStartTime = (TextView) layout.findViewById(R.id.tv_set_start_time);
		mTvShutTime = (TextView) layout.findViewById(R.id.tv_set_shut_time);
		mTvSetStartShut = (TextView) layout.findViewById(R.id.tv_set_shut_down_start_up);
		mCbLock = (CheckBox) layout.findViewById(R.id.cb_lock);
		mCbTimer = (CheckBox) layout.findViewById(R.id.cb_timer);
		mCbLock.setOnCheckedChangeListener(mLockListener);
		mCbTimer.setOnCheckedChangeListener(mLockListener);
		refreashUi();
	}

	private void refreashUi() {
		mTvReportTime.setText(String.format("%ds", CommandUtil.hexStringToInt(mSendComsModel.getCommand1())));
		mTvHz.setText("" + CommandUtil.hexStringToInt(mSendComsModel.getCommand2()));
		mTvSetPm2dot5.setText("" + CommandUtil.hexStringToInt(mSendComsModel.getCommand15()));
		mTvStartTime.setText(CommandUtil.hexStringToInt(mSendComsModel.getCommand11()) + ":"
				+ CommandUtil.hexStringToInt(mSendComsModel.getCommand10()));
		mTvShutTime.setText(CommandUtil.hexStringToInt(mSendComsModel.getCommand13()) + ":"
				+ CommandUtil.hexStringToInt(mSendComsModel.getCommand12()));
		refreashHzView();
		refreashStartShut(CommandUtil.hexStringToInt(mSendComsModel.getCommand14()));
		int functionValue = CommandUtil.hexStringToInt(mSendComsModel.getCommand4());
		mSbtnSetFunction.setCheck(1 == functionValue % 10);
		mCbTimer.setChecked(SharedPreferenceUtil.getBooleanValueByKey(XinfengApplication.CONTEXT,
				Global.CONFIG_FILE_NAME, Global.IS_TIMER_OPENED));
	}

	private void setListener() {
		mBtnAddHz.setOnClickListener(this);
		mBtnSubHz.setOnClickListener(this);
		mRgMode.setOnCheckedChangeListener(this);
		mViewSetStartShut.setOnClickListener(this);
		mViewSetPm2dot5.setOnClickListener(this);
		mViewSetStartTime.setOnClickListener(this);
		mViewSetShutTime.setOnClickListener(this);
		mSbtnSetFunction.setOnChangedListener(new OnChangedListener() {

			@Override
			public void onChanged(View view, boolean checkState) {
				if (checkState) {
					mSendComsModel.setCommand4(Integer.toHexString(1));
				} else {
					mSendComsModel.setCommand4(Integer.toHexString(2));
				}
				send();
				mSbtnSetFunction.setCheck(1 == CommandUtil.hexStringToInt(mSendComsModel.getCommand4()));
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_add_hz:
				changeHz(true);
				break;
			case R.id.btn_sub_hz:
				changeHz(false);
				break;
			case R.id.rl_set_pm2_5:
				WheelViewUtil.showPm2dot5(getActivity(),
						CommandUtil.hexStringToInt(mSendComsModel.getCommand15()) - 50, this);
				break;
			case R.id.rl_set_start_up_time:
				WheelViewUtil.showTime(getActivity(), CommandUtil.hexStringToInt(mSendComsModel.getCommand11()),
						CommandUtil.hexStringToInt(mSendComsModel.getCommand10()), mHandler, CODE_GET_START_TIME);
				break;
			case R.id.rl_set_shut_down_time:
				WheelViewUtil.showTime(getActivity(), CommandUtil.hexStringToInt(mSendComsModel.getCommand13()),
						CommandUtil.hexStringToInt(mSendComsModel.getCommand12()), mHandler, CODE_GET_SHUT_TIME);
				break;
			case R.id.btn_ok:
				mSendComsModel.setCommand15(Integer.toHexString((Integer) v.getTag()));
				mTvSetPm2dot5.setText("" + (Integer) v.getTag());
				send();
				break;
			case R.id.rl_set_shut_down_start_up:
				ActionSheetUtil.showActionSheet(getActivity(), new ActionSheetClickListener() {

					@Override
					public void onItemClick(int itemPosition) {
						if (-1 != itemPosition) {
							String oldValue = mSendComsModel.getCommand14();
							String newValue = Integer.toHexString(itemPosition);
							if (newValue.equalsIgnoreCase(oldValue)) {
								return;
							}
							refreashStartShut(itemPosition);
							mSendComsModel.setCommand14(newValue);
							send();
							if (3 == itemPosition) {
								if (null != mLoadingUpView && !mLoadingUpView.isShowing()) {
									mLoadingUpView.showPopup("正在切换至外网");
								}
								TimerUtil.startTimer(TAG, 3 * 3, 1000, new TimerActionListener() {

									@Override
									public void doAction() {
										if (0 == TimerUtil.getTimerTime(TAG)) {
											EvtLog.d("aaa", "toggleWiFi, true");
											// 关闭热点，打开wifi；
											WifiApUtil.closeWifiAp(getActivity());
											WifiApUtil.toggleWiFi(getActivity(), true);
											TimerUtil.stopTimer(TAG);
											SharedPreferenceUtil.saveValue(XinfengApplication.CONTEXT,
													Global.CONFIG_FILE_NAME, Global.IS_WIFI_MODE, 1);
											if (null != mLoadingUpView && mLoadingUpView.isShowing()) {
												mLoadingUpView.dismiss();
											}
										}
									}
								});
							} else if (2 == itemPosition && oldValue.equalsIgnoreCase(Integer.toHexString(3))) {
								if (null != mLoadingUpView && !mLoadingUpView.isShowing()) {
									mLoadingUpView.showPopup("正在切换至内网");
								}
								mSendComsModel.setCommand14(Integer.toHexString(itemPosition));
								mSendComsModel.send(true);
								TimerUtil.startTimer(TAG, 3 * 3, 1000, new TimerActionListener() {

									@Override
									public void doAction() {
										if (0 == TimerUtil.getTimerTime(TAG)) {
											WifiApUtil.toggleWiFi(getActivity(), false);
											WifiApUtil wifiAp = new WifiApUtil(getActivity());
											wifiAp.startWifiAp(Global.SSID, Global.PASSWORD, new StartWifiApListener() {

												@Override
												public void enableWifiApSuccess() {
													getActivity().runOnUiThread(new Runnable() {

														@Override
														public void run() {
															getActivity().runOnUiThread(new Runnable() {

																@Override
																public void run() {
																	if (null != mLoadingUpView
																			&& mLoadingUpView.isShowing()) {
																		mLoadingUpView.dismiss();
																	}
																}
															});
															SharedPreferenceUtil.saveValue(XinfengApplication.CONTEXT,
																	Global.CONFIG_FILE_NAME, Global.IS_WIFI_MODE, 0);
															toast("已经切换至内网");
														}
													});
												}

												@Override
												public void enableWifiApFail() {
													getActivity().runOnUiThread(new Runnable() {

														@Override
														public void run() {
															if (null != mLoadingUpView && mLoadingUpView.isShowing()) {
																mLoadingUpView.dismiss();
															}
														}
													});
												}
											});
											TimerUtil.stopTimer(TAG);
											if (null != mLoadingUpView && mLoadingUpView.isShowing()) {
												mLoadingUpView.dismiss();
											}
										}
									}
								});
							}
						}
					}
				});
				break;
			default:
				break;
		}
	}

	private void changeHz(boolean isAdd) {
		int mHz = CommandUtil.hexStringToInt(mSendComsModel.getCommand2());
		if (isAdd && 60 == mHz || !isAdd && 10 == mHz) {
			return;
		}
		if (isAdd) {
			mHz++;
		} else {
			mHz--;
		}
		mTvHz.setText("" + mHz);
		mSendComsModel.setCommand2(Integer.toHexString(mHz));
		send();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
			case R.id.rbtn_auto:
				mSendComsModel.setCommand3(Integer.toHexString(1));
				break;
			case R.id.rbtn_manual:
				mSendComsModel.setCommand3(Integer.toHexString(2));
				break;
			case R.id.rbtn_sleep:
				mSendComsModel.setCommand3(Integer.toHexString(3));
				break;

			default:
				break;
		}
		refreashHzView();
		send();
	}

	public void onEventMainThread(RcvComsModel model) {
		int count = SharedPreferenceUtil.getIntegerValueByKey(XinfengApplication.CONTEXT, Global.CONFIG_FILE_NAME,
				Global.HAS_SETTING_INFO);
		if (count <= 0) {
			mSendComsModel.setCommand3(model.getCommand3());
			send();
			refreashHzView();
		}
	}

	private void refreashHzView() {
		int mode = CommandUtil.hexStringToInt(mSendComsModel.getCommand3());
		if (1 == mode % 10) {
			mBtnAddHz.setEnabled(false);
			mBtnSubHz.setEnabled(false);
			mRgMode.check(R.id.rbtn_auto);
		} else if (2 == mode) {
			mBtnAddHz.setEnabled(true);
			mBtnSubHz.setEnabled(true);
			mRgMode.check(R.id.rbtn_manual);
		} else if (3 == mode) {
			mBtnAddHz.setEnabled(false);
			mBtnSubHz.setEnabled(false);
			mRgMode.check(R.id.rbtn_sleep);
		}
		mCbLock.setChecked(SharedPreferenceUtil.getBooleanValueByKey(XinfengApplication.CONTEXT,
				Global.CONFIG_FILE_NAME, Global.IS_LOCK_OPENED));
	}

	private void refreashStartShut(int value) {
		switch (value) {
			case 0:
				mTvSetStartShut.setText("开机");
				break;
			case 1:
				mTvSetStartShut.setText("关机");
				break;
			case 2:
				mTvSetStartShut.setText("内网");
				break;
			case 3:
				mTvSetStartShut.setText("外网");
				break;

			default:
				break;
		}
	}

	private void send() {
		mSendComsModel.send();
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
