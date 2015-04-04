package com.zjhbkj.xinfen.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zjhbkj.xinfen.R;
import com.zjhbkj.xinfen.adapter.MsginfoAdapter;
import com.zjhbkj.xinfen.model.MsgInfo;
import com.zjhbkj.xinfen.udp.UDPServer;
import com.zjhbkj.xinfen.udp.UDPServer.ClientMsgListener;
import com.zjhbkj.xinfen.udp.UDPServer.DataRecvListener;

public class HomeFragment extends FragmentBase {

	private static final int KEY_RECEIVE_COMMAND_WHAT = 1;
	private UDPServer mUdpServer;
	private ListView mLvCommands;
	private MsginfoAdapter mMsginfoAdapter;
	private List<MsgInfo> mCommands;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case KEY_RECEIVE_COMMAND_WHAT:
					mCommands.add((MsgInfo) msg.obj);
					mMsginfoAdapter.notifyDataSetChanged();
					mLvCommands.setSelection(mCommands.size() - 1);
					break;
			}
		}
	};

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
		mCommands = new ArrayList<MsgInfo>();
		mMsginfoAdapter = new MsginfoAdapter(getActivity(), mCommands);
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
		mLvCommands = (ListView) layout.findViewById(R.id.lv_receive_command);
		mLvCommands.setAdapter(mMsginfoAdapter);
		mLvCommands.setCacheColorHint(0);
		ExecutorService exec = Executors.newCachedThreadPool();
		mUdpServer = new UDPServer(new DataRecvListener() {
			// 接收到消息
			public void onRecv(MsgInfo info) {
				sendMsgToHandler(info);
			}
		}, new ClientMsgListener() {

			@Override
			public void handlerHotMsg(final String hotMsg) {
				Log.d("aaa", hotMsg);
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(getActivity(), hotMsg, Toast.LENGTH_LONG).show();
					}
				});
			}

			@Override
			public void handlerErorMsg(final String errorMsg) {
				Log.d("aaa", errorMsg);
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
					}
				});
			}
		});
		exec.execute(mUdpServer);
	}

	private void sendMsgToHandler(MsgInfo info) {
		Message msg = mHandler.obtainMessage(KEY_RECEIVE_COMMAND_WHAT, info);
		mHandler.sendMessage(msg);
	}

	@Override
	public void onDestroy() {
		if (null != mUdpServer) {
			mUdpServer.stopAcceptMessage();
			mUdpServer.closeConnection();
		}
		super.onDestroy();
	}

}
