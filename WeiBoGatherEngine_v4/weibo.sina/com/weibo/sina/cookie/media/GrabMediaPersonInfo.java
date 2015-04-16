package com.weibo.sina.cookie.media;

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
import com.weibo.common.manager.SwtichUserThreadManager;
import com.weibo.common.utils.AnsjPaser;
import com.weibo.common.utils.HtmlParserUtil;
import com.weibo.common.utils.StaticValue;
import com.weibo.sina.cookie.common.CommonJudgeManager;
import com.weibo.sina.cookie.common.GrabPageResource;
import com.weibo.utils.sina.MyStringUtils;
import com.weibo.utils.sina.RandomOperator;
import com.weibo.utils.sina.SinaVersionChangeUtil;

@Component
@Scope(value = "prototype")
public class GrabMediaPersonInfo {
	public static Logger logger = Logger.getLogger(GrabMediaPersonInfo.class);
	// @Autowired
	private GrabPageResource grabPageSource;
	@Autowired
	private PersonInfoManager personInfoManager;
	// private PersonInfo person = null;
	private PersonInfo person;
	private String ajax_mess_url = null;
	@Autowired
	private MyJson myJSON = new MyJson();;

	// 临时person定义
	private UrlPojo urlPojo;
	private String sendUrl;
	private String produceURL;
	SinaVersionChangeUtil sinaVersionChangeUtil = new SinaVersionChangeUtil();

	/**
	 * 2015-01-09新添加字段
	 */
	String domain_id = null;
	// 在校园版、机构版的自定义博主的类型时起区分作用
	String verify_type = null;

	/**
	 * 集中声明变量开始
	 */
	AnsjPaser UserJudge = null;
	String judgeText = null;
	String beginRegex = null;
	String endRegex = null;
	AnsjPaser ansjId = null;
	String person_uid = null;
	AnsjPaser ansjName = null;
	AnsjPaser ansjRealName = null;
	String realName = null;
	AnsjPaser summerParser = null;
	String person_name = null;
	String summary = null;
	String content = null;
	AnsjPaser divBlock = null;
	String paras_number = null;

	// jsoup 选择
	private List<String> selectorList = new LinkedList<String>();
	private List<String> selectorResultList = null;

	/**
	 * 集中声明变量结束
	 */
	public String getSendUrl() {
		return sendUrl;
	}

	public void setSendUrl(String sendUrl) {
		this.sendUrl = sendUrl;
	}

	public UrlPojo getUrlPojo() {
		return urlPojo;
	}

	public void setUrlPojo(UrlPojo urlPojo) {
		this.urlPojo = urlPojo;
	}

	@Deprecated
	public void saveMediaPersonInfo() throws Exception {
		if (StringUtils.isBlank(sendUrl)) {
			logger.info("---url不可为空");
			throw new Exception();
		}
		content = grabPageSource.getPageSourceOfSina(produceURL, null);
		if (content == null) {
			logger.info(sendUrl + " OPEN ERROR !");
			throw new Exception();
		}
		grabMediaPersonInfoPage(content);
	}

