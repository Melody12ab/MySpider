package com.weibo.common.threads;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.mbm.elint.entity.util.LoginPojo;
import com.mbm.elint.entity.util.TypePojo;
import com.mbm.elint.manager.business.CookieTaskManager;
import com.mbm.elint.manager.business.CookieTaskManager4QQ;
import com.mbm.elint.manager.business.KeySearchTaskManager;
import com.mbm.elint.manager.business.KeySearchTaskManager4QQ;
import com.mbm.elint.manager.business.TopicContentTaskManager;
import com.mbm.elint.manager.business.TopicTitleTaskManager;
import com.weibo.common.controler.SpiderControler;
import com.weibo.common.utils.StaticValue;
import com.weibo.qq.cookie.common.CookieTask4QQ;
import com.weibo.qq.cookie.keywords.KeySearchTask4QQ;
import com.weibo.sina.cookie.common.CookieTask;
import com.weibo.sina.cookie.keywords.KeySearchTask;
import com.weibo.sina.cookie.topic.content.TopicContentTask;
import com.weibo.sina.cookie.topic.title.TopicTitleTask;

public class SwitchCookieUserDaemonThread implements Runnable {
	public static Logger logger = Logger
			.getLogger(SwitchCookieUserDaemonThread.class);
	private LinkedList<LoginPojo> all_user_list;
	private SpiderControler spiderControler;

	private CookieTaskManager cookieTaskManager;
	private KeySearchTaskManager keySearchTaskManager;
	// for topic
	private TopicTitleTaskManager topicTitleTaskManager;
	private TopicContentTaskManager topicContentTaskManager;

	// for qq
	private CookieTaskManager4QQ cookieTaskManager4QQ;
	private KeySearchTaskManager4QQ keySearchTaskManager4QQ;

	private String platformType = null;

	public SwitchCookieUserDaemonThread(SpiderControler spiderControler,
			CookieTaskManager cookieTaskManager,
			KeySearchTaskManager keySearchTaskManager,
			TopicTitleTaskManager topicTitleTaskManager,
			TopicContentTaskManager topicContentTaskManager, String platformType) {
		this.spiderControler = spiderControler;
		this.cookieTaskManager = cookieTaskManager;
		this.keySearchTaskManager = keySearchTaskManager;
		this.topicTitleTaskManager = topicTitleTaskManager;
		this.topicContentTaskManager = topicContentTaskManager;
		this.platformType = platformType;
	}

	public SwitchCookieUserDaemonThread(SpiderControler spiderControler,
			CookieTaskManager4QQ cookieTaskManager4QQ,
			KeySearchTaskManager4QQ keySearchTaskManager4QQ, String platformType) {
		this.spiderControler = spiderControler;
		this.cookieTaskManager4QQ = cookieTaskManager4QQ;
		this.keySearchTaskManager4QQ = keySearchTaskManager4QQ;
		this.platformType = platformType;
	}

	private boolean isRunning = true;

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	private String seed_account_type = "";

