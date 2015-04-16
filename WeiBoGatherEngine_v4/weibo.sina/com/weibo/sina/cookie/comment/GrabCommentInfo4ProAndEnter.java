package com.weibo.sina.cookie.comment;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mbm.elint.entity.Comments;
import com.mbm.elint.entity.mongoDB.CommentDoc;
import com.mbm.elint.entity.util.MyJson;
import com.mbm.elint.entity.util.UrlPojo;
import com.mbm.elint.manager.business.spider.UrlsManager;
import com.mbm.elint.manager.database.mongoDB.CommentDocInfoManager;
import com.mbm.util.DateUtil;
import com.weibo.common.utils.AnsjPaser;
import com.weibo.common.utils.FormatOutput;
import com.weibo.common.utils.StaticValue;
import com.weibo.sina.cookie.common.GrabPageResource;
import com.weibo.utils.sina.MyStringUtils;

@Component
@Scope(value = "prototype")
// 代表每次的autowire得到的对象都不一样
public class GrabCommentInfo4ProAndEnter {
	public static Logger logger = Logger
			.getLogger(GrabCommentInfo4ProAndEnter.class);
	@Autowired
	private CommentDocInfoManager commentDocInfoManager;
	private GrabPageResource grabPageSource = null;
	private UrlPojo urlPojo;
	private String sendUrl;

	@Autowired
	private MyJson myJSON = new MyJson();

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

	private UrlsManager urlsManager;

	public void init(GrabPageResource grabPageSource, UrlPojo urlPojo,
			UrlsManager urlsManager) {
		this.grabPageSource = grabPageSource;
		this.urlPojo = urlPojo;
		// 这里得到的是docURL
		this.sendUrl = MyStringUtils.getUrlRemoveSlash(urlPojo.getUrl());
		this.urlsManager = urlsManager;
	}

	int page = 1;
	String url1 = null;
	String url2 = null;
	String url3 = null;
	String content = null;
	@Autowired
	private DateUtil dateUtil = new DateUtil();

	// long time = Long.MAX_VALUE;
	String beginRegex = null;
	String endRegex = null;
	AnsjPaser ansjHtml = null;
	AnsjPaser ansjContent = null;
	AnsjPaser ansjPubTime = null;
	AnsjPaser ansjUid = null;
	AnsjPaser ansjUsername = null;
	AnsjPaser ansjMid = null;
	boolean flag = false;
	int repeat_count = 0;
	String c1 = null;
	// CommentDoc commentDoc = null;
	Comments comment = null;
	String sayUrl = null;
	String temp_time = null;
	String article = null;
	String commentUid = null;
	String commentUsername = null;
	String sinaDocParam = null;
	String mid = null;

	String firstId = null;// 记录当前评论页的第一个mid
	int temp_count = 0;

	public void grabArticleComments() throws Exception {
		page = 1;
		while (page <= 50) {// 这样的话，不管如何都会有个结束
			// logger.info("ProfessionAndEnter评论抓取---正在抓取" + this.sendUrl + "的第"
			// +
			// page
			// + "页");
			System.out.println("ProfessionAndEnter评论抓取---正在抓取" + this.sendUrl
					+ "的第" + page + "页");
			temp_count = 0;
			if (page == 1) {
				url1 = this.sendUrl;
				if (!grabOnePageArticle(url1, true)) {
					return;
				}
			} else {
				url1 = StaticValue.Comment_ProAndEnter_Wb_Content_Page_Next_URL
						.replace("${pageNum}", "" + page).replace("${firstId}",
								firstId);
				if (!grabOnePageArticle(url1, false)) {
					return;
				}
			}
			page++;
		}
		// logger.info("ProfessionAndEnter评论抓取---抓取" + this.sendUrl + " 完毕!");
		System.out.println("ProfessionAndEnter评论抓取---抓取" + this.sendUrl
				+ " 完毕!");
	}

	public boolean grabOnePageArticle(String url, boolean isFirstPage)
			throws Exception {
		content = grabPageSource.getPageSourceOfSina(url, null);

		if (StringUtils.isBlank(content)) {
			return false;
		}
		// logger.info("content的源码是----" + content);
		if (isFirstPage) {
			// 抽取url源码中的数据列表json串
			beginRegex = "pl_content_weiboDetail\",";
			endRegex = "}\\)";
			ansjContent = new AnsjPaser(beginRegex, endRegex, content,
					AnsjPaser.TEXTEGEXANDNRT);
			content = ansjContent.getText();
			myJSON.createJson("{" + content + "}");
			content = myJSON.getStringByKey("html");
			// logger.info("isFirstPage content的源码是----" + content);
			// 取得首页的comments_list
			beginRegex = "node-type=\"feed_list\"";// 专业版
			endRegex = "</div>\\s{1,10}<!--评论列表";
			ansjContent = new AnsjPaser(beginRegex, endRegex, content,
					AnsjPaser.TEXTEGEXANDNRT);
			content = ansjContent.getText();
		} else {
			// logger.info("非第1页content---" + content);
			myJSON.createJson(content);
			myJSON.createJson(myJSON.getStringByKey("data"));
			content = myJSON.getStringByKey("html");
		}
		// logger.info("contentDoc的源码是----" + content);
		if (!content.contains("<dl")) {
			logger.info("ProfessionAndEnter评论抓取---该URL的微博内容已经抓取完成!");
			return false;
		} else if (!saveOnePageContent(content)) {
			return false;
		}
		// if (true) {
		// return false;
		// }
		return true;
	}

