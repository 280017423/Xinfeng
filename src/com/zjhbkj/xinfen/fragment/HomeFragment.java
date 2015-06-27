package com.zjhbkj.xinfen.fragment;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.zjhbkj.xinfen.R;
import com.zjhbkj.xinfen.activity.MainActivity;
import com.zjhbkj.xinfen.activity.WebActivity;
import com.zjhbkj.xinfen.app.XinfengApplication;
import com.zjhbkj.xinfen.commom.Global;
import com.zjhbkj.xinfen.db.DBMgr;
import com.zjhbkj.xinfen.model.RcvComsModel;
import com.zjhbkj.xinfen.util.CommandUtil;
import com.zjhbkj.xinfen.util.NetUtil;
import com.zjhbkj.xinfen.util.SharedPreferenceUtil;
import com.zjhbkj.xinfen.util.TimerUtil;
import com.zjhbkj.xinfen.util.TimerUtil.TimerActionListener;
import com.zjhbkj.xinfen.util.UIUtil;
import com.zjhbkj.xinfen.widget.ProgressView;

import de.greenrobot.event.EventBus;

public class HomeFragment extends FragmentBase implements OnClickListener {
	public static final String TAG = MainActivity.class.getName();
	private TextView mTvFrequency;
	private TextView mTvMode;
	private TextView mTvModeSwitch;
	private TextView mTvInInTemp;
	private TextView mTvInOutTemp;
	private TextView mTvOutInTemp;
	private TextView mTvOutOutTemp;
	private TextView mTvHumidity;
	private View mViewHome;
	private TextView mTvRight;
	private TextView mTvOffLineMode;
	private RcvComsModel mCurrentModel;
	private ImageView mIvChuxiao;
	private ImageView mIvchuchen;
	private ImageView mIvGaoxiao;

	private Button mPgvPm2dot5In;
	private Button mPgvPm2dot5Out;
	private Button mPgvCo2;
	private Button mPgvJiaquan;
	private String mDeviceName;
	private ImageView mIvFans;

	public static final HomeFragment newInstance() {
		HomeFragment fragment = new HomeFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		initVariables();
		super.onCreate(savedInstanceState);
	}

