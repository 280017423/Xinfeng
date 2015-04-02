package com.zjhbkj.xinfen.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjhbkj.xinfen.R;
import com.zjhbkj.xinfen.model.MsgInfo;

/**
 * 消息显示列表适配器
 */
public class MsginfoAdapter extends BaseAdapter {

	private List<MsgInfo> mData;
	private Context mContext;

	public static final String TAG = MsginfoAdapter.class.getSimpleName();

	public MsginfoAdapter(Context context, List<MsgInfo> imgData) {
		this.mData = imgData;
		this.mContext = context;
	}

	public void clearItem() {
		mData.clear();
		notifyDataSetInvalidated();
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public MsgInfo getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.item_msginfo, null);
			holder = new ViewHolder();
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (mData == null || mData.size() <= position) {
			return convertView;
		}
		holder.tvName.setText(mData.get(position).getName());
		holder.tvContent.setText(mData.get(position).getContent());
		return convertView;
	}
}

class ViewHolder {
	public TextView tvName;
	public TextView tvContent;
}
