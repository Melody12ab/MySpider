package com.weibo.sina.cookie.enterprise;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mbm.elint.entity.DocInfo;
import com.mbm.elint.entity.PersonInfo;
import com.mbm.elint.entity.util.MyJson;
import com.mbm.elint.entity.util.UrlPojo;
import com.mbm.elint.manager.business.spider.UrlsManager;
import com.mbm.elint.manager.database.DocInfoManager;
import com.weibo.common.utils.AnsjPaser;
import com.weibo.common.utils.FormatOutput;
import com.weibo.common.utils.StaticValue;
import com.weibo.sina.cookie.common.GrabPageResource;
import com.weibo.utils.sina.ExtractUtil;
import com.weibo.utils.sina.MyStringUtils;

@Component
@Scope(value = "prototype")
// 代表每次的autowire得到的对象都不一样
public class GrabEnterContentInfoV2 {
	public static Logger logger = Logger
			.getLogger(GrabEnterContentInfoV2.class);
	@Autowired
	private DocInfoManager docInfoManager;
	private GrabPageResource cookie = null;
	private PersonInfo p = null;
	private UrlPojo urlPojo;
	private String sendUrl;

	/**
	 * 截取at的工具类
	 */
	ExtractUtil extractUtil = new ExtractUtil();

	private UrlsManager urlManager;

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

	public void init(PersonInfo p, GrabPageResource cookie, UrlPojo urlPojo,
			UrlsManager urlManager) {
		this.p = p;
		this.cookie = cookie;
		this.urlPojo = urlPojo;
		this.sendUrl = MyStringUtils.getUrlRemoveSlash(urlPojo.getUrl());
		// 为转发处理而添加
		this.urlManager = urlManager;
	}

	public void init(PersonInfo p, GrabPageResource cookie, UrlPojo urlPojo) {
		this.p = p;
		this.cookie = cookie;
		this.urlPojo = urlPojo;
		this.sendUrl = MyStringUtils.getUrlRemoveSlash(urlPojo.getUrl());
	}

	int page = 1;
	String sina_id = null;
	String url1 = null;
	String url2 = null;
	String url3 = null;
	String content = null;
	// long time = Long.MAX_VALUE;
	String beginRegex = null;
	String endRegex = null;
	AnsjPaser ansjHtml = null;
	AnsjPaser ansjContent = null;
	AnsjPaser ansjPubTime = null;
	AnsjPaser ansjSayUrl = null;
	AnsjPaser ansjFrom = null;
	AnsjPaser ansjRepeat = null;
	AnsjPaser ansjComment = null;
	// AnsjPaser ansjSDU = null;
	AnsjPaser ansjMid = null;
	boolean flag = false;
	int repeat_count = 0;
	String c1 = null;
	DocInfo doc = null;
	String sayUrl = null;
	String sg = null;
	String article = null;
	String s = null;
	String sinaDocParam = null;
	String mid = null;

	// 针对转发所添加的正则解析变量
	AnsjPaser ansjForward_uid = null;
	AnsjPaser ansjForward_docUrl = null;

	public void grabArticleContents4Origin() throws Exception {
		page = 1;
		sina_id = p.getUid();
		while (page <= 50) {
			// logger.info("个人版---正在抓取" + this.sendUrl + "的第" + page + "页");
			System.out.println("个人版---正在抓取" + this.sendUrl + "的第" + page + "页");
			if (page == 1) {
				String url1 = "http://weibo.com/aj/mblog/mbloglist?_wv=5&is_ori=1&page="
						+ page + "&uid=" + sina_id + "&_t=0";
				if (!grabOnePageArticle(url1)) {
					return;
				}
			} else {
				url1 = "http://weibo.com/aj/mblog/mbloglist?_wv=5&is_ori=1&page="
						+ page
						+ "&pre_page="
						+ (page - 1)
						+ "&uid="
						+ sina_id
						+ "&_t=0";
				if (!grabOnePageArticle(url1)) {
					return;
				}
			}
			url2 = "http://weibo.com/aj/mblog/mbloglist?_wv=5&is_ori=1&page="
					+ page + "&uid=" + sina_id + "&pre_page=" + page
					+ "&_t=0&pagebar=0";
			if (!grabOnePageArticle(url2)) {
				return;
			}
			url3 = "http://weibo.com/aj/mblog/mbloglist?_wv=5&is_ori=1&page="
					+ page + "&uid=" + sina_id + "&pre_page=" + page
					+ "&_t=0&pagebar=1";
			if (!grabOnePageArticle(url3)) {
				return;
			}
			page++;
		}
		// logger.info("个人版---抓取" + this.sendUrl + " 完毕!");
		System.out.println("个人版---抓取" + this.sendUrl + " 完毕!");
	}

