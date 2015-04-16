package com.weibo.common.GUI.verify;

import org.apache.log4j.Logger;

import com.weibo.common.GUI.verify.iface.IFrame;
import com.weibo.common.utils.StaticValue;

public class VerifyCodeRunnable implements Runnable {
	private static Logger logger = Logger.getLogger(VerifyCodeRunnable.class);
	private IFrame verifyFrame;

	public VerifyCodeRunnable(IFrame verifyFrame) {
		this.verifyFrame = verifyFrame;
	}

	public void run() {
		while (verifyFrame.isRunning()) {
			try {
				logger.info("等待输入验证码!");
				Thread.sleep(StaticValue.verify_print_interval_time);// 线程等待5s后继续循环判断，客户端是否已经点了验证码
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
