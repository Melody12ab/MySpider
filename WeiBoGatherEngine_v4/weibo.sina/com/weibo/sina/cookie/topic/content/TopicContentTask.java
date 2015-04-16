package com.weibo.sina.cookie.topic.content;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mbm.elint.entity.util.KeywordPojo;
import com.mbm.elint.entity.util.LoginPojo;
import com.mbm.elint.manager.business.spider.TopicTitleManager;
import com.weibo.common.utils.StaticValue;
import com.weibo.sina.cookie.common.GrabPageResource;

@Component
public class TopicContentTask implements Runnable {
	public static Logger logger = Logger.getLogger(TopicContentTask.class);
	@Autowired
	private GrabTopicContent grabTopicContent;

	private TopicTitleManager topicTitleManager;

	public TopicTitleManager getTopicTitleManager() {
		return topicTitleManager;
	}

	public void setTopicTitleManager(TopicTitleManager topicTitleManager) {
		this.topicTitleManager = topicTitleManager;
	}

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

	public TopicContentTask() {
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

	private int topicContentTaskId;

	public int getTopicContentTaskId() {
		return topicContentTaskId;
	}

	public void setTopicContentTaskId(int topicContentTaskId) {
		this.topicContentTaskId = topicContentTaskId;
	}

	public boolean init(int topicTaskId, TopicTitleManager topicTitleManager,
			String flag, LoginPojo loginPojo) {
		this.topicContentTaskId = topicTaskId;
		this.topicTitleManager = topicTitleManager;
		this.flag = flag;
		this.loginPojo = loginPojo;
		this.grabPageResource.init(loginPojo);
		this.grabTopicContent.init(grabPageResource);
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
					if (this.topicContentTaskId % 2 == 0) {
						this.keyWordPojo = this.getTopicTitleManager()
								.getOneCircleTopicTitle();
						if (this.keyWordPojo == null) {
							this.keyWordPojo = this.getTopicTitleManager()
									.getOneTopicTitle();
						}
					} else {
						this.keyWordPojo = this.getTopicTitleManager()
								.getOneTopicTitle();
					}
					if (keyWordPojo != null) {
						while (keyWordPojo.getRepeat_count() <= StaticValue.keyword_repeat_count) {
							try {
								this.grabTopicContent
										.grabTopicContentItems(this.keyWordPojo);
								/**
								 * 执行到这里，算是退出break
								 */
								break;
							} catch (Exception e) {
								e.printStackTrace();
								if (keyWordPojo.getRepeat_count() <= StaticValue.keyword_repeat_count) {
									keyWordPojo.setRepeat_count(keyWordPojo
											.getRepeat_count() + 1);
//									logger
//											.info(keyWordPojo
//													+ "---抓取出现问题,还未达到最大重复抓取次数，将再次加入抓取队列!");
									System.out.println(keyWordPojo
											+ "---抓取出现问题,还未达到最大重复抓取次数，将再次加入抓取队列!");
								}
							}
						}
						if (keyWordPojo.getType() == 1) {
							this.getTopicTitleManager()
									.removeVisitingTopicTitle(keyWordPojo);
						} else {
							this.getTopicTitleManager().addVisitedTopicTitle(
									keyWordPojo);
						}
					} else {
						try {
							Thread
									.sleep(StaticValue.keyword_or_cookie_null_task_waiting);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
//					logger.info("话题内容列表抓取出现问题!");
					System.out.println("话题内容列表抓取出现问题!");
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
