package com.weibo.sina.cookie.profession;

import java.util.Date;

import net.sf.json.JSONObject;

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
import com.weibo.common.controler.SpiderControler;
import com.weibo.common.utils.AnsjPaser;
import com.weibo.common.utils.StaticValue;
import com.weibo.sina.cookie.common.GrabPageResource;
import com.weibo.utils.sina.MyStringUtils;
import com.weibo.utils.sina.SinaVersionChangeUtil;

@Component
@Scope(value = "prototype")
public class GrabProPersonInfo {
	public static Logger logger = Logger.getLogger(GrabProPersonInfo.class);
	private GrabPageResource grabPageSource;
	@Autowired
	private PersonInfoManager personInfoManager;
	private PersonInfo person;
	private UrlPojo urlPojo;
	private String sendUrl;
	SinaVersionChangeUtil sinaVersionChangeUtil = new SinaVersionChangeUtil();
	private String produceURL;

	public String getSendUrl() {
		return sendUrl;
	}

	public void setSendUrl(String sendUrl) {
		this.sendUrl = sendUrl;
	}

	@Autowired
	private MyJson myJSON = new MyJson();

	public GrabProPersonInfo() {
	}

	public UrlPojo getUrlPojo() {
		return urlPojo;
	}

	public void setUrlPojo(UrlPojo urlPojo) {
		this.urlPojo = urlPojo;
	}

	String content = null;
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
	AnsjPaser summaryBlock = null;
	String summaryBoockText = null;
	AnsjPaser ansjProfession = null;
	String profession = null;
	AnsjPaser ansjAttention = null;
	String attentionNumber = null;
	AnsjPaser ansjFans = null;
	String fansNumber = null;
	AnsjPaser ansjWB = null;
	String wbNumber = null;

	// 在校园版、机构版的自定义博主的类型时起区分作用
	String verify_type = null;
	String pid = null;

	public void saveProfessionPersonInfo() throws Exception {
		if (StringUtils.isBlank(sendUrl)) {
			logger.info("---url--不可为空!");
			throw new Exception();
		}
		content = grabPageSource.getPageSourceOfSina(produceURL, null);
		if (content == null) {
			logger.info(sendUrl + " OPEN ERROR !");
			throw new Exception();
		} else {
			grabProPersonInfoPage(content);
			Thread.sleep(StaticValue.cookie_sleep_interval_time);
			return;
		}
	}

