package com.weibo.sina.cookie.enterprise;

import java.net.SocketTimeoutException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mbm.elint.entity.DocInfo;
import com.mbm.elint.entity.PersonInfo;
import com.mbm.elint.entity.util.MyJson;
import com.mbm.elint.entity.util.UrlPojo;
import com.mbm.elint.manager.database.DocInfoManager;
import com.mbm.util.DateUtil;
import com.mbm.util.StringOperatorUtil;
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
// 代表每次的autowire得到的对象都不一样
public class GrabEnterContentInfo {
	public static Logger logger = Logger.getLogger(GrabEnterContentInfo.class);
	@Autowired
	private DocInfoManager docInfoManager;
	// private long docUpdateTime = 0;
	// private long docSerachTime = 0;
	private GrabPageResource grabPageResource = null;
	// private MediaPersonInfo mediaPersonInfo = null;
	SinaVersionChangeUtil sinaVersionChangeUtil = new SinaVersionChangeUtil();
	private HtmlParserUtil htmlParserUtil=new HtmlParserUtil();
	
	private PersonInfo person = null;
	private UrlPojo urlPojo;
	private String sendUrl;
	private String produceURL = null;
	private String oid;

	@Autowired
	private DateUtil dateUtil = new DateUtil();

	@Autowired
	private MyJson myJSON = new MyJson();
	// private String ajax_mess_url = null;
	private String current_URL = null;

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

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

	public void init(PersonInfo person, GrabPageResource grabPageResource,
			UrlPojo urlPojo) {
		this.person = person;
		this.grabPageResource = grabPageResource;
		this.urlPojo = urlPojo;
		this.sendUrl = MyStringUtils.getUrlRemoveSlash(urlPojo.getUrl());
		// this.produceURL = sinaVersionChangeUtil.getEnterAndProUrl(sendUrl);
		this.produceURL = sinaVersionChangeUtil.getPersonUrl(sendUrl);
	}

	public void init(GrabPageResource grabPageResource) {
		this.grabPageResource = grabPageResource;
	}

	public void init(GrabPageResource grabPageResource, PersonInfo person) {
		this.grabPageResource = grabPageResource;
		this.person = person;
	}

	public void init(GrabPageResource grabPageResource, PersonInfo person,
			String sendURL) {
		this.grabPageResource = grabPageResource;
		this.person = person;
		this.sendUrl = sendURL;
//		this.produceURL = sinaVersionChangeUtil.getEnterAndProUrl(sendUrl);
		this.produceURL = sinaVersionChangeUtil.getPersonUrl(sendUrl);
	}

	int page = 1;
	String content = null;
	String beginRegex = null;
	String endRegex = null;
	AnsjPaser ansjContent = null;
	AnsjPaser list_parser = null;
	AnsjPaser content_parser = null;
	AnsjPaser date_parser = null;
	AnsjPaser docUrl_parser = null;
	AnsjPaser follow_parser = null;
	AnsjPaser transmit_parser = null;
	AnsjPaser critic_parser = null;
	AnsjPaser mid_parser = null;
	boolean flag = true;
	int count = 0;
	String temp_item = "";
	String content_temp = "";
	int repeat_count = 0;// 记录当前页的内容当中，有几条是重复的,当达指定的每页最大重复数时，即为终止

