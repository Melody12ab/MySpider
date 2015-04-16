package com.weibo.sina.cookie.person.v2;

import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mbm.elint.entity.PersonInfo;
import com.mbm.elint.entity.util.AccountTypeRleationPojo;
import com.mbm.elint.entity.util.MyJson;
import com.mbm.elint.entity.util.UrlPojo;
import com.mbm.elint.manager.database.PersonInfoManager;
import com.mbm.util.IOUtil;
import com.mbm.util.StringOperatorUtil;
import com.vaolan.parser.VaoLanHtmlParser;
import com.vaolan.status.DataFormatStatus;
import com.weibo.common.controler.SpiderControler;
import com.weibo.common.manager.SwtichUserThreadManager;
import com.weibo.common.utils.AnsjPaser;
import com.weibo.common.utils.FormatOutput;
import com.weibo.common.utils.HtmlParserUtil;
import com.weibo.common.utils.StaticValue;
import com.weibo.sina.cookie.common.CommonJudgeManager;
import com.weibo.sina.cookie.common.GrabPageResource;
import com.weibo.utils.sina.MyStringUtils;
import com.weibo.utils.sina.SinaVersionChangeUtil;

@Component
@Scope(value = "prototype")
public class GrabPersonInfoV2 {
	public static Logger logger = Logger.getLogger(GrabPersonInfoV2.class);
	private GrabPageResource grabPageSource;
	@Autowired
	private PersonInfoManager personInfoManager;
	private String produceURL;
	private PersonInfo person = null;
	private UrlPojo urlPojo;
	private String sendUrl;
	@Autowired
	private MyJson myJSON = new MyJson();
	SinaVersionChangeUtil sinaVersionChangeUtil = new SinaVersionChangeUtil();
	private HtmlParserUtil htmlParserUtil = new HtmlParserUtil();
	// jsoup 选择
	private List<String> selectorList = new LinkedList<String>();
	private List<String> selectorResultList = null;

	/**
	 * 集中声明变量开始
	 */
	String content = null;
	String beginRegex = null;
	String endRegex = null;
	String person_uid = null;
	String renZhInfo = null;
	AnsjPaser renZhBlock = null;
	AnsjPaser divBlock = null;
	String paras_number = null;
	AnsjPaser guanZhu = null;
	String guanZhu_number = null;
	AnsjPaser fansAnsj = null;
	String fans_number = null;
	AnsjPaser wbAnsj = null;
	String wb_number = null;
	AnsjPaser UserJudge = null;
	String judgeText = null;
	AnsjPaser infoBlock = null;
	String infoBoockText = null;
	AnsjPaser ansjName = null;
	String person_name = null;
	AnsjPaser ansjAddress = null;
	String address = null;
	AnsjPaser schoolPaser = null;
	String sexText = null;
	AnsjPaser ansjblog = null;
	String blog = null;
	AnsjPaser ansjSummaryOne = null;
	String summaryOne = null;
	AnsjPaser ansjBirth = null;
	String birth = null;
	AnsjPaser ansjMail = null;
	String mail = null;
	AnsjPaser ansjQQ = null;
	String qq = null;
	AnsjPaser ansjMSN = null;
	String msn = null;
	AnsjPaser eduBlock = null;
	String eduInfo = null;
	AnsjPaser ansjEdu = null;
	AnsjPaser ansjEduItem = null;
	String edu = "";
	String temp_item_edu = "";
	AnsjPaser tagBlock = null;
	String tagsInfo = null;
	AnsjPaser tagTxtAnsj = null;
	AnsjPaser ansjId = null;

	/**
	 * 以下声明是为解决2013-09-22版微博个人信息抓取时的路径中的page_id
	 */
	String domain_id = null;
	// 在校园版、机构版的自定义博主的类型时起区分作用
	String verify_type = null;

	/**
	 * 集中声明变量结束
	 */
	public UrlPojo getUrlPojo() {
		return urlPojo;
	}

