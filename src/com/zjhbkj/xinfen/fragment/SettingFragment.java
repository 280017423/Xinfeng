package com.zjhbkj.xinfen.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
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
import com.zjhbkj.xinfen.util.SharedPreferenceUtil;
import com.zjhbkj.xinfen.util.TimerUtil;
import com.zjhbkj.xinfen.util.TimerUtil.TimerActionListener;
import com.zjhbkj.xinfen.util.WheelViewUtil;
import com.zjhbkj.xinfen.util.WifiApUtil;
import com.zjhbkj.xinfen.widget.ActionSheet.ActionSheetClickListener;
import com.zjhbkj.xinfen.widget.LoadingUpView;

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
	private View mViewSetFunctionalSwitch;
	private View mViewSetStartShut;
	private View mViewSetWifiMode;
	private View mViewSetStartTime;
	private View mViewSetShutTime;
	private View mViewSetPm2dot5;
	private TextView mTvSetFunctionalSwitch;
	private TextView mTvSetStartShut;
	private TextView mTvSetWifiMode;
	private TextView mTvSetPm2dot5;
	private TextView mTvStartTime;
	private TextView mTvShutTime;
	private CheckBox mCbLock;
	private CheckBox mCbTimer;

	private SendComsModel mSendComsModel;
	private LoadingUpView mLoadingUpView;
	private OnTouchListener mOnTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (v.getId()) {
				case R.id.btn_add_hz:
					if (event.getAction() == MotionEvent.ACTION_DOWN) {

						TimerUtil.startTimer(R.id.btn_add_hz + "", 0, 300, new TimerActionListener() {

							@Override
							public void doAction() {
								getActivity().runOnUiThread(new Runnable() {

									@Override
									public void run() {
										changeHz(true);
									}
								});
							}
						});
					} else if (event.getAction() == MotionEvent.ACTION_UP
							|| event.getAction() == MotionEvent.ACTION_CANCEL) {
						TimerUtil.stopTimer(R.id.btn_add_hz + "");
						send(true);
					}
					break;
				case R.id.btn_sub_hz:
					if (event.getAction() == MotionEvent.ACTION_DOWN) {

						TimerUtil.startTimer(R.id.btn_sub_hz + "", 0, 300, new TimerActionListener() {

							@Override
							public void doAction() {
								getActivity().runOnUiThread(new Runnable() {

									@Override
									public void run() {
										changeHz(false);
									}
								});
							}
						});
					} else if (event.getAction() == MotionEvent.ACTION_UP
							|| event.getAction() == MotionEvent.ACTION_CANCEL) {
						TimerUtil.stopTimer(R.id.btn_sub_hz + "");
						send(true);
					}
					break;
				default:
					break;
			}
			return false;
		}
	};
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
			send(true);
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
						send(true);
					}
					break;
				case CODE_GET_SHUT_TIME:
					String shutTime = (String) msg.obj;
					mTvShutTime.setText(shutTime);
					String[] shutTiems = shutTime.split(":");
					if (null != shutTiems && 2 == shutTiems.length) {
						mSendComsModel.setCommand12(Integer.toHexString(Integer.parseInt(shutTiems[1])));
						mSendComsModel.setCommand13(Integer.toHexString(Integer.parseInt(shutTiems[0])));
						send(true);
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
			String deviceName = SharedPreferenceUtil.getStringValueByKey(XinfengApplication.CONTEXT,
					Global.CONFIG_FILE_NAME, Global.CURRENT_DEVICE_ID);
			Integer command15 = SharedPreferenceUtil.getIntegerValueByKey(XinfengApplication.CONTEXT,
					Global.GLOBAL_FILE_NAME, deviceName);
			if (-1 == command15) {
				command15 = 100;
			}

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
			send(false);
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
		mTvSetPm2dot5 = (TextView) layout.findViewById(R.id.tv_set_pm_2_5);
		mViewSetFunctionalSwitch = layout.findViewById(R.id.rl_set_functional_switch);
		mViewSetStartShut = layout.findViewById(R.id.rl_set_shut_down_start_up);
		mViewSetWifiMode = layout.findViewById(R.id.rl_set_wifi_mode);
		mViewSetPm2dot5 = layout.findViewById(R.id.rl_set_pm2_5);
		mViewSetStartTime = layout.findViewById(R.id.rl_set_start_up_time);
		mViewSetShutTime = layout.findViewById(R.id.rl_set_shut_down_time);
		mTvStartTime = (TextView) layout.findViewById(R.id.tv_set_start_time);
		mTvShutTime = (TextView) layout.findViewById(R.id.tv_set_shut_time);
		mTvSetFunctionalSwitch = (TextView) layout.findViewById(R.id.tv_set_functional_switch);
		mTvSetStartShut = (TextView) layout.findViewById(R.id.tv_set_shut_down_start_up);
		mTvSetWifiMode = (TextView) layout.findViewById(R.id.tv_set_wifi_mode);
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
		mTvSetFunctionalSwitch.setText(1 == functionValue % 10 ? "开" : "关");
		mCbTimer.setChecked(SharedPreferenceUtil.getBooleanValueByKey(XinfengApplication.CONTEXT,
				Global.CONFIG_FILE_NAME, Global.IS_TIMER_OPENED));
	}

	private void setListener() {
		mBtnAddHz.setOnTouchListener(mOnTouchListener);
		mBtnSubHz.setOnTouchListener(mOnTouchListener);
		mRgMode.setOnCheckedChangeListener(this);
		mViewSetStartShut.setOnClickListener(this);
		mViewSetWifiMode.setOnClickListener(this);
		mViewSetPm2dot5.setOnClickListener(this);
		mViewSetStartTime.setOnClickListener(this);
		mViewSetShutTime.setOnClickListener(this);
		mViewSetFunctionalSwitch.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.rl_set_pm2_5:
				WheelViewUtil.showPm2dot5(getActivity(),
						CommandUtil.hexStringToInt(mSendComsModel.getCommand15()) - 50, this);
				break;
			case R.id.rl_set_start_up_time:
				WheelViewUtil.showTime(getActivity(), CommandUtil.hexStringToInt(mSendComsModel.getCommand11()),
						CommandUtil.hexStringToInt(mSendComsModel.getCommand10()), mHandler, CODE_GET_START_TIME,
						"开机时间");
				break;
			case R.id.rl_set_shut_down_time:
				WheelViewUtil
						.showTime(getActivity(), CommandUtil.hexStringToInt(mSendComsModel.getCommand13()),
								CommandUtil.hexStringToInt(mSendComsModel.getCommand12()), mHandler,
								CODE_GET_SHUT_TIME, "关机时间");
				break;
			case R.id.btn_ok:
				mSendComsModel.setCommand15(Integer.toHexString((Integer) v.getTag()));
				String deviceName = SharedPreferenceUtil.getStringValueByKey(XinfengApplication.CONTEXT,
						Global.CONFIG_FILE_NAME, Global.CURRENT_DEVICE_ID);
				SharedPreferenceUtil.saveValue(XinfengApplication.CONTEXT, Global.GLOBAL_FILE_NAME, deviceName,
						(Integer) v.getTag());
				mTvSetPm2dot5.setText("" + (Integer) v.getTag());
				send(true);
				break;
			case R.id.rl_set_wifi_mode:
				ActionSheetUtil.showWifiModeActionSheet(getActivity(), new ActionSheetClickListener() {

					@Override
					public void onItemClick(int itemPosition) {
						if (-1 == itemPosition) {
							return;
						}
						String oldValue = mSendComsModel.getCommand14();
						String newValue = Integer.toHexString(itemPosition + 2);
						if (newValue.equalsIgnoreCase(oldValue)) {
							return;
						}
						refreashStartShut(itemPosition + 2);
						mSendComsModel.setCommand14(newValue);
						send(true);
						if (1 == itemPosition) {
							if (null != mLoadingUpView && !mLoadingUpView.isShowing()) {
								mLoadingUpView.showPopup("正在切换至外网");
							}
							TimerUtil.startTimer(TAG, 3 * 3, 1000, new TimerActionListener() {

								@Override
								public void doAction() {
									if (0 == TimerUtil.getTimerTime(TAG)) {
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
						} else if (0 == itemPosition) {
							if (null != mLoadingUpView && !mLoadingUpView.isShowing()) {
								mLoadingUpView.showPopup("正在切换至内网");
							}
							mSendComsModel.setCommand14(newValue);
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
				});
				break;
			case R.id.rl_set_shut_down_start_up:
				ActionSheetUtil.showShutStartActionSheet(getActivity(), new ActionSheetClickListener() {

					@Override
					public void onItemClick(int itemPosition) {
						if (-1 == itemPosition) {
							return;
						}
						String oldValue = mSendComsModel.getCommand14();
						String newValue = Integer.toHexString(itemPosition);
						if (newValue.equalsIgnoreCase(oldValue)) {
							return;
						}
						refreashStartShut(itemPosition);
						mSendComsModel.setCommand14(newValue);
						send(true);
					}
				});
				break;
			case R.id.rl_set_functional_switch:
				ActionSheetUtil.showFunctionalSwitchActionSheet(getActivity(), new ActionSheetClickListener() {

					@Override
					public void onItemClick(int itemPosition) {
						if (-1 == itemPosition) {
							return;
						}
						String oldValue = mSendComsModel.getCommand14();
						String newValue = Integer.toHexString(itemPosition);
						if (newValue.equalsIgnoreCase(oldValue)) {
							return;
						}
						if (0 == itemPosition) {
							mSendComsModel.setCommand4(Integer.toHexString(1));
						} else {
							mSendComsModel.setCommand4(Integer.toHexString(2));
						}
						send(true);
						int functionValue = CommandUtil.hexStringToInt(mSendComsModel.getCommand4());
						mTvSetFunctionalSwitch.setText(1 == functionValue % 10 ? "开" : "关");
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
		send(true);
	}

	public void onEventMainThread(RcvComsModel model) {
		mSendComsModel.setCommand2(model.getCommand2());
		mSendComsModel.setCommand3(model.getCommand3());
		mSendComsModel.setCommand4(model.getCommand4());
		int functionValue = CommandUtil.hexStringToInt(mSendComsModel.getCommand4());
		mTvSetFunctionalSwitch.setText(1 == functionValue % 10 ? "开" : "关");
		mTvHz.setText("" + CommandUtil.hexStringToInt(mSendComsModel.getCommand2()));
		int mode = CommandUtil.hexStringToInt(mSendComsModel.getCommand4());
		boolean isTimer = mode / 10 > 0;
		refreashHzView();
		SharedPreferenceUtil.saveValue(XinfengApplication.CONTEXT, Global.CONFIG_FILE_NAME, Global.IS_TIMER_OPENED,
				isTimer);
		mCbTimer.setChecked(isTimer);
		send(false);
	}

	private void refreashHzView() {
		int mode = CommandUtil.hexStringToInt(mSendComsModel.getCommand3());
		Log.d("ccc", "发送模式:" + mode);
		if (1 == mode % 10) {
			mBtnAddHz.setEnabled(false);
			mBtnSubHz.setEnabled(false);
			mRgMode.check(R.id.rbtn_auto);
		} else if (2 == mode % 10) {
			mBtnAddHz.setEnabled(true);
			mBtnSubHz.setEnabled(true);
			mRgMode.check(R.id.rbtn_manual);
		} else if (3 == mode % 10) {
			mBtnAddHz.setEnabled(false);
			mBtnSubHz.setEnabled(false);
			mRgMode.check(R.id.rbtn_sleep);
		}
		boolean isLock = mode / 10 > 0;
		SharedPreferenceUtil.saveValue(XinfengApplication.CONTEXT, Global.CONFIG_FILE_NAME, Global.IS_LOCK_OPENED,
				isLock);
		mCbLock.setChecked(isLock);
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
				mTvSetWifiMode.setText("内网");
				break;
			case 3:
				mTvSetWifiMode.setText("外网");
				break;

			default:
				break;
		}
	}

	private void send(boolean isDelay) {
		mSendComsModel.send(isDelay);
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
