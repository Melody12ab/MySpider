package com.weibo.qq.cookie.keywords;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mbm.elint.entity.DocInfo;
import com.mbm.elint.entity.SinaKeySearchInfo;
import com.mbm.elint.entity.util.KeywordPojo;
import com.mbm.elint.manager.business.SinaKeySearchInfoManager;
import com.mbm.elint.manager.business.SinaKeySearchResultManager;
import com.mbm.elint.manager.business.spider.UrlsManager;
import com.mbm.util.DateUtil;
import com.weibo.common.utils.AnsjPaser;
import com.weibo.common.utils.StaticValue;
import com.weibo.qq.cookie.common.GrabPageSource4QQ;
import com.weibo.utils.qq.StaticValue4QQ;
import com.weibo.utils.qq.UidGeneratorUtil;
import com.weibo.utils.sina.MyStringUtils;

//抓取搜索关键词之后的结果信息
@Component
@Scope(value = "prototype")
public class GrabSearchInfo4QQ {
	public static Logger logger = Logger.getLogger(GrabSearchInfo4QQ.class);
	private GrabPageSource4QQ grabPageSource4QQ;
	long time = 0;
	long tempId = 0L;
	long docUpdateTime = 0;
	@Autowired
	private SinaKeySearchResultManager sinaKeySearchResultManager;
	@Autowired
	private SinaKeySearchInfoManager sinaKeySearchInfoManager;
	private UrlsManager urlManager;
	@Autowired
	DateUtil dateUtil = new DateUtil();
	@Autowired
	private UidGeneratorUtil uidGeneratorUtil;

	public void init(GrabPageSource4QQ grabPageSource4QQ, UrlsManager urlManager) {
		this.grabPageSource4QQ = grabPageSource4QQ;
		this.urlManager = urlManager;
	}

	// 专门测试而加
	public void init(GrabPageSource4QQ grabPageSource4QQ) {
		this.grabPageSource4QQ = grabPageSource4QQ;
	}

	public boolean getSearchResultPage(String keyword, String content)
			throws Exception {
		String beginRegex = null;
		String endRegex = null;

		// 验证是否有符合条件的结果
		beginRegex = "<div class=\"noresult\">";
		endRegex = "</div>";
		AnsjPaser ansjSearch = new AnsjPaser(beginRegex, endRegex, content,
				AnsjPaser.TEXTEGEXANDNRT);
		String dempSearch = ansjSearch.getText();
		// logger.info("dempSearch--------" + dempSearch);
		if (dempSearch != null) {
			if (dempSearch.contains("抱歉")) {
				logger.info("关键词--" + keyword
						+ "--的抓取到尾页了_++++++++++++++++++++++++++++++");
				isNormal = true;
				return false;
			} else if (dempSearch.contains("相关法律")) {
				isNormal = true;
				logger.info("关键词--" + keyword + "--出现不允许搜索的关键词，将跳过");
				return false;
			}
		}

		// 模块抽取
		beginRegex = "<ul id=\"talkList\" class=\"LC\">";
		endRegex = "</ul>";
		AnsjPaser ansjHtml = new AnsjPaser(beginRegex, endRegex, content,
				AnsjPaser.TEXTEGEXANDNRT);
		String html = ansjHtml.getText();
		// logger.info("html--" + html);

		// 单条微博内容块的提取
		beginRegex = "<li";
		endRegex = "</li>";
		AnsjPaser ansjInfo = new AnsjPaser(beginRegex, endRegex, html,
				AnsjPaser.TEXTEGEXANDNRT);
		// 提取article
		beginRegex = "div class=\"msgCnt\">";
		endRegex = "</div>";
		AnsjPaser articleAnsj = new AnsjPaser(beginRegex, endRegex);
		// 提取微博内容的docURL
		beginRegex = "<a class=\"time\"" + AnsjPaser.TEXTEGEXANDNRT + "href=\"";
		endRegex = "\"";
		AnsjPaser docUrlAnsj = new AnsjPaser(beginRegex, endRegex);
		// 提取微博发布时间publisTime
		beginRegex = "rel=\"";
		endRegex = "\"";
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
		beginRegex = "class=\"relay\"" + AnsjPaser.TEXTEGEXANDNRT + "num=\"";
		endRegex = "\"";
		AnsjPaser transmitNumAnsj = new AnsjPaser(beginRegex, endRegex);
		// 提取微博内容的评发的数据
		beginRegex = "class=\"comt\"" + AnsjPaser.TEXTEGEXANDNRT + "num=\"";
		endRegex = "\"";
		AnsjPaser commentNumAnsj = new AnsjPaser(beginRegex, endRegex);

		// 姓名抽取
		beginRegex = "<a" + AnsjPaser.TEXTEGEXANDNRT + "title=\"";
		endRegex = "\\(";
		AnsjPaser ansjName = new AnsjPaser(beginRegex, endRegex);

		// ID抽取
		beginRegex = "class=\"userName\" rel=\"";
		endRegex = "\"";
		AnsjPaser ansjId = new AnsjPaser(beginRegex, endRegex);

		int repeat_count = 0;
		int i = 0;
		while (ansjInfo.hasNext()) {
			String item = ansjInfo.getNext();
			// logger.info("item---" + item);
			DocInfo docResult = new DocInfo();
			String sayUrl = docUrlAnsj.reset(item).getText();
			// logger.info("docURL---" + sayUrl);
			if (sayUrl == null || sayUrl.trim().length() == 0) {
				logger.info("微言的url为空直接返回false,不做保存!");
				return false;
			}
			docResult.setDocUrl(sayUrl);

			// logger.info("searchKey---" + searchKey);

			docResult.setSendUrl(searchKey);

			String ids = ansjId.reset(item).getText();

			// logger.info("person id---" + ids);

			docResult.setPersonId(ids);
			if (ids != null && ids.length() != 0) {
				// 这一段先注释，最后添加
				this.urlManager.addToVisitByUidFromKeySearch4QQ(ids);
			}
			String name = ansjName.reset(item).getText();// 姓名

			// logger.info("name---" + name);
			docResult.setAuthor(name);// 文章作者

			String publishTime = publicTimeAnsj.reset(item).getText();

			Date date = new Date(Long.parseLong(publishTime + "000"));
			// 设置publishTime
			// logger.info("publishTime--" + dateUtil.getStringByDate(date));
			docResult.setPublishtime(date);

			docResult.setInsertTime(new Date());// 设置文章的插入时间

			// time = docResult.getPublishtime().getTime();
			// tempId = Long.parseLong(ids);

			String article = articleAnsj.reset(item).getText();// 正文
			/**
			 * 在这里做对元搜索抓取的关键词标红处理
			 */
			// <span style=\"color:red;\">123<\/span>
			article = article.replaceAll(
					"<span style=\"color:red;\">.*?</span>", searchKey);
			// logger.info("article---" + article);
			// dempText = dempText.replaceAll("<.*?>", "");//不再过滤标签
			docResult.setArticle(article);

			String dempRepeat = transmitNumAnsj.reset(item).getText();// 转发次数
			if (dempRepeat != null && dempRepeat.length() != 0) {
				dempRepeat = dempRepeat.replaceAll("\\(|\\)", "");
				// logger.info("转发:" + dempRepeat);
				docResult.setTransmit(Integer.parseInt(dempRepeat));
			} else {
				docResult.setTransmit(0);
			}

			String dempComment = commentNumAnsj.reset(item).getText();// 评论次数
			if (dempComment != null && dempComment.length() != 0) {
				dempComment = dempComment.replaceAll("\\(|\\)", "");
				// logger.info("comment---" + dempComment);
				docResult.setDiscuss(Integer.parseInt(dempComment));
			} else {
				docResult.setDiscuss(0);// 代表无评论
			}
			String dempFrom = originAnsj.reset(item).getText();// 来自

			// logger.info("来自:" + dempFrom);
			docResult.setOrigin(dempFrom);

			String mid = aidAnsj.reset(item).getText();

			// logger.info("mid:" + mid);

			docResult.setDocArticleId(mid);

			docResult.setWeiboType(2);// 2代表腾讯微博

			docResult.setGrab_method("qq");// 也代表腾讯微博
			i++;
			logger.info("qq doc---" + i + "  " + docResult);

			/**
			 * 这里是设置person_id_long,下次要特别转换
			 */
			// 设置新加的person_id_long
			docResult.setPerson_id_long(Long.parseLong(uidGeneratorUtil
					.md5(ids)));
			if (this.sinaKeySearchResultManager.saveSearchResultInfo(docResult)) {
				this.itemNumber++;
			} else {
				if ((++repeat_count) >= StaticValue.max_content_page_repeat_items) {
					logger.info("重复已达到最大，该searchUrl的抓取结束!");
					return false;// 抓取单页的重复数已达到最大值，该url抓取到此为止
				}
			}
			logger.info("qq search doc---" + docResult);
		}
		repeat_count = 0;
		return true;
	}

