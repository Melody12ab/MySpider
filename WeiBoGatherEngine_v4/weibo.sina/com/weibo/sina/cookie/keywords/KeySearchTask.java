package com.weibo.sina.cookie.keywords;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mbm.elint.entity.util.KeywordPojo;
import com.mbm.elint.entity.util.LoginPojo;
import com.mbm.elint.manager.business.spider.KeywordManager;
import com.mbm.elint.manager.business.spider.UrlsManager;
import com.weibo.common.utils.StaticValue;
import com.weibo.sina.cookie.common.GrabPageResource;

@Component
public class KeySearchTask implements Runnable {
	public static Logger logger = Logger.getLogger(KeySearchTask.class);
	@Autowired
	private GrabSearchInfo grabSearchInfo;
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

	private KeywordManager keyWordManager;

	public KeywordManager getKeyWordManager() {
		return keyWordManager;
	}

	public void setKeyWordManager(KeywordManager keyWordManager) {
		this.keyWordManager = keyWordManager;
	}

	public KeySearchTask() {
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

	private int keySearchTaskId;

	public int getKeySearchTaskId() {
		return keySearchTaskId;
	}

	public void setKeySearchTaskId(int keySearchTaskId) {
		this.keySearchTaskId = keySearchTaskId;
	}

	public boolean init(int keySearchTaskId, UrlsManager urlManager,
			KeywordManager keyWordManager, String flag, LoginPojo loginPojo) {
		this.keySearchTaskId = keySearchTaskId;
		this.keyWordManager = keyWordManager;
		this.flag = flag;
		this.loginPojo = loginPojo;
		this.grabPageResource.init(loginPojo);
		this.grabSearchInfo.init(grabPageResource, urlManager);
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
				if (this.keySearchTaskId % 2 == 0) {
					this.keyWordPojo = this.getKeyWordManager()
							.getOneCircleKeyWord();
					if (this.keyWordPojo == null) {
						this.keyWordPojo = this.getKeyWordManager()
								.getOneKeyWord();
					}
				} else {
					this.keyWordPojo = this.getKeyWordManager().getOneKeyWord();
				}
				if (keyWordPojo != null) {
					while (keyWordPojo.getRepeat_count() <= StaticValue.keyword_repeat_count) {
						try {
							this.grabSearchInfo
									.grabKeySearchInfo(this.keyWordPojo);
							/**
							 * 执行到这里，算是退出break
							 */
							break;
						} catch (Exception e) {
							e.printStackTrace();
							if (keyWordPojo.getRepeat_count() <= StaticValue.keyword_repeat_count) {
								keyWordPojo.setRepeat_count(keyWordPojo
										.getRepeat_count() + 1);
								logger.info(keyWordPojo
										+ "---抓取出现问题,还未达到最大重复抓取次数，将再次加入抓取队列!");
							}
						}
					}
					if (keyWordPojo.getType() == 1) {
						this.getKeyWordManager().removeVisitingKeyWord(
								keyWordPojo);
					} else {
						this.getKeyWordManager().addVisitedKeyWord(keyWordPojo);
					}
				} else {
					try {
						Thread.sleep(StaticValue.keyword_or_cookie_null_task_waiting);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
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
