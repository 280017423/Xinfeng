package com.zjhbkj.xinfen.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjhbkj.xinfen.R;
import com.zjhbkj.xinfen.model.DeviceModel;

/**
 * 设备列表适配器
 */
public class DeviceAdapter extends BaseAdapter {

	private List<DeviceModel> mDeviceModels;
	private Context mContext;

	public static final String TAG = DeviceAdapter.class.getSimpleName();

	public DeviceAdapter(Context context, List<DeviceModel> data) {
		mContext = context;
		mDeviceModels = data;
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
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_device_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		DeviceModel model = mDeviceModels.get(position);
		holder.tvContent.setText("设备：" + model.getIdValue());
		return convertView;
	}
}

class ViewHolder {
	public TextView tvContent;
}
