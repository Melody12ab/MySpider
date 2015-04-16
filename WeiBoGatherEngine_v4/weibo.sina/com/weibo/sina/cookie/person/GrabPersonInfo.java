package com.weibo.sina.cookie.person;

import java.net.SocketTimeoutException;
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
import com.mbm.util.StringOperatorUtil;
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
public class GrabPersonInfo {
	public static Logger logger = Logger.getLogger(GrabPersonInfo.class);
	private GrabPageResource grabPageSource;
	@Autowired
	private PersonInfoManager personInfoManager;
	private PersonInfo person = null;
	private UrlPojo urlPojo;
	private String sendUrl;
	private String produceURL;
	SinaVersionChangeUtil sinaVersionChangeUtil = new SinaVersionChangeUtil();
	/**
	 * 此处所有变量的声明都是为数据分析来声明--开始
	 */
	AnsjPaser UserJudge = null;// 判断用户是否存在的parser
	String judgeText = null;// 判断用户是否存在的text
	String beginRegex = null;// 正则表达式的开始
	String endRegex = null;// 正则表达式的结束

	AnsjPaser ansjName = null;// 提取名称的parser
	AnsjPaser ansjId = null; // 提取id的parser
	AnsjPaser schoolPaser = null;// 提取school的parser
	AnsjPaser infoBlock = null;// 提取基本信息的parser
	AnsjPaser ansjonline = null; // 提取主页地址的parser
	AnsjPaser ansjAddress = null;// 提取地址的parser

	String person_uid = null; // 提取person_uid的string
	String sexText = null; // 提取sex的string
	String person_name = null; // 提取name的string
	String online = null; // 提取主页地址的string
	String address = null;// 提取址址的string

	AnsjPaser ansjblog = null;
	String blog = null;
	AnsjPaser ansjSummaryOne = null;
	String summaryOne = null;
	AnsjPaser ansjRenZhOne = null;
	String ansjRenzhengText = null;
	AnsjPaser relationship = null;
	String relationshipText = null;
	AnsjPaser realNameOne = null;
	String realName = null;
	AnsjPaser ansjFansNumOne = null;
	String ansjStr = null;
	AnsjPaser wbNumText = null;
	String tempWbNum = null;
	AnsjPaser ansjGzNum = null;
	AnsjPaser dataFile = null;
	String dataPaserText = null;
	AnsjPaser ansjBir = null;
	AnsjPaser ansjMail = null;
	AnsjPaser ansjQQ = null;
	AnsjPaser ansjMSN = null;
	AnsjPaser ansjEdu = null;
	String edu = null;
	AnsjPaser workPaser = null;
	String tempWork = null;
	String content = null;// 通过URL得到的源码

	/**
	 * 此处所有变量的声明都是为数据分析来声明--结束
	 */

	public UrlPojo getUrlPojo() {
		return urlPojo;
	}

	public void setUrlPojo(UrlPojo urlPojo) {
		this.urlPojo = urlPojo;
	}

	public void savePersonInfo() throws Exception {
		if (StringUtils.isBlank(sendUrl)) {
			System.out.println("---url不可为空");
			throw new Exception();
		}
		content = grabPageSource.getPageSourceOfSina(produceURL, null);
		if (content == null) {
			logger.info(sendUrl + " OPEN ERROR !");
			throw new Exception();
		}
		grabPersonInfoPage(content);
		Thread.sleep(StaticValue.cookie_sleep_interval_time);
	}