	public void run() {
		while (isRunning) {
			int switch_count = 0;
			try {
				if (TypePojo.SINA.equals(platformType)) {
					seed_account_type = "1";
					Thread.sleep(StaticValue.sina_switch_account_interval_time);
				} else if (TypePojo.QQ.equals(platformType)) {
					seed_account_type = "2";
					Thread.sleep(StaticValue.qq_switch_account_interval_time);
				}
			} catch (InterruptedException e) {
				// e.printStackTrace();
				logger.info("切换线程在睡的时候被打断了一次,将提前切换帐户!");
			}
			if (TypePojo.SINA.equals(platformType)) {
				logger.info("开始切换SINA帐户");
			} else if (TypePojo.QQ.equals(platformType)) {
				logger.info("开始切换QQ帐户");
			}
			LoginPojo loginPojo = null;
			if (this.all_user_list == null || this.all_user_list.size() == 0) {
				this.all_user_list = this.spiderControler
						.reGetLoginPojoList(seed_account_type);
				if (this.all_user_list.size() == 0) {
					logger.info("没有可用的种子帐户，请检查!");
					break;
				}
			}
			while (this.all_user_list.size() > 0) {
				loginPojo = this.all_user_list.pollFirst();
				// 解决多个帐号循环模拟登陆时，在正常第一轮循环过后，从第二轮开始只会使用最小id所属于种子帐号的问题
				if (this.all_user_list.size() == 0) {
					this.all_user_list = this.spiderControler
							.reGetLoginPojoList(seed_account_type);
					logger.info("重新load种子帐号集合!");
				}
				platformType = loginPojo.getType();// 得到平台类型
				loginPojo = SpiderControler.getCookieForSwitch(loginPojo, 1);

				if (loginPojo.getCookie() == null) {
					if (TypePojo.SINA.equals(platformType)) {
						logger.info("Sina--切换用户时得到的cookie为null,等待下一个轮回");
					} else if (TypePojo.QQ.equals(platformType)) {
						logger.info("QQ--切换用户时得到的cookie为null,等待下一个轮回");
					}
					switch_count++;
					/**
					 * 此时说明，该次切换用户时登陆未成功
					 */
					try {
						Thread.sleep(switch_count
								* StaticValue.switch_user_once_fail_sleep_time);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				} else if (loginPojo.getCookie() != null
						&& loginPojo.getCookie().trim().startsWith("deleted")) {
					if (TypePojo.SINA.equals(platformType)) {
						logger.info("Sina  切换用户时得到的cookie为"
								+ loginPojo.getCookie() + ",等待下一个轮回");
					} else if (TypePojo.QQ.equals(platformType)) {
						logger.info("QQ  切换用户时得到的cookie为"
								+ loginPojo.getCookie() + ",等待下一个轮回");
					}
					switch_count++;
					/**
					 * 此时说明，该次切换用户时登陆未成功
					 */
					try {
						Thread.sleep(switch_count
								* StaticValue.switch_user_once_fail_sleep_time);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				} else {
					if (TypePojo.SINA.equals(platformType)) {
						logger.info("Sina 切换用户后，现在抓取帐号、关注信息，正在使用的帐户是----------"
								+ loginPojo.getUsername());
						logger
								.info("Sina 正在试图切换CookieTask任务的用户------------------------");
						SpiderControler.isRunningAccountUid4Sina = loginPojo
								.getUid();
					} else if (TypePojo.QQ.equals(platformType)) {
						logger.info("QQ 切换用户后，现在抓取帐号、关注信息，正在使用的帐户是----------"
								+ loginPojo.getUsername());
						logger
								.info("QQ 正在试图切换CookieTask任务的用户------------------------");
						SpiderControler.isRunningAccountUid4QQ = loginPojo
								.getUid();
					}
					if (TypePojo.SINA.equals(platformType)) {
						for (CookieTask cookieTask : cookieTaskManager
								.getCookieTasks()) {
							logger.info("SINA cookie抓取--"
									+ cookieTask.getCookieId()
									+ "正在试图切换正在使用的用户------------------------");
							try {
								cookieTask.setFlag("2");
								/**
								 * 更新cookieTask的cookie参数，从而切换user
								 */
								cookieTask.reInit(loginPojo);
								logger.info("cookie抓取--"
										+ cookieTask.getCookieId()
										+ "成功切换用户------------------------");
							} catch (Exception e) {
								e.printStackTrace();
								logger.info("cookie抓取--"
										+ cookieTask.getCookieId()
										+ "切换用户失败------------------------");
							} finally {
								cookieTask.setFlag("1");
							}
						}
						logger
								.info("SINA 完成切换CookieTask任务的用户------------------------");
						if (StaticValue.keyword_single_switch_enable) {
							logger.info("关键词抓取的cookies设置为永不更换!");
							break;
						} else {
							logger
									.info("Sina  正在试图切换KeySearchTask任务的用户------------------------");
							for (KeySearchTask keySearchTask : keySearchTaskManager
									.getKeySearchTasks()) {
								try {
									keySearchTask.setFlag("2");
									/**
									 * 更新cookieTask的cookie参数，从而切换user
									 */
									keySearchTask.reInit(loginPojo);
									logger.info("SINA keySearch抓取--"
											+ keySearchTask
													.getKeySearchTaskId()
											+ "成功切换用户------------------------");
								} catch (Exception e) {
									e.printStackTrace();
									logger.info("SINA keySearch抓取--"
											+ keySearchTask
													.getKeySearchTaskId()
											+ "切换用户失败------------------------");
								} finally {
									keySearchTask.setFlag("1");
								}
							}
							logger
									.info("Sina 完成切换KeySearchTask任务的用户------------------------");
						}
						// for topic title
						if (StaticValue.topic_title_single_user_switch_enable) {
							logger.info("话题抓取的cookies设置为不单独更换，而是随登陆用户的更改而更改!");
							break;
						} else {
							logger
									.info("Sina  正在试图切换TopicTitleTask任务的用户------------------------");
							for (TopicTitleTask topicTask : topicTitleTaskManager
									.getTopicTitleTasks()) {
								try {
									topicTask.setFlag("2");
									/**
									 * 更新cookieTask的cookie参数，从而切换user
									 */
									topicTask.reInit(loginPojo);
									logger.info("SINA TopicTitleTask抓取--"
											+ topicTask.getTopicTaskId()
											+ "成功切换用户------------------------");
								} catch (Exception e) {
									e.printStackTrace();
									logger.info("SINA TopicTitleTask抓取--"
											+ topicTask.getTopicTaskId()
											+ "切换用户失败------------------------");
								} finally {
									topicTask.setFlag("1");
								}
							}
							logger
									.info("Sina 完成切换TopicTitleTask任务的用户------------------------");
						}
						// for topic content
						if (StaticValue.topic_content_single_user_switch_enable) {
							logger.info("话题抓取的cookies设置为不单独更换，而是随登陆用户的更改而更改!");
							break;
						} else {
							logger
									.info("Sina  正在试图切换TopicContentTask任务的用户------------------------");
							for (TopicContentTask topicContentTask : topicContentTaskManager
									.getTopicContentTasks()) {
								try {
									topicContentTask.setFlag("2");
									/**
									 * 更新cookieTask的cookie参数，从而切换user
									 */
									topicContentTask.reInit(loginPojo);
									logger.info("SINA TopicContentTask抓取--"
											+ topicContentTask
													.getTopicContentTaskId()
											+ "成功切换用户------------------------");
								} catch (Exception e) {
									e.printStackTrace();
									logger.info("SINA TopicContentTask抓取--"
											+ topicContentTask
													.getTopicContentTaskId()
											+ "切换用户失败------------------------");
								} finally {
									topicContentTask.setFlag("1");
								}
							}
							logger
									.info("Sina 完成切换TopicContentTask任务的用户------------------------");
							break;
						}

					} else if (TypePojo.QQ.equals(platformType)) {
						for (CookieTask4QQ cookieTask : cookieTaskManager4QQ
								.getCookieTasks()) {
							logger.info("QQ cookie抓取--"
									+ cookieTask.getCookieId()
									+ "正在试图切换正在使用的用户------------------------");
							try {
								cookieTask.setFlag("2");
								/**
								 * 更新cookieTask的cookie参数，从而切换user
								 */
								cookieTask.reInit(loginPojo);
								logger.info("QQ cookie抓取--"
										+ cookieTask.getCookieId()
										+ "成功切换用户------------------------");
							} catch (Exception e) {
								e.printStackTrace();
								logger.info("QQ  cookie抓取--"
										+ cookieTask.getCookieId()
										+ "切换用户失败------------------------");
							} finally {
								cookieTask.setFlag("1");
							}
						}
						logger
								.info("SINA 完成切换CookieTask任务的用户------------------------");
						if (StaticValue.keyword_single_switch_enable) {
							break;
						} else {
							logger
									.info("QQ  正在试图切换KeySearchTask任务的用户------------------------");
							for (KeySearchTask4QQ keySearchTask : keySearchTaskManager4QQ
									.getKeySearchTasks()) {
								try {
									keySearchTask.setFlag("2");
									/**
									 * 更新cookieTask的cookie参数，从而切换user
									 */
									keySearchTask.reInit(loginPojo);
									logger.info("QQ keySearch抓取--"
											+ keySearchTask
													.getKeySearchTaskId()
											+ "成功切换用户------------------------");
								} catch (Exception e) {
									e.printStackTrace();
									logger.info("QQ keySearch抓取--"
											+ keySearchTask
													.getKeySearchTaskId()
											+ "切换用户失败------------------------");
								} finally {
									keySearchTask.setFlag("1");
								}
							}
							logger
									.info("QQ 完成切换KeySearchTask任务的用户------------------------");
							// break;
						}
					}
				}
				// 无论如何,都要做一次break
				break;
			}
		}
		logger.info("用户切换线程终止了，请检查!");
	}
}
