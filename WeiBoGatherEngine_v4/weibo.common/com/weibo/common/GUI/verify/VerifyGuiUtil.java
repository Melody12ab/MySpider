package com.weibo.common.GUI.verify;

import org.apache.log4j.Logger;

import com.mbm.util.DateUtil;
import com.mbm.util.URLReader;
import com.weibo.common.GUI.verify.iface.IVerifyElement;

public class VerifyGuiUtil {
	private static Logger logger = Logger.getLogger(VerifyGuiUtil.class);

	// 当是关键词抓取的时候，要加当前线程中的抓取对象，主要是由于多线程抓取的原因
	// 登陆的时候，第2个参数可为null即可
	// 第3个参数timeStamp，是为keyword抓取时的时间截
	// 第4个参数count,是代表是第几次生成验证码

	public String getVerifyCode(String flag, IVerifyElement verifyElement,
			String timeStamp, int count) {
		if (flag.equalsIgnoreCase("login")) {
			// 如果是login时出现的验证码，已经下载过，无需在此下载
		} else if (flag.equalsIgnoreCase("keyword")) {
			
		}
		VerifyFrame verifyFrame = new VerifyFrame(flag, verifyElement);
		VerifyCodeRunnable verifyRunnable = new VerifyCodeRunnable(verifyFrame);
		Thread tt = new Thread(verifyRunnable);
		tt.start();
		try {
			tt.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("验证码输入完毕!");
		return verifyFrame.verifyCode;
	}

	public boolean needVerify(String source) {
		if (source == null) {
			return false;
		}
		return source.contains("验证码");
	}

	public static void main(String[] args) {
	}
}
