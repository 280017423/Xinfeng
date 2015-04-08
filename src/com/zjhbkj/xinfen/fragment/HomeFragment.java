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

import de.greenrobot.event.EventBus;

public class HomeFragment extends FragmentBase {

	private UDPServer mUdpServer;
	private TextView mTvCommands;

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
		mTvCommands = (TextView) layout.findViewById(R.id.tv_receive_command);
		ExecutorService exec = Executors.newCachedThreadPool();
		mUdpServer = new UDPServer();
		exec.execute(mUdpServer);
	}

	@Override
	public void onResume() {
		RcvComsModel model = DBMgr.getHistoryData(RcvComsModel.class, "DA");
		// TODO 开始刷新
		super.onResume();
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
		mTvCommands.setText(model.toString());
		// TODO 开始刷新
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
