package com.zjhbkj.xinfen.fragment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjhbkj.xinfen.R;
import com.zjhbkj.xinfen.db.DBMgr;
import com.zjhbkj.xinfen.model.RcvComsModel;
import com.zjhbkj.xinfen.udp.UDPServer;
import com.zjhbkj.xinfen.util.CommandUtil;
import com.zjhbkj.xinfen.util.EvtLog;
import com.zjhbkj.xinfen.util.UIUtil;

import de.greenrobot.event.EventBus;

public class HomeFragment extends FragmentBase {

	private UDPServer mUdpServer;
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
		EventBus.getDefault().register(this);
		ExecutorService exec = Executors.newCachedThreadPool();
		mUdpServer = new UDPServer();
		exec.execute(mUdpServer);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View layout = inflater.inflate(R.layout.activity_home, container, false);
		initViews(layout);
		return layout;
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
				toast("正在开发中...");
			}
		});

		mViewHome = layout.findViewById(R.id.rl_home_tab_layout);
		int height = UIUtil.getScreenWidth(getActivity()) * 16 / 25;
		int width = UIUtil.getScreenWidth(getActivity()) * 4 / 5;
		UIUtil.setViewWidth(mViewHome, width);
		UIUtil.setViewHeight(mViewHome, height);
		EvtLog.d("aaa", "屏幕宽度：" + UIUtil.getScreenWidth(getActivity()));
		EvtLog.d("aaa", "mViewHome宽度：" + UIUtil.getScreenWidth(getActivity()) * 4 / 5);
		EvtLog.d("aaa", "mViewHome高度：" + height);

		mTvInM3 = (TextView) layout.findViewById(R.id.tv_pm_2dot5_in_title);
		mTvInM3.setText(Html.fromHtml("mg/m&sup3;"));
		mTvOutM3 = (TextView) layout.findViewById(R.id.tv_pm_2dot5_out_title);
		mTvOutM3.setText(Html.fromHtml("mg/m&sup3;"));

		mTvFrequency = (TextView) layout.findViewById(R.id.tv_frequency);
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
		mTvModeSwitch.setText(1 == CommandUtil.hexStringToInt(model.getCommand4()) ? "模式开关：开" : "模式开关：关");
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
		refreashUi(model);
	}

	@Override
	public void onDestroy() {
		if (null != mUdpServer) {
			mUdpServer.stopAcceptMessage();
			mUdpServer.closeConnection();
		}
		try {
			EventBus.getDefault().unregister(this);
		} catch (Exception e) {
		}
		super.onDestroy();
	}

}