	public void grabArticleContents4All() throws Exception {
		page = 1;
		sina_id = p.getUid();
		while (page <= 50) {// 这样的话，不管如何都会有个结束
			logger.info("个人版---正在抓取" + this.sendUrl + "的第" + page + "页");
			if (page == 1) {
				url1 = "http://weibo.com/aj/mblog/mbloglist?_wv=5&page=" + page
						+ "&uid=" + sina_id + "&_t=0";
				if (!grabOnePageArticle(url1)) {
					return;
				}
			} else {
				url1 = "http://weibo.com/aj/mblog/mbloglist?_wv=5&page=" + page
						+ "&pre_page=" + (page - 1) + "&uid=" + sina_id
						+ "&_t=0";
				if (!grabOnePageArticle(url1)) {
					return;
				}
			}
			url2 = "http://weibo.com/aj/mblog/mbloglist?_wv=5&page=" + page
					+ "&uid=" + sina_id + "&pre_page=" + page
					+ "&_t=0&pagebar=0";
			if (!grabOnePageArticle(url2)) {
				return;
			}
			url3 = "http://weibo.com/aj/mblog/mbloglist?_wv=5&page=" + page
					+ "&uid=" + sina_id + "&pre_page=" + page
					+ "&_t=0&pagebar=1";
			if (!grabOnePageArticle(url3)) {
				return;
			}
			page++;
			// 每处理一页，均要休眠这个时间
			Thread.sleep(StaticValue.cookie_sleep_interval_time);
		}
		logger.info("个人版---抓取" + this.sendUrl + " 完毕!");
	}

	public boolean grabOnePageArticle(String url) throws Exception {
		content = cookie.getPageSourceOfSina(url, null);
		// logger.info("得到的content源码是-----" + content);
		// if(true){
		// return false;
		// }
		if (content == null) {
			return false;
		}
		myJSON.createJson(content);
		content = myJSON.getStringByKey("data");
		if (StaticValue.log_output_enable) {
			logger.info(content);
		}
		if (!content.contains("feed_list_item")) {
			// logger.info("个人版---该URL的微博内容已经抓取完成!");
			// System.out.println("个人版---该URL的微博内容已经抓取完成!");
			return false;
		} else if (!saveOnePageContent(content)) {
			return false;
		}
		Thread.sleep(StaticValue.cookie_sleep_interval_time);
		return true;
	}

