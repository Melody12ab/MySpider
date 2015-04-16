package com.weibo.common.threads;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.mbm.elint.entity.util.LoginPojo;
import com.mbm.elint.entity.util.TypePojo;
import com.mbm.elint.manager.business.KeySearchTaskManager;
import com.weibo.common.controler.SpiderControler;
import com.weibo.common.utils.StaticValue;
import com.weibo.sina.cookie.keywords.KeySearchTask;

public class SwitchSearchUserDaemonThread implements Runnable {
	public static Logger logger = Logger
			.getLogger(SwitchSearchUserDaemonThread.class);
	private LinkedList<LoginPojo> all_user_list;
	private SpiderControler sinaSpiderControler;

	private KeySearchTaskManager keySearchTaskManager;

	public SwitchSearchUserDaemonThread(SpiderControler sinaSpiderControler,
			KeySearchTaskManager keySearchTaskManager,
			LinkedList<LoginPojo> all_user_list) {
		this.sinaSpiderControler = sinaSpiderControler;
		this.all_user_list = all_user_list;
		this.keySearchTaskManager = keySearchTaskManager;
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
			int switch_count = 0;
			try {
				Thread.sleep(StaticValue.switch_search_interval_time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			LoginPojo loginPojo = null;
			if (all_user_list.size() == 0) {
				this.all_user_list = this.sinaSpiderControler.reGetLoginPojoList(TypePojo.SINA);
			} else if (all_user_list.size() >= 10) {

			} else {
				this.all_user_list.addAll(this.sinaSpiderControler
						.reGetLoginPojoList(TypePojo.SINA));
			}
			while (all_user_list.size() >= 1) {
				loginPojo = all_user_list.pollFirst();
				if (loginPojo.getUid().equals(
						SpiderControler.isRunningAccountUid4Sina)) {
					if (all_user_list.size() == 0) {
						this.all_user_list = this.sinaSpiderControler
								.reGetLoginPojoList(TypePojo.SINA);
					}
					// 如这时的uid等于正在运行帐号信息、内容抓取的uid
					continue;
				}
				loginPojo = SpiderControler.getCookieForSwitch(loginPojo, 1);//暂设空，不用
				if (loginPojo.getCookie()!=null && loginPojo.getCookie().contains("deleted")) {
					switch_count++;
					if (all_user_list.size() == 0) {
						this.all_user_list = this.sinaSpiderControler
								.reGetLoginPojoList(TypePojo.SINA);
					}
					logger.info(loginPojo.getUsername()
							+ "登陆失败，此次切换search 用户失败，等待下一个周期");
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
					logger.info("切换用户后，现在抓取帐号、关注信息，正在使用的帐户是----------"
							+ loginPojo.getUsername());
					logger
							.info("正在试图切换KeySearchTask任务的用户------------------------");
					for (KeySearchTask keySearchTask : keySearchTaskManager
							.getKeySearchTasks()) {
						logger.info("keySearch抓取--"
								+ keySearchTask.getKeySearchTaskId()
								+ "正在试图切换正在使用的用户------------------------");
						try {
							keySearchTask.setFlag("2");
							/**
							 * 更新cookieTask的cookie参数，从而切换user
							 */
							keySearchTask.reInit(loginPojo);
							logger.info("keySearch抓取--"
									+ keySearchTask.getKeySearchTaskId()
									+ "成功切换用户------------------------");
						} catch (Exception e) {
							e.printStackTrace();
							logger.info("keySearch抓取--"
									+ keySearchTask.getKeySearchTaskId()
									+ "切换用户失败------------------------");
						} finally {
							keySearchTask.setFlag("1");
						}
					}
					logger
							.info("完成切换KeySearchTask任务的用户------------------------");
				}
				break;
			}
		}
	}
}
