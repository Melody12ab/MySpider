package com.skylight.test;

import java.util.HashMap;
import java.util.Map;

import com.tianliang.spider.crawler.httpclient.Crawl4HttpClient;
import com.tianliang.spider.pojos.HttpRequestPojo;
import com.tianliang.spider.pojos.enumeration.CharsetEnum;
import com.tianliang.spider.pojos.enumeration.HttpRequestMethod;

public class RandomTest {

	public static void main(String[] args) throws Exception {
		HttpRequestPojo requestPojo = new HttpRequestPojo();
		requestPojo.setRequestMethod(HttpRequestMethod.GET);

		 String url = "http://www.baidu.com/";
//		String url = "http://www.oscca.gov.cn/";

//		Map<String, Object> headerMap = new HashMap<String, Object>();
//		Map<String, Object> parasMap = new HashMap<String, Object>();
		Map<String, String> formNameValueMap = new HashMap<String, String>();
		
		requestPojo.setUrl(url);
		// requestPojo.setHeaderMap(headerMap);
		// requestPojo.setParasMap(parasMap);
		// form name value是为非iso-8859-1编码的value pair而添加
		requestPojo.setFormNameValePairMap(formNameValueMap, CharsetEnum.UTF8);

		String source = Crawl4HttpClient.crawlWebPage(requestPojo);

		System.out.println(source);

		System.out.println("done!");

	}
}
