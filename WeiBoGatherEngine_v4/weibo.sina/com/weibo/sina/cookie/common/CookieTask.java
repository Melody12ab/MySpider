package com.weibo.sina.cookie.common;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mbm.elint.entity.PersonInfo;
import com.mbm.elint.entity.util.LoginPojo;
import com.mbm.elint.entity.util.UrlPojo;
import com.mbm.elint.manager.business.spider.UrlsManager;
import com.weibo.common.utils.StaticValue;
import com.weibo.sina.cookie.comment.GrabCommentInfo4PersonAndMedia;
import com.weibo.sina.cookie.comment.GrabCommentInfo4ProAndEnter;
import com.weibo.sina.cookie.enterprise.GrabEnterContentInfoV2;
import com.weibo.sina.cookie.enterprise.GrabEnterPersonInfoV2;
import com.weibo.sina.cookie.media.GrabMediaContentInfo;
import com.weibo.sina.cookie.media.GrabMediaPersonInfo;
import com.weibo.sina.cookie.person.GrabContentInfo;
import com.weibo.sina.cookie.person.GrabPersonInfo;
import com.weibo.sina.cookie.person.v2.GrabAttentionInfoV2;
import com.weibo.sina.cookie.person.v2.GrabFansInfo;
import com.weibo.sina.cookie.person.v2.GrabPersonInfoV2;
import com.weibo.sina.cookie.profession.GrabProContentInfo;
import com.weibo.sina.cookie.profession.GrabProPersonInfo;
import com.weibo.utils.sina.MyStringUtils;

@Component
public class CookieTask implements Runnable {
	public static Logger logger = Logger.getLogger(CookieTask.class);

	@Autowired
	private GrabContentInfo grabContentInfo;
	@Autowired
	private GrabPageResource grabPageResource;
	@Autowired
	private GrabPersonInfo grabPersonInfo;
	@Autowired
	private GrabPersonInfoV2 grabPersonInfoV2;
	// @Autowired
	// private GrabAttentionInfo grabAttentionInfo;
	@Autowired
	private GrabFansInfo grabFansInfo;
	@Autowired
	private GrabMediaPersonInfo grabMediaPersonInfo;
	@Autowired
	private GrabMediaContentInfo grabMediaContentInfo;
	@Autowired
	private GrabEnterPersonInfoV2 grabEnterPersonInfoV2;
	// private GrabEnterPersonInfoV grabEnterPersonInfo;
	@Autowired
	private GrabEnterContentInfoV2 grabEnterContentInfoV2;
	// private GrabEnterContentInfo grabEnterContentInfo;
	@Autowired
	private GrabAttentionInfoV2 grabAttentionInfoV2;
	@Autowired
	private GrabProPersonInfo grabProPersonInfo;
	@Autowired
	private GrabProContentInfo grabProContentInfo;
	@Autowired
	private GrabProContentInfo grabGovContentInfo;

	@Autowired
	private MutualFansManager mutualFansManager;

	// 以下为评论列表抓取引擎
	@Autowired
	private GrabCommentInfo4PersonAndMedia grabCommentInfo4PersonAndMedia;
	@Autowired
	private GrabCommentInfo4ProAndEnter grabCommentInfo4ProAndEnter;

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

