package com.weibo.common.controler;

import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mbm.elint.entity.util.LoginPojo;
import com.mbm.elint.entity.util.ProxyPojo;
import com.mbm.elint.entity.util.TypePojo;
import com.mbm.elint.manager.business.CookieTaskManager;
import com.mbm.elint.manager.business.CookieTaskManager4QQ;
import com.mbm.elint.manager.business.KeySearchTaskManager;
import com.mbm.elint.manager.business.KeySearchTaskManager4QQ;
import com.mbm.elint.manager.business.TopicContentTaskManager;
import com.mbm.elint.manager.business.TopicTitleTaskManager;
import com.mbm.elint.manager.business.spider.KeywordManager;
import com.mbm.elint.manager.business.spider.TopicTitleManager;
import com.mbm.elint.manager.business.spider.UrlsManager;
import com.mbm.elint.manager.database.LoginPojoManager;
import com.weibo.common.manager.SwtichUserThreadManager;
import com.weibo.common.threads.DaemonThread;
import com.weibo.common.threads.MyThread;
import com.weibo.common.threads.MyThreadGroup;
import com.weibo.common.threads.SwitchCookieUserDaemonThread;
import com.weibo.common.threads.SwitchSearchUserDaemonThread;
import com.weibo.common.utils.StaticValue;
import com.weibo.qq.cookie.common.CookieTask4QQ;
import com.weibo.qq.cookie.keywords.KeySearchTask4QQ;
import com.weibo.sina.cookie.common.CookieTask;
import com.weibo.sina.cookie.keywords.KeySearchTask;
import com.weibo.sina.cookie.topic.content.TopicContentTask;
import com.weibo.sina.cookie.topic.title.TopicTitleTask;
import com.weibo.utils.qq.login.GetUserCookie4QQ;
import com.weibo.utils.sina.GetUserCookie;

@Component
public class SpiderControler {
	public static Logger logger = Logger.getLogger(SpiderControler.class);
	public static int engineNumber = 0;
	@Autowired
	private LoginPojoManager loginPojoManager;
	@Autowired
	private UrlsManager urlsManager;
	@Autowired
	private KeywordManager keyWordManager;
	@Autowired
	private CookieTaskManager cookieTaskManager;
	@Autowired
	private CookieTaskManager4QQ cookieTaskManager4QQ;
	@Autowired
	private KeySearchTaskManager keySearchTaskManager;
	@Autowired
	private KeySearchTaskManager4QQ keySearchTaskManager4QQ;
	// 话题抓取的task
	@Autowired
	private TopicTitleTaskManager topicTitleTaskManager;
	@Autowired
	private TopicContentTaskManager topicContentTaskManager;

	private LinkedList<LoginPojo> all_user_list = null;
	public static String isRunningAccountUid4Sina = null;
	// public static String isRunningSearchUid4Sina = null;

	public static String isRunningAccountUid4QQ = null;
	// public static String isRunningSearchUid4QQ = null;

	@Autowired
	private TopicTitleManager topicTitleManager;

	public LoginPojo currentSinaPojo = null;
	public LoginPojo currentQQPojo = null;

