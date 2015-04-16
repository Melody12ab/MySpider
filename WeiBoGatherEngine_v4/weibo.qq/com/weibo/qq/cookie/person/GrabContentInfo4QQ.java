package com.weibo.qq.cookie.person;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mbm.elint.entity.DocInfo;
import com.mbm.elint.entity.PersonInfo;
import com.mbm.elint.entity.util.MyJson;
import com.mbm.elint.entity.util.MyJsonArray;
import com.mbm.elint.entity.util.UrlPojo;
import com.mbm.elint.manager.database.DocInfoManager;
import com.mbm.util.DateUtil;
import com.weibo.common.utils.AnsjPaser;
import com.weibo.common.utils.StaticValue;
import com.weibo.qq.cookie.common.GrabPageSource4QQ;
import com.weibo.utils.qq.StaticValue4QQ;
import com.weibo.utils.qq.UidGeneratorUtil;
import com.weibo.utils.sina.MyStringUtils;

@Component
@Scope(value = "prototype")
// 代表每次的autowire得到的对象都不一样
public class GrabContentInfo4QQ {
	public static Logger logger = Logger.getLogger(GrabContentInfo4QQ.class);
	@Autowired
	private DocInfoManager docInfoManager;
	private GrabPageSource4QQ grabPageResource4QQ = null;
	private PersonInfo p = null;
	private UrlPojo urlPojo;
	private String sendUrl;
	private boolean hasNextPage = true;
	private String nextPageURL = null;
	private boolean isScrollPages = false;
	private String aid = null;
	private String last_item_ref = null;
	@Autowired
	private DateUtil dateUtil = new DateUtil();
	@Autowired
	private UidGeneratorUtil uidGeneratorUtil = new UidGeneratorUtil();
	@Autowired
	private MyJson myJSON = new MyJson();
	@Autowired
	private MyJsonArray myJsonArray = new MyJsonArray();

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

	public void init(PersonInfo p, GrabPageSource4QQ grabPageResource4QQ,
			UrlPojo urlPojo) {
		this.p = p;
		this.grabPageResource4QQ = grabPageResource4QQ;
		this.urlPojo = urlPojo;
		this.sendUrl = MyStringUtils.getUrlRemoveSlash(urlPojo.getUrl());
		this.uid = MyStringUtils.getIdByUrl(this.sendUrl);
	}

	private int currentPage = 1;

	public void grabArticleContents() throws Exception {
		// String sina_id = p.getUid();
		while (hasNextPage) {
			logger.info("腾讯微博--" + this.sendUrl + " 正在抓取第" + currentPage + "页");
			if (currentPage == 1) {
				if (!grabOnePageArticle(this.sendUrl, true)) {
					return;
				}
			} else {
				if (!grabOnePageArticle(this.nextPageURL, false))
					return;
			}
			currentPage++;
			Thread.sleep(1000);
		}
		currentPage = 1;
		logger.info("腾讯微博--" + this.sendUrl + " 抓取结束!");
	}

	String content = null;

	public boolean grabOnePageArticle(String url, boolean isFirst)
			throws Exception {
		if (isScrollPages) {
			content = grabPageResource4QQ.getPageSourceOfQQ(url,
					StaticValue4QQ.QQ_Ajax_Next_Page_Referer,
					StaticValue4QQ.QQ_Ajax_Next_Page_Host);
		} else {
			content = grabPageResource4QQ.getPageSourceOfQQ(url,
					StaticValue4QQ.QQ_ContentInfo_Referer,
					StaticValue4QQ.QQ_ContentInfo_Host);
		}

		logger.info("网页源码为---" + content);

		if (content == null) {
			return false;
		}

		if (isFirst) {
			init(content);
		}
		if (!saveOnePageContent(content)) {
			return false;
		}
		return true;
	}

	long time = Long.MAX_VALUE;
	String main_content = null;

	/**
	 * 在这里将personInfo中的uid(erliang200088),(天亮)的意思
	 */
	private String uid;
	private String name;

