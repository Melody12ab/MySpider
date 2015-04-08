package com.skylight.test;

import java.util.HashMap;
import java.util.Map;

import com.tianliang.spider.crawler.httpclient.Crawl4HttpClient;
import com.tianliang.spider.pojos.HttpRequestPojo;
import com.tianliang.spider.pojos.enumeration.CharsetEnum;
import com.tianliang.spider.pojos.enumeration.HttpRequestMethod;
import com.tianliang.spider.utils.DateUtil;

public class RandomTest {

	public static void main(String[] args) throws Exception {
		HttpRequestPojo requestPojo = new HttpRequestPojo();
		requestPojo.setRequestMethod(HttpRequestMethod.GET);

		// String url = "http://www.baidu.com/";
		String url = "http://www.tianya.cn/";
		// String url =
		// "http://passport.tianya.cn/online/checkuseronline.jsp?callback=onlineTips&t="
		// + DateUtil.getLongByDate();

		Map<String, Object> headerMap = new HashMap<String, Object>();
		Map<String, Object> parasMap = new HashMap<String, Object>();
		Map<String, String> formNameValueMap = new HashMap<String, String>();

		String cookie = "sso=r=1304296118&sid=&wsid=AAF67DB29FBAB011F985722962A01B70;user=w=skylight2015&id=98311008&f=1;temp=k=369721810&s=&t=1425729029&b=a9885934a3fe3b01190d7551af420e5a&ct=1425729029&et=-1;right=web4=n&portal=n;temp4=rm=;;JSESSIONID=abcemsq-5Dv6PbydOe0Vu;;__u_a=v2.2.7;__cid=1;__ptime=1425728941392;";
		// headerMap.put("Cookie", cookie);

		requestPojo.setHeaderMap(headerMap);
		requestPojo.setUrl(url);
		// requestPojo.setHeaderMap(headerMap);
		// requestPojo.setParasMap(parasMap);
		// form name value是为非iso-8859-1编码的value pair而添加
		requestPojo.setFormNameValePairMap(formNameValueMap, CharsetEnum.UTF8);

		String source = null;
		for (int i = 0; i < 10; i++) {
			source = Crawl4HttpClient.crawlWebPage(requestPojo);
		}

		System.out.println(source);

		System.out.println("done!");

	}
}
