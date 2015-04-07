package com.zjhbkj.xinfen.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.zjhbkj.xinfen.R;
import com.zjhbkj.xinfen.util.ActionSheetUtil;
import com.zjhbkj.xinfen.util.WheelViewUtil;
import com.zjhbkj.xinfen.widget.ActionSheet.ActionSheetClickListener;
import com.zjhbkj.xinfen.widget.SlipButton;
import com.zjhbkj.xinfen.widget.SlipButton.OnChangedListener;

public class SettingFragment extends FragmentBase implements OnClickListener, OnCheckedChangeListener {

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
	private View mViewHz;
	private View mViewSetStartShut;
	private View mViewSetDate;
	private View mViewSetTime;
	private View mViewSetStartTime;
	private View mViewSetShutTime;
	private View mViewSetPm2dot5;
	private TextView mTvSetStartShut;
	private TextView mTvSetPm2dot5;
	private TextView mTvDate;
	private TextView mTvTime;
	private TextView mTvStartTime;
	private TextView mTvShutTime;
	private int mReportTime; // 上报时间
	private int mHz; // 设置频率
	private int mMode; // 功能模式
	private int mStartShut; // 开关机模式
	private int mFunctionSwitch;
	private int mPm2dot5;
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinu;
	private int mStartHour;
	private int mStartMinu;
	private int mShutHour;
	private int mShutMinu;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case CODE_GET_DATE:
					String result = (String) msg.obj;
					mTvDate.setText(result);
					String[] date = result.split("-");
					if (null != date && 3 == date.length) {
						mYear = Integer.parseInt(date[0]);
						mMonth = Integer.parseInt(date[1]);
						mDay = Integer.parseInt(date[2]);
					}
					break;
				case CODE_GET_TIME:
					String time = (String) msg.obj;
					mTvTime.setText(time);
					String[] tiems = time.split(":");
					if (null != tiems && 2 == tiems.length) {
						mHour = Integer.parseInt(tiems[0]);
						mMinu = Integer.parseInt(tiems[1]);
					}
					break;
				case CODE_GET_START_TIME:
					String startTime = (String) msg.obj;
					mTvStartTime.setText(startTime);
					String[] startTiems = startTime.split(":");
					if (null != startTiems && 2 == startTiems.length) {
						mStartHour = Integer.parseInt(startTiems[0]);
						mStartMinu = Integer.parseInt(startTiems[1]);
					}
					break;
				case CODE_GET_SHUT_TIME:
					String shutTime = (String) msg.obj;
					mTvShutTime.setText(shutTime);
					String[] shutTiems = shutTime.split(":");
					if (null != shutTiems && 2 == shutTiems.length) {
						mShutHour = Integer.parseInt(shutTiems[0]);
						mShutMinu = Integer.parseInt(shutTiems[1]);
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
		mReportTime = 3; // 上报时间为3秒
		mHz = 30;
		mMode = 2;
		mFunctionSwitch = 1;
		mStartShut = 2;
		mPm2dot5 = 100;
		mYear = 2016;
		mMonth = 2;
		mDay = 15;
		mHour = 12;
		mMinu = 15;
		mStartHour = 14;
		mStartMinu = 19;
		mShutHour = 20;
		mShutMinu = 30;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View layout = inflater.inflate(R.layout.activity_setting, container, false);
		initViews(layout);
		setListener();
		return layout;
	}

	private void setListener() {
		mBtnAddHz.setOnClickListener(this);
		mBtnSubHz.setOnClickListener(this);
		mRgMode.setOnCheckedChangeListener(this);
		mViewSetStartShut.setOnClickListener(this);
		mViewSetPm2dot5.setOnClickListener(this);
		mViewSetTime.setOnClickListener(this);
		mViewSetStartTime.setOnClickListener(this);
		mViewSetShutTime.setOnClickListener(this);
		mViewSetDate.setOnClickListener(this);
		mSbtnSetFunction.setOnChangedListener(new OnChangedListener() {

			@Override
			public void onChanged(View view, boolean checkState) {
				if (checkState) {
					mFunctionSwitch = 1;
				} else {
					mFunctionSwitch = 2;
				}
				mSbtnSetFunction.setCheck(1 == mFunctionSwitch);
			}
		});
	}

