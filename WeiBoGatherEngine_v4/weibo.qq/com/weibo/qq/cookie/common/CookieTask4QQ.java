package com.weibo.qq.cookie.common;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mbm.elint.entity.PersonInfo;
import com.mbm.elint.entity.util.LoginPojo;
import com.mbm.elint.entity.util.UrlPojo;
import com.mbm.elint.manager.business.spider.UrlsManager;
import com.weibo.common.utils.ReadSpiderConfig;
import com.weibo.common.utils.StaticValue;
import com.weibo.qq.cookie.person.GrabAttentionInfo4QQ;
import com.weibo.qq.cookie.person.GrabContentInfo4QQ;
import com.weibo.qq.cookie.person.GrabPersonInfo4QQ;
import com.weibo.utils.sina.MyStringUtils;

@Component
public class CookieTask4QQ implements Runnable {
	public static Logger logger = Logger.getLogger(CookieTask4QQ.class);
	private static long cookie_sleep_interval_time = Long
			.parseLong(ReadSpiderConfig.getValue("cookie_spider_interval_time"));
	@Autowired
	private GrabContentInfo4QQ grabContentInfo4QQ;
	@Autowired
	private GrabPageSource4QQ grabPageResource;
	@Autowired
	private GrabPersonInfo4QQ grabPersonInfo4QQ;
	@Autowired
	private GrabAttentionInfo4QQ grabAttentionInfo4QQ;

	private int cookieId;

	public int getCookieId() {
		return cookieId;
	}

	public void setCookieId(int cookieId) {
		this.cookieId = cookieId;
	}

	/**
	 * flag=1 可以运行或正在运行 flag=2 暂停运行，正在试图切换用户
	 */
	private String flag;
	private UrlsManager urlsManager;

	public UrlsManager getUrlsManager() {
		return urlsManager;
	}