	private String temp_anything = null;

	public boolean saveOnePageContent(String content) throws IOException,
			ParseException, Exception {
		if (StringUtils.isBlank(content)) {
			return false;
		}
		// 模块抽取
		beginRegex = "<dl";
		endRegex = "</dl";
		ansjHtml = new AnsjPaser(beginRegex, endRegex, content,
				AnsjPaser.TEXTEGEXANDNRT);
		// 正文抽取
		// 特别注意:此处最后的:是中文状态的"："
		beginRegex = "<dd>" + AnsjPaser.TEXTEGEXANDNRT + "</a>：";
		endRegex = "<span class=\"W_textb\">";
		ansjContent = new AnsjPaser(beginRegex, endRegex,
				AnsjPaser.TEXTEGEXANDNRT);// 全文抓取保存，不再过滤html标签
		// 时间抽取
		beginRegex = "<span class=\"W_textb\">";
		endRegex = "</span>";
		ansjPubTime = new AnsjPaser(beginRegex, endRegex);

		// 评论人的uid抽取
		beginRegex = "usercard=\"id=";
		endRegex = "\"";
		ansjUid = new AnsjPaser(beginRegex, endRegex);

		// 评论人的username抽取
		beginRegex = "<dd>" + AnsjPaser.TEXTEGEXANDNRT + ">";
		endRegex = "</a>";
		ansjUsername = new AnsjPaser(beginRegex, endRegex);

		// mid
		beginRegex = "mid=\\\"";
		endRegex = "\\\"";
		ansjMid = new AnsjPaser(beginRegex, endRegex);
		flag = false;
		// 开始抽取
		repeat_count = 0;// 记录当前页的内容当中，有几条是重复的,当达指定的每页最大重复数时，即为终止

		while (ansjHtml.hasNext()) {
			c1 = ansjHtml.getNext();
			// logger.info("c1---"+c1);
			/**
			 * 具体取到每条评论的内容
			 */
			article = ansjContent.reset(c1).getText();
			if (article == null || article.trim().length() == 0) {
				logger.info("该条评论的内容为空直接返回false,不做保存!");
				return false;
			}
			// commentDoc = new CommentDoc();
			comment = new Comments();
			// logger.info("comment content---" + article);
			comment.setContent(article);

			comment.setDocurl(this.sendUrl);
			// logger.info("comment docUrl---" + this.sendUrl);

			temp_time = ansjPubTime.reset(c1).getText();
			// logger.info("comment publicTime---" + temp_time);
			// 暂时用new Date来代替
			// commentDoc.setPublishtime(new Date(Long.parseLong(sg)));
			comment.setPublishtime(dateUtil
					.getDateByNoneStruture4Sina(temp_time));

			comment.setGrabtime(new Date());

			// 评论者的uid的提取
			commentUid = ansjUid.reset(c1).getText();
			comment.setCommentUid(commentUid);
			// logger.info("comment uid---" + commentUid);

			if (StaticValue.comment_uid_add_account_tovisit) {
				UrlPojo urlPojo = new UrlPojo(FormatOutput.urlPrefix
						+ comment.getCommentUid(), 1);
				this.urlsManager.addUrl(urlPojo);
			}
			if (StaticValue.comment_uid_add_doc_tovisit) {
				UrlPojo urlPojo = new UrlPojo(FormatOutput.urlPrefix
						+ comment.getCommentUid(), 2);
				this.urlsManager.addUrl(urlPojo);
			}

			// 评论者的username的提取
			commentUsername = ansjUsername.reset(c1).getText();
			comment.setCommentUsername(commentUsername);
			// logger.info("comment username---" + commentUsername);

			mid = ansjMid.reset(c1).getText();
			// logger.info("comment mid---" + mid);
			if (temp_count++ == 0) {
				this.firstId = mid;
			}
			if (mid.length() > 0) {
				comment.setMid(mid);
			} else {
				comment.setMid("0");
			}
			// time = doc.getPublishtime().getTime();
			flag = true;
			if (!commentDocInfoManager.saveOrUpdate(comment)) {//
				// 此时的没有保存成功意为数据重复了
				if ((++repeat_count) >= StaticValue.max_content_page_repeat_items_comments) {
					// 此时即定为重复页，不再抓取
					// logger.info("该docUrl的评论列表重复已达到最大，评论列表抓取已结束!");
					System.out.println("该docUrl的评论列表重复已达到最大，评论列表抓取已结束!");
					return false;
				}
			}
			// logger.info("ProfessionAndEnter评论列表commentDoc--" + commentDoc);
			System.out.println("ProfessionAndEnter评论列表commentDoc--"
					+ comment);
		}
		repeat_count = 0;
		return true;
	}

	public static void main(String[] args) {
		// System.out.println(StringUtils.isBlank());
	}
}
