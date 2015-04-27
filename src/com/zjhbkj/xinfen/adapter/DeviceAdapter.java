package com.zjhbkj.xinfen.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.zjhbkj.xinfen.R;
import com.zjhbkj.xinfen.model.DeviceModel;

/**
 * 设备列表适配器
 */
public class DeviceAdapter extends BaseAdapter {

	private List<DeviceModel> mDeviceModels;
	private Context mContext;
	private OnClickListener mOnClickListener;

	public DeviceAdapter(Context context, List<DeviceModel> data, OnClickListener listener) {
		mContext = context;
		mDeviceModels = data;
		mOnClickListener = listener;
	}

	@Override
	public int getCount() {
		if (null == mDeviceModels) {
			return 0;
		}
		return mDeviceModels.size();
	}

	@Override
	public DeviceModel getItem(int position) {
		if (null == mDeviceModels) {
			return null;
		}
		return mDeviceModels.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.view_device_item, null);
			holder = new ViewHolder();
			holder.mTvName = (TextView) convertView.findViewById(R.id.tv_device_name);
			holder.mBtnDelete = (Button) convertView.findViewById(R.id.btn_del_device);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		DeviceModel model = mDeviceModels.get(position);
		holder.mTvName.setText("设备：" + model.getIdValue());
		holder.mBtnDelete.setTag(model);
		holder.mBtnDelete.setOnClickListener(mOnClickListener);
		return convertView;
	}
}

class ViewHolder {
	public TextView mTvName;
	public Button mBtnDelete;
}