	public void grabArticleContents(UrlPojo urlPojo) throws Exception {
		page = 1;
		this.oid = person.getUid();
		while (page <= 50) {
			current_URL = this.produceURL;
			logger.info("企业版---正在抓取" + this.current_URL + "的第" + page + "页");
			if (page == 1) {
				// 先抓取第一页
				if (!grabOnePageArticle(current_URL, true)) {
					break;
				}
				// 第一页没有错误,继续抓取第2页
				current_URL = StaticValue.Enterprise_Wb_Content_Page_Next_URL
						.replace("${pageNum}", page + "").replace("${prePage}",
								page + "").replace("${pageBar}", 0 + "")
						.replace("${oid}", this.oid);
				// logger.info("current_URL--" + current_URL);
				if (!grabOnePageArticle(current_URL, false)) {
					break;
				}
				// 继续抓取第3页
				current_URL = StaticValue.Enterprise_Wb_Content_Page_Next_URL
						.replace("${pageNum}", page + "").replace("${prePage}",
								page + "").replace("${pageBar}", 1 + "")
						.replace("${oid}", this.oid);
				// logger.info("current_URL--" + current_URL);
				if (!grabOnePageArticle(current_URL, false)) {
					break;
				}
			} else {
				/**
				 * 大于第一页时，注意此时的prePage的变化,第一次依然是page-1,后边才是page
				 */
				current_URL = StaticValue.Enterprise_Wb_Content_Page_Next_URL
						.replace("${pageNum}", page + "").replace("${prePage}",
								(page - 1) + "").replace("${pageBar}", 0 + "")
						.replace("${oid}", this.oid);
				if (!grabOnePageArticle(current_URL, false)) {
					break;
				}
				current_URL = StaticValue.Enterprise_Wb_Content_Page_Next_URL
						.replace("${pageNum}", page + "").replace("${prePage}",
								(page) + "").replace("${pageBar}", 0 + "")
						.replace("${oid}", this.oid);
				if (!grabOnePageArticle(current_URL, false)) {
					break;
				}
				current_URL = StaticValue.Enterprise_Wb_Content_Page_Next_URL
						.replace("${pageNum}", page + "").replace("${prePage}",
								(page) + "").replace("${pageBar}", 1 + "")
						.replace("${oid}", this.oid);
				if (!grabOnePageArticle(current_URL, false)) {
					break;
				}
			}
			page++;
			Thread.sleep(StaticValue.cookie_sleep_interval_time);
		}
		// logger.info("企业版---" + this.sendUrl + "抓取结束");
	}