	public static LoginPojo getCookieForSwitch(LoginPojo loginPojo, int flag) {
		GetUserCookie userCookie = null;
		GetUserCookie4QQ userCookie4QQ = null;
		String cookieStr = null;
		String platformType = loginPojo.getType();
		/**
		 * 不管是否正常，到切换周期时，一律切换,flag=1代表帐号、关注信息的抓取切换，flag=2代表元搜索的切换
		 */
		try {
			String temp_IE_Version = StaticValue.ieList.pollFirst();
			if (flag == 1) {// 切换用户，暂忽略关键词用户切换
				if (TypePojo.SINA.equals(platformType)) {
					// 判断是否启动代理
					if (StaticValue.proxy_open) {

					} else {
						userCookie = new GetUserCookie(loginPojo);
						cookieStr = userCookie.getCookies(temp_IE_Version);
					}
					// 取得正在运行帐户、内容抓取的uid
					isRunningAccountUid4Sina = loginPojo.getUid();
					loginPojo.setHttpClient(userCookie.getClient());
				} else if (TypePojo.QQ.equals(platformType)) {
					userCookie4QQ = new GetUserCookie4QQ(loginPojo);
					cookieStr = userCookie4QQ.getCookies();
					// 取得正在运行帐户、内容抓取的uid
					isRunningAccountUid4QQ = loginPojo.getUid();
					loginPojo.setHttpClient(userCookie4QQ.getClient());
				}
			}
			// 暂注释关键词抓取的切换
			// else if (flag == 2) {
			// userCookie = new GetUserCookie(loginPojo);
			// cookieStr = userCookie.getCookies(temp_IE_Version);
			// // 取得正在运行帐户、内容抓取的uid
			// isRunningAccountUid4Sina = loginPojo.getUid();
			// }
			StaticValue.ieList.add(temp_IE_Version);

			loginPojo.setCookie(cookieStr);
			loginPojo.setStatus("2");
		} catch (Exception e) {
			e.printStackTrace();
			loginPojo.setCookie(null);
			loginPojo.setStatus("1");
		}
		return loginPojo;
	}

	public int online_count = 0;

	public LoginPojo loadRunningPojo(String platformType) {
		online_count = 0;// 谁登陆时先还原一下
		all_user_list = loginPojoManager.getLoginPojoList(platformType);// 所有可用帐户
		LoginPojo loginPojo = null;
		if (TypePojo.SINA.equals(platformType)) {
			GetUserCookie userCookie = null;
			String cookieStr = null;
			/**
			 * 取得数据库中，共有多少个帐户
			 */
			int all_account_size = all_user_list.size();
			while ((loginPojo = all_user_list.pollFirst()) != null) {
				try {
					String temp_IE_Version = StaticValue.ieList.pollFirst();
					if (online_count < 1) {
						// 判断是否启动代理
						if (StaticValue.proxy_open) {
							ProxyPojo proxyPojo = StaticValue.proxyList.get(0);
							userCookie = new GetUserCookie(loginPojo, proxyPojo);
						} else {
							userCookie = new GetUserCookie(loginPojo);
						}
						cookieStr = userCookie.getCookies(temp_IE_Version);
						// 取得正在运行帐户、内容抓取的uid
						isRunningAccountUid4Sina = loginPojo.getUid();
					}
					StaticValue.ieList.add(temp_IE_Version);
					loginPojo.setCookie(cookieStr);
					loginPojo.setHttpClient(userCookie.getClient());
					loginPojo.setStatus("2");
					// 代表它是登陆成功了
					online_count++;
				} catch (Exception e) {
					e.printStackTrace();
					loginPojo = null;
					continue;
				}
				if (online_count >= StaticValue.max_api_user_online_number) {
					break;
				}
			}
			logger.info("sina---数据库中共有" + all_account_size + "个帐户可用,根据系统设置开启了"
					+ online_count + "个帐户");
		} else if (TypePojo.QQ.equals(platformType)) {
			GetUserCookie4QQ userCookie4QQ = null;
			String cookieStr = null;
			/**
			 * 取得数据库中，共有多少个帐户
			 */
			int all_account_size = all_user_list.size();
			while ((loginPojo = all_user_list.pollFirst()) != null) {
				try {
					if (online_count < 1) {
						userCookie4QQ = new GetUserCookie4QQ(loginPojo);
						cookieStr = userCookie4QQ.getCookies();
						if (cookieStr == null) {// 说明登陆失败，再登一次
							continue;
						}
						// 取得正在运行帐户、内容抓取的uid
						isRunningAccountUid4QQ = loginPojo.getUid();
					}
					loginPojo.setCookie(cookieStr);
					loginPojo.setHttpClient(userCookie4QQ.getClient());
					loginPojo.setStatus("2");
					// 代表它是登陆成功了
					online_count++;
				} catch (Exception e) {
					e.printStackTrace();
					loginPojo = null;
					continue;
				}
				if (online_count >= StaticValue.max_api_user_online_number) {
					break;
				}
			}
			logger.info("qq---数据库中共有" + all_account_size + "个帐户可用,根据系统设置开启了"
					+ online_count + "个帐户");
		}
		return loginPojo;
	}

