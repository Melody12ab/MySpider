package com.weibo.qq.cookie.person;

import java.util.Date;

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
import com.weibo.qq.cookie.common.GrabPageSource4QQ;
import com.weibo.utils.qq.StaticValue4QQ;
import com.weibo.utils.qq.UidGeneratorUtil;
import com.weibo.utils.sina.MyStringUtils;

@Component
@Scope(value = "prototype")
public class GrabPersonInfo4QQ {
	public static Logger logger = Logger.getLogger(GrabPersonInfo4QQ.class);
	private GrabPageSource4QQ grabPageSource4QQ;
	@Autowired
	private PersonInfoManager personInfoManager;
	@Autowired
	private UidGeneratorUtil uidGeneratorUtil;
	private PersonInfo person = null;
	private UrlPojo urlPojo;
	private String uid;
	private String urlByUid;
	private String Req_URL;

	public String getReq_URL() {
		return Req_URL;
	}

	public void setReq_URL(String reqURL) {
		Req_URL = reqURL;
	}

	public String getUrlByUid() {
		return urlByUid;
	}

	public void setUrlByUid(String urlByUid) {
		this.urlByUid = urlByUid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public UrlPojo getUrlPojo() {
		return urlPojo;
	}

	public void setUrlPojo(UrlPojo urlPojo) {
		this.urlPojo = urlPojo;
	}

	public void savePersonInfo() throws Exception {
		if (StringUtils.isBlank(uid)) {
			logger.info("---url不可为空");
			throw new Exception();
		}
		String content = grabPageSource4QQ.getPageSourceOfQQ(this.Req_URL,
				null, StaticValue4QQ.QQ_PersonInfo_Host);
		if (content == null) {
			logger.info(urlByUid + " OPEN ERROR !");
			throw new Exception();
		}
		// logger.info("htmlSource--" + content);
		grabPersonInfoPage(content);
	}

	public void grabPersonInfoPage(String content) throws Exception {
		// 测试用户是否存在
		AnsjPaser UserJudge = new AnsjPaser("<div class=\"words\">", "</div",
				content, AnsjPaser.TEXTEGEXANDNRT);
		String judgeText = UserJudge.getText();
		// System.out.println("judgeText------------" + judgeText);
		if (judgeText != null) {
			if (judgeText.trim().contains("用户不存在")) {
				logger.info("抱歉，用户不存在!");
				throw new Exception();
			}
		}
		String beginRegex = null;
		String endRegex = null;

		/**
		 * 先抽取照片等内容的大块
		 */
		beginRegex = "<div class=\"m_profile";
		endRegex = "<script";
		AnsjPaser blockAnsj = new AnsjPaser(beginRegex, endRegex, content,
				AnsjPaser.TEXTEGEXANDNRT);
		String blockDiv = blockAnsj.getText();
		if (blockDiv == null || blockDiv.length() == 0) {
			throw new Exception();
		} else {
			// logger.info("取到的首个blockDiv--" + blockDiv);
		}

		person.setUid(this.uid);

		// 提取uid，其值类似于erliang20088,不可为
		beginRegex = "class=\"avatar\">" + AnsjPaser.TEXTEGEXANDNRT + "src=\"";
		endRegex = "\"";
		AnsjPaser picAnsj = new AnsjPaser(beginRegex, endRegex, blockDiv,
				AnsjPaser.TEXTEGEXANDNRT);
		String pic_url = picAnsj.getText();
		if (pic_url == null || pic_url.length() == 0) {
			throw new Exception();
		}
		// logger.info("pic_url---" + pic_url);
		person.setPic_url(pic_url);

		/**
		 * 判断提交的网址是否正常，若抓取到的是内置的信息，认为提交的网址有错误,当然此时的url也要判断,若非本人的url则返回提交的url错误
		 */
		if (this.uid.equals(SpiderControler.isRunningAccountUid4QQ)
				&& !this.urlByUid.equals(FormatOutput.urlPrefix
						+ SpiderControler.isRunningAccountUid4QQ)) {
			logger.info(this.urlByUid + " --该url地址有误");
			throw new Exception();
		}

		// 姓名抽取
		beginRegex = "<span class=\"text_user\">";
		endRegex = "</span>";
		AnsjPaser ansjName = new AnsjPaser(beginRegex, endRegex, blockDiv,
				AnsjPaser.TEXTEGEXANDNRT);
		String person_name = ansjName.getText();
		if (person_name == null || person_name.trim().length() == 0) {
			logger.info("没有提取出person_name，直接抛出异常!");
			throw new Exception();
		}
		person.setName(person_name);

		/**
		 * 认证关系
		 */
		beginRegex = "<a href=\"http://zhaoren.t.qq.com/people.php"
				+ AnsjPaser.TEXTEGEXANDNRT + "title=\"";
		endRegex = "\"" + AnsjPaser.TEXTEGEXANDNRT + ">";
		AnsjPaser rzAnsj = new AnsjPaser(beginRegex, endRegex, blockDiv,
				AnsjPaser.TEXTEGEXANDNRT);
		String rzStr = rzAnsj.getText();
		// logger.info("认证信息为---" + rzStr);
		if (rzStr != null && rzStr.trim().length() > 0) {
			if (rzStr.contains("腾讯个人认证")) {
				person.setRenZh(1);// 1代表个人认证
			} else if (rzStr.contains("腾讯机构认证")) {
				person.setRenZh(2);// 2代表机构认证
			}
		}

		// 性别抽取
		beginRegex = "<i class=\"ico_";
		endRegex = "\">";
		AnsjPaser sexAnsj = new AnsjPaser(beginRegex, endRegex, blockDiv,
				AnsjPaser.TEXTEGEXANDNRT);
		String sexText = sexAnsj.getText();
		if (sexText != null) {
			if (sexText.equals("male")) {
				person.setSex("1");
			} else if (sexText.equals("female")) {
				person.setSex("2");
			} else if (person.getRenZh() == 2) {
				person.setSex("3");
			} else {
				person.setSex("0");
			}
			// logger.info("sexText--" + sexText);
		}
		// 地址抽取
		// beginRegex = "class=\"info\"" + AnsjPaser.TEXTEGEXANDNRT + "<i"
		// + AnsjPaser.TEXTEGEXANDNRT + "<a" + AnsjPaser.TEXTEGEXANDNRT
		// + ">";
		beginRegex = "class=\"info\"" + AnsjPaser.TEXTEGEXANDNRT + "<a"
				+ AnsjPaser.TEXTEGEXANDNRT + ">";
		endRegex = "</a>";
		AnsjPaser addressAnsj = new AnsjPaser(beginRegex, endRegex, blockDiv,
				AnsjPaser.TEXTEGEXANDNRT);
		String address = addressAnsj.getText();
		// logger.info("address---" + address);
		person.setAddress(address);
		/**
		 * 抽取微博条数、粉丝、关注等内容的div
		 */
		beginRegex = "<div class=\"m_profile m_profile_data bor6 clear";
		endRegex = "</ul";
		blockAnsj = new AnsjPaser(beginRegex, endRegex, content,
				AnsjPaser.TEXTEGEXANDNRT);
		blockDiv = blockAnsj.getText();
		if (blockDiv == null || blockDiv.length() == 0) {
			throw new Exception();
		} else {
			// logger.info("取到的第二个blockDiv--" + blockDiv);
		}

		// 听众，即粉丝的抽取
		beginRegex = "听众" + AnsjPaser.TEXTEGEXANDNRT + "<a"
				+ AnsjPaser.TEXTEGEXANDNRT + ">";
		endRegex = "</a>";
		AnsjPaser fansAnsj = new AnsjPaser(beginRegex, endRegex, blockDiv,
				AnsjPaser.TEXTEGEXANDNRT);
		String fansNum = fansAnsj.getText();
		// logger.info("fansNum---" + fansNum);
		if (fansNum != null && fansNum.trim().length() > 0) {
			person.setFansNum(Integer.parseInt(fansNum));
		} else {
			person.setFansNum(0);
		}
		// 收听，即关注的抽取
		beginRegex = "收听" + AnsjPaser.TEXTEGEXANDNRT + "<a"
				+ AnsjPaser.TEXTEGEXANDNRT + ">";
		endRegex = "</a>";
		AnsjPaser attentionAnsj = new AnsjPaser(beginRegex, endRegex, blockDiv,
				AnsjPaser.TEXTEGEXANDNRT);
		String attentionNum = attentionAnsj.getText();
		// logger.info("attentionNum---" + attentionNum);
		if (attentionNum != null && attentionNum.trim().length() > 0) {
			person.setGzNum(Integer.parseInt(attentionNum));
		} else {
			person.setGzNum(0);
		}
		// 广播，即微博数量的抽取
		beginRegex = "广播" + AnsjPaser.TEXTEGEXANDNRT + "<a"
				+ AnsjPaser.TEXTEGEXANDNRT + ">";
		endRegex = "</a>";
		AnsjPaser wbAnsj = new AnsjPaser(beginRegex, endRegex, blockDiv,
				AnsjPaser.TEXTEGEXANDNRT);
		String wbNum = wbAnsj.getText();
		// logger.info("wbNum---" + wbNum);
		if (wbNum != null && wbNum.trim().length() > 0) {
			person.setWbNum(Integer.parseInt(wbNum));
		} else {
			person.setWbNum(0);
		}
		/**
		 * 在home_userInfo.php中，抽取基本资料div块
		 */
		beginRegex = "基本资料" + AnsjPaser.TEXTEGEXANDNRT
				+ "<div class=\"u_info_bd\">";
		endRegex = "</div>";
		blockAnsj = new AnsjPaser(beginRegex, endRegex, content,
				AnsjPaser.TEXTEGEXANDNRT);
		blockDiv = blockAnsj.getText();
		if (blockDiv == null || blockDiv.length() == 0) {
			throw new Exception();
		} else {
			// logger.info("取到的基本资料块的blockDiv--" + blockDiv);
		}

		// 生日提取，即birthday的抽取，包括星座
		beginRegex = "生日：</span><span" + AnsjPaser.TEXTEGEXANDNRT + ">";
		endRegex = "</span>";
		AnsjPaser birthAnsj = new AnsjPaser(beginRegex, endRegex, blockDiv,
				AnsjPaser.TEXTEGEXANDNRT).addFilterRegex("<.*?>");
		String birthday = birthAnsj.getText();
		if (birthday != null) {
			// logger.info("birthday---" + birthday.trim());
			person.setBrithday(birthday.trim());
		}
		// 工作提取，work
		beginRegex = "从事行业：</span><span" + AnsjPaser.TEXTEGEXANDNRT + ">";
		endRegex = "</span>";
		AnsjPaser workAnsj = new AnsjPaser(beginRegex, endRegex, blockDiv,
				AnsjPaser.TEXTEGEXANDNRT).addFilterRegex("<.*?>");
		String work = workAnsj.getText();
		if (work != null) {
			// logger.info("work---" + work.trim());
			person.setWork(work);
		}
		// 个人主页提取，blogURL
		beginRegex = "个人主页：</span><span" + AnsjPaser.TEXTEGEXANDNRT + ">";
		endRegex = "</span>";
		AnsjPaser blogAnsj = new AnsjPaser(beginRegex, endRegex, blockDiv,
				AnsjPaser.TEXTEGEXANDNRT).addFilterRegex("<.*?>");
		String blogURL = blogAnsj.getText();
		if (blogURL != null) {
			// logger.info("blogURL---" + blogURL.trim());
			person.setBlogUrl(blogURL);
		}
		// summary的抽取,个人介绍
		beginRegex = "个人介绍：</span><span" + AnsjPaser.TEXTEGEXANDNRT + ">";
		endRegex = "</span>";
		AnsjPaser summaryAnsj = new AnsjPaser(beginRegex, endRegex, blockDiv,
				AnsjPaser.TEXTEGEXANDNRT).addFilterRegex("<.*?>");
		String summary = summaryAnsj.getText();
		if (summary != null) {
			// logger.info("summary---" + summary.trim());
			person.setSummary(summary);
		}
		/**
		 * 在home_userInfo.php中，抽取教育信息div块
		 */
		beginRegex = "教育信息" + AnsjPaser.TEXTEGEXANDNRT
				+ "<div class=\"u_info_bd\">";
		endRegex = "</div>";
		blockAnsj = new AnsjPaser(beginRegex, endRegex, content,
				AnsjPaser.TEXTEGEXANDNRT);
		blockDiv = blockAnsj.getText();
		if (blockDiv == null || blockDiv.length() == 0) {
			// logger.info(edu);
			// logger.info("教育信息为空");
		} else {
			// logger.info("取到的教育信息块的blockDiv--" + blockDiv);
			// 教育信息的抽取,education
			// 因为教育信息可能有多条，故要循环显示
			// 首先取出每条li的信息
			beginRegex = "<li>";
			endRegex = "</li>";
			AnsjPaser eduList = new AnsjPaser(beginRegex, endRegex, blockDiv,
					AnsjPaser.TEXTEGEXANDNRT);
			// 匹配出每条教育信息
			String edu = null;
			String eduString = "";
			while (eduList.hasNext()) {
				edu = eduList.getNext();
				eduString += edu.replaceAll("<.*?>", "");
			}
			// logger.info(eduString);
			person.setEdu(eduString);
		}
		/**
		 * 在home_userInfo.php中，抽取个人标签div块
		 */
		beginRegex = "[他她]的标签" + AnsjPaser.TEXTEGEXANDNRT
				+ "<div class=\"u_info_bd\"";
		endRegex = "</div>";
		blockAnsj = new AnsjPaser(beginRegex, endRegex, content,
				AnsjPaser.TEXTEGEXANDNRT);
		blockDiv = blockAnsj.getText();
		if (blockDiv == null || blockDiv.length() == 0) {
			// logger.info(edu);
			// logger.info("他的标签为空");
		} else {
			// logger.info("取到的他的标签块的blockDiv--" + blockDiv);
			// 个人标签都在一个li中，
			beginRegex = "<li>";
			endRegex = "</li>";
			AnsjPaser tagList = new AnsjPaser(beginRegex, endRegex, blockDiv,
					AnsjPaser.TEXTEGEXANDNRT).addFilterRegex("<.*?>");
			// 匹配出每条教育信息
			// logger.info("他的标签为--" + tagList.getText());
			String tags = tagList.getText();
			if (tags != null) {
				person.setTags(tags);
			}
		}

		// System.out.println("infoBoockText------------" + infoBoockText);
		person.setUpdateTime(new Date());
		person.setUrl(urlByUid);

		person.setWeiboType("2");

		person.setGrab_method("qq");
		// 将long型数据加入至uid_long
		// person.setUid_long(Long.parseLong(person.getUid()));
		person.setUid_long(Long
				.parseLong(uidGeneratorUtil.md5(person.getUid())));
		if (this.urlPojo.getUrlSource().length() > 0) {
			personInfoManager.savePersonInfo(true, person);
		} else {
			personInfoManager.savePersonInfo(false, person);
		}
		logger.info("qq person info-----------" + person);
	}

	public void init(PersonInfo person, GrabPageSource4QQ grabPageSource4QQ,
			UrlPojo urlPojo) {
		this.person = person;
		this.grabPageSource4QQ = grabPageSource4QQ;
		this.urlPojo = urlPojo;
		this.uid = MyStringUtils.getIdByUrl(urlPojo.getUrl());
		this.urlByUid = FormatOutput.urlPrefix_QQ + this.uid;
		this.Req_URL = StaticValue4QQ.QQ_PersonInfo_URL_Prefix + this.uid;
	}

	public void init(GrabPageSource4QQ grabPageSource4QQ, UrlPojo urlPojo) {
		this.grabPageSource4QQ = grabPageSource4QQ;
		this.urlPojo = urlPojo;
		this.uid = MyStringUtils.getIdByUrl(urlPojo.getUrl());
		this.urlByUid = FormatOutput.urlPrefix_QQ + this.uid;
		this.Req_URL = StaticValue4QQ.QQ_PersonInfo_URL_Prefix + this.uid;
	}

	public String getUid() {
		return MyStringUtils.getIdByUrl(urlPojo.getUrl());
	}

	public PersonInfo getPersonHead() throws Exception {
		String content = grabPageSource4QQ.getPageSourceOfQQ(this.Req_URL,
				null, StaticValue4QQ.QQ_PersonInfo_Host);
		if (content == null) {
			logger.info(urlByUid + " OPEN ERROR !");
			throw new Exception();
		}
		
//		logger.info("content---"+content);

		String uid = this.getUid();
		if (uid == null || uid.length() < 1) {
			throw new Exception();
		}
		person.setUid(uid);

		String beginRegex = "<div class=\"m_profile";
		String endRegex = "<script";
		AnsjPaser blockAnsj = new AnsjPaser(beginRegex, endRegex, content,
				AnsjPaser.TEXTEGEXANDNRT);
		String blockDiv = blockAnsj.getText();
		if (blockDiv == null || blockDiv.length() == 0) {
			throw new Exception();
		} else {
			// logger.info("取到的首个blockDiv--" + blockDiv);
		}

		// 姓名抽取
		beginRegex = "<span class=\"text_user\">";
		endRegex = "</span>";
		AnsjPaser ansjName = new AnsjPaser(beginRegex, endRegex, blockDiv,
				AnsjPaser.TEXTEGEXANDNRT);
		String person_name = ansjName.getText();
		if (person_name == null || person_name.trim().length() == 0) {
			logger.info("没有提取出person_name，直接抛出异常!");
			throw new Exception();
		}
		person.setName(person_name);
		// System.out.println("抽取出来的name---------------" + person_name);

		return person;
	}

}