	public boolean grabSearchOnePageInfo(String keyword, String searchURL)
			throws Exception {
		logger.info("正在抓取的搜索结果是------" + keyword + "--" + searchURL);
		try {
			String content = this.grabPageSource4QQ.getPageSourceOfQQ(
					searchURL, null, StaticValue4QQ.QQ_Keyword_Host);
			// logger.info("keyword content---" + content);
			if (getSearchResultPage(keyword, content)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(keyword + "--" + searchURL + "抓取出现了问题,即将跳过该searchURL");
		}
		return false;
	}

	private String searchKey;
	private int itemNumber;
	private boolean isNormal = false;

	public boolean grabKeySearchInfo(KeywordPojo keyword) throws Exception {
		itemNumber = 0;
		isNormal = false;
		int pageCount = 1;
		String word = keyword.getKeyWord();
		String searchURL = MyStringUtils.getUrlByKeyWord4QQ(word, "211");
		String temp_url = searchURL;
		this.searchKey = word;// 此变量主要用于在doc中保存其关键词
		logger.info("正在抓取的关键词是------" + word);
		while (grabSearchOnePageInfo(word, searchURL)) {
			pageCount += 1;
			searchURL = temp_url + "&p=" + (pageCount);
			try {
				Thread.sleep(StaticValue.keyword_grab_one_page_intervale);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (pageCount >= 51) {
				logger.info("该关键词已抓取至50页了，该关键词的抓取任务结束!");
				break;
			}
		}
		SinaKeySearchInfo sinaKeySearchInfo = new SinaKeySearchInfo();
		sinaKeySearchInfo.setKeyword(word);
		sinaKeySearchInfo.setUpdateTime(new Date());
		sinaKeySearchInfo.setType("qq");
		if (this.itemNumber == 0
				&& keyword.getRepeat_count() < StaticValue.keyword_repeat_count
				&& !isNormal) {
			// 此时说明没有抓取到任务东西,直接抛出异常，重新抓取
			throw new Exception();
		}
		sinaKeySearchInfo.setComment("" + this.itemNumber);
		this.sinaKeySearchInfoManager.saveKeySearchInfo(sinaKeySearchInfo);

		return true;
	}
	// public boolean grabKeySearchInfo(List<KeywordPojo> keywordPojos) {
	// for (KeywordPojo keyWordPojo : keywordPojos) {
	// grabKeySearchInfo(keyWordPojo);
	// }
	// return true;
	// }
}
