package com.weibo.sina.cookie.comment;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mbm.elint.entity.Comments;
import com.mbm.elint.entity.util.MyJson;
import com.mbm.elint.entity.util.UrlPojo;
import com.mbm.elint.manager.business.spider.UrlsManager;
import com.mbm.elint.manager.database.mongoDB.CommentDocInfoManager;
import com.mbm.util.DateUtil;
import com.mbm.util.StringOperatorUtil;
import com.weibo.common.manager.SwtichUserThreadManager;
import com.weibo.common.utils.AnsjPaser;
import com.weibo.common.utils.FormatOutput;
import com.weibo.common.utils.HtmlParserUtil;
import com.weibo.common.utils.StaticValue;
import com.weibo.sina.cookie.common.GrabPageResource;
import com.weibo.utils.sina.MyStringUtils;

@Component
@Scope(value = "prototype")
// 代表每次的autowire得到的对象都不一样
public class GrabCommentInfo4PersonAndMedia {
	public static Logger logger = Logger
			.getLogger(GrabCommentInfo4PersonAndMedia.class);
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

	private HtmlParserUtil htmlParserUtil=new HtmlParserUtil();
	
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
	String current_page_max_id = "";

	String firstId = null;// 记录当前评论页的第一个mid
	int temp_count = 0;

	/**
	 * 为了解决评论抓取过程中，总是中间出现假结束的情况,设置这种情况出现的最大阀值,尽量采全所有的评论
	 * 
	 * @throws Exception
	 */
	public static int page_no_item_max_times = 10;
	int current_page_no_item_max_times = 0;

	public void grabArticleComments() throws Exception {
		page = 1;
		current_page_no_item_max_times = 0;
		while (page <= 1000) {// 这样的话，不管如何都会有个结束
			System.out.println("PersonAndMedia评论抓取---正在抓取" + this.sendUrl
					+ "的第" + page + "页");
			temp_count = 0;
			if (page == 1) {
				url1 = this.sendUrl;
				if (!grabOnePageArticle(url1, true)) {
					return;
				}
			} else {
				url1 = StaticValue.Comment_PersonAndMedia_Wb_Content_Page_Next_URL
						.replace("${pageNum}", "" + page).replace("${firstId}",
								firstId).replace("${max_id}",
								current_page_max_id).replace("${rnd}",
								"" + dateUtil.getLongByDate());
				if (!grabOnePageArticle(url1, false)) {
					return;
				}
			}
			page++;
			
			//人为打断下切换线程
//			if(page==5){
//				System.out.println("人为打断开始");
//				SwtichUserThreadManager.interrupte();
//				System.out.println("人为打断完成");
//			}
			Thread.sleep(StaticValue.cookie_sleep_interval_time);
		}
		// logger.info("PersonAndMedia评论抓取---抓取" + this.sendUrl + " 完毕!");
		System.out.println("PersonAndMedia评论抓取---抓取" + this.sendUrl + " 完毕!");
	}

