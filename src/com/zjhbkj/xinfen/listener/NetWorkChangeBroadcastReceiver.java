package com.zjhbkj.xinfen.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.widget.Toast;

import com.zjhbkj.xinfen.udp.UDPClient;
import com.zjhbkj.xinfen.udp.UDPClient.ClientMsgListener;
import com.zjhbkj.xinfen.util.EvtLog;

public class NetWorkChangeBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, Intent intent) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
			for (int i = 0; i < networkInfos.length; i++) {
				State state = networkInfos[i].getState();
				if (NetworkInfo.State.CONNECTED == state) {
					Toast.makeText(context, "网络打开", Toast.LENGTH_LONG).show();
					UDPClient client = UDPClient.newInstance(new ClientMsgListener() {

						@Override
						public void handlerErorMsg(String errorMsg) {
							EvtLog.d("aaa", errorMsg);
						}
					});
					client.connectServer();
					return;
				}
			}
		}
		// 没有执行return,则说明当前无网络连接
		Toast.makeText(context, "网络关闭", Toast.LENGTH_LONG).show();
	}

}