package com.zjhbkj.xinfen;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.zjhbkj.xinfen.model.MsgInfo;
import com.zjhbkj.xinfen.udp.UDPServer;
import com.zjhbkj.xinfen.udp.UDPServer.ClientMsgListener;
import com.zjhbkj.xinfen.udp.UDPServer.DataRecvListener;
import com.zjhbkj.xinfen.wifihot.Global;
import com.zjhbkj.xinfen.wifihot.WifiHotAdmin;

public class MainActivity extends Activity implements OnClickListener {
	private static final String TAG = "MainActivity";
	private EditText etContent;
	private Button btnSend;
	private List<MsgInfo> msgList = new ArrayList<MsgInfo>();
	private Handler handler;
	private static final String KEY_WHAT = "what";
	private static final String KEY_MSGINFO = "msgInfo";
	private static final int MSG_RECVMSG = 1;
	public static final String KEY_RC_NAME = "name";
	/** 消息显示列表控件 */
	private ListView listview;
	/** 数控适配器 */
	private MsginfoAdapter adapter;
	private UDPServer server;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
		WifiHotAdmin wifiAp = new WifiHotAdmin(MainActivity.this);
		wifiAp.startWifiAp(Global.SSID, Global.PASSWORD);

	}

	private void initViews() {
		// 获取消息内容编辑框
		etContent = (EditText) findViewById(R.id.et_content);
		// 获取发送按钮
		btnSend = (Button) findViewById(R.id.btn_send);
		// 获取消息列表控件
		listview = (ListView) findViewById(R.id.listView1);
		// 新建消息显示适配器
		adapter = new MsginfoAdapter(this);
		// 设置消息适配器的数据源
		adapter.setData(msgList);
		// 设置消息显示列表的数据适配器
		listview.setAdapter(adapter);
		// 设置滚动时候的缓冲色
		listview.setCacheColorHint(0);

		// 开启服务器
		ExecutorService exec = Executors.newCachedThreadPool();
		server = new UDPServer(new DataRecvListener() {
			// 接收到消息
			public void onRecv(MsgInfo info) {
				sendMsgToHandler(info);
			}
		});
		exec.execute(server);
		// 发送消息
		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						server.send(etContent.getText().toString().trim().getBytes(), new ClientMsgListener() {

							@Override
							public void handlerErorMsg(String errorMsg) {
								Log.d("aaa", errorMsg);
							}

							@Override
							public void handlerHotMsg(String hotMsg) {
								Log.d("aaa", hotMsg);
							}
						});
					}
				}).start();

			}
		});
		this.initHandler();
	}

	/**
	 * @param info
	 *            发送消息到消息处理器中
	 */
	private void sendMsgToHandler(MsgInfo info) {
		Message msg = new Message();
		Bundle b = new Bundle();
		b.putInt(KEY_WHAT, MSG_RECVMSG);
		b.putSerializable(KEY_MSGINFO, info);
		msg.setData(b);
		handler.sendMessage(msg);
	}

	/**
	 * 初始化消息处理器
	 */
	private void initHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.getData().getInt(KEY_WHAT)) {
					case MSG_RECVMSG:
						msgList.add((MsgInfo) msg.getData().getSerializable(KEY_MSGINFO));
						adapter.notifyDataSetChanged();
						listview.setSelection(msgList.size() - 1);
						break;
				}
			}
		};
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

			default:
				break;
		}
	}
}