	public void saveMediaPersonInfo(String version) throws Exception {
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
				// IOUtil.writeFile("d:/test.txt", content);
				/**
				 * 对是否是出现网络请求繁忙页做测试，如果出现则直接进行帐户切换
				 */
				if ((CommonJudgeManager.isForbiddenToGrab(content))) {
					logger.info("发现请求繁忙，将进行一次线程中断!");
					SwtichUserThreadManager.interrupte();
					try {
						Thread.sleep(2000);
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
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
					System.out.println("出现了超时异常，要睡会再继续请求!");
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
		grabMediaPersonInfoPage(content);// 在此处已经取得domain
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
		// grabPersonInfoAjax(content);

		Thread.sleep(StaticValue.cookie_sleep_interval_time);
	}

	private HtmlParserUtil htmlParserUtil = new HtmlParserUtil();

	public String getJsonNumber(String targetUrl) {
		int error_repeat_times = 0;
		String jsonNumberStr = null;
		while (error_repeat_times < StaticValue.url_repeat_count) {
			try {
				jsonNumberStr = grabPageSource.getPageSourceOfSina(targetUrl,
						StaticValue.Referer_Media);

				/**
				 * 对是否是出现网络请求繁忙页做测试，如果出现则直接进行帐户切换
				 */
				String title = htmlParserUtil.getTitleByLine(jsonNumberStr);
				if ((!StringOperatorUtil.isBlank(title))
						&& title.contains("错误提示")) {
					logger.info("发现请求繁忙，将进行一次线程中断!");
					SwtichUserThreadManager.interrupte();
					try {
						Thread.sleep(2000);
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
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
					System.out.println("出现了超时异常，要睡会再继续请求!");
				}
			}
			error_repeat_times++;
		}

		return jsonNumberStr;
	}

	public void grabMediaPersonInfoPage(String content) throws Exception {
		// 测试用户是否存在
		if (CommonJudgeManager.isNotExistUser(content)) {
			throw new Exception("要抓取的用户不存在!");
		}
		// uid不为空，则不抽取了，若为空，则要抽取网页中的uid
		beginRegex = "CONFIG\\[\'oid\'\\]=\'";
		endRegex = "\'";
		ansjId = new AnsjPaser(beginRegex, endRegex, content,
				AnsjPaser.TEXTEGEXANDNRT);
		person_uid = ansjId.getText();
		if (person_uid == null || person_uid.length() == 0) {
			throw new Exception();
		}
		/**
		 * 判断提交的网址是否正常，若抓取到的是内置的信息，认为提交的网址有错误,当然此时的url也要判断,若非本人的url则返回提交的url错误
		 */
		if (CommonJudgeManager.isGrabCircle(person_uid, this.sendUrl)) {
			throw new Exception("抓回到种子帐户自己了，说明出错误了,please check!");
		}

		person.setUid(person_uid);
		// logger.info("uid-----" + person_uid);

		// 姓名抽取
		beginRegex = "CONFIG\\[\'onick\'\\]=\'";
		endRegex = "\'";
		ansjName = new AnsjPaser(beginRegex, endRegex, content,
				AnsjPaser.TEXTEGEXANDNRT);
		person_name = ansjName.getText();
		person.setName(person_name);

		// logger.info("person_name-----" + person_name);

		/**
		 * 因为是媒体，直接定义sex为6，企业版的定义为7
		 */
		person.setSex("6");
		// logger.info("sex--" + person.getSex());

		// 真实姓名
		beginRegex = "class=\"vip_infor\">";
		endRegex = "<";
		ansjRealName = new AnsjPaser(beginRegex, endRegex, content,
				AnsjPaser.TEXTEGEXANDNRT);
		realName = ansjRealName.getText();
		person.setRealName(realName);

		// 提取粉丝、关注、微博数量
		divBlock = new AnsjPaser("\"domid\":\"Pl_Core_T8CustomTriColumn__3\",",
				"\\}\\)", content, AnsjPaser.TEXTEGEXANDNRT);
		paras_number = divBlock.getText();

		if (paras_number == null || paras_number.trim().length() < 1) {
			// logger.info("没有粉丝方面的数据!");
		} else {
			myJSON.createJson("{" + paras_number + "}");
			paras_number = myJSON.getStringByKey("html");

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

		// 抽取媒体版微博的基本信息
		String temp_media_info_req_url = "http://weibo.com/" + person.getUid()
				+ "/about";
		content = grabPageSource.getPageSourceOfSina(temp_media_info_req_url,
				null);
		grabBasicInfo4Ajax(content);

		// logger.info("summary-----" + summary);

		person.setUpdateTime(new Date());
		person.setUrl(sendUrl);
		person.setWeiboType("1");
		person.setGrab_method("media");
		/**
		 * person对象暂为空，故先注释
		 */
		// 将long型数据加入至uid_long
		person.setUid_long(Long.parseLong(person.getUid()));

		logger.info("mediaPersonInfo info-----------" + person);
		// System.out.println("mediaPersonInfo info save successful");
		if (this.urlPojo.getUrlSource().startsWith("h")) {
			personInfoManager.savePersonInfo(true, person);
		} else {
			personInfoManager.savePersonInfo(false, person);
		}
	}

	public void grabBasicInfo4Ajax(String ajaxContent) throws Exception {
		// IOUtil.writeFile("d:/test.txt", ajaxContent);
		// 基本信息块
		AnsjPaser infoBlock = new AnsjPaser(
				"domid\":\"Pl_Core_T3Simpletext__\\d{2,5}\",",
				"\\}\\)</script>", ajaxContent, AnsjPaser.TEXTEGEXANDNRT);
		String infoBlockText = infoBlock.getText();
		// System.out.println("---" + infoBoockText);
		myJSON.createJson("{" + infoBlockText + "}");
		// System.out.println("myJSON---" + myJSON);
		infoBlockText = myJSON.getStringByKey("html");
		// logger.info("infoBoockText---" + infoBoockText);
		// IOUtil.writeFile("d:/test.txt", infoBoockText);
		if (infoBlockText != null) {
			selectorList.clear();
			selectorList.add("div.PCD_text_a");

			// 提取存在的所有模块
			selectorResultList = VaoLanHtmlParser
					.getNodeContentBySelector(infoBlockText, selectorList,
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
						if (subTitleResultList.get(0).contains("简介")) {
							// IOUtil.writeFile("d:/test.txt", blockContent);

							List<String> subDetailSelList = new LinkedList<String>();
							List<String> subDetailResultList = null;

							subDetailSelList.add("div.m_wrap");
							subDetailResultList = VaoLanHtmlParser
									.getNodeContentBySelector(blockContent,
											subDetailSelList,
											DataFormatStatus.CleanTxt);
							if (!StringOperatorUtil
									.isBlankCollection(subDetailResultList)) {
								person.setSummary(subDetailResultList.get(0));
							}
						}
					}

				}
			}
		}
	}

	public void init(PersonInfo person, GrabPageResource grabPageResource,
			UrlPojo urlPojo) {
		this.person = person;
		this.grabPageSource = grabPageResource;
		this.urlPojo = urlPojo;
		this.sendUrl = MyStringUtils.getUrlRemoveSlash(urlPojo.getUrl());
		// this.produceURL = sinaVersionChangeUtil.getMediaUrl(sendUrl);
		this.produceURL = sinaVersionChangeUtil.getPersonUrl(sendUrl);
	}

	public void init(GrabPageResource grabPageResource, UrlPojo urlPojo) {
		this.grabPageSource = grabPageResource;
		this.urlPojo = urlPojo;
		this.sendUrl = MyStringUtils.getUrlRemoveSlash(urlPojo.getUrl());
		// this.produceURL = sinaVersionChangeUtil.getMediaUrl(sendUrl);
		this.produceURL = sinaVersionChangeUtil.getPersonUrl(sendUrl);
	}

	public void init(GrabPageResource grabPageResource) {
		this.grabPageSource = grabPageResource;
	}

	public String getUid() {
		int error_repeat_times = 0;
		while (error_repeat_times < StaticValue.url_repeat_count) {
			try {
				content = grabPageSource.getPageSourceOfSina(produceURL, null);
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
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
					System.out.println("出现了超时异常，要睡会再继续请求!");
				}
			}
			error_repeat_times++;
		}

		// System.out.println("getUid----------content---------------"+content);
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
				return null;
			}
			if (judgeText.trim().equals("抱歉，你所访问的用户不存在")) {
				logger.info("抱歉，你所访问的用户不存在");
				return null;
			}
			if (judgeText.trim().contains("抱歉，你当前访问的帐号异常，暂时无法访问")) {
				logger.info("抱歉，你当前访问的帐号异常，暂时无法访问");
				return null;
			}
			if (judgeText.trim().contains("抱歉，你的帐号存在异常，暂时无法访问")) {
				logger.info("抱歉，你的帐号存在异常，暂时无法访问");
				return null;
			}
		}
		// uid不为空，则不抽取了，若为空，则要抽取网页中的uid
		beginRegex = "CONFIG\\[\'oid\'\\] = \'";
		endRegex = "\'";
		ansjId = new AnsjPaser(beginRegex, endRegex, content,
				AnsjPaser.TEXTEGEXANDNRT);
		person_uid = ansjId.getText();