	public void init(String content) throws Exception {
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
		// 提取uid，其值类似于erliang20088,不可为
		// beginRegex = "class=\"c_tx5\">\\(@";
		// endRegex = "\\)</span";
		// AnsjPaser uidAnsj = new AnsjPaser(beginRegex, endRegex, blockDiv,
		// AnsjPaser.TEXTEGEXANDNRT);
		// this.uid = uidAnsj.getText();

		if (uid == null || uid.length() == 0) {
			throw new Exception();
		}

		// 姓名抽取
		beginRegex = "<span class=\"text_user\">";
		endRegex = "</span>";
		AnsjPaser ansjName = new AnsjPaser(beginRegex, endRegex, blockDiv,
				AnsjPaser.TEXTEGEXANDNRT);
		name = ansjName.getText();
		if (name == null || name.trim().length() == 0) {
			logger.info("没有提取出person_name，直接抛出异常!");
			throw new Exception();
		}
	}

	public boolean saveOnePageContent(String content) throws IOException,
			ParseException, Exception {
		if (StringUtils.isBlank(content)) {
			logger.info("内容为空，即将退出抓取!");
			return false;
		}
		String beginRegex = null;
		String endRegex = null;
		int count = 1;
		int repeat_count = 0;// 记录当前页的内容当中，有几条是重复的,当达指定的每页最大重复数时，即为终止
		// 对格式的判断,此为对滚动页式下，返回的数据为json格式时的解析保存
		if (isScrollPages) {
			myJSON.createJson(content);
			myJSON.createJson(myJSON.getStringByKey("info"));
			if (myJSON.getIntegerByKey("hasNext") == 1) {
				hasNextPage = true;
			}
			content = myJSON.getStringByKey("talk");
			// myJSON.createJson(content.substring(1, content.length() - 1));
			myJsonArray.createJsonArray(content);

			JSONArray jsonArray = myJsonArray.getJsonArray();
			Iterator<JSONObject> iter = jsonArray.iterator();
			JSONObject jsonObj = null;
			// 小为化集中声明
			String article = null;
			Date date = null;
			String docUrl = null;
			String countsArray = null;
			String nums[] = null;
			String from = null;
			Object idObj = null;
			while (iter.hasNext()) {
				DocInfo doc = new DocInfo();
				jsonObj = iter.next();
				// logger.info("json---" + jsonObj);
				// logger.info("json id---" + jsonObj.get("id"));
				idObj = jsonObj.get("id");
				if (idObj == null) {
					logger.info("该json item不是一条真正的微博内容，将跳过!");
					continue;
				} else {
					aid = idObj.toString();
				}
				doc.setDocArticleId(aid);
				article = jsonObj.get("content").toString();
				// logger.info("json content---" + article);
				doc.setArticle(article);
				last_item_ref = jsonObj.get("realtime").toString();
				date = new Date(Long.parseLong(last_item_ref + "000"));
				// logger.info("json date---" + date);
				doc.setPublishtime(date);
				from = jsonObj.get("from").toString();
				// 提取微博来源origin
				beginRegex = "title=\"来自";
				endRegex = "\"";
				AnsjPaser originAnsj = new AnsjPaser(beginRegex, endRegex,
						from, AnsjPaser.TEXTEGEXANDNRT);
				from = originAnsj.getText();
				// logger.info("from---" + from);
				doc.setOrigin(from);
				docUrl = StaticValue4QQ.QQ_DocUrl_prefix + aid;// 它的值等于
				// logger.info("docUrl---" + docUrl);
				doc.setDocUrl(docUrl);
				countsArray = jsonObj.get("counts").toString();
				countsArray = countsArray
						.substring(1, countsArray.length() - 1);
				nums = countsArray.split(",");

				try {
					doc.setTransmit(Integer.parseInt(nums[0]));
				} catch (Exception e) {
					doc.setTransmit(0);
				}

				try {
					doc.setDiscuss(Integer.parseInt(nums[1]));
				} catch (Exception e) {
					doc.setDiscuss(0);
				}
				// docUrl,publisTime,id,origin,评论数，转发数，
				doc.setAuthor(this.name);
				doc.setWeiboType(2);
				/**
				 * 设置原始的url
				 */
				doc.setSendUrl(this.sendUrl);
				doc.setGrab_method("qq");
				doc.setPersonId(this.uid);
				// 设置新加的person_id_long
				doc.setPerson_id_long(Long.parseLong(uidGeneratorUtil
						.md5(this.uid)));
				if (!docInfoManager.saveOrUpdate(doc)) {// 此时的没有保存成功意为数据重复了
					if ((++repeat_count) >= StaticValue.max_content_page_repeat_items) {
						// 此时即定为重复页，不再抓取
						logger.info("重复已达到最大，该url的内容抓取结束!");
						return false;
					}
				}
				logger.info("第-" + (count++) + "-条docInfo--" + doc);
			}
			if (count == 1) {
				logger.info("页内记数器为0，认为抓取到尾页了，该Url抓取结束!");
				return false;
			}
		} else {
			// 首先抽取微博内容页的class="main"的div块
			beginRegex = "id=\"mainWrapper\"";
			endRegex = "<div class=\"side\">";
			AnsjPaser mainAnsj = new AnsjPaser(beginRegex, endRegex, content,
					AnsjPaser.TEXTEGEXANDNRT);
			content = mainAnsj.getText();
			// logger.info("content--" + content);

			// 抽取内容块
			beginRegex = "class=\"LC  noHead\">";
			endRegex = "</ul>";
			AnsjPaser contentAnsj = new AnsjPaser(beginRegex, endRegex,
					content, AnsjPaser.TEXTEGEXANDNRT);
			main_content = contentAnsj.getText();
			// logger.info("main_content--" + main_content);

			// 抽取每一条微言的li块
			beginRegex = "<li id";
			endRegex = "</li>";
			AnsjPaser ulListAnsj = new AnsjPaser(beginRegex, endRegex,
					main_content, AnsjPaser.TEXTEGEXANDNRT);
			/**
			 * 以下是递归提取各条信息的详细参数，匹配
			 */
			// 提取article
			beginRegex = "div class=\"msgCnt\">";
			endRegex = "</div>";
			AnsjPaser articleAnsj = new AnsjPaser(beginRegex, endRegex);

			// 提取微博内容的docURL
			beginRegex = "<a class=\"time\"" + AnsjPaser.TEXTEGEXANDNRT
					+ "href=\"";
			endRegex = "\"";
			AnsjPaser docUrlAnsj = new AnsjPaser(beginRegex, endRegex);

			// 提取微博发布时间publisTime
			beginRegex = "rel=\"";
			endRegex = "\" from=";
			AnsjPaser publicTimeAnsj = new AnsjPaser(beginRegex, endRegex);

			// 提取微博来源origin
			beginRegex = "<span class=\"left\">" + AnsjPaser.TEXTEGEXANDNRT
					+ "title=\"来自";
			endRegex = "\"";
			AnsjPaser originAnsj = new AnsjPaser(beginRegex, endRegex);

			// 提取微博内容的id
			beginRegex = "id=\"";
			endRegex = "\"";
			AnsjPaser aidAnsj = new AnsjPaser(beginRegex, endRegex);

			// 提取微博内容的转发的数据
			beginRegex = "class=\"relay\"" + AnsjPaser.TEXTEGEXANDNRT
					+ "num=\"";
			endRegex = "\"";
			AnsjPaser transmitNumAnsj = new AnsjPaser(beginRegex, endRegex);

			// 提取微博内容的评论的数据
			beginRegex = "class=\"comt\"" + AnsjPaser.TEXTEGEXANDNRT + "num=\"";
			endRegex = "\"";
			AnsjPaser commentNumAnsj = new AnsjPaser(beginRegex, endRegex);

			String temp_item = null;
			while (ulListAnsj.hasNext()) {
				DocInfo doc = new DocInfo();
				temp_item = ulListAnsj.getNext();
				String article = articleAnsj.reset(temp_item).getText();
				if (article != null && article.trim().length() == 0) {
					doc.setArticle("转发微博");
				} else {
					doc.setArticle(article);
				}
				String docURL = docUrlAnsj.reset(temp_item).getText();
				doc.setDocUrl(docURL);
				last_item_ref = publicTimeAnsj.reset(temp_item).getText();
				Date date = null;
				try {
					date = new Date(Long.parseLong(last_item_ref + "000"));
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("某一条微博出现publishTime的错误，将跳过!");
				}
				doc.setPublishtime(date);
				String origin = originAnsj.reset(temp_item).getText();
				doc.setOrigin(origin);
				aid = aidAnsj.reset(temp_item).getText();
				doc.setDocArticleId(aid);
				String transmitNum = transmitNumAnsj.reset(temp_item).getText();
				if (transmitNum != null && transmitNum.trim().length() > 0) {
					doc.setTransmit(Integer.parseInt(transmitNum));
				} else {
					doc.setTransmit(0);
				}
				String commentNum = commentNumAnsj.reset(temp_item).getText();
				if (commentNum != null && commentNum.trim().length() > 0) {
					doc.setDiscuss(Integer.parseInt(commentNum));
				} else {
					doc.setTransmit(0);
				}
				doc.setAuthor(this.name);
				doc.setWeiboType(2);
				/**
				 * 设置原始的url
				 */
				doc.setSendUrl(this.sendUrl);
				doc.setGrab_method("qq");
				doc.setPersonId(this.uid);
				// 设置新加的person_id_long
				doc.setPerson_id_long(Long.parseLong(uidGeneratorUtil
						.md5(this.uid)));
				if (!docInfoManager.saveOrUpdate(doc)) {// 此时的没有保存成功意为数据重复了
					if ((++repeat_count) >= StaticValue.max_content_page_repeat_items) {
						// 此时即定为重复页，不再抓取
						logger.info("重复已达到最大，该url的内容抓取结束!");
						return false;
					}
				}
				logger.info("第-" + (count++) + "-条docInfo--" + doc);
			}
		}
		// 翻页操作
		getNextPage(content);
		repeat_count = 0;
		return true;
	}

