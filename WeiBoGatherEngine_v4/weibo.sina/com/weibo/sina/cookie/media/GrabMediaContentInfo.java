package com.weibo.sina.cookie.media;

import java.io.IOException;
import java.net.SocketTimeoutException;
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
import com.mbm.util.DateUtil;
import com.mbm.util.StringOperatorUtil;
import com.weibo.common.manager.SwtichUserThreadManager;
import com.weibo.common.utils.AnsjPaser;
import com.weibo.common.utils.HtmlParserUtil;
import com.weibo.common.utils.StaticValue;
import com.weibo.sina.cookie.common.GrabPageResource;
import com.weibo.utils.sina.MyStringUtils;
import com.weibo.utils.sina.RandomOperator;
import com.weibo.utils.sina.SinaVersionChangeUtil;

@Component
@Scope(value = "prototype")
// 代表每次的autowire得到的对象都不一样
public class GrabMediaContentInfo {
	public static Logger logger = Logger.getLogger(GrabMediaContentInfo.class);
	@Autowired
	private DocInfoManager docInfoManager;
	// private long docUpdateTime = 0;
	// private long docSerachTime = 0;
	private GrabPageResource grabPageResource = null;
	// private MediaPersonInfo mediaPersonInfo = null;
	SinaVersionChangeUtil sinaVersionChangeUtil = new SinaVersionChangeUtil();
	private PersonInfo person = null;
	private UrlPojo urlPojo;
	private String sendUrl;
	private String produceURL = null;
	private String oid;

	@Autowired
	private DateUtil dateUtil = new DateUtil();

	@Autowired
	private MyJson myJSON = new MyJson();
	private String ajax_mess_url = null;
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

	String content = null;
	int page = 1;
	String beginRegex = null;
	String endRegex = null;
	AnsjPaser ansjUL = null;
	String str_UL = null;
	AnsjPaser ansjLI = null;
	AnsjPaser ansjContent = null;
	AnsjPaser ansjPubTime = null;
	AnsjPaser ansjSayUrl = null;
	AnsjPaser ansjFrom = null;
	AnsjPaser ansjMid = null;
	boolean flag = false;
	int repeat_count = 0;
	DocInfo doc = null;
	String c1 = null;
	String sayUrl = null;
	String date_str = null;
	String article = null;
	String mid = null;

	public void init(PersonInfo person, GrabPageResource grabPageResource,
			UrlPojo urlPojo, UrlsManager urlManager) {
		this.person = person;
		this.grabPageResource = grabPageResource;
		this.urlPojo = urlPojo;
		this.sendUrl = MyStringUtils.getUrlRemoveSlash(urlPojo.getUrl());
		produceURL = sinaVersionChangeUtil.getMediaUrl(sendUrl);
		this.urlManager = urlManager;
	}

	public void init(GrabPageResource grabPageResource) {
		this.grabPageResource = grabPageResource;
	}

	public void init(GrabPageResource grabPageResource, PersonInfo person) {
		this.grabPageResource = grabPageResource;
		this.person = person;
	}

	// url管理器,用来将抓取到的url放入相应的任务对象
	private UrlsManager urlManager;

	public void init(GrabPageResource grabPageResource, PersonInfo person,
			String sendURL, UrlsManager urlManager) {
		this.grabPageResource = grabPageResource;
		this.person = person;
		this.sendUrl = sendURL;
		produceURL = sinaVersionChangeUtil.getMediaUrl(sendUrl);
		this.urlManager = urlManager;
	}

	public void grabArticleContents(UrlPojo urlPojo) throws Exception {
		page = 1;
		this.oid = person.getUid();
		while (page <= 50) {
			current_URL = this.produceURL;
			// logger.info("媒体版---正在抓取" + this.current_URL + "的第" + page + "页");
			System.out.println("媒体版---正在抓取" + this.current_URL + "的第" + page
					+ "页");
			if (page == 1) {
				if (!grabOnePageArticle(current_URL, false)) {
					return;
				}
			} else {
				current_URL = StaticValue.Media_Wb_Content_Page_Next_URL
						.replace("${pageNum}", "" + page).replace("${oid}",
								this.oid).replace("${rnd}",
								RandomOperator.getRandom());
				if (!grabOnePageArticle(current_URL, true)) {// 代表要referer加入至header
					return;
				}
			}
			page++;
		}
		// logger.info("媒体版---"+current_URL+"抓取结束");
		System.out.println("媒体版---" + current_URL + "抓取结束");
	}

