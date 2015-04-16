package com.weibo.sina.cookie.person;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mbm.elint.entity.PersonAttention;
import com.mbm.elint.entity.util.UrlPojo;
import com.mbm.elint.manager.business.spider.UrlsManager;
import com.mbm.elint.manager.database.PersonAttentionManager;
import com.weibo.common.controler.SpiderControler;
import com.weibo.common.utils.AnsjPaser;
import com.weibo.common.utils.FormatOutput;
import com.weibo.common.utils.StaticValue;
import com.weibo.sina.cookie.common.GrabPageResource;
import com.weibo.utils.sina.MyStringUtils;

@Component
@Scope(value = "prototype")
public class GrabAttentionInfo {
	public static Logger logger = Logger.getLogger(GrabAttentionInfo.class);
	private GrabPageResource grabPageSource = null;
	private UrlPojo urlPojo;
	private String url;
	private UrlsManager urlsManager;
	@Autowired
	private PersonAttention personAttention = new PersonAttention();
	@Autowired
	private PersonAttentionManager personAttentionManager;

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

	String content = null;
	int i = 1;
	String[] attention_array = null;
	public String beginRegex = "(uid=){1}";
	public String textRegex = "(\\d){1,20}";
	public String endRegex = "(&fnick)\\S*sex";
	public AnsjPaser ansjParser = null;
	private String uid = null;

	public void savePersonAttentionInfo() throws Exception {
		if (StringUtils.isBlank(url)) {
			logger.info("---url不可为空");
			return;
		}
		i = 1;
		while (i <= StaticValue.grab_attention_max_pageNum) {
			/**
			 * 抓取关注时候，只认识uid,不认识个性domain
			 */
			temp_attention_url = FormatOutput.urlPrefix + this.uid
					+ "/follow?page=" + i;
			// logger.info("正在抓取的url---------------" + temp_attention_url);
			content = grabPageSource.getPageSourceOfSina(temp_attention_url,
					null);
			if (content != null) {
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
					temp_attention_list = parsePageResoure(content);
				} else {
					// 为了省变量，此时用它来暂存下某页的attention list
					temp_attention_url = parsePageResoure(content);
					if (temp_attention_list.contains(temp_attention_url
							.split("#")[0])) {
						logger.info("关注列表已抓到头了，共抓取了" + (i - 1) + "页");
						break;
					} else {
						temp_attention_list += ("#" + temp_attention_url);
					}
				}
				// System.out.println("temp_attention_list----------------------"
				// + temp_attention_list);
				i++;
			}
			// 抓取一页后休息一小下
			Thread.sleep(StaticValue.grab_attention_sleep_interval_time);
		}
		this.personAttention = new PersonAttention();
		this.personAttention.setUid(uid);
		this.personAttention.setAttentionUrl(this.url);
		personAttention.setAttentionIdList(temp_attention_list);
		attention_array = temp_attention_list.split("#");
		
		logger.info("temp_attention_list list length---" + temp_attention_list);
		logger.info("attention list length---" + attention_array.length);
		if(true){
			return;
		}
		if (temp_attention_list.trim().length() == 0) {
			personAttention.setAttentionSize(0);
		} else {
			personAttention.setAttentionSize(attention_array.length);
			logger.info(url + "关注列表抓取完毕,抓取到的attention_list的大小是--------"
					+ attention_array.length);
			logger.info("即将抓取" + url + "关注列表的微博用的个人信息");
			this.urlsManager.addUrlsArray(attention_array, this.url);
		}
		if (this.personAttentionManager.saveOrUpdateAttention(personAttention)) {
			logger.info(this.url + "-----保存关注列表信息成功");
		} else {
			logger.info(url + "的关注列表保存出现问题，无法抓取关注用户的个人信息了!");
		}
	}

	public String parsePageResoure(String content) {
		ansjParser = new AnsjPaser(beginRegex, textRegex, endRegex, content,
				"zel");
		return ansjParser.getTextList();
	}

	public void init(String uid, GrabPageResource grabPageResource,
			UrlPojo urlPojo, UrlsManager urlsManager) {
		this.uid = uid;
		this.grabPageSource = grabPageResource;
		this.urlPojo = urlPojo;
		this.url = MyStringUtils.getUrlRemoveSlash(urlPojo.getUrl());
		this.urlsManager = urlsManager;
	}

	public void init(String uid, GrabPageResource grabPageResource,
			UrlPojo urlPojo) {
		this.uid = uid;
		this.grabPageSource = grabPageResource;
		this.urlPojo = urlPojo;
		this.url = MyStringUtils.getUrlRemoveSlash(urlPojo.getUrl());
	}

}