	public boolean grabOnePageArticle(String url, boolean isFirstPage) {
		int error_repeat_times = 0;

		while (error_repeat_times < StaticValue.url_repeat_count) {
			try {
				content = grabPageSource.getPageSourceOfSina(url, this.sendUrl,
						isFirstPage);
				
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
			} catch (Exception e) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
					System.out.println("出现了请求异常，要睡会再继续请求!");
				}
			}
			error_repeat_times++;
		}
		// System.out.println("first page content---" + content);
		if (StringUtils.isBlank(content)) {
			return false;
		}
		
		
		// logger.info("content的源码是----" + content);
		if (isFirstPage) {
			// 抽取url源码中的数据列表json串
			beginRegex = "ns\":\"pl.content.weiboDetail.index\",";
			endRegex = "}\\)";
			ansjContent = new AnsjPaser(beginRegex, endRegex, content,
					AnsjPaser.TEXTEGEXANDNRT);
			content = ansjContent.getText();
			myJSON.createJson("{" + content + "}");
			content = myJSON.getStringByKey("html");

			// logger.info("isFirstPage content的源码是----" + content);
			// 取得首页的comments_list
			beginRegex = "class=\"comment_lists\">";// 个人版、媒体版
			endRegex = "</div>\\s{1,100}<!--评论列表";
			ansjContent = new AnsjPaser(beginRegex, endRegex, content,
					AnsjPaser.TEXTEGEXANDNRT);
			content = ansjContent.getText();

			/**
			 * 在此处取出当前页的max_id
			 */
			beginRegex = "&max_id=";// 个人版、媒体版
			endRegex = "&";
			AnsjPaser ansjMaxId = new AnsjPaser(beginRegex, endRegex, content,
					AnsjPaser.TEXTEGEXANDNRT);
			current_page_max_id = ansjMaxId.getText();
		} else {
			// logger.info("非第1页content---" + content);\
			//在这里捕捉异常，作为非json的处理，直接过滤不进行转换
			try{
				myJSON.createJson(content);
				myJSON.createJson(myJSON.getStringByKey("data"));
				content = myJSON.getStringByKey("html");
			}catch(Exception e){
				logger.info("偶然一次json解析错误，抛掉该json解析，直接放行!");
			}
		}
		// logger.info("contentDoc的源码是----" + content);
		if (!content.contains("<dl")) {
			current_page_no_item_max_times++;
			if (current_page_no_item_max_times < page_no_item_max_times) {
				System.out.println("看到了结束页,不知道真假，故假结束一次,继续往下翻页!");
				// 作为真返回一下
				return true;
			} else {
				System.out.println("看到了结束页，太多次了，认为是真的，评论列表抓取已结束!");
				return false;
			}
		} else if (!saveOnePageContent(content)) {
			return false;
		}
		// 说明遇到了有数据的页码,重置下以前的设置
		current_page_no_item_max_times = 0;

		if (StringOperatorUtil.isBlank(current_page_max_id)) {
			System.out.println("该评论列表仅有一页，抓取结束!");
			return false;
		}
		return true;
	}

	private String temp_anything = null;

	public boolean saveOnePageContent(String content) {
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
		endRegex = "<span class=\"S_txt2\">";
		ansjContent = new AnsjPaser(beginRegex, endRegex,
				AnsjPaser.TEXTEGEXANDNRT);// 全文抓取保存，不再过滤html标签
		// 时间抽取
		beginRegex = "<span class=\"S_txt2\">";
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
				// logger.info("该条评论的内容为空直接返回false,不做保存!");
				System.out.println("该条评论的内容为空直接返回false,不做保存!");
				return false;
			}
			comment = new Comments();
			// logger.info("comment content---" + article);
			comment.setContent(article);

			comment.setDocurl(this.sendUrl);
			// logger.info("comment docUrl---" + this.sendUrl);

			temp_time = ansjPubTime.reset(c1).getText();
			// logger.info("comment publicTime---" + temp_time);
			// 暂时用new Date来代替
			// commentDoc.setPublishtime(new Date(Long.parseLong(sg)));
			if (temp_time.contains("(")) {
				temp_time = temp_time.substring(1, temp_time.length() - 1);
			}
			try {
				comment.setPublishtime(dateUtil
						.getDateByNoneStruture4Sina(temp_time));
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

			comment.setGrabtime(new Date());

			// 评论者的uid的提取
			commentUid = ansjUid.reset(c1).getText();
			comment.setCommentUid(commentUid);

			if (StaticValue.comment_uid_add_account_tovisit) {
				UrlPojo urlPojo = new UrlPojo(FormatOutput.urlPrefix
						+ comment.getCommentUid(), 1);
				// this.urlsManager.addUrl(urlPojo);
			}
			if (StaticValue.comment_uid_add_doc_tovisit) {
				UrlPojo urlPojo = new UrlPojo(FormatOutput.urlPrefix
						+ comment.getCommentUid(), 2);
				// this.urlsManager.addUrl(urlPojo);
			}

			// logger.info("comment uid---" + commentUid);
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
			try {
				if (!commentDocInfoManager.saveOrUpdate(comment)) {//
					// 此时的没有保存成功意为数据重复了
					if ((++repeat_count) >= StaticValue.max_content_page_repeat_items_comments) {
						// 此时即定为重复页，不再抓取
						// logger.info("该docUrl的评论列表重复已达到最大，评论列表抓取已结束!");
						current_page_no_item_max_times++;
						if (current_page_no_item_max_times < page_no_item_max_times) {
							System.out
									.println("该docUrl的评论列表重复已达到最大，假结束一次,继续往下翻页!");
						} else {
							System.out
									.println("该docUrl的评论列表重复已达到最大，评论列表抓取已结束!");
							return false;
						}
					}
				}
			} catch (Exception e) {
				logger.info("保存一条语句时出现错误，暂放弃该条数据!");
			}

			// logger.info("PersonAndMedia评论列表commentDoc--" + commentDoc);
			System.out.println("PersonAndMedia评论列表commentDoc--" + comment);
		}
		repeat_count = 0;
		return true;
	}

	public static void main(String[] args) {
		// System.out.println(StringUtils.isBlank());
	}
}
