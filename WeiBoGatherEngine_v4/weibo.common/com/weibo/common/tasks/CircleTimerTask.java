package com.weibo.common.tasks;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.mbm.elint.entity.util.KeywordPojo;
import com.mbm.elint.entity.util.TypePojo;
import com.mbm.elint.entity.util.UrlPojo;
import com.mbm.elint.manager.business.CircleTaskUrlManager;
import com.mbm.elint.manager.business.spider.KeywordManager;
import com.mbm.elint.manager.business.spider.UrlsManager;
import com.mbm.util.DateUtil;
import com.weibo.common.utils.StaticValue;

public class CircleTimerTask extends TimerTask {
	public static Logger logger = Logger.getLogger(CircleTimerTask.class);

	/**
	 * 该任务用于定时循环，并将到了该循环抓取时间的urlPojo加入到抓取队列中去
	 */
	@Override
	public void run() {
		logger.info("开始执行周期任务!");

		logger.info("现在的circleUrls size-----------"
				+ CircleTaskUrlManager.circleUrls.size());
		logger.info("现在的circle_keywords size-----------"
				+ KeywordManager.visitedKeyWords.size());
		for (UrlPojo urlPojo : CircleTaskUrlManager.circleUrls) {
			if (DateUtil.getLongByDate() >= urlPojo.getReGrabBeginTime()) {
				if(TypePojo.isQQ(urlPojo.getUrl())){
					UrlsManager.addToVisitCircleUrl4QQ(urlPojo);	
				}else {
					UrlsManager.addToVisitCircleUrl(urlPojo);
				}
				/**
				 * 因为共享一个对象，取完后改变时间，再放回去
				 */
				if (urlPojo.getType() == 4 || urlPojo.getType() == 5) {
					urlPojo.setReGrabBeginTime(urlPojo.getReGrabBeginTime()
							+ DateUtil.getTimeByLevel(urlPojo.getTaskLevel()));
				} else if (urlPojo.getType() == 12 || urlPojo.getType() == 13) {
					urlPojo.setReGrabBeginTime(urlPojo.getReGrabBeginTime()
							+ DateUtil.getTimeByLevel(urlPojo.getTaskLevel()));
				} else if (urlPojo.getType() == 16 || urlPojo.getType() == 17) {
					urlPojo.setReGrabBeginTime(urlPojo.getReGrabBeginTime()
							+ DateUtil.getTimeByLevel(urlPojo.getTaskLevel()));
				} else if (urlPojo.getType() == 21) {
					urlPojo.setReGrabBeginTime(urlPojo.getReGrabBeginTime()
							+ DateUtil.getTimeByLevel(urlPojo.getTaskLevel()));
				} else {
					urlPojo.setReGrabBeginTime(urlPojo.getReGrabBeginTime()
							+ DateUtil.getTimeByLevel(urlPojo.getTaskLevel()));
				}
			}
		}

		for (KeywordPojo keyPojo : KeywordManager.visitedKeyWords) {
			if (DateUtil.getLongByDate() >= keyPojo.getReGrabBeginTime()) {
				// KeywordManager.addOneKeyWord(keyPojo);
				KeywordManager.addOneCircleKeyWord(keyPojo);
				KeywordManager.addOneCircleKeyWord4QQ(keyPojo);
				/**
				 * 因为共享一个对象，取完后改变时间，再放回去
				 */
				keyPojo.setReGrabBeginTime(keyPojo.getReGrabBeginTime()
						+ StaticValue.keyword_task_circle_time);
			}
		}

	}

}