	public void getNextPage(String content) {
		// 首先抽取微博内容页的翻页div块
		String beginRegex = "class=\"blueFoot\"";
		String endRegex = "</div>";
		// logger.info("content---"+content);
		AnsjPaser pagesAnsj = new AnsjPaser(beginRegex, endRegex, content,
				AnsjPaser.TEXTEGEXANDNRT);
		String pagesContent = pagesAnsj.getText();
		// logger.info("pagesContent--" + pagesContent);
		if (pagesContent == null) {
			// 说明非手动翻页方式，而是滚动翻页
			// beginRegex = "class=\"moreFoot\"";
			// endRegex = "</div>";
			// logger.info("content---" + content);
			// pagesAnsj = new AnsjPaser(beginRegex, endRegex, content,
			// AnsjPaser.TEXTEGEXANDNRT);
			// pagesContent = pagesAnsj.getText();
			// logger.info("滚动版的pagesContent---" + pagesContent);
			// if (pagesContent.contains("多")) {
			hasNextPage = true;
			nextPageURL = StaticValue4QQ.QQ_Scroll_URL.replace("${time}",
					last_item_ref).replace("${currentPage}",
					(this.currentPage + 1) + "").replace("${aid}", this.aid)
					.replace("${uid}", this.uid);
			// logger.info("nextPageURL---" + nextPageURL);
			// logger.info("nextPageURL=--" + nextPageURL);
			isScrollPages = true;// 还原翻页设置
			// } else {
			// hasNextPage = false;
			// nextPageURL = null;
			// isScrollPages = false;// 还原翻页设置
			// }
			return;
		}
		// 抽取看是否有”下一页按钮“，没有的话，直接跳过,有的取出来href标签
		beginRegex = "<a";
		endRegex = "</a>";
		AnsjPaser nextPageAnsj = new AnsjPaser(beginRegex, endRegex,
				pagesContent, AnsjPaser.TEXTEGEXANDNRT);
		String nextPageHref = nextPageAnsj.getLastText();
		// logger.info("nextPageHref content--" + nextPageHref);
		if (nextPageHref.contains("下一页")) {
			hasNextPage = true;
			beginRegex = "href=\"";
			endRegex = "\"";
			nextPageAnsj = new AnsjPaser(beginRegex, endRegex, nextPageHref,
					AnsjPaser.TEXTEGEXANDNRT);
			nextPageHref = nextPageAnsj.getLastText();
			nextPageURL = this.sendUrl + nextPageHref;
			isScrollPages = false;// 还原翻页设置
		} else {
			hasNextPage = false;
			nextPageURL = null;
			isScrollPages = false;// 还原翻页设置
		}
	}

	public String getJsonByAjax(String targetUrl, String Referer, String Host) {
		return grabPageResource4QQ.getPageSourceOfQQ(targetUrl, Referer, Host);
	}

}