	public CookieTask() {
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
					this.urlPojo = this.getUrlsManager().getOneAttentionUrl();
					if (this.urlPojo == null) {
						this.urlPojo = this.getUrlsManager().getOneUrl();
					}
				} else if (this.cookieId % 3 == 0) {
					/**
					 * 先做评论列表抓取的任务,后做周期性的任务
					 */
					this.urlPojo = this.getUrlsManager().getOneCommentUrl();
					if (this.urlPojo == null) {
						this.urlPojo = this.getUrlsManager().getOneCircleUrl();
						if (this.urlPojo == null) {
							this.urlPojo = this.getUrlsManager().getOneUrl();
						}
					}
				} else if (this.cookieId % 5 == 0) {
					this.urlPojo = this.getUrlsManager().getOneCircleUrl();
					if (this.urlPojo == null) {
						this.urlPojo = this.getUrlsManager().getOneUrl();
					}
				} else {
					this.urlPojo = this.getUrlsManager().getOneUrl();
				}
				if (urlPojo != null) {
					// isRunning = true;
					int url_type = urlPojo.getType();// 取得urlType，判断进入哪个抓取的入口
					this.uid = MyStringUtils.getIdByUrl(urlPojo.getUrl());// 取得形式上的uid,也许是domainName
					PersonInfo person = new PersonInfo();
					if (url_type == 1 || url_type == 4) {
						accountFlag = false;
						// 先执行新版个人信息抓取
						this.grabPersonInfoV2.init(person, grabPageResource,
								urlPojo);
						urlPojo.setRepeat_count(0);
						while (urlPojo.getRepeat_count() <= StaticValue.url_repeat_count) {
							try {
								grabPersonInfoV2.savePersonInfo();
								Thread
										.sleep(StaticValue.cookie_sleep_interval_time);
								accountFlag = true;
								break;// 正常结束
							} catch (Exception e) {
								// logger.info("新版解析不适用，将换用旧版解析");
								// System.out.println("新版解析不适用，将换用旧版解析");
								urlPojo.setRepeat_count(urlPojo
										.getRepeat_count() + 1);
							}
						}
						// 如果是非正常结束,则输出提示信息
						if (!accountFlag) {
							if (url_type != 4) {
								// 个人版抓取遇到异常，直接添加到企业版，不往
//								this.getUrlsManager().addErrorUrlToEnterprise(
//										urlPojo);
								this.getUrlsManager().addErrorUrlToMedia(
										urlPojo);
								System.out.println(urlPojo
										+ "  抓取出现问题,已达到最大重复抓取次数，已加入媒体版集合");
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
					} else if (url_type == 6 || url_type == 8) {// 媒体帐户信息抓取
						accountFlag = false;
						this.grabMediaPersonInfo.init(person, grabPageResource,
								urlPojo);
						urlPojo.setRepeat_count(0);
						while (urlPojo.getRepeat_count() <= StaticValue.url_repeat_count) {
							try {
								// grabMediaPersonInfo.saveMediaPersonInfo();
								grabMediaPersonInfo.saveMediaPersonInfo("v2");
								Thread
										.sleep(StaticValue.cookie_sleep_interval_time);
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
							if (url_type != 8) {
								this.getUrlsManager().addErrorUrlToEnterprise(// 再遇错的话，转至企业版去抓取
										urlPojo);
								// logger.info(urlPojo
								// + " 媒体版, 抓取出现问题,已达到最大重复抓取次数，已加入企业版集合");
								System.out.println(urlPojo
										+ " 媒体版, 抓取出现问题,已达到最大重复抓取次数，已加入企业版集合");
							} else {
								this.getUrlsManager().removeVisitingUrls(
										urlPojo);
							}
						} else {// 说明是正常结束
							if (url_type != 8) {
								this.getUrlsManager().addVisitedUrls(urlPojo);
							} else {
								this.getUrlsManager().removeVisitingUrls(
										urlPojo);
							}
						}
					} else if (url_type == 10 || url_type == 12) {// 企业帐户信息抓取
						accountFlag = false;
						this.grabEnterPersonInfoV2.init(person,
								grabPageResource, urlPojo);
						urlPojo.setRepeat_count(0);
						while (urlPojo.getRepeat_count() <= StaticValue.url_repeat_count) {
							try {
								grabEnterPersonInfoV2.saveEnterPersonInfo();
								Thread
										.sleep(StaticValue.cookie_sleep_interval_time);
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
							if (url_type != 12) {
								this.getUrlsManager().addErrorUrlToProfession(
										urlPojo);
								// logger.info(urlPojo
								// + " 企业版, 抓取出现问题,已达到最大重复抓取次数，已加入专业版集合");
								System.out.println(urlPojo
										+ " 企业版, 抓取出现问题,已达到最大重复抓取次数，已加入专业版集合");
							} else {
								this.getUrlsManager().removeVisitingUrls(
										urlPojo);
							}
						} else {// 说明是正常结束
							if (url_type != 12) {
								this.getUrlsManager().addVisitedUrls(urlPojo);
							} else {
								this.getUrlsManager().removeVisitingUrls(
										urlPojo);
							}
						}
					} else if (url_type == 14 || url_type == 16) {// 专业版帐户信息抓取
						accountFlag = false;
						this.grabProPersonInfo.init(person, grabPageResource,
								urlPojo);
						urlPojo.setRepeat_count(0);
						while (urlPojo.getRepeat_count() <= StaticValue.url_repeat_count) {
							try {
								grabProPersonInfo.saveProfessionPersonInfo();
								Thread
										.sleep(StaticValue.cookie_sleep_interval_time);
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
							if (url_type != 16) {
								this.getUrlsManager().addErrorUrl(urlPojo);
								// logger
								// .info(urlPojo
								// + " 专业版, 抓取出现问题,已达到最大重复抓取次数，已加入errorUrls集合");
								System.out
										.println(urlPojo
												+ " 专业版, 抓取出现问题,已达到最大重复抓取次数，已加入errorUrls集合");
							} else {
								this.getUrlsManager().removeVisitingUrls(
										urlPojo);
							}
						} else {// 说明是正常结束
							if (url_type != 16) {
								this.getUrlsManager().addVisitedUrls(urlPojo);
							} else {
								this.getUrlsManager().removeVisitingUrls(
										urlPojo);
							}
						}
					} else if (url_type == 2 || url_type == 5) {// 博文信息抓取
						try {
							/**
							 * 当抓取文章内容时,会先抓取该用户的帐号信息
							 */
							this.grabPersonInfo.init(person, grabPageResource,
									urlPojo);
							if (urlPojo.getPerson() == null) {
								person = this.grabPersonInfo.getPersonHead();
								urlPojo.setPerson(person);
							} else {
								// logger
								// .info("该urlPojo------已经有person了， 不用再重新获取了!");
								System.out
										.println("该urlPojo------已经有person了， 不用再重新获取了!");
								person = urlPojo.getPerson();
							}
							this.grabContentInfo.init(person, grabPageResource,
									urlPojo, this.urlsManager);
							grabContentInfo.grabArticleContents4All();
							if (url_type != 5) {
								this.getUrlsManager().addVisitedUrls(urlPojo);
							} else {
								this.getUrlsManager().removeVisitingUrls(
										urlPojo);
							}
							Thread
									.sleep(StaticValue.cookie_sleep_interval_time);
						} catch (Exception e) {
							e.printStackTrace();
							if (urlPojo.getRepeat_count() <= StaticValue.url_repeat_count) {
								urlPojo.setRepeat_count(urlPojo
										.getRepeat_count() + 1);
								this.getUrlsManager().addUrlToTail(urlPojo);
								// logger.info(urlPojo
								// + "  抓取出现问题,还未达到最大重复抓取次数，将再次加入抓取队列!");
								System.out.println(urlPojo
										+ "  抓取出现问题,还未达到最大重复抓取次数，将再次加入抓取队列!");
							} else {
								urlPojo.setRepeat_count(urlPojo
										.getRepeat_count() + 1);
								if (url_type != 5) {
									// this.getUrlsManager().addErrorUrl(urlPojo);
									this.getUrlsManager().addErrorUrlToMedia(
											urlPojo);
									// logger.info(urlPojo
									// + "  抓取出现问题,已达到最大重复抓取次数，已加入媒体版集合");
									System.out.println(urlPojo
											+ "  抓取出现问题,已达到最大重复抓取次数，已加入媒体版集合");
								} else {
									this.getUrlsManager().removeVisitingUrls(
											urlPojo);
								}
							}
						}
					} else if (url_type == 7 || url_type == 9) {
						try {
							/**
							 * 当抓取媒体版的文章内容时,会先抓取该用户的帐号信息
							 */
							this.grabMediaPersonInfo.init(person,
									grabPageResource, urlPojo);
							if (urlPojo.getPerson() == null) {
								person = this.grabMediaPersonInfo
										.getPersonHead();
								urlPojo.setPerson(person);
							} else {
								// logger
								// .info("该urlPojo------已经有mediaPerson了， 不用再重新获取了!");
								System.out
										.println("该urlPojo------已经有mediaPerson了， 不用再重新获取了!");
								person = urlPojo.getPerson();
							}
							this.grabMediaContentInfo
									.init(person, grabPageResource, urlPojo,
											this.urlsManager);
							grabMediaContentInfo.grabArticleContents(urlPojo);
							if (url_type != 9) {
								this.getUrlsManager().addVisitedUrls(urlPojo);
							} else {
								this.getUrlsManager().removeVisitingUrls(
										urlPojo);
							}
							Thread
									.sleep(StaticValue.cookie_sleep_interval_time);
						} catch (Exception e) {
							e.printStackTrace();
							if (urlPojo.getRepeat_count() <= StaticValue.url_repeat_count) {
								urlPojo.setRepeat_count(urlPojo
										.getRepeat_count() + 1);
								this.getUrlsManager().addUrlToTail(urlPojo);
								// logger
								// .info(urlPojo
								// + "媒体版内容，  抓取出现问题,还未达到最大重复抓取次数，将再次加入抓取队列!");
								System.out
										.println(urlPojo
												+ "媒体版内容，  抓取出现问题,还未达到最大重复抓取次数，将再次加入抓取队列!");
							} else {
								urlPojo.setRepeat_count(urlPojo
										.getRepeat_count() + 1);
								if (url_type != 9) {
									this.getUrlsManager()
											.addErrorUrlToEnterprise(urlPojo);
									// logger
									// .info(urlPojo
									// +
									// " 媒体版内容，  抓取出现问题,已达到最大重复抓取次数，已加入企业版集合");
									System.out
											.println(urlPojo
													+ " 媒体版内容，  抓取出现问题,已达到最大重复抓取次数，已加入企业版集合");
								} else {
									this.getUrlsManager().removeVisitingUrls(
											urlPojo);
								}
							}
						}
					} else if (url_type == 11 || url_type == 13) {// 企业版微博内容的抓取
						try {
							/**
							 * 当抓取企业片版的文章内容时,会先抓取该用户的帐号信息
							 */
							this.grabEnterPersonInfoV2.init(person,
									grabPageResource, urlPojo);
							if (urlPojo.getPerson() == null) {
								person = this.grabEnterPersonInfoV2
										.getPersonHead();
								urlPojo.setPerson(person);
							} else {
								// logger
								// .info("该urlPojo------已经有enterPerson了， 不用再重新获取了!");
								System.out
										.println("该urlPojo------已经有enterPerson了， 不用再重新获取了!");
								person = urlPojo.getPerson();
							}
							this.grabEnterContentInfoV2.init(person,
									grabPageResource, urlPojo);
							grabEnterContentInfoV2.grabArticleContents4All();
							if (url_type != 13) {
								this.getUrlsManager().addVisitedUrls(urlPojo);
							} else {
								this.getUrlsManager().removeVisitingUrls(
										urlPojo);
							}
							Thread
									.sleep(StaticValue.cookie_sleep_interval_time);
						} catch (Exception e) {
							e.printStackTrace();
							if (urlPojo.getRepeat_count() <= StaticValue.url_repeat_count) {
								urlPojo.setRepeat_count(urlPojo
										.getRepeat_count() + 1);
								this.getUrlsManager().addUrlToTail(urlPojo);
								// logger
								// .info(urlPojo
								// + "企业版内容，  抓取出现问题,还未达到最大重复抓取次数，将再次加入抓取队列!");
								System.out
										.println(urlPojo
												+ "企业版内容，  抓取出现问题,还未达到最大重复抓取次数，将再次加入抓取队列!");
							} else {
								urlPojo.setRepeat_count(urlPojo
										.getRepeat_count() + 1);
								if (url_type != 13) {
									// this.getUrlsManager().addErrorUrl(urlPojo);
									this.getUrlsManager()
											.addErrorUrlToGovernment(urlPojo);
									// logger
									// .info(urlPojo
									// +
									// " 企业版内容，  抓取出现问题,已达到最大重复抓取次数，将加入政府版集合");
									System.out
											.println(urlPojo
													+ " 企业版内容，  抓取出现问题,已达到最大重复抓取次数，将加入政府版集合");
								} else {
									this.getUrlsManager().removeVisitingUrls(
											urlPojo);
								}
							}
						}
					}
					// 政府版开始
					else if (url_type == 20 || url_type == 21) {// 专业版微博内容的抓取
						try {
							/**
							 * 当抓取政府版的文章内容时,会先抓取该用户的帐号信息
							 */
							this.grabProPersonInfo.init(person,
									grabPageResource, urlPojo);
							if (urlPojo.getPerson() == null) {
								person = this.grabProPersonInfo.getPersonHead();
								urlPojo.setPerson(person);
							} else {
								// logger
								// .info("该urlPojo------已经有govPerson了， 不用再重新获取了!");
								System.out
										.println("该urlPojo------已经有govPerson了， 不用再重新获取了!");
								person = urlPojo.getPerson();
							}
							this.grabGovContentInfo
									.init(person, grabPageResource, urlPojo,
											this.urlsManager);
							grabGovContentInfo.grabArticleContents(urlPojo);
							if (url_type != 21) {
								this.getUrlsManager().addVisitedUrls(urlPojo);
							} else {
								this.getUrlsManager().removeVisitingUrls(
										urlPojo);
							}
							Thread
									.sleep(StaticValue.cookie_sleep_interval_time);
						} catch (Exception e) {
							e.printStackTrace();
							if (urlPojo.getRepeat_count() <= StaticValue.url_repeat_count) {
								urlPojo.setRepeat_count(urlPojo
										.getRepeat_count() + 1);
								this.getUrlsManager().addUrlToTail(urlPojo);
								// logger
								// .info(urlPojo
								// + "政府版内容，  抓取出现问题,还未达到最大重复抓取次数，将再次加入抓取队列!");
								System.out
										.println(urlPojo
												+ "政府版内容，  抓取出现问题,还未达到最大重复抓取次数，将再次加入抓取队列!");
							} else {
								urlPojo.setRepeat_count(urlPojo
										.getRepeat_count() + 1);
								if (url_type != 21) {
									this.getUrlsManager()
											.addErrorUrlToProfession(urlPojo);
									// logger
									// .info(urlPojo
									// +
									// " 政府版内容，  抓取出现问题,已达到最大重复抓取次数，已加入专业版集合");
									System.out
											.println(urlPojo
													+ " 政府版内容，  抓取出现问题,已达到最大重复抓取次数，已加入专业版集合");
								} else {
									this.getUrlsManager().removeVisitingUrls(
											urlPojo);
								}
							}
						}
					}
					// 政府版结束
					else if (url_type == 15 || url_type == 17) {// 专业版微博内容的抓取
						try {
							/**
							 * 当抓取专业版的文章内容时,会先抓取该用户的帐号信息
							 */
							this.grabProPersonInfo.init(person,
									grabPageResource, urlPojo);
							if (urlPojo.getPerson() == null) {
								person = this.grabProPersonInfo.getPersonHead();
								urlPojo.setPerson(person);
							} else {
								// logger
								// .info("该urlPojo------已经有enterPerson了， 不用再重新获取了!");
								System.out
										.println("该urlPojo------已经有enterPerson了， 不用再重新获取了!");
								person = urlPojo.getPerson();
							}
							this.grabProContentInfo
									.init(person, grabPageResource, urlPojo,
											this.urlsManager);
							grabProContentInfo.grabArticleContents(urlPojo);
							if (url_type != 17) {
								this.getUrlsManager().addVisitedUrls(urlPojo);
							} else {
								this.getUrlsManager().removeVisitingUrls(
										urlPojo);
							}
							Thread
									.sleep(StaticValue.cookie_sleep_interval_time);
						} catch (Exception e) {
							e.printStackTrace();
							if (urlPojo.getRepeat_count() <= StaticValue.url_repeat_count) {
								urlPojo.setRepeat_count(urlPojo
										.getRepeat_count() + 1);
								this.getUrlsManager().addUrlToTail(urlPojo);
								// logger
								// .info(urlPojo
								// + "专业版内容，  抓取出现问题,还未达到最大重复抓取次数，将再次加入抓取队列!");
								System.out
										.println(urlPojo
												+ "专业版内容，  抓取出现问题,还未达到最大重复抓取次数，将再次加入抓取队列!");
							} else {
								urlPojo.setRepeat_count(urlPojo
										.getRepeat_count() + 1);
								if (url_type != 17) {
									this.getUrlsManager().addErrorUrl(urlPojo);
									// logger
									// .info(urlPojo
									// +
									// " 专业版内容，  抓取出现问题,已达到最大重复抓取次数，已加入errorUrls集合");
									System.out
											.println(urlPojo
													+ " 专业版内容，  抓取出现问题,已达到最大重复抓取次数，已加入errorUrls集合");
								} else {
									this.getUrlsManager().removeVisitingUrls(
											urlPojo);
								}
							}
						}
					} else if (url_type == 101 || url_type == 18) {// 评论列表的抓取内容的抓取
						try {
							this.grabCommentInfo4PersonAndMedia
									.init(grabPageResource, urlPojo,
											this.urlsManager);
							grabCommentInfo4PersonAndMedia
									.grabArticleComments();
							if (url_type != 18) {
								this.getUrlsManager().addVisitedUrls(urlPojo);
							} else {
								this.getUrlsManager().removeVisitingUrls(
										urlPojo);
							}
							Thread
									.sleep(StaticValue.cookie_sleep_interval_time);
						} catch (Exception e) {
							e.printStackTrace();
							this.getUrlsManager().addErrorUrl(urlPojo);
							logger.info(urlPojo + " 评论列表内容，  抓取出现问题,已加入错误列表!");
							this.getUrlsManager().removeVisitingUrls(urlPojo);
						}
					} else if (url_type == 102 || url_type == 19) {// 评论列表的抓取内容的抓取
						try {
							this.grabCommentInfo4ProAndEnter
									.init(grabPageResource, urlPojo,
											this.urlsManager);
							grabCommentInfo4ProAndEnter.grabArticleComments();
							if (url_type != 19) {
								this.getUrlsManager().addVisitedUrls(urlPojo);
							} else {
								this.getUrlsManager().removeVisitingUrls(
										urlPojo);
							}
							Thread
									.sleep(StaticValue.cookie_sleep_interval_time);
						} catch (Exception e) {
							e.printStackTrace();
							this.getUrlsManager().addErrorUrl(urlPojo);
							logger.info(urlPojo + " 评论列表内容，  抓取出现问题,已加入错误列表!");
							this.getUrlsManager().removeVisitingUrls(urlPojo);
						}
					} else if (url_type == 3) {// 关注列表抓取
						attentionFlag = false;
						while (urlPojo.getRepeat_count() <= StaticValue.url_repeat_count) {
							try {
								// Long.parseLong(this.uid);//为了满足提交不合理的url时，所做的修改
								this.grabPersonInfoV2.init(person,
										grabPageResource, urlPojo);
								this.grabPersonInfoV2
										.initPersonUidAndDomainId();
							} catch (Exception e) {
								this.grabPersonInfoV2.init(person,
										grabPageResource, urlPojo);
								try {
									this.grabPersonInfoV2
											.initPersonUidAndDomainId();
								} catch (Exception e1) {
									e1.printStackTrace();
								}
							} finally {
								try {
									if (person.getUid() == null
											|| person.getUid().length() == 0) {
										logger
												.info(this.urlPojo.getUrl()
														+ "--------抓取关注时，没有取到uid，抛出异常!");
										urlPojo.setRepeat_count(urlPojo
												.getRepeat_count() + 1);
										continue;
									}
									this.grabAttentionInfoV2.init(person
											.getDomain_id(), person.getUid(),
											grabPageResource, urlPojo,
											urlsManager);
									// 关注的抓取
									String attentionUidList = null;// 关注列表对应的字符串
									String fansUidList = null;// 粉丝列表对应的字符串
									try {
										attentionUidList = this.grabAttentionInfoV2
												.savePersonAttentionInfo();
									} catch (Exception e) {
										e.printStackTrace();
										logger.info(person.getUid()
												+ "新版微博的关注抓取模块出现异常,请检查!");
									}
									// 粉丝的抓取
									try {
										this.grabFansInfo.init(person
												.getDomain_id(), person
												.getUid(), grabPageResource,
												urlPojo, urlsManager);
										fansUidList = this.grabFansInfo
												.savePersonFansInfo();
									} catch (Exception e) {
										e.printStackTrace();
										logger.info(person.getUid()
												+ "新版微博的粉丝抓取模块出现异常,请检查!");
									}

									// 互粉的处理,以前边的粉丝和关注来计算，很有限
									// this.mutualFansManager.processMutualFans(
									// person.getUid(), attentionUidList,
									// fansUidList);
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
										System.out
												.println(urlPojo
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
							// logger.info(urlPojo
							// + "  抓取出现问题,已达到最大重复抓取次数，已加入errorUrls集合");
							System.out.println(urlPojo
									+ "  抓取出现问题,已达到最大重复抓取次数，已加入errorUrls集合");
							this.getUrlsManager().addErrorUrl(urlPojo);
						}
					}
				} else {
					try {
						Thread
								.sleep(StaticValue.keyword_or_cookie_null_task_waiting);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				// 每处理完一个URL，则要sleep一下
				try {
					Thread.sleep(StaticValue.cookie_sleep_interval_time);
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
