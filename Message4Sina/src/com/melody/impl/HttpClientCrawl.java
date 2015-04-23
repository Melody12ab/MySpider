package com.melody.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import com.melody.sinacookie.Cookie;
import com.melody.utils.URLencode;
import com.melody.utils.UnicodeUtils;
import com.weibo.zel.utils.sina.MyHttpConnectionManager;

public class HttpClientCrawl {

	String source = null;
	HttpClient client = MyHttpConnectionManager.getNewHttpClient();
	HttpGet hg = null;
	String cookie = null;
	HttpResponse response = null;
	StringBuilder sb = new StringBuilder();
	StringBuilder sb1 = new StringBuilder();
	BufferedReader br = null;

	Pattern pattern = Pattern.compile("\"html\":\"(.+)\"");

	public String crawlOnePage(String name, int num) {
		String url = "http://s.weibo.com/user/&tag=" + URLencode.encode(name)
				+ "&Refer=g&page=" + num;
		hg = new HttpGet(url);
//		System.out.println(url);
		cookie = Cookie.getCookie();
		hg.setHeader("Cookie", cookie);
		String tmp = null;
		try {
			response = client.execute(hg);
			br = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent(), "UTF-8"));
			while ((tmp = br.readLine()) != null) {
				sb.append(tmp + "\n");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		String source = sb.toString();
		Matcher matcher = pattern.matcher(source);
		while (matcher.find()) {
			sb1.append(matcher.group(1) + "\n");
		}
		return UnicodeUtils.decodeUnicode(sb1.toString());
	}

	public static void main(String[] args) {
		System.out.println(new HttpClientCrawl().crawlOnePage("县长", 2));
	}

}
