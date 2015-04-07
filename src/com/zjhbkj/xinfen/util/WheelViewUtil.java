package com.zjhbkj.xinfen.util;

import java.util.Calendar;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zjhbkj.xinfen.R;
import com.zjhbkj.xinfen.widget.wheelview.OnWheelChangedListener;
import com.zjhbkj.xinfen.widget.wheelview.WheelView;
import com.zjhbkj.xinfen.widget.wheelview.adapters.ArrayWheelAdapter;
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
		final TextView title = (TextView) contentView.findViewById(R.id.tv_pop_title);
		Button btnOk = (Button) contentView.findViewById(R.id.btn_ok);
		title.setText("选择pm2.5");
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

	public static void initWheelView(final Context context, int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		int startYear = cal.get(Calendar.YEAR);
		int endYear = 2099;
		final String[] years = new String[endYear - startYear + 1];
		for (int i = startYear; i <= endYear; i++) {
			years[i - startYear] = i + "年";
		}
		String[] months = new String[12];
		for (int i = 1; i <= 12; i++) {
			months[i - 1] = i + "月";
		}
		final View contentView = View.inflate(context, R.layout.view_wheelview_pop, null);
		final WheelView wvYear = (WheelView) contentView.findViewById(R.id.wv_item_1);
		final WheelView wvMonth = (WheelView) contentView.findViewById(R.id.wv_item_2);
		final WheelView wvDay = (WheelView) contentView.findViewById(R.id.wv_item_3);
		wvMonth.setVisibility(View.VISIBLE);
		wvDay.setVisibility(View.VISIBLE);
		final PopupWindow mPopupWindow = new PopupWindow(
				contentView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		contentView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
			}
		});
		OnWheelChangedListener listener = new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				switch (wheel.getId()) {
					case R.id.wv_item_1:
						refreashDayWheelView(context, wvDay, wvYear.getCurrentItem(), wvMonth.getCurrentItem(),
								wvDay.getCurrentItem());
						break;
					case R.id.wv_item_2:
						refreashDayWheelView(context, wvDay, wvYear.getCurrentItem(), wvMonth.getCurrentItem(),
								wvDay.getCurrentItem());
						break;
					case R.id.wv_item_3:
						break;
					default:
						break;
				}
			}
		};
		wvYear.addChangingListener(listener);
		wvMonth.addChangingListener(listener);
		wvDay.addChangingListener(listener);
		ArrayWheelAdapter<String> mYearAdapter = new ArrayWheelAdapter<String>(context, years);
		ArrayWheelAdapter<String> mMonthAdapter = new ArrayWheelAdapter<String>(context, months);
		wvYear.setViewAdapter(mYearAdapter);
		wvMonth.setViewAdapter(mMonthAdapter);
		wvYear.setVisibleItems(5);
		wvMonth.setVisibleItems(5);
		wvYear.setCyclic(true);
		wvMonth.setCyclic(true);
		wvYear.setCurrentItem(year - 1900);
		wvMonth.setCurrentItem(month - 1);

		refreashDayWheelView(context, wvDay, wvYear.getCurrentItem(), wvMonth.getCurrentItem(), day - 1);
		mPopupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
	}

	private static void refreashDayWheelView(Context context, WheelView wvDay, int year, int month, int current) {
		int dayNum = DateUtil.getDayNum(year, month);
		String[] days = new String[dayNum];
		for (int i = 1; i <= dayNum; i++) {
			days[i - 1] = i + "日";
		}
		if (current >= days.length) {
			current = days.length - 1;
		}
		ArrayWheelAdapter<String> mDayAdapter = new ArrayWheelAdapter<String>(context, days);
		wvDay.setVisibleItems(5);
		wvDay.setCyclic(true);
		wvDay.setCurrentItem(current);
		wvDay.setViewAdapter(mDayAdapter);
	}
}
