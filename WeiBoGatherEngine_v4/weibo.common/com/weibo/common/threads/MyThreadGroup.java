package com.weibo.common.threads;

import org.apache.log4j.Logger;

import com.weibo.common.utils.FormatOutput;

public class MyThreadGroup extends ThreadGroup {
	public static Logger logger = Logger.getLogger(MyThreadGroup.class);
	public MyThreadGroup(String name) {
		super(name);
		logger.info(FormatOutput.threadPrefix + " 线程组初始化完成");
	}
}