	public void grabProPersonInfoPage(String content) throws Exception {
		UserJudge = new AnsjPaser("<div class=\"error\">", "</div>", content,
				"[\\s\\S]*?");
		judgeText = UserJudge.getText();
		if (judgeText != null && judgeText.trim().contains("抱歉")) {
			logger.info(this.sendUrl + "该Url未找到!");
			throw new Exception();
		}

		beginRegex = "CONFIG\\['oid'\\] = '";
		endRegex = "'";
		ansjId = new AnsjPaser(beginRegex, endRegex, content, "[\\s\\S]*?");
		person_uid = ansjId.getText();
		if (person_uid == null || person_uid.length() == 0)
			throw new Exception();
		if (person_uid.equals(SpiderControler.isRunningAccountUid4Sina)) {
			logger.info(sendUrl + " --估计是遇到错误了，回到root抓取用户了");
			throw new Exception();
		}
		person.setUid(person_uid);
		// logger.info("uid---" + person_uid);

		// 取得pid
		beginRegex = "CONFIG\\[\'pid\'\\]\\s=\\s\'";
		endRegex = "\'";
		AnsjPaser pidParser = new AnsjPaser(beginRegex, endRegex,
				content, AnsjPaser.TEXTEGEXANDNRT);
		pid = pidParser.getText();

		// 提取出verify_type,为设置自定义的属性作准备，暂只在校园版、机构版等的情况下有区分作用
		// 在此处提取网页中的page_id
		beginRegex = "CONFIG\\[\'verified_type\'\\]\\s=\\s\'";
		endRegex = "\'";
		AnsjPaser verifyTypeParser = new AnsjPaser(beginRegex, endRegex,
				content, AnsjPaser.TEXTEGEXANDNRT);
		verify_type = verifyTypeParser.getText();
		person.setVerifyType(verify_type);

		// 在个人版中，将domain_id作为pid,对应到AccountTypeRelationPojo对象的相应的key中
		String accountType = AccountTypeRleationPojo.getAccountType(pid,
				verify_type);
		person.setAccountType(accountType);

		beginRegex = "CONFIG\\['onick'\\] = \"";
		endRegex = "\"";
		ansjName = new AnsjPaser(beginRegex, endRegex, content, "[\\s\\S]*?");
		person_name = ansjName.getText();
		person.setName(person_name);
		// logger.info("person_name---" + person_name);
		person.setSex("8");
		infoBlock = new AnsjPaser("\\{\"pid\":\"pl_content_litePersonInfo\",",
				"\\}\\)", content, "[\\s\\S]*?");
		infoBoockText = infoBlock.getText();
		if (infoBoockText != null)
			infoBoockText = JSONObject.fromObject(("{" + infoBoockText + "}"))
					.get("html").toString();
		// logger.info("infoBoockText---" + infoBoockText);
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
		// logger.info("enterName---" + enterName);
		// 不同于以前任一版本的summary获取方式
		summaryBlock = new AnsjPaser("\\{\"pid\":\"pl_content_noticeBoard\",",
				"\\}\\)", content, "[\\s\\S]*?");
		summaryBoockText = summaryBlock.getText();

		myJSON.createJson("{" + summaryBoockText + "}");
		summaryBoockText = myJSON.getStringByKey("html");
		// logger.info("summaryBoockText---" + summaryBoockText);
		beginRegex = "<p class=\"cont_words\">";
		endRegex = "</p>";
		ansjProfession = new AnsjPaser(beginRegex, endRegex, summaryBoockText,
				"[\\s\\S]*?");
		profession = ansjProfession.getText();
		if (profession == null) {
			profession = "还没写简介";
		} else {
			profession = profession.replaceAll(StaticValue.Specical_Char, "");
			profession = profession.trim();
		}
		person.setSummary(profession);
		// logger.info("profession---" + profession);
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
		person.setGzNum(Integer.valueOf(Integer.parseInt(attentionNumber)));
		beginRegex = "fans\"><strong>";
		endRegex = "</strong>";
		ansjFans = new AnsjPaser(beginRegex, endRegex, blockText, "[\\s\\S]*?");
		fansNumber = ansjFans.getText();
		person.setFansNum(Integer.valueOf(Integer.parseInt(fansNumber)));
		beginRegex = "type=0\"><strong>";
		endRegex = "</strong>";
		ansjWB = new AnsjPaser(beginRegex, endRegex, blockText, "[\\s\\S]*?");
		wbNumber = ansjWB.getText();
		person.setWbNum(Integer.valueOf(Integer.parseInt(wbNumber)));
		person.setUpdateTime(new Date());
		person.setUrl(sendUrl);
		person.setGrab_method("profession");
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
			personInfoManager.savePersonInfo(false, person);
		}
		System.out.println("professionPersonInfo info-----------" + person);
		// System.out.println("professionPersonInfo save successful");
	}

	public void init(PersonInfo person, GrabPageResource grabPageResource,
			UrlPojo urlPojo) {
		this.person = person;
		grabPageSource = grabPageResource;
		this.urlPojo = urlPojo;
		sendUrl = MyStringUtils.getUrlRemoveSlash(urlPojo.getUrl());
		produceURL = sinaVersionChangeUtil.getEnterAndProUrl(sendUrl);
	}

	public void init(GrabPageResource grabPageResource, UrlPojo urlPojo) {
		grabPageSource = grabPageResource;
		this.urlPojo = urlPojo;
		sendUrl = MyStringUtils.getUrlRemoveSlash(urlPojo.getUrl());
		produceURL = sinaVersionChangeUtil.getEnterAndProUrl(sendUrl);
	}

	public PersonInfo getPersonHead() throws Exception {
		content = grabPageSource.getPageSourceOfSina(produceURL, null);

		UserJudge = new AnsjPaser("<div class=\"error\">", "</div>", content,
				"[\\s\\S]*?");
		judgeText = UserJudge.getText();
		if (judgeText != null && judgeText.trim().contains("抱歉")) {
			logger.info(this.sendUrl + "该Url未找到!");
			throw new Exception();
		}
		beginRegex = "CONFIG\\['oid'\\] = '";
		endRegex = "'";
		ansjId = new AnsjPaser(beginRegex, endRegex, content, "[\\s\\S]*?");
		person_uid = ansjId.getText();
		if (person_uid == null || person_uid.length() == 0)
			throw new Exception();
		if (person_uid.equals(SpiderControler.isRunningAccountUid4Sina)) {
			logger.info(sendUrl + " --估计是遇到错误了，回到root抓取用户了");
			throw new Exception();
		}
		person.setUid(person_uid);
		beginRegex = "CONFIG\\['onick'\\] = \"";
		endRegex = "\"";
		ansjName = new AnsjPaser(beginRegex, endRegex, content, "[\\s\\S]*?");
		person_name = ansjName.getText();
		person.setName(person_name);

		return person;
	}
}
