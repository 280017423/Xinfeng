package com.zjhbkj.xinfen.util;

import com.zjhbkj.xinfen.listener.TimerCheckListener;

public class TimerCheckUtil {
	private int mCount;
	private int mTimeOutCount = 1;
	private int mSleepTime = 1000; // 1s
	private boolean mExitFlag;
	private Thread mThread;
	private TimerCheckListener mTimerCheckListener;

	public TimerCheckUtil(TimerCheckListener listener) {
		mTimerCheckListener = listener;
		mThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (!mExitFlag) {
					mCount++;
					if (mCount < mTimeOutCount) {
						if (null != mTimerCheckListener) {
							boolean needExit = mTimerCheckListener.doTimerCheckWork();
							if (needExit) {
								exit();
							}
						}
						try {
							Thread.sleep(mSleepTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
							exit();
						}
					} else {
						if (null != mTimerCheckListener) {
							mTimerCheckListener.doTimeOutWork();
						}
						exit();
					}
				}
			}
		});
	}

	/**
	 * start
	 * 
	 * @param times
	 *            How many times will check?
	 * @param sleepTime
	 *            ms, Every check sleep time.
	 */
	public void start(int timeOutCount, int sleepTime) {
		mTimeOutCount = timeOutCount;
		mSleepTime = sleepTime;
		mThread.start();
	}

	public void exit() {
		mExitFlag = true;
	}

}
