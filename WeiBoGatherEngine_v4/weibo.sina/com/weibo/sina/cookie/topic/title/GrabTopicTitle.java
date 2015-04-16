package com.weibo.sina.cookie.topic.title;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mbm.elint.entity.mongoDB.TopicDoc;
import com.mbm.elint.entity.util.KeywordPojo;
import com.mbm.elint.entity.util.MyJson;
import com.mbm.elint.manager.business.spider.TopicTitleManager;
import com.mbm.elint.manager.database.mongoDB.TopicInfoManager;
import com.mbm.util.DateUtil;
import com.weibo.common.utils.AnsjPaser;
import com.weibo.common.utils.FormatOutput;
import com.weibo.common.utils.StaticValue;
import com.weibo.sina.cookie.common.GrabPageResource;

@Component
@Scope(value = "prototype")
// 代表每次的autowire得到的对象都不一样
public class GrabTopicTitle {
	public static Logger logger = Logger.getLogger(GrabTopicTitle.class);
	@Autowired
	private TopicInfoManager topicInfoManager;

	private GrabPageResource grabPageSource = null;
	private String sendUrl;

	private TopicTitleManager topicTitleManager = null;

	@Autowired
	private MyJson myJSON = new MyJson();

	public String getSendUrl() {
		return sendUrl;
	}

	public void setSendUrl(String sendUrl) {
		this.sendUrl = sendUrl;
	}

	public void init(GrabPageResource grabPageSource,
			TopicTitleManager topicTitleManager) {
		this.grabPageSource = grabPageSource;
		this.topicTitleManager = topicTitleManager;
	}

	String topic_url = null;
	String content = null;
	@Autowired
	private DateUtil dateUtil = new DateUtil();

	String beginRegex = null;
	String endRegex = null;
	AnsjPaser ansjHtml = null;
	AnsjPaser ansjContent = null;
	AnsjPaser ansjHotCount = null;
	AnsjPaser ansjTid = null;
	boolean flag = false;
	int repeat_count = 0;
	String c1 = null;
	TopicDoc topicDoc = null;
	String sayUrl = null;
	String temp_hour_hot_count = null;
	String article = null;

	int temp_count = 0;
	long last_time = 0;

	public void grabTopicTitleItems() throws Exception {
		logger.info("话题标题抓取---开始");
		last_time = DateUtil.getLongByDate();
		topic_url = "http://huati.weibo.com/aj_topiclist/big?ctg1=99&ctg2=0&prov=0&sort=time&p=1&t=1&_t=0&__rnd="
				+ last_time;
		if (!grabOnePageArticle(topic_url)) {
			return;
		}
		logger.info("话题标题抓取---完毕");
	}

	public boolean grabOnePageArticle(String url) throws Exception {
		content = grabPageSource
				.getPageSourceOfSina(
						url,
						"http://huati.weibo.com/?bottomnav=1&wvr=5#!/?ctg1=99&ctg2=0&prov=0&sort=time&p=1");
		// content = IOUtil.readFile("D://topic.txt");
		if (StringUtils.isBlank(content)) {
			return false;
		}
		myJSON.createJson(content);
		content = myJSON.getStringByKey("data");
		myJSON.createJson(content);
		content = myJSON.getStringByKey("html");
		// logger.info("话题ajax返回值----" + content);
		if (!saveOnePageContent(content)) {
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
		// 模块抽取
		beginRegex = "<li";
		endRegex = "</li>";
		ansjHtml = new AnsjPaser(beginRegex, endRegex, content,
				AnsjPaser.TEXTEGEXANDNRT);

		// 链接抽取--抽取每个话题对应的url链接
		beginRegex = "tid=";
		endRegex = "&";
		ansjTid = new AnsjPaser(beginRegex, endRegex, content,
				AnsjPaser.TEXTEGEXANDNRT);

		// 正文抽取
		// 特别注意:此处最后的:是中文状态的"："
		beginRegex = "topic=";
		endRegex = "\"";
		ansjContent = new AnsjPaser(beginRegex, endRegex,
				AnsjPaser.TEXTEGEXANDNRT);// 全文抓取保存，不再过滤html标签
		// 一小时热度抽取
		beginRegex = "<span class=\"num_info\">";
		endRegex = "</span>";
		ansjHotCount = new AnsjPaser(beginRegex, endRegex);

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
				logger.info("该条话题标题的内容为空直接返回false,不做保存!");
				return false;
			}
			topicDoc = new TopicDoc();
			// logger.info("话题名称---" + article);
			topicDoc.setContent(article);

			// topic id提取
			topicDoc.setTopicUrl(FormatOutput.topic_urlPrefix
					+ ansjTid.reset(c1).getText());
			temp_hour_hot_count = ansjHotCount.reset(c1).getText();
			topicDoc.setOneHourHotCount(Integer.parseInt(temp_hour_hot_count));
			// logger.info("hour count---" + temp_hour_hot_count);

			// 暂时用new Date来代替
			topicDoc.setPublishTime(new Date());

			topicDoc.setGrabTime(new Date());

			flag = true;
			if (!topicInfoManager.saveOrUpdate(topicDoc)) {//
				// 此时的没有保存成功意为数据重复了
				if ((++repeat_count) >= StaticValue.max_content_page_repeat_items_comments) {
					// 此时即定为重复页，不再抓取
					logger.info("此次抓取的话题标题列表重复已达到最大，话题标题列表抓取已结束!");
					return false;
				}
			}
			/**
			 * 保存一个话题后,检查是否开始抓取话题后,将话题标题加入到抓取标题对应的内容列表
			 */
			if (StaticValue.topic_title_add_grab_task_enable) {
				// 0 代表在周期列表抓取当中
				topicTitleManager.addOneTopicTitle(new KeywordPojo(topicDoc
						.getContent(), 0));
			}

			logger.info("话题标题列表topicDoc--" + topicDoc);
		}
		repeat_count = 0;
		return true;
	}

	public static void main(String[] args) {
		// System.out.println(new Date().getTime());
		System.out
				.println("\u62b1\u6b49\uff0c\u4f60\u6ca1\u6709\u6743\u9650\u8bbf\u95ee\u6b64\u9875\u9762");
	}
}