	public void setUrlPojo(UrlPojo urlPojo) {
		this.urlPojo = urlPojo;
	}

	public void savePersonInfo() throws Exception {
		if (StringUtils.isBlank(sendUrl)) {
			logger.info("---url不可为空");
			throw new Exception();
		}
		int error_repeat_times = 0;
		while (error_repeat_times < StaticValue.url_repeat_count) {
			try {
				content = grabPageSource.getPageSourceOfSina(this.produceURL,
						null);
				// logger.info("content---"+content);
				// IOUtil.writeFile("d:/test.txt",content);
				/**
				 * 对是否是出现网络请求繁忙页做测试，如果出现则直接进行帐户切换
				 */
				String title = htmlParserUtil.getTitleByLine(content);
				if ((!StringOperatorUtil.isBlank(title))
						&& title.contains("错误提示")) {
					logger.info("发现请求繁忙，将进行一次线程中断!");
					SwtichUserThreadManager.interrupte();
					try {
						Thread.sleep(60 * 1000);
						logger.info("打断切换线程后，先sleep一会儿!");
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					logger.info("打断线程后的sleep完成，即将进入抓取!");
				} else {
					// 到这里说明不是超时的错误,直接break掉本次while循环即可
					break;
				}
			} catch (SocketTimeoutException e) {
				try {
					logger.info("请求出现超时，sleep a while!");
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
					logger.info("出现了超时异常，要睡会再继续请求!");
				}
			}
			error_repeat_times++;
		}

		// System.out.println("head---content----"+content);
		if (content == null) {
			logger.info(sendUrl + " OPEN ERROR !");
			throw new Exception();
		}
		// System.out.println("head---content----" + content);
		grabPersonInfoHead(content);// 在此处已经取得domain
		/**
		 * 组合新的个人信息的url
		 */
		String temp_ref = null;
		if (person.getRenZh() == 0) {// 说明没有认证
			temp_ref = "/p/" + (domain_id + person.getUid()) + "/info";
		} else {
			temp_ref = "/p/" + (domain_id + person.getUid()) + "/info";
		}

		// 在个人版中，将domain_id作为pid,对应到AccountTypeRelationPojo对象的相应的key中
		String accountType = AccountTypeRleationPojo.getAccountType(domain_id,
				verify_type);
		person.setAccountType(accountType);

		// String temp_person_info_req_url = StaticValue.Person_Info_Req_URL
		// .replace("${page_id}", domain_id + person.getUid()).replace(
		// "${doamin_id}", domain_id).replace("${ref}", temp_ref);
		String temp_person_info_req_url = "http://weibo.com" + temp_ref;

		content = grabPageSource.getPageSourceOfSina(temp_person_info_req_url,
				null);

		// IOUtil.writeFile("d:/test.txt", content);
		grabPersonInfoAjax(content);

		Thread.sleep(StaticValue.cookie_sleep_interval_time);
	}

	public void initPersonUidAndDomainId() throws Exception {
		if (StringUtils.isBlank(sendUrl)) {
			logger.info("---url不可为空");
			throw new Exception();
		}
		String headContent = grabPageSource.getPageSourceOfSina(
				this.produceURL, null);
		// uid不为空，则不抽取了，若为空，则要抽取网页中的uid
		beginRegex = "CONFIG\\[\'oid\'\\]=\'";
		endRegex = "\'";
		AnsjPaser ansjId = new AnsjPaser(beginRegex, endRegex, headContent,
				AnsjPaser.TEXTEGEXANDNRT);
		person_uid = ansjId.getText();
		// logger.info("person_uid---" + person_uid);
		if (person_uid == null || person_uid.length() == 0) {
			throw new Exception();
		}
		/**
		 * 判断提交的网址是否正常，若抓取到的是内置的信息，认为提交的网址有错误,当然此时的url也要判断,若非本人的url则返回提交的url错误
		 */
		if (CommonJudgeManager.isGrabCircle(person_uid, this.sendUrl)) {
			throw new Exception("抓回到种子帐户自己了，说明出错误了,please check!");
		}
		person.setUid(person_uid);// 设置person_uid

		// 在此处提取网页中的page_id
		beginRegex = "CONFIG\\[\'domain\'\\]=\'";
		endRegex = "\'";
		AnsjPaser domainIdParser = new AnsjPaser(beginRegex, endRegex,
				headContent, AnsjPaser.TEXTEGEXANDNRT);
		domain_id = domainIdParser.getText();

		if (domain_id == null) {
			System.out.println("没有取到domainIdParser");
			throw new Exception();
		}
		person.setDomain_id(domain_id);

	}

	public void grabPersonInfoHead(String headContent) throws Exception {
		// uid不为空，则不抽取了，若为空，则要抽取网页中的uid
		beginRegex = "CONFIG\\[\'oid\'\\]=\'";
		endRegex = "\'";
		AnsjPaser ansjId = new AnsjPaser(beginRegex, endRegex, headContent,
				AnsjPaser.TEXTEGEXANDNRT);
		person_uid = ansjId.getText();
		// logger.info("person_uid---" + person_uid);
		if (person_uid == null || person_uid.length() == 0) {
			throw new Exception();
		}

		// 在此处提取网页中的page_id
		beginRegex = "CONFIG\\[\'domain\'\\]=\'";
		endRegex = "\'";
		AnsjPaser domainIdParser = new AnsjPaser(beginRegex, endRegex,
				headContent, AnsjPaser.TEXTEGEXANDNRT);
		domain_id = domainIdParser.getText();

		if (domain_id == null) {
			System.out.println("没有取到domainIdParser");
			throw new Exception();
		}

		/**
		 * 判断提交的网址是否正常，若抓取到的是内置的信息，认为提交的网址有错误,当然此时的url也要判断,若非本人的url则返回提交的url错误
		 */
		if (CommonJudgeManager.isGrabCircle(person_uid, this.sendUrl)) {
			logger.info("抓回到种子帐户自己了，说明出错误了,please check,将提前切换种子帐户!");
			SwtichUserThreadManager.interrupte();
			try {
				Thread.sleep(2000);
				logger.info("打断切换线程后，先sleep一会儿!");
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			logger.info("切换完成!");
		}

		person.setUid(person_uid);// 设置person_uid

		// 提取出verify_type,为设置自定义的属性作准备，暂只在校园版、机构版等的情况下有区分作用
		// 在此处提取网页中的page_id
		beginRegex = "CONFIG\\[\'verified_type\'\\]=\'";
		endRegex = "\'";
		AnsjPaser verifyTypeParser = new AnsjPaser(beginRegex, endRegex,
				headContent, AnsjPaser.TEXTEGEXANDNRT);
		verify_type = verifyTypeParser.getText();
		// 这里的认证类型，暂只做个人版的，其它版的微博暂置后
		verify_type = "person";
		person.setVerifyType(verify_type);

		// 提取认证模块
		int renZh = 0;

		renZhBlock = new AnsjPaser("\\{\"ns\":\"pl.content.homeFeed.index\",",
				"\\}\\)", headContent, AnsjPaser.TEXTEGEXANDNRT);
		renZhInfo = renZhBlock.getText();
		myJSON.createJson("{" + renZhInfo + "}");
		renZhInfo = myJSON.getStringByKey("html");

		// logger.info("renZhInfo---" + renZhInfo);
		// IOUtil.writeFile("d:/test.txt",renZhInfo);

		if (renZhInfo == null || renZhInfo.trim().length() < 1) {
			// logger.info("没有认证");
			renZh = 0;// 设置为0 ，代表没有认证
		} else {
			// 得到认证专门的认证div block,通过封装的jsoup包
			selectorList.clear();
			selectorList.add("p.verify>span>a.icon_verify_v");
			List<String> verify_flag = VaoLanHtmlParser
					.getNodeContentBySelector(renZhInfo, selectorList,
							DataFormatStatus.TagAllContent);

			if (!StringOperatorUtil.isBlankCollection(verify_flag)) {// 说明有认证
				renZh = 1;
				// logger.info("之前renZhInfo---"+renZhInfo);
				selectorList.clear();
				selectorList.add("p.info>span");
				List<String> verify_info_list = VaoLanHtmlParser
						.getNodeContentBySelector(renZhInfo, selectorList,
								DataFormatStatus.TagAllContent);
				if (!StringOperatorUtil.isBlankCollection(verify_info_list)) {
					String verify_info = VaoLanHtmlParser
							.getCleanTxt(verify_info_list.get(0));
					person.setVerifyInfo(verify_info);
				}
			}
		}
		person.setRenZh(renZh);

		// 提取粉丝、关注、微博数量
		divBlock = new AnsjPaser("\"domid\":\"Pl_Core_T8CustomTriColumn__3\",",
				"\\}\\)", headContent, AnsjPaser.TEXTEGEXANDNRT);
		paras_number = divBlock.getText();

		if (paras_number == null || paras_number.trim().length() < 1) {
			// logger.info("没有粉丝方面的数据!");
		} else {
			myJSON.createJson("{" + paras_number + "}");
			paras_number = myJSON.getStringByKey("html");

			// logger.info("paras_number---" + paras_number);
			// IOUtil.writeFile("d:/test.txt", paras_number);

			selectorList.clear();
			selectorList.add("td.S_line1");

			selectorResultList = VaoLanHtmlParser.getNodeContentBySelector(
					paras_number, selectorList, DataFormatStatus.TagAllContent);
			// 不为空说明有关于粉丝等等的值
			if (!StringOperatorUtil.isBlankCollection(selectorResultList)) {
				List<String> subSelList = new LinkedList<String>();
				// subSelList.add("strong.W_f12");
				subSelList.add("strong");
				List<String> subSelResultList = null;
				for (String line : selectorResultList) {
					if (line.contains("关注")) {
						subSelResultList = VaoLanHtmlParser
								.getNodeContentBySelector(line, subSelList,
										DataFormatStatus.CleanTxt);
						if (!StringOperatorUtil
								.isBlankCollection(subSelResultList)) {
							person.setGzNum(Integer.parseInt(subSelResultList
									.get(0)));
						}
						continue;
					}
					if (line.contains("粉丝")) {
						subSelResultList = VaoLanHtmlParser
								.getNodeContentBySelector(line, subSelList,
										DataFormatStatus.CleanTxt);
						if (!StringOperatorUtil
								.isBlankCollection(subSelResultList)) {
							person.setFansNum(Integer.parseInt(subSelResultList
									.get(0)));
						}
						continue;
					}
					if (line.contains("微博")) {
						subSelResultList = VaoLanHtmlParser
								.getNodeContentBySelector(line, subSelList,
										DataFormatStatus.CleanTxt);
						if (!StringOperatorUtil
								.isBlankCollection(subSelResultList)) {
							person.setWbNum(Integer.parseInt(subSelResultList
									.get(0)));
						}
						continue;
					}
				}
			}
		}
	}

	public void grabPersonInfoAjax(String content) throws Exception {
		// 测试用户是否存在
		UserJudge = new AnsjPaser("<span class=\"icon_delM\"></span><p>", "<",
				content, AnsjPaser.TEXTEGEXANDNRT);
		judgeText = UserJudge.getText();
		// System.out.println("judgeText------------" + judgeText);
		if (judgeText != null) {
			if (judgeText.trim().equals("抱歉，你访问的页面地址有误，或者该页面不存在")) {
				logger.info("抱歉，你访问的页面地址有误，或者该页面不存在");
				throw new Exception();
			}
			if (judgeText.trim().equals("抱歉，你所访问的用户不存在")) {
				logger.info("抱歉，你所访问的用户不存在");
				throw new Exception();
			}
			if (judgeText.trim().contains("抱歉，你当前访问的帐号异常，暂时无法访问")) {
				logger.info("抱歉，你当前访问的帐号异常，暂时无法访问");
				throw new Exception();
			}
			if (judgeText.trim().contains("抱歉，你的帐号存在异常，暂时无法访问")) {
				logger.info("抱歉，你的帐号存在异常，暂时无法访问");
				throw new Exception();
			}
		}
		// 基本信息块
		infoBlock = new AnsjPaser(
				"domid\":\"Pl_Official_PersonalInfo__\\d{2,5}\",",
				"\\}\\)</script>", content, AnsjPaser.TEXTEGEXANDNRT);
		infoBoockText = infoBlock.getText();
		// System.out.println("---" + infoBoockText);
		myJSON.createJson("{" + infoBoockText + "}");
		// System.out.println("myJSON---" + myJSON);
		infoBoockText = myJSON.getStringByKey("html");
		// logger.info("infoBoockText---" + infoBoockText);
		// IOUtil.writeFile("d:/test.txt", infoBoockText);
		if (infoBoockText != null) {
			selectorList.clear();
			selectorList.add("div.PCD_text_b2");

			// 提取存在的所有模块
			selectorResultList = VaoLanHtmlParser
					.getNodeContentBySelector(infoBoockText, selectorList,
							DataFormatStatus.TagAllContent);
			if (!StringOperatorUtil.isBlankCollection(selectorResultList)) {
				List<String> subTitleSelList = new LinkedList<String>();
				List<String> subTitleResultList = null;

				subTitleSelList.add("h4");
				for (String blockContent : selectorResultList) {
					// 首先提取基本信息模块
					subTitleResultList = VaoLanHtmlParser.getNestTagContent(
							blockContent, subTitleSelList,
							DataFormatStatus.CleanTxt);
					if (!StringOperatorUtil
							.isBlankCollection(subTitleResultList)) {
						if (subTitleResultList.get(0).contains("基本信息")) {
							// IOUtil.writeFile("d:/test.txt", blockContent);

							List<String> subDetailSelList = new LinkedList<String>();
							List<String> subDetailResultList = null;

							subDetailSelList.add("li.li_1");
							subDetailResultList = VaoLanHtmlParser
									.getNodeContentBySelector(blockContent,
											subDetailSelList,
											DataFormatStatus.TagAllContent);
							if (!StringOperatorUtil
									.isBlankCollection(subDetailResultList)) {
								List<String> subItemSelList = new LinkedList<String>();
								List<String> subItemResultList = null;
								subItemSelList.add("span.pt_detail");
								for (String itemLine : subDetailResultList) {
									subItemResultList = VaoLanHtmlParser
											.getNodeContentBySelector(itemLine,
													subItemSelList,
													DataFormatStatus.CleanTxt);
									if (!StringOperatorUtil
											.isBlankCollection(subItemResultList)) {
										if (itemLine.contains("昵称")) {
											person.setName(subItemResultList
													.get(0));
											continue;
										}
										if (itemLine.contains("所在地")) {
											person.setAddress(subItemResultList
													.get(0));
											continue;
										}
										if (itemLine.contains("性别")) {
											sexText = subItemResultList.get(0);
											if (sexText != null) {
												if (sexText.equals("男")) {
													person.setSex("1");
												} else if (sexText.equals("女")) {
													person.setSex("2");
												} else {
													person.setSex("0");
												}
											}
											continue;
										}

										if (itemLine.contains("生日")) {
											person
													.setBrithday(subItemResultList
															.get(0));
											continue;
										}

										if (itemLine.contains("博客")) {
											person.setBlogUrl(subItemResultList
													.get(0));
											continue;
										}

										// 个性域名提取，暂未配置该字段
										if (itemLine.contains("个性域名")) {
											// person.setBlogUrl(subItemResultList
											// .get(0));
											continue;
										}

										if (itemLine.contains("简介")) {
											person.setSummary(subItemResultList
													.get(0));
											continue;
										}
										// 注册时间提取，暂未配置该字段
										if (itemLine.contains("注册时间")) {
											// person.setBlogUrl(subItemResultList
											// .get(0));
											continue;
										}
									}
								}
							}
						}
						if (subTitleResultList.get(0).contains("教育信息")) {
							// IOUtil.writeFile("d:/test.txt", blockContent);

							List<String> subDetailSelList = new LinkedList<String>();
							List<String> subDetailResultList = null;

							subDetailSelList.add("li.li_1");
							subDetailResultList = VaoLanHtmlParser
									.getNodeContentBySelector(blockContent,
											subDetailSelList,
											DataFormatStatus.TagAllContent);
							if (!StringOperatorUtil
									.isBlankCollection(subDetailResultList)) {
								List<String> subItemSelList = new LinkedList<String>();
								List<String> subItemResultList = null;
								subItemSelList.add("span.pt_detail");
								for (String itemLine : subDetailResultList) {
									subItemResultList = VaoLanHtmlParser
											.getNodeContentBySelector(itemLine,
													subItemSelList,
													DataFormatStatus.CleanTxt);
									if (!StringOperatorUtil
											.isBlankCollection(subItemResultList)) {
										if (itemLine.contains("大学")) {
											if (StringOperatorUtil
													.isBlank(person.getEdu())) {
												person.setEdu(subItemResultList
														.get(0));
											} else {
												person.setEdu(person.getEdu()
														+ "\n"
														+ subItemResultList
																.get(0));
											}
											continue;
										}
										if (itemLine.contains("高中")) {
											if (StringOperatorUtil
													.isBlank(person.getEdu())) {
												person.setEdu(subItemResultList
														.get(0));
											} else {
												person.setEdu(person.getEdu()
														+ "\n"
														+ subItemResultList
																.get(0));
											}
											continue;
										}
									}
								}
							}

						}
						if (subTitleResultList.get(0).contains("标签信息")) {
							// IOUtil.writeFile("d:/test.txt", blockContent);
							List<String> subDetailSelList = new LinkedList<String>();
							List<String> subDetailResultList = null;

							subDetailSelList.add("li.li_1");
							subDetailResultList = VaoLanHtmlParser
									.getNodeContentBySelector(blockContent,
											subDetailSelList,
											DataFormatStatus.TagAllContent);
							if (!StringOperatorUtil
									.isBlankCollection(subDetailResultList)) {
								List<String> subItemSelList = new LinkedList<String>();
								List<String> subItemResultList = null;
								subItemSelList.add("span.pt_detail");
								for (String itemLine : subDetailResultList) {
									subItemResultList = VaoLanHtmlParser
											.getNodeContentBySelector(itemLine,
													subItemSelList,
													DataFormatStatus.CleanTxt);
									if (!StringOperatorUtil
											.isBlankCollection(subItemResultList)) {
										if (itemLine.contains("标签")) {
											person.setTags(subItemResultList
													.get(0));
											continue;
										}
									}
								}
							}

						}

					}

				}
			}
			person.setWeiboType("1");// 表示是新浪微博

			// 邮箱、qq、msn、qq等信息，暂未发现在界面显示，故省掉
			// // 邮箱抽取
			// beginRegex = "邮箱" + AnsjPaser.TEXTEGEXANDNRT + "class="
			// + AnsjPaser.TEXTEGEXANDNRT + ">";
			// endRegex = "<";
			// ansjMail = new AnsjPaser(beginRegex, endRegex, infoBoockText,
			// AnsjPaser.TEXTEGEXANDNRT);
			// mail = ansjMail.getText();
			// // logger.info("邮箱---" + mail);
			// person.setEmail(mail);
			// // qq抽取
			// beginRegex = "QQ" + AnsjPaser.TEXTEGEXANDNRT + "<p>";
			// endRegex = "<";
			// ansjQQ = new AnsjPaser(beginRegex, endRegex, infoBoockText,
			// AnsjPaser.TEXTEGEXANDNRT);
			// qq = ansjQQ.getText();
			// // logger.info("QQ---" + qq);
			// person.setQq(qq);
			//
			// // msn抽取
			// beginRegex = "MSN" + AnsjPaser.TEXTEGEXANDNRT + "<p>";
			// endRegex = "<";
			// ansjMSN = new AnsjPaser(beginRegex, endRegex, infoBoockText,
			// AnsjPaser.TEXTEGEXANDNRT);
			// msn = ansjMSN.getText();
			// // logger.info("msn---" + msn);
			// person.setMsn(msn);
		}

		person.setUpdateTime(new Date());
		person.setUrl(sendUrl);

		person.setGrab_method("cookies");
		// 将long型数据加入至uid_long
		person.setUid_long(Long.parseLong(person.getUid()));

		logger.info("person info-----------" + person);

		if (this.urlPojo.getUrlSource().startsWith("h")) {
			personInfoManager.savePersonInfo(true, person);
		} else {
			if (this.urlPojo.getUrlSource().startsWith("s")) {//
				// 此时说明系统管理员特殊添加，做特别处理
				personInfoManager.savePersonInfo4System(person);
			} else {
				personInfoManager.savePersonInfo(false, person);
			}
		}
		// System.out.println("person info-----------" + person);
	}

	public void init(PersonInfo person, GrabPageResource grabPageResource,
			UrlPojo urlPojo) {
		this.person = person;
		this.grabPageSource = grabPageResource;
		this.urlPojo = urlPojo;
		this.sendUrl = MyStringUtils.getUrlRemoveSlash(urlPojo.getUrl());
		this.produceURL = sinaVersionChangeUtil.getPersonUrl(sendUrl);
	}

	public void init(GrabPageResource grabPageResource, UrlPojo urlPojo) {
		this.grabPageSource = grabPageResource;
		this.urlPojo = urlPojo;
		this.sendUrl = MyStringUtils.getUrlRemoveSlash(urlPojo.getUrl());
		this.produceURL = sinaVersionChangeUtil.getPersonUrl(sendUrl);
	}

	// public String getUid() {
	// content = grabPageSource.getPageSourceOfSina(produceURL, null);
	// // System.out.println("getUid----------content---------------"+content);
	// if (content == null) {
	// logger.info(sendUrl + " OPEN ERROR !");
	// return null;
	// }
	// // 测试用户是否存在
	// UserJudge = new AnsjPaser("<span class=\"icon_delM\"></span><p>", "<",
	// content, AnsjPaser.TEXTEGEXANDNRT);
	// judgeText = UserJudge.getText();
	// if (judgeText != null) {
	// if (judgeText.trim().equals("抱歉，你访问的页面地址有误，或者该页面不存在")) {
	// logger.info("抱歉，你访问的页面地址有误，或者该页面不存在");
	// return null;
	// }
	// if (judgeText.trim().equals("抱歉，你所访问的用户不存在")) {
	// logger.info("抱歉，你所访问的用户不存在");
	// return null;
	// }
	// if (judgeText.trim().contains("抱歉，你当前访问的帐号异常，暂时无法访问")) {
	// logger.info("抱歉，你当前访问的帐号异常，暂时无法访问");
	// return null;
	// }
	// if (judgeText.trim().contains("抱歉，你的帐号存在异常，暂时无法访问")) {
	// logger.info("抱歉，你的帐号存在异常，暂时无法访问");
	// return null;
	// }
	// }
	// // uid不为空，则不抽取了，若为空，则要抽取网页中的uid
	// beginRegex = "CONFIG\\[\'oid\'\\] = \'";
	// endRegex = "\'";
	// ansjId = new AnsjPaser(beginRegex, endRegex, content,
	// AnsjPaser.TEXTEGEXANDNRT);
	// person_uid = ansjId.getText();
	//
	// if (person_uid == null || person_uid.length() == 0) {
	// return null;
	// }
	//
	// return person_uid;
	// }

	public PersonInfo getPersonHead() throws Exception {
		content = grabPageSource.getPageSourceOfSina(this.produceURL, null);
		// System.out.println("content-----------------------"+content);

		if (content == null) {
			logger.info(sendUrl + " OPEN ERROR !");
			return null;
		}
		// 测试用户是否存在
		UserJudge = new AnsjPaser("<span class=\"icon_delM\"></span><p>", "<",
				content, AnsjPaser.TEXTEGEXANDNRT);
		judgeText = UserJudge.getText();
		if (judgeText != null) {
			if (judgeText.trim().equals("抱歉，你访问的页面地址有误，或者该页面不存在")) {
				logger.info("抱歉，你访问的页面地址有误，或者该页面不存在");
				throw new Exception();
			}
			if (judgeText.trim().equals("抱歉，你所访问的用户不存在")) {
				logger.info("抱歉，你所访问的用户不存在");
				throw new Exception();
			}
			if (judgeText.trim().contains("抱歉，你当前访问的帐号异常，暂时无法访问")) {
				logger.info("抱歉，你当前访问的帐号异常，暂时无法访问");
				throw new Exception();
			}
			if (judgeText.trim().contains("抱歉，你的帐号存在异常，暂时无法访问")) {
				logger.info("抱歉，你的帐号存在异常，暂时无法访问");
				throw new Exception();
			}
		}
		// uid不为空，则不抽取了，若为空，则要抽取网页中的uid
		beginRegex = "CONFIG\\[\'oid\'\\]=\'";
		endRegex = "\'";
		ansjId = new AnsjPaser(beginRegex, endRegex, content,
				AnsjPaser.TEXTEGEXANDNRT);
		person_uid = ansjId.getText();

		/**
		 * 测试部分--start
		 */
		// logger.info("content------"+content);
		/**
		 * 测试部分--end
		 */
		if (person_uid == null || person_uid.length() == 0) {
			throw new Exception();
		}
		if (person_uid.equals(SpiderControler.isRunningAccountUid4Sina)) {
			// logger.info(this.sendUrl + " --估计是遇到错误了，回到root抓取用户了");
			System.out.println(this.sendUrl + " --估计是遇到错误了，回到root抓取用户了");
			// logger.info("content----"+content);
			throw new Exception();
		}
		person.setUid(person_uid);
		// 姓名抽取
		beginRegex = "CONFIG\\[\'onick\'\\]=\'";
		endRegex = "\'";
		ansjName = new AnsjPaser(beginRegex, endRegex, content,
				AnsjPaser.TEXTEGEXANDNRT);
		person_name = ansjName.getText();

		if (person_name == null || person_name.trim().length() == 0) {
			logger.info("没有提取出person_name，直接抛出异常!");
			throw new Exception();
		}

		person.setName(person_name);
		// System.out.println("抽取出来的name---------------" + person_name);

		return person;
	}

	public static void main(String[] args) throws Exception {
		String content = IOUtil.readFile("d:\\test.txt");

		// System.out.println(source);
		MyJson myJSON = new MyJson();
		//
		AnsjPaser infoBlock = new AnsjPaser("ns\":\"pl.header.head.index\",",
				"\\}\\)</script>", content, AnsjPaser.TEXTEGEXANDNRT);
		// String infoBoockText = infoBlock.getText();
		//
		// System.out.println("---" + infoBoockText);
		//
		// myJSON.createJson("{" + infoBoockText + "}");
		//
		// System.out.println("myJSON---" + myJSON);
		// 提取粉丝、关注、微博数量
		AnsjPaser divBlock = new AnsjPaser("class=\"user_atten", "</ul>",
				content, AnsjPaser.TEXTEGEXANDNRT);

		System.out.println(divBlock.getText());

	}
}
