package com.weibo.sina.cookie.keywords;

import java.util.Date;

import net.sf.json.JSONObject;

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
import com.weibo.common.GUI.verify.VerifyGuiUtil;
import com.weibo.common.GUI.verify.iface.IVerifyElement;
import com.weibo.common.utils.AnsjPaser;
import com.weibo.common.utils.MyHttpConnectionManager;
import com.weibo.common.utils.StaticValue;
import com.weibo.sina.cookie.common.GrabPageResource;
import com.weibo.utils.sina.MyStringUtils;

//抓取搜索关键词之后的结果信息
@Component
@Scope(value = "prototype")
public class GrabSearchInfo implements IVerifyElement {
	public static Logger logger = Logger.getLogger(GrabSearchInfo.class);
	// private GrabPageResource grabPageResource = new GrabPageResource();
	private GrabPageResource grabPageResource;
	// 关键词抓取时的验证码的图片的下标值，主要是为了区分不同线程间的图片的不一致
	private int verify_keyword_pic_index;

	public int getVerify_keyword_pic_index() {
		return verify_keyword_pic_index;
	}

	public void setVerify_keyword_pic_index(int verifyKeywordPicIndex) {
		verify_keyword_pic_index = verifyKeywordPicIndex;
	}

	public String current_main_thread_name = Thread.currentThread().getName();

	// 验证码参数---结束

	public String getCurrent_main_thread_name() {
		return current_main_thread_name;
	}

	// 验证码输入类
	private VerifyGuiUtil verifyGuiUtil = new VerifyGuiUtil();

	long time = 0;
	long tempId = 0L;
	long docUpdateTime = 0;
	@Autowired
	private SinaKeySearchResultManager sinaKeySearchResultManager;
	@Autowired
	private SinaKeySearchInfoManager sinaKeySearchInfoManager;
	private UrlsManager urlManager;

	public void init(GrabPageResource grabPageResource, UrlsManager urlManager) {
		this.grabPageResource = grabPageResource;
		this.urlManager = urlManager;
	}

	// 为测试用
	public void init(GrabPageResource grabPageResource) {
		this.grabPageResource = grabPageResource;
	}

	String beginRegex = null;
	String endRegex = null;
	AnsjPaser ansjHtml = null;
	String html = null;
	AnsjPaser ansjSearch = null;
	String dempSearch = null;
	AnsjPaser ansjInfo = null;
	AnsjPaser ansjName = null;
	AnsjPaser ansjId = null;
	AnsjPaser ansjText = null;
	AnsjPaser ansjRepeat = null;
	AnsjPaser ansjComment = null;
	AnsjPaser ansjFrom = null;
	AnsjPaser ansjPubTime = null;
	AnsjPaser ansjDocUrl = null;
	AnsjPaser ansjMid = null;
	int repeat_count = 0;
	String c1 = null;
	DocInfo docResult = null;
	String sayUrl = null;
	String ids = null;
	String name = null;
	String dempTime = null;
	Date date = null;
	String dempText = null;
	String dempRepeat = null;
	String dempComment = null;
	String dempFrom = null;
	String mid = null;
	private String searchKey;
	private int itemNumber;
	private boolean isNormal = false;
	String temp_url = null;
	int pageCount = 1;
	String content = null;
	String word = null;
	String searchURL = null;
	SinaKeySearchInfo sinaKeySearchInfo = null;