		if (person_uid == null || person_uid.length() == 0) {
			return null;
		}

		return person_uid;
	}

	public PersonInfo getPersonHead() throws Exception {
		content = grabPageSource.getPageSourceOfSina(produceURL, null);
		// System.out.println("content-----------------------"+content);
		if (content == null) {
			logger.info(sendUrl + " OPEN ERROR !");
			return null;
		}
		// 测试用户是否存在
		if (CommonJudgeManager.isNotExistUser(content)) {
			throw new Exception("要抓取的用户不存在!");
		}
		// uid不为空，则不抽取了，若为空，则要抽取网页中的uid
		beginRegex = "CONFIG\\[\'oid\'\\]=\'";
		endRegex = "\'";
		ansjId = new AnsjPaser(beginRegex, endRegex, content,
				AnsjPaser.TEXTEGEXANDNRT);
		person_uid = ansjId.getText();
		if (person_uid == null || person_uid.length() == 0) {
			throw new Exception("person_uid不可为空!");
		}
		if (CommonJudgeManager.isGrabCircle(person_uid, this.sendUrl)) {
			throw new Exception("抓回到种子帐户自己了，说明出错误了,please check!");
		}
		person.setUid(person_uid);
		// 姓名抽取
		beginRegex = "CONFIG\\[\'onick\'\\]=\'";
		endRegex = "\'";
		ansjName = new AnsjPaser(beginRegex, endRegex, content,
				AnsjPaser.TEXTEGEXANDNRT);
		person_name = ansjName.getText();
		person.setName(person_name);
		// System.out.println("抽取出来的name---------------" + person_name);
		return person;
	}

}
