package com.weibo.common.threads;

import java.util.Date;

import org.apache.log4j.Logger;

import com.weibo.common.utils.FormatOutput;

public class MyThread extends Thread {
	public static Logger logger = Logger.getLogger(MyThread.class);
	private MyThreadGroup myThreadGroup;

	// 记录上次切换的时间
	public static long last_interrupt_time = 0;
	// 最小间隔时间
	private static long min_interval_time = 1000 * 60;

	public MyThreadGroup getMyThreadGroup() {
		return myThreadGroup;
	}

	public void setMyThreadGroup(MyThreadGroup myThreadGroup) {
		this.myThreadGroup = myThreadGroup;
	}

	public MyThread(MyThreadGroup myThreadGroup, Runnable target, String name) {
		super(myThreadGroup, target, name);
		logger.info(FormatOutput.threadPrefix + name + "  线程开启了");
	}

	public MyThread(Runnable target, String name) {
		super(target, name);
		logger.info(FormatOutput.threadPrefix + name + "  线程开启了");
	}

	// 打断下当前线程，需要临时切换帐号
	public void interruptSleepThread() {
		long currentTime = new Date().getTime();
		// 防止多个线程阻塞
		synchronized (MyThread.class) {
			// 表明可以切换,要不然不需要切换
			if (currentTime - last_interrupt_time >= min_interval_time) {
				last_interrupt_time = currentTime;
				logger.info("打断用户切换线程的睡眠,即将切换用户!");
				this.interrupt();
			}
		}
	}
}