	private void initViews(View layout) {
		mRgMode = (RadioGroup) layout.findViewById(R.id.rg_mode);
		TextView tvTitle = (TextView) layout.findViewById(R.id.title_with_back_title_btn_mid);
		mTvReportTime = (TextView) layout.findViewById(R.id.tv_report_time);
		mTvHz = (TextView) layout.findViewById(R.id.tv_hz);
		mBtnAddHz = (Button) layout.findViewById(R.id.btn_add_hz);
		mBtnSubHz = (Button) layout.findViewById(R.id.btn_sub_hz);
		mSbtnSetFunction = (SlipButton) layout.findViewById(R.id.sb_set_functional_switch);
		mTvSetPm2dot5 = (TextView) layout.findViewById(R.id.tv_set_pm_2_5);
		mTvDate = (TextView) layout.findViewById(R.id.tv_set_date);
		mViewHz = layout.findViewById(R.id.rl_set_frequency);
		mViewSetStartShut = layout.findViewById(R.id.rl_set_shut_down_start_up);
		mViewSetPm2dot5 = layout.findViewById(R.id.rl_set_pm2_5);
		mViewSetDate = layout.findViewById(R.id.rl_set_date);
		mViewSetStartTime = layout.findViewById(R.id.rl_set_start_up_time);
		mViewSetShutTime = layout.findViewById(R.id.rl_set_shut_down_time);
		mViewSetTime = layout.findViewById(R.id.rl_set_time);
		mTvTime = (TextView) layout.findViewById(R.id.tv_set_time);
		mTvStartTime = (TextView) layout.findViewById(R.id.tv_set_start_time);
		mTvShutTime = (TextView) layout.findViewById(R.id.tv_set_shut_time);
		mTvSetStartShut = (TextView) layout.findViewById(R.id.tv_set_shut_down_start_up);
		tvTitle.setText(R.string.bottom_tab_setting);
		mTvReportTime.setText(String.format("%ds", mReportTime));
		mTvHz.setText("" + mHz);
		mTvSetPm2dot5.setText("" + mPm2dot5);
		mTvDate.setText(mYear + "-" + mMonth + "-" + mDay);
		mTvTime.setText(mHour + ":" + mMinu);
		mTvStartTime.setText(mStartHour + ":" + mStartMinu);
		mTvShutTime.setText(mShutHour + ":" + mShutMinu);
		refreashHzView();
		refreashStartShut(mStartShut);
		mSbtnSetFunction.setCheck(1 == mFunctionSwitch);
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
				WheelViewUtil.showPm2dot5(getActivity(), mPm2dot5 - 50, this);
				break;
			case R.id.rl_set_time:
				WheelViewUtil.showTime(getActivity(), mHour, mMinu, mHandler, CODE_GET_TIME);
				break;
			case R.id.rl_set_start_up_time:
				WheelViewUtil.showTime(getActivity(), mStartHour, mStartMinu, mHandler, CODE_GET_START_TIME);
				break;
			case R.id.rl_set_shut_down_time:
				WheelViewUtil.showTime(getActivity(), mShutHour, mShutMinu, mHandler, CODE_GET_SHUT_TIME);
				break;
			case R.id.rl_set_date:
				WheelViewUtil.showDate(getActivity(), mYear, mMonth, mDay, mHandler);
				break;
			case R.id.btn_ok:
				mPm2dot5 = (Integer) v.getTag();
				mTvSetPm2dot5.setText("" + mPm2dot5);
				break;
			case R.id.rl_set_shut_down_start_up:
				ActionSheetUtil.showActionSheet(getActivity(), new ActionSheetClickListener() {

					@Override
					public void onItemClick(int itemPosition) {
						if (-1 != mStartShut) {
							mStartShut = itemPosition;
						}
						refreashStartShut(itemPosition);
					}
				});
				break;

			default:
				break;
		}
	}

	private void changeHz(boolean isAdd) {
		if (isAdd && 60 == mHz || !isAdd && 0 == mHz) {
			return;
		}
		if (isAdd) {
			mHz++;
		} else {
			mHz--;
		}
		mTvHz.setText("" + mHz);
		// TODO 修改数据库，发送指令
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
			case R.id.rbtn_auto:
				mMode = 1;
				break;
			case R.id.rbtn_manual:
				mMode = 2;
				break;
			case R.id.rbtn_sleep:
				mMode = 3;
				break;

			default:
				break;
		}
		refreashHzView();
	}

	private void refreashHzView() {
		if (1 == mMode) {
			mViewHz.setVisibility(View.GONE);
			mRgMode.check(R.id.rbtn_auto);
		} else if (2 == mMode) {
			mViewHz.setVisibility(View.VISIBLE);
			mRgMode.check(R.id.rbtn_manual);
		} else if (3 == mMode) {
			mViewHz.setVisibility(View.GONE);
			mRgMode.check(R.id.rbtn_sleep);
		}
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
}