	public boolean saveOnePageContent(String content) throws IOException,
			ParseException, Exception {
		if (StringUtils.isBlank(content)) {
			return false;
		}
		// logger.info("内容content---" + content);
		// 模块抽取
		beginRegex = "<div action-type=\"feed_list_item";
		endRegex = "<div node-type=\"feed_list_repeat\"";
		ansjHtml = new AnsjPaser(beginRegex, endRegex, content,
				AnsjPaser.TEXTEGEXANDNRT);

		// 正文抽取
		beginRegex = "<div class=\"WB_text\" node-type=\"feed_list_content\"[\\s\\S]*?>";
		endRegex = "</div>";
		// AnsjPaser ansjContent = new AnsjPaser(beginRegex, endRegex,
		// AnsjPaser.TEXTEGEXANDNRT).addFilterRegex("<.*?>");
		ansjContent = new AnsjPaser(beginRegex, endRegex,
				AnsjPaser.TEXTEGEXANDNRT);// 全文抓取保存，不再过滤html标签
		// 时间抽取
		beginRegex = "node-type=\"feed_list_item_date\" date=\"";
		endRegex = "\"";
		ansjPubTime = new AnsjPaser(beginRegex, endRegex);
		/**
		 *每条微言的url地址抽取
		 */
		beginRegex = "S_link2 WB_time[\\s\\S]*?href=\"";
		endRegex = "\">";
		ansjSayUrl = new AnsjPaser(beginRegex, endRegex);

		// 来源抽取
		beginRegex = "S_link2 WB_time[\\s\\S]*?来自</em><a.*?>";
		endRegex = "</a>";
		ansjFrom = new AnsjPaser(beginRegex, endRegex);
		// 转发抽取
		beginRegex = "<a.*?>转发\\(";
		endRegex = "\\)</a>";

		ansjRepeat = new AnsjPaser(beginRegex, endRegex);
		// 评论抽取
		beginRegex = "<a.*?>评论\\(";
		endRegex = "\\)</a>";
		ansjComment = new AnsjPaser(beginRegex, endRegex);
		// 文章访问参数
		// 暂不获取
		// beginRegex =
		// "node-type=\\\"feed_list_item_date\\\" date=\\\".*?href=\\\"/\\d*/";
		// endRegex = "\\\"";
		// ansjSDU = new AnsjPaser(beginRegex,
		// endRegex).addFilterRegex("<.*?>");
		// mid
		beginRegex = "mid=\\\"";
		endRegex = "\\\"";
		ansjMid = new AnsjPaser(beginRegex, endRegex).addFilterRegex("<.*?>");
		flag = false;
		// 开始抽取
		repeat_count = 0;// 记录当前页的内容当中，有几条是重复的,当达指定的每页最大重复数时，即为终止

		/**
		 * 为转发关系添加的提取
		 */
		// 转发博主的uid抽取
		beginRegex = "usercard=\"id=";
		endRegex = "\"";
		ansjForward_uid = new AnsjPaser(beginRegex, endRegex);

		beginRegex = "S_func2 WB_time[\\s\\S]*?href=\"";
		endRegex = "\"";
		ansjForward_docUrl = new AnsjPaser(beginRegex, endRegex);
		// logger.info("个人版---抓取" + this.sendUrl + " 完毕!");
		String forward_uid = null;
		String forward_docUrl = null;

		while (ansjHtml.hasNext()) {
			c1 = ansjHtml.getNext();
			/**
			 * 具体取到每条微言的url
			 */
			sayUrl = ansjSayUrl.reset(c1).getText();
			// logger.info("sayUrl---" + sayUrl);
			if (sayUrl == null || sayUrl.trim().length() == 0) {
				logger.info("微言的url为空直接返回false,不做保存!");
				return false;
			}

			doc = new DocInfo();

			/**
			 * 此时,首先判断是不是转发，如果是，先将将转发的博主的 uid加到待抓取帐号列表中,然后将该转发内容抓取下来，存入数据库
			 */
			if (c1.contains("isForward=\"1\"")) {
				forward_uid = ansjForward_uid.reset(c1).getText();
				forward_docUrl = ansjForward_docUrl.reset(c1).getText();

				// 没有通过正则来抓取这条微博，而是直接将这个微博博主的URL加到抓取内容的列表当中
				// 简单了点，但实时性会稍微慢点
				/**
				 * 暂注掉转发的抓去---2013-09-22
				 */
				if (StaticValue.forward_account_uid_add_tovisit) {
					this.urlManager.addToVisitByUidFromTransmit(forward_uid);
				}

				doc.setIsForward(1);// 代表有转发
				doc.setForwardDocUrl(FormatOutput.urlPrefix_no_slash
						+ forward_docUrl);
			} else {
				doc.setIsForward(0);// 代表的是原创，没有转发
			}
			
			//加入评论部分
			if(StaticValue.doc_url_add_comment_tovisit){
				UrlPojo urlPojo=new UrlPojo(doc.getDocUrl(),102);
			    this.urlManager.addCommentUrlToFirst(urlPojo);
			}

			// 关于转发的微博的处理结束

			doc.setDocUrl(FormatOutput.urlPrefix_no_slash + sayUrl);

			sg = ansjPubTime.reset(c1).getText();
			// System.out.println(new Date(Long.parseLong(sg))+"------------");
			doc.setPublishtime(new Date(Long.parseLong(sg)));
			doc.setOrigin(ansjFrom.reset(c1).getText());
			article = ansjContent.reset(c1).getText();
			doc.setArticle(article);
			doc.setInsertTime(new Date());
			s = ansjRepeat.reset(c1).getText();
			if (StringUtils.isNotBlank(s)) {
				s = s.trim();
				doc.setTransmit(Integer.parseInt(s));
			} else {
				doc.setTransmit(0);
			}
			doc.setPersonId(p.getUid());
			s = ansjComment.reset(c1).getText();

			if (StringUtils.isNotBlank(s)) {
				s = s.trim();
				doc.setDiscuss(Integer.parseInt(s));
			} else {
				doc.setDiscuss(0);
			}
			// 暂不获取
			// sinaDocParam = ansjSDU.reset(c1).getText();
			// doc.setSinaDocParam(sinaDocParam);
			mid = ansjMid.reset(c1).getText();
			if (mid.length() > 0) {
				doc.setDocArticleId(mid);
			} else {
				doc.setDocArticleId("0");
			}
			// time = doc.getPublishtime().getTime();
			flag = true;
			doc.setAuthor(p.getName());
			doc.setWeiboType(1);
			/**
			 * 设置原始的url
			 */
			doc.setSendUrl(this.sendUrl);
			doc.setGrab_method("cookies");

			// 设置新加的person_id_long
			doc.setPerson_id_long(Long.parseLong(doc.getPersonId()));

			// 将at字段加入doc对象
			doc.setExtAt(extractUtil.getAtString(doc.getArticle()));

			if (!docInfoManager.saveOrUpdate(doc)) {// 此时的没有保存成功意为数据重复了
				if ((++repeat_count) >= StaticValue.max_content_page_repeat_items) {
					// 此时即定为重复页，不再抓取
					System.out.println("重复已达到最大，该url的内容抓取结束!");
					return false;
				}
			}
			// logger.info("person doc save successful");
			System.out.println("person doc save successful---" + doc);
			// System.out.println("person doc save successful");
		}
		repeat_count = 0;
		return true;
	}

	public static void main(String[] args) {
		String source = "mid=\"3566930046944013\" isForward=\"1\" class=\"W";
		System.out.println(source.contains("isForward=1"));
	}
}
