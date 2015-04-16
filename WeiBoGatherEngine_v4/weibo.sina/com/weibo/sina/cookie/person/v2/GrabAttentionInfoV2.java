package com.weibo.sina.cookie.person.v2;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mbm.elint.entity.PersonAttention;
import com.mbm.elint.entity.util.MyJson;
import com.mbm.elint.entity.util.UrlPojo;
import com.mbm.elint.manager.business.spider.UrlsManager;
import com.mbm.elint.manager.database.PersonAttentionManager;
import com.mbm.util.IOUtil;
import com.mbm.util.StringOperatorUtil;
import com.vaolan.parser.VaoLanHtmlParser;
import com.vaolan.status.DataFormatStatus;
import com.weibo.common.controler.SpiderControler;
import com.weibo.common.utils.AnsjPaser;
import com.weibo.common.utils.FormatOutput;
import com.weibo.common.utils.StaticValue;
import com.weibo.sina.cookie.common.GrabPageResource;
import com.weibo.utils.sina.MyStringUtils;

@Component
@Scope(value = "prototype")
public class GrabAttentionInfoV2 {
	public static Logger logger = Logger.getLogger(GrabAttentionInfoV2.class);
	private GrabPageResource grabPageSource = null;
	private UrlPojo urlPojo;
	private String url;
	private UrlsManager urlsManager;
	@Autowired
	private PersonAttention personAttention;
	@Autowired
	private PersonAttentionManager personAttentionManager;
	@Autowired
	private MyJson myJSON = new MyJson();

	// jsoup 选择
	private List<String> selectorList = new LinkedList<String>();
	private List<String> selectorResultList = null;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public UrlPojo getUrlPojo() {
		return urlPojo;
	}

	public void setUrlPojo(UrlPojo urlPojo) {
		this.urlPojo = urlPojo;
	}

	String temp_attention_url = "";
	String temp_attention_list = "";
	// StringBuilder temp_attention_list=null;
	String relationInfo = "";

	String domId = null;

	AnsjPaser innerUlParser = new AnsjPaser("<ul class=\"follow_list\"",
			"</ul>", AnsjPaser.TEXTEGEXANDNRT);

