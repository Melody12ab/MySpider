package com.weibo.sina.cookie.enterprise;

import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mbm.elint.entity.PersonInfo;
import com.mbm.elint.entity.util.UrlPojo;
import com.mbm.elint.manager.database.PersonInfoManager;
import com.weibo.common.controler.SpiderControler;
import com.weibo.common.utils.AnsjPaser;
import com.weibo.common.utils.FormatOutput;
import com.weibo.common.utils.StaticValue;
import com.weibo.sina.cookie.common.GrabPageResource;
import com.weibo.utils.sina.MyStringUtils;
import com.weibo.utils.sina.SinaVersionChangeUtil;

@Component
@Scope(value = "prototype")
public class GrabEnterPersonInfo {
	public static Logger logger = Logger.getLogger(GrabEnterPersonInfo.class);
	private GrabPageResource grabPageSource;
	@Autowired
	private PersonInfoManager personInfoManager;
	private PersonInfo person;
	private UrlPojo urlPojo;
	private String sendUrl;
	private String produceURL;

	public String getSendUrl() {
		return sendUrl;
	}

	public void setSendUrl(String sendUrl) {
		this.sendUrl = sendUrl;
	}

	SinaVersionChangeUtil sinaVersionChangeUtil = new SinaVersionChangeUtil();

	public GrabEnterPersonInfo() {
	}

	public UrlPojo getUrlPojo() {
		return urlPojo;
	}

	public void setUrlPojo(UrlPojo urlPojo) {
		this.urlPojo = urlPojo;
	}

	String content = null;

	public void saveEnterprisePersonInfo() throws Exception {
		if (StringUtils.isBlank(sendUrl)) {
			logger.info("---url--不可为空!");
			throw new Exception();
		}
		content = grabPageSource.getPageSourceOfSina(produceURL, null);
		if (content == null) {
			logger.info(sendUrl + " OPEN ERROR !");
			throw new Exception();
		} else {
			grabEnterprisePersonInfoPage(content);
			return;
		}
	}

	AnsjPaser UserJudge = null;
	String judgeText = null;
	String beginRegex = null;
	String endRegex = null;
	AnsjPaser ansjId = null;
	String person_uid = null;
	AnsjPaser ansjName = null;
	String person_name = null;
	AnsjPaser infoBlock = null;
	String infoBoockText = null;
	AnsjPaser blockDiv = null;
	String blockText = null;
	AnsjPaser ansjEnterName = null;
	String enterName = null;
	AnsjPaser ansjProfession = null;
	String profession = null;
	AnsjPaser ansjAttention = null;
	String attentionNumber = null;
	AnsjPaser ansjFans = null;
	String fansNumber = null;
	AnsjPaser ansjWB = null;
	String wbNumber = null;

	public void grabEnterprisePersonInfoPage(String content) throws Exception {
		UserJudge = new AnsjPaser("<div class=\"error\">", "</div>", content,
				"[\\s\\S]*?");
		judgeText = UserJudge.getText();
		if (judgeText != null && judgeText.trim().contains("抱歉")) {
			logger.info(this.sendUrl + "该Url未找到!");
			throw new Exception();
		}
		beginRegex = "CONFIG\\['oid'\\]='";
		endRegex = "'";
		ansjId = new AnsjPaser(beginRegex, endRegex, content, "[\\s\\S]*?");
		person_uid = ansjId.getText();
		if (person_uid == null || person_uid.length() == 0)
			throw new Exception();
		if (person_uid.equals(SpiderControler.isRunningAccountUid4Sina)
				&& !sendUrl.equals((new StringBuilder(String
						.valueOf(FormatOutput.urlPrefix))).append(
						SpiderControler.isRunningAccountUid4Sina).toString())) {
			logger.info(sendUrl + " --估计是遇到错误了，回到root抓取用户了");
			throw new Exception();
		}
		person.setUid(person_uid);
		beginRegex = "CONFIG\\['onick'\\]=\'";
		endRegex = "\'";
		ansjName = new AnsjPaser(beginRegex, endRegex, content, "[\\s\\S]*?");
		person_name = ansjName.getText();
		person.setName(person_name);
		person.setSex("7");

		infoBlock = new AnsjPaser("\\{\"pid\":\"pl_content_litePersonInfo\",",
				"\\}\\)", content, "[\\s\\S]*?");
		infoBoockText = infoBlock.getText();
		if (infoBoockText != null)
			infoBoockText = JSONObject.fromObject(
					(new StringBuilder("{")).append(infoBoockText).append("}")
							.toString()).get("html").toString();
		beginRegex = "class=\"W_textb\">";
		endRegex = "</dd>";
		blockDiv = new AnsjPaser(beginRegex, endRegex, infoBoockText,
				"[\\s\\S]*?");
		blockText = blockDiv.getText();
		beginRegex = "<p>";
		endRegex = "</p>";
		ansjEnterName = new AnsjPaser(beginRegex, endRegex, blockText,
				"[\\s\\S]*?");
		enterName = ansjEnterName.getText();
		person.setRealName(enterName);
		beginRegex = "class=\"W_texta\">";
		endRegex = "</dd>";
		blockDiv = new AnsjPaser(beginRegex, endRegex, infoBoockText,
				"[\\s\\S]*?");
		blockText = blockDiv.getText();
		beginRegex = "<p>";
		endRegex = "</p>";
		ansjProfession = new AnsjPaser(beginRegex, endRegex, blockText,
				"[\\s\\S]*?");
		profession = ansjProfession.getText();
		profession = profession.substring(3, profession.length());
		profession = profession.replaceAll(StaticValue.Specical_Char, "");
		person.setSummary(profession);
		beginRegex = "class=\"user_atten clearfix\">";
		endRegex = "</ul>";
		blockDiv = new AnsjPaser(beginRegex, endRegex, infoBoockText,
				"[\\s\\S]*?");
		blockText = blockDiv.getText();
		beginRegex = "follow\"><strong>";
		endRegex = "</strong>";
		ansjAttention = new AnsjPaser(beginRegex, endRegex, blockText,
				"[\\s\\S]*?");
		attentionNumber = ansjAttention.getText();
		person.setGzNum(Integer.parseInt(attentionNumber));
		beginRegex = "fans\"><strong>";
		endRegex = "</strong>";
		ansjFans = new AnsjPaser(beginRegex, endRegex, blockText, "[\\s\\S]*?");
		fansNumber = ansjFans.getText();
		person.setFansNum(Integer.parseInt(fansNumber));
		beginRegex = "type=0\"><strong>";
		endRegex = "</strong>";
		ansjWB = new AnsjPaser(beginRegex, endRegex, blockText, "[\\s\\S]*?");
		wbNumber = ansjWB.getText();
		person.setWbNum(Integer.parseInt(wbNumber));
		person.setUpdateTime(new Date());
		person.setUrl(sendUrl);
		person.setGrab_method("enterprise");
		person.setRenZh(1);
		person.setWeiboType("1");
		/**
		 * person对象暂为空，故先注释
		 */
		// 将long型数据加入至uid_long
		person.setUid_long(Long.parseLong(person.getUid()));

		if (this.urlPojo.getUrlSource().startsWith("h")) {
			personInfoManager.savePersonInfo(true, person);
		} else {
			if (this.urlPojo.getUrlSource().startsWith("s")) {// 此时说明系统管理员特殊添加，做特别处理
				personInfoManager.savePersonInfo4System(person);
			} else {
				personInfoManager.savePersonInfo(false, person);
			}
		}
		// logger.info("enterprisePersonInfo info-----------" + person);
		System.out.println("enterprisePersonInfo info save successful");
	}

