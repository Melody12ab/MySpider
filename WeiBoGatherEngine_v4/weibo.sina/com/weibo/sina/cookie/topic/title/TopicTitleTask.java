package com.weibo.sina.cookie.topic.title;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mbm.elint.entity.util.KeywordPojo;
import com.mbm.elint.entity.util.LoginPojo;
import com.mbm.elint.manager.business.spider.TopicTitleManager;
import com.weibo.common.utils.StaticValue;
import com.weibo.sina.cookie.common.GrabPageResource;

@Component
public class TopicTitleTask implements Runnable {
	public static Logger logger = Logger.getLogger(TopicTitleTask.class);
	@Autowired
	private GrabTopicTitle grabTopicTitle;
	/**
	 * flag=1 可以运行或正在运行 flag=2 暂停运行，正在试图切换用户
	 */
	private String flag;

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	private LoginPojo loginPojo;

	public LoginPojo getLoginPojo() {
		return loginPojo;
	}

	public void setLoginPojo(LoginPojo loginPojo) {
		this.loginPojo = loginPojo;
	}

	private String cookieStr;
	@Autowired
	private GrabPageResource grabPageResource;

	public TopicTitleTask() {
	}

	public String getCookieStr() {
		return cookieStr;
	}

	public void setCookieStr(String cookieStr) {
		this.cookieStr = cookieStr;
	}

	private KeywordPojo keyWordPojo;

	public KeywordPojo getKeyWordPojo() {
		return keyWordPojo;
	}

	public void setKeyWordPojo(KeywordPojo keyWordPojo) {
		this.keyWordPojo = keyWordPojo;
	}

	private int topicTaskId;

	public int getTopicTaskId() {
		return topicTaskId;
	}

	public void setTopicTaskId(int topicTaskId) {
		this.topicTaskId = topicTaskId;
	}

	public boolean init(int topicTaskId, TopicTitleManager topicTitleManager, String flag,
			LoginPojo loginPojo) {
		this.topicTaskId = topicTaskId;
		this.flag = flag;
		this.loginPojo = loginPojo;
		this.grabPageResource.init(loginPojo);
		this.grabTopicTitle.init(grabPageResource,topicTitleManager);
		return true;
	}

	public void reInit(LoginPojo loginPojo) {
		this.grabPageResource.init(loginPojo);
	}

	private boolean isRunning = true;

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public void run() {
		while (isRunning) {
			if (flag.equals("1")) {
				try {
					this.grabTopicTitle.grabTopicTitleItems();
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("话题标题列表抓取出现问题!");
				}
				try {
					// 多长时间抓一次话题列表
					Thread.sleep(StaticValue.topic_interval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				try {
					Thread.sleep(StaticValue.switch_user_thread_sleep_time);
				} catch (InterruptedException e) {
					e.printStackTrace();
					logger.info("试图切换用户时被打断，将重新尝试切换用户!");
					continue;
				}
			}

		}
	}

}