	public boolean grabSearchOnePageInfo(String keyword, String searchURL)
			throws Exception {
		logger.info("正在抓取的搜索结果是------" + keyword + "--" + searchURL);
		try {
			content = this.grabPageResource
					.getPageSourceOfSina(searchURL, null);
			// logger.info("content----" + content);
			if (getSearchResultPage(keyword, content)) {
				return true;
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(keyword + "--" + searchURL + "抓取出现了问题,即将跳过该searchURL");
		}
		return false;
	}

	public boolean grabKeySearchInfo(KeywordPojo keyword) throws Exception {
		itemNumber = 0;
		isNormal = false;
		pageCount = 1;
		word = keyword.getKeyWord();
		searchURL = MyStringUtils.getUrlByKeyWord(word);
		temp_url = searchURL;
		this.searchKey = word;// 此变量主要用于在doc中保存其关键词
		logger.info("正在抓取的关键词是------" + word);
		while (grabSearchOnePageInfo(word, searchURL)) {
			pageCount += 1;
			searchURL = temp_url + "&page=" + (pageCount);
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
		sinaKeySearchInfo = new SinaKeySearchInfo();
		sinaKeySearchInfo.setKeyword(word);
		sinaKeySearchInfo.setUpdateTime(new Date());
		sinaKeySearchInfo.setType("sina");
		
		sinaKeySearchInfo.setComment("" + this.itemNumber);

		// System.out.println("outer one doc--" + sinaKeySearchInfo);
		this.sinaKeySearchInfoManager.saveKeySearchInfo(sinaKeySearchInfo);
		System.out.println("search keyword meta --" + sinaKeySearchInfo);

		return true;
	}

	public boolean getSearchResultPage(String keyword, String str) {
		/**
		 * 先验证有无验证码
		 */
		// 试抽取是否有验证码区域
		beginRegex = "pid\":\"pl_common_sassfilter\",";
		endRegex = "\\)</script>";
		ansjHtml = new AnsjPaser(beginRegex, endRegex, str,
				AnsjPaser.TEXTEGEXANDNRT);
		html = ansjHtml.getText();
		if (html != null && html.trim().length() > 0) {
			html = (String) JSONObject.fromObject("{" + html).get("html");
			// System.out.println("ansjHtml-------------------------" + html);
			if (verifyGuiUtil.needVerify(html)) {
				logger.info("搜索结果页---出现验证码");
				beginRegex = "&ts=";
				endRegex = "\"";
				ansjHtml = new AnsjPaser(beginRegex, endRegex, html,
						AnsjPaser.TEXTEGEXANDNRT);
				String timeStamp = ansjHtml.getText();
				System.out.println("timeStamp---------" + timeStamp);
				if (this.grabPageResource.isValidVerifyCode(timeStamp,
						searchURL, verifyGuiUtil, this)) {
					logger.info("验证已通过,将进入下一页抓取!");
					return true;
				} else {
					logger.info("最终没有通过验证!");
					return false;
				}
				// 解决完验证码后，当前页不抓了，返回true，直接进入下一页的抓取
			}
		}
		// 模块抽取,得到页页含有博文部分的json数据
		beginRegex = "pid\":\"pl_weibo_direct\",";
		endRegex = "\\)</script>";
		ansjHtml = new AnsjPaser(beginRegex, endRegex, str,
				AnsjPaser.TEXTEGEXANDNRT);
		html = ansjHtml.getText();
		// System.out.println("html---" + html);
		html = (String) JSONObject.fromObject("{" + html).get("html");
		// System.out.println("ansjHtml-------------------------" + html);

		// 验证是否有符合条件的结果
		beginRegex = "<p class=\"noresult_tit\">";
		endRegex = "未找到";
		ansjSearch = new AnsjPaser(beginRegex, endRegex, html,
				AnsjPaser.zel_all_chars);
		dempSearch = ansjSearch.getSimpleText();
		// System.out.println("dempSearch--------" + dempSearch);
		if (dempSearch != null)
			if (dempSearch.contains("抱歉")) {
				logger.info("关键词--" + keyword
						+ "--的抓取到尾页了_++++++++++++++++++++++++++++++");
				isNormal = true;
				return false;
			}

		beginRegex = "<p class=\"noresult_tit\">";
		endRegex = "相关法律";
		ansjSearch = new AnsjPaser(beginRegex, endRegex, html,
				AnsjPaser.zel_all_chars);
		dempSearch = ansjSearch.getSimpleText();
		if (dempSearch != null)
			if (dempSearch.contains("根据")) {
				isNormal = true;
				logger.info("关键词--" + keyword + "--出现不允许搜索的关键词，将跳过");
				return false;
			}
		beginRegex = "<div class=\"WB_cardwrap S_bg2 clearfix";
		endRegex = "<div node-type=\"feed_list_repeat\"";
		ansjInfo = new AnsjPaser(beginRegex, endRegex, html,
				AnsjPaser.TEXTEGEXANDNRT);
		// 姓名抽取
		beginRegex = "<div class=\"face\">[\\s\\S]*?title=\"";
		endRegex = "\"";
		ansjName = new AnsjPaser(beginRegex, endRegex);
		// ID抽取
		beginRegex = "<div class=\"feed_content wbcon\"[\\s\\S]*?usercard=\"id=";
		endRegex = "&usercardkey";

		ansjId = new AnsjPaser(beginRegex, endRegex);
		// 正文抽取
		beginRegex = "<p class=\"comment_txt\"[\\s\\S]*?>";
		endRegex = "</p>";
		ansjText = new AnsjPaser(beginRegex, endRegex, AnsjPaser.TEXTEGEXANDNRT);

		// 转发抽取
		// beginRegex =
		// "<a href=\"javascript:void\\(0\\);\" action-type=\"feed_list_forward\"[\\s\\S]*?>转发";
		beginRegex = "<span class=\"line S_line1\"[\\s\\S]*?转发</span>[\\s\\S]*?<em>";
		endRegex = "</em>";
		ansjRepeat = new AnsjPaser(beginRegex, endRegex);
		// 评论抽取
		// beginRegex =
		// "<a href=\"javascript:void\\(0\\);\" action-type=\"feed_list_comment\"[\\s\\S]*?>评论";
		beginRegex = "<span class=\"line S_line1\"[\\s\\S]*?评论</span>[\\s]*?<em>";
		endRegex = "</em>";
		ansjComment = new AnsjPaser(beginRegex, endRegex);
		// 客户端抽取
		beginRegex = "<div class=\"feed_from W_textb\"[\\s\\S]*?来自\\s*<a[\\s\\S]*?>";
		endRegex = "</a>";
		ansjFrom = new AnsjPaser(beginRegex, endRegex);

		// 时间抽取
		beginRegex = "<div class=\"feed_from W_textb\"[\\s\\S]*?<a[\\s\\S]*?date=\"";
		endRegex = "\"";
		ansjPubTime = new AnsjPaser(beginRegex, endRegex);
		/**
		 * 文章的docUrl抽取
		 */
		// beginRegex =
		// "class=\"info W_linkb W_textb\"[\\s\\S]*/span>[\\s\\S]*href=\"";
		beginRegex = "<div class=\"feed_from W_textb\"[\\s\\S]*?href=\"";
		// endRegex = "\"\\s+title=";
		endRegex = "\"";
		ansjDocUrl = new AnsjPaser(beginRegex, endRegex);

		// mid抽取
		beginRegex = "<div mid=\"";
		endRegex = "\"";
		ansjMid = new AnsjPaser(beginRegex, endRegex);
		repeat_count = 0;
		while (ansjInfo.hasNext()) {
			c1 = ansjInfo.getNext();
			// System.out.println("item content-------------------------" + c1);
			docResult = new DocInfo();

			sayUrl = ansjDocUrl.reset(c1).getText();
			if (sayUrl == null || sayUrl.trim().length() == 0) {
				logger.info("微言的url为空直接返回false,不做保存!");
				return false;
			}
			docResult.setDocUrl(sayUrl);
			// docResult.setKeyword(searchKey);
			docResult.setSendUrl(searchKey);
			// System.out.println("c1-----------------"+c1);
			ids = ansjId.reset(c1).getText();
			docResult.setPersonId(ids);
			if (ids != null && ids.length() != 0) {
				// 将抓取到的uid结果，是否加入到待抓取结果
				if (StaticValue.is_add_keyword_crawl_result_todo_task) {
					this.urlManager.addToVisitByUidFromKeySearch(ids);
				}
			}

			// System.out.println("html---"+html);
			name = ansjName.reset(c1).getText();// 姓名
			// System.out.println("name--------"+name);
			docResult.setAuthor(name);// 文章作者
			dempTime = ansjPubTime.reset(c1).getText();
			date = new Date(Long.parseLong(dempTime));
			// 设置publishTime
			docResult.setPublishtime(date);
			docResult.setInsertTime(new Date());
			time = docResult.getPublishtime().getTime();
			try{
				tempId = Long.parseLong(ids);	
			}catch(Exception e){
				e.printStackTrace();
				continue;
			}
			
			// sinaSearchConfig.setUpdateTime(date);
			dempText = ansjText.reset(c1).getText();// 正文
			// dempText = dempText.replaceAll("<.*?>", "");//不再过滤标签
			/**
			 * 在这里做对元搜索抓取的关键词标红处理
			 */
			// <span style=\"color:red;\">123<\/span>
			if (dempText != null) {
				dempText = dempText.trim();
				dempText = dempText.replaceAll("<em class=\"red\">.*?</em>",
						searchKey);
			}
			docResult.setArticle(dempText);
			dempRepeat = ansjRepeat.reset(c1).getText();// 转发次数
			if (dempRepeat != null && dempRepeat.length() != 0) {
				// dempRepeat = dempRepeat.replaceAll("\\(|\\)", "");
				dempRepeat = (dempRepeat = dempRepeat.trim() == "" ? "0"
						: dempRepeat);
				// System.out.println("转发:" + dempRepeat);
				docResult.setTransmit(Integer.parseInt(dempRepeat));
			} else {
				docResult.setTransmit(0);
			}
			dempComment = ansjComment.reset(c1).getText();// 评论次数
			if (dempComment != null && dempComment.length() != 0) {
				// dempComment = dempComment.replaceAll("\\(|\\)", "");
				dempComment = (dempComment = dempComment.trim() == "" ? "0"
						: dempComment);
				docResult.setDiscuss(Integer.parseInt(dempComment));
			} else {
				// System.out.println("discuss----------");
				docResult.setDiscuss(0);// 代表无评论
			}
			dempFrom = ansjFrom.reset(c1).getText();// 来自
			mid = ansjMid.reset(c1).getText();
			docResult.setDocArticleId(mid);
			// System.out.println("来自:" + dempFrom);
			docResult.setOrigin(dempFrom);
			docResult.setWeiboType(1);

			docResult.setGrab_method("cookie");
			// 设置新加的person_id_long
			docResult
					.setPerson_id_long(Long.parseLong(docResult.getPersonId()));

			if (this.sinaKeySearchResultManager.saveSearchResultInfo(docResult)) {
				this.itemNumber++;
			} else {
				if ((++repeat_count) >= StaticValue.max_content_page_repeat_items) {
					logger.info("重复已达到最大，该searchUrl的抓取结束!");
					return false;// 抓取单页的重复数已达到最大值，该url抓取到此为止
				}
			}
			logger.info("search doc---" + docResult);
			// System.out.println("search doc save successful");
		}
		repeat_count = 0;
		return true;
	}

	public static void main(String[] args) throws Exception {
		String keyword = "军演";
		KeywordPojo pojo = new KeywordPojo(keyword, 0);
		// String source = IOUtil.readFile("d://test.txt");
		// GrabSearchInfo grabSearchInfo = new GrabSearchInfo();
		// grabSearchInfo.getSearchResultPage4Debug("123", source);
		// grabSearchInfo.getSearchResultPage("123", source);
		// String
		// str="\u7cfb\u7edf\u7e41\u5fd9\uff0c\u8bf7\u7a0d\u5019\u518d\u8bd5\u5427\u3002";
		// System.out.println(str);

		GrabPageResource grabPageSource = new GrabPageResource();
		grabPageSource.setHttpClient(MyHttpConnectionManager.getHttpClient());

		String cookieString = "SUS=SID-3057467163-1416743059-GZ-5tmgw-c4eb2ef9ad5b0fd10a817b6498b92d41; path=/; domain=.weibo.com;SUS=SID-3057467163-1416743059-GZ-5tmgw-c4eb2ef9ad5b0fd10a817b6498b92d41; path=/; domain=.weibo.com; httponly;SUE=es%3D4179116543cc383f8e82d60208681845%26ev%3Dv1%26es2%3D15637ddc1e00b5a1859c523b661429c8%26rs0%3D16Sv5Qz4J1kMQjrlcZoqAIwpaxe5iZ0aNixK8BYW3zWUrikxxV%252FSGWhiy1zcbW7ETzs7TmVYnapL3x25Awu6Gg1LAkXIrYeVSTsNv6%252BMvrk8SCRm%252BmJw10RlU0yGwIAhBKaG2tP312t%252F2gkJ%252FNlM9MH7ggS8rGGQB0l11yz2zjI%253D%26rv%3D0;path=/;domain=.weibo.com;Httponly;SUP=cv%3D1%26bt%3D1416743059%26et%3D1416829459%26d%3Dc909%26i%3D2d41%26us%3D1%26vf%3D0%26vt%3D0%26ac%3D2%26st%3D0%26uid%3D3057467163%26name%3Dda04xiang%2540126.com%26nick%3Dda04xiang%26fmp%3D%26lcp%3D;path=/;domain=.weibo.com;SUB=_2AkMjLUeka8NlrABRnf8Rymzjao5H-jyQ_Y1SAn7uJhIyHRgf7kceqSU-maVOaPDIm9Hk91h8S7dWFndW8Q..; path=/; domain=.weibo.com; httponly;SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WWwqiSB0x6ks32W94_jMKZl5JpX5K2t; expires=Monday, 23-Nov-15 11:44:19 GMT; path=/; domain=.weibo.com;SRT=E.vAfqJD43iDR3JORqvebuJXmBvXvCvXM4VV8OvnEXBvzvvv4mT8a2hnVmPGMh!nfCvvAivOmKvAmLvAmMvXvCmXmC*B.vAflW-P9Rc0lR-ykADvnJqiQVbiRVPBtS!r3JZPQVqbgVdWiMZ4siOzu4DbmKPWQW3E94rzn5DShVqJpPq!rQ-9iNs9ji49ndDPIJeA7; expires=Wednesday, 20-Nov-24 11:44:19 GMT; path=/; domain=.passport.weibo.com; httponly;SRF=1416743059; expires=Wednesday, 20-Nov-24 11:44:19 GMT; path=/; domain=.passport.weibo.com;ALF=1448279059; expires=Mon, 23-Nov-2015 11:44:19 GMT; path=/; domain=.weibo.com;SSOLoginState=1416743059; path=/; domain=.weibo.com;myuid=3057467163;SinaRot_wb_r_topic=39;UV5PAGE=usr511_179;; UV5=usr319_182;;SSOLoginState=1416743059;;UOR=,,login.sina.com.cn;;_s_tentry=login.sina.com.cn;";

		// String cookieString =
		// "SWB=usrmd1368; WBStore=2f280b0f46628758|undefined; WBtopGlobal_register_version=5dda576219ed64a2; un=da04xiang@126.com; UV5=usrmdins312_210; _s_tentry=-; Apache=1591319549908.3916.1396425689467; SINAGLOBAL=1591319549908.3916.1396425689467; ULV=1396425689587:1:1:1:1591319549908.3916.1396425689467:; v5reg=usr1024; appkey=; UV5PAGE=usr511_177; UUG=usr1476; SUS=SID-3057467163-1396425723-XD-xz4tq-8cb053991d0a5fe39e49c72b4774df70; SUE=es%3Dbc85b657a1ca038d28e15dabd740e02c%26ev%3Dv1%26es2%3D6fce478a50894d3014b2f77e98c7dfbc%26rs0%3DCFD%252FtEDhqpCse%252BkSIqol6W3t%252FY4eXXp59FLGqSw0xfG%252FLmGVz9%252FmC0vd0L7ZMHDq9DnRb9agpTEKA2FM48ccI2id4wHyJPKAFf3ygT4bb0cLaPCQdwyta9Jie3ZZr9vW0H9h8ORdEd2dt57Nz6s8hfkNdGhsNtUr7NWX%252FAQdik8%253D%26rv%3D0; SUP=cv%3D1%26bt%3D1396425723%26et%3D1396512123%26d%3Dc909%26i%3D17c4%26us%3D1%26vf%3D0%26vt%3D0%26ac%3D%26st%3D0%26uid%3D3057467163%26name%3Dda04xiang%2540126.com%26nick%3Dda04xiang%26fmp%3D%26lcp%3D; SUB=Aap_qFwQIxJwMGuNLPQFZw473XXuK_tuzEn_4hXaDpngrEX83C5osTv-8Vt3BHg7dgMbKjWFd48hhNRtlP_zOyrWGpkXyeqRoAhItbVv95BXPWHSSZIAaOGB1i89HacI9w%3D%3D; SUBP=002A2c-gVlwEm1uAWxfgXELuuu1xVxBxAAdg3c0olkQWP1AxizfE_jU; ALF=1399017721; SSOLoginState=1396425723";
		grabPageSource.setCookieString(cookieString);

		GrabSearchInfo grabSearchInfo = new GrabSearchInfo();
		grabSearchInfo.init(grabPageSource, null);

		grabSearchInfo.grabKeySearchInfo(pojo);

	}
}