	public void init(PersonInfo person, GrabPageResource grabPageResource,
			UrlPojo urlPojo) {
		this.person = person;
		grabPageSource = grabPageResource;
		this.urlPojo = urlPojo;
		sendUrl = MyStringUtils.getUrlRemoveSlash(urlPojo.getUrl());
//		this.produceURL = sinaVersionChangeUtil.getEnterAndProUrl(sendUrl)
//				+ "/map";
		this.produceURL = this.sendUrl;
	}

	public void init(GrabPageResource grabPageResource, UrlPojo urlPojo) {
		grabPageSource = grabPageResource;
		this.urlPojo = urlPojo;
		sendUrl = MyStringUtils.getUrlRemoveSlash(urlPojo.getUrl());
		// this.produceURL = sinaVersionChangeUtil.getEnterAndProUrl(sendUrl)
		// + "/map";
		this.produceURL = this.sendUrl;
	}

	public PersonInfo getPersonHead() throws Exception {
		content = grabPageSource.getPageSourceOfSina(produceURL, null);
//		System.out.println("企业版的content---"+content);
		/**
		 * enterprise与profession之间的正则通用问题
		 */
//		infoBlock = new AnsjPaser("\\{\"pid\":\"pl_content_litePersonInfo\",",
//				"\\}\\)", content, "[\\s\\S]*?");
//		infoBoockText = infoBlock.getText();
//		if (infoBoockText != null)
//			infoBoockText = JSONObject.fromObject(
//					(new StringBuilder("{")).append(infoBoockText).append("}")
//							.toString()).get("html").toString();
//		beginRegex = "class=\"W_texta\">";
//		endRegex = "</dd>";
//		blockDiv = new AnsjPaser(beginRegex, endRegex, infoBoockText,
//				"[\\s\\S]*?");
//		blockText = blockDiv.getText();
//		if (blockText == null || blockText.trim().length() == 0) {
//			logger.info("在媒体版中没有取到summary部分，说明此url是专业版的url");
//			throw new Exception();
//		}
		UserJudge = new AnsjPaser("<div class=\"error\">", "</div>", content,
				"[\\s\\S]*?");
		judgeText = UserJudge.getText();
		if (judgeText != null && judgeText.trim().contains("抱歉")) {
			logger.info(this.sendUrl + "该Url未找到!");
			throw new Exception();
		}
		beginRegex = "CONFIG\\['oid'\\]='";
		endRegex = "'";
		ansjId = new AnsjPaser(beginRegex, endRegex, content, "[\\s\\S]*?");
		person_uid = ansjId.getText();
		if (person_uid == null || person_uid.isEmpty())
			throw new Exception();
		if (person_uid.equals(SpiderControler.isRunningAccountUid4Sina)) {
			logger.info(sendUrl + " --估计是遇到错误了，回到root抓取用户了");
			throw new Exception();
		}
		person.setUid(person_uid);
		beginRegex = "CONFIG\\['onick'\\]='";
		endRegex = "'";
		ansjName = new AnsjPaser(beginRegex, endRegex, content, "[\\s\\S]*?");
		person_name = ansjName.getText();
		person.setName(person_name);

		return person;
	}
}
