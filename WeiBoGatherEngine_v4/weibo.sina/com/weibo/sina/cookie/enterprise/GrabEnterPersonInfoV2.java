package com.weibo.sina.cookie.enterprise;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
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
import com.weibo.sina.cookie.common.GrabPageResource;
import com.weibo.utils.sina.MyStringUtils;
import com.weibo.utils.sina.SinaVersionChangeUtil;

@Component
@Scope(value = "prototype")
public class GrabEnterPersonInfoV2 {
	public static Logger logger = Logger.getLogger(GrabEnterPersonInfoV2.class);
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
	private HtmlParserUtil htmlParserUtil=new HtmlParserUtil();
	
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
	String tagsInfo = "";
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

	public void saveEnterPersonInfo() throws Exception {
		if (StringUtils.isBlank(sendUrl)) {
			logger.info("---url不可为空");
			throw new Exception();
		}
		content = grabPageSource.getPageSourceOfSina(this.produceURL, null);
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
			temp_ref = "/u/" + person.getUid();
		} else {
			temp_ref = "/p/" + (domain_id + person.getUid()) + "/info";
		}

		// 将domain_id作为pid,对应到AccountTypeRelationPojo对象的相应的key中
		String accountType = AccountTypeRleationPojo.getAccountType(domain_id,
				verify_type);
		person.setAccountType(accountType);
		
		String temp_person_info_req_url = StaticValue.Person_Info_Req_URL
				.replace("${page_id}", domain_id + person.getUid()).replace(
						"${doamin_id}", domain_id).replace("${ref}", temp_ref);

