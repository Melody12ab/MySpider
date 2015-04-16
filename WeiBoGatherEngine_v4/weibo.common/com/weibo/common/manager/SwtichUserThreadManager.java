package com.weibo.common.manager;

import com.weibo.common.threads.MyThread;

/**
 * 特意写出来管理切换登陆帐户时候的interrupt
 * 
 * @author zel
 * 
 */
public class SwtichUserThreadManager {
	public static MyThread myThread = null;

	public static void setSwitchThread(MyThread myThread) {
		SwtichUserThreadManager.myThread = myThread;
	}

	public static void interrupte() {
		myThread.interruptSleepThread();
	}
}