	public LinkedList<LoginPojo> reGetLoginPojoList(String type) {
		if (this.all_user_list == null || this.all_user_list.size() == 0) {
			return loginPojoManager.getLoginPojoList(type);
		} else {
			return this.all_user_list;
		}
	}

	private SwitchCookieUserDaemonThread switch_account_daemon = null;
	private SwitchSearchUserDaemonThread switch_search_daemon = null;

	private DaemonThread daemon = null;

	public boolean startSpider() throws Exception {
		if (StaticValue.isPausing) {
			this.continueSpider();
			return true;
		}
		MyThreadGroup threadGroup = new MyThreadGroup("线程组");// 线程组初始化
		/**
		 * 首先判断平台是否开启
		 */
		if (StaticValue.sina_spider_enable) {
			logger.info("开始启动Sina抓取程序");
			currentSinaPojo = loadRunningPojo(TypePojo.SINA);
			logger.info("Sina Account加载完毕-------------------------------");
			// 守护线程初始化
			daemon = new DaemonThread(threadGroup, "1", urlsManager);
			MyThread daemonThread = new MyThread(daemon, "守护线程");
			daemonThread.start();
			int cookie_threads_number = 1;
			int keyword_threads_number = 1;
			int topic_title_threads_number = 1;
			int topic_content_threads_number = 1;
			if ((online_count >= 1)
					|| online_count >= StaticValue.max_api_user_online_number) {
				// loginPojo = isRunningUserList.pollFirst();
				logger.info("Sina  现在抓取帐号、关注信息，正在使用的帐户是："
						+ currentSinaPojo.getUsername());
				isRunningAccountUid4Sina = currentSinaPojo.getUid();
				if (StaticValue.cookie_spider_enable) {
					logger.info("Sina cookie抓取方式已启用");
					/**
					 * 取得cookieTaskManager中的每个CookieTask
					 */
					ArrayList<CookieTask> cookieTasks = cookieTaskManager
							.getCookieTasks();
					for (CookieTask task : cookieTasks) {
						engineNumber++;
						MyThread cookieThread = new MyThread(threadGroup, task,
								"cookie抓取--" + cookie_threads_number);
						// 用cookie登陆sina代码块
						if (task.init(cookie_threads_number, urlsManager, "1",
								currentSinaPojo)) {
							cookieThread.start();
						}
						cookie_threads_number++;
					}
				} else {
					logger.info("Sina cookie抓取方式已禁用");
				}
				// 话题 标题的抓取
				if (StaticValue.topic_title_spider_enable) {
					logger.info("topic_title抓取方式已启用");
					/**
					 * 取得keySearchTaskManager中的每个keySearchTask
					 */
					ArrayList<TopicTitleTask> topicTitleTasks = topicTitleTaskManager
							.getTopicTitleTasks();
					for (TopicTitleTask topicTask : topicTitleTasks) {
						engineNumber++;
						MyThread keyTaskThread = new MyThread(threadGroup,
								topicTask, "TopicTitleTask抓取--"
										+ (topic_title_threads_number));
						// 用cookie登陆sina代码块
						if (topicTask.init(topic_title_threads_number,
								this.topicTitleManager, "1", currentSinaPojo)) {
							keyTaskThread.start();
						}
						topic_title_threads_number++;
					}
				} else {
					logger.info("topic_title话题列表抓取方式已禁用");
				}

				// 话题对应的内容列表的抓取
				if (StaticValue.topic_content_spider_enable) {
					logger.info("topic_content抓取方式已启用");
					/**
					 * 取得keySearchTaskManager中的每个keySearchTask
					 */
					ArrayList<TopicContentTask> topicContentTasks = topicContentTaskManager
							.getTopicContentTasks();
					for (TopicContentTask topicContentTask : topicContentTasks) {
						engineNumber++;
						MyThread keyTaskThread = new MyThread(threadGroup,
								topicContentTask, "TopicContentTask抓取--"
										+ (topic_content_threads_number));
						// 用cookie登陆sina代码块
						if (topicContentTask.init(topic_content_threads_number,
								this.topicTitleManager, "1", currentSinaPojo)) {
							keyTaskThread.start();
						}
						topic_content_threads_number++;
					}
				} else {
					logger.info("topic_content话题列表抓取方式已禁用");
				}

				// 现在是统一用一个帐户，不做以下的输出
				// logger.info("现在是元搜索，正在使用的帐户是：" +
				// currentSinaPojo.getUsername());
				if (StaticValue.keyword_spider_enable) {
					logger.info("keyword抓取方式已启用");
					/**
					 * 取得keySearchTaskManager中的每个keySearchTask
					 */
					ArrayList<KeySearchTask> keySearchTasks = keySearchTaskManager
							.getKeySearchTasks();
					for (KeySearchTask keyTask : keySearchTasks) {
						engineNumber++;
						MyThread keyTaskThread = new MyThread(threadGroup,
								keyTask, "KeyWords抓取--"
										+ (keyword_threads_number));
						// 用cookie登陆sina代码块
						if (keyTask.init(keyword_threads_number,
								this.urlsManager, keyWordManager, "1",
								currentSinaPojo)) {
							keyTaskThread.start();
						}
						keyword_threads_number++;
					}
				} else {
					logger.info("Sina KeyWords抓取方式已禁用");
				}
				// 开启切换用户的线程，切换一个之后，关键词和非关键词抓取共用一个cookies
				switch_account_daemon = new SwitchCookieUserDaemonThread(this,
						cookieTaskManager, keySearchTaskManager,
						topicTitleTaskManager, topicContentTaskManager,
						TypePojo.SINA);
				MyThread switch_user_thread = new MyThread(
						switch_account_daemon, "切换 sina account用户");
				switch_user_thread.start();
				
				SwtichUserThreadManager.setSwitchThread(switch_user_thread);
			}
		} else {
			logger.info("新浪微博平台抓取已禁用!");
		}
		if (StaticValue.qq_spider_enable) {
			logger.info("开始启动QQ抓取程序");
			currentQQPojo = loadRunningPojo(TypePojo.QQ);
			logger.info("QQ account加载完毕-------------------------------");

			// 守护线程初始化
			// 如果已经在新浪抓取的时候初始化，此次将不再初始化
			if (daemon == null) {
				daemon = new DaemonThread(threadGroup, "1", urlsManager);
				MyThread daemonThread = new MyThread(daemon, "守护线程");
				daemonThread.start();
			}
			int cookie_threads_number = 1;
			int keyword_threads_number = 1;
			if ((online_count >= 1)
					|| online_count >= StaticValue.max_api_user_online_number) {
				logger.info("QQ  现在抓取帐号、关注信息，正在使用的帐户是："
						+ currentQQPojo.getUsername());
				// logger.info("currentQQPojo---"+currentQQPojo);
				isRunningAccountUid4QQ = currentQQPojo.getUid();
				if (StaticValue.cookie_spider_enable) {
					logger.info("QQ cookie抓取方式已启用");
					/**
					 * 取得cookieTaskManager中的每个CookieTask
					 */
					ArrayList<CookieTask4QQ> cookieTasks = cookieTaskManager4QQ
							.getCookieTasks();
					for (CookieTask4QQ task : cookieTasks) {
						engineNumber++;
						MyThread cookieThread = new MyThread(threadGroup, task,
								"cookie抓取--" + cookie_threads_number);
						// 用cookie登陆sina代码块
						if (task.init(cookie_threads_number, urlsManager, "1",
								currentQQPojo)) {
							cookieThread.start();
						}
						cookie_threads_number++;
					}
				} else {
					logger.info("QQ cookie抓取方式已禁用");
				}
				// 现在是统一用一个帐户，不做以下的输出
				// logger.info("现在是元搜索，正在使用的帐户是：" +
				// currentSinaPojo.getUsername());
				if (StaticValue.keyword_spider_enable) {
					logger.info("qq keyword抓取方式已启用");
					/**
					 * 取得keySearchTaskManager中的每个keySearchTask
					 */
					ArrayList<KeySearchTask4QQ> keySearchTasks = keySearchTaskManager4QQ
							.getKeySearchTasks();
					for (KeySearchTask4QQ keyTask : keySearchTasks) {
						engineNumber++;
						MyThread keyTaskThread = new MyThread(threadGroup,
								keyTask, "qq KeyWords抓取--"
										+ (keyword_threads_number));
						// 用cookie登陆sina代码块
						if (keyTask.init(keyword_threads_number,
								this.urlsManager, keyWordManager, "1",
								currentQQPojo)) {
							keyTaskThread.start();
						}
						keyword_threads_number++;
					}
				} else {
					logger.info("qq KeyWords抓取方式已禁用");
				}
				// 开启切换用户的线程，切换一个之后，关键词和非关键词抓取共用一个cookies
				switch_account_daemon = new SwitchCookieUserDaemonThread(this,
						cookieTaskManager4QQ, keySearchTaskManager4QQ,
						TypePojo.QQ);
				MyThread switch_user_thread = new MyThread(
						switch_account_daemon, "切换 qq account用户");
				switch_user_thread.start();
			}
		}

		if (engineNumber == 0) {
			logger.info("无抓取程序启动!");
			StaticValue.isRunning = false;
			// System.exit(0);// 当无用户登陆开始抓取时，不再关闭tomcat
		} else {
			TaskControler.urlsManager = urlsManager;
			TaskControler.keyWordManager = keyWordManager;
			TaskControler.topicTitleManager = topicTitleManager;
			StaticValue.isRunning = true;
			logger.info("抓取程序已启动完成!");
		}
		return true;
	}

