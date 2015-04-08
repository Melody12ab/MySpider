package com.tianliang.spider.tianya.login;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;

import com.tianliang.spider.crawler.httpclient.Crawl4HttpClient;
import com.tianliang.spider.pojos.HttpRequestPojo;
import com.tianliang.spider.pojos.enumeration.CharsetEnum;
import com.tianliang.spider.pojos.enumeration.HttpRequestMethod;
import com.tianliang.spider.utils.DateUtil;
import com.tianliang.spider.utils.RegexPaserUtil;

/**
 * 天涯论谈模拟登陆
 * 
 * @author zel
 * 
 */
public class LoginTianYaManager {
	public static String getJSessionId() {
		HttpRequestPojo requestPojo = new HttpRequestPojo();
		requestPojo.setRequestMethod(HttpRequestMethod.GET);

		 String url ="http://passport.tianya.cn/online/checkuseronline.jsp?callback=online&rd=0.8600873896743535";
//		String url = "http://passport.tianya.cn/online/checkuseronline.jsp?callback=onlineTips&t=1428492410231";

		requestPojo.setUrl(url);

		// String source = Crawl4HttpClient.crawlWebPage(requestPojo);
		CloseableHttpResponse response = Crawl4HttpClient
				.getResponse(requestPojo);
		Header[] headerArray = response.getAllHeaders();
		String temp = null;
		String jSessionId = null;
		for (int i = 0; i < headerArray.length; i++) {
			// System.out.println("首页的预置信息--header[" + i + "]------" + h[i]);
			if (headerArray[i].getName().contains("Set-Cookie")) {
				temp = headerArray[i].getValue();
				// System.out.println(temp);
				if (temp.contains("JSESSIONID")) {
					String[] keyArray = temp.split("\\s");
					if (keyArray.length == 2) {
						jSessionId = keyArray[0];
						break;
					}
				}
			}
		}
		return jSessionId;
	}