		content = grabPageSource.getPageSourceOfSina(temp_person_info_req_url,
				null);
		// System.out.println("enterprise html source content----" + content);
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
		if (person_uid.equals(SpiderControler.isRunningAccountUid4Sina)
				&& !this.sendUrl.equals(FormatOutput.urlPrefix
						+ SpiderControler.isRunningAccountUid4Sina)) {
			logger.info(this.sendUrl + " --该url地址有误");
			throw new Exception();
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

	public void grabPersonInfoHead(String pageAllContent) throws Exception {
		// uid不为空，则不抽取了，若为空，则要抽取网页中的uid
		beginRegex = "CONFIG\\[\'oid\'\\]=\'";
		endRegex = "\'";
		AnsjPaser ansjId = new AnsjPaser(beginRegex, endRegex, pageAllContent,
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
				pageAllContent, AnsjPaser.TEXTEGEXANDNRT);
		domain_id = domainIdParser.getText();

		if (domain_id == null) {
			System.out.println("没有取到domainIdParser");
			throw new Exception();
		}

		/**
		 * 判断提交的网址是否正常，若抓取到的是内置的信息，认为提交的网址有错误,当然此时的url也要判断,若非本人的url则返回提交的url错误
		 */
		if (person_uid.equals(SpiderControler.isRunningAccountUid4Sina)
				&& !this.sendUrl.equals(FormatOutput.urlPrefix
						+ SpiderControler.isRunningAccountUid4Sina)) {
			logger.info(this.sendUrl + " --该url地址有误");
			throw new Exception();
		}
		person.setUid(person_uid);// 设置person_uid

		// 提取出verify_type,为设置自定义的属性作准备，暂只在校园版、机构版等的情况下有区分作用
		// 在此处提取网页中的page_id
		beginRegex = "CONFIG\\[\'verified_type\'\\]=\'";
		endRegex = "\'";
		AnsjPaser verifyTypeParser = new AnsjPaser(beginRegex, endRegex,
				pageAllContent, AnsjPaser.TEXTEGEXANDNRT);
		verify_type = verifyTypeParser.getText();
		person.setVerifyType(verify_type);
		
		// 在这里拿到企业帐户的昵称，此为跟人物帐号不同之处
		// 在此处提取网页中的page_id
		beginRegex = "CONFIG\\[\'onick\'\\]=\'";
		endRegex = "\'";
		AnsjPaser nickParser = new AnsjPaser(beginRegex, endRegex,
				pageAllContent, AnsjPaser.TEXTEGEXANDNRT);
		String name = nickParser.getText();

		if (name == null) {
			System.out.println("没有取到企业帐户的昵称name，将抛异常!");
			throw new Exception();
		}
		person.setName(name);
		person.setSex("7");// sex=7代表是企业版

		// 首先得到针对个人信息的大块div
		AnsjPaser personInfoDivParser = new AnsjPaser(
				"\\{\"ns\":\"pl.header.head.index\",", "\\}\\)",
				pageAllContent, AnsjPaser.TEXTEGEXANDNRT);
		String personInfoDiv = personInfoDivParser.getText();
		myJSON.createJson("{" + personInfoDiv + "}");
		personInfoDiv = myJSON.getStringByKey("html");

		// 提取认证模块
		int renZh = 0;
		List<String> selList = new ArrayList<String>();
		selList.add("div.pf_info_identity");
		// System.out.println("personInfoDiv------"+personInfoDiv);
		List<String> listInfo = VaoLanHtmlParser.getNodeContentBySelector(
				personInfoDiv, selList, DataFormatStatus.TagAllContent);
		if (listInfo != null && (!listInfo.isEmpty())) {
			renZhInfo = listInfo.get(0);
		}
		// System.out.println("enterprise info---" + renZhInfo);
		if (renZhInfo == null || renZhInfo.trim().length() < 1) {
			renZh = 0;// 设置为0 ，代表没有认证
		} else {
			// System.out.println("renZhInfo---" + renZhInfo);
			selList.clear();
			selList.add("div.identity_info");

			List<String> isRenZhList = VaoLanHtmlParser
					.getNodeContentBySelector(renZhInfo, selList,
							DataFormatStatus.TagAllContent);

			if (isRenZhList != null && (!isRenZhList.isEmpty())) {// 说明有认证
				renZh = 1;
				String renZhInfo_div = VaoLanHtmlParser.getCleanTxt(isRenZhList
						.get(0));
				person.setVerifyInfo(renZhInfo_div);
			}
		}
		person.setRenZh(renZh);

		// 提取粉丝、关注、微博数量
		selList.clear();
		selList.add("table.W_tc");
		List<String> infoList = VaoLanHtmlParser.getNodeContentBySelector(
				personInfoDiv, selList, DataFormatStatus.TagAllContent);
		if (infoList != null && (!infoList.isEmpty())) {
			paras_number = infoList.get(0);
		}

		if (paras_number == null || paras_number.trim().length() < 1) {
			// logger.info("没有粉丝方面的数据!");
			throw new Exception();
		} else {
			selList.clear();
			selList.add("td.S_line1");
			selList.add("strong");
			infoList = VaoLanHtmlParser.getNodeContentBySelector(paras_number,
					selList, DataFormatStatus.TagAllContent);
			// 解析出来三条数字信息逐条匹配之
			guanZhu_number = VaoLanHtmlParser.getCleanTxt(infoList.get(0));
			fans_number = VaoLanHtmlParser.getCleanTxt(infoList.get(1));
			wb_number = VaoLanHtmlParser.getCleanTxt(infoList.get(2));

			person.setGzNum(Integer.parseInt(guanZhu_number));
			person.setFansNum(Integer.parseInt(fans_number));
			person.setWbNum(Integer.parseInt(wb_number));
		}
	}

	public void grabPersonInfoAjax(String content) throws Exception {
		// 按显示的顺序来提取各企业帐号信息
		List<String> selList = new ArrayList<String>();// jsoup选择之用

		// 首先取得企业简介信息
		AnsjPaser jsonContentParser = new AnsjPaser(
				"\\{\"ns\":\"pl.content.foldingText.index\",", "\\}\\)",
				content, AnsjPaser.TEXTEGEXANDNRT);
		String jsonContent = jsonContentParser.getText();
		if (jsonContent != null && (!jsonContent.isEmpty())) {
			myJSON.createJson("{" + jsonContent + "}");
			jsonContent = myJSON.getStringByKey("html");
			// System.out.println("jsonContent----"+jsonContent);
			selList.add("div.max_height");
			selList.add("p");

			List<String> infoList = VaoLanHtmlParser.getNodeContentBySelector(
					jsonContent, selList, DataFormatStatus.CleanTxt);

			if (!StringOperatorUtil.isBlankCollection(infoList)) {
				String summary = infoList.get(0);
				// System.out.println("summary---"+summary);
				person.setSummary(summary);
			}
		}
		// 取得联系方式
		jsonContentParser = new AnsjPaser(
				"\\{\"ns\":\"pl.content.textMulti.index\",", "\\}\\)", content,
				AnsjPaser.TEXTEGEXANDNRT);
		jsonContent = jsonContentParser.getText();
		if (!StringOperatorUtil.isBlank(jsonContent)) {
			myJSON.createJson("{" + jsonContent + "}");
			jsonContent = myJSON.getStringByKey("html");
			// System.out.println("jsonContent----" + jsonContent);

			selList.clear();
			selList.add("li");
			List<String> infoList = VaoLanHtmlParser.getNodeContentBySelector(
					jsonContent, selList, DataFormatStatus.TagAllContent);

			if (!StringOperatorUtil.isBlankCollection(infoList)) {
				for (String item : infoList) {
					String imageString = VaoLanHtmlParser.getTagContent(item,
							"img", DataFormatStatus.TagAllContent);
					// System.out.println("imageString   " + imageString);
					if (!StringOperatorUtil.isBlank(imageString)) {
						if (imageString.contains("contact2")) {// contact2代表邮箱
							person.setEmail(VaoLanHtmlParser.getCleanTxt(item));
						} else if (imageString.contains("contact4")) {// contact4代表地址
							person.setAddress(VaoLanHtmlParser
									.getCleanTxt(item));
						}
					}
				}
			}
		}

		// 取得企业帐户的标签
		// PRF_text_e
		jsonContentParser = new AnsjPaser("\"domid\":\"Pl_Core_LeftTag__26\",",
				"\\}\\)", content, AnsjPaser.TEXTEGEXANDNRT);
		jsonContent = jsonContentParser.getText();
		if (!StringOperatorUtil.isBlank(jsonContent)) {
			myJSON.createJson("{" + jsonContent + "}");
			jsonContent = myJSON.getStringByKey("html");
			// System.out.println("tag jsonContent----" + jsonContent);

			selList.clear();
			selList.add("a");
			List<String> infoList = VaoLanHtmlParser.getNodeContentBySelector(
					jsonContent, selList, DataFormatStatus.CleanTxt);

			if (!StringOperatorUtil.isBlankCollection(infoList)) {
				for (String tagName : infoList) {
					// System.out.println("tagName   " + tagName);
					tagsInfo += tagName + "#";
				}
				if (!StringOperatorUtil.isBlank(tagsInfo)) {
					tagsInfo = tagsInfo.substring(0, tagsInfo.length() - 1);
				}
				// logger.info("tagsInfo-----" + tagsInfo);
				person.setTags(tagsInfo);
			}
		}
		person.setUpdateTime(new Date());
		person.setUrl(sendUrl);

		person.setGrab_method("enterprise");
		// 将long型数据加入至uid_long
		person.setUid_long(Long.parseLong(person.getUid()));

		if (this.urlPojo.getUrlSource().startsWith("h")) {
			personInfoManager.savePersonInfo(true, person);
		} else {
			if (this.urlPojo.getUrlSource().startsWith("s")) {
				// 此时说明系统管理员特殊添加，做特别处理
				personInfoManager.savePersonInfo4System(person);
			} else {
				personInfoManager.savePersonInfo(false, person);
			}
		}
		// logger.info("person info-----------" + person);
		System.out.println("person info-----------" + person);
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

	public String getUid() {
		int error_repeat_times = 0;

		while (error_repeat_times < StaticValue.url_repeat_count) {
			try {
				content = grabPageSource.getPageSourceOfSina(produceURL, null);
				/**
				 * 对是否是出现网络请求繁忙页做测试，如果出现则直接进行帐户切换
				 */
				String title=htmlParserUtil.getTitleByLine(content);
				if ((!StringOperatorUtil.isBlank(title)) && title.contains("错误提示")) {
					logger.info("发现请求繁忙，将进行一次线程中断!");
					SwtichUserThreadManager.interrupte();
					try {
						Thread.sleep(2000);
						logger.info("打断切换线程后，先sleep一会儿!");	
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					logger.info("打断线程后的sleep完成，即将进入抓取!");
				}else {
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
		beginRegex = "CONFIG\\[\'oid\'\\]=\'";
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