	public void setUrlsManager(UrlsManager urlsManager) {
		this.urlsManager = urlsManager;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	private LoginPojo loginPojo;
	private String cookieStr;
	private PersonInfo person;

	public CookieTask4QQ() {
	}

	public PersonInfo getPerson() {
		return person;
	}

	public void setPersonInfo(PersonInfo person) {
		this.person = person;
	}

	public String getCookieStr() {
		return cookieStr;
	}

	public void setCookieStr(String cookieStr) {
		this.cookieStr = cookieStr;
	}

	public LoginPojo getLoginPojo() {
		return loginPojo;
	}

	public void setLoginPojo(LoginPojo loginPojo) {
		this.loginPojo = loginPojo;
	}

	private UrlPojo urlPojo;

	public UrlPojo getUrlPojo() {
		return urlPojo;
	}

	public void setUrlPojo(UrlPojo urlPojo) {
		this.urlPojo = urlPojo;
	}

	public boolean init(int cookieId, UrlsManager urlsManager, String flag,
			LoginPojo loginPojo) {
		this.cookieId = cookieId;
		this.urlsManager = urlsManager;
		this.flag = flag;
		this.loginPojo = loginPojo;
		this.grabPageResource.init(loginPojo);
		return true;
	}

	/**
	 * 切换用户后会重新初始化各个组成部分的cookie
	 */
	public void reInit(LoginPojo loginPojo) {
		this.grabPageResource.init(loginPojo);
	}

	private String uid;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	private boolean attentionFlag = false;
	private boolean accountFlag = false;
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
				/**
				 * 此为每三个线程中的第三个为可以采集关注信息的线程,以cookieId为序号
				 * 分别区分开采信attentionUrl还是circleUrl,还是一般的url
				 */
				if (this.cookieId % 2 == 0) {
					this.urlPojo = this.getUrlsManager().getOneAttentionUrl4QQ();
					if (this.urlPojo == null) {
						this.urlPojo = this.getUrlsManager().getOneUrl4QQ();
					}
				} else if (this.cookieId % 3 == 0) {
					this.urlPojo = this.getUrlsManager().getOneCircleUrl4QQ();
					if (this.urlPojo == null) {
						this.urlPojo = this.getUrlsManager().getOneUrl4QQ();
					}
				} else {
					this.urlPojo = this.getUrlsManager().getOneUrl4QQ();
				}
				if (urlPojo != null) {
					// isRunning = true;
					int url_type = urlPojo.getType();// 取得urlType，判断进入哪个抓取的入口
					this.uid = MyStringUtils.getIdByUrl(urlPojo.getUrl());// 取得形式上的uid,也许是domainName
					PersonInfo person = new PersonInfo();
					if (url_type == 1 || url_type == 4) {
						accountFlag = false;
						// 先执行新版个人信息抓取
						this.grabPersonInfo4QQ.init(person, grabPageResource,
								urlPojo);
						urlPojo.setRepeat_count(0);
						while (urlPojo.getRepeat_count() <= StaticValue.url_repeat_count) {
							try {
								grabPersonInfo4QQ.savePersonInfo();
								Thread.sleep(cookie_sleep_interval_time);
								accountFlag = true;
								break;// 正常结束
							} catch (Exception e) {
								e.printStackTrace();
								urlPojo.setRepeat_count(urlPojo
										.getRepeat_count() + 1);
							}
						}
						// 如果是非正常结束,则输出提示信息
						if (!accountFlag) {
							if (url_type != 4) {
								this.getUrlsManager().addErrorUrl(urlPojo);
								logger.info("QQ  " + urlPojo
										+ "  抓取出现问题,已达到最大重复抓取次数，已加入错误集合");
								// this.getUrlsManager().addErrorUrlToMedia(
								// urlPojo);
								// logger.info(urlPojo
								// + "  抓取出现问题,已达到最大重复抓取次数，已加入媒体版集合");
							} else {
								this.getUrlsManager().removeVisitingUrls(
										urlPojo);
							}
						} else {// 说明是正常结束
							if (url_type != 4) {
								this.getUrlsManager().addVisitedUrls(urlPojo);
							} else {
								this.getUrlsManager().removeVisitingUrls(
										urlPojo);
							}
						}
					} else if (url_type == 2 || url_type == 5) {
						try {
							/**
							 * 当抓取文章内容时,会先抓取该用户的帐号信息
							 */
							this.grabPersonInfo4QQ.init(person,
									grabPageResource, urlPojo);
							if (urlPojo.getPerson() == null) {
								person = this.grabPersonInfo4QQ.getPersonHead();
								urlPojo.setPerson(person);
							} else {
								logger
										.info("该urlPojo------已经有person了， 不用再重新获取了!");
								person = urlPojo.getPerson();
							}
							this.grabContentInfo4QQ.init(person,
									grabPageResource, urlPojo);
							grabContentInfo4QQ.grabArticleContents();
							if (url_type != 5) {
								this.getUrlsManager().addVisitedUrls(urlPojo);
							} else {
								this.getUrlsManager().removeVisitingUrls(
										urlPojo);
							}
							Thread.sleep(cookie_sleep_interval_time);
						} catch (Exception e) {
							e.printStackTrace();
							if (urlPojo.getRepeat_count() <= StaticValue.url_repeat_count) {
								urlPojo.setRepeat_count(urlPojo
										.getRepeat_count() + 1);
								this.getUrlsManager().addUrlToTail4QQ(urlPojo);
								logger.info(urlPojo
										+ "  抓取出现问题,还未达到最大重复抓取次数，将再次加入抓取队列!");
							} else {
								urlPojo.setRepeat_count(urlPojo
										.getRepeat_count() + 1);
								if (url_type != 5) {
									this.getUrlsManager().addErrorUrl(urlPojo);
									logger.info("QQ  " + urlPojo
											+ "  抓取出现问题,已达到最大重复抓取次数，已加入错误列表集合");
									// this.getUrlsManager().addErrorUrlToMedia(
									// urlPojo);
									// logger.info(urlPojo
									// + "  抓取出现问题,已达到最大重复抓取次数，已加入媒体版集合");
								} else {
									this.getUrlsManager().removeVisitingUrls(
											urlPojo);
								}
							}
						}
					} else if (url_type == 3) {
						attentionFlag = false;
						while (urlPojo.getRepeat_count() <= StaticValue.url_repeat_count) {
							try {
								// Long.parseLong(this.uid);//为了满足提交不合理的url时，所做的修改
								this.grabPersonInfo4QQ.init(grabPageResource,
										urlPojo);
								this.uid = this.grabPersonInfo4QQ.getUid();
							} catch (Exception e) {
								this.grabPersonInfo4QQ.init(grabPageResource,
										urlPojo);
								this.uid = this.grabPersonInfo4QQ.getUid();
							} finally {
								try {
									if (this.uid == null
											|| this.uid.length() == 0) {
										logger
												.info(this.urlPojo.getUrl()
														+ "--------抓取关注时，没有取到uid，抛出异常!");
										continue;
									}
									this.grabAttentionInfo4QQ.init(this.uid,
											grabPageResource, urlPojo,urlsManager);
									try {
										this.grabAttentionInfo4QQ
												.savePersonAttentionInfo();
									} catch (Exception e) {
										e.printStackTrace();
									}
									/**
									 * 执行到这里,算是正常结束,设置标志位
									 */
									attentionFlag = true;
									break;
								} catch (Exception e) {
									e.printStackTrace();
									if (urlPojo.getRepeat_count() <= StaticValue.url_repeat_count) {
										urlPojo.setRepeat_count(urlPojo
												.getRepeat_count() + 1);
										logger
												.info(urlPojo
														+ "  关注抓取出现问题,还未达到最大重复抓取次数,立即重新抓取之!");
										continue;
									}
								}
							}
						}
						if (attentionFlag) {
							this.getUrlsManager().addVisitedAttentionUrl(
									urlPojo);
						} else {
							logger.info(urlPojo
									+ "  抓取出现问题,已达到最大重复抓取次数，已加入errorUrls集合");
							this.getUrlsManager().addErrorUrl(urlPojo);
						}
					}
				} else {
					try {
						Thread.sleep(5000);
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