	/**
	 * 得到每次成功保存后的uidListString
	 * 
	 * @return
	 * @throws Exception
	 */
	public String savePersonAttentionInfo() throws Exception {
		if (StringUtils.isBlank(url)) {
			logger.info("---url不可为空");
			return null;
		}
		String content = null;
		int i = 1;

		while (i <= StaticValue.grab_attention_max_pageNum) {
			System.out.println("正在抓取" + this.url + "关注第 " + i + " 页");
			/**
			 * 抓取关注时候，只认识uid,不认识个性domain
			 */
			if (i == 1) {
				temp_attention_url = StaticValue.attention_req_url_first
						.replace("${domain_id+uid}", this.domain_id + this.uid)
						.replace("${domain_id}", this.domain_id).replace(
								"${pageNo}", "" + i);
				// temp_attention_url = "http://weibo.com/p/" + this.domain_id
				// + this.uid + "/follow?from=page_" + this.domain_id
				// + "&wvr=5&mod=headfollow&ajaxpagelet=1&page=1";
				// content = grabPageSource.getPageSourceOfSina(
				// temp_attention_url, null);
			} else {
				temp_attention_url = StaticValue.attention_req_url.replace(
						"${domain_id+uid}", this.domain_id + this.uid).replace(
						"${domain_id+uid}", this.domain_id + this.uid).replace(
						"${pageNo}", "" + i).replace("${pids}", domId);
				// temp_attention_url=temp_attention_url+"&_t=FM_141967949531882";
				// content =
				// grabPageSource.getPageSourceOfSina(temp_attention_url,
				// "http://weibo.com/yaochen");
			}

			content = grabPageSource.getPageSourceOfSina(temp_attention_url,
					null);
			// logger.info("正在抓取的url---------------" + temp_attention_url);
			// content = grabPageSource.getPageSourceOfSina(temp_attention_url,
			// null);

			// logger.info("content---"+content);
			// IOUtil.writeFile("d:/test.txt", content);

			// 此处对json进行解析，再分析
			AnsjPaser relationBlock = new AnsjPaser(
					"\\{\"ns\":\"pl.content.followTab.index\",", "\\}\\)",
					content, AnsjPaser.TEXTEGEXANDNRT);
			relationInfo = relationBlock.getText();
			myJSON.createJson("{" + relationInfo + "}");

			// 如果i==1时，得到domId,为下边的请求做准备
			if (i == 1) {
				domId = myJSON.getStringByKey("domid");
			}

			relationInfo = myJSON.getStringByKey("html");// 解析json得到关注关系所在的数据块
			// logger.info("relationInfo---" + relationInfo);
			// IOUtil.writeFile("d:/test.txt", relationInfo);

			// 对relationInfo字段过滤，得到UID列表最近的ul标答值
			innerUlParser.reset(relationInfo);

			if (relationInfo != null) {
				if (i == 1) {
					/**
					 * 判断提交的网址是否正常，若抓取到的是内置的信息，认为提交的网址有错误,当然此时的url也要判断,
					 * 若非本人的url则返回提交的url错误
					 */
					// 当提交的关注url不存在时，抛出异常
					if (this.uid
							.equals(SpiderControler.isRunningAccountUid4Sina)
							&& !this.url.equals(FormatOutput.urlPrefix
									+ SpiderControler.isRunningAccountUid4Sina)) {
						throw new Exception();
					}
					// logger.info(relationInfo);
					// logger.info(innerUlParser.getText());
					selectorList.clear();
					selectorList.add("ul.follow_list");
					selectorResultList = VaoLanHtmlParser
							.getNodeContentBySelector(relationInfo,
									selectorList,
									DataFormatStatus.TagAllContent);
					if (!StringOperatorUtil
							.isBlankCollection(selectorResultList)) {
						// IOUtil
						// .writeFile("d:/test.txt", selectorResultList.get(0));
						temp_attention_list = parsePageResoure(selectorResultList
								.get(0));
					}
				} else {
					// 为了省变量，此时用它来暂存下某页的attention list
					selectorList.clear();
					selectorList.add("ul.follow_list");
					selectorResultList = VaoLanHtmlParser
							.getNodeContentBySelector(relationInfo,
									selectorList,
									DataFormatStatus.TagAllContent);
					if (!StringOperatorUtil
							.isBlankCollection(selectorResultList)) {
						// IOUtil
						// .writeFile("d:/test.txt", selectorResultList.get(0));
						temp_attention_url = parsePageResoure(selectorResultList
								.get(0));

						if (temp_attention_url != null) {
							if (temp_attention_list.contains(temp_attention_url
									.split("#")[0])) {
								logger.info("关注列表已抓到头了，共抓取了" + (i - 1) + "页");
								break;
							} else {
								temp_attention_list += ("#" + temp_attention_url);
							}
						}
					} else {
						// 没抓取到任何记录,认为不能抓或没有了!
						System.out.println(this.url + "关注的第 " + i + " 页"
								+ ",没有抓取到记录，认为该url关注抓取结束");
						break;
					}
				}
				// System.out.println("执行了一次!");
				System.out.println("完成抓取" + this.url + "关注第 " + i + " 页");
				i++;
				Thread.sleep(StaticValue.grab_attention_sleep_interval_time);
			}
			// System.out.println("attention list---" + temp_attention_list);
			// 抓取一页后休息一小下
			// Thread.sleep(StaticValue.grab_attention_sleep_interval_time);
		}
		this.personAttention = new PersonAttention();
		this.personAttention.setUid(uid);
		this.personAttention.setAttentionUrl(this.url);
		personAttention.setAttentionIdList(temp_attention_list);
		// logger.info("attention list --" + temp_attention_list);
		String[] attention_array = temp_attention_list.split("#");
		if (temp_attention_list.trim().length() == 0) {
			personAttention.setAttentionSize(0);
		} else {
			personAttention.setAttentionSize(attention_array.length);
			logger.info(url + "关注列表抓取完毕,抓取到的attention_list的大小是--------"
					+ attention_array.length);
			logger.info("即将抓取" + url + "关注列表的微博用的个人信息");
			// this.urlsManager.addUrlsArray(attention_array, this.url);// 暂注掉
		}
		if (this.personAttentionManager.saveOrUpdateAttention(personAttention)) {
			logger.info(this.url + "-----保存关注列表信息成功");
		} else {
			logger.info(url + "的关注列表保存出现问题，无法抓取关注用户的个人信息了!");
		}

		return temp_attention_list;
	}

	public static String beginRegex = "<img usercard=\"id=";
	public static String textRegex = "(\\d){1,20}";
	public static String endRegex = "\"" + AnsjPaser.TEXTEGEXANDNRT + ">";

	public static AnsjPaser ansjParser = null;

	/**
	 * 对于粉丝的uid列表的获取
	 * 
	 * @param content
	 * @return
	 */
	public String parsePageResoure(String content) {
		if (content == null) {
			return null;
		}
		ansjParser = new AnsjPaser(beginRegex, textRegex, endRegex, content,
				"zel");
		return ansjParser.getTextList();
	}

	private String uid;
	private String domain_id;// 保存每个博主的domain_id

	public String getDomain_id() {
		return domain_id;
	}

	public void setDomain_id(String domainId) {
		domain_id = domainId;
	}

	public void init(String domain_id, String uid,
			GrabPageResource grabPageResource, UrlPojo urlPojo,
			UrlsManager urlsManager) {
		this.domain_id = domain_id;
		this.uid = uid;
		this.grabPageSource = grabPageResource;
		this.urlPojo = urlPojo;
		this.url = MyStringUtils.getUrlRemoveSlash(urlPojo.getUrl());
		this.urlsManager = urlsManager;
	}

	// 用于测试的init方法
	public void init(String domain_id, String uid,
			GrabPageResource grabPageResource, UrlPojo urlPojo) {
		this.domain_id = domain_id;
		this.uid = uid;
		this.grabPageSource = grabPageResource;
		this.urlPojo = urlPojo;
		this.url = MyStringUtils.getUrlRemoveSlash(urlPojo.getUrl());
	}

}