	private void initVariables() {
		mDeviceName = SharedPreferenceUtil.getStringValueByKey(XinfengApplication.CONTEXT, Global.CONFIG_FILE_NAME,
				Global.CURRENT_DEVICE_ID);
		initOffLineTimer();
		EventBus.getDefault().register(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View layout = inflater.inflate(R.layout.activity_home, container, false);
		initViews(layout);
		setListener(layout);
		return layout;
	}

	private void setListener(View layout) {
		mPgvPm2dot5In.setOnClickListener(this);
		mPgvPm2dot5Out.setOnClickListener(this);
		mPgvCo2.setOnClickListener(this);
		mPgvJiaquan.setOnClickListener(this);
		mTvInInTemp.setOnClickListener(this);
		mTvInOutTemp.setOnClickListener(this);
		mTvOutInTemp.setOnClickListener(this);
		mTvOutOutTemp.setOnClickListener(this);
		mTvHumidity.setOnClickListener(this);
	}

	private void initViews(View layout) {
		TextView tvTitle = (TextView) layout.findViewById(R.id.title_with_back_title_btn_mid);
		tvTitle.setText(R.string.title_home);
		mTvRight = (TextView) layout.findViewById(R.id.tv_title_with_right);
		mTvRight.setBackgroundResource(R.drawable.tongyong_button_bg_shape);
		mTvRight.setText("分享");
		mTvRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (null == mCurrentModel) {
					return;
				}
				String shareInfo = getString(R.string.share_info, mCurrentModel.getDisplayPm2dotOut() + " ppm",
						mCurrentModel.getDisplayPm2dotIn() + " ppm",
						"" + CommandUtil.hexStringToInt(mCurrentModel.getCommand13()) + Html.fromHtml("&#8451;"), ""
								+ CommandUtil.hexStringToInt(mCurrentModel.getCommand11()) + Html.fromHtml("&#8451;"),
						"" + CommandUtil.hexStringToInt(mCurrentModel.getCommand12()) + Html.fromHtml("&#8451;"), ""
								+ CommandUtil.hexStringToInt(mCurrentModel.getCommand14()) + Html.fromHtml("&#8451;"),
						CommandUtil.hexStringToInt(mCurrentModel.getCommand2()) / 100.0 + " ppm",
						mCurrentModel.getDisplayCo2() + " ppm");
				showShare(shareInfo);
			}
		});

		mViewHome = layout.findViewById(R.id.rl_home_tab_layout);
		int height = UIUtil.getScreenWidth(getActivity()) * 16 / 25;
		int width = UIUtil.getScreenWidth(getActivity()) * 4 / 5;
		if (isLandscape()) {
			height = UIUtil.getScreenHeight(getActivity()) * 16 / 25 * 3 / 4;
			width = UIUtil.getScreenHeight(getActivity()) * 4 / 5 * 3 / 4;
		}
		UIUtil.setViewWidth(mViewHome, width);
		UIUtil.setViewHeight(mViewHome, height);

		mIvFans = (ImageView) layout.findViewById(R.id.iv_fans);
		mTvFrequency = (TextView) layout.findViewById(R.id.tv_frequency);
		mTvOffLineMode = (TextView) layout.findViewById(R.id.tv_offline_mode);
		mTvMode = (TextView) layout.findViewById(R.id.tv_mode);
		mTvModeSwitch = (TextView) layout.findViewById(R.id.tv_mode_switch);

		mTvInInTemp = (TextView) layout.findViewById(R.id.tv_in_in_wind_temp);
		mTvInOutTemp = (TextView) layout.findViewById(R.id.tv_in_out_wind_temp);
		mTvOutInTemp = (TextView) layout.findViewById(R.id.tv_out_in_wind_temp);
		mTvOutOutTemp = (TextView) layout.findViewById(R.id.tv_out_out_wind_temp);

		mTvHumidity = (TextView) layout.findViewById(R.id.tv_humidity);

		mIvChuxiao = (ImageView) layout.findViewById(R.id.iv_chuxiao);
		mIvchuchen = (ImageView) layout.findViewById(R.id.iv_chuchen);
		mIvGaoxiao = (ImageView) layout.findViewById(R.id.iv_gaoxiao);

		mPgvJiaquan = (Button) layout.findViewById(R.id.pgv_jiaquan);
		mPgvCo2 = (Button) layout.findViewById(R.id.pgv_co2);
		mPgvPm2dot5In = (Button) layout.findViewById(R.id.pgv_pm_2dot5_in);
		mPgvPm2dot5Out = (Button) layout.findViewById(R.id.pgv_pm_2dot5_out);

		int pgvWidth = UIUtil.getScreenWidth(getActivity()) / 4;
		int pgvHeight = UIUtil.getScreenWidth(getActivity()) * 2 / 16;
		if (isLandscape()) {
			pgvWidth = (UIUtil.getScreenHeight(getActivity()) / 4) * 3 / 4;
			pgvHeight = (UIUtil.getScreenHeight(getActivity()) * 2 / 16) * 3 / 4;
		} else {
			UIUtil.setViewWidth(mPgvJiaquan, pgvWidth);
			UIUtil.setViewHeight(mPgvJiaquan, pgvWidth);
			UIUtil.setViewWidth(mPgvCo2, pgvWidth);
			UIUtil.setViewHeight(mPgvCo2, pgvWidth);
			UIUtil.setViewWidth(mPgvPm2dot5In, pgvWidth);
			UIUtil.setViewHeight(mPgvPm2dot5In, pgvWidth);
			UIUtil.setViewWidth(mPgvPm2dot5Out, pgvWidth);
			UIUtil.setViewHeight(mPgvPm2dot5Out, pgvWidth);
		}
		mTvFrequency.setPadding(0, 0, 0, height * 4 / 20);
		mTvHumidity.setPadding(0, height * 3 / 8, 0, 0);

		mTvOutOutTemp.setPadding(0, height * 9 / 20, 0, 0);
		mTvOutInTemp.setPadding(0, height / 4, width / 20, 0);
		mTvInOutTemp.setPadding(width * 5 / 20, height * 5 / 20, 0, 0);
		mTvInInTemp.setPadding(0, height * 9 / 20, width * 3 / 20, 0);

		int isWifiMode = SharedPreferenceUtil.getIntegerValueByKey(XinfengApplication.CONTEXT, Global.CONFIG_FILE_NAME,
				Global.IS_WIFI_MODE);
		String wifiMode = isWifiMode == 1 ? "外网" : "内网";
		mTvOffLineMode.setText("（" + mDeviceName + wifiMode + "离线）");
	}

	private void initOffLineTimer() {
		TimerUtil.stopTimer(TAG);
		TimerUtil.startTimer(TAG, 30, 1000, new TimerActionListener() {

			@Override
			public void doAction() {
				final int isWifiMode = SharedPreferenceUtil.getIntegerValueByKey(XinfengApplication.CONTEXT,
						Global.CONFIG_FILE_NAME, Global.IS_WIFI_MODE);
				if (0 >= TimerUtil.getTimerTime(TAG)) {
					if (!isAdded()) {
						return;
					}
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							String wifiMode = isWifiMode == 1 ? "外网" : "内网";
							roate(false);
							mTvOffLineMode.setText("（" + mDeviceName + wifiMode + "离线）");
						}
					});
					initOffLineTimer();
				}
			}
		});
	}

	@Override
	public void onResume() {
		RcvComsModel model = DBMgr.getHistoryData(RcvComsModel.class, "DA");
		refreashUi(model);
		super.onResume();
	}

	private void refreashUi(RcvComsModel model) {
		if (null == model) {
			return;
		}
		int isWifiMode = SharedPreferenceUtil.getIntegerValueByKey(XinfengApplication.CONTEXT, Global.CONFIG_FILE_NAME,
				Global.IS_WIFI_MODE);
		String wifiMode = isWifiMode == 1 ? "外网" : "内网";
		mCurrentModel = model;
		mTvFrequency.setText("频率：" + CommandUtil.hexStringToInt(model.getCommand1()) + " Hz");
		initOffLineTimer();
		if ("00".equals(model.getDisplayCo2()) || "0".equals(model.getDisplayCo2())) {
			roate(false);
			mTvOffLineMode.setText("（" + mDeviceName + wifiMode + "离线）");
		} else {
			roate(true, CommandUtil.hexStringToInt(model.getCommand1()));
			mTvOffLineMode.setText("（" + mDeviceName + wifiMode + "在线）");
		}
		UIUtil.setUnderLine(mTvFrequency);

		int cleanValue = CommandUtil.hexStringToInt(model.getCommand4());
		mTvModeSwitch.setText(1 == cleanValue % 10 ? "静电除尘：开" : "静电除尘：关");

		switch (cleanValue / 10) {
			case 1:
				mIvGaoxiao.setBackgroundResource(R.drawable.gaoxiao_guoqi);
				((MainActivity) getActivity()).showMyDialog();
				break;
			case 2:
				mIvChuxiao.setBackgroundResource(R.drawable.chuxiao_guoqi);
				((MainActivity) getActivity()).showMyDialog();
				break;
			case 3:
				mIvchuchen.setBackgroundResource(R.drawable.chuchen_guoqi);
				((MainActivity) getActivity()).showMyDialog();
				break;

			default:
				break;
		}
		mPgvPm2dot5Out.setText(model.getDisplayPm2dotOut());
		mPgvPm2dot5In.setText(model.getDisplayPm2dotIn());
		mPgvCo2.setText(model.getDisplayCo2());
		mPgvJiaquan.setText("" + CommandUtil.hexStringToInt(model.getCommand2()) / 100.0);
		mTvInInTemp.setText("" + CommandUtil.hexStringToInt(model.getCommand11()) + Html.fromHtml("&#8451;"));
		mTvInOutTemp.setText("" + CommandUtil.hexStringToInt(model.getCommand12()) + Html.fromHtml("&#8451;"));
		mTvOutInTemp.setText("" + CommandUtil.hexStringToInt(model.getCommand13()) + Html.fromHtml("&#8451;"));
		mTvOutOutTemp.setText("" + CommandUtil.hexStringToInt(model.getCommand14()) + Html.fromHtml("&#8451;"));

		mTvHumidity.setText("湿度：" + CommandUtil.hexStringToInt(model.getCommand15()) + "%");

		int mode = CommandUtil.hexStringToInt(model.getCommand3());
		Log.d("ccc", "接受模式:" + mode % 10);
		switch (mode % 10) {
			case 1:
				mTvMode.setText("智能模式");
				break;
			case 2:
				mTvMode.setText("手动模式");
				break;
			case 3:
				mTvMode.setText("睡眠模式");
				break;
			default:
				break;
		}
	}

	private void roate(boolean isShow) {
		roate(isShow, 0);
	}

	private void roate(boolean isShow, int hz) {
		mIvFans.clearAnimation();
		Animation mAnim = new RotateAnimation(0f, 3600f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mAnim.setRepeatCount(Animation.INFINITE);
		mAnim.setFillAfter(true);
		mAnim.setInterpolator(new LinearInterpolator());

		if (isShow) {
			mAnim.setDuration(15000 - (hz - 10) * 200);
			mIvFans.startAnimation(mAnim);
		} else {
			mIvFans.clearAnimation();
		}
	}

	/**
	 * 收到指令方法
	 * 
	 * @param model
	 *            指令数据
	 */
	public void onEventMainThread(RcvComsModel model) {
		refreashUi(model);
	}

	/**
	 * 收到指令方法
	 * 
	 * @param model
	 *            指令数据
	 */
	public void onEventMainThread(String info) {
		// toast(info);
	}

	@Override
	public void onDestroy() {
		try {
			EventBus.getDefault().unregister(this);
		} catch (Exception e) {
		}
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.pgv_pm_2dot5_in:
				jumpToGraphic("pm25in");
				break;
			case R.id.pgv_pm_2dot5_out:
				jumpToGraphic("pm25out");
				break;
			case R.id.tv_in_in_wind_temp:
				jumpToGraphic("wendu1");
				break;
			case R.id.tv_in_out_wind_temp:
				jumpToGraphic("wendu2");
				break;
			case R.id.tv_out_in_wind_temp:
				jumpToGraphic("wendu3");
				break;
			case R.id.tv_out_out_wind_temp:
				jumpToGraphic("wendu4");
				break;
			case R.id.tv_humidity:
				jumpToGraphic("shidu");
				break;
			case R.id.pgv_co2:
				jumpToGraphic("co2");
				break;
			case R.id.pgv_jiaquan:
				jumpToGraphic("jiaquan");
				break;

			default:
				break;
		}

	}

	private void jumpToGraphic(String action) {
		if (!NetUtil.isNetworkAvailable()) {
			toast(getString(R.string.network_is_not_available));
			return;
		}
		Intent intent = new Intent(getActivity(), WebActivity.class);
		intent.putExtra("url", Global.getGraphicUrl(action));
		startActivity(intent);
	}

	private void showShare(String content) {
		if (!NetUtil.isNetworkAvailable()) {
			toast(getString(R.string.network_is_not_available));
			return;
		}
		OnekeyShare oks = new OnekeyShare();
		oks.disableSSOWhenAuthorize(); // 关闭sso授权
		oks.setTitle(getString(R.string.app_name)); // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setText(content); // text是分享文本，所有平台都需要这个字段
		// oks.setComment("我是测试评论文本");
		oks.setSilent(false); // 启动分享GUI
		oks.setShareFromQQAuthSupport(false);
		oks.setDialogMode(); // 令编辑页面显示为Dialog模式
		oks.show(getActivity());
	}

}