	public void continueSpider() {
		if (StaticValue.isPausing) {
			for (CookieTask task : this.cookieTaskManager.getCookieTasks()) {
				// task.setRunning(false);
				task.setFlag("1");
			}

			for (KeySearchTask keyTask : this.keySearchTaskManager
					.getKeySearchTasks()) {
				// keyTask.setRunning(false);
				keyTask.setFlag("1");
			}

			if (topicTitleTaskManager.getTopicTitleTasks() != null) {
				for (TopicTitleTask titleTask : topicTitleTaskManager
						.getTopicTitleTasks()) {
					// titleTask.setRunning(false);
					titleTask.setFlag("1");
				}
			}

			if (topicContentTaskManager.getTopicContentTasks() != null) {
				for (TopicContentTask contentTask : topicContentTaskManager
						.getTopicContentTasks()) {
					// contentTask.setRunning(false);
					contentTask.setFlag("1");
				}
			}
			StaticValue.isPausing = false;
			StaticValue.isRunning = true;
		} else {
			logger.info("不在暂停过程中，请检查!");
		}
	}

	/**
	 * 将任务线程暂停掉，而非完全退出!
	 * 
	 * @return
	 */
	public boolean stopSpider() {
		if (StaticValue.isRunning) {
			// this.daemon.setRunning(false);
			// this.switch_account_daemon.setRunning(false);
			// this.switch_search_daemon.setRunning(false);

			for (CookieTask task : this.cookieTaskManager.getCookieTasks()) {
				// task.setRunning(false);
				task.setFlag("2");
			}

			for (KeySearchTask keyTask : this.keySearchTaskManager
					.getKeySearchTasks()) {
				// keyTask.setRunning(false);
				keyTask.setFlag("2");
			}

			if (topicTitleTaskManager.getTopicTitleTasks() != null) {
				for (TopicTitleTask titleTask : topicTitleTaskManager
						.getTopicTitleTasks()) {
					// titleTask.setRunning(false);
					titleTask.setFlag("2");
				}
			}

			if (topicContentTaskManager.getTopicContentTasks() != null) {
				for (TopicContentTask contentTask : topicContentTaskManager
						.getTopicContentTasks()) {
					// contentTask.setRunning(false);
					contentTask.setFlag("2");
				}
			}
			StaticValue.isPausing = true;
			StaticValue.isRunning = false;
		} else {
			logger.info("现在抓取系统没有启动无需关闭!");
		}
		return true;
	}
}
