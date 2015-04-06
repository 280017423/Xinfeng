package com.zjhbkj.xinfen.util;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.zjhbkj.xinfen.R;
import com.zjhbkj.xinfen.widget.wheelview.WheelView;
import com.zjhbkj.xinfen.widget.wheelview.adapters.NumericWheelAdapter;

public class WheelViewUtil {

	public static void showWheelview(Context context, int current, final OnClickListener listener) {
		View contentView = View.inflate(context, R.layout.view_wheelview_pop, null);
		final PopupWindow mPopupWindow = new PopupWindow(
				contentView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		contentView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
			}
		});
		final WheelView wv1 = (WheelView) contentView.findViewById(R.id.wv_item_1);
		Button btnOk = (Button) contentView.findViewById(R.id.btn_ok);
		NumericWheelAdapter adapter = new NumericWheelAdapter(context, 50, 255);
		wv1.setViewAdapter(adapter);
		wv1.setCurrentItem(current);
		wv1.setVisibleItems(5);
		wv1.setCyclic(true);
		mPopupWindow.setFocusable(true);
		// 点击popupwindow窗口之外的区域popupwindow消失
		ColorDrawable dw = new ColorDrawable(0x00);
		mPopupWindow.setBackgroundDrawable(dw);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
				v.setTag(50 + wv1.getCurrentItem());
				listener.onClick(v);
			}
		});
	}
}
