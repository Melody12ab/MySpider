package com.weibo.sina.cookie.topic.content;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mbm.elint.entity.DocInfo;
import com.mbm.elint.entity.util.KeywordPojo;
import com.mbm.elint.entity.util.MyJson;
import com.mbm.elint.manager.database.DocInfoManager;
import com.mbm.util.DateUtil;
import com.weibo.common.utils.AnsjPaser;
import com.weibo.common.utils.StaticValue;
import com.weibo.sina.cookie.common.GrabPageResource;

@Component
@Scope(value = "prototype")
// 代表每次的autowire得到的对象都不一样
public class GrabTopicContent {
	public static Logger logger = Logger.getLogger(GrabTopicContent.class);
	@Autowired
	private DocInfoManager docInfoManager;
	private GrabPageResource grabPageSource = null;
	private String sendUrl;

	@Autowired
	private MyJson myJSON = new MyJson();

	public String getSendUrl() {
		return sendUrl;
	}

	public void setSendUrl(String sendUrl) {
		this.sendUrl = sendUrl;
	}

	public void init(GrabPageResource grabPageSource) {
		this.grabPageSource = grabPageSource;
	}

	String topic_url = null;
	String content = null;
	@Autowired
	private DateUtil dateUtil = new DateUtil();

	String beginRegex = null;
	String endRegex = null;
	AnsjPaser ansjHtml = null;
	AnsjPaser ansjContent = null;
	AnsjPaser ansjName = null;
	AnsjPaser ansjMid = null;
	AnsjPaser ansjUid = null;
	AnsjPaser ansjDocUrl = null;
	AnsjPaser ansjPublishTime = null;
	AnsjPaser ansjFrom = null;
	AnsjPaser ansjComment = null;
	AnsjPaser ansjTransmit = null;
	AnsjPaser ansjNextPage = null;

	boolean flag = false;
	int repeat_count = 0;
	String c1 = null;
	DocInfo topicContentItemDoc = null;
	String docUrl = null;
	String temp_hour_hot_count = null;
	String article = null;

	int temp_count = 0;
	long now_time = 0;
	int page_number = 0;
	String nickName = null;
	String uid = null;
	String mid = null;
	String publishTime_string = null;
	String from = null;
	String transmit = null;
	String comment = null;

	boolean hasNextPage = true;
	String next_page_string = null;
	private String topicTitle = null;