	public void grabPersonInfoPage(String content) throws Exception {
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
		// uid不为空，则不抽取了，若为空，则要抽取网页中的uid
		beginRegex = "CONFIG\\[\'oid\'\\] = \'";
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
		if (person_uid.equals(SpiderControler.isRunningAccountUid4Sina)
				&& !this.sendUrl.equals(FormatOutput.urlPrefix
						+ SpiderControler.isRunningAccountUid4Sina)) {
			logger.info(this.sendUrl + " --该url地址有误");
			throw new Exception();
		}
		person.setUid(person_uid);

		// 姓名抽取
		beginRegex = "CONFIG\\[\'onick\'\\] = \'";
		endRegex = "\'";
		ansjName = new AnsjPaser(beginRegex, endRegex, content,
				AnsjPaser.TEXTEGEXANDNRT);
		person_name = ansjName.getText();
		if (person_name == null || person_name.trim().length() == 0) {
			logger.info("没有提取出person_name，直接抛出异常!");
			throw new Exception();
		}
		person.setName(person_name);
		// 性别抽取
		beginRegex = "\\$CONFIG\\[\\'ogender\\'\\] = '";
		endRegex = "'";

		schoolPaser = new AnsjPaser(beginRegex, endRegex, content,
				AnsjPaser.TEXTEGEXANDNRT);
		sexText = schoolPaser.getText();
		if (sexText != null) {
			if (sexText.equals("m")) {
				person.setSex("1");
			} else if (sexText.equals("f")) {
				person.setSex("2");
			} else {
				person.setSex("0");
			}
		}
		// 基本信息块
		infoBlock = new AnsjPaser("\\{\"pid\":\"pl_content_hisPersonalInfo\",",
				"\\}\\)", content, AnsjPaser.TEXTEGEXANDNRT);
		String infoBoockText = infoBlock.getText();
		if (infoBoockText != null) {
			infoBoockText = JSONObject.fromObject("{" + infoBoockText + "}")
					.get("html").toString();
			// System.out.println("解析后的infoBoockText----------"+infoBoockText);

			// 主页保留地址
			ansjonline = new AnsjPaser("class=\"online\">", "<", infoBoockText,
					AnsjPaser.TEXTEGEXANDNRT);

			online = ansjonline.getText();
			// 地址抽取
			ansjAddress = new AnsjPaser(
					"<img width=\"11\" height=\"12\" class=\".*?>", "<",
					infoBoockText, AnsjPaser.TEXTEGEXANDNRT);
			address = ansjAddress.getText();
			if (StringUtils.isNotBlank(address))
				person.setAddress(address.replace("&nbsp;", ""));
			// 博客：
			ansjblog = new AnsjPaser("博客：<a href=\"", "\"", infoBoockText,
					AnsjPaser.TEXTEGEXANDNRT);
			blog = ansjblog.getText();
			person.setBlogUrl(blog);
			person.setWeiboType("1");
			// 简介
			ansjSummaryOne = new AnsjPaser(
					"<img width=\"11\" height=\"12\" class=\".*?>",
					"<div class=\"concern clearfix\">", infoBoockText,
					AnsjPaser.TEXTEGEXANDNRT);
			summaryOne = ansjSummaryOne.getText();
			if (StringUtils.isNotBlank(summaryOne)) {
				if (address != null) {
					summaryOne = summaryOne.replace(address, "");
				}
				if (blog != null) {
					summaryOne = summaryOne.replace(blog, "")
							.replace("博客：", "");
				}
				summaryOne = summaryOne.replaceAll("\\r|\\n|\\t", "")
						.replaceAll("</?[^>]+>", "");
				summaryOne = summaryOne.replaceAll(StaticValue.Specical_Char,
						"");
				person.setSummary(summaryOne.trim());
				// logger.info("summary------"+summaryOne.trim());
			}

			// 认证关系
			ansjRenZhOne = new AnsjPaser(
					"<div class=\"left\">[\\s\\S]*? title= \"", "\"",
					infoBoockText, AnsjPaser.TEXTEGEXANDNRT);
			ansjRenzhengText = ansjRenZhOne.getText();
			if (ansjRenzhengText != null) {
				person.setRenZh(MyStringUtils.getUserType(ansjRenzhengText
						.trim()));
			} else {
				person.setRenZh(0);
			}

			// 个人关系块
			relationship = new AnsjPaser(
					"\\{\"pid\":\"pl_content_litePersonInfo\",", "\\}\\)",
					content, AnsjPaser.TEXTEGEXANDNRT);
			relationshipText = relationship.getText();
			relationshipText = JSONObject.fromObject(
					"{" + relationshipText + "}").get("html").toString();
			// System.out.println(relationshipText);
			// 认证名称
			realNameOne = new AnsjPaser("<dt><span title=\"", "\"",
					relationshipText, AnsjPaser.TEXTEGEXANDNRT);
			realName = realNameOne.getText();
			if (StringUtils.isNotBlank(realName)) {
				person.setRealName(realName);
			} else {
				person.setRealName(null);
			}
			// 粉丝数目
			beginRegex = "<strong node-type=\"fans\">";
			endRegex = "<";
			ansjFansNumOne = new AnsjPaser(beginRegex, endRegex,
					relationshipText, AnsjPaser.TEXTEGEXANDNRT);
			ansjStr = ansjFansNumOne.getText();
			// ansjFansNum.reset(string);

			if (StringUtils.isBlank(ansjStr)) {
			} else {
				person.setFansNum(Integer.parseInt(ansjStr.trim()));
			}
			// 微博数量
			beginRegex = "<strong node-type=\"weibo\">";
			endRegex = "<";
			wbNumText = new AnsjPaser(beginRegex, endRegex, relationshipText,
					AnsjPaser.TEXTEGEXANDNRT);
			tempWbNum = wbNumText.getText();

			if (StringUtils.isBlank(tempWbNum)) {

			} else {
				person.setWbNum(Integer.parseInt(tempWbNum.trim()));
			}

			// 关注数量
			beginRegex = "<strong node-type=\"follow\">";
			endRegex = "<";
			ansjGzNum = new AnsjPaser(beginRegex, endRegex, relationshipText,
					AnsjPaser.TEXTEGEXANDNRT);
			judgeText = ansjGzNum.getText();
			if (StringUtils.isBlank(judgeText)) {
			} else {
				person.setGzNum(Integer.parseInt(judgeText.trim()));
			}
		}
		// 档案块
		dataFile = new AnsjPaser("\\{\"pid\":\"pl_content_hisData\",",
				"\\}\\)", content, AnsjPaser.TEXTEGEXANDNRT);
		dataPaserText = dataFile.getText();
		// System.out.println("dataPaserText-----------------"+dataPaserText);
		if (dataPaserText != null) {
			dataPaserText = JSONObject.fromObject("{" + dataPaserText + "}")
					.get("html").toString();
			dataPaserText = dataPaserText.replaceAll("\\r|\\n|\\t", "");

			if (StringUtils.isNotBlank(dataPaserText)) {
				// 生日抽取
				beginRegex = "生日：";
				endRegex = "<";
				ansjBir = new AnsjPaser(beginRegex, endRegex, dataPaserText,
						AnsjPaser.TEXTEGEXANDNRT);
				person.setBrithday(ansjBir.getText());
				// 邮箱抽取
				beginRegex = "邮箱：";
				endRegex = "<";
				ansjMail = new AnsjPaser(beginRegex, endRegex, dataPaserText,
						AnsjPaser.TEXTEGEXANDNRT);
				person.setEmail(ansjMail.getText());
				// qq抽取
				beginRegex = "QQ：";
				endRegex = "<";
				ansjQQ = new AnsjPaser(beginRegex, endRegex, dataPaserText,
						AnsjPaser.TEXTEGEXANDNRT);
				person.setQq(ansjQQ.getText());
				// msn抽取
				beginRegex = "MSN：";
				endRegex = "<";
				ansjMSN = new AnsjPaser(beginRegex, endRegex, dataPaserText,
						AnsjPaser.TEXTEGEXANDNRT);
				person.setMsn(ansjMSN.getText());
				// 得到教育及工作情况

				ansjEdu = new AnsjPaser("教育信息", "</div>", dataPaserText,
						AnsjPaser.TEXTEGEXANDNRT);
				edu = ansjEdu.getText();
				if (StringUtils.isNotBlank(edu)) {
					edu = edu.replaceAll("</?[^>]+>", " ");
					person.setEdu(edu.trim());
				}
				workPaser = new AnsjPaser("<strong>工作信息", "</div>",
						dataPaserText, AnsjPaser.TEXTEGEXANDNRT);
				tempWork = workPaser.getText();
				if (StringUtils.isNotBlank(tempWork)) {
					tempWork = tempWork.replaceAll("</?[^>]+>", " ");
					person.setWork(tempWork.trim());
				}
			}
		}
		person.setUpdateTime(new Date());
		person.setUrl(sendUrl);

		person.setGrab_method("cookies");
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
		// logger.info("person info-----------" + person);
		// logger.info("person save successful!");
		System.out.println("person save successful!");
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
	private HtmlParserUtil htmlParserUtil=new HtmlParserUtil();
	public String getUid() {
		int error_repeat_times = 0;

		while (error_repeat_times < StaticValue.url_repeat_count) {
			try {
				content = grabPageSource.getPageSourceOfSina(this.produceURL, null);
				
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
		content = grabPageSource.getPageSourceOfSina(this.produceURL, null);
		if (StaticValue.log_output_enable) {
			logger.info(content);
		}
		// System.out.println("content-----------------------"+content);
		if (content == null) {
			logger.info(sendUrl + " OPEN ERROR !");
			return null;
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
			// logger.info("没有提取出person_name，直接抛出异常!");
			System.out.println("没有提取出person_name，直接抛出异常!");
			throw new Exception();
		}

		person.setName(person_name);
		// System.out.println("抽取出来的name---------------" + person_name);
		/**
		 * 判断是哪种URL，现在个人版、专业版均可取到此处
		 */
		// AnsjPaser infoBlock = new AnsjPaser(
		// "\\{\"pid\":\"pl_content_litePersonInfo\",", "\\}\\)", content,
		// "[\\s\\S]*?");
		// String infoBoockText = infoBlock.getText();
		// if (infoBoockText != null)
		// infoBoockText = JSONObject.fromObject(("{" + infoBoockText + "}"))
		// .get("html").toString();
		// beginRegex = "class=\"W_textb\">";
		// endRegex = "</dd>";
		// AnsjPaser blockDiv = new AnsjPaser(beginRegex, endRegex,
		// infoBoockText,
		// "[\\s\\S]*?");
		// String blockText = blockDiv.getText();
		// if(blockText!=null){
		// logger.info("此时的URL为非个人版，将抛出异常!");
		// }
		return person;
	}

}