	public static String getLoginSuccessUrl(String cookies) {
		HttpRequestPojo requestPojo = new HttpRequestPojo();
		requestPojo.setRequestMethod(HttpRequestMethod.POST);

		String url = "https://passport.tianya.cn/login?from=index&_goto=login";

		Map<String, Object> headerMap = new HashMap<String, Object>();
		Map<String, String> formNameValueMap = new HashMap<String, String>();

		// 设置header 参数
		headerMap
				.put("Accept",
						"image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/QVOD, application/QVOD, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
		headerMap.put("Accept-Language", "zh-CN");
		headerMap.put("Content-Type", "application/x-www-form-urlencoded");
		headerMap.put("Host", "passport.tianya.cn");
		headerMap.put("Referer", "http://www.tianya.cn/");
		headerMap
				.put("User-Agent",
						"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; InfoPath.3)");

		headerMap.put("Cookie", cookies);

		// 设置post表单参数
		formNameValueMap.put("vwriter", "findfunny");
		formNameValueMap.put("vpassword", "a1a2a3");
		formNameValueMap.put("method", "name");

		requestPojo.setUrl(url);

		requestPojo.setHeaderMap(headerMap);
		requestPojo.setFormNameValePairMap(formNameValueMap, CharsetEnum.UTF8);

		String source = Crawl4HttpClient.crawlWebPage(requestPojo);

		// 得到login success url as a referer for next step
		RegexPaserUtil regexParserUtil = new RegexPaserUtil(
				"location\\.href=\"", "\"", "[\\S\\s]*");
		regexParserUtil.reset(source);

		String login_success_url = regexParserUtil.getText();

		// System.out.println(login_success_url);
		return login_success_url;
	}

	public static String login_successful_redirect_url = null;

	// 要跳转到的几个url中
	public static List<String> getSendUrlList(String cookies) {
		HttpRequestPojo requestPojo = new HttpRequestPojo();
		requestPojo.setRequestMethod(HttpRequestMethod.GET);

		login_successful_redirect_url = getLoginSuccessUrl(cookies);

		Map<String, Object> headerMap = new HashMap<String, Object>();
		Map<String, String> formNameValueMap = new HashMap<String, String>();

		// 设置header 参数
		headerMap
				.put("Accept",
						"image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/QVOD, application/QVOD, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
		headerMap.put("Accept-Language", "zh-CN");
		headerMap.put("Content-Type", "application/x-www-form-urlencoded");
		headerMap.put("Host", "passport.tianya.cn");
		headerMap
				.put("User-Agent",
						"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; InfoPath.3)");
		headerMap.put("Cookie", cookies);

		requestPojo.setUrl(login_successful_redirect_url);

		requestPojo.setHeaderMap(headerMap);
		requestPojo.setFormNameValePairMap(formNameValueMap, CharsetEnum.UTF8);

		String source = Crawl4HttpClient.crawlWebPage(requestPojo);

		// 得到login success url as a referer for next step
		RegexPaserUtil regexParserUtil = new RegexPaserUtil("src=\"", "\"",
				"([\\S\\s]*?)");
		regexParserUtil.reset(source);

		List<String> reusltUrlList = new LinkedList<String>();
		while (regexParserUtil.getMatcher().find()) {
			reusltUrlList.add(regexParserUtil.getMatcher().group(1));
		}
		return reusltUrlList;
	}

	/**
	 * 获取cookies的主入口类
	 * 
	 */
	public static String getCookies() {
		String cookiesString = getJSessionId();
		HttpRequestPojo requestPojo = new HttpRequestPojo();
		requestPojo.setRequestMethod(HttpRequestMethod.GET);

		// 设置cookies参数
		StringBuilder cookiesBuilder = new StringBuilder();
		cookiesBuilder.append(cookiesString + ";");

		List<String> reusltUrlList = getSendUrlList(cookiesBuilder.toString());

		Map<String, Object> headerMap = new HashMap<String, Object>();
		Map<String, String> formNameValueMap = new HashMap<String, String>();

		// 设置header 参数
		headerMap.put("Accept", "*/*");
		headerMap.put("Accept-Language", "zh-CN");
		headerMap.put("Host", "passport.tianya.cn");
		headerMap.put("Connection", "Keep-Alive");
		headerMap
				.put("User-Agent",
						"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; InfoPath.3)");

		cookiesBuilder.append("__u_a=v2.2.7;");
		cookiesBuilder.append("__cid=1;");
		cookiesBuilder.append("__ptime=" + DateUtil.getLongByDate() + ";");
		headerMap.put("Referer", login_successful_redirect_url);
		headerMap.put("Cookie", cookiesBuilder.toString());

		String url = reusltUrlList.get(0);
		requestPojo.setUrl(url);

		requestPojo.setHeaderMap(headerMap);

		CloseableHttpResponse response = Crawl4HttpClient
				.getResponse(requestPojo);

		Header[] h = response.getAllHeaders();
		String temp = null;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < h.length; i++) {
			// System.out.println("首页的预置信息--header[" + i + "]------" + h[i]);
			if (h[i].getName().contains("Set-Cookie")) {
				temp = h[i].getValue();
				// String[] keyArray = temp.split("\\s");
				// System.out.println(keyArray[0]);
				String key = temp.split(";")[0];
				sb.append(key + ";");
				// System.out.println(key);
				// System.out.println(temp);
			}
		}
		sb.append(";" + cookiesBuilder.toString());

		try {
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	public static String checkUserOnLine(String cookies) {
		HttpRequestPojo requestPojo = new HttpRequestPojo();
		requestPojo.setRequestMethod(HttpRequestMethod.GET);

		Map<String, Object> headerMap = new HashMap<String, Object>();

		// 设置header 参数
		headerMap.put("Accept", "*/*");
		headerMap.put("Accept-Language", "zh-CN");
		headerMap.put("Host", "passport.tianya.cn");
		headerMap.put("Connection", "Keep-Alive");
		headerMap
				.put("User-Agent",
						"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; InfoPath.3)");

		headerMap.put("Referer", login_successful_redirect_url);
		headerMap.put("Cookie", cookies);

		// check url
		String check_url = "http://passport.tianya.cn/online/checkuseronline.jsp?callback=onlineTips&t="
				+ DateUtil.getLongByDate();
		requestPojo.setUrl(check_url);

		String source = Crawl4HttpClient.crawlWebPage(requestPojo);

		return source;
	}

	public static void main(String[] args) throws Exception {
		// String cookies = getCookies();
		// System.out.println("cookies---" + cookies);
		// // 验证用户是否已登录在线
		// String check_source = checkUserOnLine(cookies);
		// System.out.println(check_source);

		// 验证部分
		HttpRequestPojo requestPojo = new HttpRequestPojo();
		String cookie_manual = "sso=r=952644062&sid=&wsid=5E5FAB4D9379BD30BBFD733AFF9220C6;user=w=findfunny&id=96654221&f=1;temp=k=634563547&s=&t=1425739486&b=6759d074c2483cd64582e528e85257cb&ct=1425739486&et=-1;right=web4=n&portal=n;temp4=rm=;;JSESSIONID=abczxAjxLYgq6em8GS0Vu;;__u_a=v2.2.7;__cid=1;__ptime=1425739397338;";
		Map<String, Object> headerMap = new HashMap<String, Object>();
		String url = "http://www.tianya.cn/96654221";
		requestPojo.setUrl(url);
		headerMap.put("Host", "www.tianya.cn");
		headerMap.put("Cookie", cookie_manual);

		requestPojo.setHeaderMap(headerMap);

		String source = Crawl4HttpClient.crawlWebPage(requestPojo);

		// String jid = getJSessionId();
		// System.out.println(jid);
		// String source = Crawl4HttpClient.parserResponse_v2(response);
		System.out.println(source);
	}
}
