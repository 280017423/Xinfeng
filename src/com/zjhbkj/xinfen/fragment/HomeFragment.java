package com.zjhbkj.xinfen.fragment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zjhbkj.xinfen.R;
import com.zjhbkj.xinfen.db.DBMgr;
import com.zjhbkj.xinfen.model.RcvComsModel;
import com.zjhbkj.xinfen.udp.UDPServer;
import com.zjhbkj.xinfen.util.CommandUtil;

import de.greenrobot.event.EventBus;

public class HomeFragment extends FragmentBase {

	private UDPServer mUdpServer;
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
		mTvPpm.setText("" + CommandUtil.hexStringToInt(model.getCommand2()) / 100.0);
		int mode = CommandUtil.hexStringToInt(model.getCommand3());
		switch (mode) {
			case 1:
				mTvMode.setText("自动");
				break;
			case 2:
				mTvMode.setText("手动");
				break;
			case 3:
				mTvMode.setText("睡眠");
				break;
			default:
				break;
		}
		mTvModeSwitch.setText(1 == CommandUtil.hexStringToInt(model.getCommand4()) ? "开" : "关");
		mTvPm2dot5Out.setText(model.getDisplayPm2dotOut());
		mTvPm2dot5In.setText(model.getDisplayPm2dotIn());
		mTvCo2.setText(model.getDisplayCo2());
		mTvInInTemp.setText("" + CommandUtil.hexStringToInt(model.getCommand11()));
		mTvInOutTemp.setText("" + CommandUtil.hexStringToInt(model.getCommand12()));
		mTvOutInTemp.setText("" + CommandUtil.hexStringToInt(model.getCommand13()));
		mTvOutOutTemp.setText("" + CommandUtil.hexStringToInt(model.getCommand14()));

		mTvHumidity.setText("" + CommandUtil.hexStringToInt(model.getCommand15()));
	}

	public void onEventMainThread(String info) {
		Toast.makeText(getActivity(), info, Toast.LENGTH_LONG).show();
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
