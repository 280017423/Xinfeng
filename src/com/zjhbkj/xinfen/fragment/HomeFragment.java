package com.zjhbkj.xinfen.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.zjhbkj.xinfen.R;
import com.zjhbkj.xinfen.activity.MainActivity;
import com.zjhbkj.xinfen.activity.WebActivity;
import com.zjhbkj.xinfen.commom.Global;
import com.zjhbkj.xinfen.db.DBMgr;
import com.zjhbkj.xinfen.model.RcvComsModel;
import com.zjhbkj.xinfen.util.CommandUtil;
import com.zjhbkj.xinfen.util.NetUtil;
import com.zjhbkj.xinfen.util.TimerUtil;
import com.zjhbkj.xinfen.util.TimerUtil.TimerActionListener;
import com.zjhbkj.xinfen.util.UIUtil;

import de.greenrobot.event.EventBus;

public class HomeFragment extends FragmentBase implements OnClickListener {
	public static final String TAG = MainActivity.class.getName();
	private TextView mTvFrequency;
	private TextView mTvPpm;
	private TextView mTvMode;
	private TextView mTvModeSwitch;
	private TextView mTvPm2dot5Out;
	private TextView mTvPm2dot5In;
	private TextView mTvCo2;
	private TextView mTvInInTemp;
	private TextView mTvInOutTemp;
	private TextView mTvOutInTemp;
	private TextView mTvOutOutTemp;
	private TextView mTvHumidity;
	private View mViewHome;
	private TextView mTvInM3;
	private TextView mTvOutM3;
	private TextView mTvRight;
	private TextView mTvOffLineMode;

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
		layout.findViewById(R.id.iv_pm_2dot5_in).setOnClickListener(this);
		layout.findViewById(R.id.iv_pm_2dot5_out).setOnClickListener(this);
		mTvInInTemp.setOnClickListener(this);
		mTvInOutTemp.setOnClickListener(this);
		mTvOutInTemp.setOnClickListener(this);
		mTvOutOutTemp.setOnClickListener(this);
		mTvHumidity.setOnClickListener(this);
		mTvCo2.setOnClickListener(this);
		mTvPpm.setOnClickListener(this);
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
				showShare("我是测试分享的...");
			}
		});

		mViewHome = layout.findViewById(R.id.rl_home_tab_layout);
		int height = UIUtil.getScreenWidth(getActivity()) * 16 / 25;
		int width = UIUtil.getScreenWidth(getActivity()) * 4 / 5;
		UIUtil.setViewWidth(mViewHome, width);
		UIUtil.setViewHeight(mViewHome, height);

		mTvInM3 = (TextView) layout.findViewById(R.id.tv_pm_2dot5_in_title);
		mTvInM3.setText(Html.fromHtml("mg/m&sup3;"));
		mTvOutM3 = (TextView) layout.findViewById(R.id.tv_pm_2dot5_out_title);
		mTvOutM3.setText(Html.fromHtml("mg/m&sup3;"));

		mTvFrequency = (TextView) layout.findViewById(R.id.tv_frequency);
		mTvOffLineMode = (TextView) layout.findViewById(R.id.tv_offline_mode);
		mTvPpm = (TextView) layout.findViewById(R.id.tv_ppm);
		mTvMode = (TextView) layout.findViewById(R.id.tv_mode);
		mTvModeSwitch = (TextView) layout.findViewById(R.id.tv_mode_switch);
		mTvPm2dot5Out = (TextView) layout.findViewById(R.id.tv_pm_2dot5_out_low);
		mTvPm2dot5In = (TextView) layout.findViewById(R.id.tv_pm_2dot5_in_low);
		mTvCo2 = (TextView) layout.findViewById(R.id.tv_co2_low);

		mTvInInTemp = (TextView) layout.findViewById(R.id.tv_in_in_wind_temp);
		mTvInOutTemp = (TextView) layout.findViewById(R.id.tv_in_out_wind_temp);
		mTvOutInTemp = (TextView) layout.findViewById(R.id.tv_out_in_wind_temp);
		mTvOutOutTemp = (TextView) layout.findViewById(R.id.tv_out_out_wind_temp);

		mTvHumidity = (TextView) layout.findViewById(R.id.tv_humidity);

		mTvHumidity.setPadding(0, height / 4, 0, 0);
		mTvOutInTemp.setPadding(width * 1 / 20, height / 4, 0, 0);
		mTvOutOutTemp.setPadding(0, height / 4, width * 1 / 20, 0);
		mTvFrequency.setPadding(0, height * 9 / 20, 0, 0);
		mTvInInTemp.setPadding(width * 3 / 20, height * 9 / 20, 0, 0);
		mTvInOutTemp.setPadding(0, height * 9 / 20, width * 3 / 20, 0);

		mTvPpm.setPadding(0, 0, 0, height / 20);
		mTvCo2.setPadding(0, 0, 0, height / 20);
	}

	private void initOffLineTimer() {
		TimerUtil.startTimer(TAG, 60, 1000, new TimerActionListener() {

			@Override
			public void doAction() {
				if (0 == TimerUtil.getTimerTime(TAG)) {
					if (!isAdded()) {
						return;
					}
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							mTvOffLineMode.setText("（离线）");
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

		mTvFrequency.setText("频率：" + CommandUtil.hexStringToInt(model.getCommand1()) + " hz");
		mTvPpm.setText("甲醛：" + CommandUtil.hexStringToInt(model.getCommand2()) / 100.0 + " ppm");
		if ("00".equals(model.getDisplayCo2())) {
			mTvOffLineMode.setText("（离线）");
		} else {
			mTvOffLineMode.setText("（在线）");
		}
		mTvCo2.setText(Html.fromHtml("CO₂") + "：" + model.getDisplayCo2() + " ppm");
		UIUtil.setUnderLine(mTvFrequency);
		UIUtil.setUnderLine(mTvPpm);
		UIUtil.setUnderLine(mTvCo2);

		int mode = CommandUtil.hexStringToInt(model.getCommand3());
		switch (mode) {
			case 1:
				mTvMode.setText("自动模式");
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
		int cleanValue = CommandUtil.hexStringToInt(model.getCommand4());
		mTvModeSwitch.setText(1 == cleanValue % 10 ? "模式开关：开" : "模式开关：关");

		switch (cleanValue / 10) {
			case 1:
				// TODO 需要显示滤网状态
				((MainActivity) getActivity()).showMyDialog();
				break;
			case 2:
				((MainActivity) getActivity()).showMyDialog();
				break;
			case 3:
				((MainActivity) getActivity()).showMyDialog();
				break;

			default:
				break;
		}
		mTvPm2dot5Out.setText(model.getDisplayPm2dotOut());
		mTvPm2dot5In.setText(model.getDisplayPm2dotIn());
		mTvInInTemp.setText("" + CommandUtil.hexStringToInt(model.getCommand11()) + Html.fromHtml("&#8451;"));
		mTvInOutTemp.setText("" + CommandUtil.hexStringToInt(model.getCommand12()) + Html.fromHtml("&#8451;"));
		mTvOutInTemp.setText("" + CommandUtil.hexStringToInt(model.getCommand13()) + Html.fromHtml("&#8451;"));
		mTvOutOutTemp.setText("" + CommandUtil.hexStringToInt(model.getCommand14()) + Html.fromHtml("&#8451;"));

		mTvHumidity.setText("湿度：" + CommandUtil.hexStringToInt(model.getCommand15()));
	}

	public void onEventMainThread(String info) {
		// Toast.makeText(getActivity(), info, Toast.LENGTH_LONG).show();
	}

	/**
	 * 收到指令方法
	 * 
	 * @param model
	 *            指令数据
	 */
	public void onEventMainThread(RcvComsModel model) {
		TimerUtil.setTimerTime(TAG, 60);
		refreashUi(model);
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
			case R.id.iv_pm_2dot5_in:
				jumpToGraphic("pm25in");
				break;
			case R.id.iv_pm_2dot5_out:
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
			case R.id.tv_co2_low:
				jumpToGraphic("co2");
				break;
			case R.id.tv_ppm:
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