	public boolean grabOnePageArticle(String url, boolean isFirstPage) {
		int error_repeat_times = 0;
		
		while (error_repeat_times < StaticValue.url_repeat_count) {
			try {
				content = grabPageResource.getPageSourceOfSina(url,
						StaticValue.Referer_Enter);
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
		
		if (isFirstPage) {
			// 抽取url源码中的数据列表json串
			beginRegex = "pl_content_hisFeed\",";
			endRegex = "}\\)";
			ansjContent = new AnsjPaser(beginRegex, endRegex, content,
					AnsjPaser.TEXTEGEXANDNRT);
			content = ansjContent.getText();
			// logger.info("第1页content---" + content);
			myJSON.createJson("{" + content + "}");
			content = myJSON.getStringByKey("html");
			content += "<dl action-type=\"feed_list_item\"";
			// logger.info("content----" + content);
		} else {
			// logger.info("非第1页content---" + content);
			myJSON.createJson(content);
			content = myJSON.getStringByKey("data");
			// System.out.println("非firstPage转换后的content----" + content);
			// System.out.println("*********************");
			// return;
		}

		if (!content.contains("feed_list_item")) {
			logger.info("企业版---该URL的微博内容已经抓取完成!");
			return false;
		} else if (!saveOnePageContent(content)) {
			return false;
		}

		return true;
	}

	public boolean saveOnePageContent(String content) {
		/**
		 * 此处是为了便于匹配抽取，手工添加该suffix String
		 */
		// 得到json串后，首先抽取最外层的feed_lists div
		beginRegex = "action-type=\"feed_list_item\"";
		// endRegex = "/dl>[\\s]*<dl";
		endRegex = "/dl>[\\s]*((<dl)|(<div node-type=\"lazyload\")|([\\s]+</div)|(<div node-type=\"feed_list_page\"))";
		list_parser = new AnsjPaser(beginRegex, endRegex, content,
				AnsjPaser.TEXTEGEXANDNRT);

		// 下面将每个要提取的字段，正则化
		// 微言正文内容
		beginRegex = "<p" + AnsjPaser.TEXTEGEXANDNRT + ">";
		endRegex = "</p>";
		content_parser = new AnsjPaser(beginRegex, endRegex,
				AnsjPaser.TEXTEGEXANDNRT);

		// 抽取发布时间
		beginRegex = "<a title=\"";
		endRegex = "\" node-type=\"feed_list_item_date";
		date_parser = new AnsjPaser(beginRegex, endRegex, "[\\d\\-:\\s]{8,20}");

		// 抽取docURL
		beginRegex = "date=\"[\\d]+\" href=\"\\/([\\d]+)/";
		endRegex = "\" class=\"date\">";
		docUrl_parser = new AnsjPaser(beginRegex, endRegex,
				AnsjPaser.TEXTEGEXANDNRT);

		// 抽取來自哪里
		beginRegex = "feed_list_tagList" + AnsjPaser.TEXTEGEXANDNRT
				+ "rel=\"nofollow\">";
		endRegex = "</a>";
		follow_parser = new AnsjPaser(beginRegex, endRegex,
				AnsjPaser.TEXTEGEXANDNRT);

		// 抽取转发数量
		beginRegex = "feed_list_tagList" + AnsjPaser.TEXTEGEXANDNRT + "转发\\(";
		endRegex = "\\)</a>";
		transmit_parser = new AnsjPaser(beginRegex, endRegex,
				AnsjPaser.TEXTEGEXANDNRT);

		// 抽取转发数量
		beginRegex = "feed_list_tagList" + AnsjPaser.TEXTEGEXANDNRT + "评论\\(";
		endRegex = "\\)</a>";
		critic_parser = new AnsjPaser(beginRegex, endRegex,
				AnsjPaser.TEXTEGEXANDNRT);

		// 抽取mid数据
		beginRegex = "mid=\\\"";
		endRegex = "\\\"";
		mid_parser = new AnsjPaser(beginRegex, endRegex,
				AnsjPaser.TEXTEGEXANDNRT);
		flag = true;
		count = 0;
		repeat_count = 0;
		while (list_parser.hasNext()) {
			DocInfo doc = new DocInfo();
			/**
			 * 首先设置uid,作为 docURL的prefix
			 */
			doc.setPersonId(person.getUid());
			// count++;
			temp_item = list_parser.getNext();
			content_temp = content_parser.reset(temp_item).getText();
			// logger.info("content--" + count + ":  " + content_temp);
			doc.setArticle(content_temp);
			content_temp = docUrl_parser.reset(temp_item).getText();
			if (content_temp == null || content_temp.trim().length() == 0) {
				return false;// 若没得到docURL,直接跳过该微言
			} else {
				doc.setDocUrl(FormatOutput.urlPrefix_ProAndEnter
						+ doc.getPersonId() + "/" + content_temp);
			}
			// logger.info("docUrl--:" + content_temp);
			content_temp = date_parser.reset(temp_item).getText();
			content_temp += ":00";
			try {
				doc.setPublishtime(dateUtil.getDateByString(content_temp));
			} catch (Exception e) {
				logger.info("补捉到时间转化导师常,该企业版抓取认为结束!");
				return false;
			}

			// logger.info("date--:" + content_temp);
			content_temp = follow_parser.reset(temp_item).getText();
			doc.setOrigin(content_temp);
			// logger.info("來自--:" + content_temp);
			content_temp = transmit_parser.reset(temp_item).getText();
			if (content_temp == null) {
				doc.setTransmit(0);
			} else {
				doc.setTransmit(Integer.parseInt(content_temp));
			}
			// logger.info("转发--:" + content_temp);
			content_temp = critic_parser.reset(temp_item).getText();
			if (content_temp == null) {
				doc.setDiscuss(0);
			} else {
				doc.setDiscuss(Integer.parseInt(content_temp));
			}
			// logger.info("评论--:" + content_temp);
			content_temp = mid_parser.reset(temp_item).getText();
			doc.setDocArticleId(content_temp);
			// logger.info("mid--:" + content_temp);

			doc.setSinaDocParam("0");
			// time = doc.getPublishtime().getTime();****************
			flag = true;
			doc.setAuthor(person.getName());// ************************
			doc.setWeiboType(1);
			/**
			 * 设置原始的url
			 */
			doc.setSendUrl(this.sendUrl);
			doc.setGrab_method("media");
			doc.setInsertTime(new Date());
			// 设置新加的person_id_long
			doc.setPerson_id_long(Long.parseLong(doc.getPersonId()));// *****************8
			doc.setGrab_method("enterprise");
			try {
				if (!docInfoManager.saveOrUpdate(doc)) {// 此时的没有保存成功意为数据重复了
					if ((++repeat_count) >= StaticValue.max_content_page_repeat_items) {
						// 此时即定为重复页，不再抓取
						logger.info("重复已达到最大，该url的内容抓取结束!");
						return false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("企业版--微博内容抓取时出现问题，此url抓取结束");
				return false;
			}
			// logger.info("企业版--doc--" + doc);
			System.out.println("enterprise doc save successful");
		}
		repeat_count = 0;
		if (temp_item.trim().length() == 0) {// 说明while循环 没有执行，即抓取结束
			return false;
		} else {
			return true;
		}
	}

}
