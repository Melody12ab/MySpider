package com.mbm.elint.manager.business;

import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.weibo.common.tasks.CircleTimerTask;
import com.weibo.common.utils.StaticValue;

@Component
public class TimerManager {
	private static Logger logger = LoggerFactory.getLogger(TimerManager.class);
	static {
		CircleTimerTask myTimerTask = new CircleTimerTask();
		Timer timer = new Timer();
		timer.schedule(myTimerTask, StaticValue.circle_task_later, StaticValue.monitor_list_circle_time);
		logger.info("循环任务定时器初始化完毕!");
	}
}
