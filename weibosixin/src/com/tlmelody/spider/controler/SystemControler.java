package com.tlmelody.spider.controler;

import java.util.HashMap;
import java.util.Map;

import com.tianliang.spider.crawler.httpclient.Crawl4HttpClient;
import com.tianliang.spider.pojos.HttpRequestPojo;
import com.tianliang.spider.pojos.enumeration.CharsetEnum;
import com.tianliang.spider.pojos.enumeration.HttpRequestMethod;

/**
 * 系统运行的控制器类
 * 
 * @author zel
 */
public class SystemControler {
	public static String PushSiXin(String username,String pass,String uid,String text) throws Exception {
		HttpRequestPojo requestPojo = new HttpRequestPojo();
		requestPojo.setRequestMethod(HttpRequestMethod.POST);
//		String username="****";
//		String pass="****";
//		String uid="****";
		String cookie_manual =com.melody.sixin.MelodySiXin.getMyCookie(username, pass, uid) ;
		
		Map<String, Object> headerMap = new HashMap<String, Object>();
		Map<String, String> formNameValueMap = new HashMap<String, String>();
		String url = "http://www.weibo.com/aj/message/add?ajwvr=6&__rnd=1428660615636";
		requestPojo.setUrl(url);
		
		headerMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		headerMap.put("Accept-Encoding", "gzip, deflate");
		headerMap.put("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		headerMap.put("Cache-Control", "no-cache");
		headerMap.put("Connection", "keep-alive");
		headerMap.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		headerMap.put("Host", "www.weibo.com");
		headerMap.put("Pragma", "no-cache");
		headerMap.put("Referer", "http://www.weibo.com/message/history?uid=3771336795&name=melody12ab");
		headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0");
		headerMap.put("X-Requested-With", "XMLHttpRequest");
		headerMap.put("Cookie", cookie_manual);
		
		
		
		//表单数据
		formNameValueMap.put("fids", "");
		formNameValueMap.put("location", "msgdialog");
		formNameValueMap.put("module", "msgissue");
		formNameValueMap.put("style_id", "1");
//		formNameValueMap.put("text", "你好啊，hello,123!!!");
		formNameValueMap.put("text", text);
		formNameValueMap.put("tovfids", "");
		formNameValueMap.put("uid", "3771336795");
		
		requestPojo.setHeaderMap(headerMap);
		requestPojo.setFormNameValePairMap(formNameValueMap, CharsetEnum.UTF8);

		String source = Crawl4HttpClient.crawlWebPage(requestPojo);
		String result=ResultParser.parser(source);
//		System.out.println(result);
		return result;
	}	
}