	public void grabTopicContentItems(KeywordPojo topicTitlePojo)
			throws Exception {
		topicTitle = topicTitlePojo.getKeyWord();
		hasNextPage = true;
		page_number = 1;
		while (hasNextPage && page_number <= 50) {
			// logger.info("正在抓取话题-" + topicTitle + "-的内容列表,第-" + page_number
			// + "-页");
			System.out.println("正在抓取话题-" + topicTitle + "-的内容列表,第-"
					+ page_number + "-页");
			now_time = DateUtil.getLongByDate();
			// topic_url = StaticValue.topic_content_page_next_url.replace(
			// "${keyword}", URLEncoder.encode(topicTitle)).replace(
			// "${pageNumber}", "" + page_number).replace("${time_long}",
			// "" + now_time);
			// 严格按照get请求中，在httpwatch中捕捉到的参数值传递，捕捉到的是中文，并没有URLEncoder.encode()进行url编码,所以此处的也不可调用进行url编码
			topic_url = StaticValue.topic_content_page_next_url.replace(
					"${keyword}", topicTitle).replace("${pageNumber}",
					"" + page_number).replace("${time_long}", "" + now_time);
			if (!grabOnePageArticle(topicTitle, topic_url)) {
				return;
			}
			page_number++;

			// 由于它有反爬，故要等待一段时间
			try {
				Thread.sleep(StaticValue.keyword_grab_one_page_intervale);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// logger.info("话题-" + topicTitle + "-的内容列表抓取---完毕");
		System.out.println("话题-" + topicTitle + "-的内容列表抓取---完毕");
	}

	public boolean grabOnePageArticle(String topicTitle, String url)
			throws Exception {
		content = grabPageSource.getPageSourceOfSina(url, null);
		if (StringUtils.isBlank(content)) {
			return false;
		}
		// logger.info("content---" + content);
		// logger.info("url---" + url);
		myJSON.createJson(content);
		content = myJSON.getStringByKey("data");
		myJSON.createJson(content);
		content = myJSON.getStringByKey("html");
		// logger.info("话题content----ajax返回值----" + content);
		// 当出现刷新太快时，该线程将一直等待
		while (content != null && content.contains("你刷新太快啦")) {
			// 此时即将等待
			System.out.println("话题内容的抓取刷新太快了，将进入等待周期!");
			Thread
					.sleep(StaticValue.topic_content_grab_too_fast_fresh_waitting_time);
		}
		if (!saveOnePageContent(topicTitle, content)) {
			return false;
		}
		Thread.sleep(StaticValue.cookie_sleep_interval_time);
		return true;
	}

	public boolean saveOnePageContent(String topicTitle, String content)
			throws IOException, ParseException {
		// content = IOUtil.readFile("D://topic.txt");
		if (StringUtils.isBlank(content)) {
			return false;
		}
		// 每个item模块抽取
		beginRegex = "i node-";
		endRegex = "<div class=\"clear\"></div>";
		ansjHtml = new AnsjPaser(beginRegex, endRegex, content,
				AnsjPaser.TEXTEGEXANDNRT);

		// 链接抽取--抽取每个话题对应的url链接
		beginRegex = "time W_textc[\\s\\S]*?href=\"";
		endRegex = "\"";
		ansjDocUrl = new AnsjPaser(beginRegex, endRegex, content,
				AnsjPaser.TEXTEGEXANDNRT);

		// 正文抽取
		// 特别注意:此处最后的:是中文状态的"："
		beginRegex = "<p class=\"con_txt\">";
		endRegex = "</p>";
		ansjContent = new AnsjPaser(beginRegex, endRegex,
				AnsjPaser.TEXTEGEXANDNRT);// 全文抓取保存，不再过滤html标签

		// 昵称
		beginRegex = "usercard" + AnsjPaser.TEXTEGEXANDNRT + "title=\"";
		endRegex = "\"";
		ansjName = new AnsjPaser(beginRegex, endRegex);

		// uid
		beginRegex = "usercard" + AnsjPaser.TEXTEGEXANDNRT + "uid=";
		endRegex = "\"";
		ansjUid = new AnsjPaser(beginRegex, endRegex);
		// mid
		beginRegex = "list-data=\"mid=";
		endRegex = "\"";
		ansjMid = new AnsjPaser(beginRegex, endRegex);

		// publish time
		beginRegex = "time W_textc" + AnsjPaser.TEXTEGEXANDNRT + "href="
				+ AnsjPaser.TEXTEGEXANDNRT + ">";
		endRegex = "</a";
		ansjPublishTime = new AnsjPaser(beginRegex, endRegex);

		// from where
		beginRegex = "from W_textc\">来自<" + AnsjPaser.TEXTEGEXANDNRT
				+ "((title=\")|>)";
		endRegex = "((\")|(</a))";
		ansjFrom = new AnsjPaser(beginRegex, endRegex);

		// transmit
		beginRegex = "转发\\(";
		endRegex = "\\)";
		ansjTransmit = new AnsjPaser(beginRegex, endRegex);

		// transmit
		beginRegex = "评论\\(";
		endRegex = "\\)";
		ansjComment = new AnsjPaser(beginRegex, endRegex);

		// 是否有下一页的判断
		beginRegex = "li class=\"feed_pages\"" + AnsjPaser.TEXTEGEXANDNRT;
		endRegex = "</li";
		ansjNextPage = new AnsjPaser(beginRegex, endRegex);

		flag = false;
		// 开始抽取
		repeat_count = 0;// 记录当前页的内容当中，有几条是重复的,当达指定的每页最大重复数时，即为终止

		while (ansjHtml.hasNext()) {
			c1 = ansjHtml.getNext();
			// logger.info("c1----" + c1);
			/**
			 * 具体取到每条评论的内容
			 */
			article = ansjContent.reset(c1).getText();
			if (article == null || article.trim().length() == 0) {
				// logger.info("该条话题的内容为空直接返回false,不做保存!");
				System.out.println("该条话题的内容为空直接返回false,不做保存!");
				return false;
			}
			topicContentItemDoc = new DocInfo();

			// temp_count++;// 记数用

			topicContentItemDoc.setArticle(article);
			// logger.info("话题内容条目---" + article);

			// docUrl提取
			docUrl = ansjDocUrl.reset(c1).getText();
			topicContentItemDoc.setDocUrl(docUrl);
			// logger.info("条目docUrl---" + docUrl);

			nickName = ansjName.reset(c1).getText();
			topicContentItemDoc.setAuthor(nickName);
			// logger.info("条目nickName---" + nickName);

			uid = ansjUid.reset(c1).getText();
			topicContentItemDoc.setPersonId(uid);
			// logger.info("条目uid---" + uid);

			mid = ansjMid.reset(c1).getText();
			topicContentItemDoc.setDocArticleId(mid);
			// logger.info("条目mid---" + mid);

			publishTime_string = ansjPublishTime.reset(c1).getText();
			// logger.info("条目publishtime---" + publishTime_string);
			Date publishDate = this.dateUtil
					.getDateByNoneStruture4Sina(publishTime_string);
			topicContentItemDoc.setPublishtime(publishDate);

			from = ansjFrom.reset(c1).getText();
			topicContentItemDoc.setOrigin(from);
			// logger.info("条目from---" + from);

			transmit = ansjTransmit.reset(c1).getText();
			if (transmit == null) {
				topicContentItemDoc.setTransmit(0);
			} else {
				topicContentItemDoc.setTransmit(Integer.parseInt(transmit));
			}
			// logger.info("条目transmit---" + transmit);

			comment = ansjComment.reset(c1).getText();
			if (comment == null) {
				topicContentItemDoc.setDiscuss(0);
			} else {
				topicContentItemDoc.setDiscuss(Integer.parseInt(comment));
			}
			// logger.info("条目comment---" + comment);

			// 非抓取字段填充
			topicContentItemDoc.setGrab_method("topic");
			topicContentItemDoc.setInsertTime(new Date());
			topicContentItemDoc.setWeiboType(1);
			topicContentItemDoc.setSendUrl("#" + topicTitle + "#");
			// 设置新加的person_id_long
			topicContentItemDoc.setPerson_id_long(Long
					.parseLong(topicContentItemDoc.getPersonId()));

			flag = true;

			if (!docInfoManager.saveOrUpdate(topicContentItemDoc)) {//
				// 此时的没有保存成功意为数据重复了
				if ((++repeat_count) >= StaticValue.max_content_page_repeat_items_comments) {
					// 此时即定为重复页，不再抓取
					// logger.info("此次抓取的话题内容条目重复已达到最大，话题内容条目抓取已结束!");
					System.out.println("此次抓取的话题内容条目重复已达到最大，话题内容条目抓取已结束!");
					return false;
				}
			}
			// logger.info("话题Content列表topicDoc--" + topicContentItemDoc);
			System.out.println("话题Content列表topicDoc--" + topicContentItemDoc);
		}

		// 判断是否存在下一页
		next_page_string = ansjNextPage.reset(content).getText();
		if (next_page_string != null && next_page_string.contains("下一页")) {
			// logger.info("有下一页了");
			hasNextPage = true;
		} else {
			hasNextPage = false;
			// System.out.println("没页了!");
		}
		// logger.info("共--" + temp_count + "--条!");
		repeat_count = 0;
		// return false;
		return true;
	}

	public static void main(String[] args) throws Exception {
		// GrabTopicContent grabTopicContent = new GrabTopicContent();
		// grabTopicContent.saveOnePageContent(null, null);
	}
}