	public boolean grabOnePageArticle(String url, boolean referer)
			throws Exception {
		if (referer) {
			content = grabPageResource.getPageSourceOfSina(url,
					StaticValue.Referer_Media);
			myJSON.createJson(content);
			content = myJSON.getStringByKey("data");
		} else {
			content = grabPageResource.getPageSourceOfSina(url, null);
		}
		if (content == null) {
			return false;
		}
		if (!saveOnePageContent(content)) {
			return false;
		}
		return true;
	}
	private HtmlParserUtil htmlParserUtil=new HtmlParserUtil();
	public String getJsonNumber(String targetUrl) {
		int error_repeat_times = 0;
        String jsonNumberStr=null;
		while (error_repeat_times < StaticValue.url_repeat_count) {
			try {
				jsonNumberStr = grabPageResource.getPageSourceOfSina(targetUrl,
						StaticValue.Referer_Media);
				
				/**
				 * 对是否是出现网络请求繁忙页做测试，如果出现则直接进行帐户切换
				 */
				String title=htmlParserUtil.getTitleByLine(jsonNumberStr);
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
		
		return jsonNumberStr;
	}

	public boolean saveOnePageContent(String content) throws IOException,
			ParseException, Exception {
		if (StringUtils.isBlank(content)) {
			return false;
		}
		// 媒体版--最外层大模块抽取
		beginRegex = "id=\"feed_list\">";
		endRegex = "</ul>";
		ansjUL = new AnsjPaser(beginRegex, endRegex, content,
				AnsjPaser.TEXTEGEXANDNRT);
		str_UL = ansjUL.getText();
		// logger.info("得到ansjHtml---" + str_UL);

		// 每个li的抽取
		beginRegex = "<li";
		endRegex = "</li>";

		ansjLI = new AnsjPaser(beginRegex, endRegex, str_UL,
				AnsjPaser.TEXTEGEXANDNRT);

		// 正文抽取
		beginRegex = "type=\"[\\d]\">";
		endRegex = "</p>";
		ansjContent = new AnsjPaser(beginRegex, endRegex,
				AnsjPaser.TEXTEGEXANDNRT);

		// 时间抽取
		beginRegex = "date=\"";
		endRegex = "\"";
		ansjPubTime = new AnsjPaser(beginRegex, endRegex,
				AnsjPaser.TEXTEGEXANDNRT);

		/**
		 *每条微言的url地址抽取
		 */
		beginRegex = "<cite>" + AnsjPaser.TEXTEGEXANDNRT + "<a href=\"";
		endRegex = "\"";
		ansjSayUrl = new AnsjPaser(beginRegex, endRegex,
				AnsjPaser.TEXTEGEXANDNRT);

		// 来源抽取
		beginRegex = "rel=\"nofollow\">";
		endRegex = "</";
		ansjFrom = new AnsjPaser(beginRegex, endRegex, AnsjPaser.TEXTEGEXANDNRT);
		beginRegex = "mid=\\\"";
		endRegex = "\\\"";
		ansjMid = new AnsjPaser(beginRegex, endRegex, AnsjPaser.TEXTEGEXANDNRT);
		flag = false;
		// 开始抽取
		repeat_count = 0;// 记录当前页的内容当中，有几条是重复的,当达指定的每页最大重复数时，即为终止
		while (ansjLI.hasNext()) {
			doc = new DocInfo();
			c1 = ansjLI.getNext();
			/**
			 * 具体取到每条微言的url
			 */
			sayUrl = ansjSayUrl.reset(c1).getText();
			if (sayUrl == null || sayUrl.trim().length() == 0) {
				logger.info("微言的url为空直接返回false,不做保存!");
				return false;
			}
			// doc.setDocUrl(FormatOutput.urlPrefix + sayUrl);
			doc.setDocUrl(sayUrl);
			
			//加入评论部分
			if(StaticValue.doc_url_add_comment_tovisit){
				UrlPojo urlPojo=new UrlPojo(doc.getDocUrl(),102);
			    this.urlManager.addCommentUrlToFirst(urlPojo);
			}
			
			date_str = ansjPubTime.reset(c1).getText();
			// System.out.println(new Date(Long.parseLong(sg))+"------------");
			try {
				doc.setPublishtime(dateUtil.getDateByString(date_str));
			} catch (Exception e) {
				logger
						.info(doc.getDocUrl()
								+ " 该docURL的publishTime抽取出现问题，将跑过!");
				continue;
			}
			doc.setOrigin(ansjFrom.reset(c1).getText());
			article = ansjContent.reset(c1).getText().trim();
			doc.setArticle(article);
			doc.setInsertTime(new Date());

			/**
			 * 每条微言做ajax请求，获取该微言的数量信息,因此先请求到mid
			 */
			mid = ansjMid.reset(c1).getText();
			if (mid != null && mid.length() > 0) {
				doc.setDocArticleId(mid);
			} else {
				doc.setDocArticleId("0");
			}
			/**
			 * 替代以上取得各个帐户中的数量信息,用ajax请求方式
			 */
			ajax_mess_url = StaticValue.Media_Wb_Content_Num_URL.replace(
					"${mid}", mid).replace("${oid}", person.getUid()).replace(
					"${rnd}", RandomOperator.getRandom());
			// logger.info("ajax_mess_url--" + ajax_mess_url);
			myJSON.createJson(this.getJsonNumber(ajax_mess_url));
			myJSON.createJson(myJSON.getStringByKey("data"));
			myJSON.createJson(myJSON.getStringByKey(mid));
			// String temp_number = ansjRepeat.reset(c1).getText().trim();
			doc.setTransmit(myJSON.getIntegerByKey("rttCount"));
			doc.setPersonId(person.getUid());// ************************

			doc.setDiscuss(myJSON.getIntegerByKey("commtCount"));

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

			// 设置新加的person_id_long
			doc.setPerson_id_long(Long.parseLong(doc.getPersonId()));// *****************8
			// logger.info("doc---" + doc);
			System.out.println("media doc save successful");
			if (!docInfoManager.saveOrUpdate(doc)) {// 此时的没有保存成功意为数据重复了
				if ((++repeat_count) >= StaticValue.max_content_page_repeat_items) {
					// 此时即定为重复页，不再抓取
					logger.info("重复已达到最大，该url的内容抓取结束!");
					return false;
				}
			}
		}
		repeat_count = 0;
		return true;
	}
}
